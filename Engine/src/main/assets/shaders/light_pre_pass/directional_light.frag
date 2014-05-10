#version 300 es

precision mediump float;

uniform sampler2D u_Texture;
uniform sampler2D u_NormalTexture;
uniform sampler2D u_ShadowMap;

uniform vec4 u_ProjectionVector;
uniform vec3 u_ClipVector;
uniform highp mat4 u_SMMatrix;

uniform vec3 u_LightDirection;
uniform vec3 u_LightColor;

in vec2 v_TextCoord;

layout(location = 0) out vec4 outColor0;
layout(location = 1) out vec4 outColor1;

const float c_Bias = 0.002;

const float c_Zero = 0.0;
const float c_One = 1.0;

const vec3 c_EyePosition = vec3(0.0, 0.0, -1.0);

vec3 getPosition(vec2 uv) {
    float z = u_ClipVector.x / (texture(u_Texture, uv).x * u_ClipVector.y + u_ClipVector.z);

    return vec3((uv * u_ProjectionVector.xy + u_ProjectionVector.zw) * z, z);
}

void main() {
    vec3 position = getPosition(v_TextCoord);
    vec3 normal = texture(u_NormalTexture, v_TextCoord).xyz * 2.0 - vec3(c_One);

    float diffuse = dot(normal, u_LightDirection);

    vec3 reflection = reflect(-u_LightDirection, normal);

    float specular = dot(reflection, normalize(c_EyePosition - position));

    if (diffuse > c_Zero) {
        vec4 temp = u_SMMatrix * vec4(v_TextCoord, texture(u_Texture, v_TextCoord).x, 1.0);

        vec2 coord = temp.xy / temp.w;
        float depth = temp.z / temp.w - c_Bias;

        float shadow = 0.0;

        for (float i = -0.001464; i <= 0.001464; i += 0.001464) {
            for (float j = -0.001464; j <= 0.001464; j += 0.001464) {
                shadow += depth < texture(u_ShadowMap, coord + vec2(i, j)).x ? 0.0625 : 0.0;
            }
        }

        diffuse *= shadow;
        specular *= shadow;
    }

    outColor0 = vec4(u_LightColor * max(c_Zero, diffuse), c_One);
    outColor1 = vec4(u_LightColor * pow(max(c_Zero, specular), 20.0), c_One);
}