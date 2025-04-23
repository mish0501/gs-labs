package org.graphics.renderes.shapes;

import org.graphics.utils.GenerateObjectsUtil;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

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

    private Vector3f planeNormal;
    private float planeD;

    private Matrix4f modelMatrix;

    private int numberOfTriangles;

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

    public Pyramid setPlane(Vector3f normal, float d) {
        this.planeNormal = normal;
        this.planeD = d;

        calculateVertices();

        return this;
    }

    private Vector3f intersectPlane(Vector3f p1, Vector3f p2) {
        Vector3f direction = new Vector3f(p2).sub(p1);
        float t = (planeD - planeNormal.dot(p1)) / planeNormal.dot(direction);
        return new Vector3f(p1).add(direction.mul(t));
    }

    private boolean isIntersecting(Vector3f p1, Vector3f p2) {
        float d1 = planeNormal.dot(p1) - planeD;
        float d2 = planeNormal.dot(p2) - planeD;
        return d1 * d2 < 0;
    }

    @Override
    protected void calculateVertices() {
        float[] vertices;
        int[] indices;

        if (planeNormal != null) {
            List<Vector3f> intersectionPoints = new ArrayList<>();
            List<Vector3f> belowPlaneVertices = new ArrayList<>();
            List<Vector3f> belowPlaneColors = new ArrayList<>();

            for (int i = 0; i < baseVertexCount; i++) {
                Vector3f p1 = baseVertices[i];
                Vector3f p2 = apex;
                if (isIntersecting(p1, p2)) {
                    intersectionPoints.add(intersectPlane(p1, p2));
                }
                if (planeNormal.dot(p1) - planeD < 0) {
                    belowPlaneVertices.add(p1);
                    belowPlaneColors.add(baseColor[i]);
                }
            }

            int newVertexCount = belowPlaneVertices.size() + intersectionPoints.size();
            vertices = new float[newVertexCount * 6];
            int index = 0;

            for (int i = 0; i < belowPlaneVertices.size(); i++) {
                Vector3f vertex = belowPlaneVertices.get(i);
                Vector3f color = belowPlaneColors.get(i);
                vertices[index++] = vertex.x;
                vertices[index++] = vertex.y;
                vertices[index++] = vertex.z;
                vertices[index++] = color.x;
                vertices[index++] = color.y;
                vertices[index++] = color.z;
            }

            for (Vector3f point : intersectionPoints) {
                vertices[index++] = point.x;
                vertices[index++] = point.y;
                vertices[index++] = point.z;
                vertices[index++] = apexColor.x;
                vertices[index++] = apexColor.y;
                vertices[index++] = apexColor.z;
            }

            indices = new int[(belowPlaneVertices.size() - 2) * 3 + intersectionPoints.size() * 3 + belowPlaneVertices.size() * 6];

            // Indices for the base
            index = 0;
            for (int i = 1; i < belowPlaneVertices.size() - 1; i++) {
                indices[index++] = 0;
                indices[index++] = i;
                indices[index++] = i + 1;
            }

            // Indices for the new top face
            int offset = belowPlaneVertices.size();
            for (int i = 0; i < intersectionPoints.size(); i++) {
                indices[index++] = offset;
                indices[index++] = offset + ((i + 1) % intersectionPoints.size());
                indices[index++] = offset + i;
            }

            // Indices for the faces between the base and the new top face
            int belowSize = belowPlaneVertices.size();
            int intersectionSize = intersectionPoints.size();
            for (int i = 0; i < belowSize; i++) {
                indices[index++] = i;
                indices[index++] = (i + 1) % belowSize;
                indices[index++] = offset + (i % intersectionSize);
                indices[index++] = offset + (i % intersectionSize);
                indices[index++] = (i + 1) % belowSize;
                indices[index++] = offset + ((i + 1) % intersectionSize);
            }
        } else {
            vertices = new float[(baseVertexCount + 1) * 6];

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

            indices = new int[baseVertexCount * 3 + (baseVertexCount - 2) * 3];

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
        }

        vaoID = GenerateObjectsUtil.generateVAO();

        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertices.length);
        vertexBuffer.put(vertices).flip();

        vboID = GenerateObjectsUtil.generateVBO(vertexBuffer);

        IntBuffer indexBuffer = BufferUtils.createIntBuffer(indices.length);
        indexBuffer.put(indices).flip();
        numberOfTriangles = indices.length;

        eboID = GenerateObjectsUtil.generateEBO(indexBuffer);

        GenerateObjectsUtil.bindVertexAttributeColor();

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
        glDrawElements(GL_TRIANGLES, numberOfTriangles, GL_UNSIGNED_INT, 0);
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
