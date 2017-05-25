package entities;

import objectModels.TexturedModel;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;
import render.DisplayManager;

/**
 *
 * @author Gabriel Huertas
 */
public class Player extends Entity {

    /**
     * The speed towards a direction in 3D space
     */
    private static final float RUN_SPEED = 10;
    /**
     * The speed to pitch, roll or yaw
     */
    private static final float TURN_SPEED = 80;
    private static final float GRAVITY = -50;
    /**
     * Determinates the maximum height of the jump
     */
    private static final float JUMP_POWER = 30;
    /**
     * Terrain Height (For this purposes the terrain is flat and the Y cord of
     * the suface is set to zero)
     */
    private static final float TERRAIN_HEIGHT = 0;

    private float currentSpeed;
    private float currentTurnSpeed;
    /**
     * Downfall speed
     */
    private float upwardsSpeed;
    /**
     * Check if player is in the ai
     */
    private boolean isInTheAir;

    public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale, float elasticity) {
        super(model, position, rotX, rotY, rotZ, scale, elasticity);
        currentSpeed = 0;
        currentTurnSpeed = 0;
        isInTheAir = false;
    }

    public void movePlayer() {
        checkInputs();
        /**
         * Do real-time translation and rotation
         */
        super.increaseRotation(0, currentTurnSpeed * DisplayManager.getDeltha(), 0);
        /**
         * Distance corresponds to the hipotenuse of a triangle formed by two
         * catetus which represents pointing vectors towards x and z axis
         * displacements
         *
         */
        float distance = currentSpeed * DisplayManager.getDeltha();
        float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
        float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));

        /**
         * Update position after doing translation
         */
        super.increasePosition(dx, 0, dz);
        upwardsSpeed += GRAVITY * DisplayManager.getDeltha();
        super.increasePosition(0, upwardsSpeed * DisplayManager.getDeltha(), 0);
        if (super.getPosition().y < TERRAIN_HEIGHT) {
            upwardsSpeed = 0;
            super.getPosition().y = TERRAIN_HEIGHT;
            isInTheAir = false;
        }

    }

    private void jump() {
        upwardsSpeed = JUMP_POWER;
        isInTheAir = true;
    }

    /**
     * Manage input from keybord to move forward or backwards, to rotate
     * clockwise or anticlockwise
     */
    public void checkInputs() {
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            currentSpeed = RUN_SPEED;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            currentSpeed = -RUN_SPEED;
        } else {
            currentSpeed = 0;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            currentTurnSpeed = -TURN_SPEED;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            currentTurnSpeed = TURN_SPEED;
        } else {
            currentTurnSpeed = 0;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) && !isInTheAir) {
            jump();
        }
    }

}
