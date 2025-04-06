package org.graphics.renderes;

import org.graphics.utils.GenerateObjectsUtil;
import org.graphics.utils.ShaderProgram;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;


public class Renderer {
    private ShaderProgram shaderProgram;
    private int vaoID, vboID;

    private static final int DEGREE = 3; // Степен на B-spline
    private static final int POINTS_PER_SEGMENT = 20;

    private int controlPointCount; // Брой на контролни точки

    // Брой на кривите сегменти
    private int totalCurveVertices = 0;

    public void init() {
        shaderProgram = new ShaderProgram("res/shaders/vertexShader.vert", "res/shaders/fragmentShader.frag");

        float[][] controlPoints = {
                {-0.8f, -0.6f},
                {-0.4f, 0.8f},
                {0.0f, -0.4f},
                {0.4f, 0.6f},
                {0.8f, -0.5f}
        };
        controlPointCount = controlPoints.length;

        float[] vertices = generateBSplineVertices(controlPoints);

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

        // Рисуваме кривата
        glDrawArrays(GL_LINE_STRIP, 0, totalCurveVertices);

        // Рисуваме контролните точки
        glPointSize(10);
        glDrawArrays(GL_POINTS, totalCurveVertices, controlPointCount);


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

    private float[] generateBSplineVertices(float[][] ctrl) {
        int n = ctrl.length - 1;
        int knotCount = n + DEGREE + 2;
        float[] knot = new float[knotCount];

        // Генериране на равномерно разпределен clamped knot вектор
        for (int i = 0; i < knotCount; i++) {
            if (i <= DEGREE) {
                knot[i] = 0;
                continue;
            }

            if (i >= knotCount - DEGREE - 1) {
                knot[i] = 1;
                continue;
            }

            knot[i] = (float) (i - DEGREE) / (knotCount - 2 * DEGREE - 1);
        }

        int segments = n - DEGREE + 1;
        totalCurveVertices = segments * POINTS_PER_SEGMENT;

        float[] vertices = new float[(totalCurveVertices + ctrl.length) * 6];
        int vIndex = 0;

        for (int i = DEGREE; i < knot.length - 1; i++) {
            float tStart = knot[i];
            float tEnd = knot[i + 1];

            if (tStart == tEnd) continue;

            for (int j = 0; j < POINTS_PER_SEGMENT; j++) {
                float t = tStart + (tEnd - tStart) * j / (POINTS_PER_SEGMENT - 1);
                float[] pt = deBoor(t, ctrl, knot);

                // Добавяме позиция
                vertices[vIndex++] = pt[0];
                vertices[vIndex++] = pt[1];
                vertices[vIndex++] = 0.0f;

                // Добавяме цвят (зелен)
                vertices[vIndex++] = 0.0f;
                vertices[vIndex++] = 1.0f;
                vertices[vIndex++] = 0.0f;
            }
        }

        // Контролните точки – в червено
        for (float[] p : ctrl) {
            vertices[vIndex++] = p[0];
            vertices[vIndex++] = p[1];
            vertices[vIndex++] = 0.0f;

            vertices[vIndex++] = 1.0f;
            vertices[vIndex++] = 0.0f;
            vertices[vIndex++] = 0.0f;
        }

        return vertices;
    }

    private float[] deBoor(float t, float[][] ctrl, float[] knot) {
        int n = ctrl.length - 1;
        int m = knot.length - 1;

        int s = DEGREE;
        while (s < m - 1 && t >= knot[s + 1]) s++;

        float[][] d = new float[DEGREE + 1][2];

        for (int j = 0; j <= DEGREE; j++) {
            int index = s - DEGREE + j;
            if (index < 0) index = 0;
            if (index > n) index = n;

            d[j][0] = ctrl[index][0];
            d[j][1] = ctrl[index][1];
        }

        for (int r = 1; r <= DEGREE; r++) {
            for (int j = DEGREE; j >= r; j--) {
                int i1 = s - DEGREE + j;
                int i2 = s + 1 + j - r;

                // Ограничаваме достъпа до допустими граници
                if (i1 < 0) i1 = 0;
                if (i1 >= knot.length) i1 = knot.length - 1;
                if (i2 < 0) i2 = 0;
                if (i2 >= knot.length) i2 = knot.length - 1;

                float denom = knot[i2] - knot[i1];
                float alpha = denom == 0 ? 0 : (t - knot[i1]) / denom;

                d[j][0] = (1 - alpha) * d[j - 1][0] + alpha * d[j][0];
                d[j][1] = (1 - alpha) * d[j - 1][1] + alpha * d[j][1];
            }
        }

        return d[DEGREE];
    }
}
