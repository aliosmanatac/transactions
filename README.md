# Transactions API

##Install & Run
```
mvn clean install
mvn spring-boot:run
```

##Example Requests
###1. transactions
```
curl -d '{"amount": 1000.912, "timestamp":1531966089907}' -H "Content-Type: application/json" -X POST http://localhost:8080/transactions -i
```

###2. statistics
```
curl -X GET http://localhost:8080/statistics -i
```

##Implementation Details
This service aims to provide "transactions" and "statistics" APIs with O(1) complexity. To achieve this, 
a scheduled thread runs every second once. It calculates and stores the statistics internally to provide 
constant time access to statitics API.

The caveat of this approach is that it does not guarantee 0-60 seconds constraint for the transactions filtering.
In other words, any transaction may not be included in statistics in the first 1 second after it is stored in the API, or may not be 
filtered out in 1 second after it has 60 seconds of age.





