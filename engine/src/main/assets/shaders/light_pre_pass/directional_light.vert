#version 300 es

in vec4 a_Position;
in vec2 a_TextCoord;

out vec2 v_TextCoord;

void main() {
    v_TextCoord = (a_Position.xy + vec2(1.0)) * 0.5;

    gl_Position = a_Position;
}