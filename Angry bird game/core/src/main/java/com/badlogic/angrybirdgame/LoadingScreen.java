package com.badlogic.angrybirdgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

public class LoadingScreen implements Screen {
    private final Main game;

    private Array<Texture> loadingTextures; // Array to hold loading images
    private int currentFrame = 0;           // Index of the current frame
    private float frameDuration = 0.1f;     // Duration for each frame (in seconds)
    private float stateTime = 0;            // Time accumulator to track time between frames
    private float currentTime = 0;
    private float endTime = 0.5f;
    

    public LoadingScreen(Main game) {
        this.game = game;
        show();
    }

    @Override
    public void show() {
        // Load the textures for the loading animation
        loadingTextures = new Array<Texture>();
        loadingTextures.add(new Texture("loading/1.png"));
        loadingTextures.add(new Texture("loading/2.png"));
        loadingTextures.add(new Texture("loading/3.png"));
        loadingTextures.add(new Texture("loading/4.png"));
        loadingTextures.add(new Texture("loading/5.png"));
        loadingTextures.add(new Texture("loading/6.png"));
        loadingTextures.add(new Texture("loading/7.png"));
        loadingTextures.add(new Texture("loading/8.png"));
        loadingTextures.add(new Texture("loading/9.png"));
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update the state time
        stateTime += delta;
        currentTime += delta;
        

        // Switch to the next frame if enough time has passed
        if (stateTime > frameDuration) {
            currentFrame = (currentFrame + 1) % loadingTextures.size; // Loop through images
            stateTime = 0;
        }

        // Draw the current frame
        game.spriteBatch.begin();
        game.spriteBatch.draw(loadingTextures.get(currentFrame),-50, 30, 637 * 0.5f, 358 * 0.5f); // Drawing centered
        game.spriteBatch.end();
        
        if(currentTime > endTime) {
        	switch (game.curLevel) {
        	case 1:
        		game.setScreen(new GameScreen1(game));
        		dispose();
        		break;
        	case 2:
        		game.setScreen(new GameScreen2(game));
        		dispose();
        		break;
        	case 3:
        		game.setScreen(new GameScreen3(game));
        		dispose();
        		break;
        	}
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
        // Dispose of textures
        for (Texture texture : loadingTextures) {
            texture.dispose();
        }
    }
}
