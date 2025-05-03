package com.badlogic.angrybirdgame;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Structure {
    public String material, type;
    private int life;
    private int maxLife;
    public Body body;
    private Texture texture;
    private TextureRegion region;
    private float longSide;
    private float shortSide;
    private float density;
    public float angle;

    public boolean isDead = false;

    private static final float FRICTION = 0.6f;
    private static final float RESTITUTION = 0.2f;

    private static final float SHORT = 1.4f;
    private static final float MED = 2.8f;
    private static final float LONG = 3.4f;
    private static final float SHORTER = 0.3f;

    private static final float WOODDEN = 20f;
    private static final float STONEDEN = 40f;
    private static final float GLASSDEN = 8f;

    private static final int WOODLIFE = 10;
    private static final int STONELIFE = 15;
    private static final int GLASSLIFE = 6;
    private FixtureDef fixtureDef;

    public Structure(World world, Vector2 position, String type, String material, float angle) {
        this.material = material;
        this.type = type;
        this.angle = angle;

        identify(type, material);

        buildBody(density, FRICTION, RESTITUTION, position, world, angle);
    }

    private void identify(String type, String material) {
        switch (material) {
            case "wood":
                this.life = WOODLIFE;
                this.maxLife = WOODLIFE;
                this.density = WOODDEN;
                this.texture = new Texture("struct/w1.png");
                this.region = new TextureRegion(texture);
                switch (type) {
                    case "long":
                        this.longSide = LONG;
                        this.shortSide = SHORTER;
                        break;
                    case "med":
                        this.longSide = MED;
                        this.shortSide = SHORTER;
                        break;
                    case "short":
                        this.longSide = SHORT;
                        this.shortSide = SHORTER;
                        break;
                }
                break;

            case "stone":
                this.life = STONELIFE;
                this.maxLife = STONELIFE;
                this.density = STONEDEN;
                this.texture = new Texture("struct/s1.png");
                this.region = new TextureRegion(texture);
                switch (type) {
                    case "long":
                        this.longSide = LONG;
                        this.shortSide = SHORTER;
                        break;
                    case "med":
                        this.longSide = MED;
                        this.shortSide = SHORTER;
                        break;
                    case "short":
                        this.longSide = SHORT;
                        this.shortSide = SHORTER;
                        break;
                }
                break;

            case "ice":
                this.life = GLASSLIFE;
                this.maxLife = GLASSLIFE;
                this.density = GLASSDEN;
                this.texture = new Texture("struct/g1.png");
                this.region = new TextureRegion(texture);
                switch (type) {
                    case "long":
                        this.longSide = LONG;
                        this.shortSide = SHORTER;
                        break;
                    case "med":
                        this.longSide = MED;
                        this.shortSide = SHORTER;
                        break;
                    case "short":
                        this.longSide = SHORT;
                        this.shortSide = SHORTER;
                        break;
                }
                break;
        }
    }

    private void updateTexture() {
        if (life <= maxLife / 1.5) {
            if (texture != null) {
                texture.dispose();
            }

            switch (material) {
                case "wood":
                    if (life <= maxLife / 3) {
                        texture = new Texture("struct/w3.png");
                    } else if (life <= maxLife / 1.5) {
                        texture = new Texture("struct/w2.png");
                    }
                    break;
                case "stone":
                    if (life <= maxLife / 3) {
                        texture = new Texture("struct/s3.png");
                    } else if (life <= maxLife / 1.5) {
                        texture = new Texture("struct/s2.png");
                    }
                    break;
                case "ice":
                    if (life <= maxLife / 3) {
                        texture = new Texture("struct/g3.png");
                    } else if (life <= maxLife / 1.5) {
                        texture = new Texture("struct/g2.png");
                    }

                    break;
            }

            region = new TextureRegion(texture);
        }
    }

    public void buildBody(float density, float friction, float restitution, Vector2 position, World world, float angle) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);
        bodyDef.angle = angle;

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(longSide / 2, shortSide / 2);

        fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = density;
        fixtureDef.friction = friction;
        fixtureDef.restitution = restitution;

        body.createFixture(fixtureDef);
        shape.dispose();
    }

    public void draw(SpriteBatch batch) {
        if (isDead) return;

        Vector2 position = body.getPosition();
        float angle = body.getAngle();

        float originX = longSide / 2;
        float originY = shortSide / 2;

        updateTexture();

        batch.draw(region,
                position.x - originX, position.y - originY,
                originX, originY,
                longSide, shortSide,
                1f, 1f,
                angle * MathUtils.radiansToDegrees
        );
    }


    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int calculateDamage(float distance, float relativeSpeed) {
        float damage = relativeSpeed / (distance + 1);
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

        //System.out.println("structure: " + life);
        
        if (life <= 0) {
        	isDead = true;
        	body.getFixtureList().first().setSensor(true);
        	
        	
        }
    }
    
}
