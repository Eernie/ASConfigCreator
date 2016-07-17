package nl.eernie.as.variables.logicexpression.tokenizer;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LiteralTokenTest
{
	@Test
	public void testMethods()
	{
		final LiteralToken literalToken = new LiteralToken("Dit is een test");
		assertEquals("Dit is een test", literalToken.getLiteral());
		assertEquals("Dit is een test", literalToken.toString());
	}
}