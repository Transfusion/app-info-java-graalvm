package io.github.transfusion.app_info_java_graalvm.Android;

import io.github.transfusion.app_info_java_graalvm.AbstractPolyglotAdapter;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import java.time.ZonedDateTime;

/**
 * Partial wrapper of <a href="https://github.com/icyleaf/android_parser">this fork of</a> the <a href="https://github.com/SecureBrain/ruby_apk">ruby_apk</a> gem
 */
public abstract class APK extends AbstractPolyglotAdapter {
    public abstract String path();

    /**
     * @return [Android::Manifest] manifest instance
     * [nil] when parsing manifest is failed
     */
    public abstract Manifest manifest();

    /**
     * @return [Android::Dex] dex instance
     * [nil] when parsing dex is failed.
     */
    public abstract Value dex();

    /**
     * @return [String] binary data of apk
     */
    public abstract String bindata();

    /**
     * @return [Resource] resouce data
     * [nil] when parsing resource is failed.
     */
    public abstract Value resource();

    /**
     * @return [Integer] bytes
     */
    public abstract Long size();

    /**
     * # return hex digest string of apk file
     * # @param [Symbol] type hash digest type(:sha1, sha256, :md5)
     * # @return [String] hex digest string
     * # @raise [ArgumentError] type is knknown type
     *
     * @param type [Symbol] type hash digest type(:sha1, sha256, :md5)
     * @return [String] hex digest string
     */
    public String digest_(String type) {
        Value callDigest = getContext().eval("ruby", "-> recv, arg { recv.digest(arg) }");
        Value symbol = getContext().eval("ruby", ":" + type);
        return callDigest.execute(getValue(), symbol).asString();
    }

    /**
     * returns date of AndroidManifest.xml as Apk date
     *
     * @return [Time]
     */
    public abstract ZonedDateTime time();

    @FunctionalInterface
    public interface EachFileConsumer {
        /**
         * @param name file name
         * @param data a <a href="https://github.com/oracle/truffleruby/issues/1722">BINARY</a> ruby string
         */
        void method(String name, Value data);
    }

    /**
     * <a href="https://github.com/oracle/truffleruby/blob/master/doc/user/jruby-migration.md#passing-blocks">How to pass blocks</a>
     *
     * @param consumer an implementation of {@link EachFileConsumer} which gets called with each file
     * # @yield [name, data]
     * # @yieldparam [String] name file name in apk
     * # @yieldparam [String] data file data in apk
     */
    public void each_file_(EachFileConsumer consumer) {
        Context ctx = getContext();
        Value rubyLambda = ctx.eval("ruby", "-> recv, block { recv.each_file { |name, data| block.call(name, data) } }");
        rubyLambda.execute(getValue(), ctx.asValue(consumer));
    }

    /**
     * find and return binary data with name
     *
     * @param name file name in apk(fullpath)
     * @return BINARY ruby string
     */
    public abstract Value file(String name);

    @FunctionalInterface
    public interface EachEntryConsumer {
        /**
         * @param entry a <a href="https://rubydoc.info/github/rubyzip/rubyzip/Zip/Entry">Zip::Entry</a>
         */
        void method(Value entry);
    }

    public void each_entry_(EachEntryConsumer consumer) {
        Context ctx = getContext();
        Value rubyLambda = ctx.eval("ruby", "-> recv, block { recv.each_entry { |entry| block.call(entry) } }");
        rubyLambda.execute(getValue(), ctx.asValue(consumer));
    }

    /**
     * # find and return zip entry with name
     *
     * @param name name file name in apk(fullpath)
     * @return [Zip::Entry] zip entry object
     */
    public abstract Value entry(String name);

//    # find files which is matched with block condition
//    no point in exposing this function directly since we cannot manipulate BINARY encoded ruby strings from java anyways

    /**
     * # extract application icon data from AndroidManifest and resource.
     *
     * @return <pre>{@code [Hash{ String => String }] hash key is icon filename. value is image data}</pre>
     */
    public abstract Value icon();

    /**
     * @param icon_id [String] icon_id to be searched in the resource.
     * @return <pre>{@code [Hash{ String => String }] hash key is icon filename. value is image data }</pre>
     */
    public Value icon_by_id_(String icon_id) {
        return getValue().getMember("icon_by_id").execute(icon_id);
    }

    /**
     * get application label from AndroidManifest and resources.
     *
     * @param lang [String] lang language code like 'ja', 'cn', ...
     * @return [String] application label string
     * [nil] when label is not found
     */
    public abstract String label(String lang);

    // the following 3 methods return a Hash

    /**
     * get screen layout xml datas
     *
     * @return <pre>{@code [Hash{ String => Android::Layout }] key: laytout file path, value: layout object }</pre>
     */
    public abstract Value layouts();

    /**
     * apk's signature information
     *
     * @return <pre>{@code [Hash{ String => OpenSSL::PKCS7 } ] key: sign file path, value: signature}</pre>
     */
    public abstract Value signs();

    /**
     * certificate info which is used for signing
     *
     * @return <pre>{@code [Hash{String => OpenSSL::X509::Certificate }] key: sign file path, value: first certficate in the sign file }</pre>
     */
    public abstract Value certificates();
}
