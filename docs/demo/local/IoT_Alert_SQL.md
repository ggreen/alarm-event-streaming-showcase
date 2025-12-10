# IoT Alert SQL Demo

Start Rabbit

```shell
./deployment/local/containers/rabbit.sh
```

Start MQTT 5 Source

```shell
java -jar applications/http-source/target/http-source-0.0.1-SNAPSHOT.jar \
  --mqtt.connectionUrl=tcp://localhost:1883 \
  --spring.application.name=http-mqtt-source \
  --mqtt.userName=guest \
  --mqtt.userPassword=guest --spring.profiles.active=mqtt
```

Start Alarm app for imani

```shell
java -jar applications/alert-app/target/alert-app-0.0.1-SNAPSHOT.jar --spring.rabbitmq.host=localhost --spring.rabbitmq.username=guest --spring.rabbitmq.password=guest --spring.cloud.stream.bindings.input.destination="amq.topic" --stream.destination="alerts.alert" --stream.exchange.bind.key="#"   --stream.filter.sql="account = 'imani' AND level IN ('critical', 'high','medium','low')" --server.port=8080
```


CRITICAL ONLY imani

```shell
java -jar applications/alert-app/target/alert-app-0.0.1-SNAPSHOT.jar --spring.rabbitmq.host=localhost --spring.application.name="imani-critical" --spring.rabbitmq.username=guest --spring.rabbitmq.password=guest --spring.cloud.stream.bindings.input.destination="amq.topic" --stream.destination="alerts.alert" --stream.exchange.bind.key="#"   --stream.filter.sql="account = 'imani' AND level IN ('critical')" --server.port=8911
```


----------------

# Post Alerts



```shell
curl -X 'POST' \
  'http://localhost:8383/publisher?topic=alerts.alert' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -H "account: imani" \
  -H "level: critical" \
  -d '{"id" : "01", "account" : "imani", "level" : "critical", "time" : "7:00AM", "event" : "Break-in in progress" }'
```

```shell
curl -X 'POST' \
  'http://localhost:8383/publisher?topic=alerts' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -H "account: imani" \
  -H "level: high" \
  -d '{"id" : "03", "account" : "imani", "level" : "high", "time" : "7:00AM", "event" : "BUS Left!!" }'
```


```shell
curl -X 'POST' \
  'http://localhost:8383/publisher?topic=alerts' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -H "account: imani" \
  -H "level: critical" \
  -d '{"id" : "02", "account" : "imani", "level" : "critical", "time" : "7:00AM", "event" : "BUS COMING!!" }'
  
```


Open All

```shell
open http:///localhost:8080
```


Only Critical

```shell
open http:///localhost:8911
```

Restart Apps

Generator Activities

```shell
java -jar applications/generator-supplier-source/target/generator-supplier-source-0.0.1-SNAPSHOT.jar --spring.cloud.stream.bindings.input.producer.poller.fixed-delay=1 --spring.cloud.stream.poller.fixedDelay=1 --spring.cloud.stream.poller.max-messages=1000000
```