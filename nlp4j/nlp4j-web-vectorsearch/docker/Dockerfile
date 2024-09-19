# Use the official Solr 9 image as the base image for Solr
FROM solr:9 AS base

# Switch to root to modify /opt/solr and install packages
USER root

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

# Install Tomcat and download the .war file
RUN apt-get update && apt-get install -y wget tomcat9 && \
    wget https://github.com/oyahiroki/nlp4j/raw/master/nlp4j/nlp4j-web-vectorsearch/dist/nlp4j-web-vectorsearch.war -O /var/lib/tomcat9/webapps/nlp4j-web-vectorsearch.war

# Expose ports for Solr and Tomcat
EXPOSE 8983 8080

# Switch back to solr user
USER solr

# Start Solr, initialize it, and start Tomcat
CMD ["sh", "-c", "/opt/solr/bin/solr start & sleep 5 && /opt/solr/init_solr.sh && catalina.sh run"]
