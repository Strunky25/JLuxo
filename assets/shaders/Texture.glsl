#type vertex
#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 texCoord;

uniform mat4 viewProjection;
uniform mat4 transform;

out vec2 texCord;

void main() {
    gl_Position = viewProjection * transform * vec4(position, 1.0);
    texCord = texCoord;
}


#type fragment
#version 330 core

uniform sampler2D tex;
uniform vec4 col;

in vec2 texCord;

out vec4 color;

void main() {
    color = texture(tex, texCord * 10) * col;
}