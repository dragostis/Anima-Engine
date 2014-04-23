uniform mat4 u_MVPMatrix;

attribute vec4 a_Position;
attribute vec2 a_TextCoord;

void main() {
    gl_Position = u_MVPMatrix * a_Position;
}