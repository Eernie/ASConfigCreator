package nl.eernie.as.variables.logicexpression.parser;

import java.util.List;

import javax.annotation.Nonnull;

public abstract class LogicalExpression
{
	@Nonnull
	public static final LogicalExpression TRUE = new LogicalTrue();

	@Nonnull
	public static final LogicalExpression FALSE = new LogicalFalse();

	public abstract boolean evaluate(@Nonnull final List<? extends CharSequence> contexts);
}