package nl.eernie.as;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.eernie.as.application_server.ApplicationServer;
import nl.eernie.as.configuration.Configuration;
import nl.eernie.as.parsers.ConfigurationParser;
import nl.eernie.as.parsers.DefaultJbossParser;

import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;

public class ASConfigCreatorTest
{
	@Test
	public void testInitialization()
	{
		Configuration configuration = new Configuration();
		configuration.getApplicationServers().add(ApplicationServer.WILDFLY);
		ASConfigCreator creator = new ASConfigCreator(configuration);

		@SuppressWarnings("unchecked")
		Map<ApplicationServer, Set<ConfigurationParser>> configurationParsers = (Map<ApplicationServer, Set<ConfigurationParser>>) Whitebox.getInternalState(creator, "configurationParsers");

		assertTrue(configurationParsers.containsKey(ApplicationServer.WILDFLY));
		assertTrue(configurationParsers.containsKey(null));
		assertTrue(configurationParsers.get(ApplicationServer.WILDFLY).iterator().next() instanceof DefaultJbossParser);
	}

	@Test
	public void testInclude() throws IOException
	{
		Path outputDirectory = Files.createTempDirectory("asConfig");

		Configuration configuration = new Configuration();
		configuration.getApplicationServers().add(ApplicationServer.WILDFLY);
		configuration.getContexts().add("core");
		configuration.setOutputDirectoryPath(outputDirectory.toFile());
		ASConfigCreator creator = new ASConfigCreator(configuration);

		creator.createConfigFiles(getClass().getResource("/includeFiles/master.xml").getPath());

		String outputFile = outputDirectory.toString() + "/jboss.cli";

		List<String> expected = Arrays.asList("batch", "/system-property=property:add(value=property)", "run-batch", "", "batch", "/system-property=property:add(value=property)", "run-batch", "");
		List<String> actual = Files.readAllLines(Paths.get(outputFile), Charset.defaultCharset());
		assertEquals(expected, actual);
	}

	@Test
	public void testTags() throws IOException
	{
		Path outputDirectory = Files.createTempDirectory("asConfig");

		Configuration configuration = new Configuration();
		configuration.getApplicationServers().add(ApplicationServer.WILDFLY);
		configuration.getContexts().add("core");
		configuration.setFromTag("1.0.0");
		configuration.setToTag("1.0.1");
		configuration.setOutputDirectoryPath(outputDirectory.toFile());
		ASConfigCreator creator = new ASConfigCreator(configuration);

		creator.createConfigFiles(getClass().getResource("/tags.xml").getPath());

		String outputFile = outputDirectory.toString() + "/jboss.cli";

		List<String> expected = Arrays.asList("batch", "/system-property=property that will be processed:add(value=value)", "run-batch", "");
		List<String> actual = Files.readAllLines(Paths.get(outputFile), Charset.defaultCharset());
		assertEquals(expected, actual);
	}

	@Test
	public void testApplicationServer() throws IOException
	{
		Path outputDirectory = Files.createTempDirectory("asConfig");

		Configuration configuration = new Configuration();
		configuration.getApplicationServers().add(ApplicationServer.WILDFLY);
		configuration.getContexts().add("core");
		configuration.setOutputDirectoryPath(outputDirectory.toFile());
		ASConfigCreator creator = new ASConfigCreator(configuration);

		creator.createConfigFiles(getClass().getResource("/applicationServer.xml").getPath());

		String outputFile = outputDirectory.toString() + "/jboss.cli";

		List<String> expected = Arrays.asList("batch", "/system-property=property that will be processed:add(value=value)", "run-batch", "", "batch", "/system-property=property that will be processed:add(value=value)", "run-batch", "");
		List<String> actual = Files.readAllLines(Paths.get(outputFile), Charset.defaultCharset());
		assertEquals(expected, actual);
	}
}
