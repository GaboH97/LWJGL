package shaders;

import org.lwjgl.util.vector.Matrix4f;

import toolbox.Maths;

import entities.Camera;
import entities.Light;
import org.lwjgl.util.vector.Vector3f;

public class StaticShader extends ShaderProgram {

    private static final String VERTEX_FILE = "src/shaders/vertex_shader.glsl";
    private static final String FRAGMENT_FILE = "src/shaders/fragment_shader.glsl";
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

    public StaticShader() {
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
    }
    
    public void loadSkyColor(float r, float g, float b){
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
    
    public void loadSpecularLightAttributes(float shineDamping , float reflectivity) {
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
