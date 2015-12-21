package nl.eernie.as;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;

import java.io.File;

public class GenerateConfigFileMojoTest extends AbstractMojoTestCase
{
	public void testValidProject() throws Exception
	{
		File pom = getTestFile("src/test/resources/pom.xml");

		GenerateConfigFileMojo mojo = (GenerateConfigFileMojo) lookupMojo("generateConfig", pom);

		mojo.execute();

	}
}