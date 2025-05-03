package com.badlogic.angrybirdgame;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class PauseScreen implements Screen {
	private final Main game;
	private final GameScreen gameScreen;
    
    private Texture blackTexture;
    private Texture level;
    private Texture font;
    private Texture restartTexture;
    private Texture resumeTexture;
    private Texture menuTexture;
    private Texture restart1;
    private Texture resume1;
    private Texture menu1;
    private Texture birdsTexture;
    
    private Circle restartCircle;
    private Circle menuCircle;
    private Circle resumeCircle;

    private Stage stage;
    private boolean isTransitioning = false;
    
    public PauseScreen(Main game, GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;
        show();
    }


    @Override
    public void show() {
        // Initialize resources here if needed
    	switch (game.curLevel) {
    	case 1:
    		level = new Texture("bg/tropical-forest.png");
    		font = new Texture("bg/tropical-forest-font.png");
    		break;
    	case 2:
    		level = new Texture("bg/frozen-mountain.png");
    		font = new Texture("bg/frozen-font.png");
    		break;
    	case 3:
    		level = new Texture("bg/space.png");
    		font = new Texture("bg/asteroid-font.png");
    		break;
    	}
    	
       
        blackTexture = new Texture("bg/black-screen.jpg");
        restartTexture = new Texture("bt/restart.png");
        resumeTexture = new Texture("bt/play.png");
        menuTexture = new Texture("bt/menu.png");
        restart1 = new Texture ("bt/restart1.png");
        resume1 = new Texture("bt/play1.png");
        menu1 = new Texture("bt/menu1.png");
        birdsTexture = new Texture("bg/bird-gang.png");
        
        restartCircle = new Circle();
        menuCircle = new Circle();
        resumeCircle = new Circle();

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
    	
    	if (isTransitioning) {
    		stage.act(delta);
            stage.draw();
    	}
    	// Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        game.camera.unproject(touchPos);
        game.spriteBatch.setProjectionMatrix(game.camera.combined);
        
        if (!isTransitioning) {
        	// Draw the menu
            game.spriteBatch.begin();
            
            game.spriteBatch.draw(level, 0, 0, level.getWidth(), level.getHeight());
            
            
            game.spriteBatch.setColor(1, 1, 1, 0.5f);
            game.spriteBatch.draw(blackTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            game.spriteBatch.setColor(1, 1, 1, 1f);
            
            game.spriteBatch.draw(blackTexture, -700, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            game.spriteBatch.draw(font, 20, 450, font.getWidth() * 0.7f, font.getHeight() * 0.7f);
            game.spriteBatch.draw(birdsTexture, 350, 120, birdsTexture.getWidth(), birdsTexture.getHeight());

            resumeCircle.set(100 + 30, 300 + 30, 30);
            restartCircle.set(160 + 30, 230 + 30, 30);
            menuCircle.set(100 + 30, 160 + 30, 30);

            if (menuCircle.contains(touchPos.x, touchPos.y)) {
                game.spriteBatch.draw(menuTexture, 100, 160, 70, 70); 
            } else {
            	game.spriteBatch.draw(menu1, 100, 160, 70, 70); 
            }
            
            if (resumeCircle.contains(touchPos.x, touchPos.y)) {
                game.spriteBatch.draw(resumeTexture, 100, 300, 70, 70); 
            } else {
            	game.spriteBatch.draw(resume1, 100, 300, 70, 70); 
            }
            
            if (restartCircle.contains(touchPos.x, touchPos.y)) {
                game.spriteBatch.draw(restartTexture, 160, 230, 70, 70); 
            } else {
            	game.spriteBatch.draw(restart1, 160, 230, 70, 70); 
            }
            
            game.spriteBatch.end();
        }
        

        // Handle input
        if (Gdx.input.justTouched() && !isTransitioning) {
            if (restartCircle.contains(touchPos.x, touchPos.y)) {
            	game.click.play(0.2f);
            	gameScreen.deleteGameState();
            	switch (game.curLevel) {
            	case 1:
            		game.setScreen(new GameScreen1(game));
            		break;
            	case 2:
            		game.setScreen(new GameScreen2(game));
            		break;
            	case 3:
            		game.setScreen(new GameScreen3(game));
            		break;
            	}
                dispose(); 
            }
            
            if (menuCircle.contains(touchPos.x, touchPos.y )) {
            	game.click.play(0.2f);
            	gameScreen.saveGameState();
            	switch (game.curLevel) {
            	case 1:
            		startTransitionTo(new MenuScreen(game), new Texture(Gdx.files.internal("bg/tropical-forest.png")));
            		break;
            	case 2:
            		startTransitionTo(new MenuScreen(game), new Texture(Gdx.files.internal("bg/frozen-mountain.png")));
            		break;
            	case 3:
            		startTransitionTo(new MenuScreen(game), new Texture(Gdx.files.internal("bg/space.png")));
            		break;
            	}
                dispose(); 
            }
            
            if (resumeCircle.contains(touchPos.x, touchPos.y )) {
            	game.click.play(0.2f);

        		gameScreen.isPaused = false;
        		
        		game.setScreen(gameScreen);

                dispose(); 
            }
        }
        
        stage.act(delta);
        stage.draw();
    }
    
    public void startTransitionTo(Screen nextScreen, Texture fadeTexture) {
        isTransitioning = true;

        Image fadeOverlay = new Image(fadeTexture);
        fadeOverlay.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        fadeOverlay.getColor().a = 1f; // Set opacity to fully opaque

        stage.addActor(fadeOverlay);

        // Fade out the overlay and switch screens
        fadeOverlay.addAction(Actions.sequence(
            Actions.fadeOut(1f, Interpolation.fade), // Duration of fade-out (1 second)
            Actions.run(() -> {
                game.setScreen(nextScreen); // Transition to the next screen
                dispose(); // Clean up current screen resources
            })
        ));
    }

    @Override
    public void resize(int width, int height) {
        // Handle resizing if necessary
    }

    @Override
    public void pause() {
        // Handle pause if necessary
    }

    @Override
    public void resume() {
        // Handle resume if necessary
    }

    @Override
    public void hide() {
        // Called when this screen is no longer the current screen
    }

    @Override
    public void dispose() {
    	level.dispose();
        font.dispose();
        blackTexture.dispose();
        restartTexture.dispose();
        resumeTexture.dispose();
        menuTexture.dispose();

    }
}