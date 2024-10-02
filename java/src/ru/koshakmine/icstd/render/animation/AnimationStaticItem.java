package ru.koshakmine.icstd.render.animation;

import com.zhekasmirnov.innercore.api.NativeItemModel;
import com.zhekasmirnov.innercore.api.NativeRenderMesh;
import ru.koshakmine.icstd.type.common.ItemStack;

public class AnimationStaticItem extends AnimationBase<AnimationStaticItem> {
    private ItemStack stack;

    public AnimationStaticItem(float x, float y, float z) {
        super(x, y, z);
    }

    public void setItem(ItemStack item){
        stack = item;
    }

    @Override
    public void updateRender() {
        final NativeRenderMesh lastMesh = getMesh();
        if(lastMesh != null) NativeItemModel.releaseMesh(lastMesh);

        if(isLoaded() && stack != null){
            final NativeItemModel model = NativeItemModel.getForWithFallback(stack.id, stack.data);
            setMesh(model.getItemRenderMesh(1, false), model.getWorldTextureName());
        }
        super.updateRender();
    }

    @Override
    public synchronized void loadCustom(IAnimationUpdate<AnimationStaticItem> update) {
        super.loadCustom(update);
    }
}
