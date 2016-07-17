package nl.eernie.as.variables.logicexpression.parser;

import java.util.List;

import javax.annotation.Nonnull;

class LogicalLiteral extends LogicalExpression
{
	private final CharSequence context;

	LogicalLiteral(@Nonnull final CharSequence context)
	{
		this.context = context;
	}

	@Override
	public boolean evaluate(@Nonnull final List<? extends CharSequence> contexts)
	{
		return contexts.contains(context);
	}

	@Override
	public String toString()
	{
		return new StringBuilder(context).toString();
	}
}