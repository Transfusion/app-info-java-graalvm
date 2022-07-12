package io.github.transfusion.app_info_java_graalvm;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import java.lang.reflect.Field;

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

    private Context ctx;

    /**
     * Workaround instead of using getValue().getContext() since we can only close a context using the original object returned by the builder, and not
     * <a href="https://github.com/oracle/graal/blob/1ef351ef185d72310a5bc91c3ac466b3818fc25a/sdk/src/org.graalvm.polyglot/src/org/graalvm/polyglot/Context.java#L794">the one returned by Context.get()</a>
     *
     * @return the original context that was used to instantiate this adapter IF this object was instantiated by ourselves and not via any getter method
     */
    public Context getContext() {
        if (this.ctx == null) return getValue().getContext();
        return this.ctx;
    }

    /**
     * @return true if this object was automatically created
     */
    public boolean managed() {
        return this.ctx == null;
    }

    /**
     * Only used internally by extending classes
     *
     * @param ctx the original context object
     */
    public void setContext(Context ctx) {
        this.ctx = ctx;
    }
}
