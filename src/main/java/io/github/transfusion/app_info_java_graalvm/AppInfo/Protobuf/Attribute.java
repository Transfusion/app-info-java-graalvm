package io.github.transfusion.app_info_java_graalvm.AppInfo.Protobuf;

import io.github.transfusion.app_info_java_graalvm.AbstractPolyglotAdapter;
import org.graalvm.polyglot.Value;

public abstract class Attribute extends AbstractPolyglotAdapter {
    public abstract Long resource_id();

    public abstract String namespace();

    public abstract String name();

    public abstract Value value();
}
