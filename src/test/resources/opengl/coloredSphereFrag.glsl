#define PROCESSING_LIGHT_SHADER

uniform float border;

varying vec4 vertPosition;

void main() {
  vec4 color;
  if (vertPosition.y < border) {
    color = vec4(1.0, 1.0, 0.0, 1.0);
  } else {
    color = vec4(0.0, 0.0, 1.0, 1.0);
  }
  gl_FragColor = color;
}
