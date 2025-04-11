package org.graphics.renderes.shapes;

import org.graphics.utils.GenerateObjectsUtil;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL46.*;

public class PlaneRenderer extends BaseShapeRenderer implements ShapeRenderer {
    private int vaoID, vboID, eboID;
    private Matrix4f modelMatrix;

    private final float width;
    private final float depth;
    private final float[] color;

    public PlaneRenderer(float width, float depth, float[] color) {
        this.width = width;
        this.depth = depth;
        this.color = color;

        calculateModelMatrix();
        calculateVertices();
    }

    @Override
    protected void calculateVertices() {
        float[] vertices = {
                // Position                    Color
                -width / 2, 0.0f, -depth / 2, color[0], color[1], color[2], // Vertex 0
                width / 2, 0.0f, -depth / 2, color[0], color[1], color[2], // Vertex 1
                width / 2, 0.0f, depth / 2, color[0], color[1], color[2], // Vertex 2
                -width / 2, 0.0f, depth / 2, color[0], color[1], color[2]  // Vertex 3
        };

        int[] indices = {
                0, 1, 2,   // First triangle
                2, 3, 0    // Second triangle
        };

        // Convert arrays to buffers
        FloatBuffer vertexBuffer = MemoryUtil.memAllocFloat(vertices.length);
        vertexBuffer.put(vertices).flip();

        IntBuffer indexBuffer = MemoryUtil.memAllocInt(indices.length);
        indexBuffer.put(indices).flip();

        // Generate VAO
        vaoID = GenerateObjectsUtil.generateVAO();

        // Generate VBO
        vboID = GenerateObjectsUtil.generateVBO(vertexBuffer);

        // Generate EBO (Index Buffer)
        eboID = GenerateObjectsUtil.generateEBO(indexBuffer);

        GenerateObjectsUtil.bindVertexAttribute();

        // Unbind VAO/VBO
        GenerateObjectsUtil.unbindObjects();

        // Free memory
        MemoryUtil.memFree(vertexBuffer);
        MemoryUtil.memFree(indexBuffer);
    }

    private void calculateModelMatrix() {
        modelMatrix = new Matrix4f();
        modelMatrix.identity(); // You can add transformations here if needed
    }

    @Override
    public void render() {
        calculateModelMatrix();

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
    }

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
