package nl.eernie.as.variables.logicexpression.parser;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.annotation.Nonnull;

import nl.eernie.as.variables.logicexpression.SharedBufferCharSequence;
import nl.eernie.as.variables.logicexpression.tokenizer.ExpressionToken;
import nl.eernie.as.variables.logicexpression.tokenizer.LiteralToken;
import nl.eernie.as.variables.logicexpression.tokenizer.Token;
import nl.eernie.as.variables.logicexpression.tokenizer.Tokenizer;

public final class Parser
{
	private Parser()
	{
	}

	@Nonnull
	public static LogicalExpression parse(@Nonnull final List<Token> tokens)
	{
		return parseOr(tokens);
	}

	@Nonnull
	private static Queue<List<Token>> splitOnToken(@Nonnull final List<Token> tokens, @Nonnull final Token splitToken)
	{
		final Queue<List<Token>> result = new LinkedList<>();
		int previousPosition = 0;
		for (int i = 0; i < tokens.size(); i++)
		{
			final Token currentToken = tokens.get(i);
			if (currentToken == splitToken)
			{
				result.add(tokens.subList(previousPosition, i));
				previousPosition = i + 1;
			}
		}
		result.add(tokens.subList(previousPosition, tokens.size()));
		return result;
	}

	@Nonnull
	private static LogicalExpression parseOr(@Nonnull final List<Token> tokens)
	{
		// TODO: Support varargs for AND and OR

		final Queue<List<Token>> splitTokens = splitOnToken(tokens, Token.OR);
		final List<Token> lhsTokens = splitTokens.remove();
		if (lhsTokens.isEmpty())
		{
			throw new IllegalArgumentException("Missing expression on the left-hand side of the OR operator");
		}

		LogicalExpression lhs = parseAnd(lhsTokens);

		while (!splitTokens.isEmpty())
		{
			final List<Token> rhsTokens = splitTokens.remove();
			if (rhsTokens.isEmpty())
			{
				throw new IllegalArgumentException("Missing expression on the right-hand side of the OR operator");
			}

			final LogicalExpression rhs = parseAnd(rhsTokens);
			lhs = new LogicalOrExpression(lhs, rhs);
		}

		return lhs;
	}

	@Nonnull
	private static LogicalExpression parseAnd(@Nonnull final List<Token> tokens)
	{
		// TODO: Support varargs for AND and OR

		final Queue<List<Token>> splitTokens = splitOnToken(tokens, Token.AND);

		final List<Token> lhsTokens = splitTokens.remove();
		if (lhsTokens.isEmpty())
		{
			throw new IllegalArgumentException("Missing expression on the left-hand side of the AND operator");
		}

		LogicalExpression lhs = parseNot(lhsTokens);

		while (!splitTokens.isEmpty())
		{
			final List<Token> rhsTokens = splitTokens.remove();
			if (rhsTokens.isEmpty())
			{
				throw new IllegalArgumentException("Missing expression on the right-hand side of the AND operator");
			}

			final LogicalExpression rhs = parseNot(rhsTokens);
			lhs = new LogicalAndExpression(lhs, rhs);
		}

		return lhs;
	}

	@Nonnull
	private static LogicalExpression parseNot(@Nonnull final List<Token> tokens)
	{
		if (tokens.size() == 1)
		{
			Token firstToken = tokens.get(0);
			if (firstToken == Token.NOT)
			{
				throw new IllegalArgumentException("No expression after the NOT operator");
			}
			else
			{
				return parseSingle(firstToken);
			}
		}
		else if (tokens.size() == 2)
		{
			Token firstToken = tokens.get(0);
			Token secondToken = tokens.get(1);
			if (firstToken == Token.NOT)
			{
				final LogicalExpression inverseExpression = parseSingle(secondToken);
				return new LogicalNotExpression(inverseExpression);
			}
		}

		throw new IllegalArgumentException("Too many tokens left");
	}

	@Nonnull
	private static LogicalExpression parseSingle(@Nonnull final Token token)
	{
		if (token instanceof LiteralToken)
		{
			return parseSingle((LiteralToken) token);
		}
		else if (token instanceof ExpressionToken)
		{
			return parseSingle((ExpressionToken) token);
		}
		else
		{
			throw new IllegalArgumentException("Expected first token to be either literal or group expression.");
		}
	}

	@Nonnull
	private static LogicalLiteral parseSingle(@Nonnull final LiteralToken token)
	{
		return new LogicalLiteral(token.getLiteral());
	}

	@Nonnull
	private static LogicalExpression parseSingle(@Nonnull final ExpressionToken token)
	{
		final SharedBufferCharSequence expression;
		if (token.getExpression() instanceof SharedBufferCharSequence)
		{
			expression = (SharedBufferCharSequence) token.getExpression();
		}
		else
		{
			expression = new SharedBufferCharSequence(token.getExpression());
		}
		final List<Token> tokens = Tokenizer.tokenize(expression);
		return Parser.parse(tokens);
	}
}