# MP-DS
Master Project Distributed System

## Deploy the infrastructure

Kafka, Flink, Prometheus, and Grafana can be deployed on a Kubernetes cluster using [Helm](https://helm.sh). The charts are located in the ``helm-charts`` directory.

### Configuration

Configure which charts to deploy in the global values.yaml by setting ``enabled: true`` for each desired technology. Cluster sizes and ports for external access can also be specified here.

Each subchart can be deployed by itself and contains its own values.yaml file with futher configurations. If deployed from the umbrella chart, values in the global values.yaml will overwrite the values in the subchart's values.yaml.

### Using the helm charts
Deploy the charts with:
```
helm install [DEPLOYMENT NAME] [CHART DIRECTORY]
```

Uninstall the charts with:
```
helm uninstall [DEPLOYMENT NAME]
```
