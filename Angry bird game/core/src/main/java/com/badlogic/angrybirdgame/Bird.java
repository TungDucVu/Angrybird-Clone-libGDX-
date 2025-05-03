package com.badlogic.angrybirdgame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Bird {
    public Body body;
    private int life;
    public int maxLife;
    public Texture texture;
    public TextureRegion region;
    private float density;
    float radius;
    float maxPower;
    public String type;
    
    public FixtureDef fixtureDef;
    
    
    public boolean isFlying = false;
    public boolean isReady = false;
    public boolean isDead = false;
    
    private static final float MAXRED = 10f;
    private static final float MAXBIG = 40f;
    private static final float MAXYELLOW = 15f;
    
    private static final float REDDENS = 8f;
    private static final float BIGDENS = 8f;
    private static final float YELLOWDENS = 8f;
    
    private static final float REDRADIUS = 0.2f;
    private static final float BIGRADIUS = 0.4f;
    private static final float YELLOWRADIUS = 0.2f;
    
    private static final int REDLIFE = 15;
    private static final int BIGLIFE = 17;
    private static final int YELLOWLIFE = 15;
    
    private void identify(String type) {
    	switch (type) {
    		case "red":
    			texture = new Texture("skin/red.png");
    			region = new TextureRegion(texture);
    			life = REDLIFE;
    			maxLife = REDLIFE;
    			density = REDDENS;
    			radius = REDRADIUS;
    			maxPower = MAXRED;
    			break;
    		case "big":
    			texture = new Texture("skin/big.png");
    			region = new TextureRegion(texture);
    			life = BIGLIFE;
    			maxLife = BIGLIFE;
    			density = BIGDENS;
    			radius = BIGRADIUS;
    			maxPower = MAXBIG;
    			break;
    		case "yellow":
    			texture = new Texture("skin/yellow.png");
    			region = new TextureRegion(texture);
    			life = YELLOWLIFE;
    			maxLife = YELLOWLIFE;
    			density = YELLOWDENS;
    			radius = YELLOWRADIUS;
    			maxPower = MAXYELLOW;
    			break;
    	}
    }
    
    public boolean isMoving() {
        // Check the body's velocity to determine if the bird is moving
    	if (!isDead) {
    		return body.getLinearVelocity().len() > 0.1f;
    	}
        return false;
    }
        
    private void updateTexture() {
        if (life <= maxLife / 2) {
            // Dispose of the old texture if it exists
            if (texture != null) {
                texture.dispose();
            } 

            switch (type) {
                case "red":
                    texture = new Texture("skin/dead-red.png");
                    break;
                case "big":
                    texture = new Texture("skin/dead-big.png");
                    break;
                case "yellow":
                    texture = new Texture("skin/dead-yellow.png");
                    break;
            }
           
        }
        
        if (isDead) { 
        	texture = new Texture("skin/birdDissapear.png");
        }
        
        
        region = new TextureRegion(texture);  

    }


    public Bird(float x, float y, World world, String type) {
    	this.type = type;
    	identify(type);
        // Body definition
    	BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);

        // Create body in the world
        this.body = world.createBody(bodyDef);
        
     // Define shape
        CircleShape shape = new CircleShape();
        shape.setRadius(radius);  // Scale the radius as needed

        // Define fixture properties
        fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = density;
        fixtureDef.friction = 0.6f;
        fixtureDef.restitution = 0.7f;  // Elasticity
        fixtureDef.isSensor = false;
        //System.out.println("Fixture created for bird at position: " + body.getPosition());

        body.setAngularDamping(2.0f);

        // Create fixture on body
        body.createFixture(fixtureDef);
        
        shape.dispose();
        
        freezeBird();

    }
    
    
    
    public void freezeBird() {
        // Set velocity to zero and make sure the bird is not affected by gravity
        body.setLinearVelocity(0, 0);  // Freeze the bird's motion
        body.setAngularVelocity(0);    // Prevent any rotation
        body.setGravityScale(0);       // Disable gravity
        body.getFixtureList().first().setSensor(true);
    }
    
    public void unfreezeBird() {
        // Allow the bird to be affected by gravity again
        body.setGravityScale(1);  // Enable gravity
        isReady = true;
        body.getFixtureList().first().setSensor(false);
    }
    
    public void draw(SpriteBatch batch) {
    	// Get the position and angle of the column body
        Vector2 position = body.getPosition();
        float angle = body.getAngle();

        // The center of the texture for rotation
        float originX = radius; // Half of the width
        float originY = radius; // Half of the height
        
        updateTexture();
        
        
        // Draw the texture rotated by the body's angle
        batch.draw(region,
                position.x - originX, position.y - originY, // Bottom-left corner offset by origin
                originX, originY, // Origin for rotation (center of texture)
                2 * radius, 2 * radius, // Width and Height of the texture (scaled)
                1f, 1f, // Scale the texture (no scaling here)
                angle * MathUtils.radiansToDegrees // Rotation (convert radians to degrees)
        );

        if (life <= 0) {
        	isDead = true;
        	isReady = false;
        	body.getFixtureList().first().setSensor(true);
        	
        } 
        
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }
            
    public int calculateDamage(float distance, float relativeSpeed) {
        
        float damage = relativeSpeed / (distance + 1);  // Adding 1 to avoid division by zero
        
        return (int) damage;
    }

    public void dispose() {
    	body.getWorld().destroyBody(body);
    	texture.dispose();
    }
    
    public void onHit(int damage) {
    	if (isDead) {
            return;
        }
        life -= damage;

    }
    
    public void relocate(float newX, float newY) {
        // Set the new position of the bird's body
        body.setTransform(newX, newY, body.getAngle()); // Keep the same angle

        // Reset the bird's state
        isDead = false; // Reset dead state
        isReady = false; // Reset ready state
        isFlying = false; // Reset flying state

        // Freeze the bird after relocation to prevent immediate movement
        freezeBird();
    }
    
    
}
