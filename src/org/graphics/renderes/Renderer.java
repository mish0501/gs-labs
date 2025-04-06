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
        // Базовите точки
        float[] p0 = {-0.8f, 0.3f, 0.0f, 1.0f, 0.0f, 0.0f};
        float[] p1 = {0.0f, -0.1f, 0.0f, 1.0f, 0.0f, 0.0f};
        float[] p2 = {0.6f, 0.2f, 0.0f, 1.0f, 0.0f, 0.0f};

        List<Float> data = new ArrayList<>();

        //Да визуализираме и базовите точки, за да се убедим,
        // че в последствие те лежат на кривата
        for (float coord : p0) {
            data.add(coord);
        }

        for (float coord : p1) {
            data.add(coord);
        }

        for (float coord : p2) {
            data.add(coord);
        }

        // точките от кривата
        for (int i = 0; i < POINTS; i++) {
            float x = -0.9f + i * (1.8f / (POINTS - 1)); // да се изобразят в интервала [-0.9, 0.9]

            float l0 = (x - p1[0]) * (x - p2[0]) / ((p0[0] - p1[0]) * (p0[0] - p2[0]));
            float l1 = (x - p0[0]) * (x - p2[0]) / ((p1[0] - p0[0]) * (p1[0] - p2[0]));
            float l2 = (x - p0[0]) * (x - p1[0]) / ((p2[0] - p0[0]) * (p2[0] - p1[0]));

            float y = p0[1] * l0 + p1[1] * l1 + p2[1] * l2;

            // позиция (x, y, z) + цвят (R,G,B)
            data.add(x);
            data.add(y);
            data.add(0.0f);      // Z
            data.add(0.0f);      // R
            data.add(1.0f);      // G
            data.add(0.0f);      // B
        }

        float[] array = new float[data.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = data.get(i);
        }
        return array;
    }
}
