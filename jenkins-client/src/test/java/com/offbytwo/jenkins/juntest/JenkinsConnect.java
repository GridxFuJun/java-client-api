package com.offbytwo.jenkins.juntest;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.client.JenkinsHttpClient;
import org.apache.http.Header;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;

import javax.net.ssl.SSLContext;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;


public class JenkinsConnect {

    static final String JENKINS_URL = "https://jenkins-qa.internal.gridx.com:443";
    static final String JENKINS_TOKEN = "Basic a2FiZWw6MTFkNWEzZGJiMDY5ZDI2ZjgxOTYwMDBiZWU1YzE1ODg2NA==";

    private JenkinsConnect() {
    }


    public static JenkinsHttpClient getClient() {
        JenkinsHttpClient jenkinsHttpClient = null;
        try {
            Header header = new BasicHeader("Authorization", JENKINS_TOKEN);
            HttpClientBuilder builder = HttpClientBuilder.create().setDefaultHeaders(Arrays.asList(header));
            jenkinsHttpClient = new JenkinsHttpClient(new URI(JENKINS_URL), builder);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return jenkinsHttpClient;
    }

    /**
     * 连接 Jenkins
     */
    public static JenkinsServer connection() {
        JenkinsServer jenkinsServer = null;
        try {
            Header header = new BasicHeader("Authorization", JENKINS_TOKEN);
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null,
                    new TrustStrategy() {
                        public boolean isTrusted(X509Certificate[] chain,
                                                 String authType) throws CertificateException {
                            return true;
                        }
                    }).build();

            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    sslContext, new String[]{"TLSv1"}, null,
                    SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            HttpClientBuilder builder = HttpClientBuilder.create().setDefaultHeaders(Arrays.asList(header)).setSSLSocketFactory(sslsf);

            JenkinsHttpClient jenkinsHttpClient = new JenkinsHttpClient(new URI(JENKINS_URL), builder);
            jenkinsServer = new JenkinsServer(jenkinsHttpClient);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return jenkinsServer;
    }
}
