package nl.eernie.as.variables.logicexpression.parser;

import java.util.List;

import javax.annotation.Nonnull;

class LogicalAndExpression extends LogicalExpression
{
	private final LogicalExpression[] expressions;

	LogicalAndExpression(@Nonnull final LogicalExpression left, @Nonnull final LogicalExpression right, @Nonnull final LogicalExpression... rest)
	{
		expressions = new LogicalExpression[rest.length + 2];
		expressions[0] = left;
		expressions[1] = right;
		System.arraycopy(rest, 0, expressions, 2, rest.length);
	}

	@Override
	public boolean evaluate(@Nonnull final List<? extends CharSequence> contexts)
	{
		boolean result = expressions[0].evaluate(contexts);
		for (int i = 1; i < expressions.length; i++)
		{
			if (result == false)
			{
				return false;
			}
			result = result && expressions[i].evaluate(contexts);
		}
		return result;
	}

	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder();
		sb.append('(');
		sb.append(expressions[0].toString());
		for (int i = 1; i < expressions.length; i++)
		{
			sb.append(" AND ");
			sb.append(expressions[i].toString());
		}
		sb.append(')');
		return sb.toString();
	}
}