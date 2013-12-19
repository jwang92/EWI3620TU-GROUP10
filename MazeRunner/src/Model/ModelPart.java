package Model;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.media.opengl.GL;

import Utils.Point3D;

import com.sun.opengl.util.texture.Texture;

/** @author Oskar */
public class ModelPart {

    private final List<Point3D> vertices = new ArrayList<Point3D>();
    private final List<Point2D.Float> textureCoordinates = new ArrayList<Point2D.Float>();
    private final List<Point3D> normals = new ArrayList<Point3D>();
    private final List<Face> faces = new ArrayList<Face>();
    private final HashMap<String, Model.Material> materials = new HashMap<String, Model.Material>();
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

    public HashMap<String, Model.Material> getMaterials() {
        return materials;
    }

    /** @author Oskar */
    public static class Face {

        private final int[] vertexIndices = {-1, -1, -1};
        private final int[] normalIndices = {-1, -1, -1};
        private final int[] textureCoordinateIndices = {-1, -1, -1};
        private Model.Material material;

        public Model.Material getMaterial() {
            return material;
        }
        
        public boolean hasMaterial(){
        	if(material != null){
        		return true;
        	}
        	return false;
        }

        public boolean hasNormals() {
            return normalIndices[0] != -1;
        }

        public boolean hasTextureCoordinates() {
            return textureCoordinateIndices[0] != -1;
        }
        
        public boolean hasTexture(){
        	return material.hasTexture();
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
        
        public Texture getTexture(){
        	return material.getTexture();
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

        public Face(int[] vertexIndices, int[] normalIndices, int[] textureCoordinateIndices, Model.Material material) {
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
}