package nl.eernie.as.parsers;

import java.io.IOException;

import nl.eernie.as.application_server.ApplicationServer;
import nl.eernie.as.aschangelog.BaseEntry;
import nl.eernie.as.configuration.Configuration;

public interface ConfigurationParser
{
	boolean canHandleChangeLogEntry(BaseEntry entry);

	boolean canHandleApplicationServer(ApplicationServer applicationServer);

	void beginTransaction();

	void handle(BaseEntry baseEntry);

	void commitTransaction();

	void writeFileToDirectory() throws IOException;

	void initParser(Configuration configuration);
}
