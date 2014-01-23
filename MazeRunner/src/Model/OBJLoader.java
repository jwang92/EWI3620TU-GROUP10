package Model;
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

import java.awt.geom.Point2D;
import java.io.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.media.opengl.GL;

import Utils.BufferTools;
import Utils.Point3D;


/**
 * @author Oskar
 * Extended by Guido Remmerswaal
 */
public class OBJLoader {

	/**
	 * Generate a displaylist for a model
	 * @param m model
	 * @param gl jogl
	 * @return return the id of the displaylist
	 */
    public static int createDisplayList(Model m,GL gl) {
        int displayList = gl.glGenLists(1);
        gl.glNewList(displayList, GL.GL_COMPILE_AND_EXECUTE);
        {
            gl.glMaterialf(GL.GL_FRONT, GL.GL_SHININESS, 120);
            gl.glColor3f(0.4f, 0.27f, 0.17f);
            gl.glBegin(GL.GL_TRIANGLES);
            for (Model.Face face : m.getFaces()) {
                if (face.hasNormals()) {
                    Point3D n1 = m.getNormals().get(face.getNormalIndices()[0] - 1);
                    gl.glNormal3f(n1.x, n1.y, n1.z);
                }
                Point3D v1 = m.getVertices().get(face.getVertexIndices()[0] - 1);
                gl.glVertex3f(v1.x, v1.y, v1.z);
                if (face.hasNormals()) {
                    Point3D n2 = m.getNormals().get(face.getNormalIndices()[1] - 1);
                    gl.glNormal3f(n2.x, n2.y, n2.z);
                }
                Point3D v2 = m.getVertices().get(face.getVertexIndices()[1] - 1);
                gl.glVertex3f(v2.x, v2.y, v2.z);
                if (face.hasNormals()) {
                    Point3D n3 = m.getNormals().get(face.getNormalIndices()[2] - 1);
                    gl.glNormal3f(n3.x, n3.y, n3.z);
                }
                Point3D v3 = m.getVertices().get(face.getVertexIndices()[2] - 1);
                gl.glVertex3f(v3.x, v3.y, v3.z);
            }
            gl.glEnd();
        }
        gl.glEndList();
        return displayList;
    }

    
    private static FloatBuffer reserveData(int size) {
        return BufferTools.reserveData(size);
    }

    private static float[] asFloats(Point3D v) {
        return new float[]{v.x, v.y, v.z};
    }
    
    private static float[] asFloats(Point2D.Float v) {
        return new float[]{v.x, v.y};
    }
    

