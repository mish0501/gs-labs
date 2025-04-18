#version 460 core

in vec3 vertexColor;  // Получаваме интерполирания цвят от vertex шейдъра
out vec4 FragColor;

uniform float alpha;

void main() {
    FragColor = vec4(vertexColor, alpha); // Оцветяване с интерполиран цвят
}
