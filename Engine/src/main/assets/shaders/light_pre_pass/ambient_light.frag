#version 300 es

precision mediump float;

uniform sampler2D u_Texture;

uniform sampler2D u_DiffuseTexture;
uniform sampler2D u_SpecularTexture;

uniform vec3 u_AmbientColor;
uniform vec3 u_DiffuseColor;
uniform vec3 u_SpecularColor;

uniform vec2 u_ScreenRatio;

in vec2 v_TextCoord;

layout(location = 0) out vec4 outColor;

void main() {
    vec2 coord = gl_FragCoord.xy * u_ScreenRatio;

    vec3 diffuse = texture(u_DiffuseTexture, coord).xyz * u_DiffuseColor;
    vec3 specular = texture(u_SpecularTexture, coord).xyz * u_SpecularColor;

    vec3 light = u_AmbientColor + diffuse + specular;

    outColor = texture(u_Texture, v_TextCoord) * vec4(light, 1.0);
}