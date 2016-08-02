package nl.eernie.as.variables.logicexpression.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;

import nl.eernie.as.variables.logicexpression.SharedBufferCharSequence;
import nl.eernie.as.variables.logicexpression.tokenizer.Token;
import nl.eernie.as.variables.logicexpression.tokenizer.TokenizerTest1;

import org.junit.Test;

@SuppressWarnings("null")
public class ParserTest
{
	@Test
	public void testAnd()
	{
		final LogicalExpression actualExpression = Parser.parse(Arrays.asList(TokenizerTest1.A, Token.AND, TokenizerTest1.B));
		assertEquals(false, actualExpression.evaluate(Collections.<CharSequence> emptyList()));
		assertEquals(false, actualExpression.evaluate(Arrays.asList(new SharedBufferCharSequence("A"))));
		assertEquals(false, actualExpression.evaluate(Arrays.asList(new SharedBufferCharSequence("B"))));
		assertEquals(true, actualExpression.evaluate(Arrays.asList(new SharedBufferCharSequence("A"), new SharedBufferCharSequence("B"))));
	}

	@Test
	public void testAndEmptyLHS()
	{
		try
		{
			Parser.parse(Arrays.asList(Token.AND, TokenizerTest1.B));
			fail("IllegalArgumentException expected");
		}
		catch (Exception e)
		{
			assertEquals(IllegalArgumentException.class, e.getClass());
			assertEquals("Missing expression on the left-hand side of the AND operator", e.getMessage());
		}
	}

	@Test
	public void testAndEmptyRHS()
	{
		try
		{
			Parser.parse(Arrays.asList(TokenizerTest1.A, Token.AND));
			fail("IllegalArgumentException expected");
		}
		catch (Exception e)
		{
			assertEquals(IllegalArgumentException.class, e.getClass());
			assertEquals("Missing expression on the right-hand side of the AND operator", e.getMessage());
		}
	}

	@Test
	public void testOr()
	{
		final LogicalExpression actualExpression = Parser.parse(Arrays.asList(TokenizerTest1.A, Token.OR, TokenizerTest1.B));
		assertEquals(false, actualExpression.evaluate(Collections.<CharSequence> emptyList()));
		assertEquals(true, actualExpression.evaluate(Arrays.asList(new SharedBufferCharSequence("A"))));
		assertEquals(true, actualExpression.evaluate(Arrays.asList(new SharedBufferCharSequence("B"))));
		assertEquals(true, actualExpression.evaluate(Arrays.asList(new SharedBufferCharSequence("A"), new SharedBufferCharSequence("B"))));
	}

	@Test
	public void testOrEmptyLHS()
	{
		try
		{
			Parser.parse(Arrays.asList(Token.OR, TokenizerTest1.B));
			fail("IllegalArgumentException expected");
		}
		catch (Exception e)
		{
			assertEquals(IllegalArgumentException.class, e.getClass());
			assertEquals("Missing expression on the left-hand side of the OR operator", e.getMessage());
		}
	}

	@Test
	public void testOrEmptyRHS()
	{
		try
		{
			Parser.parse(Arrays.asList(TokenizerTest1.A, Token.OR));
			fail("IllegalArgumentException expected");
		}
		catch (Exception e)
		{
			assertEquals(IllegalArgumentException.class, e.getClass());
			assertEquals("Missing expression on the right-hand side of the OR operator", e.getMessage());
		}
	}

	@Test
	public void testNot()
	{
		final LogicalExpression actualExpression = Parser.parse(Arrays.asList(Token.NOT, TokenizerTest1.A));
		assertEquals(true, actualExpression.evaluate(Collections.<CharSequence> emptyList()));
		assertEquals(false, actualExpression.evaluate(Arrays.asList(new SharedBufferCharSequence("A"))));
		assertEquals(true, actualExpression.evaluate(Arrays.asList(new SharedBufferCharSequence("B"))));
	}

	@Test
	public void testNotNoTokenAfterOperator()
	{
		try
		{
			Parser.parse(Arrays.asList(Token.NOT));
			fail("IllegalArgumentException expected");
		}
		catch (Exception e)
		{
			assertEquals(IllegalArgumentException.class, e.getClass());
			assertEquals("No expression after the NOT operator", e.getMessage());
		}
	}

	@Test
	public void testNotTooManyTokens1()
	{
		try
		{
			Parser.parse(Arrays.asList(Token.NOT, TokenizerTest1.A, TokenizerTest1.B));
			fail("IllegalArgumentException expected");
		}
		catch (Exception e)
		{
			assertEquals(IllegalArgumentException.class, e.getClass());
			assertEquals("Too many tokens left", e.getMessage());
		}
	}

	@Test
	public void testNotTooManyTokens2()
	{
		try
		{
			Parser.parse(Arrays.asList(TokenizerTest1.A, TokenizerTest1.B));
			fail("IllegalArgumentException expected");
		}
		catch (Exception e)
		{
			assertEquals(IllegalArgumentException.class, e.getClass());
			assertEquals("Too many tokens left", e.getMessage());
		}
	}

	@Test
	public void testGroupExpression()
	{
		final LogicalExpression actualExpression = Parser.parse(Arrays.asList(Token.NOT, TokenizerTest1.A_AND_B));
		assertEquals(true, actualExpression.evaluate(Collections.<CharSequence> emptyList()));
		assertEquals(true, actualExpression.evaluate(Arrays.asList(new SharedBufferCharSequence("A"))));
		assertEquals(true, actualExpression.evaluate(Arrays.asList(new SharedBufferCharSequence("B"))));
		assertEquals(false, actualExpression.evaluate(Arrays.asList(new SharedBufferCharSequence("A"), new SharedBufferCharSequence("B"))));
	}
}