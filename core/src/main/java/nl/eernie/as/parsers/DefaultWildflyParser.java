package nl.eernie.as.parsers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import nl.eernie.as.application_server.ApplicationServer;
import nl.eernie.as.aschangelog.AddDatasource;
import nl.eernie.as.aschangelog.Datasource;
import nl.eernie.as.aschangelog.DeleteDatasource;
import nl.eernie.as.aschangelog.UpdateDatasource;
import nl.eernie.as.configuration.Configuration;

import org.apache.commons.io.FileUtils;

public class DefaultWildflyParser extends DefaultJbossParser implements ConfigurationParser
{
	private Configuration configuration;

	@Override
	public boolean canHandleApplicationServer(ApplicationServer applicationServer)
	{
		return applicationServer == ApplicationServer.WILDFLY;
	}

	@Override
	protected void handleEntry(AddDatasource entry)
	{
		addDatasource(entry);
	}

	@Override
	protected void handleEntry(UpdateDatasource entry)
	{
		deleteDatasource(entry.getName());
		addDatasource(entry);
	}

	@Override
	protected void handleEntry(DeleteDatasource entry)
	{
		deleteDatasource(entry.getName());
	}

	@Override
	protected void addDatasource(Datasource entry)
	{
		stringBuilder.append("data-source add --name=").append(entry.getName());
		stringBuilder.append(" --driver-name=").append(entry.getDriverName());
		stringBuilder.append(" --jndi-name=").append(entry.getJndi());
		stringBuilder.append(" --connection-url=").append(entry.getConnectionUrl());
		stringBuilder.append(" --user-name=").append(entry.getUsername());
		stringBuilder.append(" --password=").append(entry.getPassword());
		stringBuilder.append(" --jta=").append(entry.isJTA());
		stringBuilder.append(" --enabled=").append(true);
		stringBuilder.append('\n');
	}

	@Override
	public void initParser(Configuration configuration)
	{
		super.initParser(configuration);
		this.configuration = configuration;
	}

	@Override
	public void writeFileToDirectory(File outputDirectoryPath) throws IOException
	{
		File file = new File(outputDirectoryPath, "wildfly.cli");
		FileUtils.write(file, stringBuilder);

		Properties properties = new Properties();
		for (String key : configuration.getFoundVariables())
		{
			properties.put(key, "");
		}

		try (FileWriter writer = new FileWriter(new File(outputDirectoryPath, "wildfly.properties")))
		{
			properties.store(writer, "Generated Wildfly CLI properties");
		}
	}
}
