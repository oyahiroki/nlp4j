# Introduction 

NLP4J Core Component

# Background

Current language processing cannot handle multiple language processing runtimes in a unified manner. As an example, there is no unified way to handle Stanford NLP, an English parser, and GiNZA, a Japanese parser.

# Motive

Replacing the morphological and parsing engines would require rewriting all source code related to text parsing.

# Proposal

There should be a framework for natural language processing that does not depend on the specific code of parsers and morphological analyzers, nor should it depend on runtimes such as Python or Java.


# Features

nlp4j-core provides

- Data imporer for NLP (Example: CSV, JSON
- Annotator Framework for NLP
- Importer Framework for Text Index Services (Example: Apache Solr, Azure Search)


# MAVEN Repository

	https://mvnrepository.com/search?q=org.nlp4j

# Build for Release

	mvn -DperformRelease=true clean deploy

	mvn -e -X -DperformRelease=true clean deploy
	
	usage: mvn [options] [<goal(s)>] [<phase(s)>]
	Options:
	-D,--define <arg> Define a system property
	-e,--errors Produce execution error messages
	-X,--debug  Produce execution debug output
	
# Maven Commands

	mvn test
	mvn javadoc:javadoc

# Maven Build Tips

package without test

	mvn package -Dmaven.test.skip


	mvn dependency:copy-dependencies
		output dir is: ./target/dependency


	mvn assembly:assembly -DdescriptorId=bin


	mvn dependency:copy-dependencies -DoutputDirectory=lib
	

	mvn dependency:tree
	[INFO] --- maven-dependency-plugin:2.8:tree (default-cli) @ nlp4j-core ---
	[WARNING] The artifact xml-apis:xml-apis:jar:2.0.2 has been relocated to xml-apis:xml-apis:jar:1.0.b2
	[INFO] org.nlp4j:nlp4j-core:jar:1.3.7.0
	[INFO] +- com.squareup.okhttp3:okhttp:jar:3.14.9:compile
	[INFO] +- com.squareup.okio:okio:jar:2.2.2:compile
	[INFO] |  \- org.jetbrains.kotlin:kotlin-stdlib:jar:1.2.60:runtime
	[INFO] |     +- org.jetbrains.kotlin:kotlin-stdlib-common:jar:1.2.60:runtime
	[INFO] |     \- org.jetbrains:annotations:jar:13.0:runtime
	[INFO] +- org.apache.logging.log4j:log4j-core:jar:2.17.2:compile
	[INFO] +- org.apache.logging.log4j:log4j-api:jar:2.17.2:compile
	[INFO] +- commons-io:commons-io:jar:2.11.0:compile
	[INFO] +- org.apache.commons:commons-text:jar:1.9:compile
	[INFO] +- org.apache.commons:commons-lang3:jar:3.12.0:compile
	[INFO] +- org.apache.commons:commons-collections4:jar:4.4:compile
	[INFO] +- org.apache.commons:commons-csv:jar:1.9.0:compile
	[INFO] +- com.google.code.gson:gson:jar:2.9.0:compile
	[INFO] \- junit:junit:jar:3.8.2:test

# Maven Central Repository Console

[https://oss.sonatype.org/](https://oss.sonatype.org/)		


# Maven Central Repository への公開方法 （公式Early Access版）

[https://central.sonatype.org/publish-ea/publish-ea-guide/](https://central.sonatype.org/publish-ea/publish-ea-guide/)


