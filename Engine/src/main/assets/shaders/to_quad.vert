attribute vec4 a_Position;
attribute vec2 a_TextCoord;

varying vec2 v_TextCoord;

void main() {
    v_TextCoord = (a_Position.xy + vec2(1.0)) / 2.0;

    gl_Position = a_Position;
}