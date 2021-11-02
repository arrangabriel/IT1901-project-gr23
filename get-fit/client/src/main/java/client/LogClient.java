package client;

import java.io.IOException;
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

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Representation of a connection to a get-fit server
 */
public class LogClient{

    /** URL to remote server. */
    private final String url;
    
    /** Port to the remote server.  */
    private final int port;

    /**
     * Constructs a LogClient from a builder.
     * @param builder
     */
    public LogClient(LogClientBuilder builder) {

        this.url = builder.url;
        this.port = builder.port;

    }

    /**
     * Gets a LogEntry from the server.
     * @param id The id of the LogEntry to retrieve.
     * @return The LogEntry represented by a hash map.
     * @throws URISyntaxException If the id ruins the URI syntax.
     * @throws IOException If the underlying resources cannot be allocated.
     * @throws InterruptedException If the request was interrupted before retreiving the http response.
     * @throws ExecutionException If the request completed exceptionally.
     */
    public HashMap<String, String> getLogEntry(String id) throws URISyntaxException, IOException, InterruptedException, ExecutionException {

        HttpResponse<String> response = this.get("/api/v1/entries/"+id);
        String jsonString = response.body();

        JSONObject jsonObject = new JSONObject(jsonString);

        HashMap<String, String> responseHash = new HashMap<String, String>();

        for (String key : jsonObject.keySet()) {
            responseHash.put(key, jsonObject.getString(key));
        }

        return responseHash;

    }

