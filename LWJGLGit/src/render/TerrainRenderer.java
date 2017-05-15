package render;

import entities.Entity;
import java.util.List;
import objectModels.RawModel;
import objectModels.TexturedModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import shaders.StaticShader;
import shaders.TerrainShader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexturePack;
import toolbox.Maths;

/**
 *
 * @author Gabriel Huertas
 */
public class TerrainRenderer {

    private TerrainShader shader;

    public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.connectUnits();
        shader.stop();
    }

    public void render(List<Terrain> terrains) {
        for (Terrain terrain : terrains) {
            prepareTerrainModel(terrain);
            prepareInstance(terrain);
            GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            unbindTexturedModel();
        }
    }

    public void prepareTerrainModel(Terrain terrain) {
        RawModel rawModel = terrain.getModel();
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0); //Access to vertex positions
        GL20.glEnableVertexAttribArray(1); //Access to texture coords
        GL20.glEnableVertexAttribArray(2); //Access to normals
        //ModelTexture modelTexture = terrainModel.getTexture();
        //shader.loadSpecularLightAttributes(modelTexture.getShineDamping(), modelTexture.getReflectivity());
       // GL13.glActiveTexture(GL13.GL_TEXTURE0);
        //GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrainModel.getTexture().getTextureID());
        bindTerrainTextures(terrain);
        shader.loadSpecularLightAttributes(1, 0);
    }
    
    /**
     * Bind Terrain Pack textures all at once given their ID
     */
    public void bindTerrainTextures(Terrain terrain){
        TerrainTexturePack pack = terrain.getTerrainTexturePack();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, pack.getBackgroundTexture().getTextureID());
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, pack.getrTexture().getTextureID());
        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, pack.getgTexture().getTextureID());
        GL13.glActiveTexture(GL13.GL_TEXTURE3);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, pack.getbTexture().getTextureID());
        GL13.glActiveTexture(GL13.GL_TEXTURE4);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getBlendMapTexture().getTextureID());
    }

    public void prepareInstance(Terrain terrain) {
        /**
         * Transformation matrix set up for a flat surface with no rotation and
         * scaled 1x times
         */
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(new Vector3f(terrain.getX(), 0, terrain.getZ()),
                0, 0, 0, 1);
        shader.loadTransformationMatrix(transformationMatrix);
    }

    public void unbindTexturedModel() {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }
}
