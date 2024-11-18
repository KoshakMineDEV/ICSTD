package ru.koshakmine.icstd.render;

import com.zhekasmirnov.innercore.api.*;
import ru.koshakmine.icstd.type.block.BlockID;

import java.util.HashMap;

public class RenderModel implements Cloneable {
    @Override
    public RenderModel clone() {
        try {
            RenderModel clone = (RenderModel) super.clone();
            clone.boxes = (HashMap<String, AbstractBox>) boxes.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public static abstract class AbstractBox implements Cloneable {
        public abstract void addNativeBlockModel(NativeBlockModel renderer);
        public abstract void addCollisionShape(NativeICRender.CollisionShape shape);
        public abstract void addICRenderModel(NativeICRender.Model model);
        public abstract void addRenderMesh(NativeRenderMesh mesh);

        @Override
        public AbstractBox clone() {
            try {
                return (AbstractBox) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new AssertionError();
            }
        }
    }

    public static abstract class BaseBox extends AbstractBox {
        public float x1, y1, z1, x2, y2, z2;

        public BaseBox(float x1, float y1, float z1, float x2, float y2, float z2) {
            this.x1 = x1;
            this.y1 = y1;
            this.z1 = z1;
            this.x2 = x2;
            this.y2 = y2;
            this.z2 = z2;
        }

        @Override
        public AbstractBox clone() {
            final BaseBox box = (BaseBox) super.clone();
            box.x1 = x1;
            box.y1 = y1;
            box.z1 = z1;
            box.x2 = x2;
            box.y2 = y2;
            box.z2 = z2;
            return box;
        }

        @Override
        public void addCollisionShape(NativeICRender.CollisionShape shape) {
            shape.addEntry().addBox(x1, y1, z1, x2, y2, z2);
        }

        @Override
        public void addICRenderModel(NativeICRender.Model model) {
            final NativeBlockModel renderer = new NativeBlockModel();
            addNativeBlockModel(renderer);
            model.addEntry().setModel(renderer);
        }

        @Override
        public void addRenderMesh(NativeRenderMesh mesh) {
            mesh.setNormal(0, -1, 0);
            mesh.addVertex(x1, y1, z1);
            mesh.addVertex(x1, y1, z2);
            mesh.addVertex(x2, y1, z1);
            mesh.addVertex(x1, y1, z2);
            mesh.addVertex(x2, y1, z1);
            mesh.addVertex(x2, y1, z2);

            mesh.setNormal(0, -1, 0);
            mesh.addVertex(x1, y2, z1);
            mesh.addVertex(x1, y2, z2);
            mesh.addVertex(x2, y2, z1);
            mesh.addVertex(x1, y2, z2);
            mesh.addVertex(x2, y2, z1);
            mesh.addVertex(x2, y2 , z2);

            mesh.setNormal(-1, 0, 0);
            mesh.addVertex(x1, y1, z1);
            mesh.addVertex(x1, y1, z2);
            mesh.addVertex(x1, y2, z1);
            mesh.addVertex(x1, y1, z2);
            mesh.addVertex(x1, y2, z1);
            mesh.addVertex(x1, y2, z2);

            mesh.setNormal(1, 0, 0);
            mesh.addVertex(x2, y1, z1);
            mesh.addVertex(x2, y1, z2);
            mesh.addVertex(x2, y2, z1);
            mesh.addVertex(x2, y1, z2);
            mesh.addVertex(x2, y2, z1);
            mesh.addVertex(x2, y2, z2);

            mesh.setNormal(0, 0, -1);
            mesh.addVertex(x1, y1, z1);
            mesh.addVertex(x2, y1, z1);
            mesh.addVertex(x1, y2, z1);

            mesh.addVertex(x2, y1, z1);
            mesh.addVertex(x1, y2, z1);
            mesh.addVertex(x2, y2, z1);

            mesh.setNormal(0, 0, 1);
            mesh.addVertex(x1, y1, z2);
            mesh.addVertex(x2, y1, z2);
            mesh.addVertex(x1, y2, z2);

            mesh.addVertex(x2, y1, z2);
            mesh.addVertex(x1, y2, z2);
            mesh.addVertex(x2, y2, z2);
        }
    }

    public static class BoxId extends BaseBox {
        public int id, data;

        public BoxId(float x1, float y1, float z1, float x2, float y2, float z2, int id, int data) {
            super(x1, y1, z1, x2, y2, z2);

            this.id = id;
            this.data = data;
        }

        public BoxId(float x1, float y1, float z1, float x2, float y2, float z2, int id) {
            this(x1, y1, z1, x2, y2, z2, id, 0);
        }

        @Override
        public void addNativeBlockModel(NativeBlockModel renderer) {
            renderer.addBox(x1, y1, z1, x2, y2, z2, id, data);
        }

        @Override
        public void addCollisionShape(NativeICRender.CollisionShape shape) {
            if(id != 0) super.addCollisionShape(shape);
        }
    }

    public static class BoxTexture extends BaseBox {
        public String texture;
        public int meta;

        public BoxTexture(float x1, float y1, float z1, float x2, float y2, float z2, String texture, int meta) {
            super(x1, y1, z1, x2, y2, z2);

            this.texture = texture;
            this.meta = meta;
        }

        public BoxTexture(float x1, float y1, float z1, float x2, float y2, float z2, String texture) {
            this(x1, y1, z1, x2, y2, z2, texture, 0);
        }

        @Override
        public void addNativeBlockModel(NativeBlockModel renderer) {
            renderer.addBox(x1, y1, z1, x2, y2, z2, texture, meta);
        }
    }

    private HashMap<String, AbstractBox> boxes = new HashMap<>();

    protected String genName() {
        int id = 0;
        while (boxes.containsKey(String.valueOf(id))) id++;
        return String.valueOf(id);
    }

    public RenderModel addBox(String name, AbstractBox box) {
        boxes.put(name, box);
        return this;
    }

    public RenderModel addBox(AbstractBox box) {
        return addBox(genName(), box);
    }

    public NativeBlockModel getNativeBlockModel() {
        final NativeBlockModel model = new NativeBlockModel();
        for(AbstractBox box : boxes.values()) {
            box.addNativeBlockModel(model);
        }
        return model;
    }

    public NativeICRender.CollisionShape getCollisionShape() {
        final NativeICRender.CollisionShape shape = new NativeICRender.CollisionShape();
        for(AbstractBox box : boxes.values()) {
            box.addCollisionShape(shape);
        }
        return shape;
    }

    public NativeICRender.Model getICRenderModel() {
        final NativeICRender.Model model = new NativeICRender.Model();
        for(AbstractBox box : boxes.values()) {
            box.addICRenderModel(model);
        }
        return model;
    }

    public NativeRenderMesh getRenderMesh() {
        final NativeRenderMesh mesh = new NativeRenderMesh();
        for(AbstractBox box : boxes.values()) {
            box.addRenderMesh(mesh);
        }
        return mesh;
    }

    public RenderModel setBlockModel(int id, int data) {
        NativeBlockRenderer.setStaticICRender(id, data, getICRenderModel());
        NativeBlockRenderer.setCustomCollisionAndRaycastShape(id, data, getCollisionShape());
        return this;
    }


    public RenderModel setBlockModel(int id) {
        return setBlockModel(id, -1);
    }

    public RenderModel setItemModel(int id, int data) {
        NativeItemModel.getForWithFallback(id, data)
                .setModel(getNativeBlockModel());
        return this;
    }

    public RenderModel setItemModel(int id) {
        return setItemModel(id, 0);
    }

    static {
        final RenderModel model = new RenderModel();
        model.addBox(new RenderModel.BoxTexture(0, 0, 0, 1, 1, 1, "stone"));
        model.setBlockModel(BlockID.STONE);
    }
}
