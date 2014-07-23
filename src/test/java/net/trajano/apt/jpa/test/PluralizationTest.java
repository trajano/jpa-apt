package net.trajano.apt.jpa.test;

import static org.junit.Assert.assertEquals;
import net.trajano.apt.jpa.internal.MetaTableModule;

import org.junit.Test;

/**
 * Tests pluralization.
 */
public class PluralizationTest {

    /**
     * Tests {@link MetaTableModule#pluralize(String)}.
     */
    @Test
    public void testPluralize() {
        assertEquals("Sessions", MetaTableModule.pluralize("Session"));
        assertEquals("Pluses", MetaTableModule.pluralize("Plus"));
        assertEquals("Entities", MetaTableModule.pluralize("Entity"));
        assertEquals("Boxes", MetaTableModule.pluralize("Box"));
    }
}