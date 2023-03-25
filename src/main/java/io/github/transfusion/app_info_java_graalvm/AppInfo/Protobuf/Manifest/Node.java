package io.github.transfusion.app_info_java_graalvm.AppInfo.Protobuf.Manifest;

import io.github.transfusion.app_info_java_graalvm.AbstractPolyglotAdapter;
import org.graalvm.polyglot.Value;

import java.util.ArrayList;
import java.util.List;

public abstract class Node extends AbstractPolyglotAdapter {
    public abstract String name();

    /**
     * @return Keys of attributes at the current node
     */
    public List<String> attributes_() {
        Value children = getValue().getMember("attributes").execute();
        return new ArrayList<String>(children.getMember("keys").execute().as(List.class));
    }

    public Attribute getAttribute(String attribute) {
        Value accessor = getContext().eval("ruby", "-> hash, key { hash[key] }");
        return accessor.execute(getValue().getMember("attributes").execute(), attribute).as(Attribute.class);
    }

    /**
     * @return Keys of children at the current node
     */
    public List<String> children_() {
        Value children = getValue().getMember("children").execute();
        return new ArrayList<String>(children.getMember("keys").execute().as(List.class));
    }

    /**
     * <a href="https://github.com/icyleaf/app_info/blob/39690b3feb2fafb32b26c0d25e55d56cef526e7f/lib/app_info/protobuf/manifest.rb#L83-L107">Children</a> is a hash of key to AppInfo::Protobuf::Node or an Array of AppInfo::Protobuf::Node
     * Note that in the ruby API, there are certain conditions under which a single node will be returned instead of an Array
     *
     * @return list of {@link Node}, may have a single element
     */
    public List<Node> getChild(String key) {
        Value v = getValue().getMember(key).execute();

        if (v.getMember("is_a?").execute(getContext().eval("ruby", "Array")).asBoolean()) {
            return iterableToList(v, Node.class);
        } else {
            List<Node> res = new ArrayList<>();
            res.add(v.as(Node.class));
            return res;
        }
    }


}
