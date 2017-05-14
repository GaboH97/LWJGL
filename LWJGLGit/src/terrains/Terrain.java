
package terrains;

import objectModels.RawModel;
import render.Loader;
import shaders.StaticShader;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

/**
 *
 * @author Gabriel Huertas
 */
public class Terrain {

    private static final float SIZE = 800;
    private static final int VERTEX_COUNT = 128;

    private float x;
    private float z;
    private RawModel model;
    //private ModelTexture texture;
    private TerrainTexturePack terrainTexturePack;
    private TerrainTexture blendMapTexture;
    
    
    public Terrain(float x, float z, Loader loader, TerrainTexturePack terrainTexturePack, TerrainTexture blenderMapTexture) {
        this.terrainTexturePack = terrainTexturePack;
        this.blendMapTexture = blenderMapTexture;
        this.x = x * SIZE;
        this.z = z * SIZE;
        this.model  = generateTerrain(loader);
    }
    
    private RawModel generateTerrain(Loader loader) {
        /**
         * ALL GRIDS TO BE RENDERED IN AN SQUARE MATRIX
         */
        int count = VERTEX_COUNT * VERTEX_COUNT;
        /**
         * VERTICES SIZE (X,Y,Z);
         */
        float[] vertices = new float[count * 3];
         /**
         * NORMALS SIZE (X,Y,Z);
         */
        float[] normals = new float[count * 3];
         /**
         * TEXTURE COORDS SIZE (X,Y);
         */
        float[] textureCoords = new float[count * 2];
        
        int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT - 1)];
        int vertexPointer = 0;
        for (int i = 0; i < VERTEX_COUNT; i++) {
            for (int j = 0; j < VERTEX_COUNT; j++) {
                vertices[vertexPointer * 3] = (float) j / ((float) VERTEX_COUNT - 1) * SIZE;
                vertices[vertexPointer * 3 + 1] = 0;
                vertices[vertexPointer * 3 + 2] = (float) i / ((float) VERTEX_COUNT - 1) * SIZE;
                normals[vertexPointer * 3] = 0;
                normals[vertexPointer * 3 + 1] = 1;
                normals[vertexPointer * 3 + 2] = 0;
                textureCoords[vertexPointer * 2] = (float) j / ((float) VERTEX_COUNT - 1);
                textureCoords[vertexPointer * 2 + 1] = (float) i / ((float) VERTEX_COUNT - 1);
                vertexPointer++;
            }
        }
        int pointer = 0;
        for (int gz = 0; gz < VERTEX_COUNT - 1; gz++) {
            for (int gx = 0; gx < VERTEX_COUNT - 1; gx++) {
                int topLeft = (gz * VERTEX_COUNT) + gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz + 1) * VERTEX_COUNT) + gx;
                int bottomRight = bottomLeft + 1;
                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }
        return loader.loadToVAO(vertices, textureCoords, normals, indices);
    }

    public float getX() {
        return x;
    }

    public float getZ() {
        return z;
    }

    public RawModel getModel() {
        return model;
    }

    public TerrainTexturePack getTerrainTexturePack() {
        return terrainTexturePack;
    }

    public TerrainTexture getBlendMapTexture() {
        return blendMapTexture;
    }

    public void setBlendMapTexture(TerrainTexture blendMapTexture) {
        this.blendMapTexture = blendMapTexture;
    }

    public void setTerrainTexturePack(TerrainTexturePack terrainTexturePack) {
        this.terrainTexturePack = terrainTexturePack;
    }

    
    
    
}
