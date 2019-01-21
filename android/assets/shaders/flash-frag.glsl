#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform float flashOpacityValue;

/*float cubicOut(float t) {
  float f = t - 1.0;
  return f * f * f + 1.0;
}*/

void main() {

  vec4 texColor = texture2D(u_texture, v_texCoords);
  vec3 white = texColor.rgb + vec3(flashOpacityValue, flashOpacityValue, flashOpacityValue);
  texColor.rgb = white;

  gl_FragColor = v_color * texColor; 

}