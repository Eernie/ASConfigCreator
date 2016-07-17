package nl.eernie.as.variables.logicexpression.parser;

import java.util.List;

import javax.annotation.Nonnull;

public class LogicalFalse extends LogicalExpression
{
	LogicalFalse()
	{
	}

	@Override
	public boolean evaluate(@Nonnull List<? extends CharSequence> contexts)
	{
		return false;
	}

	@Override
	public String toString()
	{
		return "FALSE";
	}
}