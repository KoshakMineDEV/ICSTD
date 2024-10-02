package ru.koshakmine.icstd.render.animation;

import com.zhekasmirnov.innercore.api.NativeRenderMesh;
import com.zhekasmirnov.innercore.api.NativeShaderUniformSet;
import com.zhekasmirnov.innercore.api.NativeStaticRenderer;
import ru.koshakmine.icstd.network.NetworkSide;
import ru.koshakmine.icstd.runtime.Updatable;
import ru.koshakmine.icstd.type.common.Position;

public class AnimationBase<T extends AnimationBase> {
    public class Transform {
        public Transform clear(){
            NativeStaticRenderer.transformClear(ptr);
            return this;
        }

        public Transform lock(){
            NativeStaticRenderer.transformLock(ptr);
            return this;
        }

        public Transform unlock(){
            NativeStaticRenderer.transformUnlock(ptr);
            return this;
        }

        public Transform matrix(float f0, float f1, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15){
            NativeStaticRenderer.transformAddTransform(ptr, f0, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15);
            return this;
        }

        public Transform scale(float x, float y, float z){
            NativeStaticRenderer.transformScale(ptr, x, y, z);
            return this;
        }

        public Transform scaleLegacy(float scale){
            NativeStaticRenderer.transformScaleLegacy(ptr, scale);
            return this;
        }

        public Transform rotate(double x, double y, double z){
            NativeStaticRenderer.transformRotate(ptr, (float) x, (float) y, (float) z);
            return this;
        }

        public Transform translate(double x, double y, double z){
            NativeStaticRenderer.transformTranslate(ptr, (float) x, (float) y, (float) z);
            return this;
        }
    }


    private long ptr = -1;
    private boolean isLoaded = false;
    private Position position;
    private final Transform transform = new Transform();

    private NativeRenderMesh mesh;
    private String skin, material;
    private float scale = 1;

    public AnimationBase(float x, float y, float z){
        position = new Position(x, y, z);
    }

    public void setPos(float x, float y, float z){
        if(ptr != -1) NativeStaticRenderer.setPos(ptr, x, y, z);
        position = new Position(x, y, z);
    }

    public void setInterpolationEnabled(boolean enabled){
        if(ptr != -1) NativeStaticRenderer.setInterpolationEnabled(ptr, enabled);
    }

    public void setIgnoreBlocklight(boolean ignore){
        if(ptr != -1) NativeStaticRenderer.setIgnoreBlocklight(ptr, ignore);
    }

    public void setBlockLightPos(float x, float y, float z){
        if(ptr != -1) NativeStaticRenderer.setBlockLightPos(ptr, x, y, z);
    }

    public void resetBlockLightPos(){
        if(ptr != -1) NativeStaticRenderer.resetBlockLightPos(ptr);
    }

    public void setSkylightMode(){
        setBlockLightPos(position.x, 256, position.z);
        setIgnoreBlocklight(false);
    }

    public void setBlocklightMode(){
        resetBlockLightPos();
        setIgnoreBlocklight(false);
    }

    public void setIgnoreLightMode(){
        resetBlockLightPos();
        setIgnoreBlocklight(true);
    }

    public boolean exists(){
        return ptr != -1 && NativeStaticRenderer.nativeExists(ptr);
    }

    public void setMesh(NativeRenderMesh mesh, String skin){
        this.mesh = mesh;
        this.skin = skin;

        if(ptr != -1) {
            NativeStaticRenderer.setMesh(ptr, mesh.getPtr());
            NativeStaticRenderer.setRenderer(ptr, 0);
            NativeStaticRenderer.setSkin(ptr, skin);
            setScale(scale);
            setMaterial(material);
        }
    }

    public void setScale(float scale){
        this.scale = scale;
        if(ptr != -1) NativeStaticRenderer.setScale(ptr, scale);
    }

    public void setMaterial(String material){
        this.material = material;
        if(ptr != -1 && material != null) NativeStaticRenderer.setMeshMaterial(ptr, material);
    }

    public void updateRender(){
        if(isLoaded){
            setMesh(this.mesh, this.skin);
        }else if(ptr != -1){
            NativeStaticRenderer.remove(ptr);
            ptr = -1;
        }
    }

    public synchronized void load(){
        isLoaded = true;
        if(ptr != -1){
            destroy();
        }
        ptr = NativeStaticRenderer.createStaticRenderer(-1, position.x, position.y, position.z);
        updateRender();
    }

    public synchronized void loadCustom(IAnimationUpdate<T> update){
        load();
        T self = (T) this;
        Updatable.addUpdatable(NetworkSide.LOCAL, () -> {
            update.update(self);
            return !isLoaded();
        });
    }

    public synchronized void destroy(){
        isLoaded = false;
        updateRender();
    }

    public NativeShaderUniformSet getShaderUniforms(){
        return new NativeShaderUniformSet(NativeStaticRenderer.nativeGetShaderUniformSet(ptr));
    }

    @Override
    protected void finalize() throws Throwable {
        destroy();
        super.finalize();
    }

    public long getPtr() {
        return ptr;
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public Position getPosition() {
        return position;
    }

    public Transform getTransform() {
        return transform;
    }

    public NativeRenderMesh getMesh() {
        return mesh;
    }

    public String getSkin() {
        return skin;
    }

    public float getScale() {
        return scale;
    }

    public String getMaterial() {
        return material;
    }
}
