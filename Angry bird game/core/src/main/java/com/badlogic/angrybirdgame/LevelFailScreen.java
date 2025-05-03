package com.badlogic.angrybirdgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class LevelFailScreen implements Screen {
    private final Main game;
    
    private Texture level;
    private Texture levelFail;
    private Texture blackTexture;
    
    private Texture menu;
    private Texture menu1;
    private Texture restart;
    private Texture restart1;
    private Texture next1;
    
    
    private Circle menuCircle;
    private Circle restartCircle;
    
    private Stage stage;
    private boolean isTransitioning = false;

    public LevelFailScreen(Main game) {
        this.game = game;
        show();
    }

    @Override
    public void show() {
        // Initialize resources here if needed
    	switch (game.curLevel) {
    	case 1:
    		level = new Texture("bg/tropical-forest.png");
    		break;
    	case 2:
    		level = new Texture("bg/frozen-mountain.png");
    		break;
    	case 3:
    		level = new Texture("bg/space.png");
    		break;
    	}
    	
        levelFail = new Texture("bg/level-fail.png");
        blackTexture = new Texture("bg/black-screen.jpg");
        menu = new Texture("bt/menu.png");
        restart = new Texture ("bt/restart.png");
        menu1 = new Texture("bt/menu1.png");
        restart1 = new Texture("bt/restart1.png");
        next1 = new Texture("bt/next1.png");
        

        restartCircle = new Circle();
        menuCircle = new Circle();

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void render(float delta) {
    	
    	if (isTransitioning) {
    		stage.act(delta);
            stage.draw();
    	}
        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        game.camera.unproject(touchPos); 
        game.spriteBatch.setProjectionMatrix(game.camera.combined);
        
        if (!isTransitioning) {
        	// Draw the menu
            game.spriteBatch.begin();
            
            game.spriteBatch.draw(level, 0, 0, level.getWidth(), level.getHeight());
            
            game.spriteBatch.setColor(1, 1, 1, 0.5f);
            game.spriteBatch.draw(blackTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            game.spriteBatch.setColor(1, 1, 1, 1f);

            game.spriteBatch.draw(levelFail, 319, 0, levelFail.getWidth(), levelFail.getHeight());
            
            menuCircle.set(344 + 35, 162, 35);
            restartCircle.set(455 + 35, 162, 35);
          
            
            if (menuCircle.contains(touchPos.x, touchPos.y)) {
                game.spriteBatch.draw(menu, 344, 127, 70, 70); 
            } else {
            	game.spriteBatch.draw(menu1, 344, 127, 70, 70); 
            }
            
            if (restartCircle.contains(touchPos.x, touchPos.y)) {
                game.spriteBatch.draw(restart, 445, 127, 70, 70); 
            } else {
            	game.spriteBatch.draw(restart1, 445, 127, 70, 70); 
            }
            
            game.spriteBatch.draw(next1, 546, 127, 70, 70); 
            
            

            game.spriteBatch.end();      
        }

        // Handle input
        if (Gdx.input.justTouched()) {
        	
            if (menuCircle.contains(touchPos.x, touchPos.y)) {
            	game.click.play(0.2f);
            	switch (game.curLevel) {
            	case 1:
            		startTransitionTo(new MenuScreen(game), new Texture(Gdx.files.internal("bg/tropical-forest.png")));
            		break;
            	case 2:
            		startTransitionTo(new MenuScreen(game), new Texture(Gdx.files.internal("bg/frozen-mountain.png")));
            		break;
            	case 3:
            		startTransitionTo(new MenuScreen(game), new Texture(Gdx.files.internal("bg/space.png")));
            		break;
            	}
                dispose(); 
            }
            if (restartCircle.contains(touchPos.x, touchPos.y)) {
            	game.click.play(0.2f);
            	switch (game.curLevel) {
            	case 1:
            		game.setScreen(new LoadingScreen(game));
            		break;
            	case 2:
            		game.setScreen(new LoadingScreen(game));
            		break;
            	case 3:
            		game.setScreen(new LoadingScreen(game));
            		break;
            	}
                dispose(); 
            }            
            
        }
        
        stage.act(delta);
        stage.draw();

    }
    
    public void startTransitionTo(Screen nextScreen, Texture fadeTexture) {
        isTransitioning = true;

        Image fadeOverlay = new Image(fadeTexture);
        fadeOverlay.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        fadeOverlay.getColor().a = 1f; // Set opacity to fully opaque

        stage.addActor(fadeOverlay);

        // Fade out the overlay and switch screens
        fadeOverlay.addAction(Actions.sequence(
            Actions.fadeOut(1f, Interpolation.fade), // Duration of fade-out (1 second)
            Actions.run(() -> {
                game.setScreen(nextScreen); // Transition to the next screen
                dispose(); // Clean up current screen resources
            })
        ));
    }
    
    

    @Override
    public void resize(int width, int height) {
        // Handle resizing if necessary
    }

    @Override
    public void pause() {
        // Handle pause if necessary
    }

    @Override
    public void resume() {
        // Handle resume if necessary
    }

    @Override
    public void hide() {
        // Called when this screen is no longer the current screen
    }

    @Override
    public void dispose() {
    	level.dispose();
        levelFail.dispose();
        menu.dispose();
        menu1.dispose();
        restart.dispose();
        restart1.dispose();
        next1.dispose();
        
    }
}