# Use the official Solr 9 image as the base image
FROM solr:9 AS solr

# Switch to root to modify /opt/solr and install packages
USER root

# Set the default value for SOLR_ENDPOINT
ENV SOLR_ENDPOINT=http://localhost:8983/solr/
# http://host.docker.internal:8888
ENV EMBEDDING_ENDPOINT=http://localhost:8888
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

# Install necessary packages
RUN apt-get update && apt-get install -y wget git python3.10 python3-pip

# Set Python 3.10 as the default python3
RUN update-alternatives --install /usr/bin/python3 python3 /usr/bin/python3.10 1

# Create the TRANSFORMERS_CACHE directory and set permissions
RUN mkdir -p /opt/transformers_cache && chown solr:solr /opt/transformers_cache

# Set the TRANSFORMERS_CACHE to a writable directory
ENV TRANSFORMERS_CACHE=/opt/transformers_cache

# Install Tomcat manually by downloading the official Tomcat distribution
RUN wget https://dlcdn.apache.org/tomcat/tomcat-9/v9.0.95/bin/apache-tomcat-9.0.95.tar.gz -O /tmp/tomcat.tar.gz && \
    mkdir /opt/tomcat && \
    tar xzf /tmp/tomcat.tar.gz -C /opt/tomcat --strip-components=1 && \
    rm /tmp/tomcat.tar.gz

# Add execute permissions to catalina.sh and change ownership of /opt/tomcat to solr user
RUN chmod +x /opt/tomcat/bin/catalina.sh && \
    chown -R solr:solr /opt/tomcat

# Clone the repository and install the requirements
RUN git clone https://github.com/oyahiroki/nlp4j-llm-embeddings-e5.git /opt/nlp4j-llm-embeddings-e5 && \
    cd /opt/nlp4j-llm-embeddings-e5 && \
    pip3 install -r requirements.txt

# Download the .war file and place it in the Tomcat webapps directory
RUN wget "https://github.com/oyahiroki/nlp4j/raw/master/nlp4j/nlp4j-web-vectorsearch/dist/nlp4j-web-vectorsearch.war?3$(date +%Y%m%d%H%M%S)" -O /opt/tomcat/webapps/nlp4j-web-vectorsearch.war

# Expose ports for Solr, Tomcat, and the embedding server
EXPOSE 8983 8080 8888

# Switch back to solr user
USER solr

# Start Solr, initialize it, and start Tomcat and the embedding server
CMD ["sh", "-c", "/opt/solr/bin/solr start & sleep 5 && /opt/solr/init_solr.sh && python3 /opt/nlp4j-llm-embeddings-e5/nlp4j-embedding-server-e5.py & /opt/tomcat/bin/catalina.sh run"]
