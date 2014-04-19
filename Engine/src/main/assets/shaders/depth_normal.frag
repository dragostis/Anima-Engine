precision mediump float;

uniform mat4 u_MVMatrix;

uniform sampler2D u_Texture;
uniform sampler2D u_NormalMap;

varying vec2 v_TextCoord;

const float c_Zero = 0.0;
const float c_One = 1.0;

void main() {
    gl_FragData[0] = vec4(gl_FragCoord.z, gl_FragCoord.z, gl_FragCoord.z, c_One);
    gl_FragData[1] = normalize(u_MVMatrix * vec4(texture2D(u_NormalMap, v_TextCoord).xyz * 2.0
                           - vec3(1.0), c_Zero));
}