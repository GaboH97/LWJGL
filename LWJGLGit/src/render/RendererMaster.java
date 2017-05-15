package render;

import entities.Camera;
import entities.Entity;
import entities.Light;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import objectModels.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import shaders.StaticShader;
import shaders.TerrainShader;
import terrains.Terrain;

/**
 *
 * @author Gabriel Huertas
 */
public class RendererMaster {

    /**
     * Rendering tools
     */
    private StaticShader shader;
    private Renderer renderer;
    private TerrainShader terrainShader;
    private TerrainRenderer terrainRenderer;
    /**
     * A map to store a K,V pairs being the textured Model the key and a list of
     * entity instances the value, so it allows to handle multiple entities in a
     * more optimized way
     */
    private Map<TexturedModel, List<Entity>> entitiesMap;
    private List<Terrain> terrains;

    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000;

    private Matrix4f projectionMatrix;

    public RendererMaster() {
        /**
         * Avoid rendering vertices inside textures
         */
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
        this.shader = new StaticShader();
        this.terrainShader = new TerrainShader();
        createProjectionMatrix();
        this.renderer = new Renderer(shader, projectionMatrix);
        this.terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
        entitiesMap = new HashMap<TexturedModel, List<Entity>>();
        terrains = new ArrayList<Terrain>();
    }

    /**
     * METHOD ORIGINALLY MADE IN THE RENDERER CLASS
     *
     * Since this first stage will affect all the rendering stuff we call it
     * just once instead of every time we want to render an entity
     */
    public void prepare() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(0.5f, 0.5f, 0.5f, 1);
    }

    /**
     * Take as input the new entity to render, it that entity has and existing
     * model then its added to the list of entities that point to that model
     *
     * @param entity
     */
    public void processEntity(Entity entity) {
        TexturedModel model = entity.getModel();
        List<Entity> entitiesList = entitiesMap.get(model);
        /**
         * If there's a model for the incoming entity then it's added to the
         * entities list of that model if not, a new TexturedModel is added to
         * the map with a new list containing the incoming entity
         */
        if (entitiesList != null) {
            entitiesList.add(entity);
        } else {
            List<Entity> newEntitiesList = new ArrayList<>();
            newEntitiesList.add(entity);
            entitiesMap.put(model, newEntitiesList);
        }
    }

    public void processTerrain(Terrain terrain) {
        terrains.add(terrain);
    }

    public void render(Light light, Camera camera) {
        prepare();
        shader.start();
        shader.loadSkyColor(0.5f, 0.5f, 0.5f);
        shader.loadLightPositionVector(light);
        shader.loadViewMatrix(camera);
        renderer.render(entitiesMap);
        shader.stop();
        terrainShader.start();
        terrainShader.loadSkyColor(0.5f, 0.5f, 0.5f);
        terrainShader.loadLightPositionVector(light);
        terrainShader.loadViewMatrix(camera);
        terrainRenderer.render(terrains);
        terrainShader.stop();
        entitiesMap.clear();
        terrains.clear();
    }

    public void cleanAll() {
        shader.cleanAll();
        terrainShader.cleanAll();
    }

    public void createProjectionMatrix() {
        float aspectRatio = (float) Display.getDisplayMode().getWidth() / (float) Display.getDisplayMode().getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;

        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
        projectionMatrix.m33 = 0;
    }

}
