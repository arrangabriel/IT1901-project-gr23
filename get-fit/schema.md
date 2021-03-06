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

Endpoint: `/api/v1/entries/list`

Arguments:
- s: Sorting
    - date: Sort by date `s=date`
    - duration: Sort by duration `s=duration`
    - title: Sort by title `s=title`
    - reverse: Reverse sorting `r=false`
- f: Filtering
    - category: Filter by category `c={category}`
    - subcategory: Filter by subcategory `sc={subcategory}`
    - date: Filter by date `d={start yyyy-mm-dd}-{end yyyy-mm-dd}`

Response:

```json
{
    "entries": [
        {
            "id": "0",
            "title": "Example title",
            "comment": "Example comment",
            "date": "1970-01-01",
            "feeling": "1",
            "duration": "60",
            "distance": "1",
            "maxHeartRate": "40",
            "exerciseCategory": "strength",
            "exerciseSubCategory": "push"
        },
        {
            "id": "1",
            "title": "Example title",
            "comment": "Example comment",
            "date": "2038-01-19",
            "feeling": "10",
            "duration": "3600",
            "distance": "100",
            "maxHeartRate": "220",
            "exerciseCategory": "running",
            "exerciseSubCategory": "long"
        },
        {
            "id": "2",
            "title": "Example title",
            "comment": "Example comment",
            "date": "2019-12-31",
            "feeling": "5",
            "duration": "1800",
            "distance": "null",
            "maxHeartRate": "null",
            "exerciseCategory": "swimming",
            "exerciseSubCategory": "null"
        }
    ]
    
}
```
## Get a list of Filters

Type: <span style="color:#28b463">GET</span>

Endpoint: `/api/v1/entries/filters`

Response:

```json
{
    "categories": {
        "strength": [
            "push",
            "pull"
        ],
        "running": [
            "short",
            "highintensity"
        ],
        "cycling": [
            "short",
            "highintensity"
        ]
    }
}
```

## Get Statistics data

Type: <span style="color:#28b463">GET</span>

Endpoint: `/api/v1/entries/stats`

Arguments:
- date: Filter by date `d={start yyyy-mm-dd}-{end yyyy-mm-dd}`
- category: Filter by category `c={category}`

Response:

```json
{
    "empty": "False",
    "count": "5",
    "totalDuration": "2.6",
    "averageDuration": "0.5",
    "averageSpeed": "7.0",
    "averageFeeling": "3.3",
    "maximumHr": "180"
}
```

## Get Chart Data

Type: <span style="color:#28b463">GET</span>

Endpoint: `/api/v1/entries/chart`

Arguments:
- date: Filter by date `d={start yyyy-mm-dd}-{end yyyy-mm-dd}`

Response:

```json
{
    "swimming": "2",
    "running": "3",
    "strength": "0",
    "cycling": "2"
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

## Remove LogEntry

Type: <span style="color:#2e86c1">POST</span>

Endpoint: `/api/v1/entries/remove/{entryId}`

Payload:

```json
{
    "id": "0"
}
```



