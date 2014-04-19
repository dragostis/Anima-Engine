attribute vec4 a_Position;
attribute vec2 a_TextCoord;

varying vec2 v_TextCoord;

void main() {
    v_TextCoord = a_TextCoord;

    gl_Position = a_Position;
}