#type vertex
#version 330 core

uniform mat4 viewProjection;
uniform mat4 transform;

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 texCoord;

out vec3 v_pos;

void main() {
    gl_Position = viewProjection * transform * vec4(position, 1.0);
    v_pos = position;
}

#type fragment
#version 330 core

uniform vec4 col;

in vec3 v_pos;
out vec4 color;

void main() {
    color = col;
}
