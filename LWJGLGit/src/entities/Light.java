
package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Gabriel Huertas
 */
public class Light {

    private Vector3f position;
    private Vector3f color;

    public Light(Vector3f position, Vector3f color) {
        this.position = position;
        this.color = color;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }

    public void move() {
        if (Keyboard.isKeyDown(Keyboard.KEY_M)) {
            position.z -= 10.5f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_N)) {
            position.x += 10.5f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_B)) {
            position.x -= 10.5f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_V)) {
            position.z += 10.5f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_C)) {
            position.y += 10.5f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_O)) {
            if (position.y - 0.5f > 1) {
                position.y -= 0.5f;
            }

        }

    }

}
