package nl.eernie.as.parsers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import nl.eernie.as.application_server.ApplicationServer;
import nl.eernie.as.aschangelog.AddConnectionFactory;
import nl.eernie.as.aschangelog.AddDLQ;
import nl.eernie.as.aschangelog.AddDatasource;
import nl.eernie.as.aschangelog.AddDriver;
import nl.eernie.as.aschangelog.AddMailSession;
import nl.eernie.as.aschangelog.AddProperty;
import nl.eernie.as.aschangelog.AddQueue;
import nl.eernie.as.aschangelog.AddSecurityDomain;
import nl.eernie.as.aschangelog.ChangeLogLevel;
import nl.eernie.as.aschangelog.CustomChange;
import nl.eernie.as.aschangelog.DeleteConnectionFactory;
import nl.eernie.as.aschangelog.DeleteDLQ;
import nl.eernie.as.aschangelog.DeleteDatasource;
import nl.eernie.as.aschangelog.DeleteDriver;
import nl.eernie.as.aschangelog.DeleteMailSession;
import nl.eernie.as.aschangelog.DeleteProperty;
import nl.eernie.as.aschangelog.DeleteQueue;
import nl.eernie.as.aschangelog.DeleteSecurityDomain;
import nl.eernie.as.aschangelog.LogLevels;
import nl.eernie.as.aschangelog.UpdateDatasource;
import nl.eernie.as.aschangelog.UpdateDriver;
import nl.eernie.as.aschangelog.UpdateMailSession;
import nl.eernie.as.aschangelog.UpdateProperty;
import nl.eernie.as.aschangelog.UpdateQueue;
import nl.eernie.as.aschangelog.UpdateSecurityDomain;
import nl.eernie.as.configuration.Configuration;

import org.junit.Test;

public class DefaultJbossParserTest
{
	@Test
	public void testCanHandleApplicationServer()
	{
		DefaultJbossParser parser = new DefaultJbossParser();
		assertTrue(parser.canHandleApplicationServer(ApplicationServer.JBOSS));
		assertFalse(parser.canHandleApplicationServer(ApplicationServer.WILDFLY));
		assertFalse(parser.canHandleApplicationServer(ApplicationServer.WEBSPHERE));
	}

	@Test
	public void testBeginTransaction() throws IOException
	{
		DefaultJbossParser parser = new DefaultJbossParser();
		parser.beginTransaction();
		List<String> expected = Collections.singletonList("batch");
		verifyOutput(parser, expected);
	}

	@Test
	public void testCommitTransaction() throws IOException
	{
		DefaultJbossParser parser = new DefaultJbossParser();
		parser.commitTransaction();
		List<String> expected = Arrays.asList("run-batch", "");
		verifyOutput(parser, expected);
	}

	@Test
	public void testAddProperty() throws IOException
	{
		DefaultJbossParser parser = new DefaultJbossParser();
		AddProperty baseEntry = new AddProperty();
		baseEntry.setName("prop");
		baseEntry.setValue("val");
		parser.handle(baseEntry);
		List<String> expected = Collections.singletonList("/system-property=prop:add(value=val)");
		verifyOutput(parser, expected);
	}

	@Test
	public void testUpdateProperty() throws IOException
	{
		DefaultJbossParser parser = new DefaultJbossParser();
		UpdateProperty baseEntry = new UpdateProperty();
		baseEntry.setName("prop");
		baseEntry.setValue("val");
		parser.handle(baseEntry);
		List<String> expected = Arrays.asList("/system-property=prop:remove", "/system-property=prop:add(value=val)");
		verifyOutput(parser, expected);
	}

	@Test
	public void testDeleteProperty() throws IOException
	{
		DefaultJbossParser parser = new DefaultJbossParser();
		DeleteProperty baseEntry = new DeleteProperty();
		baseEntry.setName("prop");
		parser.handle(baseEntry);
		List<String> expected = Collections.singletonList("/system-property=prop:remove");
		verifyOutput(parser, expected);
	}

