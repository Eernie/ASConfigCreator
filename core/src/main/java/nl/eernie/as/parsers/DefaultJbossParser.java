package nl.eernie.as.parsers;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import nl.eernie.as.Version;
import nl.eernie.as.application_server.ApplicationServer;
import nl.eernie.as.aschangelog.AddConnectionFactory;
import nl.eernie.as.aschangelog.AddDLQ;
import nl.eernie.as.aschangelog.AddDatasource;
import nl.eernie.as.aschangelog.AddDriver;
import nl.eernie.as.aschangelog.AddKeycloakAdapter;
import nl.eernie.as.aschangelog.AddMailSession;
import nl.eernie.as.aschangelog.AddProperty;
import nl.eernie.as.aschangelog.AddQueue;
import nl.eernie.as.aschangelog.AddSecurityDomain;
import nl.eernie.as.aschangelog.AddXADatasource;
import nl.eernie.as.aschangelog.BaseEntry;
import nl.eernie.as.aschangelog.ChangeLogLevel;
import nl.eernie.as.aschangelog.CustomChange;
import nl.eernie.as.aschangelog.Datasource;
import nl.eernie.as.aschangelog.DeleteConnectionFactory;
import nl.eernie.as.aschangelog.DeleteDLQ;
import nl.eernie.as.aschangelog.DeleteDatasource;
import nl.eernie.as.aschangelog.DeleteDriver;
import nl.eernie.as.aschangelog.DeleteKeycloakAdapter;
import nl.eernie.as.aschangelog.DeleteMailSession;
import nl.eernie.as.aschangelog.DeleteProperty;
import nl.eernie.as.aschangelog.DeleteQueue;
import nl.eernie.as.aschangelog.DeleteSecurityDomain;
import nl.eernie.as.aschangelog.DeleteXADatasource;
import nl.eernie.as.aschangelog.Driver;
import nl.eernie.as.aschangelog.MailSession;
import nl.eernie.as.aschangelog.Property;
import nl.eernie.as.aschangelog.Queue;
import nl.eernie.as.aschangelog.SecurityDomain;
import nl.eernie.as.aschangelog.UpdateDatasource;
import nl.eernie.as.aschangelog.UpdateDriver;
import nl.eernie.as.aschangelog.UpdateMailSession;
import nl.eernie.as.aschangelog.UpdateProperty;
import nl.eernie.as.aschangelog.UpdateQueue;
import nl.eernie.as.aschangelog.UpdateSecurityDomain;
import nl.eernie.as.aschangelog.UpdateXADatasource;
import nl.eernie.as.configuration.Configuration;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

public class DefaultJbossParser implements ConfigurationParser
{
	private Set<Class<? extends BaseEntry>> parsableEntries = new HashSet<>(Arrays.asList(AddProperty.class, UpdateProperty.class, DeleteProperty.class, AddQueue.class, UpdateQueue.class, DeleteQueue.class, AddDLQ.class, DeleteDLQ.class, AddDriver.class, UpdateDriver.class, DeleteDriver.class, AddDatasource.class, UpdateDatasource.class, DeleteDatasource.class, AddSecurityDomain.class, UpdateSecurityDomain.class, DeleteSecurityDomain.class, AddMailSession.class, UpdateMailSession.class, DeleteMailSession.class, AddConnectionFactory.class, DeleteConnectionFactory.class, ChangeLogLevel.class, CustomChange.class, AddXADatasource.class, UpdateXADatasource.class, DeleteXADatasource.class, AddKeycloakAdapter.class, DeleteKeycloakAdapter.class));
	protected StringBuilder header = new StringBuilder();
	protected StringBuilder stringBuilder = new StringBuilder();
	protected StringBuilder footer = new StringBuilder();

	@Override
	public boolean canHandleChangeLogEntry(BaseEntry entry)
	{
		return parsableEntries.contains(entry.getClass());
	}

	@Override
	public boolean canHandleApplicationServer(ApplicationServer applicationServer)
	{
		return applicationServer == ApplicationServer.JBOSS;
	}

	@Override
	public void beginTransaction()
	{
		stringBuilder.append("batch\n");
	}

