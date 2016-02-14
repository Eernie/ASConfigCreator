package nl.eernie.as.changelog;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import nl.eernie.as.aschangelog.BaseEntry;
import nl.eernie.as.aschangelog.Tag;
import nl.eernie.as.reporter.Reporter;

public class ChangelogReporter implements Reporter
{
	private final ChangeLog changeLog = new ChangeLog();

	@Override
	public void tag(Tag tag)
	{
		Version version = new Version();
		version.setName(tag.getVersion());
		changeLog.getVersions().add(version);
	}

	@Override
	public void reportEntry(BaseEntry baseEntry)
	{
		if (changeLog.getVersions().size() == 0)
		{
			Version versionUnknown = new Version();
			versionUnknown.setName("Unknown");
			changeLog.getVersions().add(versionUnknown);
		}

		Version lastVersion = changeLog.getVersions().getLast();

		addChange(lastVersion, baseEntry.getClass(), baseEntry);
	}

	@Override
	public void writeFileToDirectory(File outputDirectoryPath)
	{
		File file = new File(outputDirectoryPath, "changelogd.md");
		try
		{
			FileWriter writer = new FileWriter(file);
			new ChangeLogTemplate().render(writer, changeLog, new Date());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void addChange(Version lastVersion, Class<? extends BaseEntry> classCategory, BaseEntry baseEntry)
	{
		if (!lastVersion.getChanges().containsKey(classCategory))
		{
			lastVersion.getChanges().put(classCategory, new ArrayList<BaseEntry>());
		}
		lastVersion.getChanges().get(classCategory).add(baseEntry);
	}
}
