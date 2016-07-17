package nl.eernie.as.variables.logicexpression.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

public class LogicalLiteralTest
{
	private static final LogicalLiteral TEST_LOGICAL_LITERAL = new LogicalLiteral("Test");

	@SuppressWarnings("null")
	@Test
	public void testEvaluation()
	{
		assertFalse(TEST_LOGICAL_LITERAL.evaluate(Collections.<CharSequence> emptyList()));
		assertFalse(TEST_LOGICAL_LITERAL.evaluate(Arrays.asList("OtherContext")));
		assertTrue(TEST_LOGICAL_LITERAL.evaluate(Arrays.asList("Test")));
	}

	@Test
	public void testToString()
	{
		assertEquals("Test", TEST_LOGICAL_LITERAL.toString());
	}
}