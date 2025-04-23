package org.graphics.renderes;

import org.graphics.utils.Camera;
import org.graphics.utils.GenerateObjectsUtil;
import org.graphics.utils.InputAction;
import org.graphics.utils.ShaderProgram;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL46.*;

public class Renderer {
    private int vao;
    private ShaderProgram shaderProgram;
    private ShaderProgram crosshairShaderProgram;
    private float aspectRatio = 1f;
    private Camera camera;
    // Reference to the camera
    private int textureID;

    private float alpha = 1.0f; // Alpha value for the shapes

    private int crosshairVAO;

    public void init() {
        shaderProgram = new ShaderProgram("res/shaders/vertexShaderTexture.vert", "res/shaders/fragmentShaderTexture.frag");
        crosshairShaderProgram = new ShaderProgram("res/shaders/crosshairVertexShader.vert", "res/shaders/crosshairFragmentShader.frag");
        loadTexture("res/textures/sky.png");

        float[] vertices = {
                // X Y Z U V
                -0.5f, -0.5f, 0.0f, 0.0f, 0.0f,
                0.5f, -0.5f, 0.0f, 3.0f, 0.0f,
                0.0f, 0.5f, 0.0f, 1.5f, 3.0f
        };

        vao = GenerateObjectsUtil.generateVAO();

        FloatBuffer buffer = MemoryUtil.memAllocFloat(vertices.length);
        buffer.put(vertices).flip();

        GenerateObjectsUtil.generateVBO(buffer);

        GenerateObjectsUtil.bindVertexAttributeTexture();

        GenerateObjectsUtil.unbindObjects();

        MemoryUtil.memFree(buffer);

//        calculateCrosshairVertices();
    }

    public void render() {
        shaderProgram.use();
        float time = (System.nanoTime() / 1_000_000_000.0f);
        shaderProgram.setUniform("time", time);

        glBindVertexArray(vao);
        glDrawArrays(GL_TRIANGLES, 0, 3);
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

        GenerateObjectsUtil.bindVertexAttributeColor();

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

    private void loadTexture(String path) {
        textureID = glGenTextures();

        glBindTexture(GL_TEXTURE_2D, textureID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);

        STBImage.stbi_set_flip_vertically_on_load(true);
        ByteBuffer image = STBImage.stbi_load(path, width, height, channels, 4);

        if (image != null) {
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(), height.get(), 0, GL_RGBA,
                    GL_UNSIGNED_BYTE, image);
            glGenerateMipmap(GL_TEXTURE_2D);

            STBImage.stbi_image_free(image);
        } else {
            System.err.println("Неуспешно зареждане на текстура: " + path);
        }
    }
}
