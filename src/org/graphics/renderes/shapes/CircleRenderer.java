package org.graphics.renderes.shapes;

import org.graphics.utils.GenerateObjectsUtil;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL30.*;

public class CircleRenderer extends BaseShapeRenderer implements ShapeRenderer {
    private int vaoID, vboID;
    private final int vertexCount;

    private final float centerX, centerY, radius;
    private final int segments;

    private float[] centerColor = new float[]{1.0f, 1.0f, 1.0f};
    private float[] edgeColor = new float[]{1.0f, 0.0f, 1.0f};

    public CircleRenderer(float centerX, float centerY, float radius) {
        vertexCount = 102; // 1 center + 1 extra vertex to close the circle

        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
        this.segments = 100;

        calculateVertices();
    }

    public CircleRenderer(float centerX, float centerY, float radius, int segments) {
        vertexCount = segments + 2; // 1 center + 1 extra vertex to close the circle

        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
        this.segments = segments;

        calculateVertices();
    }

    public CircleRenderer(float centerX, float centerY, float radius, int segments, float[] centerColor) {
        vertexCount = segments + 2; // 1 center + 1 extra vertex to close the circle

        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
        this.segments = segments;
        this.centerColor = centerColor;

        calculateVertices();
    }

    public CircleRenderer(float centerX, float centerY, float radius, int segments, float[] centerColor, float[] edgeColor) {
        vertexCount = segments + 2; // 1 center + 1 extra vertex to close the circle

        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
        this.segments = segments;
        this.centerColor = centerColor;
        this.edgeColor = edgeColor;

        calculateVertices();
    }

    @Override
    protected void calculateVertices() {
        // Generate vertex data
        float[] vertices = new float[vertexCount * 6];
        vertices[0] = centerX;
        vertices[1] = centerY;
        vertices[2] = 0.0f;
        vertices[3] = centerColor[0];  //R
        vertices[4] = centerColor[1];  //G
        vertices[5] = centerColor[2];  //B

        for (int i = 1; i <= segments + 1; i++) {
            float angle = (float) (2.0 * Math.PI * i / segments);
            vertices[i * 6] = centerX + (float) Math.cos(angle) * radius / aspectRation;
            vertices[i * 6 + 1] = centerY + (float) Math.sin(angle) * radius;
            vertices[i * 6 + 2] = 0.0f;
            vertices[i * 6 + 3] = edgeColor[0];
            vertices[i * 6 + 4] = edgeColor[1];
            vertices[i * 6 + 5] = edgeColor[2];
        }

        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertices.length);
        vertexBuffer.put(vertices).flip();

        // Generate VAO
        vaoID = GenerateObjectsUtil.generateVAO();

        // Generate VBO
        vboID = GenerateObjectsUtil.generateVBO(vertexBuffer);

        GenerateObjectsUtil.bindVertexAttributeColor();

        // Unbind VAO/VBO
        GenerateObjectsUtil.unbindObjects();

        // Free memory
        MemoryUtil.memFree(vertexBuffer);
    }

    @Override
    public void render() {
        glBindVertexArray(vaoID);
        glDrawArrays(GL_TRIANGLE_FAN, 0, vertexCount);
        glBindVertexArray(0);
    }

    @Override
    public void cleanup() {
        glDeleteBuffers(vboID);
        glDeleteVertexArrays(vaoID);
    }

    @Override
    public Matrix4f getModelMatrix() {
        return null;
    }
}
