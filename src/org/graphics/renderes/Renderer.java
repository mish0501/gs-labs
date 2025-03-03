package org.graphics.renderes;

import org.graphics.renderes.shapes.CircleRenderer;
import org.graphics.renderes.shapes.ShapeRenderer;
import org.graphics.utils.ShaderProgram;


public class Renderer {
    private ShaderProgram shaderProgram;
    private ShapeRenderer[] shapes;

    public void init() {
        shaderProgram = new ShaderProgram("res/shaders/vertexShader.vert", "res/shaders/fragmentShader.frag");

        shapes = new ShapeRenderer[]{
                new CircleRenderer(0.0f, 0.0f, 0.5f, 100, new float[]{1.0f, 0.99f, 0.99f}, new float[]{1.0f, 0.0f, 0.0f}),
        };
    }

    public void render() {
        shaderProgram.use();

        for (ShapeRenderer shape : shapes) {
            shape.render();
        }
    }

    public void cleanup() {
        for (ShapeRenderer shape : shapes) {
            shape.cleanup();
        }
    }

    public void updateAspectRation(int width, int height) {
        for (ShapeRenderer shape : shapes) {
            shape.updateAspectRation(width, height);
        }
    }
}
