package ru.koshakmine.icstd;

import java.io.IOException;
import java.util.HashMap;

import com.zhekasmirnov.apparatus.multiplayer.Network;
import com.zhekasmirnov.apparatus.multiplayer.client.ModdedClient;
import com.zhekasmirnov.apparatus.multiplayer.server.ConnectedClient;
import com.zhekasmirnov.innercore.api.mod.ui.window.UIWindow;
import com.zhekasmirnov.innercore.api.runtime.other.PrintStacking;
import org.json.JSONException;
import org.json.JSONObject;
import ru.koshakmine.icstd.impl.TestBlock;
import ru.koshakmine.icstd.event.Event;
import ru.koshakmine.icstd.impl.TestUpdatable;
import ru.koshakmine.icstd.level.Level;
import ru.koshakmine.icstd.runtime.Updatable;

public class ICSTD {
    //TODO: remove this class completely cuz it is used for internal tests

    static {
        Event.onCall("ModsLoaded", args -> {
            TestBlock block = new TestBlock();
        });

        Network.getSingleton().addClientPacket("aboba", (JSONObject json, String meta) -> {
            try {
                Level.clientMessage(json.getString("message"));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });

        Event.onDestroyBlock((position, block, player) -> {
            // PrintStacking.print("Position: " + position + ", Block: " + block + ", Player: " + player);

            Updatable.addUpdatable(new TestUpdatable(position));

            final ConnectedClient client = Network.getSingleton().getServer().getConnectedClientForPlayer(player);
            if(client != null){
                final JSONObject data = new JSONObject();
                try {
                    data.put("message", position.toString());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                client.send("aboba", data);
            }
        });
    }

    public static void boot(HashMap<?, ?> args) throws IOException {

    }
}