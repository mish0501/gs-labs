package org.graphics.renderes.shapes;

import org.joml.Matrix4f;

public interface ShapeRenderer {
    void render();

    void cleanup();

    void updateAspectRation(int width, int height);

    Matrix4f getModelMatrix();
}

