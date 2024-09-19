# Use the official Solr 9 image as the base image
FROM solr:9 AS solr

# Switch to root to modify /opt/solr and install packages
USER root

# Set the default value for SOLR_ENDPOINT
ENV SOLR_ENDPOINT=http://localhost:8983/solr/

# Create a script to initialize Solr core and schema
RUN echo '#!/bin/bash\n\
    /opt/solr/bin/solr create -c sandbox\n\
    sleep 10\n\
    curl -X POST -H "Content-type:application/json" --data-binary "\
    {\"add-field-type\":{\"name\":\"vector1024\",\"class\":\"solr.DenseVectorField\",\"vectorDimension\":1024,\"similarityFunction\":\"cosine\"}}" http://localhost:8983/solr/sandbox/schema\n\
    curl -X POST -H "Content-type:application/json" --data-binary "{ \"add-field\":{ \"name\":\"vector\", \"type\":\"vector1024\", \"indexed\":true, \"stored\":true } }" http://localhost:8983/solr/sandbox/schema\n'\
    > /opt/solr/init_solr.sh

# Make the script executable
RUN chmod +x /opt/solr/init_solr.sh

# Install wget
RUN apt-get update && apt-get install -y wget

# Install Tomcat manually by downloading the official Tomcat distribution
RUN wget https://dlcdn.apache.org/tomcat/tomcat-9/v9.0.95/bin/apache-tomcat-9.0.95.tar.gz -O /tmp/tomcat.tar.gz && \
    mkdir /opt/tomcat && \
    tar xzf /tmp/tomcat.tar.gz -C /opt/tomcat --strip-components=1 && \
    rm /tmp/tomcat.tar.gz

# Add execute permissions to catalina.sh and change ownership of /opt/tomcat to solr user
RUN chmod +x /opt/tomcat/bin/catalina.sh && \
    chown -R solr:solr /opt/tomcat

# Download the .war file and place it in the Tomcat webapps directory
RUN wget --header="Cache-Control: no-cache" https://github.com/oyahiroki/nlp4j/raw/master/nlp4j/nlp4j-web-vectorsearch/dist/nlp4j-web-vectorsearch.war?nocache2 -O /opt/tomcat/webapps/nlp4j-web-vectorsearch.war

# Expose ports for Solr and Tomcat
EXPOSE 8983 8080

# Switch back to solr user
USER solr

# Start Solr, initialize it, and start Tomcat
CMD ["sh", "-c", "/opt/solr/bin/solr start & sleep 5 && /opt/solr/init_solr.sh && /opt/tomcat/bin/catalina.sh run"]
