package org.graphics.renderes;

import org.graphics.utils.Camera;
import org.graphics.utils.GenerateObjectsUtil;
import org.graphics.utils.InputAction;
import org.graphics.utils.ShaderProgram;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL46.*;

public class Renderer {
    private int vao;
    private ShaderProgram shaderProgram;
    private ShaderProgram crosshairShaderProgram;
    private float aspectRatio = 1f;
    private Camera camera;  // Reference to the camera

    private float alpha = 1.0f; // Alpha value for the shapes

    private int crosshairVAO;

    public void init() {
        shaderProgram = new ShaderProgram("res/shaders/vertexShader.vert", "res/shaders/fragmentShader.frag");
        crosshairShaderProgram = new ShaderProgram("res/shaders/crosshairVertexShader.vert", "res/shaders/crosshairFragmentShader.frag");

        float[] vertices = {
                // Триъгълник отдолу (червен)
                -0.6f, -0.6f, 0.0f, 1.0f, 0.0f, 0.0f,
                0.2f, -0.6f, 0.0f, 1.0f, 0.0f, 0.0f,
                -0.2f, 0.2f, 0.0f, 1.0f, 0.0f, 0.0f,

                // Триъгълник отгоре (зелен)
                -0.3f, -0.3f, 0.0f, 0.0f, 1.0f, 0.0f,
                0.5f, -0.3f, 0.0f, 0.0f, 1.0f, 0.0f,
                0.1f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f,
        };

        vao = GenerateObjectsUtil.generateVAO();

        FloatBuffer buffer = MemoryUtil.memAllocFloat(vertices.length);
        buffer.put(vertices).flip();

        GenerateObjectsUtil.generateVBO(buffer);

        GenerateObjectsUtil.bindVertexAttribute();

        GenerateObjectsUtil.unbindObjects();

        MemoryUtil.memFree(buffer);

//        calculateCrosshairVertices();
    }

    public void render() {
        shaderProgram.use();

        glBindVertexArray(vao);
        shaderProgram.setUniform("alpha", 1.0f);
        glDrawArrays(GL_TRIANGLES, 0, 3);

        shaderProgram.setUniform("alpha", alpha);
        glDrawArrays(GL_TRIANGLES, 3, 3);
        glBindVertexArray(0);

//        renderCrosshair();
    }

    public void handleInputAction(InputAction action) {
        switch (action) {
            case INCREASE -> setAlpha(clamp(alpha + 0.01f));
            case DECREASE -> setAlpha(clamp(alpha - 0.01f));
        }
    }

    private float clamp(float value) {
        return Math.max(0.0f, Math.min(1.0f, value));
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }


    public void cleanup() {
//        for (ShapeRenderer shape : shapes) {
//            shape.cleanup();
//        }
    }

    public void updateAspectRatio(int width, int height) {
//        for (ShapeRenderer shape : shapes) {
//            shape.updateAspectRation(width, height);
//        }

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
        glDisable(GL_DEPTH_TEST | GL_BLEND); // Disable depth test so crosshair always renders on top
        crosshairShaderProgram.use();

        glLineWidth(3);
        glBindVertexArray(crosshairVAO);
        glDrawArrays(GL_LINES, 0, 4); // Render two lines
        glBindVertexArray(0);
        glEnable(GL_DEPTH_TEST | GL_BLEND); // Re-enable depth testing
    }
}
