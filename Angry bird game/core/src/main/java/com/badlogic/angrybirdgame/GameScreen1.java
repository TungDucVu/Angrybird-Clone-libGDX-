package com.badlogic.angrybirdgame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class GameScreen1 extends GameScreen {

	public GameScreen1(Main game) {
		super(game);
		if (game.atLevel <= 1) {
			game.atLevel = 1;
		}
		game.curLevel = 1;
		bg = new Texture("bg/tropical-forest.png");
		shotx = 2.4f;
		shoty = 2.15f;
		partx = 2.5f;
		party = 2.8f;
		inix = 2.8f;
		iniy = 3.7f;
		birdx = 1.7f;
		birdy = 2.2f;
		ground = new Ground(0, 2.15f, 35, 0, world);
		currentGameScreen = this;
		
	}
	@Override
	public void populating() {
		birds.clear();
    	pigs.clear();
    	structures.clear();
    	
    	birds.add(new Bird(2.8f, 3.7f, world, "big"));
    	birds.add(new Bird(2f, 2.15f + 0.2f, world, "red"));
    	birds.add(new Bird(1.5f, 2.15f + 0.2f, world, "yellow"));
    	
    	pigs.add(new Pig(9.35f, 2.15f + 1.4f + 0.2f + 0.2f, world, "small"));
    	pigs.add(new Pig(10.67f, 2.15f + 2.8f + 0.15f + 0.3f, world, "med"));
    	pigs.add(new Pig(12.7f, 2.15f + 3.4f + 0.15f + 0.3f, world, "med"));

    	
        structures.add(new Structure(world, new Vector2(9.35f, 2.15f + 0.7f), "short", "wood", MathUtils.HALF_PI));
        structures.add(new Structure(world, new Vector2(9.35f, 2.15f + 1.4f + 0.15f), "short", "stone", 0));
        structures.add(new Structure(world, new Vector2(10.67f, 2.15f + 1.4f), "med", "wood", MathUtils.HALF_PI));
        structures.add(new Structure(world, new Vector2(10.67f, 2.15f + 2.8f + 0.15f), "short", "stone", 0));
        structures.add(new Structure(world, new Vector2(11.8f, 2.15f + 1.7f), "long", "wood", MathUtils.HALF_PI));
        structures.add(new Structure(world, new Vector2(13.55f, 2.15f + 1.7f),"long", "wood", MathUtils.HALF_PI));
        structures.add(new Structure(world, new Vector2(12.7f, 2.15f + 3.4f + 0.15f), "med", "stone", 0));	

        
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
    	game.level3.stop();
        game.music.stop();
        game.level1.setVolume(0.5f);
        game.level1.play();
        
    }
	
	
	
	
}