package com.badlogic.angrybirdgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.math.Interpolation;

public class MenuScreen implements Screen {
    private final Main game;
    
    private Texture level1, level2, level3, level2Lock, level3Lock, selectLevel, back, back1, background;
    private Rectangle level1Rect, level2Rect, level3Rect, backRect;
    
    private boolean isLevel1Hovered;
    private boolean isLevel2Hovered;
    private boolean isLevel3Hovered;
    private boolean isBackHovered;

    
    private Stage stage;
    private Image moveInImage;
    private Image moveOutImage;
    private boolean isTransitioning = false;

    // Constructor
    public MenuScreen(Main game) {
        this.game = game;
        soundManage();
        show();
    }
    
    private void soundManage() {
    	game.level1.stop();
    	game.level2.stop();
    	game.level3.stop();
    	game.chirping.stop();
    	game.music.setVolume(0.3f);
    	game.music.play();
        
    }

    @Override
    public void show() {
        level1 = new Texture("bg/level1.png");
        level2 = new Texture("bg/level2.png");
        level3 = new Texture("bg/level3.png");
        level2Lock = new Texture("bg/level2-lock.png");
        level3Lock = new Texture("bg/level3-lock.png");
        selectLevel = new Texture("bg/Select-level.png");
        back = new Texture("bt/back.png");
        back1 = new Texture("bt/back1.png");
        background = new Texture("bg/homescreen12-blur.png");

        level1Rect = new Rectangle(81, 22, level1.getWidth(), level1.getHeight());
        level2Rect = new Rectangle(360, 22, level2.getWidth(), level2.getHeight());
        level3Rect = new Rectangle(639, 22, level3.getWidth(), level3.getHeight());
        backRect = new Rectangle(11, 460, back.getWidth(), back.getHeight());
        
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        
        
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        game.camera.unproject(touchPos); 
        
        isLevel1Hovered = level1Rect.contains(touchPos.x, touchPos.y);
        isLevel2Hovered = level2Rect.contains(touchPos.x, touchPos.y);
        isLevel3Hovered = level3Rect.contains(touchPos.x, touchPos.y);
        isBackHovered = backRect.contains(touchPos.x, touchPos.y);
       

        if (!isTransitioning) {
            // Regular rendering when no transition is happening
            game.spriteBatch.begin();
            game.spriteBatch.draw(background, 0, 0, background.getWidth(), background.getHeight());

            // Scale the textures if hovered
            if (isLevel1Hovered) {
                game.spriteBatch.draw(level1, 71, 12, level1.getWidth(), level1.getHeight()); // slightly larger
            } else {
                game.spriteBatch.draw(level1, 81, 22, level1.getWidth() * 0.9f, level1.getHeight() * 0.9f);
            }
            
            if (game.atLevel < 2) {
            	level2 = level2Lock;
            }
            
            if (isLevel2Hovered) {
                game.spriteBatch.draw(level2, 350, 12, level2.getWidth(), level2.getHeight()); // slightly larger
            } else {
                game.spriteBatch.draw(level2, 360, 22, level2.getWidth() * 0.9f, level2.getHeight() * 0.9f);
            }
            
            if (game.atLevel < 3) {
            	level3 = level3Lock;
            }

            if (isLevel3Hovered) {
                game.spriteBatch.draw(level3, 629, 12, level3Lock.getWidth(), level3.getHeight()); // slightly larger
            } else {
                game.spriteBatch.draw(level3, 639, 22, level3Lock.getWidth() * 0.9f, level3.getHeight() * 0.9f);
            }

            // Draw the select level and back buttons
            game.spriteBatch.draw(selectLevel, 393, 460, selectLevel.getWidth(), selectLevel.getHeight());

            if (isBackHovered) {
                game.spriteBatch.draw(back, 11, 450, back.getWidth(), back.getHeight()); // slightly larger if hovered
            } else {
                game.spriteBatch.draw(back1, 11, 450, back.getWidth(), back.getHeight());
                
            }
            game.spriteBatch.end();
        }

        // Handle input for click and start transitions
        if (Gdx.input.justTouched() && !isTransitioning) {
            if (level1Rect.contains(touchPos.x, touchPos.y)) {
            	game.click.play(0.2f);
            	if (game.atLevel <= 1) game.atLevel = 1;
            	game.curLevel = 1;
                game.setScreen(new LoadingScreen(game)); 
                dispose(); 
            }
            
            if (level2Rect.contains(touchPos.x, touchPos.y)) {
            	game.click.play(0.2f);
            	if (!level2.equals(level2Lock)) {
            		if (game.atLevel <= 2) game.atLevel = 2;
            		game.curLevel = 2;
                    game.setScreen(new LoadingScreen(game));
                    dispose(); 
            	} 
                
                
            }
            
            if (level3Rect.contains(touchPos.x, touchPos.y)) {
            	game.click.play(0.2f);
            	if (!level3.equals(level3Lock)) {
            		if (game.atLevel <= 3) game.atLevel = 3;
            		game.curLevel = 3;
                    game.setScreen(new LoadingScreen(game)); 
                    dispose(); 
            	}
                

            }
            
            if (backRect.contains(touchPos.x, touchPos.y)) {
            	game.click.play(0.2f);
                startTransitionTo(new Texture("bg/homescreen12.jpg"), new Texture("bg/homescreen12-blur.png"), new HomeScreen(game));
            }
        }
        
        stage.act(delta);
        stage.draw();
    }

    private void startTransitionTo(Texture imageIn, Texture imageOut, Screen nextScreen) {
        isTransitioning = true;
        
        moveInImage = new Image(imageIn);
        moveOutImage = new Image(imageOut);
        
        moveInImage.setPosition(-Gdx.graphics.getWidth(), 0); // Start off-screen (right side)
        moveOutImage.setPosition(0, 0);
        
        stage.addActor(moveInImage);
        stage.addActor(moveOutImage);

        // Animate the transition of the imageOut and imageIn
        moveInImage.addAction(Actions.moveTo(0, 0, 0.5f, Interpolation.sine)); // Move the incoming image to the center
        moveOutImage.addAction(Actions.sequence(
            Actions.moveTo(Gdx.graphics.getWidth(), 0, 0.5f, Interpolation.sine), // Move the outgoing image off-screen to the left
            Actions.run(() -> {
                // Transition to the next screen after the outgoing image has moved out
                game.setScreen(nextScreen);
                dispose();
            })
        ));
    }

    @Override
    public void resize(int width, int height) {
        // Handle resizing
    }

    @Override
    public void pause() {
        // Handle pause
    }

    @Override
    public void resume() {
        // Handle resume
    }

    @Override
    public void hide() {
        // Handle hide
    }

    @Override
    public void dispose() {
        // Dispose textures
        level1.dispose();
        level2.dispose();
        level3.dispose();
        level2Lock.dispose();
        level3Lock.dispose();
        selectLevel.dispose();
        back.dispose();
        back1.dispose();
        background.dispose();
//        imageInTexture.dispose();
//        imageOutTexture.dispose();
        stage.dispose();
        
    }
}
