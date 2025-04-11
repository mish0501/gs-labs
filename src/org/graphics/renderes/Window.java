package org.graphics.renderes;

import static org.lwjgl.opengl.GL46.*;

import org.graphics.utils.Camera;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.opengl.GL;
import org.joml.Vector3f;

public class Window {
    private long window;
    private final int width, height;
    private final String title;
    private Renderer renderer;

    private Camera camera;

    private float lastX;
    private float lastY;
    private boolean cursorEnabled = false;

    private GLFWKeyCallback keyCallback;
    private GLFWCursorPosCallback cursorPosCallback;

    // Variables for calculating delta time
    private double lastFrameTime;

    // Key press states
    private boolean wPressed = false;
    private boolean aPressed = false;
    private boolean sPressed = false;
    private boolean dPressed = false;
    private boolean spacePressed = false;
    private boolean ctrlPressed = false;

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

        // Initialize lastX and lastY *after* width and height are set
        lastX = width / 2.0f;
        lastY = height / 2.0f;

        // Initialize the camera
        camera =
                new Camera(
                        new Vector3f(0.0f, 1.0f, 3.0f),
                        new Vector3f(0.0f, 1.0f, 0.0f),
                        -90.0f,
                        0.0f);

        renderer = new Renderer();
        renderer.init();
        renderer.updateAspectRatio(width, height);
        renderer.setCamera(camera); // Pass the camera to the renderer

        // Input callbacks
        setupInputCallbacks();
        updateCursorMode();

        // Initialize lastFrameTime
        lastFrameTime = GLFW.glfwGetTime();
    }

    private void setupInputCallbacks() {
        keyCallback =
                GLFWKeyCallback.create(
                        (_, key, _, action, _) -> {
                            if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_PRESS) {
                                cursorEnabled = !cursorEnabled;
                                updateCursorMode();
                            }

                            // Set key press flags
                            if (key == GLFW.GLFW_KEY_W) {
                                wPressed = (action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT);
                            }
                            if (key == GLFW.GLFW_KEY_S) {
                                sPressed = (action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT);
                            }
                            if (key == GLFW.GLFW_KEY_A) {
                                aPressed = (action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT);
                            }
                            if (key == GLFW.GLFW_KEY_D) {
                                dPressed = (action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT);
                            }
                            if (key == GLFW.GLFW_KEY_SPACE) {
                                spacePressed = (action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT);
                            }
                            if (key == GLFW.GLFW_KEY_LEFT_CONTROL) {
                                ctrlPressed = (action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT);
                            }
                        });

        cursorPosCallback =
                GLFWCursorPosCallback.create(
                        (_, xpos, ypos) -> {
                            if (!cursorEnabled) { // Only process mouse movement when cursor is disabled
                                float xoffset = (float) xpos - lastX;
                                float yoffset = lastY - (float) ypos; // Reversed since y-coordinates range from
                                // bottom to top
                                lastX = (float) xpos;
                                lastY = (float) ypos;

                                camera.processMouseMovement(xoffset, yoffset, true);
                            }
                        });

        GLFW.glfwSetKeyCallback(window, keyCallback);
        GLFW.glfwSetCursorPosCallback(window, cursorPosCallback);
    }

    private void updateCursorMode() {
        if (cursorEnabled) {
            GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
        } else {
            GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
            // Snap the mouse to the center of the window
            GLFW.glfwSetCursorPos(window, width / 2.0, height / 2.0);
            lastX = width / 2.0f;
            lastY = height / 2.0f;
        }
    }

    private void loop() {
        while (!GLFW.glfwWindowShouldClose(window)) {
            glEnable(GL_DEPTH_TEST);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            // Process Input
            processInput();

            // Render
            renderer.render();

            GLFW.glfwSwapBuffers(window);
            GLFW.glfwPollEvents();

            // Update delta time
            getDeltaTime();
        }

        renderer.cleanup();
    }

    private void processInput() {
        double deltaTime = getDeltaTime();

        if (wPressed) {
            camera.processKeyboard(Camera.CameraMovement.FORWARD, (float) deltaTime);
        }
        if (sPressed) {
            camera.processKeyboard(Camera.CameraMovement.BACKWARD, (float) deltaTime);
        }
        if (aPressed) {
            camera.processKeyboard(Camera.CameraMovement.LEFT, (float) deltaTime);
        }
        if (dPressed) {
            camera.processKeyboard(Camera.CameraMovement.RIGHT, (float) deltaTime);
        }
        if (spacePressed) {
            camera.processKeyboard(Camera.CameraMovement.UP, (float) deltaTime);
        }
        if (ctrlPressed) {
            camera.processKeyboard(Camera.CameraMovement.DOWN, (float) deltaTime);
        }
    }

    private double getDeltaTime() {
        double currentFrameTime = GLFW.glfwGetTime();
        double deltaTime = currentFrameTime - lastFrameTime;
        lastFrameTime = currentFrameTime;
        return deltaTime;
    }

    private void cleanup() {
        // Free the window callbacks and destroy the window
        if (keyCallback != null) {
            keyCallback.free();
        }
        if (cursorPosCallback != null) {
            cursorPosCallback.free();
        }

        GLFW.glfwDestroyWindow(window);
        GLFW.glfwTerminate();
    }

    public void run() {
        init();
        loop();
        cleanup();
    }
}
