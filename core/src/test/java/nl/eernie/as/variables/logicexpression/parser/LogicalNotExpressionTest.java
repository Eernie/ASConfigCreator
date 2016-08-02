package nl.eernie.as.variables.logicexpression.parser;

import static nl.eernie.as.variables.logicexpression.parser.LogicalExpression.FALSE;
import static nl.eernie.as.variables.logicexpression.parser.LogicalExpression.TRUE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;

import org.junit.Test;

public class LogicalNotExpressionTest
{
	private static final LogicalNotExpression NOT_FALSE = new LogicalNotExpression(FALSE);
	private static final LogicalNotExpression NOT_TRUE = new LogicalNotExpression(TRUE);

	@SuppressWarnings("null")
	@Test
	public void testEvaluate()
	{
		assertTrue(NOT_FALSE.evaluate(Collections.<CharSequence> emptyList()));
		assertFalse(NOT_TRUE.evaluate(Collections.<CharSequence> emptyList()));
	}

	@Test
	public void testToString()
	{
		assertEquals("(!FALSE)", NOT_FALSE.toString());
		assertEquals("(!TRUE)", NOT_TRUE.toString());
	}
}