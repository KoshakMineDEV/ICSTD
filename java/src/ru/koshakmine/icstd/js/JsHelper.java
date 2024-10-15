package ru.koshakmine.icstd.js;

import com.zhekasmirnov.innercore.mod.executable.Compiler;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Wrapper;

class JsHelper {
    public static Object callFunction(ScriptableObject self, String name, Object... args){
        if(self == null)
            throw new RuntimeException("Not init js");

        Object func = self.get(name, self);
        if(func instanceof Function){
            final Function function = (Function) func;
            return function.call(Compiler.assureContextForCurrentThread(), function.getParentScope(), self, args);
        }

        throw new RuntimeException("Not init js "+func.getClass() +", "+(func instanceof Wrapper)+" "+self.getClass());
    }
}
