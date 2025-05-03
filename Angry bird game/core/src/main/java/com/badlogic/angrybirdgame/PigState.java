package com.badlogic.angrybirdgame;

import java.io.Serializable;

public class PigState implements Serializable {
    public float x, y;
    public boolean isDead;
    public String type;
    
    public PigState() {}

    public PigState(Pig pig) {
        this.x = pig.body.getPosition().x;
        this.y = pig.body.getPosition().y;
        this.isDead = pig.isDead;
        this.type = pig.type;
    }
}
