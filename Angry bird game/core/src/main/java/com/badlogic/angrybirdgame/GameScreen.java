package com.badlogic.angrybirdgame;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.badlogic.gdx.utils.Json;

public class GameScreen implements Screen{
	public GameScreen currentGameScreen;
	
	public final Main game;
    
    public World world;
    public ArrayList<Bird> birds;
    public ArrayList<Pig> pigs;
    public ArrayList<Structure> structures;
    
    public Bird curBird;    
    
    private Box2DDebugRenderer debugRenderer;
    
    public Ground ground;
    private Ground wall, start;
    public OrthographicCamera camera;

    private Vector2 initialTouch, releaseTouch, initialPosition;
    private boolean isDragging;
    
    public Texture bg, slingpart, slingshot;
    public float partx, party, shotx, shoty, inix, iniy, birdx, birdy;
    public int i = 0;
    private boolean isWin = false;
    private boolean isLose = false;

    public boolean isPaused = false;    
    
    public void populating() {};
    
    
    public GameScreen(Main game) {
        this.game = game;

        initializeGame();
        
        GameState oldGame = loadGameState();

        if (oldGame != null) { 	
        	restore(oldGame);
        }
        else {
        	deleteGameState();
        	populating();
        }

    	
    }
    
    private void initializeGame() {

        debugRenderer = new Box2DDebugRenderer();
        world = new World(new Vector2(0, -9.8f), true);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 16, 9); 

        birds = new ArrayList<>();
    	pigs = new ArrayList<>();
    	structures = new ArrayList<>();
 
        slingpart = new Texture("struct/slingpart.png");
        slingshot = new Texture("struct/slingshot.png");
        
        wall = new Ground(16.5f, 0, 0, 18, world);
        start = new Ground(0, 0, 0 , 18, world);
        
