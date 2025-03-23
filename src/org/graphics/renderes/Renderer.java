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
    private int vaoID, vboID, eboID;
    private ShaderProgram shaderProgram;
//    private ShapeRenderer[] shapes;

    public void init() {
        shaderProgram = new ShaderProgram("res/shaders/vertexShader.vert", "res/shaders/fragmentShader.frag");

        float[] vertices = {
                // X,     Y,     Z,     R,   G,   B
                -0.5f, -0.2f, 0.0f, 1.0f, 1.0f, 1.0f,
                -0.2f, -0.6f, 0.0f, 1.0f, 1.0f, 0.0f,
                0.2f, -0.6f, 0.0f, 1.0f, 1.0f, 0.0f,
                0.5f, -0.2f, 0.0f, 1.0f, 0.0f, 1.0f,
                0.0f, 0.5f, 0.0f, 1.0f, 0.0f, 1.0f
        };

        int[] indices = {
                0, 1, 4,  // първи триъгълник
                2,        // втори: 1,4,2
                3         // трети: 4,2,3
        };

        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);

        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
        verticesBuffer.put(vertices).flip();
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);

        // Индексен буфер
        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        IntBuffer indexBuffer = BufferUtils.createIntBuffer(indices.length);
        indexBuffer.put(indices).flip();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);


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
        glBindVertexArray(vaoID);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glDrawElements(GL_TRIANGLE_STRIP, 5, GL_UNSIGNED_INT, 0);
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
