package org.graphics.renderes.shapes;

public interface ShapeRenderer {
    void render();

    void cleanup();

    void updateAspectRation(int width, int height);
}

