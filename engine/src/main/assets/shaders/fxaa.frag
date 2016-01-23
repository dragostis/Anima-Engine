#version 300 es

precision mediump float;

uniform sampler2D u_Texture;

uniform vec2 u_ScreenRatio;

in highp vec2 v_TextCoord;

layout(location = 0) out vec4 outColor;

const float c_FxaaSpanMax = 8.0;
const float c_FxaaReduceMul = 1.0 / 8.0;
const float c_FxaaReduceMin = 1.0 / 128.0;

void main() {
    vec3 rgbNW = texture(u_Texture, v_TextCoord + (vec2(-1.0, -1.0) * u_ScreenRatio)).xyz;
    vec3 rgbNE = texture(u_Texture, v_TextCoord + (vec2(+1.0, -1.0) * u_ScreenRatio)).xyz;
    vec3 rgbSW = texture(u_Texture, v_TextCoord + (vec2(-1.0, +1.0) * u_ScreenRatio)).xyz;
    vec3 rgbSE = texture(u_Texture, v_TextCoord + (vec2(+1.0, +1.0) * u_ScreenRatio)).xyz;
    vec3 rgbM  = texture(u_Texture, v_TextCoord).xyz;
    
    vec3 luma = vec3(0.299, 0.587, 0.114);

    float lumaNW = dot(rgbNW, luma);
    float lumaNE = dot(rgbNE, luma);
    float lumaSW = dot(rgbSW, luma);
    float lumaSE = dot(rgbSE, luma);
    float lumaM  = dot(rgbM, luma);
    
    float lumaMin = min(lumaM, min(min(lumaNW, lumaNE), min(lumaSW, lumaSE)));
    float lumaMax = max(lumaM, max(max(lumaNW, lumaNE), max(lumaSW, lumaSE)));
    
    vec2 dir;

    dir.x = -((lumaNW + lumaNE) - (lumaSW + lumaSE));
    dir.y = ((lumaNW + lumaSW) - (lumaNE + lumaSE));
    
    float dirReduce = max((lumaNW + lumaNE + lumaSW + lumaSE) * (0.25 * c_FxaaReduceMul),
        c_FxaaReduceMin);
      
    float rcpDirMin = 1.0/(min(abs(dir.x), abs(dir.y)) + dirReduce);
    
    dir = min(vec2(c_FxaaSpanMax,  c_FxaaSpanMax), 
        max(vec2(-c_FxaaSpanMax, -c_FxaaSpanMax), dir * rcpDirMin)) * u_ScreenRatio;
        
    vec3 rgbA = (1.0/2.0) * (
              texture(u_Texture, v_TextCoord + dir * (1.0/3.0 - 0.5)).xyz +
              texture(u_Texture, v_TextCoord + dir * (2.0/3.0 - 0.5)).xyz);
    vec3 rgbB = rgbA * (1.0/2.0) + (1.0/4.0) * (
              texture(u_Texture, v_TextCoord + dir * (0.0/3.0 - 0.5)).xyz +
              texture(u_Texture, v_TextCoord + dir * (3.0/3.0 - 0.5)).xyz);

    float lumaB = dot(rgbB, luma);

    if((lumaB < lumaMin) || (lumaB > lumaMax)) {
        outColor.xyz = rgbA;
    } else {
        outColor.xyz = rgbB;
    }

    outColor.a = 1.0;
}