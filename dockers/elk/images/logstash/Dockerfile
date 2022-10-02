FROM logstash:7.10.1

ADD pipeline/*.conf /usr/share/logstash/pipeline/
ADD config /usr/share/logstash/config/

RUN bin/logstash-plugin install logstash-filter-rest
