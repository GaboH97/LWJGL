#version 430 core

in vec3 position;
in vec2 textureCoordinates;
in vec3 normals;

out vec3 colour;
out vec2 pass_textureCoordinates;
out vec3 surfaceVector;
out vec3 lightPointingVector;
out vec3 cameraPointingVector;
out float visibility;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition;

const float density = 0.007;
const float gradient = 1.5;


void main(void){
    
	vec4 worldPosition =  transformationMatrix * vec4(position,1.0);
        vec4 positionRelativeToCam = viewMatrix * worldPosition;
	gl_Position = projectionMatrix * positionRelativeToCam;
	pass_textureCoordinates = textureCoordinates;


        surfaceVector = (transformationMatrix * vec4(normals,0.0)).xyz; //Since the object is transformated, we multiply 
                                                            // the normal vector with the transformation matrix
        lightPointingVector = lightPosition - (transformationMatrix * vec4(position,1.0)).xyz; //Returns a vector pointing to the light source                                                        
        
        cameraPointingVector = (inverse(viewMatrix)* vec4(0.0,0.0,0.0,1.0)).xyz - (transformationMatrix * vec4(position,1.0)).xyz;

        float distance = length(positionRelativeToCam.xyz);
        visibility = exp(-pow((distance*density),gradient));
        visibility = clamp(visibility,0.0,1.0);
}       