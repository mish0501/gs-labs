package org.graphics.renderes;

import org.graphics.utils.ShaderProgram;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;


public class Renderer {
    @SuppressWarnings("FieldCanBeLocal")
    private int vaoID, vboID;
    private ShaderProgram shaderProgram;
//    private ShapeRenderer[] shapes;

    public void init() {
        shaderProgram = new ShaderProgram("res/shaders/vertexShader.vert", "res/shaders/fragmentShader.frag");

        float[] vertices = {
                // X, Y, Z      R, G, B
                -0.025f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f,
                0.025f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f,
                -0.025f, -1.0f, 0.0f, 1.0f, 1.0f, 1.0f,
                0.025f, -1.0f, 0.0f, 1.0f, 1.0f, 1.0f,


                // Red
                -0.025f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
                0.025f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
                -0.4f, -0.45f, 0.0f, 1.0f, 0.0f, 0.0f,

                // Blue
                -0.025f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f,
                0.025f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f,
                0.0f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f,

                // Green
                -0.025f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f,
                0.025f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f,
                0.4f, -0.45f, 0.0f, 0.0f, 1.0f, 0.0f,
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

    private float angle = 0;

    public void render() {
        shaderProgram.use();

        angle += 0.05f;

        if(angle > 360) {
            angle = 0;
        }

        Matrix4f modelMatrix = new Matrix4f();
        modelMatrix.identity()
                .rotate((float) Math.toRadians(angle), 0f, 0f, 1f);

        shaderProgram.setUniform("modelMatrix", modelMatrix);

        glBindVertexArray(vaoID);
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
        glDrawArrays(GL_TRIANGLES, 4, 9);
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
