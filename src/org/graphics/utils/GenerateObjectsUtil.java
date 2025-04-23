package org.graphics.utils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL46.*;

public class GenerateObjectsUtil {
    public static int generateVAO() {
        int vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        return vaoID;
    }

    public static void bindVertexAttributeColor(){
        // Атрибут 0 - Позиция (vec3)
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        // Атрибут 1 - Цвят (vec3)
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);
    }

    public static void bindVertexAttributeTexture(){
        // Атрибут 0 - Позиция (vec3)
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        // Атрибут 1 - Цвят (vec3)
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);
    }

    public static int generateVBO(FloatBuffer vertexBuffer) {
        int vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        return vboID;
    }

    public static int generateEBO(IntBuffer indexBuffer) {
        int eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);

        return eboID;
    }

    public static void unbindObjects() {
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }
}
