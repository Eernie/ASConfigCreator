package nl.eernie.as.parsers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import nl.eernie.as.application_server.ApplicationServer;
import nl.eernie.as.aschangelog.AddDatasource;
import nl.eernie.as.aschangelog.AddXADatasource;
import nl.eernie.as.aschangelog.BaseEntry;
import nl.eernie.as.aschangelog.Datasource;
import nl.eernie.as.aschangelog.DeleteDatasource;
import nl.eernie.as.aschangelog.DeleteXADatasource;
import nl.eernie.as.aschangelog.UpdateDatasource;
import nl.eernie.as.aschangelog.UpdateXADatasource;
import nl.eernie.as.aschangelog.XaDatasource;
import nl.eernie.as.aschangelog.XaProperty;
import nl.eernie.as.configuration.Configuration;
import nl.eernie.as.util.SortedProperties;
import nl.eernie.as.variables.VariableFinder;

import org.apache.commons.io.FileUtils;

public class DefaultWildflyParser extends DefaultJbossParser implements ConfigurationParser
{
	private Set<Class<? extends BaseEntry>> parsableEntries = new HashSet<>(Arrays.asList(AddXADatasource.class, UpdateXADatasource.class, DeleteXADatasource.class));
	private Configuration configuration;

	@Override
	public boolean canHandleChangeLogEntry(BaseEntry entry)
	{
		return super.canHandleChangeLogEntry(entry) || parsableEntries.contains(entry.getClass());
	}

	@Override
	public void initParser(Configuration configuration)
	{
		super.initParser(configuration);
		this.configuration = configuration;
	}

	@Override
	public void handle(BaseEntry baseEntry)
	{
		if (baseEntry instanceof AddXADatasource)
		{
			handleEntry((AddXADatasource) baseEntry);
		}
		else if (baseEntry instanceof UpdateXADatasource)
		{
			handleEntry((UpdateXADatasource) baseEntry);
		}
		else if (baseEntry instanceof DeleteXADatasource)
		{
			deleteXADatasource(((DeleteXADatasource) baseEntry).getName());
		}
		else
		{
			super.handle(baseEntry);
		}
	}

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

	protected void handleEntry(AddXADatasource entry)
	{
		addXADatasource(entry);
	}

	protected void handleEntry(UpdateXADatasource entry)
	{
		deleteXADatasource(entry.getName());
		addXADatasource(entry);
	}

	protected void deleteXADatasource(String name)
	{
		stringBuilder.append("xa-data-source remove --name=").append(name).append('\n');
	}

	protected void addXADatasource(XaDatasource entry)
	{
		stringBuilder.append("xa-data-source add --name=").append(entry.getName());
		stringBuilder.append(" --driver-name=").append(entry.getDriverName());
		stringBuilder.append(" --jndi-name=").append(entry.getJndi());
		stringBuilder.append(" --user-name=").append(entry.getUsername());
		stringBuilder.append(" --password=").append(entry.getPassword());
		stringBuilder.append(" --jta=").append(entry.isJTA());
		stringBuilder.append('\n');

		for (XaProperty xaProperty : entry.getProperty())
		{
			stringBuilder.append("/subsystem=datasources/xa-data-source=").append(entry.getName());
			stringBuilder.append("/xa-datasource-properties=").append(xaProperty.getName());
			stringBuilder.append(":add(value=").append(xaProperty.getValue()).append(')');
			stringBuilder.append('\n');
		}
	}

	@Override
	public void writeFileToDirectory(File outputDirectoryPath) throws IOException
	{
		StringBuilder variablesBuilder = new StringBuilder();
		Properties properties = new SortedProperties();

		String fileContent = stringBuilder.toString();
		Set<String> variables = VariableFinder.findVariables(fileContent);
		fileContent = VariableFinder.replaceWithoutBrackets(variables, fileContent);
		for (String key : variables)
		{
			properties.put(key, "");
			variablesBuilder.append(String.format("set %s=${%s}\n", key, key));
		}

		File file = new File(outputDirectoryPath, "wildfly.cli");
		FileUtils.write(file, header.append(variablesBuilder).append('\n').append(fileContent));

		try (FileWriter writer = new FileWriter(new File(outputDirectoryPath, "wildfly.properties")))
		{
			properties.store(writer, "Generated Wildfly CLI properties");
		}
	}
}
