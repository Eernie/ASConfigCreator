package nl.eernie.as.application_server;

public enum ApplicationServer
{
	JBOSS("jboss"),
	WEBSPHERE("websphere", "was"),
	WILDFLY("wildfly");

	private String[] names;

	ApplicationServer(String... names)
	{
		this.names = names;
	}

	public boolean equalsFromString(String name)
	{
		for (String s : names)
		{
			if (s.equalsIgnoreCase(name))
			{
				return true;
			}
		}
		return false;
	}

	public static ApplicationServer fromValue(String serverName)
	{
		for (ApplicationServer applicationServer : values())
		{
			if (applicationServer.equalsFromString(serverName))
			{
				return applicationServer;
			}
		}
		throw new IllegalArgumentException("Unknown application server specified [" + serverName + "]");
	}
}
