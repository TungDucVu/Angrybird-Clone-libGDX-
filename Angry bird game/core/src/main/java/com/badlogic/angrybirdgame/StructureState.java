package com.badlogic.angrybirdgame;

import java.io.Serializable;

public class StructureState implements Serializable {
    public float x, y;
    public boolean isDead, is;
    public String type, material;
    public float angle;
    
    public StructureState() {}

    public StructureState(Structure structure) {
        this.x = structure.body.getPosition().x;
        this.y = structure.body.getPosition().y;
        this.isDead = structure.isDead;
        this.type = structure.type;
        this.material = structure.material;
        this.angle = structure.angle;
        
    }
}
