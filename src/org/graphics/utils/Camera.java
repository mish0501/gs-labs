package org.graphics.utils;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
    private final Vector3f position;
    private Vector3f front;
    private final Vector3f up;

    private float yaw;
    private float pitch;

    private final float movementSpeed;
    private final float mouseSensitivity;

    public Camera(Vector3f position, Vector3f up, float yaw, float pitch) {
        this.position = position;
        this.front = new Vector3f(0.0f, 0.0f, -1.0f).normalize(); // Default look direction
        this.up = up;
        this.yaw = yaw;
        this.pitch = pitch;
        this.movementSpeed = 20f; // Units per second
        this.mouseSensitivity = 0.1f;

        updateCameraVectors();
    }

    public void processKeyboard(CameraMovement direction, float deltaTime) {
        float velocity = movementSpeed * deltaTime;
        if (direction == CameraMovement.FORWARD)
            position.add(new Vector3f(front).mul(velocity));
        if (direction == CameraMovement.BACKWARD)
            position.sub(new Vector3f(front).mul(velocity));
        if (direction == CameraMovement.LEFT) {
            Vector3f right = new Vector3f();
            front.cross(up, right).normalize();
            position.sub(right.mul(velocity));
        }
        if (direction == CameraMovement.RIGHT) {
            Vector3f right = new Vector3f();
            front.cross(up, right).normalize();
            position.add(right.mul(velocity));
        }
        if (direction == CameraMovement.UP) {
            position.add(new Vector3f(up).mul(velocity));
        }
        if (direction == CameraMovement.DOWN) {
            position.sub(new Vector3f(up).mul(velocity));
        }
    }

    public void processMouseMovement(float xOffset, float yOffset, boolean constrainPitch) {
        yaw += xOffset * mouseSensitivity;
        pitch += yOffset * mouseSensitivity;

        if (constrainPitch) {
            if (pitch > 89.0f)
                pitch = 89.0f;
            if (pitch < -89.0f)
                pitch = -89.0f;
        }

        updateCameraVectors();
    }

    private void updateCameraVectors() {
        Vector3f frontVec = new Vector3f();
        frontVec.x = (float) (Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
        frontVec.y = (float) Math.sin(Math.toRadians(pitch));
        frontVec.z = (float) (Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
        front = frontVec.normalize();
    }

    public Matrix4f getViewMatrix() {
        return new Matrix4f().lookAt(position, new Vector3f(position).add(front), up);
    }

    public enum CameraMovement {
        FORWARD,
        BACKWARD,
        LEFT,
        RIGHT,
        UP,
        DOWN
    }
}
