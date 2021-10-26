# Schema

The following document explains the schema for the REST API that is implemented by the server. All clients should conform to this schema.

[TOC]

## Get LogEntry from an EntryManager

Type: `GET`

Endpoint: `/api/{entryManagerId}/{entryId}`

Response: 

```JSON
{
    "title": "Example title",
    "comment": "Example comment",
    "date": "2021-10-25",
    "feeling": "7",
    "duration": "3600",
    "distance": "3",
    "maxHeartRate": "150",
    "exerciseCategory": "STRENGTH",
    "exerciseSubCategory": "PULL"
}
```

## Get a list of EntryManagers

Type: `GET`

Endpoint: `/api/entrymanagers/list`

Response:

```JSON
{

}
```

## Get a list of LogEntries from an EntryManager

Type: `GET`

Endpoint: `/api/{entryManagerId}/entrylogs`

Response:

```JSON
{

}
```

## Add a EntryManager

Type: `POST`

Endpoint: `/api/entrymanagers/add`

Response:

```JSON
{
    "id": 0
}
```

## Add a LogEntry

Type: `POST`

Endpoint: `/api/{entryManagerId}/add`

Response:

```JSON
{
    "id": 0
}
```