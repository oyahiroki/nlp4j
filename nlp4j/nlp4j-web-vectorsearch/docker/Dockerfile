# How to RUN
# docker build -t my-nlp4j-web-vectorsearch .

# Use the official Solr 9 image as the base image
FROM solr:9 AS solr

# Switch to root to modify /opt/solr and install packages
USER root

# Set the default value for SOLR_ENDPOINT
ENV SOLR_ENDPOINT=http://localhost:8983/solr/
# http://host.docker.internal:8888
ENV EMBEDDING_ENDPOINT=http://host.docker.internal:8888
# Field name for vector
ENV EMBEDDING_FIELD=vector

# Create a script to initialize Solr core and schema
RUN echo '#!/bin/bash\n\
    /opt/solr/bin/solr create -c sandbox\n\
    sleep 10\n\
    curl -X POST -H "Content-type:application/json" --data-binary "\
    {\"add-field-type\":{\"name\":\"vector\",\"class\":\"solr.DenseVectorField\",\"vectorDimension\":1024,\"similarityFunction\":\"cosine\"}}" http://localhost:8983/solr/sandbox/schema\n\
    curl -X POST -H "Content-type:application/json" --data-binary "{ \"add-field\":{ \"name\":\"vector\", \"type\":\"vector\", \"indexed\":true, \"stored\":true } }" http://localhost:8983/solr/sandbox/schema\n'\
    > /opt/solr/init_solr.sh

# Make the script executable
RUN chmod +x /opt/solr/init_solr.sh

# Install wget
RUN apt-get update && apt-get install -y wget

# Install Tomcat manually by downloading the official Tomcat distribution
# 2024-12/23 change 9.0.95 -> 9.0.98
RUN wget https://dlcdn.apache.org/tomcat/tomcat-9/v9.0.98/bin/apache-tomcat-9.0.98.tar.gz -O /tmp/tomcat.tar.gz && \
    mkdir /opt/tomcat && \
    tar xzf /tmp/tomcat.tar.gz -C /opt/tomcat --strip-components=1 && \
    rm /tmp/tomcat.tar.gz

# Add execute permissions to catalina.sh and change ownership of /opt/tomcat to solr user
RUN chmod +x /opt/tomcat/bin/catalina.sh && \
    chown -R solr:solr /opt/tomcat

# Download the .war file and place it in the Tomcat webapps directory
# RUN wget --header="Cache-Control: no-cache" "https://github.com/oyahiroki/nlp4j/raw/master/nlp4j/nlp4j-web-vectorsearch/dist/nlp4j-web-vectorsearch.war?2$(date +%Y%m%d%H%M%S)" -O /opt/tomcat/webapps/nlp4j-web-vectorsearch.war
RUN wget "https://github.com/oyahiroki/nlp4j/raw/master/nlp4j/nlp4j-web-vectorsearch/dist/nlp4j-web-vectorsearch.war?3$(date +%Y%m%d%H%M%S)" -O /opt/tomcat/webapps/nlp4j-web-vectorsearch.war

# Expose ports for Solr and Tomcat
EXPOSE 8983 8080

# Switch back to solr user
USER solr

# Start Solr, initialize it, and start Tomcat
CMD ["sh", "-c", "/opt/solr/bin/solr start & sleep 5 && /opt/solr/init_solr.sh && /opt/tomcat/bin/catalina.sh run"]



# HOW-TO-USE
# 1. BUILD
# > docker build -t img_vectorsearch_yyyyMMdd .
# 2. RUN (1/2)
# > docker run -d --name my_solr_tomcat_container -p 8983:8983 -p 8080:8080 img_vectorsearch_yyyyMMdd
# 2. RUN (2/2)
# $ python3  nlp4j-embedding-server-e5.py
# 3. OPEN
# http://localhost:8080/nlp4j-web-vectorsearch/
