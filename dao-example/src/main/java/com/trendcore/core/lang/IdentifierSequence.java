package com.trendcore.core.lang;

import com.trendcore.core.domain.Identifiable;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class IdentifierSequence {


    public static final IdentifierSequence INSTANCE = new IdentifierSequence();

    private static final AtomicLong ID_SEQUENCE = new AtomicLong(System.nanoTime());

    public synchronized Identifiable<Long> setLongId(final Identifiable<Long> identifiableObject) {
        identifiableObject.setId(System.nanoTime());
        return identifiableObject;
    }

    public Identifiable<Long> setSequentialLongId(final Identifiable<Long> identifiableObject) {
        identifiableObject.setId(ID_SEQUENCE.incrementAndGet());
        return identifiableObject;
    }

    public Identifiable<String> setStringId(final Identifiable<String> identifiableObject) {
        identifiableObject.setId(UUID.randomUUID().toString());
        return identifiableObject;
    }


}
