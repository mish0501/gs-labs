package org.graphics.renderes;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL46.*;

public class Window {
    private long window;
    private final int width, height;
    private final String title;
    private Renderer renderer;

    public Window(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
    }

    private void init() {
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Грешка при инициализацията на GLFW!");
        }

        window = GLFW.glfwCreateWindow(width, height, title, 0, 0);

        if (window == 0) {
            throw new IllegalStateException("Грешка при създаване на прозореца!");
        }

        GLFW.glfwMakeContextCurrent(window);

        GL.createCapabilities();
        glViewport(0, 0, width, height);
        glClearColor(0.1f, 0.1f, 0.1f, 1.0f);

        renderer = new Renderer();
        renderer.init();
        renderer.updateAspectRatio(width, height);
    }

    private void loop() {
        while (!GLFW.glfwWindowShouldClose(window)) {
            glEnable(GL_DEPTH_TEST);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            renderer.render();

            GLFW.glfwSwapBuffers(window);
            GLFW.glfwPollEvents();
        }

        renderer.cleanup();
    }

    private void cleanup() {
        GLFW.glfwDestroyWindow(window);
        GLFW.glfwTerminate();
    }

    public void run() {
        init();
        loop();
        cleanup();
    }
}