    /**
     * Generate a model as VertexBufferObject
     */
    public static IntBuffer createVBO(Model model, GL gl) {
    	model = loadTextures(model,gl);
        IntBuffer vboHandle = IntBuffer.allocate(3);
        gl.glGenBuffers(3, vboHandle);
        FloatBuffer vertices = reserveData(model.getFaces().size() * 36);
        FloatBuffer normals = reserveData(model.getFaces().size() * 36);
        FloatBuffer textureCoordinates = reserveData(model.getFaces().size() * 24);
        for(int i =0;i<model.getModelParts().size();i++){
        	ModelPart modelPart = model.getModelParts().get(i);
        	model.setVerticesOffsetArrayForPart(vertices.position(), i);
        	model.setNormalOffsetArrayForPart(normals.position(), i);
            for (ModelPart.Face face : modelPart.getFaces()) {
                vertices.put(asFloats(model.getVertices().get(face.getVertexIndices()[0] - 1)));
                vertices.put(asFloats(model.getVertices().get(face.getVertexIndices()[1] - 1)));
                vertices.put(asFloats(model.getVertices().get(face.getVertexIndices()[2] - 1)));
                normals.put(asFloats(model.getNormals().get(face.getNormalIndices()[0] - 1)));
                normals.put(asFloats(model.getNormals().get(face.getNormalIndices()[1] - 1)));
                normals.put(asFloats(model.getNormals().get(face.getNormalIndices()[2] - 1)));
                if(face.hasTextureCoordinates()){
                    textureCoordinates.put(asFloats(model.getTextureCoordinates().get(face.getTextureCoordinateIndices()[0] - 1)));
                    textureCoordinates.put(asFloats(model.getTextureCoordinates().get(face.getTextureCoordinateIndices()[1] - 1)));
                    textureCoordinates.put(asFloats(model.getTextureCoordinates().get(face.getTextureCoordinateIndices()[2] - 1)));
                }
            }
        }
        vertices.flip();
        normals.flip();
        textureCoordinates.flip();
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboHandle.get(0));
        gl.glBufferData(GL.GL_ARRAY_BUFFER, vertices.capacity(),vertices, GL.GL_STATIC_DRAW);
        gl.glVertexPointer(3, GL.GL_FLOAT, 0, 0L);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboHandle.get(1));
        gl.glBufferData(GL.GL_ARRAY_BUFFER, normals.capacity(),normals, GL.GL_STATIC_DRAW);
        gl.glNormalPointer(GL.GL_FLOAT, 0, 0L);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboHandle.get(2));
        gl.glBufferData(GL.GL_ARRAY_BUFFER, textureCoordinates.capacity(),textureCoordinates, GL.GL_STATIC_DRAW);
        gl.glTexCoordPointer(2, GL.GL_FLOAT, 0, 0L);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
        return vboHandle;
    }
    
    /**
     * Load the textures of the model
     * @param model
     * @param gl
     * @return
     */
    private static Model loadTextures(Model model, GL gl){
    	 for(int i =0;i<model.getModelParts().size();i++){
    		 ModelPart modelPart = model.getModelParts().get(i);
    		 ModelPart.Face face = modelPart.getFaces().get(0);
    		 face.getMaterial().loadTexture(gl);
    	 }
    	 return model;
    }

    private static Point3D parseVertex(String line) {
        String[] xyz = line.split(" ");
        float x = Float.valueOf(xyz[1]);
        float y = Float.valueOf(xyz[2]);
        float z = Float.valueOf(xyz[3]);
        return new Point3D(x, y, z);
    }

    private static Point3D parseNormal(String line) {
        String[] xyz = line.split(" ");
        float x = Float.valueOf(xyz[1]);
        float y = Float.valueOf(xyz[2]);
        float z = Float.valueOf(xyz[3]);
        return new Point3D(x, y, z);
    }

    private static Model.Face parseFace(boolean hasNormals, String line) {
        String[] faceIndices = line.split(" ");
        int[] vertexIndicesArray = {Integer.parseInt(faceIndices[1].split("/")[0]),
                Integer.parseInt(faceIndices[2].split("/")[0]), Integer.parseInt(faceIndices[3].split("/")[0])};
        if (hasNormals) {
            int[] normalIndicesArray = new int[3];
            normalIndicesArray[0] = Integer.parseInt(faceIndices[1].split("/")[2]);
            normalIndicesArray[1] = Integer.parseInt(faceIndices[2].split("/")[2]);
            normalIndicesArray[2] = Integer.parseInt(faceIndices[3].split("/")[2]);
            return new Model.Face(vertexIndicesArray, normalIndicesArray);
        } else {
            return new Model.Face((vertexIndicesArray));
        }
    }

    /**
     * Load model without textures
     * @param f file
     * @return return model
     * @throws IOException
     */
    public static Model loadModel(File f) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(f));
        Model m = new Model();
        String line;
        while ((line = reader.readLine()) != null) {
            String prefix = line.split(" ")[0];
            if (prefix.equals("#")) {
                continue;
            } else if(prefix.equals("mtllib")){
            	continue;
            } else if(prefix.equals("o")){
        		continue;
        	} else if (prefix.equals("v")) {
                m.getVertices().add(parseVertex(line));
            } else if (prefix.equals("vt")) {
            	continue;
            } else if (prefix.equals("vn")) {
                m.getNormals().add(parseNormal(line));
            } else if(prefix.equals("usemtl")){
            	continue;
            } else if (prefix.equals("s")) {
            	continue;
            } else if (prefix.equals("f")) {
                m.getFaces().add(parseFace(m.hasNormals(), line));
            } else if (prefix.equals("l")) {
            	continue;
            } else {
                reader.close();
                throw new RuntimeException("OBJ file contains line which cannot be parsed correctly: " + line);
            }
        }
        reader.close();
        return m;
    }

    /**
     * Display with textures, isn't working correctly
     * @param m model
     * @param gl
     * @return
     */
    public static int createTexturedDisplayList(Model m, GL gl) {
        int displayList = gl.glGenLists(1);
        gl.glNewList(displayList, GL.GL_COMPILE);
        {
            gl.glBegin(GL.GL_TRIANGLES);
            for (Model.Face face : m.getFaces()) {
                if (face.hasTextureCoordinates()) {
                    gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, new float[] {face.getMaterial()
                            .diffuseColour[0], face.getMaterial().diffuseColour[1],
                            face.getMaterial().diffuseColour[2]}, 1);
                    gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, new float[] {face.getMaterial()
                            .ambientColour[0], face.getMaterial().ambientColour[1],
                            face.getMaterial().ambientColour[2]}, 1);
                    gl.glMaterialf(GL.GL_FRONT, GL.GL_SHININESS, face.getMaterial().specularCoefficient);
                    
                }
                if (face.hasNormals()) {
                    Point3D n1 = m.getNormals().get(face.getNormalIndices()[0] - 1);
                    gl.glNormal3f(n1.x, n1.y, n1.z);
                }
                
                
                if (face.hasTextureCoordinates()) {
                    Point2D.Float t1 = m.getTextureCoordinates().get(face.getTextureCoordinateIndices()[0] - 1);
                    gl.glTexCoord2f(t1.x, t1.y);
                }
                
                Point3D v1 = m.getVertices().get(face.getVertexIndices()[0] - 1);
                gl.glVertex3f(v1.x, v1.y, v1.z);
                if (face.hasNormals()) {
                    Point3D n2 = m.getNormals().get(face.getNormalIndices()[1] - 1);
                    gl.glNormal3f(n2.x, n2.y, n2.z);
                }
                
                
                if (face.hasTextureCoordinates()) {
                	Point2D.Float t2 = m.getTextureCoordinates().get(face.getTextureCoordinateIndices()[1] - 1);
                    gl.glTexCoord2f(t2.x, t2.y);
                }
                
                Point3D v2 = m.getVertices().get(face.getVertexIndices()[1] - 1);
                gl.glVertex3f(v2.x, v2.y, v2.z);
                if (face.hasNormals()) {
                    Point3D n3 = m.getNormals().get(face.getNormalIndices()[2] - 1);
                    gl.glNormal3f(n3.x, n3.y, n3.z);
                }
                
                if (face.hasTextureCoordinates()) {
                	Point2D.Float t3 = m.getTextureCoordinates().get(face.getTextureCoordinateIndices()[2] - 1);
                    gl.glTexCoord2f(t3.x, t3.y);
                }
                
                Point3D v3 = m.getVertices().get(face.getVertexIndices()[2] - 1);
                gl.glVertex3f(v3.x, v3.y, v3.z);
            }
            gl.glEnd();
        }
        gl.glEndList();
        return displayList;
    }

    /**
     * Load textured model
     * @param f file
     * @return return model
     * @throws IOException
     */
    public static Model loadTexturedModel(File f) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(f));
        Model m = new Model();
        m.setModelName(f.getAbsolutePath());
        ModelPart part = new ModelPart();
        Model.Material currentMaterial = new Model.Material();
        String line;
        boolean newPart = true;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("#")) {
                continue;
            }
            if (line.startsWith("mtllib ")) {
                String materialFileName = line.split(" ")[1];
                File materialFile = new File(f.getParentFile().getAbsolutePath() + "/" + materialFileName);
                BufferedReader materialFileReader = new BufferedReader(new FileReader(materialFile));
                String materialLine;
                Model.Material parseMaterial = new Model.Material();
                String parseMaterialName = "";
                while ((materialLine = materialFileReader.readLine()) != null) {
                    if (materialLine.startsWith("#")) {
                        continue;
                    }
                    if (materialLine.startsWith("newmtl ")) {
                        if (!parseMaterialName.equals("")) {
                            m.getMaterials().put(parseMaterialName, parseMaterial);
                        }
                        parseMaterialName = materialLine.split(" ")[1];
                        parseMaterial = new Model.Material();
                    } else if (materialLine.startsWith("Ns ")) {
                        parseMaterial.specularCoefficient = Math.min(120f, Float.valueOf(materialLine.split(" ")[1]));
                        
                    } else if (materialLine.startsWith("Ka ")) {
                        String[] rgb = materialLine.split(" ");
                        parseMaterial.ambientColour[0] = Float.valueOf(rgb[1]);
                        parseMaterial.ambientColour[1] = Float.valueOf(rgb[2]);
                        parseMaterial.ambientColour[2] = Float.valueOf(rgb[3]);
                    } else if (materialLine.startsWith("Ks ")) {
                        String[] rgb = materialLine.split(" ");
                        parseMaterial.specularColour[0] = Float.valueOf(rgb[1]);
                        parseMaterial.specularColour[1] = Float.valueOf(rgb[2]);
                        parseMaterial.specularColour[2] = Float.valueOf(rgb[3]);
                    } else if (materialLine.startsWith("Kd ")) {
                        String[] rgb = materialLine.split(" ");
                        parseMaterial.diffuseColour[0] = Float.valueOf(rgb[1]);
                        parseMaterial.diffuseColour[1] = Float.valueOf(rgb[2]);
                        parseMaterial.diffuseColour[2] = Float.valueOf(rgb[3]);
                    } 
                    
                    else if (materialLine.startsWith("map_Kd")) {
                    	parseMaterial.texturePath = f.getParentFile().getAbsolutePath() + "/" + materialLine.split(" ")[1];
                    } 
                    
                }
                m.getMaterials().put(parseMaterialName, parseMaterial);
                materialFileReader.close();
            } else if (line.startsWith("usemtl ")) {
                currentMaterial = m.getMaterials().get(line.split(" ")[1]);

            } else if (line.startsWith("v ")) {
            	if(newPart){
                	part = new ModelPart();
                	m.addModelPart(part);
                	newPart = false;
            	}
                String[] xyz = line.split(" ");
                float x = Float.valueOf(xyz[1]);
                float y = Float.valueOf(xyz[2]);
                float z = Float.valueOf(xyz[3]);
                part.getVertices().add(new Point3D(x, y, z));
                m.getVertices().add(new Point3D(x, y, z));
            } else if (line.startsWith("vn ")) {
                String[] xyz = line.split(" ");
                float x = Float.valueOf(xyz[1]);
                float y = Float.valueOf(xyz[2]);
                float z = Float.valueOf(xyz[3]);
                part.getNormals().add(new Point3D(x, y, z));
                m.getNormals().add(new Point3D(x, y, z));
            } else if (line.startsWith("vt ")) {
                String[] xyz = line.split(" ");
                float s = Float.valueOf(xyz[1]);
                float t = Float.valueOf(xyz[2]);
                part.getTextureCoordinates().add(new Point2D.Float(s, t));
                m.getTextureCoordinates().add(new Point2D.Float(s, t));
            } else if (line.startsWith("f ")) {
            	if(!newPart){
            		newPart = true;
            	}
                String[] faceIndices = line.split(" ");
                int[] vertexIndicesArray = {Integer.parseInt(faceIndices[1].split("/")[0]),
                        Integer.parseInt(faceIndices[2].split("/")[0]), Integer.parseInt(faceIndices[3].split("/")[0])};
                int[] textureCoordinateIndicesArray = {-1, -1, -1};
                
                
                if (part.hasTextureCoordinates()) {
                    textureCoordinateIndicesArray[0] = Integer.parseInt(faceIndices[1].split("/")[1]);
                    textureCoordinateIndicesArray[1] = Integer.parseInt(faceIndices[2].split("/")[1]);
                    textureCoordinateIndicesArray[2] = Integer.parseInt(faceIndices[3].split("/")[1]);
                }  
                int[] normalIndicesArray = {0, 0, 0};
                if (part.hasNormals()) {
                    normalIndicesArray[0] = Integer.parseInt(faceIndices[1].split("/")[2]);
                    normalIndicesArray[1] = Integer.parseInt(faceIndices[2].split("/")[2]);
                    normalIndicesArray[2] = Integer.parseInt(faceIndices[3].split("/")[2]);
                }
                part.getFaces().add(new ModelPart.Face(vertexIndicesArray, normalIndicesArray, 
                		textureCoordinateIndicesArray, currentMaterial));
                m.getFaces().add(new Model.Face(vertexIndicesArray, normalIndicesArray, 
                		textureCoordinateIndicesArray, currentMaterial));
                
            } else if (line.startsWith("s ")) {
                boolean enableSmoothShading = !line.contains("off");
                m.setSmoothShadingEnabled(enableSmoothShading);
            }
        }
        reader.close();
        m.initiliazeArrays();
        return m;
    }
}