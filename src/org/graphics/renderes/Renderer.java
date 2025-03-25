package org.graphics.renderes;

import org.graphics.renderes.shapes.Pyramid;
import org.graphics.renderes.shapes.ShapeRenderer;
import org.graphics.utils.ShaderProgram;
import org.joml.Matrix4f;
import org.joml.Vector3f;


public class Renderer {
    private ShaderProgram shaderProgram;
    private ShapeRenderer[] shapes;
    private float aspectRatio = 1.0f;

    public void init() {
        shaderProgram = new ShaderProgram("res/shaders/vertexShader.vert", "res/shaders/fragmentShader.frag");

        shapes = new ShapeRenderer[]{
                new Pyramid(
                        new Vector3f[]{
                                new Vector3f(-0.4f, 0.2f, -0.4f),
                                new Vector3f(0.4f, 0.2f, -0.4f),
                                new Vector3f(0.4f, 0.2f, 0.4f),
                                new Vector3f(-0.4f, 0.2f, 0.4f),
                        },
                        new Vector3f(0, 1, 0),
                        new Vector3f[]{

                                new Vector3f(0, 0, 1),
                                new Vector3f(0, 1, 0),
                                new Vector3f(1, 0, 0),
                                new Vector3f(0, 1, 0),
                        },
                        new Vector3f(1, 0.75f, 1)
                ),
                new Pyramid(
                        new Vector3f[]{
                                new Vector3f(-0.6f, 0.1f, -0.6f),
                                new Vector3f(0.6f, 0.1f, -0.6f),
                                new Vector3f(0.6f, 0.1f, 0.6f),
                                new Vector3f(-0.6f, 0.1f, 0.6f),
                        },
                        new Vector3f(0, 1, 0),
                        new Vector3f[]{
                                new Vector3f(0, 1, 0),
                                new Vector3f(0, 0, 1),
                                new Vector3f(0, 1, 0),
                                new Vector3f(1, 0, 0),
                        },
                        new Vector3f(1, 0.5f, 1)
                ).setPlane(new Vector3f(0, 0.8f, 0), 0.4f),

                new Pyramid(
                        new Vector3f[]{
                                new Vector3f(-0.75f, 0, -0.75f),
                                new Vector3f(0.75f, 0, -0.75f),
                                new Vector3f(0.75f, 0, 0.75f),
                                new Vector3f(-0.75f, 0, 0.75f),
                        },
                        new Vector3f(0, 1, 0),
                        new Vector3f[]{
                                new Vector3f(0, 0, 1),
                                new Vector3f(0, 1, 0),
                                new Vector3f(1, 0, 0),
                                new Vector3f(0, 1, 0),
                        },
                        new Vector3f(1, 0.25f, 1)
                ).setPlane(new Vector3f(0, 0.5f, 0), 0.1f)
        };
    }

    public void render() {
        shaderProgram.use();

        Matrix4f projectionMatrix = new Matrix4f()
                .perspective((float) Math.toRadians(75.0f), aspectRatio, 0.1f, 100.0f);
        shaderProgram.setUniform("projectionMatrix", projectionMatrix);

        Matrix4f viewMatrix = new Matrix4f()
                .lookAt(0f, 1.5f, 3f, 0, 0, 0, 0, 1, 0);
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

        aspectRatio = (float) width / height;
    }
}
