#version 300 es

precision mediump float;

uniform sampler2D u_Texture;
uniform sampler2D u_NormalMap;

in vec2 v_TextCoord;

layout(location = 0) out vec4 outColor;

void main() {
    outColor = texture(u_Texture, v_TextCoord);
}