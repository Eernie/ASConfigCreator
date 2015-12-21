package nl.eernie.as.variables;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VariableReplacer
{
	private static final String fieldStart = "\\$\\{";
	private static final String fieldEnd = "\\}";

	private static final String regex = fieldStart + "([^}]+)" + fieldEnd;
	private static final Pattern pattern = Pattern.compile(regex);

	public static String replace(String format, Map<String, String> properties)
	{
		Matcher m = pattern.matcher(format);
		String result = format;
		while (m.find())
		{
			String newValue;
			if (m.group(1).contains(":"))
			{
				String[] keyAndDefault = m.group(1).split(":");
				newValue = properties.get(keyAndDefault[0]);
				if (newValue == null)
				{
					newValue = keyAndDefault[1];
				}
			}
			else
			{
				newValue = properties.get(m.group(1));
				if (newValue == null)
				{
					throw new PropertyNotFoundException("No property found for key " + m.group(1));
				}
			}

			result = result.replaceFirst(regex, newValue);
		}
		return result;
	}
}
