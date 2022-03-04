# Introduction 

NLP4J Core Component

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
	
# Maven Commands

	mvn test
	mvn javadoc:javadoc

# Maven Build Tips

	mvn dependency:copy-dependencies
		output dir is: ./target/dependency

	mvn assembly:assembly -DdescriptorId=bin

