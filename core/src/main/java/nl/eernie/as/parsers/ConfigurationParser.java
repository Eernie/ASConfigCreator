package nl.eernie.as.parsers;

import nl.eernie.as.application_server.ApplicationServer;
import nl.eernie.as.aschangelog.BaseEntry;

import java.io.File;
import java.io.IOException;

public interface ConfigurationParser
{
	boolean canHandleChangeLogEntry(BaseEntry entry);

	boolean canHandleApplicationServer(ApplicationServer applicationServer);

	void beginTransaction();

	void handle(BaseEntry baseEntry);

	void commitTransaction();

	void writeFileToDirectory(File outputDirectoryPath) throws IOException;
}
