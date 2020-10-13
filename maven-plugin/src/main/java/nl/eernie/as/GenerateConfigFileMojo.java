package nl.eernie.as;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

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
	@Parameter(required = true, defaultValue = "${project.build.directory}/asconfig")
	private File outputDirectory;

	@Parameter(required = true)
	private File settingsFile;

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
		final ConfigurationLoader configurationLoader = new ConfigurationLoader(settingsFile, outputDirectory, contexts, applicationServers, outputFilename, getLog());
		final Configuration configuration = configurationLoader.loadConfiguration();

		getLog().info("Using configuration: " + configuration);

		final ASConfigCreator asConfigCreator = new ASConfigCreator(configuration);
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
}
