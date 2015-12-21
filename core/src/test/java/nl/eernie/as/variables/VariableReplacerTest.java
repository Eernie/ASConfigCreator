package nl.eernie.as.variables;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class VariableReplacerTest
{
	@Test
	public void testReplace()
	{
		try
		{
			VariableReplacer.replace("text ${text}", new HashMap<String, String>());
			fail("PropertyNotFoundException should be thrown!");
		}
		catch (PropertyNotFoundException pnfe)
		{
			assertEquals("No property found for key text", pnfe.getMessage());
		}

		Map<String, String> properties = new HashMap<>();
		properties.put("property", "value");

		String replace = VariableReplacer.replace("this property ${property} should be replaced with value", properties);
		assertEquals("this property value should be replaced with value", replace);

		properties.put("prop", "val");
		replace = VariableReplacer.replace("a ${default:var} should use the default ${prop:value} if the property is not found", properties);
		assertEquals("a var should use the default val if the property is not found", replace);


	}
}