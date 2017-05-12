# About

_GreetMe Server_ is a trivial service that provides personalized greeting messages in different languages to clients. (A
corresponding client is the web frontend _GreetMe Web_.)
 
It is merely a simple demo application that was used for winning insights of the build, deployment, and management possibilities
provided by PaaS solutions.

# Example requests

## Create a "Hello, Bob!" greeting for a person named Bob in English
Request:
```
curl -X POST -H 'Content-Type: application/json' -d '{"name": "Bob", "nativeLanguageCode": "en"}' localhost:8080/greetings
```
Response:
```
{
  "id": 1,
  "message": "Hello, Bob!"
}
```

## Get all greeting messages that have been created
Request:
```
curl localhost:8080/greetings
```
Response, assuming a french greeting for Alice has been created additionally to that for Bob:
```
[
  {
    "id": 1,
    "message": "Hello, Bob!"
  },
  {
    "id": 2,
    "message": "Bonjour, Alice!"
  }
]

```

## Get a single, already created greeting message
Request:
```
curl localhost:8080/greetings/2
```
Response:
```
{
  "id": 2,
  "message": "Bonjour, Alice!"
}
```

# Characteristics
_GreetMe Server_ contains:
* different types of automated tests: unit, component, integration, and consumer-driven contract tests
* externalized configuration
* a connection to an external service for all non-English translations of "hello"
* password secured metrics (e.g. for monitoring)
* logging
* error handling

# Missing characteristics
_GreetMe Server_ does not contain:
* persistent state / attachment of storage