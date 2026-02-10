package com.openmpy.server.verifier.application.port;

public interface VerifierPort {

    boolean verify(final String token, final String clientIp);
}
