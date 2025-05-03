package com.badlogic.angrybirdgame;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Ground {
    public Body body;

    public Ground(float x, float y, float width, float height, World world) {
        // Body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;  // Static ground (doesn't move)
        bodyDef.position.set(x, y);

        // Create body in the world
        body = world.createBody(bodyDef);

        // Define the shape (rectangle for the ground)
        PolygonShape groundShape = new PolygonShape();
        groundShape.setAsBox(width / 2, height / 2); // Set dimensions (half-width, half-height)

        // Define the fixture properties
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = groundShape;
        fixtureDef.friction = 0.5f;  // High friction to prevent objects from sliding off
        fixtureDef.restitution = 0.0f;  // No bounce on ground

        // Attach the fixture to the body
        body.createFixture(fixtureDef);
        groundShape.dispose();
    }

    public void dispose() {
        // Dispose of the body when done
        body.getWorld().destroyBody(body);
    }
}
