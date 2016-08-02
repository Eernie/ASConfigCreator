package nl.eernie.as.variables.logicexpression.tokenizer;

import javax.annotation.Nonnull;

public class ExpressionToken extends Token
{
	ExpressionToken(@Nonnull final CharSequence expression)
	{
		super(expression);
	}

	@Nonnull
	public CharSequence getExpression()
	{
		return getToken();
	}

	@Override
	public String toString()
	{
		return "(" + super.toString() + ")";
	}
}