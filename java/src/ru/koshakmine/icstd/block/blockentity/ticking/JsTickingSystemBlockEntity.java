package ru.koshakmine.icstd.block.blockentity.ticking;

import com.zhekasmirnov.innercore.mod.executable.Compiler;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import ru.koshakmine.icstd.block.blockentity.BlockEntityBase;
import ru.koshakmine.icstd.level.Level;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;

public class JsTickingSystemBlockEntity extends TickingSystemBlockEntity{
    private static final JsTickingSystemBlockEntity Instance = new JsTickingSystemBlockEntity(true);

    public static JsTickingSystemBlockEntity getInstance() {
        return Instance;
    }

    public JsTickingSystemBlockEntity(boolean isServer) {
        super(isServer);
    }

    private static final Object[] EMPTY_ARGS = new Object[0];

    @Override
    protected void onTickChunk(ConcurrentLinkedDeque<BlockEntityBase> list, int chunkX, int chunkZ, Level level) {
        final Context context = Compiler.assureContextForCurrentThread();
        final Iterator<BlockEntityBase> it = list.iterator();

        while (it.hasNext() && level.isChunkLoaded(chunkX, chunkZ)) {
            final BlockEntityBase entity = it.next();

            final JsTickBlockEntity blockEntity = ((JsTickBlockEntity) entity);
            final Function function = blockEntity.getTick();

            function.call(context, function.getParentScope(), blockEntity.getTile(), EMPTY_ARGS);
        }

    }
}
