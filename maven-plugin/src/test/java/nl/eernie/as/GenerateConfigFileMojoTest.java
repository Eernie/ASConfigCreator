package nl.eernie.as;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.junit.Assert;

import java.io.File;

public class GenerateConfigFileMojoTest extends AbstractMojoTestCase
{
	public void testValidProject() throws Exception
	{
		File pom = getTestFile("src/test/resources/pom.xml");
		Assert.assertNotNull(pom);
		Assert.assertTrue(pom.exists());

		GenerateConfigFileMojo mojo = (GenerateConfigFileMojo) lookupMojo("generateConfig", pom);
		Assert.assertNotNull(mojo);
		mojo.execute();

	}
}