package etceterum.libra.graphics.gles;

public interface GLESU {
    void gluLookAt(float eyeX, float eyeY, float eyeZ, float centerX, float centerY, float centerZ, float upX, float upY, float upZ);
    void gluPerspective(float fov, float aspect, float zNear, float zFar);
}
