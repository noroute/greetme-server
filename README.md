# OpenShift Demo-Applikationen #

## Referenzen ##

### OpenShift logging ###

 * ElasticSearch, FluentD, Kibana, ähnlich ELK
 * enthält Auth-Proxy für Log-Trennung nach Namespaces 
 * https://docs.openshift.com/enterprise/3.1/install_config/aggregate_logging.html

### OpenShift metrics ###

 * Default für Cluster metrics (nur usage (CPU, Mem, Network)) ist Hawkular
   (https://docs.openshift.org/latest/install_config/cluster_metrics.html)
 * es existiert eine Erweiterung
   [Hawkular APM](http://www.hawkular.org/hawkular-apm/), die Performace metrics
   (REST, OpenTracing) via JavaAgent liefern kann
 * Beispielhafte Integration von Prometheus wird
   [hier](http://www.openpersuasion.org/prometheus-monitoring-in-openshift/)
   beschrieben.
