package com.badlogic.angrybirdgame;

import java.io.Serializable;
import java.util.ArrayList;

public class GameState implements Serializable {
    public ArrayList<BirdState> birdStates;
    public ArrayList<PigState> pigStates;
    public ArrayList<StructureState> structureStates;
    public int currentBirdIndex, atLevel, curLevel;
    public boolean isNewGame;

    public GameState() {
    	birdStates = new ArrayList<>();
        pigStates = new ArrayList<>();
        structureStates = new ArrayList<>();
    }
}
