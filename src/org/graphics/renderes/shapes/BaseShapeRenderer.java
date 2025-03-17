package org.graphics.renderes.shapes;

public abstract class BaseShapeRenderer {
    protected  float aspectRation;

    public void updateAspectRation(int width, int height) {
        aspectRation = (float) width / height;
        calculateVertices();
    }

    protected abstract void calculateVertices();
}
