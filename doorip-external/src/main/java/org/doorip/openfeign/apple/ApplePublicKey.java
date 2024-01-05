package org.doorip.openfeign.apple;

public record ApplePublicKey(
        String kty,
        String kid,
        String use,
        String alg,
        String n,
        String e) {
}

