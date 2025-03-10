package org.graphics.renderes;

import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;

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
                // X, Y, Z      R, G, B
                -0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f,  // Червен връх
                0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 0.0f,  // Зелен връх
                0.0f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f   // Син връх
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

        Matrix4f modelMatrix = new Matrix4f();
        modelMatrix
                .identity()
                .scale(1.2f, 0.7f, 0f)
                .translate(0.3f, 0.9f, 0f);

        shaderProgram.setUniform("modelMatrix", modelMatrix);

        glPointSize(10.0f);
        glBindVertexArray(vaoID);
        glDrawArrays(GL_TRIANGLES, 0, 3);
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
