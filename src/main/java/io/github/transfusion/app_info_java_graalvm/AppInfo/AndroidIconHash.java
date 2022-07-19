package io.github.transfusion.app_info_java_graalvm.AppInfo;

import io.github.transfusion.app_info_java_graalvm.AbstractPolyglotAdapter;
import org.graalvm.polyglot.Value;

public abstract class AndroidIconHash extends AbstractPolyglotAdapter {
    public String name() {
        Value accessor = getContext().eval("ruby", "-> hash, key { hash[key] }");
        return accessor.execute(getValue(), getContext().eval("ruby", ":name")).asString();
    }

    public String file() {
        Value accessor = getContext().eval("ruby", "-> hash, key { hash[key] }");
        return accessor.execute(getValue(), getContext().eval("ruby", ":file")).asString();
    }

    public Long[] dimensions() {
        Value accessor = getContext().eval("ruby", "-> hash, key { hash[key] }");
        return accessor.execute(getValue(), getContext().eval("ruby", ":dimensions")).as(Long[].class);
    }
}
