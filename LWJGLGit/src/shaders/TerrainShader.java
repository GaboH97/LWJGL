/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shaders;

import entities.Camera;
import entities.Light;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;

/**
 *
 * @author Gabriel Huertas
 */
public class TerrainShader extends ShaderProgram {

    private static final String VERTEX_FILE = "src/shaders/terrainVertex_shader.glsl";
    private static final String FRAGMENT_FILE = "src/shaders/terrainFragment_shader.glsl";
    /**
     * Object's position, projection, camera position and light sources
     */
    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightPosition;
    private int location_lightColor;
    private int location_shineDamping;
    private int location_reflectivity;
    private int location_skyColor;
    //Uniform variables for terrain in the fragment shader
    private int location_backgroundTexture;
    private int location_rTexture;
    private int location_gTexture;
    private int location_bTexture;
    private int location_blendMapTexture;

    public TerrainShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    /**
     * Method to access to the uniform variables of the vertex shader
     */
    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoordinates");
        super.bindAttribute(2, "normals");
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_lightPosition = super.getUniformLocation("lightPosition");
        location_lightColor = super.getUniformLocation("lightColor");
        location_shineDamping = super.getUniformLocation("shineDamping");
        location_reflectivity = super.getUniformLocation("reflectivity");
        location_skyColor = super.getUniformLocation("skyColor");
        location_backgroundTexture = super.getUniformLocation("backgroundTexture");
        location_backgroundTexture = super.getUniformLocation("rTexture");
        location_backgroundTexture = super.getUniformLocation("gTexture");
        location_backgroundTexture = super.getUniformLocation("bTexture");
        location_backgroundTexture = super.getUniformLocation("blendMapTexture");
    }
    
    /**
     * Connect Uniform variables for the multi-textured terrain
     */
    public void connectUnits(){
        super.loadInt(location_backgroundTexture, 0);
        super.loadInt(location_rTexture, 1);
        super.loadInt(location_gTexture, 2);
        super.loadInt(location_bTexture, 3);
        super.loadInt(location_blendMapTexture, 4);
    }

    public void loadSkyColor(float r, float g, float b) {
        super.loadVector(location_skyColor, new Vector3f(r, g, b));
    }

    /**
     * THE FOLLOWING METHODS LOAD UP A NEW MATRIX GIVEN SOME CHANGES OF THE
     * UNIFORM VARIABLES
     *
     * @param matrix
     */
    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    public void loadLightPositionVector(Light light) {
        super.loadVector(location_lightPosition, light.getPosition());
        super.loadVector(location_lightColor, light.getColor());
    }

    public void loadSpecularLightAttributes(float shineDamping, float reflectivity) {
        super.loadFloat(location_shineDamping, shineDamping);
        super.loadFloat(location_reflectivity, reflectivity);
    }

    public void loadViewMatrix(Camera camera) {
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        super.loadMatrix(location_viewMatrix, viewMatrix);
    }

    public void loadProjectionMatrix(Matrix4f projection) {
        super.loadMatrix(location_projectionMatrix, projection);
        System.out.println("Projection Matrix " + location_projectionMatrix);
    }

}
