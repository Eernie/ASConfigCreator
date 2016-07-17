package nl.eernie.as.variables;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VariableFinder
{
	private static final Set<String> reservedKeywords = new HashSet<>(Arrays.asList(new String[] { "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const", "continue", "default", "do", "double", "else", "enum", "extends", "false", "final", "finally", "float", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long", "native", "new", "null", "package", "private", "protected", "public", "return", "short", "static", "strictfp", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "try", "true", "void", "volatile", "while" }));
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
			String variable = m.group(1);

			foundVariables.add(variable);
		}
		return foundVariables;
	}

	private static boolean isJavaIdentifier(String s)
	{
		if (s.length() == 0)
		{
			return false;
		}
		if (reservedKeywords.contains(s))
		{
			return false;
		}

		if (!Character.isJavaIdentifierStart(s.charAt(0)))
		{
			return false;
		}

		for (int i = 1; i < s.length(); i++)
		{
			if (!Character.isJavaIdentifierPart(s.charAt(i)))
			{
				return false;
			}
		}
		return true;
	}

	public static String replaceWithoutBrackets(Set<String> variables, String fileContent)
	{
		String result = fileContent;
		for (String variable : variables)
		{
			if (!isJavaIdentifier(variable))
			{
				throw new IllegalArgumentException("Variable " + variable + " is not a correct variable");
			}
			String oldVar = "${" + variable + '}';
			result = result.replace(oldVar, "$" + variable);
		}
		return result;
	}
}
