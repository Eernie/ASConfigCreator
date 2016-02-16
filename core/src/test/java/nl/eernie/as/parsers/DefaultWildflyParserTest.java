package nl.eernie.as.parsers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import nl.eernie.as.application_server.ApplicationServer;
import nl.eernie.as.aschangelog.AddDatasource;
import nl.eernie.as.aschangelog.DeleteDatasource;
import nl.eernie.as.aschangelog.UpdateDatasource;
import nl.eernie.as.configuration.Configuration;

import org.junit.Test;

public class DefaultWildflyParserTest
{

	@Test
	public void testCanHandleApplicationServer() throws Exception
	{
		DefaultWildflyParser parser = new DefaultWildflyParser();
		assertFalse(parser.canHandleApplicationServer(ApplicationServer.JBOSS));
		assertTrue(parser.canHandleApplicationServer(ApplicationServer.WILDFLY));
		assertFalse(parser.canHandleApplicationServer(ApplicationServer.WEBSPHERE));
	}

	@Test
	public void testAddDatasource() throws IOException
	{
		DefaultWildflyParser parser = new DefaultWildflyParser();
		parser.initParser(new Configuration());
		AddDatasource baseEntry = new AddDatasource();
		baseEntry.setName("DS");
		baseEntry.setJndi("java:/ds");
		baseEntry.setJTA(true);
		baseEntry.setConnectionUrl("url");
		baseEntry.setDriverName("driver");
		baseEntry.setPassword("pass");
		baseEntry.setUsername("user");
		parser.handle(baseEntry);
		List<String> expected = Collections.singletonList("data-source add --name=DS --driver-name=driver --jndi-name=java:/ds --connection-url=url --user-name=user --password=pass --jta=true --enabled=true");
		verifyOutput(parser, expected, Collections.<String> emptySet());
	}

	@Test
	public void testFoundVariables() throws IOException
	{
		DefaultWildflyParser parser = new DefaultWildflyParser();
		Configuration configuration = new Configuration();
		configuration.getFoundVariables().add("driverModule");
		parser.initParser(configuration);
		List<String> expected = Collections.emptyList();
		verifyOutput(parser, expected, Collections.singleton("driverModule"));
	}

	@Test
	public void testUpdateDatasource() throws IOException
	{
		DefaultWildflyParser parser = new DefaultWildflyParser();
		parser.initParser(new Configuration());
		UpdateDatasource baseEntry = new UpdateDatasource();
		baseEntry.setName("DS");
		baseEntry.setJndi("java:/ds");
		baseEntry.setJTA(true);
		baseEntry.setConnectionUrl("url");
		baseEntry.setDriverName("driver");
		baseEntry.setPassword("pass");
		baseEntry.setUsername("user");
		parser.handle(baseEntry);
		List<String> expected = Arrays.asList("data-source remove --name=DS", "data-source add --name=DS --driver-name=driver --jndi-name=java:/ds --connection-url=url --user-name=user --password=pass --jta=true --enabled=true");
		verifyOutput(parser, expected, Collections.<String> emptySet());
	}

	@Test
	public void testDeleteDatasource() throws IOException
	{
		DefaultWildflyParser parser = new DefaultWildflyParser();
		parser.initParser(new Configuration());
		DeleteDatasource baseEntry = new DeleteDatasource();
		baseEntry.setName("DS");
		parser.handle(baseEntry);
		List<String> expected = Collections.singletonList("data-source remove --name=DS");
		verifyOutput(parser, expected, Collections.<String> emptySet());
	}

	private void verifyOutput(DefaultWildflyParser parser, List<String> expected, Set<String> expectedProperties) throws IOException
	{
		Path tempDirectory = Files.createTempDirectory("junit");
		parser.writeFileToDirectory(tempDirectory.toFile());
		Path outputFile = Paths.get(tempDirectory.toString() + "/wildfly.cli");

		List<String> outputContent = Files.readAllLines(outputFile, Charset.defaultCharset());
		assertEquals(expected, filterActual(outputContent));

		Path propertyFile = Paths.get(tempDirectory.toString() + "/wildfly.properties");
		Properties actualProperties = new Properties();
		try (FileReader reader = new FileReader(propertyFile.toFile()))
		{
			actualProperties.load(reader);
		}
		assertEquals(expectedProperties, actualProperties.keySet());
		
		Files.deleteIfExists(outputFile);
		Files.deleteIfExists(propertyFile);
		Files.deleteIfExists(tempDirectory);
	}

	private List<String> filterActual(List<String> actual)
	{
		List<String> filtered = new ArrayList<>(actual.size());
		for (String s : actual)
		{
			if (!s.startsWith("##"))
			{
				filtered.add(s);
			}
		}
		return filtered;
	}
}
