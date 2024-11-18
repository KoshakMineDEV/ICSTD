package ru.koshakmine.icstd.render.animation;

import com.zhekasmirnov.innercore.api.NativeItemModel;
import com.zhekasmirnov.innercore.api.NativeRenderMesh;
import ru.koshakmine.icstd.type.common.ItemStack;

public class AnimationStaticItem extends AnimationBase<AnimationStaticItem> {
    private ItemStack stack = new ItemStack(0, 0);

    public AnimationStaticItem(float x, float y, float z) {
        super(x, y, z);
    }

    public void setItem(ItemStack item){
        if(item == null)
            item = stack;
        stack = item;
    }

    @Override
    public void updateRender() {
        final NativeRenderMesh lastMesh = getMesh();

        final NativeItemModel model = NativeItemModel.getForWithFallback(stack.id, stack.data);
        setMesh(model.getItemRenderMesh(1, false), model.getWorldTextureName());

        if(lastMesh != null && getMesh() != lastMesh)
            NativeItemModel.releaseMesh(lastMesh);

        super.updateRender();
    }

    @Override
    public synchronized void loadCustom(IAnimationUpdate<AnimationStaticItem> update) {
        super.loadCustom(update);
    }
}
