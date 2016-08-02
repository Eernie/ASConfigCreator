package nl.eernie.as.variables.logicexpression.parser;

import static nl.eernie.as.variables.logicexpression.parser.LogicalExpression.FALSE;
import static nl.eernie.as.variables.logicexpression.parser.LogicalExpression.TRUE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;

import org.junit.Test;

public class LogicalOrExpressionTest
{
	private static final LogicalOrExpression FALSE_OR_FALSE = new LogicalOrExpression(FALSE, FALSE);
	private static final LogicalOrExpression FALSE_OR_TRUE = new LogicalOrExpression(FALSE, TRUE);
	private static final LogicalOrExpression TRUE_OR_FALSE = new LogicalOrExpression(TRUE, FALSE);
	private static final LogicalOrExpression TRUE_OR_TRUE = new LogicalOrExpression(TRUE, TRUE);

	@SuppressWarnings("null")
	@Test
	public void testEvaluate()
	{
		assertFalse(FALSE_OR_FALSE.evaluate(Collections.<CharSequence> emptyList()));
		assertTrue(FALSE_OR_TRUE.evaluate(Collections.<CharSequence> emptyList()));
		assertTrue(TRUE_OR_FALSE.evaluate(Collections.<CharSequence> emptyList()));
		assertTrue(TRUE_OR_TRUE.evaluate(Collections.<CharSequence> emptyList()));

		assertFalse(new LogicalOrExpression(FALSE, FALSE, FALSE).evaluate(Collections.<CharSequence> emptyList()));
		assertTrue(new LogicalOrExpression(FALSE, FALSE, TRUE).evaluate(Collections.<CharSequence> emptyList()));
	}

	@Test
	public void testToString()
	{
		assertEquals("(FALSE OR FALSE)", FALSE_OR_FALSE.toString());
		assertEquals("(FALSE OR TRUE)", FALSE_OR_TRUE.toString());
		assertEquals("(TRUE OR FALSE)", TRUE_OR_FALSE.toString());
		assertEquals("(TRUE OR TRUE)", TRUE_OR_TRUE.toString());
	}
}