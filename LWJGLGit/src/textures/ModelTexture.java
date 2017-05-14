package textures;

public class ModelTexture {

    private int textureID;
    private float shineDamping;
    private float reflectivity;

    public ModelTexture(int texture, float shineDamping, float reflectivity) {
        this.textureID = texture;
        this.shineDamping = shineDamping;
        this.reflectivity = reflectivity;
    }

    public int getTextureID() {
        return textureID;
    }

    public void setTextureID(int textureID) {
        this.textureID = textureID;
    }

    public float getShineDamping() {
        return shineDamping;
    }

    public void setShineDamping(float shineDamping) {
        this.shineDamping = shineDamping;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }

}
