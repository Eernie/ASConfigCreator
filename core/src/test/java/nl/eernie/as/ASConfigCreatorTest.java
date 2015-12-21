package nl.eernie.as;

import nl.eernie.as.application_server.ApplicationServer;
import nl.eernie.as.configuration.Configuration;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ASConfigCreatorTest
{
	@Test
	public void test() throws JAXBException, IOException
	{
		Configuration configuration = new Configuration();
		configuration.getApplicationServers().add(ApplicationServer.WILDFLY);
		configuration.getContexts().add("core");
		configuration.setOutputDirectoryPath(File.createTempFile("test", "test").getParentFile());
		ASConfigCreator asConfigCreator = new ASConfigCreator(configuration);
		URL resource = getClass().getResource("/test.xml");
		asConfigCreator.createConfigFiles(resource.getPath());
	}
}