	@Test
	public void testAddQueue() throws IOException
	{
		DefaultJbossParser parser = new DefaultJbossParser();
		AddQueue baseEntry = new AddQueue();
		baseEntry.setName("prop");
		baseEntry.setJndi("java:/jndi");
		baseEntry.setPersistent(true);
		parser.handle(baseEntry);
		List<String> expected = Collections.singletonList("jms-queue add --queue-address=prop --entries=java:/jndi --durable=true");
		verifyOutput(parser, expected);
	}

	@Test
	public void testUpdateQueue() throws IOException
	{
		DefaultJbossParser parser = new DefaultJbossParser();
		UpdateQueue baseEntry = new UpdateQueue();
		baseEntry.setName("prop");
		baseEntry.setJndi("java:/jndi");
		baseEntry.setPersistent(true);
		parser.handle(baseEntry);
		List<String> expected = Arrays.asList("jms-queue remove --queue-address=prop", "jms-queue add --queue-address=prop --entries=java:/jndi --durable=true");
		verifyOutput(parser, expected);
	}

	@Test
	public void testDeleteQueue() throws IOException
	{
		DefaultJbossParser parser = new DefaultJbossParser();
		DeleteQueue baseEntry = new DeleteQueue();
		baseEntry.setName("prop");
		parser.handle(baseEntry);
		List<String> expected = Collections.singletonList("jms-queue remove --queue-address=prop");
		verifyOutput(parser, expected);
	}

	@Test
	public void testAddDLQ() throws IOException
	{
		DefaultJbossParser parser = new DefaultJbossParser();
		AddDLQ baseEntry = new AddDLQ();
		baseEntry.setDeliveryQueue("dlq");
		baseEntry.getMonitorAddress().add("address");
		parser.handle(baseEntry);
		List<String> expected = Collections.singletonList("/subsystem=messaging/hornetq-server=default/address-setting=address:add(dead-letter-address=dlq)");
		verifyOutput(parser, expected);
	}

	@Test
	public void testDeleteDLQ() throws IOException
	{
		DefaultJbossParser parser = new DefaultJbossParser();
		DeleteDLQ baseEntry = new DeleteDLQ();
		baseEntry.setDeliveryQueue("dlq");
		baseEntry.setMonitorAddress("queue");
		parser.handle(baseEntry);
		List<String> expected = Collections.singletonList("/subsystem=messaging/hornetq-server=default/address-setting=queue:remove");
		verifyOutput(parser, expected);
	}

	@Test
	public void testAddDriver() throws IOException
	{
		DefaultJbossParser parser = new DefaultJbossParser();
		AddDriver baseEntry = new AddDriver();
		baseEntry.setName("driver");
		baseEntry.setClasspath("/lib/driver.jar");
		baseEntry.setModule("driverModule");
		parser.handle(baseEntry);
		List<String> expected = Collections.singletonList("/subsystem=datasources/jdbc-driver=driver:add(driver-name=driver,driver-module-name=driverModule)");
		verifyOutput(parser, expected);
	}

	@Test
	public void testUpdateDriver() throws IOException
	{
		DefaultJbossParser parser = new DefaultJbossParser();
		UpdateDriver baseEntry = new UpdateDriver();
		baseEntry.setName("driver");
		baseEntry.setClasspath("/lib/driver.jar");
		baseEntry.setModule("driverModule");
		parser.handle(baseEntry);
		List<String> expected = Arrays.asList("/subsystem=datasources/jdbc-driver=driver:remove", "/subsystem=datasources/jdbc-driver=driver:add(driver-name=driver,driver-module-name=driverModule)");
		verifyOutput(parser, expected);
	}

	@Test
	public void testDeleteDriver() throws IOException
	{
		DefaultJbossParser parser = new DefaultJbossParser();
		DeleteDriver baseEntry = new DeleteDriver();
		baseEntry.setName("driver");
		parser.handle(baseEntry);
		List<String> expected = Collections.singletonList("/subsystem=datasources/jdbc-driver=driver:remove");
		verifyOutput(parser, expected);
	}