	@Override
	public void handle(BaseEntry baseEntry)
	{
		if (baseEntry instanceof UpdateQueue)
		{
			handleEntry((UpdateQueue) baseEntry);
		}
		else if (baseEntry instanceof AddProperty)
		{
			addProperty((Property) baseEntry);
		}
		else if (baseEntry instanceof UpdateProperty)
		{
			updateProperty((Property) baseEntry);
		}
		else if (baseEntry instanceof DeleteProperty)
		{
			deleteProperty(((DeleteProperty) baseEntry).getName());
		}
		else if (baseEntry instanceof AddQueue)
		{
			handleEntry((AddQueue) baseEntry);
		}
		else if (baseEntry instanceof DeleteQueue)
		{
			handleEntry((DeleteQueue) baseEntry);
		}
		else if (baseEntry instanceof AddDLQ)
		{
			handleEntry((AddDLQ) baseEntry);
		}
		else if (baseEntry instanceof DeleteDLQ)
		{
			handleEntry((DeleteDLQ) baseEntry);
		}
		else if (baseEntry instanceof AddDriver)
		{
			handleEntry((AddDriver) baseEntry);
		}
		else if (baseEntry instanceof UpdateDriver)
		{
			handleEntry((UpdateDriver) baseEntry);
		}
		else if (baseEntry instanceof DeleteDriver)
		{
			handleEntry((DeleteDriver) baseEntry);
		}
		else if (baseEntry instanceof AddDatasource)
		{
			handleEntry((AddDatasource) baseEntry);
		}
		else if (baseEntry instanceof UpdateDatasource)
		{
			handleEntry((UpdateDatasource) baseEntry);
		}
		else if (baseEntry instanceof DeleteDatasource)
		{
			handleEntry((DeleteDatasource) baseEntry);
		}
		else if (baseEntry instanceof AddSecurityDomain)
		{
			handleEntry((AddSecurityDomain) baseEntry);
		}
		else if (baseEntry instanceof UpdateSecurityDomain)
		{
			handleEntry((UpdateSecurityDomain) baseEntry);
		}
		else if (baseEntry instanceof DeleteSecurityDomain)
		{
			handleEntry((DeleteSecurityDomain) baseEntry);
		}
		else if (baseEntry instanceof AddMailSession)
		{
			handleEntry((AddMailSession) baseEntry);
		}
		else if (baseEntry instanceof UpdateMailSession)
		{
			handleEntry((UpdateMailSession) baseEntry);
		}
		else if (baseEntry instanceof DeleteMailSession)
		{
			handleEntry((DeleteMailSession) baseEntry);
		}
		else if (baseEntry instanceof AddConnectionFactory)
		{
			handleEntry((AddConnectionFactory) baseEntry);
		}
		else if (baseEntry instanceof DeleteConnectionFactory)
		{
			handleEntry((DeleteConnectionFactory) baseEntry);
		}
		else if (baseEntry instanceof ChangeLogLevel)
		{
			handleEntry((ChangeLogLevel) baseEntry);
		}
		else if (baseEntry instanceof CustomChange)
		{
			handleEntry((CustomChange) baseEntry);
		}
		else if (baseEntry instanceof AddKeycloakAdapter)
		{
			handleEntry((AddKeycloakAdapter) baseEntry);
		}
		else if (baseEntry instanceof DeleteKeycloakAdapter)
		{
			handleEntry((DeleteKeycloakAdapter) baseEntry);
		}
	}

	@Override
	public void commitTransaction()
	{
		stringBuilder.append("run-batch\n\n");
	}

	@Override
	public void writeFileToDirectory(File outputDirectoryPath) throws IOException
	{
		File file = new File(outputDirectoryPath, "jboss.cli");
		FileUtils.write(file, header.append(stringBuilder).append(footer));
	}

	@Override
	public void initParser(Configuration configuration)
	{
		stringBuilder = new StringBuilder();

		String host;
		try
		{
			InetAddress localHost = InetAddress.getLocalHost();
			host = localHost.getHostName() + '(' + System.getProperty("user.name") + ')';
		}
		catch (UnknownHostException e)
		{
			host = "unknown";
		}

		header = new StringBuilder();
		header.append("## *********************************************************************\n");
		header.append("## Generated JBOSS CLI script\n");
		header.append("## *********************************************************************\n");
		header.append("## Generated on: ").append(DateFormat.getDateTimeInstance().format(new Date())).append('\n');
		header.append("## Created by: ").append(host).append('\n');
		header.append("## Generated with version: ").append(Version.getVersion()).append('\n');
		header.append("## Configuration: ").append(configuration).append('\n');
		header.append("## *********************************************************************\n\n");
	}

	protected void addProperty(Property entry)
	{
		stringBuilder.append("/system-property=");
		stringBuilder.append(entry.getName());
		stringBuilder.append(":add(value=");
		stringBuilder.append(entry.getValue());
		stringBuilder.append(")\n");
	}

	protected void updateProperty(Property entry)
	{
		deleteProperty(entry.getName());
		addProperty(entry);
	}

	protected void deleteProperty(String name)
	{
		stringBuilder.append("/system-property=");
		stringBuilder.append(name);
		stringBuilder.append(":remove\n");
	}

