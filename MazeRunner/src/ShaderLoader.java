/*
 * Copyright (c) 2013, Oskar Veerhoek
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those
 * of the authors and should not be interpreted as representing official policies,
 * either expressed or implied, of the FreeBSD Project.
 */

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.IntBuffer;

import javax.media.opengl.GL;

public class ShaderLoader {

    /**
     * Loads a shader program from two source files.
     *
     * @param vertexShaderLocation the location of the file containing the vertex shader source
     * @param fragmentShaderLocation the location of the file containing the fragment shader source
     *
     * @return the shader program or -1 if the loading or compiling failed
     */
    public static int loadShaderPair(String vertexShaderLocation, String fragmentShaderLocation, GL gl) {
    	int shaderProgram = gl.glCreateProgram();
        int vertexShader = gl.glCreateShader(GL.GL_VERTEX_SHADER);
        int fragmentShader = gl.glCreateShader(GL.GL_FRAGMENT_SHADER);
        StringBuilder vertexShaderSource = new StringBuilder();
        StringBuilder fragmentShaderSource = new StringBuilder();
        
        //Vertex shader file
        int countVertex = 0;
        String[] vertexShaderSource1;
        IntBuffer vertexShaderSourceLengths;
        
        //Fragment shader file
        int countFragment = 0;
        String[] fragmentShaderSource1;
        IntBuffer fragmentShaderSourceLengths;
        
        //Input reader
        FileInputStream fIn = null;
        
        BufferedReader vertexShaderFileReader = null;
        try {
            countVertex = 0;
        	fIn = new FileInputStream(vertexShaderLocation);
            vertexShaderFileReader = new BufferedReader(new InputStreamReader(fIn));
            String line;
            while ((line = vertexShaderFileReader.readLine()) != null) {
            	countVertex ++;
                //vertexShaderSource.append(line).append('\n');
            }
            
            vertexShaderSource1 = new String[countVertex];
            vertexShaderSourceLengths = IntBuffer.allocate(countVertex);
            fIn.getChannel().position(0);
            vertexShaderFileReader = new BufferedReader(new InputStreamReader(fIn));
            int count1 = 0;
            while ((line = vertexShaderFileReader.readLine()) != null) {
            	vertexShaderSource1[count1] = line + "\n";
            	vertexShaderSourceLengths.put(line.length());
            	count1++;
                //vertexShaderSource.append(line).append('\n');
            }
            System.out.println();
            fIn.close();
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        } finally {
            if (vertexShaderFileReader != null) {
                try {
                	fIn.close();
                    vertexShaderFileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        BufferedReader fragmentShaderFileReader = null;
        try {
        	countFragment = 0;
        	fIn = new FileInputStream(fragmentShaderLocation);
            fragmentShaderFileReader = new BufferedReader(new InputStreamReader(fIn));
            String line;
            while ((line = fragmentShaderFileReader.readLine()) != null) {
            	countFragment++;
                //fragmentShaderSource.append(line).append('\n');
            }
            
            fragmentShaderSource1 = new String[countFragment];
            fragmentShaderSourceLengths = IntBuffer.allocate(countFragment);
            fIn.getChannel().position(0);
            fragmentShaderFileReader = new BufferedReader(new InputStreamReader(fIn));
            int count1 = 0;
            while ((line = fragmentShaderFileReader.readLine()) != null) {
            	fragmentShaderSource1[count1] = line + "\n";
            	fragmentShaderSourceLengths.put(line.length());
            	count1++;
                //vertexShaderSource.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        } finally {
            if (fragmentShaderFileReader != null) {
                try {
                	fIn.close();
                    fragmentShaderFileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        gl.glShaderSource(vertexShader, countVertex,vertexShaderSource1,null);
        gl.glCompileShader(vertexShader);
        IntBuffer vertexShaderStatus = IntBuffer.allocate(10);
        gl.glGetShaderiv(vertexShader, GL.GL_COMPILE_STATUS,vertexShaderStatus);
        if (vertexShaderStatus.get(0) == GL.GL_FALSE) {
            System.err.println("Vertex shader wasn't able to be compiled correctly. Error log:");
            //System.err.println(gl.glGetShaderInfoLog(vertexShader, 1024));
            return -1;
        }
        gl.glShaderSource(fragmentShader, countFragment,fragmentShaderSource1,null);
        gl.glCompileShader(fragmentShader);
        IntBuffer fragmentShaderStatus = IntBuffer.allocate(10);
        gl.glGetShaderiv(fragmentShader, GL.GL_COMPILE_STATUS,fragmentShaderStatus);
        if (fragmentShaderStatus.get(0) == GL.GL_FALSE) {
            System.err.println("Fragment shader wasn't able to be compiled correctly. Error log:");
            //System.err.println(gl.glGetShaderInfoLog(fragmentShader, 1024));
            return -1;
        }
        gl.glAttachShader(shaderProgram, vertexShader);
        gl.glAttachShader(shaderProgram, fragmentShader);
        gl.glLinkProgram(shaderProgram);
        IntBuffer shaderProgramStatus = IntBuffer.allocate(10);
        gl.glGetProgramiv(shaderProgram, GL.GL_LINK_STATUS,shaderProgramStatus);
        if (shaderProgramStatus.get(0) == GL.GL_FALSE) {
            System.err.println("Shader program wasn't linked correctly.");
            //System.err.println(gl.glGetProgramInfoLog(shaderProgram, 1024));
            return -1;
        }
        gl.glDeleteShader(vertexShader);
        gl.glDeleteShader(fragmentShader);
        return shaderProgram;
    }
}