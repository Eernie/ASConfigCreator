package nl.eernie.as.configuration;

import java.io.File;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import nl.eernie.as.application_server.ApplicationServer;

public class Configuration
{
	private final List<ApplicationServer> applicationServers = new LinkedList<>();
	private final List<String> contexts = new LinkedList<>();
	private String fromTag;
	private String toTag;
	private File outputDirectoryPath;

	public List<ApplicationServer> getApplicationServers()
	{
		return applicationServers;
	}

	public List<String> getContexts()
	{
		return contexts;
	}

	public File getOutputDirectoryPath()
	{
		return outputDirectoryPath;
	}

	public void setOutputDirectoryPath(File outputDirectoryPath)
	{
		this.outputDirectoryPath = outputDirectoryPath;
	}

	public String getToTag()
	{
		return toTag;
	}

	public void setToTag(String toTag)
	{
		this.toTag = toTag;
	}

	public String getFromTag()
	{
		return fromTag;
	}

	public void setFromTag(String fromTag)
	{
		this.fromTag = fromTag;
	}

	@Override
	public String toString()
	{
		return "Configuration{" +
			"applicationServers=" + applicationServers +
			", contexts=" + contexts +
			", fromTag='" + fromTag + '\'' +
			", toTag='" + toTag + '\'' +
			", outputDirectoryPath=" + outputDirectoryPath +
			'}';
	}
}
