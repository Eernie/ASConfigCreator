package nl.eernie.as.variables.logicexpression.tokenizer;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nonnull;

import nl.eernie.as.variables.logicexpression.SharedBufferCharSequence;

public final class Tokenizer
{
	private static final char CHAR_GROUP_OPEN = '(';
	private static final char CHAR_GROUP_CLOSE = ')';

	@Nonnull
	private static final String TOKEN_AND = " AND ";

	@Nonnull
	private static final String TOKEN_OR = " OR ";

	@Nonnull
	private static final String TOKEN_NOT = "!";

	private Tokenizer()
	{
	}

	@Nonnull
	public static List<Token> tokenize(@Nonnull final SharedBufferCharSequence expression)
	{
		List<Token> result = new LinkedList<>();
		int previousPosition = 0;
		int i = 0;
		while (i < expression.length())
		{
			char token = expression.charAt(i);
			if (token == CHAR_GROUP_OPEN)
			{
				addPreviousLiteral(expression, result, previousPosition, i);

				int openingBracketPosition = i;
				int closingBracketPosition = findMatchingClosingBracket(expression, i);
				final SharedBufferCharSequence innerExpression = expression.subSequence(openingBracketPosition + 1, closingBracketPosition);
				innerExpression.trim();
				result.add(new ExpressionToken(innerExpression));

				previousPosition = closingBracketPosition + 1;
				i = closingBracketPosition + 1;
			}
			else if (isStartOfToken(expression, TOKEN_AND, i))
			{
				addPreviousLiteral(expression, result, previousPosition, i);

				result.add(Token.AND);

				i += TOKEN_AND.length();

				previousPosition = i;
			}
			else if (isStartOfToken(expression, TOKEN_OR, i))
			{
				addPreviousLiteral(expression, result, previousPosition, i);

				result.add(Token.OR);

				i += TOKEN_OR.length();

				previousPosition = i;
			}
			else if (isStartOfToken(expression, TOKEN_NOT, i))
			{
				addPreviousLiteral(expression, result, previousPosition, i);

				result.add(Token.NOT);

				i += TOKEN_NOT.length();

				previousPosition = i;
			}
			else
			{
				// Do nothing yet
				i++;
			}
		}

		addPreviousLiteral(expression, result, previousPosition, i);

		return result;
	}

	private static void addPreviousLiteral(@Nonnull final SharedBufferCharSequence expression, @Nonnull List<Token> result, int previousPosition, int i)
	{
		final SharedBufferCharSequence literal = expression.subSequence(previousPosition, i);
		literal.trim();
		if (literal.length() != 0)
		{
			result.add(new LiteralToken(literal));
		}
	}

	private static boolean isStartOfToken(@Nonnull final CharSequence expression, @Nonnull final CharSequence token, final int position)
	{
		if (expression.length() < position + token.length())
		{
			return false;
		}

		for (int i = 0; i < token.length(); i++)
		{
			if (expression.charAt(position + i) != token.charAt(i))
			{
				return false;
			}
		}

		return true;
	}

	private static int findMatchingClosingBracket(@Nonnull final CharSequence expression, final int openingBracketPosition)
	{
		for (int i = openingBracketPosition + 1; i < expression.length(); i++)
		{
			char token = expression.charAt(i);
			if (token == CHAR_GROUP_OPEN)
			{
				i = findMatchingClosingBracket(expression, i);
			}
			if (token == CHAR_GROUP_CLOSE)
			{
				return i;
			}
		}
		throw new IllegalArgumentException("Missing closing bracket");
	}
}