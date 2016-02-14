package nl.eernie.as.changelog;

import java.util.LinkedList;

public class ChangeLog
{
	private final LinkedList<Version> versions = new LinkedList<>();

	public LinkedList<Version> getVersions()
	{
		return versions;
	}
}
