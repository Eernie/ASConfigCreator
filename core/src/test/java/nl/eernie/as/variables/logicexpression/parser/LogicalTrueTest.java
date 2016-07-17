package nl.eernie.as.variables.logicexpression.parser;

import static nl.eernie.as.variables.logicexpression.parser.LogicalExpression.TRUE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;

import org.junit.Test;

public class LogicalTrueTest
{
	@SuppressWarnings("null")
	@Test
	public void testEvaluate()
	{
		assertTrue(TRUE.evaluate(Collections.<String> emptyList()));
	}

	@Test
	public void testToString()
	{
		assertEquals("TRUE", TRUE.toString());
	}
}