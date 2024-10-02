package ru.koshakmine.icstd.runtime;

import ru.koshakmine.icstd.event.Event;
import ru.koshakmine.icstd.event.Events;

import java.util.LinkedList;

public class PostLevelLoaded {
    public interface IPostLevelLoaded {
        void call();
    }

    private final LinkedList<IPostLevelLoaded> list = new LinkedList<>();
    private boolean isLevelLoaded = false;

    public PostLevelLoaded(String name){
        Event.onCall(name, args -> {
            synchronized (list) {
                isLevelLoaded = true;
                for (IPostLevelLoaded post : list) post.call();
                list.clear();
            }
        });

        Event.onCall(Events.LevelLeft, args -> {
            isLevelLoaded = false;
        });
    }

    public void run(IPostLevelLoaded run){
        if(isLevelLoaded()){
            run.call();
            return;
        }

        synchronized (list) {
            list.add(run);
        }
    }

    public boolean isLevelLoaded() {
        return isLevelLoaded;
    }

    public static final PostLevelLoaded LOCAL = new PostLevelLoaded(Events.LevelDisplayed);
    public static final PostLevelLoaded SERVER = new PostLevelLoaded(Events.LevelLoaded);

    public static void boot(){}
}
