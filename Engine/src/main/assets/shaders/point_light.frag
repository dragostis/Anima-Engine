precision mediump float;

uniform sampler2D u_DepthTexture;
uniform sampler2D u_NormalTexture;

uniform mat4 u_IVPMatrix;

uniform vec3 u_LightPosition;
uniform vec3 u_LightColor;
uniform float u_LightRadius;

uniform vec3 u_EyePosition;

uniform vec2 u_ScreenSize;

const float c_Zero = 0.0;
const float c_One = 1.0;

void main() {
    vec2 coord = gl_FragCoord.xy / u_ScreenSize;

    vec4 position = vec4(coord * 2.0 - vec2(c_One), texture2D(u_DepthTexture, coord).x,
        c_One);
    position = u_IVPMatrix * position;
    position.xyz /= position.w;

    vec3 light = u_LightPosition - position.xyz;
    vec3 normal = texture2D(u_NormalTexture, coord).xyz * 2.0 - vec3(c_One);

    float distance = length(light);
    float attenuation = max(c_Zero, c_One - distance / u_LightRadius);

    light = normalize(light);

    float diffuse = dot(normal, light);

    vec3 reflection = reflect(-light, normal);

    float specular = dot(reflection, normalize(u_EyePosition - position));

    gl_FragData[0] = vec4(u_LightColor * max(c_Zero, diffuse) * attenuation, c_One);
    gl_FragData[1] = vec4(u_LightColor * pow(max(c_Zero, specular), 20.0) * attenuation, c_One);
}