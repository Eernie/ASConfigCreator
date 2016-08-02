package nl.eernie.as.variables.logicexpression;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import nl.eernie.as.variables.logicexpression.parser.LogicalExpression;
import nl.eernie.as.variables.logicexpression.parser.Parser;
import nl.eernie.as.variables.logicexpression.tokenizer.Token;
import nl.eernie.as.variables.logicexpression.tokenizer.Tokenizer;

public final class BooleanLogic
{
	private BooleanLogic()
	{
	}

	public static boolean matches(@Nonnull final String expression, @Nonnull List<String> givenLiterals)
	{
		List<SharedBufferCharSequence> literals = convertToSharedBufferCharSequence(givenLiterals);

		final List<Token> tokens = Tokenizer.tokenize(new SharedBufferCharSequence(expression));
		final LogicalExpression logicalExpression = Parser.parse(tokens);
		return logicalExpression.evaluate(literals);
	}

	@Nonnull
	private static List<SharedBufferCharSequence> convertToSharedBufferCharSequence(@Nonnull final List<String> givenLiterals)
	{
		final List<SharedBufferCharSequence> result = new ArrayList<>(givenLiterals.size());
		for (final String givenLiteral : givenLiterals)
		{
			result.add(new SharedBufferCharSequence(givenLiteral));
		}
		return result;
	}
}