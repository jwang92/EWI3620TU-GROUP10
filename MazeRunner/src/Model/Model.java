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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.media.opengl.GL;

import Utils.Point3D;
import Utils.TextureLoader;

import com.sun.opengl.util.texture.Texture;

/** @author Oskar */
public class Model {
	
	//Name:
	private String modelName;
	private final List<ModelPart> parts = new ArrayList<ModelPart>();
	private int[] verticesOffsetArray;
	private int[] normalOffsetArray;
	private int[] numberOfFaces;

    private final List<Point3D> vertices = new ArrayList<Point3D>();
    private final List<Point2D.Float> textureCoordinates = new ArrayList<Point2D.Float>();
    private final List<Point3D> normals = new ArrayList<Point3D>();
    private final List<Face> faces = new ArrayList<Face>();
    private final HashMap<String, Material> materials = new HashMap<String, Material>();
    private boolean enableSmoothShading = true;
    private GL gl;

    public void enableStates() {
    	
        if (hasTextureCoordinates()) {
            gl.glEnable(GL.GL_TEXTURE_2D);
        }
        
        if (isSmoothShadingEnabled()) {
            gl.glShadeModel(GL.GL_SMOOTH);
        } else {
            gl.glShadeModel(GL.GL_FLAT);
        }
    }
    
    public void setModelName(String name){
    	modelName = name;
    }
    
    public void addModelPart(ModelPart p){
    	parts.add(p);
    }
    
    public List<ModelPart> getModelParts(){
    	return parts;
    }
    
    public int[] getVerticesOffsetArray(){
    	return verticesOffsetArray;
    }
    
    public void setVerticesOffsetArrayForPart(int offset,int part){
    	verticesOffsetArray[part] = offset;
    }
    
    public int[] getNormalsOffsetArray(){
    	return normalOffsetArray;
    }
    
    public void setNormalOffsetArrayForPart(int offset,int part){
    	normalOffsetArray[part] = offset;
    }
    
    public void setNumberOfFaces(int[] numberOfF){
    	numberOfFaces = numberOfF;
    }
    
    public void initiliazeArrays(){
    	verticesOffsetArray = new int[parts.size()];
    	normalOffsetArray = new int[parts.size()];
    	numberOfFaces = new int[parts.size()];
    }

    public boolean hasTextureCoordinates() {
        return getTextureCoordinates().size() > 0;
    }

    public boolean hasNormals() {
        return getNormals().size() > 0;
    }

    public List<Point3D> getVertices() {
        return vertices;
    }


    public List<Point2D.Float> getTextureCoordinates() {
        return textureCoordinates;
    }

    public List<Point3D> getNormals() {
        return normals;
    }

    public List<Face> getFaces() {
        return faces;
    }

    public boolean isSmoothShadingEnabled() {
        return enableSmoothShading;
    }

    public void setSmoothShadingEnabled(boolean smoothShadingEnabled) {
        this.enableSmoothShading = smoothShadingEnabled;
    }

    public HashMap<String, Material> getMaterials() {
        return materials;
    }

    public static class Material {

        @Override
        public String toString() {
            return "Material{" +
                    "specularCoefficient=" + specularCoefficient +
                    ", ambientColour=" + ambientColour +
                    ", diffuseColour=" + diffuseColour +
                    ", specularColour=" + specularColour +
                    '}';
        }

        /** Between 0 and 1000. */
        public float specularCoefficient = 100;
        public float[] ambientColour = {0.2f, 0.2f, 0.2f};
        public float[] diffuseColour = {0.3f, 1, 1};
        public float[] specularColour = {1, 1, 1};
        public Texture texture = null;
        public String texturePath = "";
        
        public boolean hasTexture(){
        	if(texture != null){
        		return true;
        	}
        	return false;
        }
        
        public Texture getTexture(){
        	return texture;
        }
        
        public void loadTexture(GL gl){
        	if(!texturePath.equals("")){
        		texture = TextureLoader.loadTexture(texturePath);
        	}
        }
    }

    /** @author Oskar */
    public static class Face {

        private final int[] vertexIndices = {-1, -1, -1};
        private final int[] normalIndices = {-1, -1, -1};
        private final int[] textureCoordinateIndices = {-1, -1, -1};
        private Material material;

        public Material getMaterial() {
            return material;
        }

        public boolean hasNormals() {
            return normalIndices[0] != -1;
        }

        public boolean hasTextureCoordinates() {
            return textureCoordinateIndices[0] != -1;
        }

        public int[] getVertexIndices() {
            return vertexIndices;
        }

        public int[] getTextureCoordinateIndices() {
            return textureCoordinateIndices;
        }

        public int[] getNormalIndices() {
            return normalIndices;
        }

        public Face(int[] vertexIndices) {
            this.vertexIndices[0] = vertexIndices[0];
            this.vertexIndices[1] = vertexIndices[1];
            this.vertexIndices[2] = vertexIndices[2];
        }

        public Face(int[] vertexIndices, int[] normalIndices) {
            this.vertexIndices[0] = vertexIndices[0];
            this.vertexIndices[1] = vertexIndices[1];
            this.vertexIndices[2] = vertexIndices[2];
            this.normalIndices[0] = normalIndices[0];
            this.normalIndices[1] = normalIndices[1];
            this.normalIndices[2] = normalIndices[2];
        }

        public Face(int[] vertexIndices, int[] normalIndices, int[] textureCoordinateIndices, Material material) {
            this.vertexIndices[0] = vertexIndices[0];
            this.vertexIndices[1] = vertexIndices[1];
            this.vertexIndices[2] = vertexIndices[2];
            this.textureCoordinateIndices[0] = textureCoordinateIndices[0];
            this.textureCoordinateIndices[1] = textureCoordinateIndices[1];
            this.textureCoordinateIndices[2] = textureCoordinateIndices[2];
            this.normalIndices[0] = normalIndices[0];
            this.normalIndices[1] = normalIndices[1];
            this.normalIndices[2] = normalIndices[2];
            this.material = material;
        }
    }
    
    public boolean equals(Object other){
    	if(other instanceof Model){
    		Model that = (Model)other;
    		return this.modelName.equals(that.modelName);
    	}
    	return false;
    }
}