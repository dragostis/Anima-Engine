precision mediump float;

uniform sampler2D u_Texture;
uniform sampler2D u_NormalMap;

varying vec2 v_TextCoord;

void main() {
    gl_FragColor = texture2D(u_Texture, v_TextCoord);
}