	@Test
	public void testAddDatasource() throws IOException
	{
		DefaultJbossParser parser = new DefaultJbossParser();
		AddDatasource baseEntry = new AddDatasource();
		baseEntry.setName("DS");
		baseEntry.setJndi("java:/ds");
		baseEntry.setJTA(true);
		baseEntry.setConnectionUrl("url");
		baseEntry.setDriverName("driver");
		baseEntry.setPassword("pass");
		baseEntry.setUsername("user");
		parser.handle(baseEntry);
		List<String> expected = Arrays.asList("run-batch", "", "data-source add --name=DS --driver-name=driver --jndi-name=java:/ds --connection-url=url --user-name=user --password=pass --jta=true", "batch");
		verifyOutput(parser, expected);
	}

	@Test
	public void testUpdateDatasource() throws IOException
	{
		DefaultJbossParser parser = new DefaultJbossParser();
		UpdateDatasource baseEntry = new UpdateDatasource();
		baseEntry.setName("DS");
		baseEntry.setJndi("java:/ds");
		baseEntry.setJTA(true);
		baseEntry.setConnectionUrl("url");
		baseEntry.setDriverName("driver");
		baseEntry.setPassword("pass");
		baseEntry.setUsername("user");
		parser.handle(baseEntry);
		List<String> expected = Arrays.asList("run-batch", "", "data-source remove --name=DS", "data-source add --name=DS --driver-name=driver --jndi-name=java:/ds --connection-url=url --user-name=user --password=pass --jta=true", "batch");
		verifyOutput(parser, expected);
	}

	@Test
	public void testDeleteDatasource() throws IOException
	{
		DefaultJbossParser parser = new DefaultJbossParser();
		DeleteDatasource baseEntry = new DeleteDatasource();
		baseEntry.setName("DS");
		parser.handle(baseEntry);
		List<String> expected = Arrays.asList("run-batch", "", "data-source remove --name=DS", "batch");
		verifyOutput(parser, expected);
	}

	@Test
	public void testAddSecurityDomain() throws IOException
	{
		DefaultJbossParser parser = new DefaultJbossParser();
		AddSecurityDomain baseEntry = new AddSecurityDomain();
		baseEntry.setName("DS");
		baseEntry.setSecurityClass("Security.class");
		parser.handle(baseEntry);
		List<String> expected = Arrays.asList("/subsystem=security/security-domain=DS:add", "/subsystem=security/security-domain=DS/authentication=classic:add(login-modules=[{\"code\"=>\"Security.class\", \"flag\"=>\"required\"}])");
		verifyOutput(parser, expected);
	}

	@Test
	public void testUpdateSecurityDomain() throws IOException
	{
		DefaultJbossParser parser = new DefaultJbossParser();
		UpdateSecurityDomain baseEntry = new UpdateSecurityDomain();
		baseEntry.setName("DS");
		baseEntry.setSecurityClass("Security.class");
		parser.handle(baseEntry);
		List<String> expected = Arrays.asList("/subsystem=security/security-domain=DS:remove", "/subsystem=security/security-domain=DS:add", "/subsystem=security/security-domain=DS/authentication=classic:add(login-modules=[{\"code\"=>\"Security.class\", \"flag\"=>\"required\"}])");
		verifyOutput(parser, expected);
	}

	@Test
	public void testDeleteSecurityDomain() throws IOException
	{
		DefaultJbossParser parser = new DefaultJbossParser();
		DeleteSecurityDomain baseEntry = new DeleteSecurityDomain();
		baseEntry.setName("DS");
		parser.handle(baseEntry);
		List<String> expected = Collections.singletonList("/subsystem=security/security-domain=DS:remove");
		verifyOutput(parser, expected);
	}

	@Test
	public void testAddMailSession() throws IOException
	{
		DefaultJbossParser parser = new DefaultJbossParser();
		AddMailSession baseEntry = new AddMailSession();
		baseEntry.setName("MS");
		baseEntry.setJndi("java;/mail");
		parser.handle(baseEntry);
		List<String> expected = Collections.singletonList("/subsystem=mail/mail-session=MS:add(jndi-name=java;/mail)");
		verifyOutput(parser, expected);
	}

