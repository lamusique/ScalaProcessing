uniform mat4 transform;
attribute vec4 vertex;
varying vec4 vertPosition;

void main() {
  vertPosition = vertex;
  gl_Position = transform * vertex;
}
