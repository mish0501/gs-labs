package org.graphics.renderes;

import org.graphics.renderes.shapes.CircleRenderer;
import org.graphics.renderes.shapes.ShapeRenderer;
import org.graphics.utils.ShaderProgram;
import org.joml.Matrix4f;


public class Renderer {
    private ShaderProgram shaderProgram;
    private ShapeRenderer[] shapes;

    public void init() {
        shaderProgram = new ShaderProgram("res/shaders/vertexShader.vert", "res/shaders/fragmentShader.frag");

        shapes = new ShapeRenderer[]{
                new CircleRenderer(0.0f, 0.0f, 0.1f, 100, new float[]{1f, 1f, 0f}, new float[]{1f, 1f, 0f}),
        };
    }

    private float translateX = 0.0f;
    private float translateY = 0.5f;
    private float speed = 0.0005f;

    public void render() {
        shaderProgram.use();

        if(translateX < 0.5f && translateY >= 0.5f) {
            translateX += speed;
        }

        if(translateX >= 0.5f && translateY > -0.5f) {
            translateY -= speed;
        }

        if(translateY <= -0.5f && translateX > -0.5f) {
            translateX -= speed;
        }

        if(translateX <= -0.5f && translateY < 0.5f) {
            translateY += speed;
        }

        Matrix4f modelMatrix = new Matrix4f();
        modelMatrix.identity().translate(translateX, translateY, 0f);

        shaderProgram.setUniform("modelMatrix", modelMatrix);

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
