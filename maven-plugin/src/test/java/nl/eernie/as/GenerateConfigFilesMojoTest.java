package nl.eernie.as;

import java.io.File;

import org.apache.maven.execution.DefaultMavenExecutionRequest;
import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuilder;
import org.apache.maven.project.ProjectBuildingRequest;
import org.eclipse.aether.DefaultRepositorySystemSession;

public class GenerateConfigFilesMojoTest extends AbstractMojoTestCase
{
	public void testValidMultiProject() throws Exception
	{
		final File pom = getTestFile("src/test/resources/validMultiProject/pom.xml");

		MavenExecutionRequest executionRequest = new DefaultMavenExecutionRequest();
		ProjectBuildingRequest buildingRequest = executionRequest.getProjectBuildingRequest();
		buildingRequest.setRepositorySession(new DefaultRepositorySystemSession());
		ProjectBuilder projectBuilder = this.lookup(ProjectBuilder.class);
		MavenProject project = projectBuilder.build(pom, buildingRequest).getProject();

		GenerateConfigFilesMojo mojo = (GenerateConfigFilesMojo) lookupConfiguredMojo(project, "generateConfigs");
		mojo.execute();

		assertTrue(new File("src/test/resources/validMultiProject/target/asconfig/test1.cli").exists());
		assertTrue(new File("src/test/resources/validMultiProject/target/asconfig/test2.cli").exists());
	}

	public void testInvalidMultiProjectByNonExistingInputDirectory() throws Exception
	{
		final File pom = getTestFile("src/test/resources/invalidMultiProject/pom.xml");
		final GenerateConfigFilesMojo mojo = ((GenerateConfigFilesMojo) lookupMojo("generateConfigs", pom));
		try
		{
			mojo.execute();
			fail("A failure should be thrown!");
		}
		catch (MojoExecutionException e)
		{
			fail("A failure should be thrown!");
		}
		catch (MojoFailureException e)
		{
			assertEquals("Cannot read input directory", e.getMessage());
		}
	}

	public void testInvalidMultiProjectByMissingRequiredProperties() throws Exception
	{
		final File pom = getTestFile("src/test/resources/invalidMultiProject2/pom.xml");
		final GenerateConfigFilesMojo mojo = (GenerateConfigFilesMojo) lookupMojo("generateConfigs", pom);
		try
		{
			mojo.execute();
			fail("A failure should be thrown");
		}
		catch (MojoExecutionException e)
		{
			fail("A failure should be thrown");
		}
		catch (MojoFailureException e)
		{
			assertEquals("No valid settings files where found!", e.getMessage());
		}
	}
}