        soundManage(game);
        
    }
    
    
    public class GameContactListener implements ContactListener {
    	@Override
        public void beginContact(Contact contact) {
        	handleCollision(contact, "begin");

        }

        @Override
        public void endContact(Contact contact) {
        	handleCollision(contact, "end");
        	
        }

		@Override
		public void preSolve(Contact contact, Manifold oldManifold) {
	        
	    }

		@Override
		public void postSolve(Contact contact, ContactImpulse impulse) {}
    }
    
    private class GameInputAdapter extends InputAdapter {
    	private static final float MAX_PULL_DISTANCE = 1f; // Set maximum pull distance in world units

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            // Convert screen coordinates to world coordinates
        	if (curBird.isReady && !curBird.isFlying) {
        		Vector2 birdPosition = new Vector2(birdx, birdy);
        		initialPosition = new Vector2(screenX, Gdx.graphics.getHeight() - screenY).scl(1 / 100f);
//        		System.out.println(initialPosition.x + " " + initialPosition.y);
        		if (birdPosition.dst(initialPosition) <= curBird.radius) {
        			initialTouch = initialPosition;
        		}
        		
                isDragging = true;
                return true;
        	}
        	
        	return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            if (isDragging) {
                // Convert current drag coordinates to world coordinates
                releaseTouch = new Vector2(screenX, Gdx.graphics.getHeight() - screenY).scl(1 / 100f);
                
                if (!(initialTouch == null)) {
                	// Calculate the drag vector based on initial and current drag positions
                    Vector2 dragVector = initialTouch.cpy().sub(releaseTouch);

                    // Cap the drag vector length to the fixed maximum pull distance
                    if (dragVector.len() > MAX_PULL_DISTANCE) {
                        dragVector.setLength(MAX_PULL_DISTANCE);
                        
                    }
                    releaseTouch = initialTouch.cpy().sub(dragVector);
                    curBird.relocate(releaseTouch.x - curBird.radius*3/2 + 1.4f, releaseTouch.y - curBird.radius*3/2 + 1.85f);
                    return true;
                    
                }
                
            }
            return false;
            
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            if (isDragging) {
                // Convert release touch coordinates to world coordinates
                releaseTouch = new Vector2(screenX, Gdx.graphics.getHeight() - screenY).scl(1 / 100f);
                
                // Calculate drag vector (difference between initial touch and release touch)
                if (!(initialTouch == null)) {
                	Vector2 dragVector = initialTouch.cpy().sub(releaseTouch);

                    // Cap the drag vector length to the fixed maximum pull distance
                    if (dragVector.len() > MAX_PULL_DISTANCE) {
                        dragVector.setLength(MAX_PULL_DISTANCE);
                    }

                    // Calculate power as a proportion of the capped pull distance
                    float distance = dragVector.len();
                    float angle = dragVector.angleDeg();
                    float power = (distance / MAX_PULL_DISTANCE) * curBird.maxPower;
                    Vector2 impulse = new Vector2(power, 0).rotateDeg(angle);

                    // Unfreeze and launch the bird
                    curBird.unfreezeBird();
                    curBird.body.applyLinearImpulse(impulse, curBird.body.getWorldCenter(), true);
                    
                    isDragging = false;
                    curBird.isReady = false;
                    curBird.isFlying = true;
                    initialTouch = null;
                    game.release.play(0.3f);
                    return true;
                    
                }
                
            }
            
            return false;
        }
    }
    
    private void handleCollision(Contact contact, String phase) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        // Check for bird collisions
        if (isBirdCollision(fixtureA, fixtureB)) {
        	int damage = curBird.calculateDamage(calculateCollisionDistance(contact), calculateRelativeSpeed(contact));
        	game.hit.play(0.1f);
            curBird.onHit(damage);
            if (curBird.isDead) {
            	
            	//birds.remove(curBird);

            }
        }

        // Check for pig collisions
        if (isPigCollision(fixtureA, fixtureB)) {
            Pig pig = getPigInvolved(contact); // Get the pig involved in this collision
            if (pig != null) {
                int damage = pig.calculateDamage(calculateCollisionDistance(contact), calculateRelativeSpeed(contact));
                pig.onHit(damage);  // Apply damage to the specific pig
                if (pig.isDead) {
                	game.oink.play(1.5f);
                	//pigs.remove(pig);
                }
            }
        }

        // Check for structure collisions
        if (isStructureCollision(fixtureA, fixtureB)) {
            Structure structure = getStructureInvolved(contact); // Get the structure involved in this collision
            if (structure != null) {
                int damage = structure.calculateDamage(calculateCollisionDistance(contact), calculateRelativeSpeed(contact));
                structure.onHit(damage);  // Apply damage to the specific structure
                if (structure.isDead) {
                	switch (structure.material) {
            		case "wood":
            			game.wood.play(0.2f);
            			break;
            		case "stone":
            			game.stone.play(0.2f);
            			break;
            		case "ice":
            			game.ice.play(0.2f);
            			break;
            	}
                	//structures.remove(structure);
                }
                
            }
        }
    }

    private boolean isBirdCollision(Fixture fixtureA, Fixture fixtureB) {
    	if (fixtureA != null && fixtureB != null && !fixtureA.isSensor() && !fixtureB.isSensor()) {
    		return fixtureA.getBody().getUserData() instanceof Bird || fixtureB.getBody().getUserData() instanceof Bird;
    	}
    	return false;
        
    }

    private boolean isPigCollision(Fixture fixtureA, Fixture fixtureB) {
    	if (fixtureA != null && fixtureB != null && !fixtureA.isSensor() && !fixtureB.isSensor()) {
    		return fixtureA.getBody().getUserData() instanceof Pig || fixtureB.getBody().getUserData() instanceof Pig;
    	}
    	return false;
    }

    private boolean isStructureCollision(Fixture fixtureA, Fixture fixtureB) {
    	if (fixtureA != null && fixtureB != null && !fixtureA.isSensor() && !fixtureB.isSensor()) {
    		return fixtureA.getBody().getUserData() instanceof Structure || fixtureB.getBody().getUserData() instanceof Structure;
    	}
    	return false;
    }
    
    private Pig getPigInvolved(Contact contact) {
        for (Pig pig : pigs) {
            if (contact.getFixtureA().getBody() == pig.body || contact.getFixtureB().getBody() == pig.body) {
                return pig;  
            }
        }
        return null;  
    }


    private Structure getStructureInvolved(Contact contact) {
        for (Structure structure : structures) {
            if (contact.getFixtureA().getBody() == structure.body || contact.getFixtureB().getBody() == structure.body) {
                return structure;  
            }
        }
        return null; 
    }

    private float calculateCollisionDistance(Contact contact) {
        // Calculate distance based on the positions of the colliding bodies
        Vector2 posA = contact.getFixtureA().getBody().getPosition();
        Vector2 posB = contact.getFixtureB().getBody().getPosition();
        return posA.dst(posB); // Distance between two points
    }
    
    private float calculateRelativeSpeed(Contact contact) {
        // Get the collision point from the world manifold
        WorldManifold worldManifold = contact.getWorldManifold();
        Vector2 contactPoint = worldManifold.getPoints()[0]; // First contact point, may be multiple

        // Get the bodies involved in the collision
        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();

        // Get the velocity of each body at the contact point
        Vector2 velocityA = bodyA.getLinearVelocityFromWorldPoint(contactPoint);
        Vector2 velocityB = bodyB.getLinearVelocityFromWorldPoint(contactPoint);

        // Calculate the relative velocity vector
        Vector2 relativeVelocity = velocityA.sub(velocityB);

        // Return the magnitude of the relative velocity, which is the relative speed
        return relativeVelocity.len();
    }

    @Override
    public void render(float delta) {
    	Gdx.gl.glClearColor(0, 0, 0, 1);
    	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);	
    	
        // Check for pause input
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            pause(); // Toggle pause state
            
        }

        // If the game is paused, display the pause menu and return
        if (isPaused) {
        	
        	game.setScreen(new PauseScreen(game, currentGameScreen));
            return; // Exit early to skip game updates
        }

       
        if (!birds.isEmpty() && i < birds.size()) {
            curBird = birds.get(i);
            curBird.isReady = true;

            if (curBird.isDead) {
                i++;
                if (i < birds.size()) {
                    curBird = birds.get(i);
                    curBird.relocate(inix, iniy);
                }
            }
        }

        
        if (curBird != null) {
            sensorBody(world, curBird.body, curBird);
        }

        
        world.step(1 / 120f, 6, 2);

        
        camera.update();
        game.spriteBatch.setProjectionMatrix(camera.combined);

        
        game.spriteBatch.begin();
        
        
        game.spriteBatch.draw(bg, 0, 0, 16, 9);
        game.spriteBatch.draw(slingshot, shotx, shoty, 0.75f, 1.75f);
        
        for (Bird bird : birds) {
            bird.draw(game.spriteBatch);
        }
        for (Pig pig : pigs) {
            pig.draw(game.spriteBatch);
        }
        for (Structure structure : structures) {
            structure.draw(game.spriteBatch);
        }
        
        game.spriteBatch.draw(slingpart, partx, party, 0.6f, 0.8875f);
        

        game.spriteBatch.end();
        
        debugRenderer.render(world, camera.combined);
        
        checkWinOrLose();
    }
    
    
    @Override 
	public void show() {
    	Gdx.input.setInputProcessor(new GameInputAdapter());
    	world.setContactListener(new GameContactListener());
    }

    @Override
    public void resize(int width, int height) {}
    
    @Override
    public void pause() {
        isPaused = !isPaused; // Toggle the pause state
        saveGameState();
    } 
    	
    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
    	bg.dispose();
        for (Bird bird : birds) bird.dispose();
        for (Pig pig : pigs) pig.dispose();
        for (Structure structure : structures) structure.dispose();
        world.dispose();
        debugRenderer.dispose();
        ground.dispose();
        wall.dispose();
        start.dispose();
        slingpart.dispose();
        slingshot.dispose();
        
    }
    
    private float transitionDelay = 1.0f; // Duration to wait before transitioning
    private float transitionTimer = 0.0f; // Timer to track the delay
    private boolean transitionScheduled = false; // Flag to indicate if a transition is scheduled

    private void checkWinOrLose() {
        isWin = pigs.stream().allMatch(pig -> pig.isDead);
        isLose = birds.stream().allMatch(bird -> bird.isDead) && !isWin;

        // Check if all pigs are moving
        if (isLose && pigs.stream().allMatch(pig -> pig.isMoving())) {
            isLose = false; // If pigs are moving, reset lose condition
        }

        // If win or lose conditions are met and no transition is scheduled
        if (isWin || isLose) {
            if (!transitionScheduled) {
                transitionScheduled = true; // Schedule the transition
                transitionTimer = 0.0f; // Reset the timer
            }
        }

        // Handle the transition after the delay
        if (transitionScheduled) {
            transitionTimer += Gdx.graphics.getDeltaTime(); // Increment the timer

            if (transitionTimer >= transitionDelay) {
                if (isWin) {
                	game.win.play(0.3f);
                	deleteGameState();
                    game.setScreen(new LevelEndScreen(game));
                } else if (isLose) {
                	game.lose.play(0.3f);
                	deleteGameState();
                	game.setScreen(new LevelFailScreen(game));
                }
                transitionScheduled = false; 
            }
        }
    }

    public void sensorBody(World world, Body body, Bird bird) {
        if (body.getLinearVelocity().len2() < 0.01f) { 
        	body.getFixtureList().first().setSensor(true);
        	
        	if (bird.isFlying) {
        		bird.isDead = true;
        	}
        	
        }
    }
    
    // for UI
    public void soundManage(Main game) {
        
    }
    
    public void saveGameState() {
    	GameState gameState = new GameState();
        
        for (Bird bird : birds) {
            gameState.birdStates.add(new BirdState(bird));
        }
        
        for (Pig pig : pigs) {
            gameState.pigStates.add(new PigState(pig));
        }
        
        for (Structure structure : structures) {
            gameState.structureStates.add(new StructureState(structure));
        }
        
        gameState.currentBirdIndex = i; // Save the current bird index
        gameState.atLevel = game.atLevel;
        gameState.curLevel = game.curLevel;

        Json json = new Json();
        String jsonString = json.toJson(gameState);
        
        FileHandle file = Gdx.files.local("game_state.json");
        file.writeString(jsonString, false);
    }

    public GameState loadGameState() {
        FileHandle file = Gdx.files.local("game_state.json");
        if (file.exists()) {
            String jsonString = file.readString();
            Json json = new Json();
            GameState gameState = json.fromJson(GameState.class, jsonString);
            
            if (game.curLevel == gameState.curLevel) {
            	i = gameState.currentBirdIndex; // Restore the current bird index
                
                return gameState;
            }
        }
        return null;

    }
    
    public void deleteGameState() {
    	FileHandle file = Gdx.files.local("game_state.json");
    	if (file.exists()) {
    		file.deleteDirectory();
    	}
    }
    
    private void restore(GameState gameState) {
    	// Clear current game state
        birds.clear();
        pigs.clear();
        structures.clear();
        
        // Restore game state
        for (BirdState birdState : gameState.birdStates) {
            Bird bird = new Bird(birdState.x, birdState.y, world, birdState.type); // Create a new Bird instance
            
            bird.isDead = birdState.isDead;
            bird.isFlying = birdState.isFlying;
            bird.isReady = birdState.isReady;
            birds.add(bird);
        }
        
        for (PigState pigState : gameState.pigStates) {
            Pig pig = new Pig(pigState.x, pigState.y, world, pigState.type); // Create a new Pig instance

            pig.isDead = pigState.isDead;
            pig.type = pigState.type;
            pigs.add(pig);
        }
        
        for (StructureState structureState : gameState.structureStates) {
            Structure structure = new Structure(world, new Vector2(structureState.x, structureState.y), structureState.type, structureState.material, structureState.angle);

            structure.isDead = structureState.isDead;
            structure.type = structureState.type;
            structure.material = structureState.material;
            structure.angle = structureState.angle;
            
            structures.add(structure);
        }
        
        for (Bird bird : birds) {
            bird.body.setUserData(bird);
            
        }
        for (Pig pig : pigs) {
            pig.body.setUserData(pig);
            
        }
        for (Structure structure : structures) {
            structure.body.setUserData(structure);
            
        }
    }

}
