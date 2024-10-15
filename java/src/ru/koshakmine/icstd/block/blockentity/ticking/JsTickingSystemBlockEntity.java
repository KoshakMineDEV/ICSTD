package ru.koshakmine.icstd.block.blockentity.ticking;

import com.zhekasmirnov.innercore.mod.executable.Compiler;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import ru.koshakmine.icstd.block.blockentity.BlockEntityBase;

import java.util.LinkedList;

public class JsTickingSystemBlockEntity extends TickingSystemBlockEntity{
    public JsTickingSystemBlockEntity(boolean isServer) {
        super(isServer);
    }

    private static final Object[] EMPTY_ARGS = new Object[0];

    @Override
    protected void onTickChunk(LinkedList<BlockEntityBase> list) {
        final Context context = Compiler.assureContextForCurrentThread();

        for (BlockEntityBase entity : list) {
            final JsTickBlockEntity blockEntity = ((JsTickBlockEntity) entity);
            final Function function = blockEntity.getTick();

            function.call(context, function.getParentScope(), blockEntity.getTile(), EMPTY_ARGS);
        }
    }
}
