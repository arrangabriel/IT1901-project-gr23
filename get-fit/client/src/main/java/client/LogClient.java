package client;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Representation of a connection to a get-fit server.
 */
public class LogClient {

    /**
     * URL to remote server.
     */
    private final String url;

    /**
     * Port to the remote server.
     */
    private final int port;

    /**
     * Constructs a LogClient from a builder.
     *
     * @param serverUrl server base url.
     * @param serverPort server port.
     */
    public LogClient(final String serverUrl, final int serverPort) {
        this.url = serverUrl;
        this.port = serverPort;
    }

    /**
     * Gets a LogEntry from the server.
     *
     * @param id The id of the LogEntry to retrieve.
     * @return The LogEntry represented by a hash map.
     * @throws URISyntaxException If the id ruins the URI syntax.
     * @throws InterruptedException
     * If the request was interrupted before retreiving the http response.
     * @throws ExecutionException If the request completed exceptionally.
     */
    public HashMap<String, String> getLogEntry(final String id)
            throws URISyntaxException, InterruptedException,
            ExecutionException {

        HttpResponse<String> response = this.get("/api/v1/entries/" + id);
        String jsonString = response.body();

        JSONObject jsonObject = new JSONObject(jsonString);

        HashMap<String, String> responseHash = new HashMap<>();

        for (String key : jsonObject.keySet()) {
            responseHash.put(key, jsonObject.getString(key));
        }

        return responseHash;
    }

    /**
     * Asynchronously requests details for every id.
     *
     * @param ids the ids to request details for.
     * @return The hash map of hash maps representing the LogEntries
     * @throws URISyntaxException If any of the ids ruin the URI syntax.
     * @throws InterruptedException
     * If the requests were interrupted before retreiving the http responses.
     * @throws ExecutionException If the requests were completed exceptionally.
     */
    public HashMap<String, HashMap<String, String>> getLogEntryDetailedList(
            final String... ids)
            throws URISyntaxException, InterruptedException,
            ExecutionException {

        HashMap<String, CompletableFuture<HttpResponse<String>>> futures =
                new HashMap<>();

        for (String id : ids) {
            futures.put(id, this.getAsync("/api/v1/entries/" + id));
        }

        HashMap<String, HashMap<String, String>> responses = new HashMap<>();

        for (Entry<String, CompletableFuture<HttpResponse<String>>> futureEntry
                : futures.entrySet()) {

            JSONObject jsonObject =
                    new JSONObject(futureEntry.getValue().get().body());
            HashMap<String, String> entryHash = new HashMap<>();

            for (String key : jsonObject.keySet()) {
                entryHash.put(key, jsonObject.getString(key));
            }

            responses.put(futureEntry.getKey(), entryHash);
        }

        return responses;
    }

    /**
     * Get a list of log entries from the server without sorting or filtering.
     *
     * @return A list of log entries from the server represented as a hash map.
     * @throws URISyntaxException
     * If the hardcoded URI no longer matches the servers expectations.
     * @throws InterruptedException
     * If the request was interrupted before retreiving the http response.
     * @throws ExecutionException If the request completed exceptionally.
     */
    public List<HashMap<String, String>> getLogEntryList()
            throws URISyntaxException, InterruptedException,
            ExecutionException {
        return this.getLogEntryList(new ListBuilder());
    }

    /**
     * Get a list of log entries from the server.
     *
     * @param builder
     * The query string builder to generate a query string
     * for filtering and sorting.
     * @return A list of log entries from the server represented as a hash map.
     * @throws URISyntaxException
     * If the query entries ruin the query string syntax.
     * @throws InterruptedException
     * If the request was interrupted before retreiving the http response.
     * @throws ExecutionException If the request completed exceptionally.
     */
    public List<HashMap<String, String>> getLogEntryList(
            final ListBuilder builder)
            throws URISyntaxException, InterruptedException,
            ExecutionException {

        String queryString = "?";

        List<String> queries = new ArrayList<>();

        if (builder.reverseVal != null) {
            queries.add("r=" + builder.reverseVal);
        }
        if (builder.sortVal != null) {
            queries.add("s=" + builder.sortVal);
        }
        if (builder.categoryVal != null) {
            queries.add("c=" + builder.categoryVal);
        }
        if (builder.subCategoryVal != null) {
            queries.add("sc=" + builder.subCategoryVal);
        }
        if (builder.dateVal != null) {
            queries.add("d=" + builder.dateVal);
        }

        queryString += String.join("&", queries);

        HttpResponse<String> response =
                this.get("/api/v1/entries/list" + queryString);
        String jsonString = response.body();

        JSONObject jsonObject = new JSONObject(jsonString);

        List<HashMap<String, String>> responseList =
                new ArrayList<>();

        JSONArray array = jsonObject.getJSONArray("entries");
        for (int i = 0; i < array.length(); i++) {
            HashMap<String, String> innerMap = new HashMap<>();

            innerMap.put("id", array.getJSONObject(i).getString("id"));
            innerMap.put("name", array.getJSONObject(i).getString("name"));

            responseList.add(innerMap);
        }

        return responseList;

    }

    /**
     * Adds a log entry on the server.
     *
     * @param entry HashMap representing the log entry to add.
     * @return The id of the added log entry.
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws URISyntaxException
     */
    public String addLogEntry(final HashMap<String, String> entry)
            throws URISyntaxException, InterruptedException,
            ExecutionException {
        JSONObject payload = new JSONObject(entry);

        HttpResponse<String> response =
                this.post("/api/v1/entries/add", payload.toString());

        return new JSONObject(response.body()).getString("id");
    }

