package com.openmpy.server.document.application.verify.port;

public interface Verifier {

    boolean verify(final String token, final String clientIp);
}
