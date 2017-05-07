package com.github.incognitojam.arcade.machine;

import com.github.incognitojam.arcade.display.Display;

import javax.script.ScriptEngine;

public class MachineContext {

    private final ScriptEngine engine;
    private Display display;

    public MachineContext(ScriptEngine engine) {
        this.engine = engine;
    }

    public ScriptEngine getEngine() {
        return this.engine;
    }

    public Display getDisplay() {
        return this.display;
    }

    public void setDisplay(Display display) {
        this.display = display;
    }

    public boolean isDisplayConnected() {
        return this.display != null;
    }

}
