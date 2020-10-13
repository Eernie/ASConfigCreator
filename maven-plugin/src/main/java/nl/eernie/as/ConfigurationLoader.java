package nl.eernie.as;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import nl.eernie.as.application_server.ApplicationServer;
import nl.eernie.as.configuration.Configuration;

import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;

public class ConfigurationLoader
{
	private static final String APPLICATION_SERVERS = "applicationServers";
	private static final String CONTEXTS = "contexts";
	private static final String OUTPUT_FILENAME = "outputFilename";
	private final File settingsFile;
	private final File outputDirectory;
	private final String contexts;
	private final String applicationServers;
	private final String outputFilename;
	private final Log log;

	public ConfigurationLoader(File settingsFile, File outputDirectory, String contexts, String applicationServers, String outputFilename, Log log)
	{
		this.settingsFile = settingsFile;
		this.outputDirectory = outputDirectory;
		this.contexts = contexts;
		this.applicationServers = applicationServers;
		this.outputFilename = outputFilename;
		this.log = log;
	}

	public Configuration loadConfiguration() throws MojoFailureException
	{
		Properties properties = getProperties();
		validate(properties);
		String[] contexts = properties.getProperty(CONTEXTS).split(",");
		List<ApplicationServer> applicationServers = getApplicationServers(properties);
		String outputFilename = properties.getProperty(OUTPUT_FILENAME);

		final Configuration configuration = new Configuration();
		configuration.getContexts().addAll(Arrays.asList(contexts));
		configuration.getApplicationServers().addAll(applicationServers);
		configuration.setOutputDirectoryPath(outputDirectory);
		configuration.setOutputFilename(outputFilename);

		return configuration;
	}

	private void validate(Properties properties) throws MojoFailureException
	{
		if (!properties.containsKey(CONTEXTS))
		{
			String message = "No contexts provided in settings file";
			log.error(message);
			throw new MojoFailureException(message);
		}
		if (!properties.containsKey(APPLICATION_SERVERS))
		{
			String message = "No application servers provided in settings file";
			log.error(message);
			throw new MojoFailureException(message);
		}
	}

	private List<ApplicationServer> getApplicationServers(Properties properties)
	{
		String[] applicationServerNames = properties.getProperty(APPLICATION_SERVERS).split(",");
		List<ApplicationServer> applicationServers = new ArrayList<>(applicationServerNames.length);
		for (String serverName : applicationServerNames)
		{
			applicationServers.add(ApplicationServer.fromValue(serverName));
		}
		return applicationServers;
	}

	private Properties getProperties() throws MojoFailureException
	{
		if (!settingsFile.canRead())
		{
			String message = "Unable to read file at " + settingsFile.getPath();
			log.error(message);
			throw new MojoFailureException(message);
		}

		log.info("Using settings file " + settingsFile.getPath());
		Properties properties = new Properties();

		try (FileInputStream inStream = new FileInputStream(settingsFile))
		{
			properties.load(inStream);
			if (contexts != null)
			{
				properties.setProperty(CONTEXTS, contexts);
			}
			if (applicationServers != null)
			{
				properties.setProperty(APPLICATION_SERVERS, applicationServers);
			}
			if (outputFilename != null)
			{
				properties.setProperty(OUTPUT_FILENAME, outputFilename);
			}
			return properties;
		}
		catch (IOException e)
		{
			String message = "Something went wrong while reading file " + settingsFile.getPath();
			log.error(message);
			throw new MojoFailureException(message, e);
		}
	}
}
