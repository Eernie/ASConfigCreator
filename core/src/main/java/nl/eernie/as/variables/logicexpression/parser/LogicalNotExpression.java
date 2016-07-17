package nl.eernie.as.variables.logicexpression.parser;

import java.util.List;

import javax.annotation.Nonnull;

class LogicalNotExpression extends LogicalExpression
{
	private final LogicalExpression expression;

	LogicalNotExpression(@Nonnull final LogicalExpression expression)
	{
		this.expression = expression;
	}

	@Override
	public boolean evaluate(@Nonnull final List<? extends CharSequence> contexts)
	{
		return !expression.evaluate(contexts);
	}

	@Override
	public String toString()
	{
		return "(!" + expression.toString() + ")";
	}
}