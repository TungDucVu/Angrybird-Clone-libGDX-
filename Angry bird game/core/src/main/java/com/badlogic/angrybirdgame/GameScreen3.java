package com.badlogic.angrybirdgame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class GameScreen3 extends GameScreen {

	public GameScreen3(Main game) {
		super(game);
		if (game.atLevel <= 3) {
			game.atLevel = 3;
		}
		game.curLevel = 3;
		bg = new Texture("bg/space.png");
		shotx = 1.7f;
		shoty = 1.8f;
		partx = 1.8f;
		party = 2.5f;
		inix = 2.1f;
		iniy = 3.2f;
		birdx = 1.18f;
		birdy = 1.82f;
		ground = new Ground(0, 1.8f, 35, 0, world);
		currentGameScreen = this;
	}
	@Override
	public void populating() {
		birds.clear();
    	pigs.clear();
    	structures.clear();
    	
    	birds.add(new Bird(2.1f, 3.2f, world, "yellow"));
    	birds.add(new Bird(2.5f, 1.8f + 0.2f, world, "yellow"));
    	birds.add(new Bird(3f, 1.8f + 0.2f, world, "red"));
    	birds.add(new Bird(1.5f, 1.8f + 0.4f, world, "big"));
//    	
    	pigs.add(new Pig(8.87f, 1.8f + 2.8f + 0.3f + 0.2f, world, "small"));
    	pigs.add(new Pig(12.06f, 1.8f + 2.8f + 0.3f + 0.2f, world, "small"));
    	pigs.add(new Pig(9.9f, 1.8f + 2.8f + 0.3f + 1.4f + 0.3f + 0.2f, world, "small"));
    	pigs.add(new Pig(11f, 1.8f + 2.8f + 0.3f + 1.4f + 0.3f + 0.2f, world, "small"));
    	pigs.add(new Pig(10.45f, 1.8f + 2.8f + 0.3f + 1.4f + 0.3f + 1.4f + 0.3f + 0.25f, world, "small"));
    	pigs.add(new Pig(8.87f, 1.8f + 1.4f + 0.3f + 0.3f, world, "med"));
    	pigs.add(new Pig(12.06f, 1.8f + 1.4f + 0.3f + 0.3f, world, "med"));
    	pigs.add(new Pig(10.45f, 1.8f + 2.8f + 0.3f + 0.4f, world, "big"));
   	
        structures.add(new Structure(world, new Vector2(8.87f, 1.8f + 0.7f), "short", "wood", MathUtils.HALF_PI));
        structures.add(new Structure(world, new Vector2(12.06f, 1.8f + 0.7f), "short", "wood", MathUtils.HALF_PI));
        structures.add(new Structure(world, new Vector2(9.4f, 1.8f + 2.8f + 0.3f + 0.7f), "short", "ice", MathUtils.HALF_PI));
        structures.add(new Structure(world, new Vector2(11.5f, 1.8f + 2.8f + 0.3f + 0.7f), "short", "ice", MathUtils.HALF_PI));
//        
        structures.add(new Structure(world, new Vector2(9.7f, 1.8f + 1.4f), "med", "ice", MathUtils.HALF_PI));
        structures.add(new Structure(world, new Vector2(10.45f, 1.8f + 1.4f), "med", "stone", MathUtils.HALF_PI));
        structures.add(new Structure(world, new Vector2(11.2f, 1.8f + 1.4f), "med", "ice", MathUtils.HALF_PI));
        
        structures.add(new Structure(world, new Vector2(8.35f, 1.8f + 0.7f), "short", "ice", MathUtils.HALF_PI));
        structures.add(new Structure(world, new Vector2(12.56f, 1.8f + 0.7f), "short", "ice", MathUtils.HALF_PI));
//        
        structures.add(new Structure(world, new Vector2(8.87f, 1.8f + 1.4f + 0.15f), "short", "ice", 0));
        structures.add(new Structure(world, new Vector2(12.06f, 1.8f + 1.4f + 0.15f), "short", "ice", 0));
        structures.add(new Structure(world, new Vector2(9.4f, 1.8f + 2.8f + 0.3f + 1.4f + 0.3f + 0.71f), "short", "wood", MathUtils.HALF_PI));
        structures.add(new Structure(world, new Vector2(10.45f, 1.8f + 2.8f + 0.3f + 1.4f + 0.3f + 0.71f), "short", "ice", MathUtils.HALF_PI));
        structures.add(new Structure(world, new Vector2(11.5f, 1.8f + 2.8f + 0.3f + 1.4f + 0.3f + 0.71f), "short", "wood", MathUtils.HALF_PI));
//        
        structures.add(new Structure(world, new Vector2(10.45f, 1.8f + 2.8f + 0.3f + 1.4f + 0.16f), "med", "stone", 0));
        structures.add(new Structure(world, new Vector2(10.45f, 1.8f + 2.8f + 0.3f + 1.4f + 0.3f + 1.4f + 0.17f), "med", "stone", 0));
        structures.add(new Structure(world, new Vector2(10.45f, 1.8f + 2.8f + 0.15f), "long", "stone", 0));

        
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
    	game.level2.stop();
    	game.level1.stop();
        game.music.stop();
        game.level3.setVolume(0.2f);
        game.level3.play();
        game.chirping.setVolume(0.3f);
        game.chirping.play();
        
    }
	
	
}