	protected void handleEntry(DeleteQueue entry)
	{
		removeQueue(entry.getName());
	}

	protected void handleEntry(AddQueue entry)
	{
		createQueue(entry);
	}

	protected void handleEntry(UpdateQueue entry)
	{
		removeQueue(entry.getName());
		createQueue(entry);
	}

	protected void removeQueue(String name)
	{
		stringBuilder.append("jms-queue remove");
		stringBuilder.append(" --queue-address=").append(name);
		stringBuilder.append('\n');
	}

	protected void createQueue(Queue entry)
	{
		stringBuilder.append("jms-queue add");
		stringBuilder.append(" --queue-address=").append(entry.getName());
		stringBuilder.append(" --entries=").append(entry.getJndi());
		stringBuilder.append(" --durable=").append(entry.isPersistent());
		stringBuilder.append('\n');
	}

	protected void handleEntry(AddDLQ entry)
	{
		for (String matchingJndi : entry.getMonitorAddress())
		{
			stringBuilder.append("/subsystem=messaging/hornetq-server=default/address-setting=").append(matchingJndi);
			stringBuilder.append(":add(dead-letter-address=").append(entry.getDeliveryQueue());
			if (entry.getExpiryAddress() != null)
			{
				stringBuilder.append(",expiry-address=").append(entry.getExpiryAddress());
			}
			if (entry.getMaxDeliveryAttempts() != null)
			{
				stringBuilder.append(",max-delivery-attempts=").append(entry.getMaxDeliveryAttempts());
			}
			if (entry.getMaxSizeBytes() != null)
			{
				stringBuilder.append(",max-size-bytes=").append(entry.getMaxSizeBytes());
			}
			if (entry.getPageSizeBytes() != null)
			{
				stringBuilder.append(",page-size-bytes=").append(entry.getPageSizeBytes());
			}
			if (entry.getMessageCounterHistoryDayLimit() != null)
			{
				stringBuilder.append(",message-counter-history-day-limit=").append(entry.getMessageCounterHistoryDayLimit());
			}
			stringBuilder.append(')');
			stringBuilder.append('\n');
		}
	}

	protected void handleEntry(DeleteDLQ entry)
	{
		stringBuilder.append("/subsystem=messaging/hornetq-server=default/address-setting=").append(entry.getMonitorAddress());
		stringBuilder.append(":remove\n");
	}

	protected void handleEntry(AddDriver entry)
	{
		addDriver(entry);
	}

	protected void handleEntry(UpdateDriver entry)
	{
		deleteDriver(entry.getName());
		addDriver(entry);
	}

	protected void handleEntry(DeleteDriver entry)
	{
		deleteDriver(entry.getName());
	}

	protected void addDriver(Driver entry)
	{
		stringBuilder.append("/subsystem=datasources/jdbc-driver=").append(entry.getName());
		stringBuilder.append(":add(driver-name=").append(entry.getName());
		stringBuilder.append(",driver-module-name=").append(entry.getModule());
		if (entry.getXaDriver() != null)
		{
			stringBuilder.append(",driver-xa-datasource-class-name=").append(entry.getXaDriver());
		}
		stringBuilder.append(')');
		stringBuilder.append('\n');
	}

	protected void deleteDriver(String entry)
	{
		stringBuilder.append("/subsystem=datasources/jdbc-driver=").append(entry).append(":remove\n");
	}

	protected void handleEntry(AddDatasource entry)
	{
		commitTransaction();
		addDatasource(entry);
		beginTransaction();
	}

	protected void handleEntry(UpdateDatasource entry)
	{
		commitTransaction();
		deleteDatasource(entry.getName());
		addDatasource(entry);
		beginTransaction();
	}

	protected void handleEntry(DeleteDatasource entry)
	{
		commitTransaction();
		deleteDatasource(entry.getName());
		beginTransaction();
	}

	protected void addDatasource(Datasource entry)
	{
		stringBuilder.append("data-source add --name=").append(entry.getName());
		stringBuilder.append(" --driver-name=").append(entry.getDriverName());
		stringBuilder.append(" --jndi-name=").append(entry.getJndi());
		stringBuilder.append(" --connection-url=").append(entry.getConnectionUrl());
		stringBuilder.append(" --user-name=").append(entry.getUsername());
		stringBuilder.append(" --password=").append(entry.getPassword());
		stringBuilder.append(" --jta=").append(entry.isJTA());
		stringBuilder.append('\n');
	}

	protected void deleteDatasource(String name)
	{
		stringBuilder.append("data-source remove --name=").append(name).append('\n');
	}

