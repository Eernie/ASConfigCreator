package nl.eernie.as.variables;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class VariableFinderTest
{
	@Test
	public void testReplace()
	{
		Set<String> variables = VariableFinder.findVariables("text ${text}");

		assertEquals(Collections.singleton("text"), variables);

		variables = VariableFinder.findVariables("a ${default} should use the default ${prop} if the property is not found");
		assertEquals(new HashSet<>(Arrays.asList("default", "prop")), variables);

	}
}
