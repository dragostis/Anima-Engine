package org.anima.engine.graphics;

import android.opengl.GLES30;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Program {
    private int programHandle;

    public Program(InputStream vertexShaderStream, InputStream fragmentShaderStream)
            throws IOException {
        String vertexShader = getShader(vertexShaderStream);
        String fragmentShader = getShader(fragmentShaderStream);

        int vertexShaderHandle = GLES30.glCreateShader(GLES30.GL_VERTEX_SHADER);

        if (vertexShaderHandle != 0) {
            GLES30.glShaderSource(vertexShaderHandle, vertexShader);
            GLES30.glCompileShader(vertexShaderHandle);

            final int[] compileStatus = new int[1];
            GLES30.glGetShaderiv(vertexShaderHandle, GLES30.GL_COMPILE_STATUS, compileStatus, 0);

            if (compileStatus[0] == 0) {
                throw new RuntimeException(GLES30.glGetShaderInfoLog(vertexShaderHandle));
            }
        } else {
            throw new RuntimeException("Error creating vertex shader.");
        }

        int fragmentShaderHandle = GLES30.glCreateShader(GLES30.GL_FRAGMENT_SHADER);

        if (fragmentShaderHandle != 0) {
            GLES30.glShaderSource(fragmentShaderHandle, fragmentShader);
            GLES30.glCompileShader(fragmentShaderHandle);

            final int[] compileStatus = new int[1];
            GLES30.glGetShaderiv(fragmentShaderHandle, GLES30.GL_COMPILE_STATUS, compileStatus, 0);

            if (compileStatus[0] == 0) {
                throw new RuntimeException(GLES30.glGetShaderInfoLog(fragmentShaderHandle));
            }
        } else {
            throw new RuntimeException("Error creating fragment shader.");
        }

        programHandle = GLES30.glCreateProgram();

        if (programHandle != 0) {
            GLES30.glAttachShader(programHandle, vertexShaderHandle);
            GLES30.glAttachShader(programHandle, fragmentShaderHandle);

            GLES30.glLinkProgram(programHandle);

            final int[] linkStatus = new int[1];
            GLES30.glGetProgramiv(programHandle, GLES30.GL_LINK_STATUS, linkStatus, 0);

            if (linkStatus[0] == 0) {
                throw new RuntimeException(GLES30.glGetProgramInfoLog(programHandle));
            }
        } else {
            throw new RuntimeException("Error linking program.");
        }
    }

    public int getProgramHandle() {
        return programHandle;
    }

    public void use() {
        GLES30.glUseProgram(programHandle);
    }

    private String getShader(InputStream inputStream) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String line;
        StringBuilder stringBuilder = new StringBuilder();

        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append('\n');
        }

        return stringBuilder.toString();
    }
}
