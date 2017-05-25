package entities;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

/**
 * CREATE A 3RD PERSON CAMERA THAT ALLOWS TO CONTROL THE DISTANCE FROM THE
 * CAMERA TO THE PLAYER, TO CONTROL THE YAW
 *
 * @author Invitado
 */
public class Camera {

    private float distanceFromPlayer;
    private float angleAroundPlayer;

    private Vector3f position = new Vector3f(0, 0, 0);
    /**
     * Rotation around x, y and z axis;
     */
    private float pitch;
    private float yaw;
    private float roll;

    private Player player;

    public Camera(Player player) {
        this.player = player;
        distanceFromPlayer = 50;
        angleAroundPlayer = 0;
        pitch = 20;
        yaw = 0;
    }

    public void move() {
        calculateZoom();
        calculatePitch();
        calculteAngleAroundPlayer();

        float horizontalDistance = calculateHorizontalDistance();
        float verticalDistance = calculateVerticalDistance();
        calculateCameraPosition(horizontalDistance, verticalDistance);
        this.yaw=180-(player.getRotY()+angleAroundPlayer);
        /*  if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            position.z -= 0.5f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            position.x += 0.5f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            position.x -= 0.5f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            position.z += 0.5f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_H)) {
            position.y += 0.5f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_L)) {
            if (position.y - 0.5f > 1) {
                position.y -= 0.5f;
            }

        }
         */
    }

    /**
     * Calculates the distance from the camera's position to the player's one
     */
    public void calculateZoom() {
        
        /**
         * Adjust zoom in/out to make it more controllable
         */
        distanceFromPlayer -= Mouse.getDWheel() * 0.01f;;
    }

    /**
     * Calculate the depression angle between an horizontal plane and the line
     * of sight
     */
    public void calculatePitch() {
        //Check if right click button is pressed
        if (Mouse.isButtonDown(1)) {
            /**
             * Check how much the mouse has moved to right and down
             */ 
            pitch -= Mouse.getDY() * 0.1f;
        }
    }

    /**
     * Calculate the distance from the player to the camera around the X axis
     */
    public void calculteAngleAroundPlayer() {
        if (Mouse.isButtonDown(0)) {
            /**
             * Check how much the mouse has moved to left and right
             */
             
            angleAroundPlayer -= Mouse.getDX() * 0.3f;
        }
    }

    private void calculateCameraPosition(float horizontalDistance, float verticalDistance) {
        float theta = player.getRotY() + angleAroundPlayer;
        float offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
        float offsetZ = (float) (verticalDistance * Math.cos(Math.toRadians(theta)));
        position.x = player.getRotX() - offsetX;
        position.y = player.getPosition().y + verticalDistance;
        position.z = player.getRotZ() - offsetZ;
    }

    private float calculateHorizontalDistance() {
        return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
    }

    private float calculateVerticalDistance() {
        return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }
}
