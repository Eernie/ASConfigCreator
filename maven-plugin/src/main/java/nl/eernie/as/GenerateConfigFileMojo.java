package nl.eernie.as;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import nl.eernie.as.application_server.ApplicationServer;
import nl.eernie.as.configuration.Configuration;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "generateConfig", defaultPhase = LifecyclePhase.GENERATE_RESOURCES)
public class GenerateConfigFileMojo extends AbstractMojo
{
	private static final String APPLICATION_SERVERS = "applicationServers";
	private static final String CONTEXTS = "contexts";

	@Parameter(required = true, defaultValue = "${project.build.directory}/asconfig")
	private File outputDirectory;

	@Parameter(required = true)
	private File settingsFile;

	@Parameter(required = true, defaultValue = "${project.basedir}/src/main/asconfig/master.xml")
	private File masterFile;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException
	{
		Properties properties = getProperties();
		validate(properties);
		String[] contexts = properties.getProperty(CONTEXTS).split(",");
		List<ApplicationServer> applicationServers = getApplicationServers(properties);

		Configuration configuration = new Configuration();
		configuration.getContexts().addAll(Arrays.asList(contexts));
		configuration.getApplicationServers().addAll(applicationServers);
		configuration.setOutputDirectoryPath(outputDirectory);

		getLog().info("Using configuration: " + configuration);

		ASConfigCreator asConfigCreator = new ASConfigCreator(configuration);
		try
		{
			asConfigCreator.createConfigFiles(Paths.get(masterFile.getAbsolutePath()).toUri().toURL().getPath());
		}
		catch (IOException e)
		{
			String message = "Something went wrong";
			getLog().error(message, e);
			throw new MojoExecutionException(message, e);
		}
	}

	private void validate(Properties properties) throws MojoFailureException
	{
		if (!properties.containsKey(CONTEXTS))
		{
			String message = "No contexts provided in settings file";
			getLog().error(message);
			throw new MojoFailureException(message);
		}
		if (!properties.containsKey(APPLICATION_SERVERS))
		{
			String message = "No application servers provided in settings file";
			getLog().error(message);
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
			getLog().error(message);
			throw new MojoFailureException(message);
		}

		getLog().info("Using settings file " + settingsFile.getPath());
		Properties properties = new Properties();

		try (FileInputStream inStream = new FileInputStream(settingsFile))
		{
			properties.load(inStream);
			return properties;
		}
		catch (IOException e)
		{
			String message = "Something went wrong while reading file " + settingsFile.getPath();
			getLog().error(message);
			throw new MojoFailureException(message, e);
		}
	}
}
