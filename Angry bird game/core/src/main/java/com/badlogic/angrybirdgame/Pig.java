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

public class Pig {
    public Body body;
    private int life;
    public int maxLife;
    private Texture texture;
    private TextureRegion region;
    private float density;
    private float radius;
    public String type;
    
    public boolean isDead = false;
        
    private static final float MEDDENS = 8f;
    private static final float BIGDENS = 8f;
    private static final float SMALLDENS = 8f;
    
    private static final float MEDRADIUS = 0.3f;
    private static final float BIGRADIUS = 0.4f;
    private static final float SMALLRADIUS = 0.2f;
    
    private static final int MEDLIFE = 10;
    private static final int BIGLIFE = 15;
    private static final int SMALLLIFE = 8;
    
    private FixtureDef fixtureDef;
    
    private void identify(String type) {
    	switch (type) {
		case "med":
			texture = new Texture("skin/med.png");
			region = new TextureRegion(texture);
			life = MEDLIFE;
			maxLife = MEDLIFE;
			density = MEDDENS;
			radius = MEDRADIUS;
			
			break;
		case "big":
			texture = new Texture("skin/king.png");
			region = new TextureRegion(texture);
			life = BIGLIFE;
			maxLife = BIGLIFE;
			density = BIGDENS;
			radius = BIGRADIUS;
			
			break;
		case "small":
			texture = new Texture("skin/small.png");
			region = new TextureRegion(texture);
			life = SMALLLIFE;
			maxLife = SMALLLIFE;
			density = SMALLDENS;
			radius = SMALLRADIUS;
			
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
    		
    		if (texture != null) {
                texture.dispose();
            }
    		
    		switch(type) {
    			case "med":
    				texture = new Texture("skin/dead-med.png");
    				break;
    			case "big":
    				if (life <= maxLife / 4) {
    					texture = new Texture("skin/dead2-king.png");
    				} else if (life <= maxLife / 2) {
    					texture = new Texture("skin/dead1-king.png");
    				}
    				break;
    			case "small":
    				texture = new Texture("skin/dead-small.png");
    				break;
    		}
    		
    		
    	}
    	
    	if (isDead) { 
        	texture = new Texture("skin/pigDissapear.png");
        }
    	
    	region = new TextureRegion(texture); 
    	
    }

    public Pig(float x, float y, World world, String type) {
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
        //fixtureDef.isSensor = false;
        //System.out.println("Fixture created for pig at position: " + body.getPosition());
        
        body.setAngularDamping(2.0f);

        // Create fixture on body
        body.createFixture(fixtureDef);
        
        shape.dispose();
    }
    
    public void draw(SpriteBatch batch) {
        
        // Get the position and angle of the column body
        Vector2 position = body.getPosition();
        float angle = body.getAngle();

        // The center of the texture for rotation
        float originX = radius; // Half of the width
        float originY = radius; // Half of the height
        
        float Yscale = 1f;
        if (type == "big") {
        	Yscale = 1.3f;
        	originY = radius - 0.1f;
        }
        
        updateTexture();

        // Draw the texture rotated by the body's angle
        batch.draw(region,
                position.x - originX, position.y - originY, // Bottom-left corner offset by origin
                originX, originY, // Origin for rotation (center of texture)
                2 * radius, 2 * radius, // Width and Height of the texture (scaled)
                1f, Yscale, // Scale the texture (no scaling here)
                angle * MathUtils.radiansToDegrees // Rotation (convert radians to degrees)
        );
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
        // Reduce life when hit
        life -= damage;

        //System.out.println("pig: " + life);
        
        if (life <= 0) {
        	
        	isDead = true;
        	body.getFixtureList().first().setSensor(true);
        	
        	
        }
    }
    
}
