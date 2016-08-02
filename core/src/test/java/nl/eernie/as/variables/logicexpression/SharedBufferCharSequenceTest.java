package nl.eernie.as.variables.logicexpression;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class SharedBufferCharSequenceTest
{
	private static final SharedBufferCharSequence LONG_BUFFER = new SharedBufferCharSequence("This is the complete buffer");

	private static final SharedBufferCharSequence X1 = new SharedBufferCharSequence("X");
	private static final SharedBufferCharSequence X2 = new SharedBufferCharSequence("X");
	private static final SharedBufferCharSequence X3 = new SharedBufferCharSequence("X");
	private static final SharedBufferCharSequence Y = new SharedBufferCharSequence("Y");

	@Test
	public void testLength()
	{
		assertEquals("Length method incorrect", 27, LONG_BUFFER.length());
	}

	@Test
	public void testToString()
	{
		assertEquals("ToString method incorrect", "This is the complete buffer", LONG_BUFFER.toString());
	}

	@Test
	public void testSubSequence()
	{
		final SharedBufferCharSequence testSubSequence = LONG_BUFFER.subSequence(12, 20);
		assertEquals("complete", testSubSequence.toString());

		final SharedBufferCharSequence testSubSequenceStart = LONG_BUFFER.subSequence(0, 4);
		assertEquals("This", testSubSequenceStart.toString());

		final SharedBufferCharSequence testSubSequenceEnd = LONG_BUFFER.subSequence(21, 27);
		assertEquals("buffer", testSubSequenceEnd.toString());

		try
		{
			LONG_BUFFER.subSequence(-1, 5);
			fail("StringIndexOutOfBoundsException expected");
		}
		catch (StringIndexOutOfBoundsException e)
		{
			assertEquals(StringIndexOutOfBoundsException.class, e.getClass());
			assertEquals(e.getMessage(), "String index out of range: -1");
		}

		try
		{
			LONG_BUFFER.subSequence(20, 28);
			fail("StringIndexOutOfBoundsException expected");
		}
		catch (StringIndexOutOfBoundsException e)
		{
			assertEquals(StringIndexOutOfBoundsException.class, e.getClass());
			assertEquals(e.getMessage(), "String index out of range: 28");
		}
	}

	@Test
	public void testTrim()
	{
		final SharedBufferCharSequence trimBothSides = new SharedBufferCharSequence(" ABC ");
		trimBothSides.trim();
		assertEquals("Trim both sides failed", "ABC", trimBothSides.toString());

		final SharedBufferCharSequence trimTwice = trimBothSides;
		trimTwice.trim();
		assertEquals("Trim twice failed", "ABC", trimTwice.toString());

		final SharedBufferCharSequence trimTheEnd = new SharedBufferCharSequence("ABC  ");
		trimTheEnd.trim();
		assertEquals("Trim the end failed", "ABC", trimTheEnd.toString());

		final SharedBufferCharSequence trimTheBeginning = new SharedBufferCharSequence("  ABC");
		trimTheBeginning.trim();
		assertEquals("Trim the beginning failed", "ABC", trimTheBeginning.toString());

		final SharedBufferCharSequence trimOnlySpaces = new SharedBufferCharSequence("   ");
		trimOnlySpaces.trim();
		assertEquals("Trim only spaces failed", "", trimOnlySpaces.toString());

		final SharedBufferCharSequence trimEmptyString = new SharedBufferCharSequence("");
		trimEmptyString.trim();
		assertEquals("Trim empty string failed", "", trimEmptyString.toString());

		final SharedBufferCharSequence trimNoSpaces = new SharedBufferCharSequence("ABC");
		trimNoSpaces.trim();
		assertEquals("Trim nothing failed", "ABC", trimNoSpaces.toString());
	}

	@Test
	public void testCharAt()
	{
		assertEquals("CharAt method incorrect", 'T', LONG_BUFFER.charAt(0));
		assertEquals("CharAt method incorrect", 'r', LONG_BUFFER.charAt(26));

		try
		{
			LONG_BUFFER.charAt(-1);
			fail("StringIndexOutOfBoundsException expected");
		}
		catch (StringIndexOutOfBoundsException e)
		{
			assertEquals(StringIndexOutOfBoundsException.class, e.getClass());
			assertEquals(e.getMessage(), "String index out of range: -1");
		}

		try
		{
			LONG_BUFFER.charAt(27);
			fail("StringIndexOutOfBoundsException expected");
		}
		catch (StringIndexOutOfBoundsException e)
		{
			assertEquals(StringIndexOutOfBoundsException.class, e.getClass());
			assertEquals(e.getMessage(), "String index out of range: 27");
		}
	}

	/*
	 * http://docs.oracle.com/javase/7/docs/api/java/lang/Object.html#equals(java.lang.Object)
	 *
	 * It is reflexive: for any non-null reference value x, x.equals(x) should return true.
	 * It is symmetric: for any non-null reference values x and y, x.equals(y) should return true if and only if y.equals(x) returns true.
	 * It is transitive: for any non-null reference values x, y, and z, if x.equals(y) returns true and y.equals(z) returns true, then x.equals(z) should return true.
	 * It is consistent: for any non-null reference values x and y, multiple invocations of x.equals(y) consistently return true or consistently return false, provided no information used in equals comparisons on the objects is modified.
	 * For any non-null reference value x, x.equals(null) should return false.
	 */

	@Test
	public void testEqualsIsReflexive()
	{
		assertTrue("Reflexive fail", X1.equals(X1));
	}

	@Test
	public void testEqualsIsSymmetric()
	{
		assertTrue("Symmetric fail", X1.equals(X2));
		assertTrue("Symmetric fail", X2.equals(X1));
	}

	@Test
	public void testEqualsIsTransitive()
	{
		assertTrue("Transitive fail", X1.equals(X2));
		assertTrue("Transitive fail", X2.equals(X3));
		assertTrue("Transitive fail", X1.equals(X3));
	}

	@Test
	public void testNotEqualsIncompatibleType()
	{
		assertFalse("Incompatibe type", X1.equals(Y));
	}

	@Test
	public void testEqualsIncompatibleType()
	{
		assertFalse("Incompatibe type", X1.equals(Boolean.TRUE));
	}

	@Test
	public void testEqualsToNullIs()
	{
		assertFalse("Equals to NULL should be false", X1.equals(null));
	}

	@Test
	public void testEqualsIsConsistent()
	{
		assertTrue("Consistency fail", X1.equals(X2));
		assertTrue("Consistency fail", X1.equals(X2));
		assertTrue("Consistency fail", X1.equals(X2));

		assertFalse("Consistency fail", X1.equals(Y));
		assertFalse("Consistency fail", X1.equals(Y));
		assertFalse("Consistency fail", X1.equals(Y));
	}

	/*
	 * http://docs.oracle.com/javase/7/docs/api/java/lang/Object.html#hashCode()
	 *
	 * Whenever it is invoked on the same object more than once during an execution of a Java application, the hashCode method must consistently return the same integer, provided no information used in equals comparisons on the object is modified. This integer need not remain consistent from one execution of an application to another execution of the same application.
	 * If two objects are equal according to the equals(Object) method, then calling the hashCode method on each of the two objects must produce the same integer result.
	 * It is not required that if two objects are unequal according to the equals(java.lang.Object) method, then calling the hashCode method on each of the two objects must produce distinct integer results. However, the programmer should be aware that producing distinct integer results for unequal objects may improve the performance of hash tables.
	 */

	@Test
	public void testHashCodeIsConsistent()
	{
		final int hashCode = X1.hashCode();

		assertEquals("HashCode is not consistent", hashCode, X1.hashCode());
		assertEquals("HashCode is not consistent", hashCode, X1.hashCode());
		assertEquals("HashCode is not consistent", hashCode, X1.hashCode());
	}

	@Test
	public void testHashCodeProducesSameNumber()
	{
		final int hashCode = X1.hashCode();

		assertEquals("HashCode does not produce the same number", hashCode, X2.hashCode());
		assertEquals("HashCode does not produce the same number", hashCode, X3.hashCode());
	}
}