#extension GL_OES_EGL_image_external : require
precision mediump float;
uniform samplerExternalOES uTextureSampler;
uniform float uScanningLineY;
varying vec2 vTextureCoord;
void main()
{
  vec4 uv = texture2D(uTextureSampler, vTextureCoord);
  float top = uScanningLineY + 0.005;
  float bottom = uScanningLineY - 0.005;
  if(vTextureCoord.x <= top && vTextureCoord.x >= bottom){
    vec4 scanningColor = vec4(0.0, (vTextureCoord.x - bottom)/0.01, 0.0, (vTextureCoord.x - bottom)/0.01);
    gl_FragColor = mix(uv, scanningColor, 0.5);
  }else{
    gl_FragColor = uv;
  }
}
