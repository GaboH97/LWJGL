#version 430 core

in vec3 colour;
in vec2 pass_textureCoordinates;
in vec3 surfaceVector;
in vec3 lightPointingVector;
in vec3 cameraPointingVector;
in float visibility;

out vec4 out_Color;

//Using multiple textures to create a mix of them using a blendMap
uniform sampler2D backgroundTexture;
uniform sampler2D rTexture;
uniform sampler2D gTexture;
uniform sampler2D bTexture;
uniform sampler2D blendMapTexture;

uniform vec3 lightColor;
uniform float shineDamping;
uniform float reflectivity;
uniform vec3 skyColor;

void main(void){
    
        //Get the blendmap color of every pixel
        vec4 blendMapColor = texture(blendMapTexture,pass_textureCoordinates);
        
        float backTextureAmount = 1 -(blendMapColor.r+blendMapColor.g+blendMapColor.b);
        vec2 tiledCoords = pass_textures*40.0f;
        vec4 backgroundTextureColor = texture(backgroundTexture,tiledCoords)* backTextureAmount;
        vec4 rTextureColor =texture(rTexture,tiledCoords)* blendMapColor.r;
        vec4 gTextureColor =texture(gTexture,tiledCoords)* blendMapColor.g;
        vec4 bTextureColor =texture(bTexture,tiledCoords)* blendMapColor.b;
        vec4 totalColor = backgroundTextureColor +rTextureColor+gTextureColor+bTextureColor;
        
        

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

        out_Color = vec4(diffuseLight,1.0)*totalColor+ vec4(specularLight,1.0);
        out_Color = mix(vec4(skyColor,1.0),out_Color,visibility);

}