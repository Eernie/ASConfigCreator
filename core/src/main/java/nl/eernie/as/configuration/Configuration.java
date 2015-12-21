package nl.eernie.as.configuration;

import nl.eernie.as.application_server.ApplicationServer;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class Configuration
{
	private final List<ApplicationServer> applicationServers = new LinkedList<>();
	private final List<String> contexts = new LinkedList<>();
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

	@Override
	public String toString()
	{
		return "Configuration{" + "applicationServers=" + applicationServers + ", contexts=" + contexts + ", outputDirectoryPath=" + outputDirectoryPath + '}';
	}
}
