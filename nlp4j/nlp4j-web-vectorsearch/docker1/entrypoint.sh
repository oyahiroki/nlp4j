#!/bin/bash

# Check if NVIDIA GPU is available
if command -v nvidia-smi &> /dev/null
then
    echo "NVIDIA GPU detected."
    # GPUがある場合の処理
    python3 /opt/nlp4j-llm-embeddings-e5/nlp4j-embedding-server-e5.py --use-gpu &
else
    echo "NVIDIA GPU not detected."
    # GPUがない場合の処理
    python3 /opt/nlp4j-llm-embeddings-e5/nlp4j-embedding-server-e5.py &
fi

# Start Solr and Tomcat
/opt/solr/bin/solr start
sleep 5
/opt/solr/init_solr.sh
/opt/tomcat/bin/catalina.sh run
