package nl.eernie.as.variables.logicexpression.tokenizer;

import static nl.eernie.as.variables.logicexpression.tokenizer.Token.AND;
import static nl.eernie.as.variables.logicexpression.tokenizer.Token.NOT;
import static nl.eernie.as.variables.logicexpression.tokenizer.Token.OR;
import static org.junit.Assert.assertArrayEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import nl.eernie.as.variables.logicexpression.SharedBufferCharSequence;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class TokenizerTest1
{
	public static final Token A = new LiteralToken(new SharedBufferCharSequence("A"));
	public static final Token B = new LiteralToken(new SharedBufferCharSequence("B"));
	public static final Token C = new LiteralToken(new SharedBufferCharSequence("C"));
	public static final Token A_AND_B = new ExpressionToken(new SharedBufferCharSequence("A AND B"));

	@Parameters
	public static Collection<Object[]> data()
	{
		// @formatter:off
		return Arrays.asList(new Object[][] {
			{ "A",             new Token[] { A }},
			{ "!A",            new Token[] { NOT, A }},
			{ "A OR B",        new Token[] { A, OR, B }},
			{ "A AND B",       new Token[] { A, AND, B }},
			{ "(A AND B)",     new Token[] { A_AND_B }},
			{ "A (A AND B) B", new Token[] { A, A_AND_B, B}},

			// Some chaining
			{ "A AND B AND C", new Token[] { A, AND, B, AND, C }},
			{ "A OR B OR C",   new Token[] { A, OR, B, OR, C }},
			{ "A ! B !C",      new Token[] { A, NOT, B, NOT, C }},

			{ " ( ( A ) ) ",   new Token[] { new ExpressionToken(new SharedBufferCharSequence("( A )")) }}, // Note that inner spaces are not trimmed (they are when tokenizing the ExpressionToken again
			{ "()",            new Token[] { new ExpressionToken(new SharedBufferCharSequence("")) }},

			{ " Token with space ", new Token[] { new LiteralToken(new SharedBufferCharSequence("Token with space")) }},
			{ " AND ! OR ",         new Token[] { AND, NOT, OR }},
			{ "  A  OR  B  ",       new Token[] { A, OR, B }}, // Test trimming of literals
			{ "   ",                new Token[] { }},
			{ "",                   new Token[] { }},

			{ "(A AND B) AND !A AND B OR A", new Token[] { A_AND_B, AND, NOT, A, AND, B, OR, A }}, // Java operators in correct order
			{ "A OR B AND !(A AND B)",       new Token[] { A, OR, B, AND, NOT, A_AND_B }}, // Java operators in reverse order
		});
		// @formatter:on
	}

	@Parameter(value = 0)
	public String inputExpression;

	@Parameter(value = 1)
	public Token[] expectedOutput;

	@SuppressWarnings("null")
	@Test
	public void test()
	{
		final List<Token> actualOutput = Tokenizer.tokenize(new SharedBufferCharSequence(inputExpression));
		assertArrayEquals(expectedOutput, actualOutput.toArray());
	}
}