    /**
     * Asynchronously requests details for every id.
     * @param ids the ids to request details for.
     * @return The hash map of hash maps representing the LogEntries
     * @throws URISyntaxException If any of the ids ruin the URI syntax.
     * @throws IOException If the underlying resources cannot be allocated.
     * @throws InterruptedException If the requests were interrupted before retreiving the http responses.
     * @throws ExecutionException If the requests were completed exceptionally.
     */
    public HashMap<String, HashMap<String, String>> getLogEntryDetailedList(String... ids) throws URISyntaxException, IOException, InterruptedException, ExecutionException {

        HashMap<String, CompletableFuture<HttpResponse<String>>> futures = new HashMap<>();

        for (String id : ids) {
            futures.put(id, this.getAsync("/api/v1/entries/"+id));
        }

        HashMap<String, HashMap<String, String>> responses = new HashMap<>();

        for (Entry<String, CompletableFuture<HttpResponse<String>>> futureEntry : futures.entrySet()) {

            JSONObject jsonObject = new JSONObject(futureEntry.getValue().get().body());
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
     * @return A list of log entries from the server represented as a hash map.
     * @throws URISyntaxException If the hardcoded URI no longer matches the servers expectations.
     * @throws IOException If the underlying resources cannot be allocated.
     * @throws InterruptedException If the request was interrupted before retreiving the http response.
     * @throws ExecutionException If the request completed exceptionally.
     */
    public List<HashMap<String, String>> getLogEntryList() throws URISyntaxException, IOException, InterruptedException, ExecutionException {
        return this.getLogEntryList(new ListBuilder());
    }

    /**
     * Get a list of log entries from the server.
     * @param builder The query string builder to generate a query string for filtering and sorting.
     * @return A list of log entries from the server represented as a hash map.
     * @throws URISyntaxException If the query entries ruin the query string syntax.
     * @throws IOException If the underlying resources cannot be allocated.
     * @throws InterruptedException If the request was interrupted before retreiving the http response.
     * @throws ExecutionException If the request completed exceptionally.
     */
    public List<HashMap<String, String>> getLogEntryList(ListBuilder builder) throws URISyntaxException, IOException, InterruptedException, ExecutionException {

        String queryString = "?";

        List<String> queries = new ArrayList<>();

        if (builder.reverse != null) {
            queries.add("r=" + builder.reverse.toString());
        }
        if (builder.sort != null) {
            queries.add("s="+builder.sort);
        }
        if (builder.category != null) {
            queries.add("c="+builder.category);
        }
        if (builder.subCategory != null) {
            queries.add("sc="+builder.subCategory);
        }
        if (builder.date != null) {
            queries.add("d="+builder.date);
        }

        queryString += String.join("&", queries);


        HttpResponse<String> response = this.get("/api/v1/entries/list"+queryString);
        String jsonString = response.body();

        JSONObject jsonObject = new JSONObject(jsonString);

        List<HashMap<String, String>> responseList = new ArrayList<HashMap<String, String>>();

        JSONArray array = jsonObject.getJSONArray("entries");
        for (int i = 0; i < array.length(); i++) {
            HashMap<String, String> innerMap = new HashMap<String, String>();

            innerMap.put("id", array.getJSONObject(i).getString("id"));
            innerMap.put("name", array.getJSONObject(i).getString("name"));

            responseList.add(innerMap);
        }        

        return responseList;

    }

    /**
     * Elementary asynchronous get request.
     * @param endpoint Where to send the request to.
     * @return The Http response promise.
     * @throws URISyntaxException If the URI syntax is incorrect.
     * @throws IOException If the underlying resources cannot be allocated.
     */
    private CompletableFuture<HttpResponse<String>> getAsync(String endpoint) throws URISyntaxException, IOException {

        HttpClient client = HttpClient.newBuilder()
            .build();

        HttpRequest request = HttpRequest.newBuilder()
            .GET()
            .uri(
                new URI(this.url+":"+this.port+endpoint)
            )
            .build();

        return client.sendAsync(request, BodyHandlers.ofString());

    }

    /**
     * Elementary synchronous get request.
     * @param endpoint Where to send the request to.
     * @return The Http response.
     * @throws URISyntaxException If the URI syntax is incorrect.
     * @throws IOException If the underlying resources cannot be allocated.
     * @throws InterruptedException If the underlying asynchronous request was interrupted before retreival.
     * @throws ExecutionException If the underlying asynchronous request completed exceptionally.
     */
    private HttpResponse<String> get(String endpoint) throws URISyntaxException, IOException, InterruptedException, ExecutionException {


        
        return this.getAsync(endpoint).get();

    }

    /**
     * Elementary asynchronous post request.
     * @param endpoint Where to send the requests to.
     * @param payload Where to send to the server.
     * @return The Http response promise.
     * @throws URISyntaxException If the URI syntax is incorrect.
     * @throws IOException If the underlying resources cannot be allocated.
     */
    private CompletableFuture<HttpResponse<String>> postAsync(String endpoint, String payload) throws URISyntaxException, IOException {

        HttpClient client = HttpClient.newBuilder()
            .build();

        HttpRequest request = HttpRequest.newBuilder()
            .POST(HttpRequest.BodyPublishers.ofString(payload))
            .uri(
                new URI(this.url+":"+this.port+endpoint)
            )
            .build();

        return client.sendAsync(request, BodyHandlers.ofString());

    }

    /***
     * Elementary synchronous post request.
     * @param endpoint Where to send the request to.
     * @param payload What to send to the server.
     * @return  The Http response.
     * @throws URISyntaxException If the URI syntax is incorrect.
     * @throws IOException If the underlying resources cannot be allocated.
     * @throws InterruptedException If the underlying asynchronous request was interrupted before retreival.
     * @throws ExecutionException If the underlying asynchronous request completed exceptionally.
     */
    private HttpResponse<String> post(String endpoint, String payload) throws URISyntaxException, IOException, InterruptedException, ExecutionException {

        return this.postAsync(endpoint, payload).get();

    }


    /**
     * Builder for a LogClient.
     * @see LogClient
    */
    public static class LogClientBuilder {

        /** The url to send the requests to */
        private String url;

        /** The port to send the requests to*/
        private int port;

        /**
         * Set the url to the server.
         * @param server_url
         */
        public void url(String server_url) {
            this.url = server_url;
        }

        /**
         * Set the port to the server.
         * @param server_prot
         */
        public void port(int server_prot) {
            this.port = server_prot;
        }

        /**
         * Build a LogClient.
         * @return the LogClient.
         */
        public LogClient build() {
            return new LogClient(this);
        }

    }

    /**
     * Builder for filtering and sorting the list of log entries.
     * @see #getLogEntryList
     */
    public static class ListBuilder {

        /** Wether the sorting should be reversed. */
        private Boolean reverse = null;

        /** How the list should be sorted. */
        private String sort = null;

        /** What category should be filtered for. */
        private String category = null;

        /** What subcategory should be filtered for. */
        private String subCategory = null;

        /** What date interval should be included */
        private String date = null;

        /**
         * Set reverse.
         * @param reverse
         */
        public void reverse(boolean reverse) {
            this.reverse = reverse;
        }

        /**
         * Set sorting.
         * @param sort
         */
        public void sort(String sort) {
            this.sort = sort;
        }

        /**
         * Set category filtering.
         * @param category
         */
        public void category(String category) {
            this.category = category;
        }

        /**
         * Set subCategory filtering.
         * @param subCategory
         */
        public void subCategory(String subCategory) {
            this.subCategory = subCategory;
        }

        /**
         * Set date filterin.
         * @param date
         */
        public void date(String date) {
            this.date = date;
        }

    }
    
}
