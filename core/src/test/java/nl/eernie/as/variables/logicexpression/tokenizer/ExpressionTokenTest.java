package nl.eernie.as.variables.logicexpression.tokenizer;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ExpressionTokenTest
{
	@Test
	public void testMethods()
	{
		final ExpressionToken expressionToken = new ExpressionToken("Dit is een test");
		assertEquals("Dit is een test", expressionToken.getExpression());
		assertEquals("(Dit is een test)", expressionToken.toString());
	}
}