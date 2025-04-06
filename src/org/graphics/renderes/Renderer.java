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

    public void init() {
        shaderProgram = new ShaderProgram("res/shaders/vertexShader.vert", "res/shaders/fragmentShader.frag");

        float[] vertices = generateLagrangeCurveVertices();


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

        glPointSize(10);
        glDrawArrays(GL_POINTS, 0, 3);
        glDrawArrays(GL_LINE_STRIP, 3, POINTS);

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

    private float[] generateLagrangeCurveVertices() {
        float[][] basePoints = {
                {-0.8f, 0.3f},
                {0.0f, -0.1f},
                {0.6f, 0.2f},
        };

        List<Float> data = new ArrayList<>();

        // Визуализираме и базовите точки, за да проследим дали наистина лежат на кривата
        for (float[] point : basePoints) {
            data.add(point[0]); // x
            data.add(point[1]); // y
            data.add(0.0f);     // z
            data.add(1.0f);     // r
            data.add(0.0f);     // g
            data.add(0.0f);     // b
        }

        for (int i = 0; i < POINTS; i++) {
            float x = -0.9f + i * (1.8f / (POINTS - 1));  // равномерно по x в интервала [-0.9, 0.9]
            float y = 0.0f;

            for (int j = 0; j < basePoints.length; j++) {
                float Lj = 1.0f;
                for (int k = 0; k < basePoints.length; k++) {
                    if (j != k) {
                        Lj *= (x - basePoints[k][0]) / (basePoints[j][0] - basePoints[k][0]);
                    }
                }
                y += basePoints[j][1] * Lj;
            }

            data.add(x);
            data.add(y);
            data.add(0.0f);     // z
            data.add(0.0f);     // r
            data.add(1.0f);     // g
            data.add(0.0f);     // b
        }

        float[] array = new float[data.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = data.get(i);
        }
        return array;
    }

}
