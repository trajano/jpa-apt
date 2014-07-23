package net.trajano.apt.jpa.test;

import static org.junit.Assert.assertEquals;
import net.trajano.apt.jpa.internal.MetaTableModel;

import org.junit.Test;

/**
 * Tests the module.
 */
public class PluralizationTest {

	/**
	 * Tests the module method.
	 */
	@Test
	public void testPluralize() {
		assertEquals("Sessions", MetaTableModel.pluralize("Session"));
		assertEquals("Pluses", MetaTableModel.pluralize("Plus"));
		assertEquals("Entities", MetaTableModel.pluralize("Entity"));
		assertEquals("Boxes", MetaTableModel.pluralize("Box"));
	}
}