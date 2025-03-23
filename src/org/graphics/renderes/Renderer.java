package org.graphics.renderes;

import static org.lwjgl.opengl.GL46.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.graphics.renderes.shapes.ShapeRenderer;
import org.graphics.utils.ShaderProgram;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;


public class Renderer {
    @SuppressWarnings("FieldCanBeLocal")
    private int vaoID, vboID;
    private ShaderProgram shaderProgram;
//    private ShapeRenderer[] shapes;

    public void init() {
        shaderProgram = new ShaderProgram("res/shaders/vertexShader.vert", "res/shaders/fragmentShader.frag");

        float[] vertices = {
                // X,     Y,     Z,     R,   G,   B
                0.2f, 0.1f, 0.3f, 1.0f, 1.0f, 1.0f,
                0.2f, 0.1f, 0.3f, 1.0f, 0.0f, 0.0f,
        };

        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);

        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
        verticesBuffer.put(vertices).flip();
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);

        // Атрибут 0 - Позиция (vec3)
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        // Атрибут 1 - Цвят (vec3)
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public void render() {
        shaderProgram.use();

        Matrix4f modelMatrix = new Matrix4f().identity()
                .rotate((float) Math.toRadians(-30), 0, 0, 1);
        shaderProgram.setUniform("modelMatrix", modelMatrix);

        glPointSize(10.0f);
        glBindVertexArray(vaoID);
        glDrawArrays(GL_POINTS, 0, 2);
        glBindVertexArray(0);
    }

    public void cleanup() {
//        for (ShapeRenderer shape : shapes) {
//            shape.cleanup();
//        }
    }

    public void updateAspectRation(int width, int height) {
//        for (ShapeRenderer shape : shapes) {
//            shape.updateAspectRation(width, height);
//        }
    }
}
