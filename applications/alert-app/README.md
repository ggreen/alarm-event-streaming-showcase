
```json
{
  "id": "01",
  "level": "Critical",
  "time": "02:22 PM",
  "dateTime": "2025-11-30T23:54:17.285Z",
  "event": "Testing"
}
```

```shell
curl -X 'POST' \
  'http://localhost:8080/alert' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "id": "01",
  "level": "Critical",
  "time": "02:22 PM",
  "event": "Testing"
}'
```