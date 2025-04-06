package org.graphics.renderes;

import org.graphics.utils.GenerateObjectsUtil;
import org.graphics.utils.ShaderProgram;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;


public class Renderer {
    private ShaderProgram shaderProgram;
    private int vaoID, vboID;

    private static final int POINTS = 100;
    private int controlPointsCount;

    public void init() {
        shaderProgram = new ShaderProgram("res/shaders/vertexShader.vert", "res/shaders/fragmentShader.frag");

        float[] vertices = getVertices();


        vaoID = GenerateObjectsUtil.generateVAO();

        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertices.length);
        vertexBuffer.put(vertices).flip();

        vboID = GenerateObjectsUtil.generateVBO(vertexBuffer);

        GenerateObjectsUtil.bindVertexAttribute();

        GenerateObjectsUtil.unbindObjects();
    }

    public void render() {
        shaderProgram.use();

        glBindVertexArray(vaoID);

        glDrawArrays(GL_LINE_STRIP, 0, POINTS);
        glPointSize(10);
        glDrawArrays(GL_POINTS, POINTS, controlPointsCount);


        glBindVertexArray(0);
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
//
//        aspectRatio = (float) width / height;
    }

    private float[] deCasteljau(float t, float[][] points) {
        if (points.length == 1) return points[0];

        float[][] next = new float[points.length - 1][2];
        for (int i = 0; i < next.length; i++) {
            next[i][0] = (1 - t) * points[i][0] + t * points[i + 1][0];
            next[i][1] = (1 - t) * points[i][1] + t * points[i + 1][1];
        }

        return deCasteljau(t, next);
    }

    private float[] getVertices() {
        final int stride = 6; // x, y, z, r, g, b
        final float z = 0.0f;
        final float[] curveColor = {0.0f, 1.0f, 0.0f};
        final float[] ctrlColor = {1.0f, 0.0f, 0.0f};

        float[][] controlPoints = {
                {-0.9f, -0.6f},
                {-0.3f, 0.9f},
                {0.4f, 0.5f},
                {0.3f, -0.9f},
                {0.8f, -0.3f}
        };
        controlPointsCount = controlPoints.length;

        float[] vertices = new float[(POINTS + controlPoints.length) * stride];

        // Генерираме точки по кривата на Безие
        for (int i = 0; i < POINTS; i++) {
            float t = i / (float) (POINTS - 1);
            float[] point = deCasteljau(t, controlPoints);

            int offset = i * stride;
            vertices[offset] = point[0];
            vertices[offset + 1] = point[1];
            vertices[offset + 2] = z;
            vertices[offset + 3] = curveColor[0];
            vertices[offset + 4] = curveColor[1];
            vertices[offset + 5] = curveColor[2];
        }

        // Добавяме контролни точки в края
        for (int i = 0; i < controlPoints.length; i++) {
            int offset = (POINTS + i) * stride;
            vertices[offset] = controlPoints[i][0];
            vertices[offset + 1] = controlPoints[i][1];
            vertices[offset + 2] = z;
            vertices[offset + 3] = ctrlColor[0];
            vertices[offset + 4] = ctrlColor[1];
            vertices[offset + 5] = ctrlColor[2];
        }

        return vertices;
    }

}
