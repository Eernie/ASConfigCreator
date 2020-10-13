package nl.eernie.as.variables.logicexpression.parser;

import static nl.eernie.as.variables.logicexpression.parser.LogicalExpression.FALSE;
import static nl.eernie.as.variables.logicexpression.parser.LogicalExpression.TRUE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;

import org.junit.Test;

public class LogicalAndExpressionTest
{
	private static final LogicalAndExpression FALSE_AND_FALSE = new LogicalAndExpression(FALSE, FALSE);
	private static final LogicalAndExpression FALSE_AND_TRUE = new LogicalAndExpression(FALSE, TRUE);
	private static final LogicalAndExpression TRUE_AND_FALSE = new LogicalAndExpression(TRUE, FALSE);
	private static final LogicalAndExpression TRUE_AND_TRUE = new LogicalAndExpression(TRUE, TRUE);

	@SuppressWarnings("null")
	@Test
	public void testEvaluate()
	{
		assertFalse(FALSE_AND_FALSE.evaluate(Collections.emptyList()));
		assertFalse(FALSE_AND_TRUE.evaluate(Collections.emptyList()));
		assertFalse(TRUE_AND_FALSE.evaluate(Collections.emptyList()));
		assertTrue(TRUE_AND_TRUE.evaluate(Collections.emptyList()));

		assertFalse(new LogicalAndExpression(TRUE, TRUE, FALSE).evaluate(Collections.emptyList()));
		assertTrue(new LogicalAndExpression(TRUE, TRUE, TRUE).evaluate(Collections.emptyList()));
	}

	@Test
	public void testToString()
	{
		assertEquals("(FALSE AND FALSE)", FALSE_AND_FALSE.toString());
		assertEquals("(FALSE AND TRUE)", FALSE_AND_TRUE.toString());
		assertEquals("(TRUE AND FALSE)", TRUE_AND_FALSE.toString());
		assertEquals("(TRUE AND TRUE)", TRUE_AND_TRUE.toString());
	}
}