    /**
     * Updates a log entry on the server.
     *
     * @param id    The id of the log entry to update.
     * @param entry HashMap representing the log entry to update.
     * @return The id of the updated log entry.
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws URISyntaxException
     */
    public String updateLogEntry(
            final String id,
            final HashMap<String, String> entry)
            throws URISyntaxException, InterruptedException,
            ExecutionException {
        JSONObject payload = new JSONObject(entry);

        HttpResponse<String> response =
                this.post("/api/v1/entries/edit/" + id, payload.toString());

        return new JSONObject(response.body()).getString("id");
    }

    /**
     * Retreives a list of exercise categories from the server.
     *
     * @return A list of exercise categories from the server
     * represented as a hash map.
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws URISyntaxException
     */
    public HashMap<String, List<String>> getExerciseCategories()
            throws URISyntaxException, InterruptedException,
            ExecutionException {
        HttpResponse<String> response = this.get("/api/v1/entries/filters");

        JSONObject jsonObject = new JSONObject(response.body());
        HashMap<String, List<String>> categories =
                new HashMap<>();

        for (String category : jsonObject.keySet()) {
            JSONArray array = jsonObject.getJSONArray(category);
            List<String> subCategories = new ArrayList<>();

            for (int i = 0; i < array.length(); i++) {
                subCategories.add(array.getString(i));
            }
            categories.put(category, subCategories);
        }

        return categories;
    }

    /**
     * Deletes a log entry on the server.
     *
     * @param id The id of the log entry to delete.
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws URISyntaxException
     */
    public void deleteLogEntry(final String id)
            throws URISyntaxException, InterruptedException,
            ExecutionException {
        this.post("/api/v1/entries/remove/" + id, "");
    }

    /**
     * Elementary asynchronous get request.
     *
     * @param endpoint Where to send the request to.
     * @return The Http response promise.
     * @throws URISyntaxException If the URI syntax is incorrect.
     */
    private CompletableFuture<HttpResponse<String>> getAsync(
            final String endpoint)
            throws URISyntaxException {

        HttpClient client = HttpClient.newBuilder()
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(new URI(this.url + ":" + this.port + endpoint))
                .build();

        return client.sendAsync(request, BodyHandlers.ofString());

    }

    /**
     * Elementary synchronous get request.
     *
     * @param endpoint Where to send the request to.
     * @return The Http response.
     * @throws URISyntaxException If the URI syntax is incorrect.
     * @throws InterruptedException
     * If the underlying asynchronous request was interrupted before retreival.
     * @throws ExecutionException
     * If the underlying asynchronous request completed exceptionally.
     */
    private HttpResponse<String> get(final String endpoint)
            throws URISyntaxException, InterruptedException,
            ExecutionException {

        return this.getAsync(endpoint).get();
    }

    /**
     * Elementary asynchronous post request.
     *
     * @param endpoint Where to send the requests to.
     * @param payload  Where to send to the server.
     * @return The Http response promise.
     * @throws URISyntaxException If the URI syntax is incorrect.
     */
    private CompletableFuture<HttpResponse<String>> postAsync(
            final String endpoint,
            final String payload)
            throws URISyntaxException {

        HttpClient client = HttpClient.newBuilder()
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .uri(
                        new URI(this.url + ":" + this.port + endpoint)
                )
                .build();

        return client.sendAsync(request, BodyHandlers.ofString());
    }

    /***
     * Elementary synchronous post request.
     * @param endpoint Where to send the request to.
     * @param payload What to send to the server.
     * @return The Http response.
     * @throws URISyntaxException If the URI syntax is incorrect.
     * @throws InterruptedException
     * If the underlying asynchronous request was interrupted before retreival.
     * @throws ExecutionException
     * If the underlying asynchronous request completed exceptionally.
     */
    private HttpResponse<String> post(
            final String endpoint,
            final String payload)
            throws URISyntaxException, InterruptedException,
            ExecutionException {

        return this.postAsync(endpoint, payload).get();
    }

    /**
     * Builder for filtering and sorting the list of log entries.
     *
     * @see #getLogEntryList
     */
    public static class ListBuilder {

        /**
         * Wether the sorting should be reversed.
         */
        private Boolean reverseVal = false;

        /**
         * How the list should be sorted.
         */
        private String sortVal = null;

        /**
         * What category should be filtered for.
         */
        private String categoryVal = null;

        /**
         * What subcategory should be filtered for.
         */
        private String subCategoryVal = null;

        /**
         * What date interval should be included.
         */
        private String dateVal = null;

        /**
         * Set reverse.
         *
         * @return a ListBuilder instance.
         */
        public ListBuilder reverse() {
            this.reverseVal = !this.reverseVal;
            return this;
        }

        /**
         * Set sorting.
         *
         * @param sort sorting configuration.
         * @return a ListBuilder instance.
         */
        public ListBuilder sort(final String sort) {
            this.sortVal = sort;
            return this;
        }

        /**
         * Set category filtering.
         *
         * @param category category to filter by.
         * @return a ListBuilder instance.
         */
        public ListBuilder category(final String category) {
            this.categoryVal = category;
            return this;
        }

        /**
         * Set subCategory filtering.
         *
         * @param subCategory sub category to filter by.
         * @return a ListBuilder instance.
         */
        public ListBuilder subCategory(final String subCategory) {
            this.subCategoryVal = subCategory;
            return this;
        }

        /**
         * Set date filtering.
         *
         * @param date date interval to filter by
         *             (format: yyyy-mm-dd-yyyy-mm-dd).
         * @return a ListBuilder instance.
         */
        public ListBuilder date(final String date) {
            this.dateVal = date;
            return this;
        }
    }
}
