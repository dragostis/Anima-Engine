package com.ideas.anima.engine;

import com.ideas.anima.engine.graphics.World;

public abstract class Screen {
    protected final Game game;
    private World world;

    public Screen(Game game) {
        this.game = game;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public void draw() {
        world.draw();
    }

    public void pause() {

    }

    public void dispose() {

    }

    public abstract void resume();

    public abstract void update(float deltaTime);
}
