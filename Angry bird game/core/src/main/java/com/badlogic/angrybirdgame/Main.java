package com.badlogic.angrybirdgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;


public class Main extends Game{
	public SpriteBatch spriteBatch;
    public OrthographicCamera camera; // Add a public camera
    public Music music;
    public Music level1;
    public Music level2;
    public Music level3;
    public Music chirping;
    public Sound click, wood, stone, ice, oink, win, lose, hit, release;
    public float lastMusicPosition = 0;
    
    public int atLevel, curLevel;

    @Override
    public void create() {
    	atLevel = atLevelState();
        spriteBatch = new SpriteBatch();
        music = Gdx.audio.newMusic(Gdx.files.internal("sound/Original Main Theme - Angry Birds Music.mp3"));   
        
        level1 = Gdx.audio.newMusic(Gdx.files.internal("sound/level1-sound.mp3"));
        level2 = Gdx.audio.newMusic(Gdx.files.internal("sound/level2-sound.mp3"));
        level3 = Gdx.audio.newMusic(Gdx.files.internal("sound/level3-sound.mp3"));
        
        
        click = Gdx.audio.newSound(Gdx.files.internal("sound/clicksound.mp3"));
        
        wood = Gdx.audio.newSound(Gdx.files.internal("sound/wood.mp3"));
        stone = Gdx.audio.newSound(Gdx.files.internal("sound/rock.mp3"));
        ice = Gdx.audio.newSound(Gdx.files.internal("sound/crack.mp3"));
        oink = Gdx.audio.newSound(Gdx.files.internal("sound/oink.mp3"));
        hit = Gdx.audio.newSound(Gdx.files.internal("sound/on hit.mp3"));
        release = Gdx.audio.newSound(Gdx.files.internal("sound/bow and fly.mp3"));
        
        win = Gdx.audio.newSound(Gdx.files.internal("sound/win.mp3"));
        lose = Gdx.audio.newSound(Gdx.files.internal("sound/lose.mp3"));
        
        chirping = Gdx.audio.newMusic(Gdx.files.internal("sound/chirping.mp3"));
        
        // Initialize the camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Set the initial screen
        setScreen(new HomeScreen(this));
    }
    
    private int atLevelState() {
    	FileHandle file = Gdx.files.local("game_state.json");
        if (file.exists()) {
            String jsonString = file.readString();
            Json json = new Json();
            GameState gameState = json.fromJson(GameState.class, jsonString);
            
            return gameState.atLevel;
        }
        return 1;

    }
    
    @Override
    public void dispose() {
        super.dispose();
        spriteBatch.dispose();
    }
}
