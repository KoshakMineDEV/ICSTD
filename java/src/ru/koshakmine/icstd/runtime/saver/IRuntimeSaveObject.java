package ru.koshakmine.icstd.runtime.saver;

import java.util.UUID;

public interface IRuntimeSaveObject extends ISave {
    String getName();
    UUID getSaveId();
}
