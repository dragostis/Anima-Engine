#version 300 es

precision mediump float;

uniform mat4 u_MVMatrix;

uniform sampler2D u_Texture;
uniform sampler2D u_NormalMap;

in vec2 v_TextCoord;

layout(location = 0) out vec4 outColor;

const float c_Zero = 0.0;
const float c_One = 1.0;

void main() {
    vec3 normal = vec3(u_MVMatrix * vec4(texture(u_NormalMap, v_TextCoord).xyz * 2.0 - vec3(c_One),
        c_Zero));

    outColor = vec4(normalize(normal) * 0.5 + vec3(0.5), c_One);
}