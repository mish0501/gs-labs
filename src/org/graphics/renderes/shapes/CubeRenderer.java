package org.graphics.renderes.shapes;

import org.graphics.utils.GenerateObjectsUtil;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL30.*;

public class CubeRenderer extends BaseShapeRenderer implements ShapeRenderer {
    private int vaoID, vboID, eboID;

    private final float[] topLeft, bottomRight, frontFaceColor, backFaceColor, leftFaceColor, rightFaceColor, bottomFaceColor, topFaceColor;

    private Matrix4f modelMatrix;

    public CubeRenderer(float[] topLeft, float[] bottomRight, float[] frontFaceColor, float[] backFaceColor, float[] leftFaceColor, float[] rightFaceColor, float[] bottomFaceColor, float[] topFaceColor) {
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
        this.frontFaceColor = frontFaceColor;
        this.backFaceColor = backFaceColor;
        this.leftFaceColor = leftFaceColor;
        this.rightFaceColor = rightFaceColor;
        this.bottomFaceColor = bottomFaceColor;
        this.topFaceColor = topFaceColor;

        calculateModelMatrix();
        calculateVertices();
    }

    @Override
    protected void calculateVertices() {
        float[] vertices = {
                // Back face
                topLeft[0], topLeft[1], topLeft[2], backFaceColor[0], backFaceColor[1], backFaceColor[2],
                bottomRight[0], topLeft[1], topLeft[2], backFaceColor[0], backFaceColor[1], backFaceColor[2],
                bottomRight[0], bottomRight[1], topLeft[2], backFaceColor[0], backFaceColor[1], backFaceColor[2],
                topLeft[0], bottomRight[1], topLeft[2], backFaceColor[0], backFaceColor[1], backFaceColor[2],

                // Front face
                topLeft[0], topLeft[1], bottomRight[2], frontFaceColor[0], frontFaceColor[1], frontFaceColor[2],
                bottomRight[0], topLeft[1], bottomRight[2], frontFaceColor[0], frontFaceColor[1], frontFaceColor[2],
                bottomRight[0], bottomRight[1], bottomRight[2], frontFaceColor[0], frontFaceColor[1], frontFaceColor[2],
                topLeft[0], bottomRight[1], bottomRight[2], frontFaceColor[0], frontFaceColor[1], frontFaceColor[2],

                // Left face
                topLeft[0], topLeft[1], topLeft[2], leftFaceColor[0], leftFaceColor[1], leftFaceColor[2],
                topLeft[0], topLeft[1], bottomRight[2], leftFaceColor[0], leftFaceColor[1], leftFaceColor[2],
                topLeft[0], bottomRight[1], bottomRight[2], leftFaceColor[0], leftFaceColor[1], leftFaceColor[2],
                topLeft[0], bottomRight[1], topLeft[2], leftFaceColor[0], leftFaceColor[1], leftFaceColor[2],

                // Right face
                bottomRight[0], topLeft[1], topLeft[2], rightFaceColor[0], rightFaceColor[1], rightFaceColor[2],
                bottomRight[0], topLeft[1], bottomRight[2], rightFaceColor[0], rightFaceColor[1], rightFaceColor[2],
                bottomRight[0], bottomRight[1], bottomRight[2], rightFaceColor[0], rightFaceColor[1], rightFaceColor[2],
                bottomRight[0], bottomRight[1], topLeft[2], rightFaceColor[0], rightFaceColor[1], rightFaceColor[2],

                // Top face
                topLeft[0], topLeft[1], topLeft[2], topFaceColor[0], topFaceColor[1], topFaceColor[2],
                bottomRight[0], topLeft[1], topLeft[2], topFaceColor[0], topFaceColor[1], topFaceColor[2],
                bottomRight[0], topLeft[1], bottomRight[2], topFaceColor[0], topFaceColor[1], topFaceColor[2],
                topLeft[0], topLeft[1], bottomRight[2], topFaceColor[0], topFaceColor[1], topFaceColor[2],

                // Bottom face
                topLeft[0], bottomRight[1], topLeft[2], bottomFaceColor[0], bottomFaceColor[1], bottomFaceColor[2],
                bottomRight[0], bottomRight[1], topLeft[2], bottomFaceColor[0], bottomFaceColor[1], bottomFaceColor[2],
                bottomRight[0], bottomRight[1], bottomRight[2], bottomFaceColor[0], bottomFaceColor[1], bottomFaceColor[2],
                topLeft[0], bottomRight[1], bottomRight[2], bottomFaceColor[0], bottomFaceColor[1], bottomFaceColor[2]
        };

        int[] indices = {
                0, 1, 2, 2, 3, 0,  // Back
                4, 5, 6, 6, 7, 4,  // Front
                8, 9, 10, 10, 11, 8,  // Left
                12, 13, 14, 14, 15, 12,  // Right
                16, 17, 18, 18, 19, 16,  // Bottom
                20, 21, 22, 22, 23, 20   // Top
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

        GenerateObjectsUtil.bindVertexAttributeColor();

        // Unbind VAO/VBO
        GenerateObjectsUtil.unbindObjects();

        // Free memory
        MemoryUtil.memFree(vertexBuffer);
        MemoryUtil.memFree(indexBuffer);
    }

    private void calculateModelMatrix() {
        float angle = (float) Math.toRadians(25);

        modelMatrix = new Matrix4f();
        modelMatrix.identity()
                .rotate(angle, 0, 1, 0);
    }

    @Override
    public void render() {
        calculateModelMatrix();

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glDrawElements(GL_TRIANGLES, 36, GL_UNSIGNED_INT, 0);
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
