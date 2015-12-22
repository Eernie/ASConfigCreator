package nl.eernie.as;

import org.apache.maven.execution.DefaultMavenExecutionRequest;
import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuilder;
import org.apache.maven.project.ProjectBuildingRequest;
import org.eclipse.aether.DefaultRepositorySystemSession;

import java.io.File;

public class GenerateConfigFileMojoTest extends AbstractMojoTestCase
{
	public void testValidProject() throws Exception
	{
		File pom = getTestFile("src/test/resources/validProject/pom.xml");

		MavenExecutionRequest executionRequest = new DefaultMavenExecutionRequest();
		ProjectBuildingRequest buildingRequest = executionRequest.getProjectBuildingRequest();
		buildingRequest.setRepositorySession(new DefaultRepositorySystemSession());
		ProjectBuilder projectBuilder = this.lookup(ProjectBuilder.class);
		MavenProject project = projectBuilder.build(pom, buildingRequest).getProject();

		GenerateConfigFileMojo mojo = (GenerateConfigFileMojo) lookupConfiguredMojo(project, "generateConfig");
		mojo.execute();

		assertTrue(new File("src/test/resources/validProject/target/asconfig/jboss.cli").exists());
	}

	public void testInvalidProjectByNoSettings() throws Exception
	{
		File pom = getTestFile("src/test/resources/invalidProject/pom.xml");
		GenerateConfigFileMojo mojo = (GenerateConfigFileMojo) lookupMojo("generateConfig", pom);
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
			assertEquals("Unable to read file at doesn't exist", e.getMessage());
		}
	}

	public void testInvalidProjectByNoContext() throws Exception
	{
		File pom = getTestFile("src/test/resources/invalidProject2/pom.xml");
		GenerateConfigFileMojo mojo = (GenerateConfigFileMojo) lookupMojo("generateConfig", pom);
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
			assertEquals("No contexts provided in settings file", e.getMessage());
		}
	}

	public void testInvalidProjectByNoApplicationServer() throws Exception
	{
		File pom = getTestFile("src/test/resources/invalidProject3/pom.xml");
		GenerateConfigFileMojo mojo = (GenerateConfigFileMojo) lookupMojo("generateConfig", pom);
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
			assertEquals("No application servers provided in settings file", e.getMessage());
		}
	}
}