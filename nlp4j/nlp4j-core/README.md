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

# Maven Central

	https://central.sonatype.com/artifact/org.nlp4j/nlp4j-core?smo=true

# MAVEN Repository

	https://mvnrepository.com/search?q=org.nlp4j


