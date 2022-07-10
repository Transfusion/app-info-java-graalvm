package io.github.transfusion.app_info_java_graalvm.AppInfo;

import io.github.transfusion.app_info_java_graalvm.AbstractPolyglotAdapter;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

public abstract class MacOS extends AbstractPolyglotAdapter {
    public static MacOS from(Context polyglot, String path) {
        polyglot.eval("ruby", "require 'app-info'");
        MacOS macOS = polyglot.eval("ruby", "AppInfo::Macos.new('" + path + "')").as(MacOS.class);
        macOS.setContext(polyglot);
        return macOS;
    }

    public static MacOS from(String path) {
        Context polyglot = Context.newBuilder().
                allowAllAccess(true).build();
        return MacOS.from(polyglot, path);
    }

    public abstract String info_path();

    public abstract Long size();

    public String humanSize() {
        Context ctx = getContext();
        Value callSize = ctx.eval("ruby", "-> recv, arg { recv.size(human_size: arg) }");
        Value res = callSize.execute(getValue(), true);
        return res.asString();
    }

    public void clear() {
        Context ctx = getContext();
        Value v = ctx.eval("ruby", "-> recv { recv.clear! }");
        v.execute(getValue());
    }

}
