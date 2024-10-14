package ru.koshakmine.icstd.ui;

import com.zhekasmirnov.innercore.api.mod.ScriptableObjectHelper;
import com.zhekasmirnov.innercore.api.mod.ui.window.UIWindowStandard;
import org.mozilla.javascript.ScriptableObject;
import ru.koshakmine.icstd.ui.elements.WindowElement;

public class WindowStandard extends WindowGroup {
    private static ScriptableObject buildContent(String title){
        final ScriptableObject content = ScriptableObjectHelper.createEmpty();
        final ScriptableObject standard = ScriptableObjectHelper.createEmpty();
        final ScriptableObject header = ScriptableObjectHelper.createEmpty();
        final ScriptableObject header_ = ScriptableObjectHelper.createEmpty();

        header.put("text", header, header_);
        header_.put("text", header_, title);

        final ScriptableObject def = ScriptableObjectHelper.createEmpty();
        def.put("standard", def, true);

        standard.put("header", standard, header);
        standard.put("inventory", standard, def);
        standard.put("background", standard, def);

        content.put("standard", content, standard);

        return content;
    }

    public WindowStandard(String title) {
        super(new UIWindowStandard(buildContent(title)) {
            @Override
            protected boolean isLegacyFormat() {
                return true;
            }
        });
    }

    public Window getMain(){
        return getWindow("main");
    }

    public void putElement(String name, WindowElement element) {
        getMain().putElement(name, element);
    }
}
