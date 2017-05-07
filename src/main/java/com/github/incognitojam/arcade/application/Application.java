package com.github.incognitojam.arcade.application;

public class Application {

    private final String name;
    private final String version;
    private final String author;

    public Application(String name, String version, String author) {
        this.name = name;
        this.version = version;
        this.author = author;
    }

    public String getName() {
        return this.name;
    }

    public String getVersion() {
        return this.version;
    }

    public String getAuthor() {
        return this.author;
    }

}
