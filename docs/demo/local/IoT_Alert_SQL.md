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

Start Alarm app for all imani alerts

```shell
java -jar applications/alert-app/target/alert-app-0.0.1-SNAPSHOT.jar --spring.rabbitmq.host=localhost --spring.rabbitmq.username=guest --spring.rabbitmq.password=guest --spring.cloud.stream.bindings.input.destination="amq.topic" --stream.destination="alerts.alert" --stream.exchange.bind.key="#"   --stream.filter.sql="account = 'imani' AND level IN ('critical', 'high','medium','low')" --server.port=8080 --stream.activity.filter.name="account" --stream.activity.filter.value="imani" --alert.refresh.rateSeconds=1
```


CRITICAL ONLY imani

```shell
java -jar applications/alert-app/target/alert-app-0.0.1-SNAPSHOT.jar --spring.rabbitmq.host=localhost --spring.application.name="imani-critical" --spring.rabbitmq.username=guest --spring.rabbitmq.password=guest --spring.cloud.stream.bindings.input.destination="amq.topic" --stream.destination="alerts.alert" --stream.exchange.bind.key="#"   --stream.filter.sql="account = 'imani' AND level IN ('critical')" --server.port=8911 --stream.activity.filter.name="account" --stream.activity.filter.value="imani"
```


----------------

# Post Alerts

Post critical alert

```shell
curl -X 'POST' \
  'http://localhost:8383/publisher?topic=alerts.alert' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -H "account: imani" \
  -H "level: critical" \
  -d '{"id" : "01", "account" : "imani", "level" : "critical", "time" : "7:00AM", "event" : "Break-in in progress" }'
```

Post High

```shell
curl -X 'POST' \
  'http://localhost:8383/publisher?topic=alerts' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -H "account: imani" \
  -H "level: high" \
  -d '{"id" : "03", "account" : "imani", "level" : "high", "time" : "7:00AM", "event" : "BUS Left!!" }'
```

Post Critical

```shell
curl -X 'POST' \
  'http://localhost:8383/publisher?topic=alerts' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -H "account: imani" \
  -H "level: critical" \
  -d '{"id" : "02", "account" : "imani", "level" : "critical", "time" : "7:00AM", "event" : "BUS COMING!!" }'
  
```


Open Imani for All alerts

```shell
open http:///localhost:8080
```


Only Critical

```shell
open http:///localhost:8911
```

Restart Apps


-----------------------------

Generator Activities

```shell
java -jar applications/generator-supplier-source/target/generator-supplier-source-0.0.1-SNAPSHOT.jar --spring.cloud.stream.bindings.input.producer.poller.fixed-delay=1 --spring.cloud.stream.poller.fixedDelay=1 --spring.cloud.stream.poller.max-messages=1000000
```


No new alerts

Start AMQP 1.0 Source

```shell
java -jar applications/http-source/target/http-source-0.0.1-SNAPSHOT.jar \
  -spring.rabbitmq.host=localhost \
  --spring.application.name=activities-source \
  --spring.rabbitmq.username=guest \
  --spring.rabbitmq.password=guest --spring.profiles.active="amqp1.0" --source.amqp.filter.property.name="account" --server.port=8555 --spring.cloud.stream.bindings.output.destination="activities.activity"
```


```json
{
  "id" :  "1", 
  "icon" : "fa-door-open",
  "time" : "06:30 PM", 
  "activity" : "Garage Door Opened"
}
```

```shell
curl -X 'POST' \
  'http://localhost:8555/publisher?topic=imani' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "id" :  "1", 
  "icon" : "fa-door-open",
  "time" : "06:30 PM", 
  "activity" : "Garage Door Opened"
}'
```
```shell
curl -X 'POST' \
  'http://localhost:8555/publisher?topic=imani' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "id" :  "2", 
  "icon" : "fa-door-closed",
  "time" : "06:31 PM", 
  "activity" : "Garage Door Closed"
}'
```

```shell
curl -X 'POST' \
  'http://localhost:8555/publisher?topic=imani' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "id" :  "3", 
  "icon" : "fa-door-open",
  "time" : "06:33 PM", 
  "activity" : "Garage Door Opened"
}'
```

```shell
curl -X 'POST' \
  'http://localhost:8555/publisher?topic=imani' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "id" :  "4", 
  "icon" : "fa-door-closed",
  "time" : "06:31 PM", 
  "activity" : "Garage Door Closed"
}'
```