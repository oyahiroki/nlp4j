
# NLP4J Solr

# Solr Query Examples

## Facet Counting

https://solr.apache.org/guide/6_6/faceting.html

facet=true
facet.field=field_name
facet.mincount=1

http://localhost:8983/solr/sandbox/select?facet.field=word_ss&facet=true&q=*%3A*&rows=0

http://localhost:8983/solr/sandbox/select?facet.field=word_ss&facet.mincount=1&facet=true&q=item%3A*&rows=1

