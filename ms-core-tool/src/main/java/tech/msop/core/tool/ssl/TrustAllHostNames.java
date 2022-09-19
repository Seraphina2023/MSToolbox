package tech.msop.core.tool.ssl;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * 信任所有 host name
 *
 * @author ruozhuliufeng
 */
public class TrustAllHostNames implements HostnameVerifier {
    public static final TrustAllHostNames INSTANCE = new TrustAllHostNames();

    @Override
    public boolean verify(String s, SSLSession sslSession) {
        return true;
    }
}
