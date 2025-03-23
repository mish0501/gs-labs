package org.graphics;

import org.graphics.renderes.Window;

public class Main {
    public static void main(String[] args) {
        Window window = new Window(800, 800, "OpenGL with Shaders");
        window.run();
    }
}
