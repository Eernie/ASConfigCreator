package nl.eernie.as.variables.logicexpression.tokenizer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import nl.eernie.as.variables.logicexpression.SharedBufferCharSequence;

import org.junit.Test;

public class TokenizerTest2
{
	@Test
	public void test1()
	{
		try
		{
			Tokenizer.tokenize(new SharedBufferCharSequence("("));
			fail("Expected exception");
		}
		catch (Exception e)
		{
			assertEquals(IllegalArgumentException.class, e.getClass());
			assertEquals("Missing closing bracket", e.getMessage());
		}
	}
}