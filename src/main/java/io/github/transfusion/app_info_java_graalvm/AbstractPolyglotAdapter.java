package io.github.transfusion.app_info_java_graalvm;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class to be extended by other abstract classes which are actually instances of org.graalvm.polyglot.Value.
 * Exposes getValue() which returns the actual org.graalvm.polyglot.Value using reflection
 */
public abstract class AbstractPolyglotAdapter {
    /**
     * @return the org.graalvm.polyglot.Value if this class is actually an instance of Value.
     */
    public Value getValue() {
        Class<? extends AbstractPolyglotAdapter> c = this.getClass();
        try {
            Field f = c.getDeclaredField("this");
            return (Value) f.get(this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @return Alias of getValue().getContext()
     */
    public Context getContext() {
        return getValue().getContext();
    }

    public static <T> List<T> iterableToList(Value iterable, Class<T> clazz) {
        List<T> res = new ArrayList<>();
        for (Value it = iterable.getIterator(); it.hasIteratorNextElement(); ) {
            Value v = it.getIteratorNextElement();
            res.add(v.as(clazz));
        }
        return res;
    }
}
