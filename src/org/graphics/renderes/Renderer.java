package org.graphics.renderes;

import org.graphics.renderes.shapes.CubeRenderer;
import org.graphics.renderes.shapes.PlaneRenderer;
import org.graphics.renderes.shapes.ShapeRenderer;
import org.graphics.utils.Camera;
import org.graphics.utils.GenerateObjectsUtil;
import org.graphics.utils.ShaderProgram;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL46.*;

public class Renderer {
    private ShaderProgram shaderProgram;
    private ShaderProgram crosshairShaderProgram;
    private ShapeRenderer[] shapes;
    private float aspectRatio = 1f;
    private Camera camera;  // Reference to the camera

    private int crosshairVAO;

    public void init() {
        shaderProgram = new ShaderProgram("res/shaders/vertexShader.vert", "res/shaders/fragmentShader.frag");
        crosshairShaderProgram = new ShaderProgram("res/shaders/crosshairVertexShader.vert", "res/shaders/fragmentShader.frag");

        shapes = new ShapeRenderer[]{
                new CubeRenderer(
                        new float[]{0.0f, 0.0f, 0.0f}, new float[]{0.5f, 0.5f, 0.5f},
                        new float[]{1.0f, 0.0f, 0.0f}, new float[]{0.0f, 1.0f, 0.0f},
                        new float[]{0.0f, 0.0f, 1.0f}, new float[]{1.0f, 0.0f, 1.0f},
                        new float[]{1.0f, 1.0f, 0.0f}, new float[]{0.0f, 1.0f, 1.0f}
                ),
                new PlaneRenderer(5.0f, 5.0f, new float[]{0.5f, 0.5f, 0.5f})
        };

        calculateCrosshairVertices();
    }

    public void render() {
        shaderProgram.use();

        Matrix4f projectionMatrix = new Matrix4f()
                .perspective((float) Math.toRadians(45.0f), aspectRatio, 0.1f, 100.0f);
        shaderProgram.setUniform("projectionMatrix", projectionMatrix);

        // Get the view matrix from the camera
        Matrix4f viewMatrix = camera.getViewMatrix();
        shaderProgram.setUniform("viewMatrix", viewMatrix);

        for (ShapeRenderer shape : shapes) {
            shaderProgram.setUniform("modelMatrix", shape.getModelMatrix());
            shape.render();
        }

        renderCrosshair();
    }

    public void cleanup() {
        for (ShapeRenderer shape : shapes) {
            shape.cleanup();
        }
    }

    public void updateAspectRatio(int width, int height) {
        for (ShapeRenderer shape : shapes) {
            shape.updateAspectRation(width, height);
        }

        aspectRatio = (float) width / height;
        calculateCrosshairVertices();
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    private void calculateCrosshairVertices() {
        float horizontalLineLength = 0.02f * aspectRatio;

        float[] crosshairVertices = {
                // Horizontal line
                -horizontalLineLength, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f,
                horizontalLineLength, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f,
                // Vertical line
                0.0f, -0.02f, 0.0f, 0.0f, 1.0f, 0.0f,
                0.0f, 0.02f, 0.0f, 0.0f, 1.0f, 0.0f
        };

        crosshairVAO = GenerateObjectsUtil.generateVAO();

        FloatBuffer buffer = MemoryUtil.memAllocFloat(crosshairVertices.length);
        buffer.put(crosshairVertices).flip();

        GenerateObjectsUtil.generateVBO(buffer);

        GenerateObjectsUtil.bindVertexAttribute();

        GenerateObjectsUtil.unbindObjects();

        MemoryUtil.memFree(buffer);
    }

    private void renderCrosshair() {
        glDisable(GL_DEPTH_TEST); // Disable depth test so crosshair always renders on top
        crosshairShaderProgram.use();

        glLineWidth(3);
        glBindVertexArray(crosshairVAO);
        glDrawArrays(GL_LINES, 0, 4); // Render two lines
        glBindVertexArray(0);
        glEnable(GL_DEPTH_TEST); // Re-enable depth testing
    }
}
