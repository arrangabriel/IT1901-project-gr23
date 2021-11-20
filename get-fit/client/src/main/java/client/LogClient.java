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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Representation of a connection to a get-fit server.
 */
public class LogClient {

    /**
     * OK response value.
     */
    private static final int OK_CODE = 200;

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
     * @param serverUrl  server base url.
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
     * @throws URISyntaxException      If the id ruins the URI syntax.
     * @throws InterruptedException
     * If the request was interrupted before retrieving the http response.
     * @throws ExecutionException      If the request completed exceptionally.
     * @throws ServerResponseException
     * If there was an error with the server response.
     */
    public HashMap<String, String> getLogEntry(final String id)
            throws URISyntaxException, InterruptedException,
            ExecutionException, ServerResponseException {
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
     * Get a list of log entries from the server.
     *
     * @param builder The query string builder to generate a query string
     *                for filtering and sorting.
     * @return A list of log entries from the server represented as a hash map.
     * @throws URISyntaxException
     * If the query entries ruin the query string syntax.
     * @throws InterruptedException
     * If the request was interrupted before retrieving the http response.
     * @throws ExecutionException      If the request completed exceptionally.
     * @throws ServerResponseException
     * If there was an error with the server response.
     */
    public List<HashMap<String, String>> getLogEntryList(
            final SortArgWrapper builder)
            throws URISyntaxException, InterruptedException,
            ExecutionException, ServerResponseException {
        String queryString = "?";

        List<String> queries = new ArrayList<>();

        queries.add("r=" + builder.reverseVal);

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

        JSONObject jsonObject = new JSONObject(response.body());

        List<HashMap<String, String>> responseList =
                new ArrayList<>();

        JSONArray array = jsonObject.getJSONArray("entries");
        for (int i = 0; i < array.length(); i++) {
            HashMap<String, String> innerMap = new HashMap<>();

            for (String field : array.getJSONObject(i).keySet()) {
                innerMap.put(field, array.getJSONObject(i).getString(field));
            }

            responseList.add(innerMap);
        }

        return responseList;

    }

    /**
     * Gets a hashmap with statistics data from the server.
     *
     * @param builder The query string builder to generate a query string
     *                for filtering and sorting.
     * @return A hashmap with the data content.
     * @throws URISyntaxException      If the query entries ruin
     *                                 the query string syntax.
     * @throws InterruptedException    If the request was interrupted
     *                                 before retrieving the http response.
     * @throws ExecutionException      If the request completed exceptionally.
     * @throws ServerResponseException
     * If there was an error with the server response.
     */
    public HashMap<String, String> getStatistics(
            final SortArgWrapper builder)
            throws URISyntaxException, InterruptedException,
            ExecutionException, ServerResponseException {
        String queryString = "?";

        List<String> queries = new ArrayList<>();

        queries.add("d=" + builder.dateVal);

        if (builder.categoryVal != null) {
            queries.add("c=" + builder.categoryVal);
        }
        queryString += String.join("&", queries);

        HttpResponse<String> response =
                this.get("/api/v1/entries/stats" + queryString);

        return getResponseHashMap(response);
    }

    /**
     * Gets data to build a statistical chart.
     *
     * @param builder a listBuilder instance.
     * @return chart data.
     * @throws URISyntaxException      If the query entries ruin
     *                                 the query string syntax.
     * @throws InterruptedException    If the request was interrupted
     *                                 before retrieving the http response.
     * @throws ExecutionException      If the request completed exceptionally.
     * @throws ServerResponseException
     * If there was an error with the server response.
     */
    public HashMap<String, String> getChartData(
            final SortArgWrapper builder)
            throws URISyntaxException, InterruptedException,
            ExecutionException, ServerResponseException {

        String queryString = "?";

        List<String> queries = new ArrayList<>();

        queries.add("d=" + builder.dateVal);

        queryString += String.join("&", queries);

        HttpResponse<String> response =
                this.get("/api/v1/entries/chart" + queryString);

        return getResponseHashMap(response);
    }

    private HashMap<String, String> getResponseHashMap(
            final HttpResponse<String> response) {
        JSONObject jsonObject = new JSONObject(response.body());

        HashMap<String, String> responseHash = new HashMap<>();

        for (String key : jsonObject.keySet()) {
            responseHash.put(key, jsonObject.getString(key));
        }
        return responseHash;
    }

    /**
     * Adds a log entry on the server.
     *
     * @param entry HashMap representing the log entry to add.
     * @throws URISyntaxException      If the query entries ruin
     *                                 the query string syntax.
     * @throws InterruptedException    If the request was interrupted
     *                                 before retrieving the http response.
     * @throws ExecutionException      If the request completed exceptionally.
     * @throws ServerResponseException
     * If there was an error with the server response.
     */
    public void addLogEntry(final HashMap<String, String> entry)
            throws URISyntaxException, InterruptedException,
            ExecutionException, ServerResponseException {
        JSONObject payload = new JSONObject(entry);
        this.post("/api/v1/entries/add", payload.toString());
    }

    /**
     * Retrieves a list of exercise categories from the server.
     *
     * @return A list of exercise categories from the server
     * represented as a hash map.
     * @throws URISyntaxException      If the query entries ruin
     *                                 the query string syntax.
     * @throws InterruptedException    If the request was interrupted
     *                                 before retrieving the http response.
     * @throws ExecutionException      If the request completed exceptionally.
     * @throws ServerResponseException
     * If there was an error with the server response.
     */
    public HashMap<String, List<String>> getExerciseCategories()
            throws URISyntaxException, InterruptedException,
            ExecutionException, ServerResponseException {
        HttpResponse<String> response = this.get("/api/v1/entries/filters");

        JSONObject jsonObject = new JSONObject(response.body());
        jsonObject = jsonObject.getJSONObject("categories");
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
     * @throws URISyntaxException      If the query entries ruin
     *                                 the query string syntax.
     * @throws InterruptedException    If the request was interrupted
     *                                 before retrieving the http response.
     * @throws ExecutionException      If the request completed exceptionally.
     * @throws ServerResponseException
     * If there was an error with the server response.
     */
    public void deleteLogEntry(final String id)
            throws URISyntaxException, InterruptedException,
            ExecutionException, ServerResponseException {
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
     * @throws URISyntaxException      If the query entries ruin
     *                                 the query string syntax.
     * @throws InterruptedException    If the request was interrupted
     *                                 before retrieving the http response.
     * @throws ExecutionException      If the request completed exceptionally.
     * @throws ServerResponseException
     * If there was an error with the server response.
     */
    private HttpResponse<String> get(final String endpoint)
            throws URISyntaxException, InterruptedException,
            ExecutionException, ServerResponseException {

        HttpResponse<String> response = this.getAsync(endpoint).get();
        if (response.statusCode() != OK_CODE) {
            throw new ServerResponseException(
                    HttpResponses.getResponseText(response.statusCode()),
                    response.statusCode());
        }
        return response;
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
     * @throws URISyntaxException If the URI syntax is incorrect.
     * @throws InterruptedException
     * If the underlying asynchronous request was interrupted before retrieval.
     * @throws ExecutionException
     * If the underlying asynchronous request completed exceptionally.
     * @throws ServerResponseException
     * If there was an error with the server response.
     */
    private void post(
            final String endpoint,
            final String payload)
            throws URISyntaxException, InterruptedException,
            ExecutionException, ServerResponseException {

        HttpResponse<String> response = this.postAsync(endpoint, payload).get();
        if (response.statusCode() != OK_CODE) {
            throw new ServerResponseException(
                    HttpResponses.getResponseText(response.statusCode()),
                    response.statusCode());
        }
    }

    /**
     * Utility class to ease filtering and sorting the list of log entries.
     */
    public static class SortArgWrapper {

        /**
         * Whether the sorting should be reversed.
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
        public SortArgWrapper reverse() {
            this.reverseVal = !this.reverseVal;
            return this;
        }

        /**
         * Set sorting.
         *
         * @param sort sorting configuration.
         * @return a ListBuilder instance.
         */
        public SortArgWrapper sort(final String sort) {
            this.sortVal = sort;
            return this;
        }

        /**
         * Set category filtering.
         *
         * @param category category to filter by.
         * @return a ListBuilder instance.
         */
        public SortArgWrapper category(final String category) {
            this.categoryVal = category;
            return this;
        }

        /**
         * Set subCategory filtering.
         *
         * @param subCategory sub category to filter by.
         * @return a ListBuilder instance.
         */
        public SortArgWrapper subCategory(final String subCategory) {
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
        public SortArgWrapper date(final String date) {
            this.dateVal = date;
            return this;
        }
    }
}
