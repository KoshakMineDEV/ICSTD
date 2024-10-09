package ru.koshakmine.icstd.js;

import com.zhekasmirnov.innercore.mod.executable.Compiler;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

class JsHelp {
    public static Object callFunction(Scriptable scriptable, String name, Object... args){
        if(scriptable == null)
            throw new RuntimeException("Not init js");

        Object func = scriptable.get(name, scriptable);
        if(func instanceof Function){
            final Function function = (Function) func;
            return function.call(Compiler.assureContextForCurrentThread(), function.getParentScope(), scriptable, args);
        }

        throw new RuntimeException("Not init js");
    }
}
