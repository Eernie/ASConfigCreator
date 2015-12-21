package nl.eernie.as;

import nl.eernie.as.application_server.ApplicationServer;
import nl.eernie.as.aschangelog.ApplicationServerChangeLog;
import nl.eernie.as.aschangelog.BaseEntry;
import nl.eernie.as.aschangelog.ChangeSet;
import nl.eernie.as.aschangelog.Include;
import nl.eernie.as.configuration.Configuration;
import nl.eernie.as.parsers.ConfigurationParser;
import nl.eernie.as.variables.VariableReplacer;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.reflections.Reflections;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class ASConfigCreator
{
	private Configuration configuration;
	private Map<ApplicationServer, Set<ConfigurationParser>> configurationParsers = new HashMap<>();

	public ASConfigCreator(Configuration configuration)
	{
		this.configuration = configuration;
		initializeConfigurationParser();
	}

	public void createConfigFiles(String changeLogFilePath) throws IOException
	{
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
		for (ChangeSet changeSet : applicationServerChangeLog.getChangeSet())
		{
			if (!configurationHasContext(changeSet.getContext()))
			{
				continue;
			}

			ApplicationServer applicationServer = null;
			if (changeSet.getApplicationServer() != null)
			{
				applicationServer = getApplicationServer(changeSet.getApplicationServer());
				if (applicationServer == null)
				{
					continue;
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
	}

	private ApplicationServerChangeLog createApplicationServerChangeLog(String changeLogFilePath)
	{
		File file = new File(changeLogFilePath);

		try
		{
			String fileContent = FileUtils.readFileToString(file);
			String replacedFile = VariableReplacer.replace(fileContent, configuration.getProperties());

			InputStream inputStream = new ByteArrayInputStream(replacedFile.getBytes());
			JAXBContext context = JAXBContext.newInstance(ApplicationServerChangeLog.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			return (ApplicationServerChangeLog) unmarshaller.unmarshal(inputStream);
		}
		catch (FileNotFoundException e)
		{
			throw new RuntimeException("File couldn't be found", e);
		}
		catch (JAXBException | IOException e)
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
			if (!server.equalsFromString(applicationServer))
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
