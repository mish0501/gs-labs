package org.graphics.renderes.shapes;

import org.graphics.utils.GenerateObjectsUtil;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;

public class Pyramid extends BaseShapeRenderer implements ShapeRenderer {
    private int vaoID, vboID, eboID;

    private final Vector3f[] baseVertices, baseColor;
    private final int baseVertexCount;
    private final Vector3f apex, apexColor;

    private Matrix4f modelMatrix;

    public Pyramid(Vector3f[] baseVertices, Vector3f apex, Vector3f[] baseColor, Vector3f apexColor) {
        if (baseVertices.length != baseColor.length) {
            throw new IllegalArgumentException("Base vertices and base color must have the same length");
        }

        this.baseVertices = baseVertices;
        this.baseVertexCount = baseVertices.length;
        this.apex = apex;
        this.baseColor = baseColor;
        this.apexColor = apexColor;

        calculateModelMatrix();
        calculateVertices();
    }

    @Override
    protected void calculateVertices() {
        float[] vertices = new float[(baseVertexCount + 1) * 6];

        vertices[0] = apex.x;
        vertices[1] = apex.y;
        vertices[2] = apex.z;
        vertices[3] = apexColor.x;
        vertices[4] = apexColor.y;
        vertices[5] = apexColor.z;

        int index = 6;

        for (int i = 0; i < baseVertexCount; i++) {
            vertices[index++] = baseVertices[i].x;
            vertices[index++] = baseVertices[i].y;
            vertices[index++] = baseVertices[i].z;
            vertices[index++] = baseColor[i].x;
            vertices[index++] = baseColor[i].y;
            vertices[index++] = baseColor[i].z;
        }

        int[] indices = new int[baseVertexCount * 3 + (baseVertexCount - 2) * 3];

        // Indices for the sides
        for (int i = 0; i < baseVertexCount; i++) {
            indices[i * 3] = 0; // Apex
            indices[i * 3 + 1] = i + 1; // Current base vertex
            indices[i * 3 + 2] = (i + 1) % baseVertexCount + 1; // Next base vertex
        }

        // Indices for the base
        int offset = baseVertexCount * 3;
        for (int i = 1; i < baseVertexCount - 1; i++) {
            indices[offset + (i - 1) * 3] = 1; // First base vertex
            indices[offset + (i - 1) * 3 + 1] = i + 1; // Current base vertex
            indices[offset + (i - 1) * 3 + 2] = i + 2; // Next base vertex
        }

        vaoID = GenerateObjectsUtil.generateVAO();

        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertices.length);
        vertexBuffer.put(vertices).flip();

        vboID = GenerateObjectsUtil.generateVBO(vertexBuffer);

        IntBuffer indexBuffer = BufferUtils.createIntBuffer(indices.length);
        indexBuffer.put(indices).flip();

        eboID = GenerateObjectsUtil.generateEBO(indexBuffer);

        GenerateObjectsUtil.bindVertexAttribute();

        GenerateObjectsUtil.unbindObjects();

        // Free memory
        MemoryUtil.memFree(vertexBuffer);
        MemoryUtil.memFree(indexBuffer);
    }

    private void calculateModelMatrix() {
        float angle = (float) Math.toRadians(25);

        modelMatrix = new Matrix4f()
                .identity()
                .rotate(angle, 0, 1, 0);
    }

    @Override
    public void render() {
        calculateModelMatrix();

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glDrawElements(GL_TRIANGLES, baseVertexCount * 3 + (baseVertexCount - 2) * 3, GL_UNSIGNED_INT, 0);
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
    }

    @Override
    public void cleanup() {
        glDeleteBuffers(vboID);
        glDeleteBuffers(eboID);
        glDeleteVertexArrays(vaoID);
    }


    @Override
    public Matrix4f getModelMatrix() {
        return modelMatrix;
    }
}
