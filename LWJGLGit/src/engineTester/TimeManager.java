package engineTester;

import org.lwjgl.Sys;

/**
 *
 * @author Gabriel Huertas
 */
public class TimeManager {

    private long lastFrameTime;
    private long deltha;
    private long currentFrameTime;

    public TimeManager() {
        lastFrameTime = 0;
        deltha = 0;
        currentFrameTime = 0;
    }

    /**
     *
     * @return Elapsed time in seconds
     */
    private static long getCurrentTime() {
        return Sys.getTime() * 1000 / Sys.getTimerResolution();
    }

    public  long getLastFrameTime() {
        return lastFrameTime;
    }

    public  void setLastFrameTime(long lastFrameTime) {
        this.lastFrameTime = lastFrameTime;
    }

    public  long getDeltha() {
        return deltha;
    }

    public  void setDeltha(long deltha) {
       this.deltha = deltha;
    }

    public long getCurrentFrameTime() {
        return currentFrameTime;
    }

    public void setCurrentFrameTime(long currentFrameTime) {
        this.currentFrameTime = currentFrameTime;
    }

}
