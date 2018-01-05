package org.jasig.cas.adaptors.x509.authentication.principal;

import com.google.common.collect.ImmutableList;
import org.jasig.cas.adaptors.x509.util.CertUtils;
import org.jasig.cas.authentication.AbstractCredential;

import javax.validation.constraints.NotNull;
import java.security.cert.X509Certificate;

/**
 * An X.509 certificate credential.
 *
 * @author Scott Battaglia
 * @author Marvin S. Addison
 * @since 3.0.0
 *
 */
public final class X509CertificateCredential extends AbstractCredential {

    /** Unique Id for serialization. */
    private static final long serialVersionUID = 631753409512746474L;

    /** The collection of certificates sent with the request. */
    private final X509Certificate[] certificates;

    /** The certificate that we actually use. */
    private X509Certificate certificate;

    /**
     * Instantiates a new x509 certificate credential.
     *
     * @param certificates the certificates
     */
    public X509CertificateCredential(@NotNull final X509Certificate[] certificates) {
        this.certificates = ImmutableList.copyOf(certificates).toArray(new X509Certificate[certificates.length]);
    }

    public X509Certificate[] getCertificates() {
        return ImmutableList.copyOf(this.certificates).toArray(new X509Certificate[this.certificates.length]);
    }

    public void setCertificate(final X509Certificate certificate) {
        this.certificate = certificate;
    }

    public X509Certificate getCertificate() {
        return this.certificate;
    }

    @Override
    public String getId() {
        X509Certificate cert = null;
        if (this.certificate != null) {
            cert = this.certificate;
        } else if (this.certificates.length > 0) {
            cert = this.certificates[0];
        }

        if (cert != null) {
            return CertUtils.toString(cert);
        }
        return UNKNOWN_ID;
    }
}
