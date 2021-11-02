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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Representation of a connection to a get-fit server
 */
public class LogClient{

    private final String url;
    
    private final int port;

    public LogClient(LogClientBuilder builder) {

        this.url = builder.url;
        this.port = builder.port;

    }

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

    public List<HashMap<String, String>> getLogEntryList() throws URISyntaxException, IOException, InterruptedException, ExecutionException {
        return this.getLogEntryList(new ListBuilder());
    }

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

    private CompletableFuture<HttpResponse<String>> getAsync(String endpoint) throws URISyntaxException, IOException, InterruptedException {

        HttpClient client = HttpClient.newBuilder()
            .build();

        HttpRequest request = HttpRequest.newBuilder()
            .GET()
            .uri(
                new URI(this.url+":"+this.port+"/"+endpoint)
            )
            .build();

        return client.sendAsync(request, BodyHandlers.ofString());

    }

    private HttpResponse<String> get(String endpoint) throws URISyntaxException, IOException, InterruptedException, ExecutionException {

        return this.getAsync(endpoint).get();

    }


    private CompletableFuture<HttpResponse<String>> postAsync(String endpoint, String payload) throws URISyntaxException, IOException, InterruptedException {

        HttpClient client = HttpClient.newBuilder()
            .build();

        HttpRequest request = HttpRequest.newBuilder()
            .POST(HttpRequest.BodyPublishers.ofString(payload))
            .uri(
                new URI(this.url+endpoint)
            )
            .build();

        return client.sendAsync(request, BodyHandlers.ofString());

    }

    private HttpResponse<String> post(String endpoint, String payload) throws URISyntaxException, IOException, InterruptedException, ExecutionException {

        return this.postAsync(endpoint, payload).get();

    }



    public static class LogClientBuilder {

        private String url;
        private int port;

        public void url(String server_url) {
            this.url = server_url;
        }

        public void port(int server_prot) {
            this.port = server_prot;
        }

        public LogClient build() {
            return new LogClient(this);
        }

    }

    public static class ListBuilder {

        private Boolean reverse = null;
        private String sort = null;
        private String category = null;
        private String subCategory = null;
        private String date = null;

        public void reverse(boolean reverse) {
            this.reverse = reverse;
        }

        public void sort(String sort) {
            this.sort = sort;
        }

        public void category(String category) {
            this.category = category;
        }

        public void subCategory(String subCategory) {
            this.subCategory = subCategory;
        }

        public void date(String date) {
            this.date = date;
        }

    }
    
}
