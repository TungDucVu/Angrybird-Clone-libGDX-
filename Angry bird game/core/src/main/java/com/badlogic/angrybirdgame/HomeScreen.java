package com.badlogic.angrybirdgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class HomeScreen implements Screen {
    private final Main game;

    private Texture backgroundTexture;
    private Texture muteButtonTexture;

    private Button playButton, loadButton, soundButton, quitButton;

    private Stage stage;
    private Image moveInImage;
    private Image moveOutImage;
    private boolean isTransitioning = false;

    public HomeScreen(Main game) {
        this.game = game;
        soundManage();
        show();
    }
    
    public void soundManage() {
    	game.level1.stop();
        game.level2.stop();
        game.level3.stop();
        game.music.setLooping(true);
        game.music.setVolume(0.3f);
        game.music.play();
    }

    @Override
    public void show() {
        // Load background texture
        backgroundTexture = new Texture("bg/Homescreen12.jpg");

        // Initialize buttons with textures and positions
        playButton = new Button(new Texture("bt/playbutton.png"), 430, 312);
        loadButton = new Button(new Texture("bt/levels.png"), 417, 249);
        soundButton = new Button(new Texture("bt/sound.png"), 417, 184);
        quitButton = new Button(new Texture("bt/quit.png"), 432, 121);
        muteButtonTexture = new Texture("bt/mute.png");

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        game.camera.unproject(touchPos);

        game.spriteBatch.begin();
        game.spriteBatch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Draw buttons
        drawButton(playButton, touchPos);
        drawButton(loadButton, touchPos);
        drawButton(soundButton, touchPos, game.music.isPlaying() ? soundButton.texture : muteButtonTexture);
        drawButton(quitButton, touchPos);

        game.spriteBatch.end();

        // Handle button actions
        if (Gdx.input.justTouched()) {
            if (playButton.isTouched(touchPos)) {
            	game.click.play(0.2f);
            	animateTransition(new Texture("bg/homescreen12-blur.png"), new Texture("bg/homescreen12.jpg"), new MenuScreen(game));
            }

            if (loadButton.isTouched(touchPos)) {
            	game.click.play(0.2f);
                animateTransition(new Texture("bg/homescreen12-blur.png"), new Texture("bg/homescreen12.jpg"), new MenuScreen(game));
            }

            if (soundButton.isTouched(touchPos)) {
            	game.click.play(0.2f);
                toggleSound();
            }

            if (quitButton.isTouched(touchPos)) {
            	game.click.play(0.2f);
                dispose();
                Gdx.app.exit();
            }
        }

        stage.act(delta);
        stage.draw();
    }

    private void drawButton(Button button, Vector3 touchPos) {
        drawButton(button, touchPos, button.texture);
    }

    private void drawButton(Button button, Vector3 touchPos, Texture texture) {
        if (button.isHovered(touchPos)) {
            game.spriteBatch.draw(texture, button.bounds.x, button.bounds.y, button.bounds.width, button.bounds.height);
        } else {
            game.spriteBatch.draw(texture, button.bounds.x, button.bounds.y, button.bounds.width * 0.9f, button.bounds.height * 0.9f);
        }
    }
    
    public void startTransitionTo(Screen nextScreen) {
        isTransitioning = true;

        // Create a black fade overlay
        Texture fadeTexture = new Texture(Gdx.files.internal("bg/frozen-dark.png")); // Use a black 1x1 px image
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

    private void animateTransition(Texture imageIn, Texture imageOut, Screen nextScreen) {
        if (!isTransitioning) {
            isTransitioning = true;
            
            // Create Image actors for both the incoming and outgoing images
            moveInImage = new Image(imageIn);
            moveOutImage = new Image(imageOut);
            
            // Set initial positions for the incoming image and the outgoing image
            moveInImage.setPosition(Gdx.graphics.getWidth()-10, 0); // Start off-screen (right side)
            moveOutImage.setPosition(0, 0); // The outgoing image is on screen
            
            // Add both images to the stage
            stage.addActor(moveInImage);
            stage.addActor(moveOutImage);

            // Add actions for both images
            moveInImage.addAction(Actions.moveTo(0, 0, 0.5f, Interpolation.sine)); // Move the incoming image to the center
            moveOutImage.addAction(Actions.sequence(
                Actions.moveTo(-Gdx.graphics.getWidth(), 0, 0.5f, Interpolation.sine), // Move the outgoing image off-screen to the left
                Actions.run(() -> {
                    // Transition to the next screen after the outgoing image has moved out
                    game.setScreen(nextScreen);
                    dispose();
                })
            ));
        }
    }

    private void toggleSound() {
        if (game.music.isPlaying()) {
            game.lastMusicPosition = game.music.getPosition();
            game.music.pause();
        } else {
            game.music.setPosition(game.lastMusicPosition);
            game.music.play();
        }
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        backgroundTexture.dispose();
        playButton.dispose();
        loadButton.dispose();
        soundButton.dispose();
        quitButton.dispose();
        muteButtonTexture.dispose();
        
    }

    // Helper class for button handling
    private class Button {
        Texture texture;
        Rectangle bounds;

        Button(Texture texture, float x, float y) {
            this.texture = texture;
            this.bounds = new Rectangle(x, y, texture.getWidth(), texture.getHeight());
        }

        boolean isHovered(Vector3 touchPos) {
            return bounds.contains(touchPos.x, touchPos.y);
        }

        boolean isTouched(Vector3 touchPos) {
            return isHovered(touchPos) && Gdx.input.justTouched();
        }

        void dispose() {
            texture.dispose();
        }
    }
}
