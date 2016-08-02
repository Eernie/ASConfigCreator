package nl.eernie.as.variables.logicexpression.tokenizer;

import javax.annotation.Nonnull;

public class LiteralToken extends Token
{
	LiteralToken(@Nonnull final CharSequence literal)
	{
		super(literal);
	}

	@Nonnull
	public CharSequence getLiteral()
	{
		return getToken();
	}
}