/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package render;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import objectModels.RawModel;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Gabriel Huertas
 */
public class OBJLoader {

    public static RawModel loadObjModel(String fileName, Loader loader) {

        File file = new File("./src/util/"+fileName+".obj");
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(OBJLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        /**
         * Creates a buffered reader where we want to store the input data
         */
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis));

        /**
         * Creates four lists that will store the data from the .obj file, this
         * means, the vertices, textures, normal vectors and indices
         */
        List<Vector3f> vertices = new ArrayList<Vector3f>();
        List<Vector2f> textures = new ArrayList<Vector2f>();
        List<Vector3f> normals = new ArrayList<Vector3f>();
        List<Integer> indices = new ArrayList<Integer>();

        /**
         * Since the data must be passed to the float Buffers in the Loader We
         * first create float arrays to temporaly
         *
         */
        float[] verticesArray = null;
        float[] texturesArray = null;
        float[] normalsArray = null;
        int[] indicesArray = null;

        String line;
        try {
            while (true) {
                line = bufferedReader.readLine();
                String[] currentLine = line.split(" ");
                /**
                 * If reffers to a position vertex
                 */
                if (line.startsWith("v ")) {
                    Vector3f positionVector = new Vector3f(Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
                    vertices.add(positionVector);
                }/**
                 * If reffers to a texture vertex
                 */
                else if (line.startsWith("vt ")) {
                    Vector2f textureVector = new Vector2f(Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2]));
                    textures.add(textureVector);
                }/**
                 * If reffers to a normal vertex
                 */
                else if (line.startsWith("vn ")) {
                    Vector3f normalVector = new Vector3f(Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
                    normals.add(normalVector);
                }/**
                 * If reffers to a face vertex. Now that we know the size of the
                 * vertices array we set up the size of the textures and normals
                 * arrays as specified below. We break from the while statement
                 * since we've reached faces part of the .obj file
                 */
                else if (line.startsWith("f ")) {
                    texturesArray = new float[vertices.size() * 2];
                    normalsArray = new float[vertices.size() * 3];
                    break;
                }
            }

            while (line != null) {
                if (!line.startsWith("f ")) {
                    line = bufferedReader.readLine();
                    continue;
                }
                /**
                 * We break the line into three parts And then every single
                 * slice into three values that correspond to vertex data,
                 * texture data and normal data face = v1,t1,n1 / v2,t2,n2 /
                 * v3,t3,n3
                 */
                String[] currentLine = line.split(" ");
                String[] vertex1 = currentLine[1].split("/");
                String[] vertex2 = currentLine[2].split("/");
                String[] vertex3 = currentLine[3].split("/");
                processVertex(vertex1, indices, textures, normals, texturesArray, normalsArray);
                processVertex(vertex2, indices, textures, normals, texturesArray, normalsArray);
                processVertex(vertex3, indices, textures, normals, texturesArray, normalsArray);
                line = bufferedReader.readLine();
            }
            bufferedReader.close();

        } catch (Exception e) {
        }
        verticesArray = new float[vertices.size() * 3];
        indicesArray = new int[indices.size()];
        int vertexPointer = 0;

        for (Vector3f vertex : vertices) {
            verticesArray[vertexPointer++] = vertex.x;
            verticesArray[vertexPointer++] = vertex.y;
            verticesArray[vertexPointer++] = vertex.z;
        }

        for (int i = 0; i < indices.size(); i++) {
            indicesArray[i] = indices.get(i);
        }
        return loader.loadToVAO(verticesArray, texturesArray, normalsArray, indicesArray);
    }

    private static void processVertex(String[] vertexArray, List<Integer> indices,
            List<Vector2f> textures, List<Vector3f> normals,
            float[] textureArray, float[] normalsArray) {
        int currentVertexPosition = Integer.parseInt(vertexArray[0]) - 1;
        indices.add(currentVertexPosition);
        /**
         * Add the texture that corresponds to this vertex
         */
        Vector2f currentText = textures.get(Integer.parseInt(vertexArray[1]) - 1);
        textureArray[currentVertexPosition * 2] = currentText.x;
        textureArray[currentVertexPosition * 2 + 1] = 1 - currentText.y;
        /**
         * Add the normal that corresponds to this vertex
         */
        Vector3f currentNormal = normals.get(Integer.parseInt(vertexArray[2]) - 1);
        normalsArray[currentVertexPosition * 3] = currentNormal.x;
        normalsArray[currentVertexPosition * 3 + 1] = currentNormal.y;
        normalsArray[currentVertexPosition * 3 + 2] = currentNormal.z;
    }

}
