package engineTester;

import com.sun.javafx.tk.quantum.MasterTimer;
import objectModels.RawModel;
import objectModels.TexturedModel;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import render.DisplayManager;
import render.Loader;
import textures.ModelTexture;
import entities.Camera;
import entities.Entity;
import entities.Light;
import java.io.File;
import java.util.ArrayList;
import render.OBJLoader;
import render.RendererMaster;
import terrains.Terrain;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

public class MainGameLoop {

    public static void main(String[] args) {

        System.setProperty("org.lwjgl.librarypath", new File("./src/natives").getAbsolutePath());

        DisplayManager.createDisplay();
        Loader loader = new Loader();

        /* SET UP TERRAIN */
        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("mud"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

        /* CONSTRUCT TERRAIN PACK WITH THE GIVEN TERRAINS*/
        TerrainTexturePack terrainTexturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);

        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

        RendererMaster rendererMaster = new RendererMaster();

        ArrayList<Entity> entities = new ArrayList<>();

        RawModel model = OBJLoader.loadObjModel("footballobj", loader);

        TexturedModel staticModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("football"), 10, 0.3f));

        for (int i = 0; i < 300; i++) {
            entities.add(new Entity(staticModel, new Vector3f((float) ((Math.random() * (200 - (-100))) + (-100)), (float) (Math.random() * 70), (float) (-Math.random() * 100)), 0, 0, 0, 1, (float) ((Math.random() * (0.7 - 0.5)) + 0.5)));
        }

        Camera camera = new Camera();
        /**
         * Position of the light, color of the light
         */
        Light light = new Light(new Vector3f(0, 200, 200), new Vector3f(1, 1, 1));

        Terrain terrain = new Terrain(0, -1f, loader, terrainTexturePack, blendMap);
        Terrain terrain2 = new Terrain(-1f, -1f, loader, terrainTexturePack, blendMap);

        float dt = (float) (1 / DisplayManager.FPS_CAP);

        while (!Display.isCloseRequested()) {
            camera.move();
            light.move();
            rendererMaster.processTerrain(terrain);
            rendererMaster.processTerrain(terrain2);
            for (Entity entity : entities) {
                entity.updatePosition();
                rendererMaster.processEntity(entity);
            }
            rendererMaster.render(light, camera);
            DisplayManager.updateDisplay();
        }

        rendererMaster.cleanAll();
        loader.cleanUp();
        DisplayManager.closeDisplay();

    }

}
