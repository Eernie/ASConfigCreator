package nl.eernie.as;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import nl.eernie.as.application_server.ApplicationServer;
import nl.eernie.as.configuration.Configuration;

import org.junit.Test;

public class ASConfigCreatorTest
{
	@Test
	public void testTags() throws IOException
	{
		Path outputDirectory = Files.createTempDirectory("asConfig");

		Configuration configuration = new Configuration();
		configuration.getApplicationServers().add(ApplicationServer.WILDFLY);
		configuration.getContexts().add("core");
		configuration.setOutputDirectoryPath(outputDirectory.toFile());
		ASConfigCreator creator = new ASConfigCreator(configuration);

		creator.createConfigFiles(getClass().getResource("/tags.xml").getPath());
		System.out.println(outputDirectory);
	}
}
