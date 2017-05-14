#version 430 core

in vec3 colour;
in vec2 pass_textureCoordinates;
in vec3 surfaceVector;
in vec3 lightPointingVector;
in vec3 cameraPointingVector;
in float visibility;

out vec4 out_Color;

uniform sampler2D modelTexture;
uniform vec3 lightColor;
uniform float shineDamping;
uniform float reflectivity;
uniform vec3 skyColor;

void main(void){
        
        //Normalize all vectors
        vec3 unitSurfaceVector = normalize(surfaceVector); //In order make its size (1,1,1)
        vec3 unitLightPointingVector = normalize(lightPointingVector);
        vec3 unitCameraPointingVector = normalize(cameraPointingVector);
        
        float dotProd = dot(unitSurfaceVector,unitLightPointingVector); //Product is an scalar between 1 (Maximum light) and 0 (Maximum darkness)
        
        float brightness = max(dotProd,0.0);
        vec3 diffuseLight = brightness * lightColor; //This will result into the final diffuse light of every pixel
	vec3 lightDirection = -unitLightPointingVector;
        vec3 reflectedLightVector = reflect(lightDirection,unitSurfaceVector);

        float specularFactor = max(dot(reflectedLightVector,unitCameraPointingVector),0.0);
        float damplingFactor = pow(specularFactor,shineDamping);
        vec3 specularLight = damplingFactor*reflectivity*lightColor;

        out_Color = vec4(diffuseLight,1.0)*texture(modelTexture,pass_textureCoordinates)+ vec4(specularLight,1.0); 
        out_Color = mix(vec4(skyColor,1.0),out_Color,visibility);

}