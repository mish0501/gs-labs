package org.graphics.renderes;

import org.graphics.renderes.shapes.CubeRenderer;
import org.graphics.renderes.shapes.ShapeRenderer;
import org.graphics.utils.ShaderProgram;
import org.joml.Matrix4f;


public class Renderer {
    private ShaderProgram shaderProgram;
    private ShapeRenderer[] shapes;

    public void init() {
        shaderProgram = new ShaderProgram("res/shaders/vertexShader.vert", "res/shaders/fragmentShader.frag");

        shapes = new ShapeRenderer[]{
                new CubeRenderer(
                        new float[]{-0.5f, -0.5f, 0.5f}, new float[]{0.5f, 0.5f, -0.5f},
                        new float[]{1.0f, 0.0f, 0.0f}, new float[]{0.0f, 1.0f, 0.0f},
                        new float[]{0.0f, 0.0f, 1.0f}, new float[]{1.0f, 1.0f, 0.0f},
                        new float[]{1.0f, 0.0f, 1.0f}, new float[]{0.0f, 1.0f, 1.0f}
                ),
                new CubeRenderer(
                        new float[]{0.5f, -0.5f, -2.5f}, new float[]{1.5f, 0.5f, -1.5f},
                        new float[]{1.0f, 0.0f, 0.0f}, new float[]{0.0f, 1.0f, 0.0f},
                        new float[]{0.0f, 0.0f, 1.0f}, new float[]{1.0f, 1.0f, 0.0f},
                        new float[]{1.0f, 0.0f, 1.0f}, new float[]{0.0f, 1.0f, 1.0f}
                )
        };
    }

    public void render() {
        shaderProgram.use();

        Matrix4f projectionMatrix = new Matrix4f()
                .perspective((float) Math.toRadians(75.0f), 1.0f, 0.1f, 100f);
//                .ortho(-1.0f, 1.0f, -1.0f, 1.0f, 0.1f, 100f);
        shaderProgram.setUniform("projectionMatrix", projectionMatrix);

        Matrix4f viewMatrix = new Matrix4f()
                .lookAt(0.0f, 1.5f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1f, 0.0f);
        shaderProgram.setUniform("viewMatrix", viewMatrix);

        for (ShapeRenderer shape : shapes) {
            shaderProgram.setUniform("modelMatrix", shape.getModelMatrix());
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
