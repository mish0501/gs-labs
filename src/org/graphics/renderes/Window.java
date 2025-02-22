package org.graphics.renderes;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

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
        if(!GLFW.glfwInit()){
            throw new IllegalStateException("Грешка при инициализацията на GLFW!");
        }

        window = GLFW.glfwCreateWindow(width, height, title, 0, 0);

        if(window == 0){
            throw new IllegalStateException("Грешка при създаване на прозореца!");
        }

        GLFW.glfwMakeContextCurrent(window);

        GL.createCapabilities();
        GL11.glViewport(0, 0, width, height);
        GL11.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);

        renderer = new Renderer();
        renderer.init();
    }

    private void loop() {
        while(!GLFW.glfwWindowShouldClose(window)){
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

            renderer.render();

            GLFW.glfwSwapBuffers(window);
            GLFW.glfwPollEvents();
        }
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
