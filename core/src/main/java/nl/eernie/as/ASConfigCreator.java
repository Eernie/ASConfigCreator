package nl.eernie.as;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import nl.eernie.as.application_server.ApplicationServer;
import nl.eernie.as.aschangelog.ApplicationServerChangeLog;
import nl.eernie.as.aschangelog.BaseEntry;
import nl.eernie.as.aschangelog.ChangeSet;
import nl.eernie.as.aschangelog.Include;
import nl.eernie.as.aschangelog.Tag;
import nl.eernie.as.configuration.Configuration;
import nl.eernie.as.parsers.ConfigurationParser;

import org.apache.commons.io.FilenameUtils;
import org.reflections.Reflections;

public class ASConfigCreator
{
	private Configuration configuration;
	private Map<ApplicationServer, Set<ConfigurationParser>> configurationParsers = new HashMap<>();
	private boolean fromTagFound;
	private boolean endTagFound = false;

	public ASConfigCreator(Configuration configuration)
	{
		this.configuration = configuration;
		initializeConfigurationParser();
	}

	public void createConfigFiles(String changeLogFilePath) throws IOException
	{
		fromTagFound = configuration.getFromTag() == null;
		endTagFound = false;
		for (ConfigurationParser configurationParser : configurationParsers.get(null))
		{
			configurationParser.initParser(configuration);
		}
		parseFile(changeLogFilePath);
		for (ConfigurationParser configurationParser : configurationParsers.get(null))
		{
			configurationParser.writeFileToDirectory(configuration.getOutputDirectoryPath());
		}
	}

	private void parseFile(String changeLogFilePath)
	{
		ApplicationServerChangeLog applicationServerChangeLog = createApplicationServerChangeLog(changeLogFilePath);
		for (Include include : applicationServerChangeLog.getInclude())
		{
			if (include.getContext() == null || (include.getContext() != null && configurationHasContext(include.getContext())))
			{
				String path;
				if (include.isRelativeToCurrentFile())
				{
					path = FilenameUtils.getFullPath(changeLogFilePath);
					path = path + include.getPath();
				}
				else
				{
					path = include.getPath();
				}
				parseFile(path);
			}
		}

		for (Serializable entry : applicationServerChangeLog.getChangeLogEntry())
		{
			if (entry instanceof Tag)
			{
				processTag((Tag) entry);
			}
			else if (entry instanceof ChangeSet)
			{
				processChangeSet((ChangeSet) entry);
			}
		}
	}

	private void processTag(Tag tag)
	{
		if (tag.getVersion().equals(configuration.getFromTag()))
		{
			fromTagFound = true;
		}
		if (tag.getVersion().equals(configuration.getToTag()))
		{
			endTagFound = true;
		}
	}

	private void processChangeSet(ChangeSet changeSet)
	{
		if (!fromTagFound || endTagFound)
		{
			return;
		}
		else if (!configurationHasContext(changeSet.getContext()))
		{
			return;
		}

		ApplicationServer applicationServer = null;
		if (changeSet.getApplicationServer() != null)
		{
			applicationServer = getApplicationServer(changeSet.getApplicationServer());
			if (applicationServer == null)
			{
				return;
			}
		}

		Set<ConfigurationParser> configurationParsers = this.configurationParsers.get(applicationServer);
		for (ConfigurationParser configurationParser : configurationParsers)
		{
			configurationParser.beginTransaction();
		}

		for (BaseEntry baseEntry : changeSet.getChangeSetEntry())
		{
			for (ConfigurationParser configurationParser : configurationParsers)
			{
				if (configurationParser.canHandleChangeLogEntry(baseEntry))
				{
					configurationParser.handle(baseEntry);
				}
			}
		}

		for (ConfigurationParser configurationParser : configurationParsers)
		{
			configurationParser.commitTransaction();
		}
	}

	private ApplicationServerChangeLog createApplicationServerChangeLog(String changeLogFilePath)
	{
		File file = new File(changeLogFilePath);

		try
		{
			JAXBContext context = JAXBContext.newInstance(ApplicationServerChangeLog.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			return (ApplicationServerChangeLog) unmarshaller.unmarshal(file);
		}
		catch (JAXBException e)
		{
			throw new RuntimeException("Something went wrong while unmarshalling the file " + changeLogFilePath, e);
		}
	}

	private boolean configurationHasContext(String contextList)
	{
		String[] contexts = contextList.split(",");
		for (String context : contexts)
		{
			boolean negate = context.startsWith("!");
			context = context.replace("!", "");
			boolean contains = configuration.getContexts().contains(context);
			if ((contains && !negate) || (!contains && negate))
			{
				return true;
			}
		}
		return false;
	}

	private ApplicationServer getApplicationServer(String applicationServer)
	{
		for (ApplicationServer server : configuration.getApplicationServers())
		{
			if (server.equalsFromString(applicationServer))
			{
				return server;
			}
		}
		return null;
	}

	private void initializeConfigurationParser()
	{
		Reflections reflections = new Reflections("nl.eernie.as");
		Set<Class<? extends ConfigurationParser>> configurationParserClasses = reflections.getSubTypesOf(ConfigurationParser.class);
		for (Class<? extends ConfigurationParser> configurationParserClass : configurationParserClasses)
		{
			try
			{
				ConfigurationParser configurationParser = configurationParserClass.newInstance();
				for (ApplicationServer applicationServer : configuration.getApplicationServers())
				{
					if (configurationParser.canHandleApplicationServer(applicationServer))
					{
						addParser(applicationServer, configurationParser);
						addParser(null, configurationParser);
					}
				}
			}
			catch (InstantiationException | IllegalAccessException e)
			{
				throw new RuntimeException("Couldn't instantiate new parser of class: " + configurationParserClass);
			}
		}
	}

	private void addParser(ApplicationServer applicationServer, ConfigurationParser configurationParser)
	{
		if (!configurationParsers.containsKey(applicationServer))
		{
			configurationParsers.put(applicationServer, new LinkedHashSet<ConfigurationParser>(1));
		}
		configurationParsers.get(applicationServer).add(configurationParser);
	}
}
