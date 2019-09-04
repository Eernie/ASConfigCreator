package nl.eernie.as.configuration;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import nl.eernie.as.application_server.ApplicationServer;

public class Configuration
{
	private final List<ApplicationServer> applicationServers = new LinkedList<>();
	private final List<String> contexts = new LinkedList<>();
	private String fromTag;
	private String toTag;
	private File outputDirectoryPath;
	private String outputFilename;

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

	public String getOutputFilename()
	{
		return outputFilename;
	}

	public void setOutputFilename(String outputFilename)
	{
		this.outputFilename = outputFilename;
	}

	@Override
	public String toString()
	{
		return "Configuration{" + "applicationServers=" + applicationServers + ", contexts=" + contexts + ", fromTag='" + fromTag + '\'' + ", toTag='" + toTag + '\'' + ", outputDirectoryPath=" + outputDirectoryPath + ", outputFilename='" + outputFilename + '\'' + '}';
	}
}
