package nl.eernie.as.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

public class SortedProperties extends Properties
{
	private static final long serialVersionUID = 1L;
	private static final KeysComparator KEYS_COMPARATOR = new KeysComparator();

	@Override
	public synchronized Enumeration<Object> keys()
	{
		Enumeration<Object> keysEnum = super.keys();
		Vector<Object> keyList = new Vector<Object>(size());
		while (keysEnum.hasMoreElements())
		{
			keyList.add(keysEnum.nextElement());
		}
		Collections.sort(keyList, KEYS_COMPARATOR);
		return keyList.elements();
	}

	private static class KeysComparator implements Comparator<Object>
	{
		@Override
		public int compare(Object o1, Object o2)
		{
			if (!(o1 instanceof String))
			{
				throw new IllegalArgumentException("First object must be of type String");
			}
			if (!(o2 instanceof String))
			{
				throw new IllegalArgumentException("Second object must be of type String");
			}

			return ((String) o1).compareTo((String) o2);
		}
	}
}
