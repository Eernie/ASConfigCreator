package nl.eernie.as.parsers;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import nl.eernie.as.application_server.ApplicationServer;
import nl.eernie.as.aschangelog.AddDatasource;
import nl.eernie.as.aschangelog.DeleteDatasource;
import nl.eernie.as.aschangelog.UpdateDatasource;

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
		verifyOutput(parser, expected);
	}

	@Test
	public void testUpdateDatasource() throws IOException
	{
		DefaultWildflyParser parser = new DefaultWildflyParser();
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
		verifyOutput(parser, expected);
	}

	@Test
	public void testDeleteDatasource() throws IOException
	{
		DefaultWildflyParser parser = new DefaultWildflyParser();
		DeleteDatasource baseEntry = new DeleteDatasource();
		baseEntry.setName("DS");
		parser.handle(baseEntry);
		List<String> expected = Collections.singletonList("data-source remove --name=DS");
		verifyOutput(parser, expected);
	}

	private void verifyOutput(DefaultWildflyParser parser, List<String> expected) throws IOException
	{
		Path tempDirectory = Files.createTempDirectory("junit");
		parser.writeFileToDirectory(tempDirectory.toFile());
		Path outputFile = Paths.get(tempDirectory.toString() + "/jboss.cli");
		List<String> fileContent = Files.readAllLines(outputFile, Charset.defaultCharset());

		assertEquals(expected, fileContent);

		Files.deleteIfExists(outputFile);
		Files.deleteIfExists(tempDirectory);
	}
}
