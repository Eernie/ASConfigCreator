package nl.eernie.as.variables.logicexpression.tokenizer;

import javax.annotation.Nonnull;

public class Token
{
	@Nonnull
	public static final Token AND = new Token("AND");

	@Nonnull
	public static final Token OR = new Token("OR");

	@Nonnull
	public static final Token NOT = new Token("!");

	@Nonnull
	private final CharSequence token;

	Token(@Nonnull final CharSequence token)
	{
		this.token = token;
	}

	@Nonnull
	CharSequence getToken()
	{
		return token;
	}

	@Override
	public String toString()
	{
		return new StringBuilder(token).toString();
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (token == null ? 0 : token.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		Token other = (Token) obj;
		if (token == null)
		{
			if (other.token != null)
			{
				return false;
			}
		}
		else if (!token.equals(other.token))
		{
			return false;
		}
		return true;
	}
}