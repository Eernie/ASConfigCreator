package nl.eernie.as.variables.logicexpression;

import javax.annotation.Nonnull;

public class SharedBufferCharSequence implements CharSequence
{
	@Nonnull
	private final CharSequence buffer;

	private int bufferStart;
	private int bufferEnd;

	public SharedBufferCharSequence(@Nonnull final CharSequence buffer)
	{
		this(buffer, 0, buffer.length());
	}

	private SharedBufferCharSequence(@Nonnull final CharSequence buffer, int start, int end)
	{
		this.buffer = buffer;
		bufferStart = start;
		bufferEnd = end;
	}

	@Override
	public int length()
	{
		return bufferEnd - bufferStart;
	}

	@Override
	public char charAt(int index)
	{
		if (bufferEnd <= index)
		{
			throw new StringIndexOutOfBoundsException(index);
		}
		return buffer.charAt(bufferStart + index);
	}

	@Override
	public SharedBufferCharSequence subSequence(int start, int end)
	{
		if (start < 0)
		{
			throw new StringIndexOutOfBoundsException(start);
		}
		if (end > length() || start > end)
		{
			throw new StringIndexOutOfBoundsException(end);
		}
		return new SharedBufferCharSequence(buffer, bufferStart + start, bufferStart + end);
	}

	public void trim()
	{
		while (bufferStart < bufferEnd && Character.isWhitespace(buffer.charAt(bufferStart)))
		{
			bufferStart++;
		}
		while (bufferEnd > bufferStart && Character.isWhitespace(buffer.charAt(bufferEnd - 1)))
		{
			bufferEnd--;
		}
	}

	@Override
	public String toString()
	{
		return buffer.subSequence(bufferStart, bufferEnd).toString();
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + buffer.hashCode();
		result = prime * result + bufferEnd;
		result = prime * result + bufferStart;
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
		SharedBufferCharSequence other = (SharedBufferCharSequence) obj;

		if (length() != other.length())
		{
			return false;
		}

		for (int i = 0; i < length(); i++)
		{
			if (other.charAt(i) != charAt(i))
			{
				return false;
			}
		}

		return true;
	}
}