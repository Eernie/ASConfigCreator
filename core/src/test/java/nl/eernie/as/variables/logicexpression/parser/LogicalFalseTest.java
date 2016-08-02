package nl.eernie.as.variables.logicexpression.parser;

import static nl.eernie.as.variables.logicexpression.parser.LogicalExpression.FALSE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Collections;

import org.junit.Test;

public class LogicalFalseTest
{
	@SuppressWarnings("null")
	@Test
	public void testEvaluate()
	{
		assertFalse(FALSE.evaluate(Collections.<CharSequence> emptyList()));
	}

	@Test
	public void testToString()
	{
		assertEquals("FALSE", FALSE.toString());
	}
}