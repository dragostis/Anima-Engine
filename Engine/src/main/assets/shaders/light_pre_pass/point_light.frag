#version 300 es

precision mediump float;

uniform sampler2D u_DepthTexture;
uniform sampler2D u_NormalTexture;

uniform vec4 u_ProjectionVector;
uniform vec3 u_ClipVector;

uniform vec3 u_LightPosition;
uniform vec3 u_LightColor;
uniform float u_LightRadius;

uniform vec2 u_ScreenRatio;

layout(location = 0) out vec4 outColor0;
layout(location = 1) out vec4 outColor1;

const float c_Zero = 0.0;
const float c_One = 1.0;

const vec3 c_EyePosition = vec3(0.0, 0.0, -1.0);

vec3 getPosition(vec2 uv) {
    float z = u_ClipVector.x / (texture(u_DepthTexture, uv).x * u_ClipVector.y + u_ClipVector.z);

    return vec3((uv * u_ProjectionVector.xy + u_ProjectionVector.zw) * z, z);
}

void main() {
    vec2 coord = gl_FragCoord.xy * u_ScreenRatio;

    vec3 position = getPosition(coord);

    vec3 light = u_LightPosition - position;
    vec3 normal = texture(u_NormalTexture, coord).xyz * 2.0 - vec3(c_One);

    float distance = length(light);
    float attenuation = max(c_Zero, c_One - distance / u_LightRadius);

    light = normalize(light);

    float diffuse = dot(normal, light);

    vec3 reflection = reflect(-light, normal);

    float specular = dot(reflection, normalize(c_EyePosition - position));

    outColor0 = vec4(u_LightColor * max(c_Zero, diffuse) * attenuation, c_One);
    outColor1 = vec4(u_LightColor * pow(max(c_Zero, specular), 20.0) * attenuation, c_One);
}