	@Test
	public void testUpdateMailSession() throws IOException
	{
		DefaultJbossParser parser = new DefaultJbossParser();
		UpdateMailSession baseEntry = new UpdateMailSession();
		baseEntry.setName("MS");
		baseEntry.setJndi("java;/mail");
		parser.handle(baseEntry);
		List<String> expected = Arrays.asList("/subsystem=mail/mail-session=MS:remove", "/subsystem=mail/mail-session=MS:add(jndi-name=java;/mail)");
		verifyOutput(parser, expected);
	}

	@Test
	public void testDeleteMailSession() throws IOException
	{
		DefaultJbossParser parser = new DefaultJbossParser();
		DeleteMailSession baseEntry = new DeleteMailSession();
		baseEntry.setName("MS");
		parser.handle(baseEntry);
		List<String> expected = Collections.singletonList("/subsystem=mail/mail-session=MS:remove");
		verifyOutput(parser, expected);
	}

	@Test
	public void testAddConnectionFactory() throws IOException
	{
		DefaultJbossParser parser = new DefaultJbossParser();
		AddConnectionFactory baseEntry = new AddConnectionFactory();
		baseEntry.setName("CF");
		baseEntry.setAmountOfConnection(BigInteger.TEN);
		baseEntry.getJndi().add("jndi");
		baseEntry.getJndi().add("second");
		parser.handle(baseEntry);
		List<String> expected = Collections.singletonList("/subsystem=messaging/hornetq-server=default/connection-factory=CF:add(connector={\"in-vm\"=>undefined}, entries=[\"jndi\",\"second\"])");
		verifyOutput(parser, expected);
	}

	@Test
	public void testDeleteConnectionFactory() throws IOException
	{
		DefaultJbossParser parser = new DefaultJbossParser();
		DeleteConnectionFactory baseEntry = new DeleteConnectionFactory();
		baseEntry.setName("CF");
		parser.handle(baseEntry);
		List<String> expected = Collections.singletonList("/subsystem=messaging/hornetq-server=default/connection-factory=CF:remove");
		verifyOutput(parser, expected);
	}

	@Test
	public void testChangeLogLevel() throws IOException
	{
		DefaultJbossParser parser = new DefaultJbossParser();
		ChangeLogLevel baseEntry = new ChangeLogLevel();
		baseEntry.setPackage("nl.eernie");
		baseEntry.setType(LogLevels.DEBUG);
		parser.handle(baseEntry);
		List<String> expected = Collections.singletonList("/subsystem=logging/logger=nl.eernie:add(level=DEBUG)");
		verifyOutput(parser, expected);
	}

	@Test
	public void testCustomChange() throws IOException
	{
		DefaultJbossParser parser = new DefaultJbossParser();
		CustomChange baseEntry = new CustomChange();
		baseEntry.setChange("This is a custom change");
		parser.handle(baseEntry);
		List<String> expected = Collections.singletonList("This is a custom change");
		verifyOutput(parser, expected);
	}

	@Test
	public void testInitParser() throws IOException
	{
		DefaultJbossParser parser = new DefaultJbossParser();
		parser.initParser(new Configuration());

		Path tempDirectory = Files.createTempDirectory("junit");
		parser.writeFileToDirectory(tempDirectory.toFile());
		Path outputFile = Paths.get(tempDirectory.toString() + "/jboss.cli");
		List<String> fileContent = Files.readAllLines(outputFile, Charset.defaultCharset());

		assertEquals("## Generated JBOSS CLI script", fileContent.get(1));
		assertTrue(fileContent.get(3).startsWith("## Generated on: "));
		assertTrue(fileContent.get(4).startsWith("## Created by: "));
		assertEquals("## Configuration: Configuration{applicationServers=[], contexts=[], properties={}, fromTag='null', toTag='null', outputDirectoryPath=null}", fileContent.get(5));

		Files.deleteIfExists(outputFile);
		Files.deleteIfExists(tempDirectory);

	}

	private void verifyOutput(DefaultJbossParser parser, List<String> expected) throws IOException
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
