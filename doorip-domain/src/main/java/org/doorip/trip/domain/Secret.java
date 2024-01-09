package org.doorip.trip.domain;

public enum Secret {
    OUR, MY;

    public static Secret of(boolean isSecret) {
        if (isSecret) {
            return OUR;
        }
        return MY;
    }

    public static boolean of(Secret secret) {
        return !secret.equals(OUR);
    }
}
