package org.graphics.renderes;

import org.graphics.renderes.shapes.CircleRenderer;
import org.graphics.renderes.shapes.CubeRenderer;
import org.graphics.renderes.shapes.ShapeRenderer;
import org.graphics.utils.ShaderProgram;
import org.joml.Matrix4f;


public class Renderer {
    private ShaderProgram shaderProgram;
    private ShapeRenderer[] shapes;

    public void init() {
        shaderProgram = new ShaderProgram("res/shaders/vertexShader.vert", "res/shaders/fragmentShader.frag");

        float[] frontFaceColor = new float[]{1.0f, 0.0f, 0.0f};
        float[] backFaceColor = new float[]{0.0f, 1.0f, 0.0f};
        float[] leftFaceColor = new float[]{0.0f, 0.0f, 1.0f};
        float[] rightFaceColor = new float[]{1.0f, 1.0f, 0.0f};
        float[] bottomFaceColor = new float[]{1.0f, 0.0f, 1.0f};
        float[] topFaceColor = new float[]{0.0f, 1.0f, 1.0f};

        shapes = new ShapeRenderer[]{
                new CubeRenderer(
                        new float[]{-0.5f, 0.8f, 0.5f}, new float[]{0.5f, 0.6f, -0.5f},
                        frontFaceColor, backFaceColor,
                        leftFaceColor, rightFaceColor,
                        bottomFaceColor, topFaceColor
                ),
                new CubeRenderer(
                        new float[]{-0.5f, 0.6f, 0.5f}, new float[]{-0.25f, 0f, 0.25f},
                        frontFaceColor, backFaceColor,
                        leftFaceColor, rightFaceColor,
                        bottomFaceColor, topFaceColor
                ),
                new CubeRenderer(
                        new float[]{0.25f, 0.6f, -0.25f}, new float[]{0.5f, 0f, -0.5f},
                        frontFaceColor, backFaceColor,
                        leftFaceColor, rightFaceColor,
                        bottomFaceColor, topFaceColor
                ),
                new CubeRenderer(
                        new float[]{0.25f, 0.6f, 0.5f}, new float[]{0.5f, 0f, 0.25f},
                        frontFaceColor, backFaceColor,
                        leftFaceColor, rightFaceColor,
                        bottomFaceColor, topFaceColor
                ),
                new CubeRenderer(
                        new float[]{-0.5f, 0.6f, -0.25f}, new float[]{-0.25f, 0f, -0.5f},
                        frontFaceColor, backFaceColor,
                        leftFaceColor, rightFaceColor,
                        bottomFaceColor, topFaceColor
                ),
        };
    }

    public void render() {
        shaderProgram.use();

        Matrix4f projectionMatrix = new Matrix4f()
                .perspective((float) Math.toRadians(75.0f), 1.0f, 0.1f, 100f);
        shaderProgram.setUniform("projectionMatrix", projectionMatrix);

        Matrix4f viewMatrix = new Matrix4f()
                .lookAt(0f, -1f, -2f, 0.0f, 0.0f, 0.0f, 0.0f, 1f, 0.0f);
        shaderProgram.setUniform("viewMatrix", viewMatrix);

        Matrix4f modelMatrix = new Matrix4f().identity().rotate((float) Math.toRadians(18.0f), 0.0f, 1.0f, 0.0f);
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
