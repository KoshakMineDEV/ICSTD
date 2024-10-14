package ru.koshakmine.icstd.ui;

import com.zhekasmirnov.innercore.api.mod.ScriptableObjectHelper;
import com.zhekasmirnov.innercore.api.mod.ui.elements.UIElement;
import com.zhekasmirnov.innercore.api.mod.ui.types.UIStyle;
import com.zhekasmirnov.innercore.api.mod.ui.window.IWindowEventListener;
import com.zhekasmirnov.innercore.api.mod.ui.window.UIWindow;
import com.zhekasmirnov.innercore.api.mod.ui.window.UIWindowLocation;
import org.mozilla.javascript.ScriptableObject;
import ru.koshakmine.icstd.ui.elements.WindowElement;

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
}
