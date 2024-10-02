package ru.koshakmine.icstd.runtime.saver;

import org.json.JSONException;
import org.json.JSONObject;

public interface IRead {
    void read(JSONObject jsonObject) throws JSONException;
}
