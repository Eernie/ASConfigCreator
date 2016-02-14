package nl.eernie.as.reporter;

import java.io.File;

import nl.eernie.as.aschangelog.BaseEntry;
import nl.eernie.as.aschangelog.Tag;

public interface Reporter
{
	void tag(Tag tag);

	void reportEntry(BaseEntry baseEntry);

	void writeFileToDirectory(File outputDirectoryPath);
}
