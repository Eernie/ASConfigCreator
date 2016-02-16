package nl.eernie.as.variables;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VariableFinder
{
	private static final String fieldStart = "\\$\\{";
	private static final String fieldEnd = "\\}";
	private static final String regex = fieldStart + "([^}]+)" + fieldEnd;
	private static final Pattern pattern = Pattern.compile(regex);

	public static Set<String> findVariables(String fileContent)
	{
		Matcher m = pattern.matcher(fileContent);
		Set<String> foundVariables = new HashSet<>();
		while (m.find())
		{

			foundVariables.add(m.group(1));
		}
		return foundVariables;
	}
}
