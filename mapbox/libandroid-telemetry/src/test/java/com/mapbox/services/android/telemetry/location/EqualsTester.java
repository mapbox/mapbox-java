package com.mapbox.services.android.telemetry.location;

/**
 * Created by mapbox2017 on 9/13/17.
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class EqualsTester {

    public static void testReflexive(Object obj) {
        assertEquals("An object should be reflexibly equal to itself.", obj, obj);
    }

    public static void testSymmetric(Object objA, Object objB) {
        assertEquals("Two equal objects should be symetrically equal to each other. A == B", objA, objB);
        assertEquals("Two equal objects should be symetrically equal to each other. B == A", objB, objA);
    }

    public static void testTransitive(Object o1, Object o2, Object o3) {
        assertEquals("Test equal transitively A == B", o1, o2);
        assertEquals("Test equal transitively B == C.", o2, o3);
        assertEquals("Test equal transitively C == A", o1, o3);
    }

    public static void testNonNullity(Object obj) {
        assertFalse("Non-null object should NOT be equals to null!", obj.equals(null));
    }

    public static void testDifferent(Object objA, Object objB) {
        assertFalse("Objects with different values should be different", objA.equals(objB));
    }
}
