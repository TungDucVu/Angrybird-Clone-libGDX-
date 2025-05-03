package com.badlogic.angrybirdgame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class GameScreen2 extends GameScreen {

	public GameScreen2(Main game) {
		super(game);
		if (game.atLevel <= 2) {
			game.atLevel = 2;
		}
		game.curLevel = 2;
		bg = new Texture("bg/frozen-mountain.png");
		ground = new Ground(0, 1.75f, 35, 0, world);
		shotx = 2.4f;
		shoty = 1.75f;
		partx = 2.5f;
		party = 2.4f;
		inix = 2.8f;
		iniy = 3.3f;
		birdx = 1.7f;
		birdy = 2;
		currentGameScreen = this;
	}
	
	@Override
	public void populating() {
		birds.clear();
    	pigs.clear();
    	structures.clear();
    	
    	birds.add(new Bird(2.8f, 3.3f, world, "yellow"));
    	birds.add(new Bird(2.5f, 1.75f + 0.2f, world, "yellow"));
    	birds.add(new Bird(2f, 1.75f + 0.2f, world, "red"));
    	birds.add(new Bird(1.2f, 1.75f + 0.4f, world, "big"));
    	birds.add(new Bird(0.2f, 1.75f + 0.4f, world, "big"));
   	
    	pigs.add(new Pig(9.08f, 1.75f + 1.4f + 0.3f + 0.2f, world, "small"));
    	pigs.add(new Pig(14.2f, 1.75f + 1.4f + 0.3f + 0.2f, world, "small"));
    	pigs.add(new Pig(10.45f, 1.75f + 0.3f + 0.3f + 0.3f + 0.3f + 1.4f + 0.3f + 0.2f, world, "small"));
    	pigs.add(new Pig(12.85f, 1.75f + 0.3f + 0.3f + 0.3f + 0.3f + 1.4f + 0.3f + 0.2f, world, "small"));
    	pigs.add(new Pig(11.62f, 1.75f + 0.3f + 0.3f + 0.3f + 0.3f + 2.8f + 0.3f + 0.3f, world, "med"));
    	pigs.add(new Pig(11.65f, 1.75f + 0.4f, world, "big"));
   	
        structures.add(new Structure(world, new Vector2(9.08f, 1.75f + 0.7f), "short", "wood", MathUtils.HALF_PI));
        structures.add(new Structure(world, new Vector2(14.2f, 1.75f + 0.7f), "short", "wood", MathUtils.HALF_PI));
        structures.add(new Structure(world, new Vector2(10.45f, 1.75f + 0.3f + 0.3f + 0.3f + 0.3f + 0.7f), "short", "wood", MathUtils.HALF_PI));
        structures.add(new Structure(world, new Vector2(12.85f, 1.75f + 0.3f + 0.3f + 0.3f + 0.3f + 0.7f), "short", "wood", MathUtils.HALF_PI));
        structures.add(new Structure(world, new Vector2(11.62f, 1.75f + 0.3f + 0.3f + 0.3f + 0.3f + 1.4f), "med", "wood", MathUtils.HALF_PI));
        structures.add(new Structure(world, new Vector2(9.08f, 1.75f + 1.4f + 0.15f), "short", "stone", 0));
        structures.add(new Structure(world, new Vector2(14.2f, 1.75f + 1.4f + 0.15f), "short", "stone", 0));
        structures.add(new Structure(world, new Vector2(10.45f, 1.75f + 0.3f + 0.3f + 0.3f + 0.3f + 1.4f + 0.15f), "short", "stone", 0));
        structures.add(new Structure(world, new Vector2(12.85f, 1.75f + 0.3f + 0.3f + 0.3f + 0.3f + 1.4f + 0.15f), "short", "stone", 0));
        structures.add(new Structure(world, new Vector2(11.62f, 1.75f + 0.3f + 0.3f + 0.3f + 0.3f + 2.8f + 0.15f), "short", "stone", 0));
        structures.add(new Structure(world, new Vector2(11.65f, 1.75f + 0.3f + 0.3f + 0.3f + 0.15f), "med", "ice", 0));
        structures.add(new Structure(world, new Vector2(10.4f, 1.75f + 0.15f), "short", "ice", 0));
        structures.add(new Structure(world, new Vector2(10.4f, 1.75f + 0.3f + 0.15f + 0.05f), "short", "ice", 0));
        structures.add(new Structure(world, new Vector2(10.4f, 1.75f + 0.3f + 0.3f + 0.15f + 0.05f), "short", "ice", 0));
        structures.add(new Structure(world, new Vector2(12.9f, 1.75f + 0.15f), "short", "ice", 0));
        structures.add(new Structure(world, new Vector2(12.9f, 1.75f + 0.3f + 0.15f + 0.05f), "short", "ice", 0));
        structures.add(new Structure(world, new Vector2(12.9f, 1.75f + 0.3f + 0.3f + 0.20f), "short", "ice", 0));

        
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
	@Override
	public void soundManage(Main game) {
    	game.level1.stop();
    	game.level3.stop();
        game.music.stop();
        game.level2.setVolume(0.2f);
        game.level2.play();
        game.chirping.setVolume(0.3f);
        game.chirping.play();
        
    }
	
	
}