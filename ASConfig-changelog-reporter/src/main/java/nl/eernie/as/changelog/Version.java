package nl.eernie.as.changelog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.eernie.as.aschangelog.BaseEntry;

public class Version
{
	private String name;
	private final Map<Class<? extends BaseEntry>, List<BaseEntry>> changes = new HashMap<>();

	public Map<Class<? extends BaseEntry>, List<BaseEntry>> getChanges()
	{
		return changes;
	}

	public <X extends BaseEntry> List<X> getChanges(Class<X> key){
		return (List<X>) changes.get(key);
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}
