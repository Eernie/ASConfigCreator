package nl.eernie.as.variables.logicexpression;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import org.junit.Test;

public class BooleanLogicTest
{
	@SuppressWarnings("null")
	@Nonnull
	private List<String> TEST_LITERALS = Arrays.asList("A", "B", "C");

	@Test
	public void testMatches()
	{
		assertEquals(true, BooleanLogic.matches("A AND B AND C", TEST_LITERALS));
		assertEquals(false, BooleanLogic.matches("X OR Y", TEST_LITERALS));
	}
}