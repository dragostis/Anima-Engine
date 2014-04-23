precision mediump float;

uniform sampler2D u_Texture;

uniform sampler2D u_DiffuseTexture;
uniform sampler2D u_SpecularTexture;

uniform vec3 u_AmbientColor;
uniform vec3 u_DiffuseColor;
uniform vec3 u_SpecularColor;

uniform vec2 u_ScreenSize;

varying vec2 v_TextCoord;

void main() {
    vec2 coord = gl_FragCoord.xy / u_ScreenSize;

    vec3 diffuse = texture2D(u_DiffuseTexture, coord).xyz * u_DiffuseColor;
    vec3 specular = texture2D(u_SpecularTexture, coord).xyz * u_SpecularColor;

    vec3 light = u_AmbientColor + diffuse + specular;

    gl_FragColor = texture2D(u_Texture, v_TextCoord) * vec4(light, 1.0);
}