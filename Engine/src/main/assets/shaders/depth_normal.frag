precision mediump float;

uniform mat4 u_MMatrix;

uniform sampler2D u_Texture;
uniform sampler2D u_NormalMap;

varying vec2 v_TextCoord;

const float c_Zero = 0.0;
const float c_One = 1.0;

void main() {
    vec3 normal = vec3(u_MMatrix * vec4(texture2D(u_NormalMap, v_TextCoord).xyz * 2.0 - vec3(c_One),
        c_Zero));

    gl_FragData[0] = vec4(normalize(normal) * 0.5 + vec3(0.5), c_One);
}