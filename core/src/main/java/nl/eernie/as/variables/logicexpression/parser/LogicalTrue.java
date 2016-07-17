package nl.eernie.as.variables.logicexpression.parser;

import java.util.List;

import javax.annotation.Nonnull;

public class LogicalTrue extends LogicalExpression
{
	LogicalTrue()
	{
	}

	@Override
	public boolean evaluate(@Nonnull List<? extends CharSequence> contexts)
	{
		return true;
	}

	@Override
	public String toString()
	{
		return "TRUE";
	}
}