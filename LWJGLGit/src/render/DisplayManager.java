package render;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager {

    private static final int WIDTH = 1080;
    private static final int HEIGHT = 720;
    public static final int FPS_CAP = 120;
    private static long lastFrameTime;
    private static float deltha;

    public static void createDisplay() {
        ContextAttribs attribs = new ContextAttribs(3, 2)
                .withForwardCompatible(true)
                .withProfileCore(true);

        try {
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.create(new PixelFormat(), attribs);
            Display.setTitle("Bouncing Balls");
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        GL11.glViewport(0, 0, WIDTH, HEIGHT);
        lastFrameTime = getCurrentTime();
    }

    public static void updateDisplay() {

        Display.sync(FPS_CAP);
        Display.update();
        long currentTime = getCurrentTime();
        /**
         * Difference in seconds between two real timestamps
         */
        deltha = (currentTime - lastFrameTime)/1000f;
        
        /**
         * Update lastFrameTime
         */
        lastFrameTime = currentTime;

    }

    /**
     *
     * @return Elapsed time in seconds
     */
    private static long getCurrentTime() {
        return Sys.getTime() * 1000 / Sys.getTimerResolution();
    }

    public static float getDeltha() {
        return deltha;
    }

    public static void closeDisplay() {

        Display.destroy();

    }

}
