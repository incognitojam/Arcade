package com.github.incognitojam.arcade.display;

import com.github.incognitojam.arcade.Arcade;

import java.util.HashMap;
import java.util.Map;

public class DisplayManager {

    private final Arcade arcade;
    private Map<Integer, Display> displayMap;

    public DisplayManager(Arcade arcade) {
        this.arcade = arcade;

        loadDisplays();
    }

    public void dispose() {
        saveDisplays();
    }

    private void loadDisplays() {
        this.displayMap = new HashMap<>();
    }

    private void verifyDisplays() {

    }

    private void saveDisplays() {

    }

    public boolean doesDisplayExist(int displayId) {
        return this.displayMap.containsKey(displayId);
    }

}
