package org.graphics.renderes.shapes;

import org.lwjgl.BufferUtils;

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

        updateVertices();
    }

    public CircleRenderer(float centerX, float centerY, float radius, int segments) {
        vertexCount = segments + 2; // 1 center + 1 extra vertex to close the circle

        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
        this.segments = segments;

        updateVertices();
    }

    public CircleRenderer(float centerX, float centerY, float radius, int segments, float[] centerColor) {
        vertexCount = segments + 2; // 1 center + 1 extra vertex to close the circle

        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
        this.segments = segments;
        this.centerColor = centerColor;

        updateVertices();
    }

    public CircleRenderer(float centerX, float centerY, float radius, int segments, float[] centerColor, float[] edgeColor) {
        vertexCount = segments + 2; // 1 center + 1 extra vertex to close the circle

        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
        this.segments = segments;
        this.centerColor = centerColor;
        this.edgeColor = edgeColor;

        updateVertices();
    }

    @Override
    protected void updateVertices() {
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

        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
        verticesBuffer.put(vertices).flip();

        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        // Атрибут 0 - Позиция (vec3)
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        // Атрибут 1 - Цвят (vec3)
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
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
}
