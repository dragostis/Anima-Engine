#version 300 es

uniform mat4 u_MVPMatrix;

in vec4 a_Position;
in vec2 a_TextCoord;

out vec2 v_TextCoord;

void main() {
    v_TextCoord = a_TextCoord;

    gl_Position = u_MVPMatrix * a_Position;
}