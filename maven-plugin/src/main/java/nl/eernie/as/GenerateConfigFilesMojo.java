package nl.eernie.as;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import nl.eernie.as.configuration.Configuration;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "generateConfigs", defaultPhase = LifecyclePhase.GENERATE_RESOURCES)
public class GenerateConfigFilesMojo extends AbstractMojo
{
	@Parameter(required = true)
	private File inputDirectory;

	@Parameter(required = true, defaultValue = "${project.build.directory}/asconfig")
	private File outputDirectory;

	@Parameter(property = "asconfig.contexts")
	private String contexts;

	@Parameter(property = "asconfig.applicationsServers")
	private String applicationServers;

	@Parameter(required = true, defaultValue = "${project.basedir}/src/main/asconfig/master.xml")
	private File masterFile;

	@Parameter(property = "asconfig.outputFilename")
	private String outputFilename;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException
	{
		if (!inputDirectory.canRead())
		{
			String message = "Cannot read input directory";
			getLog().error(message);
			throw new MojoFailureException(message);
		}
		final List<Path> settingsFiles = getSettingsFiles();
		for (Path settingsFile : settingsFiles)
		{
			try
			{
				final ConfigurationLoader configurationLoader = new ConfigurationLoader(settingsFile.toFile(), outputDirectory, contexts, applicationServers, outputFilename, getLog());
				final Configuration configuration = configurationLoader.loadConfiguration();

				getLog().info("Using configuration: " + configuration);

				final ASConfigCreator asConfigCreator = new ASConfigCreator(configuration);
				asConfigCreator.createConfigFiles(Paths.get(masterFile.getAbsolutePath()).toUri().toURL().getPath());
			}
			catch (Exception e)
			{
				String message = "Could not execute generateConfig for file " + settingsFile;
				getLog().error(message, e);
				throw new MojoExecutionException(message, e);
			}
		}
	}

	private List<Path> getSettingsFiles() throws MojoFailureException
	{
		final List<Path> settingsFiles;
		try
		{
			settingsFiles = Files.list(inputDirectory.toPath()).filter(path -> path.toString().endsWith(".properties")).collect(Collectors.toList());
		}
		catch (IOException e)
		{
			String message = "Could not read settings file(s)";
			getLog().error(message, e);
			throw new MojoFailureException(message, e);
		}
		if (settingsFiles.isEmpty())
		{
			String message = "No valid settings files where found!";
			getLog().error(message);
			throw new MojoFailureException(message);
		}
		return settingsFiles;
	}
}
