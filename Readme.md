# ASConfigCreator
[![Build Status](https://travis-ci.org/Eernie/ASConfigCreator.svg?branch=develop)](https://travis-ci.org/Eernie/ASConfigCreator)
[![Coverage Status](https://coveralls.io/repos/Eernie/ASConfigCreator/badge.svg?branch=develop&service=github)](https://coveralls.io/github/Eernie/ASConfigCreator?branch=develop)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/nl.eernie.as/ASConfigCreator/badge.svg)](https://maven-badges.herokuapp.com/maven-central/nl.eernie.as/ASConfigCreator)

Sourcecontrol for you application server configuration.
## Builing the source
Its as easy as using maven:
```bash
mvn install
```

## Usage
There are two ways to use the application, directly or trough the maven plugin.

### Direct
For the direct approach you'll have to setup some configuration:
```java
Configuration configuration = new Configuration();
configuration.getContexts().addAll(Arrays.asList(contexts));
configuration.getApplicationServers().addAll(applicationServers);
configuration.setOutputDirectoryPath(outputDirectory);

ASConfigCreator asConfigCreator = new ASConfigCreator(configuration);
asConfigCreator.createConfigFiles(masterFile.getAbsolutePath());
```

### Maven plugin
For the maven plugin you can use the following snippet:
```xml
<plugin>
	<groupId>nl.eernie.as</groupId>
	<artifactId>ASConfigCreator-maven-plugin</artifactId>
	<version>[latestVersion]</version>
	<configuration>
		<settingsFile>${basedir}/settings.properties</settingsFile>
	</configuration>
</plugin>
```
