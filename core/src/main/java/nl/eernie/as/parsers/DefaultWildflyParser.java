package nl.eernie.as.parsers;

import nl.eernie.as.application_server.ApplicationServer;
import nl.eernie.as.aschangelog.AddDatasource;
import nl.eernie.as.aschangelog.Datasource;
import nl.eernie.as.aschangelog.DeleteDatasource;
import nl.eernie.as.aschangelog.UpdateDatasource;

public class DefaultWildflyParser extends DefaultJbossParser implements ConfigurationParser
{
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
}
