package io.github.transfusion.app_info_java_graalvm.AppInfo;

import io.github.transfusion.app_info_java_graalvm.AbstractPolyglotAdapter;
import org.graalvm.polyglot.Value;

public abstract class APK extends AbstractPolyglotAdapter {
    public static abstract class Sign extends AbstractPolyglotAdapter {
        public abstract String path();

        /**
         * @return <a href="https://ruby-doc.org/stdlib-3.1.1/libdoc/openssl/rdoc/OpenSSL/PKCS7.html">OpenSSL::PKCS7</a>
         */
        public abstract Value sign();
    }


    public static abstract class Certificate extends AbstractPolyglotAdapter {
        public abstract String path();

        /**
         * @return <a href="https://docs.ruby-lang.org/en/master/OpenSSL/X509/Certificate.html">OpenSSL::X509::Certificate</a>
         */
        public abstract Value certificate();
    }

}
