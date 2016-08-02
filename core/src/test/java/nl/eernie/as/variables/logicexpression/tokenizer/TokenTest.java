package nl.eernie.as.variables.logicexpression.tokenizer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TokenTest
{
	private static final Token TEST_LITERAL_TOKEN = new Token("Token");
	private static final Token OTHER_LITERAL_TOKEN = new Token("Another token");

	private static final Token TOKEN_1 = new Token("ABC");
	private static final Token TOKEN_2 = new Token("ABC");
	private static final Token TOKEN_3 = new Token("ABC");

	@SuppressWarnings("null")
	private static final Token NULL_TOKEN = new Token(null);

	@Test
	public void testMethods()
	{
		assertEquals("Token", TEST_LITERAL_TOKEN.getToken());
		assertEquals("Token", TEST_LITERAL_TOKEN.toString());
	}

	@Test
	public void testConstantTokens()
	{
		assertEquals(new Token("AND"), Token.AND);
		assertEquals(new Token("OR"), Token.OR);
		assertEquals(new Token("!"), Token.NOT);
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
		assertTrue("Reflexive fail", TOKEN_1.equals(TOKEN_1));
	}

	@Test
	public void testEqualsIsSymmetric()
	{
		assertTrue("Symmetric fail", TOKEN_1.equals(TOKEN_2));
		assertTrue("Symmetric fail", TOKEN_2.equals(TOKEN_1));
	}

	@Test
	public void testEqualsIsTransitive()
	{
		assertTrue("Transitive fail", TOKEN_1.equals(TOKEN_2));
		assertTrue("Transitive fail", TOKEN_2.equals(TOKEN_3));
		assertTrue("Transitive fail", TOKEN_1.equals(TOKEN_3));
	}

	@Test
	public void testNotEqualsIncompatibleType()
	{
		assertFalse("Incompatibe type", TOKEN_1.equals(TEST_LITERAL_TOKEN));
	}

	@Test
	public void testEqualsIncompatibleType()
	{
		assertFalse("Incompatibe type", TOKEN_1.equals(Boolean.TRUE));
	}

	@Test
	public void testEqualsToNullIs()
	{
		assertFalse("Equals to NULL should be false", TOKEN_1.equals(null));
	}

	@Test
	public void testEqualsIsConsistent()
	{
		assertTrue("Consistency fail", TOKEN_1.equals(TOKEN_2));
		assertTrue("Consistency fail", TOKEN_1.equals(TOKEN_2));
		assertTrue("Consistency fail", TOKEN_1.equals(TOKEN_2));

		assertFalse("Consistency fail", TOKEN_1.equals(TEST_LITERAL_TOKEN));
		assertFalse("Consistency fail", TOKEN_1.equals(TEST_LITERAL_TOKEN));
		assertFalse("Consistency fail", TOKEN_1.equals(TEST_LITERAL_TOKEN));
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
		final int hashCode = TOKEN_1.hashCode();

		assertEquals("HashCode is not consistent", hashCode, TOKEN_1.hashCode());
		assertEquals("HashCode is not consistent", hashCode, TOKEN_1.hashCode());
		assertEquals("HashCode is not consistent", hashCode, TOKEN_1.hashCode());
	}

	@Test
	public void testHashCodeProducesSameNumber()
	{
		final int hashCode = TOKEN_1.hashCode();

		assertEquals("HashCode does not produce the same number", hashCode, TOKEN_2.hashCode());
		assertEquals("HashCode does not produce the same number", hashCode, TOKEN_3.hashCode());
	}

	@SuppressWarnings("null")
	@Test
	public void testEqualsAndHashCodeForNullToken()
	{
		assertTrue(NULL_TOKEN.equals(new Token(null)));
		assertEquals(NULL_TOKEN.hashCode(), new Token(null).hashCode());
		assertFalse(NULL_TOKEN.equals(new Token("Blah")));

		assertNotEquals(NULL_TOKEN.hashCode(), TEST_LITERAL_TOKEN.hashCode());
		assertFalse(NULL_TOKEN.equals(TEST_LITERAL_TOKEN));
	}

	@Test
	public void testEqualsAndHashCodeForOtherLiteralToken()
	{
		assertNotEquals(TEST_LITERAL_TOKEN.hashCode(), OTHER_LITERAL_TOKEN.hashCode());
		assertFalse(TEST_LITERAL_TOKEN.equals(OTHER_LITERAL_TOKEN));
	}
}