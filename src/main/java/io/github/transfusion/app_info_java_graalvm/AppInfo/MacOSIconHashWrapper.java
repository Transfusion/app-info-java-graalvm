package io.github.transfusion.app_info_java_graalvm.AppInfo;

import io.github.transfusion.app_info_java_graalvm.AbstractPolyglotAdapter;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import java.util.List;

public abstract class MacOSIconHashWrapper extends AbstractPolyglotAdapter {
    public static abstract class IconSet extends AbstractPolyglotAdapter {
        public String name() {
            Context ctx = getContext();
            Value call = ctx.eval("ruby", "-> recv { recv[:name] }");
            Value res = call.execute(getValue());
            return res.asString();
        }

        public String file() {
            Context ctx = getContext();
            Value call = ctx.eval("ruby", "-> recv { recv[:file] }");
            Value res = call.execute(getValue());
            return res.asString();
        }

        public int[] dimensions() {
            Context ctx = getContext();
            Value call = ctx.eval("ruby", "-> recv { recv[:dimensions] }");
            Value res = call.execute(getValue());
            return res.as(int[].class);
        }
    }

    public String name() {
        Context ctx = getContext();
        Value call = ctx.eval("ruby", "-> recv { recv[:name] }");
        Value res = call.execute(getValue());
        return res.asString();
    }

    public String file() {
        Context ctx = getContext();
        Value call = ctx.eval("ruby", "-> recv { recv[:file] }");
        Value res = call.execute(getValue());
        return res.asString();
    }

    /**
     * @return null if icons(convert: false)
     */
    public List<IconSet> sets() {
        Context ctx = getContext();
        boolean hasSets = ctx.eval("ruby", "-> recv { recv.key?(:sets) }").execute(getValue()).asBoolean();
        if (!hasSets) return null;
        return iterableToList(ctx.eval("ruby", "-> recv { recv[:sets] }").execute(getValue()), IconSet.class);
    }


}
