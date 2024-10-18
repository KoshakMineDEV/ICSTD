package ru.koshakmine.icstd.ui;

import com.zhekasmirnov.horizon.util.FileUtils;
import com.zhekasmirnov.innercore.api.mod.ScriptableObjectHelper;
import com.zhekasmirnov.innercore.api.mod.ui.elements.UIElement;
import com.zhekasmirnov.innercore.api.mod.ui.types.UIStyle;
import com.zhekasmirnov.innercore.api.mod.ui.window.IWindowEventListener;
import com.zhekasmirnov.innercore.api.mod.ui.window.UIWindow;
import com.zhekasmirnov.innercore.api.mod.ui.window.UIWindowLocation;
import com.zhekasmirnov.innercore.api.mod.ui.window.UIWindowStandard;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mozilla.javascript.ScriptableObject;
import ru.koshakmine.icstd.ui.elements.WindowElement;
import com.zhekasmirnov.innercore.api.runtime.saver.serializer.ScriptableSerializer;

import java.io.File;
import java.util.HashMap;

public class Window implements IWindow {
    protected UIWindow window;

    public Window(UIWindow window){
        this.window = window;
    }

    public Window(){
        this.window = new UIWindow(ScriptableObjectHelper.createEmpty());
    }

    public Window(UIWindowLocation location){
        this.window = new UIWindow(location);
        this.window.setContent(ScriptableObjectHelper.createEmpty());
    }

    public void putElement(String name, WindowElement element){
        final ScriptableObject content = window.getContent();
        if(!content.has("elements", content))
            content.put("elements", content, ScriptableObjectHelper.createEmpty());

        final ScriptableObject elements = (ScriptableObject) content.get("elements", content);
        elements.put(name, elements, element.toElement());

        window.setContent(content);
    }

    @Override
    public UIWindow getWindow() {
        return window;
    }

    @Override
    public void open(){
        window.open();
    }

    @Override
    public void close(){
        window.close();
    }

    public HashMap<String, UIElement> getElements(){
        return window.getElements();
    }

    public UIWindowLocation getLocation(){
        return window.getLocation();
    }

    public float getScale(){
        return window.getScale();
    }

    public UIStyle getStyle(){
        return window.getStyle();
    }

    public void invalidateAllContent(){
        window.invalidateAllContent();
    }

    public void invalidateBackground(){
        window.invalidateBackground();
    }

    public void invalidateDrawing(boolean currentThread){
        window.invalidateDrawing(currentThread);
    }

    public void invalidateElements(boolean currentThread){
        window.invalidateElements(currentThread);
    }

    public void invalidateForeground(){
        window.invalidateForeground();
    }

    public boolean isBlockingBackground(){
        return window.isBlockingBackground();
    }

    public boolean isDynamic(){
        return window.isDynamic();
    }

    public boolean isInventoryNeeded(){
        return window.isInventoryNeeded();
    }

    public boolean isNotFocusable(){
        return window.isNotFocusable();
    }

    @Override
    public boolean isOpened(){
        return window.isOpened();
    }

    public boolean isTouchable(){
        return window.isTouchable();
    }

    public boolean onBackPressed(){
        return window.onBackPressed();
    }

    public void setAsGameOverlay(boolean inGameOverlay){
        window.setAsGameOverlay(inGameOverlay);
    }

    public void setBackgroundColor(int color){
        window.setBackgroundColor(color);
    }

    public void setBlockingBackground(boolean blockingBackground){
        window.setBlockingBackground(blockingBackground);
    }

    public void setCloseOnBackPressed(boolean value){
        window.setCloseOnBackPressed(value);
    }

    public void setDynamic(boolean value){
        window.setDynamic(value);
    }

    public void setEventListener(IWindowEventListener listener){
        window.setEventListener(listener);
    }

    public void setInventoryNeeded(boolean inventoryNeeded){
        window.setInventoryNeeded(inventoryNeeded);
    }

    public void setTouchable(boolean touchable){
        window.setTouchable(touchable);
    }

    public void updateWindowLocation(){
        window.updateWindowLocation();
    }

    public void updateScrollDimensions(){
        window.updateScrollDimensions();
    }

    public void updateWindowPositionAndSize(){
        window.updateWindowPositionAndSize();
    }

    public void forceRefresh(){
        window.forceRefresh();
    }

    private static IWindow loadDefWindow(JSONObject json){
        return new Window(new UIWindow((ScriptableObject) ScriptableSerializer.scriptableFromJson(json)));
    }

    private static IWindow loadStandard(JSONObject json){
        return new WindowStandard(new UIWindowStandard((ScriptableObject) ScriptableSerializer.scriptableFromJson(json)) {
            @Override
            protected boolean isLegacyFormat() {
                return true;
            }
        });
    }

    private static IWindow loadWindow(JSONObject json) throws JSONException {
        final String type = json.getString("type");

        if(type == null || type.equals("default")){
            return loadDefWindow(json);
        }else if(type.equals("standard")){
            return loadStandard(json);
        }else if (type.equals("group")) {
            final JSONObject windows = json.getJSONObject("windows");
            final JSONArray names = windows.names();
            final WindowGroup group = new WindowGroup();

            for(int i = 0;i < names.length();i++){
                final String windowName = names.getString(i);
                group.addWindowInstance(windowName, loadWindow(windows.getJSONObject(windowName)));
            }

            return group;
        }

        throw new RuntimeException("Not support type");
    }

    public static IWindow loadWindow(String path){
        try{
            return loadWindow(FileUtils.readJSON(new File(path)));
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
