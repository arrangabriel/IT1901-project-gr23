package client;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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

    private CompletableFuture<HttpResponse<String>> getAsync(String endpoint) throws URISyntaxException, IOException, InterruptedException {

        HttpClient client = HttpClient.newBuilder()
            .build();

        HttpRequest request = HttpRequest.newBuilder()
            .GET()
            .uri(
                new URI(this.url+endpoint)
            )
            .build();

        return client.sendAsync(request, BodyHandlers.ofString());

    }

    private HttpResponse<String> get(String endpoint) throws URISyntaxException, IOException, InterruptedException, ExecutionException {

        return this.getAsync(endpoint).get();

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
    
}
