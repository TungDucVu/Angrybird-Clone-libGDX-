package com.badlogic.angrybirdgame;

import java.io.Serializable;

public class BirdState implements Serializable {
	 public float x, y;
	 public boolean isDead, isFlying, isReady;
	 public String type;
	 
	 public BirdState() {}
	
	 public BirdState(Bird bird) {
	     this.x = bird.body.getPosition().x;
	     this.y = bird.body.getPosition().y;
	     this.isDead = bird.isDead;
	     this.isFlying = bird.isFlying;
	     this.isReady = bird.isReady;
	     this.type = bird.type;
	 }
}