	protected void handleEntry(AddSecurityDomain entry)
	{
		addSecurityDomain(entry);
	}

	protected void handleEntry(UpdateSecurityDomain entry)
	{
		deleteSecurityDomain(entry.getName());
		addSecurityDomain(entry);
	}

	protected void handleEntry(DeleteSecurityDomain entry)
	{
		deleteSecurityDomain(entry.getName());
	}

	protected void addSecurityDomain(SecurityDomain entry)
	{
		stringBuilder.append("/subsystem=security/security-domain=").append(entry.getName());
		stringBuilder.append(":add");
		stringBuilder.append("\n");

		stringBuilder.append("/subsystem=security/security-domain=").append(entry.getName());
		stringBuilder.append("/authentication=classic:add(login-modules=[{\"code\"=>\"").append(entry.getSecurityClass()).append('"');
		stringBuilder.append(", \"flag\"=>\"required\"}])");
		stringBuilder.append('\n');
	}

	protected void deleteSecurityDomain(String entry)
	{
		stringBuilder.append("/subsystem=security/security-domain=").append(entry);
		stringBuilder.append(":remove");
		stringBuilder.append('\n');
	}

	protected void handleEntry(AddMailSession entry)
	{
		addMailSession(entry);
	}

	protected void handleEntry(UpdateMailSession entry)
	{
		deleteMailSession(entry.getName());
		addMailSession(entry);
	}

	protected void handleEntry(DeleteMailSession entry)
	{
		deleteMailSession(entry.getName());
	}

	protected void addMailSession(MailSession entry)
	{
		stringBuilder.append("/subsystem=mail/mail-session=").append(entry.getName());
		stringBuilder.append(":add(jndi-name=").append(entry.getJndi()).append(')');
		stringBuilder.append('\n');

		if (entry.getHostname() != null && entry.getPort() != null)
		{
			stringBuilder.append("/socket-binding-group=standard-sockets/remote-destination-outbound-socket-binding=");
			stringBuilder.append(entry.getName());
			stringBuilder.append(":add(host=").append(entry.getHostname());
			stringBuilder.append(",port=").append(entry.getPort()).append(")\n");

			stringBuilder.append("/subsystem=mail/mail-session=").append(entry.getName());
			stringBuilder.append("/server=smtp:add( outbound-socket-binding-ref=").append(entry.getName()).append(")\n");
		}
	}

	protected void deleteMailSession(String entry)
	{
		stringBuilder.append("/subsystem=mail/mail-session=").append(entry);
		stringBuilder.append(":remove");
		stringBuilder.append('\n');
	}

	protected void handleEntry(AddConnectionFactory entry)
	{
		stringBuilder.append("/subsystem=messaging/hornetq-server=default/connection-factory=").append(entry.getName());
		String joined = StringUtils.join(entry.getJndi().iterator(), "\",\"");
		stringBuilder.append(":add(connector={\"in-vm\"=>undefined}, entries=[\"").append(joined).append("\"])");
		stringBuilder.append('\n');
	}

	protected void handleEntry(DeleteConnectionFactory entry)
	{
		stringBuilder.append("/subsystem=messaging/hornetq-server=default/connection-factory=").append(entry.getName());
		stringBuilder.append(":remove");
		stringBuilder.append('\n');
	}

	protected void handleEntry(ChangeLogLevel entry)
	{
		stringBuilder.append("/subsystem=logging/logger=").append(entry.getPackage());
		stringBuilder.append(":add(level=").append(entry.getType().value()).append(")\n");
	}

	protected void handleEntry(CustomChange baseEntry)
	{
		stringBuilder.append(baseEntry.getChange());
		stringBuilder.append('\n');
	}

	private void handleEntry(AddKeycloakAdapter addKeycloakAdapter)
	{
		stringBuilder.append("/subsystem=security/security-domain=keycloak/:add\n");
		stringBuilder.append("/subsystem=security/security-domain=keycloak/authentication=classic/:add(login-modules=[{ \"code\" => \"org.keycloak.adapters.jboss.KeycloakLoginModule\",\"flag\" => \"required\"}])\n");
		header.append("/extension=org.keycloak.keycloak-adapter-subsystem/:add(module=org.keycloak.keycloak-adapter-subsystem)\n");
		header.append("/subsystem=keycloak:add\n");
	}

	private void handleEntry(DeleteKeycloakAdapter deleteKeycloakAdapter)
	{
		stringBuilder.append("/subsystem=security/security-domain=keycloak/:remove\n");
		footer.append("/extension=org.keycloak.keycloak-adapter-subsystem/:remove\n");
		footer.append("/subsystem=keycloak:remove\n");
	}
}
