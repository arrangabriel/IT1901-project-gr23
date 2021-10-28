# Schema

The following document explains the schema for the REST API that is implemented by the server. All clients should conform to this schema.

[[_TOC_]]

## Get LogEntry from an EntryManager

Type: <span style="color:#28b463">GET</span>

Endpoint: `/api/v1/entries/{entryId}`

Response: 

```json
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

## Get a list of LogEntries from an EntryManager

Type: <span style="color:#28b463">GET</span>

Endpoint: `/api/v1/entries/all`

Response:

```json
{
    "0": {
        "title": "Example Title"
    },

    "1": {
        "title": "Second Example Title"
    }
}
```

## Add a LogEntry

Type: <span style="color:#2e86c1">POST</span>

Endpoint: `/api/v1/entries/add`

Payload:

```json
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

Response:

```json
{
    "id": "0"
}
```

## Edit a LogEntry

Type: <span style="color:#2e86c1">POST</span>

Endpoint: `/api/v1/entries/edit/{entryId}`

Payload:

```json
{
    "title": "Example title 2",
    "comment": "Example comment 2",
    "date": "2021-10-25",
    "feeling": "6",
    "duration": "3600",
    "distance": "3",
    "maxHeartRate": "150",
    "exerciseCategory": "STRENGTH",
    "exerciseSubCategory": "PULL"

}
```



