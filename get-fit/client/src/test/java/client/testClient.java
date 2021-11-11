package client;

import java.net.URISyntaxException;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class testClient {
    
    private WireMockConfiguration mockConfig;
    private WireMockServer mockServer;

    private LogClient logClient;

    @BeforeEach
    public void startWireMockServer() throws URISyntaxException{
        mockConfig = WireMockConfiguration.wireMockConfig().port(8080);
        mockServer = new WireMockServer(mockConfig.portNumber());
        mockServer.start();
        WireMock.configureFor("localhost", mockConfig.portNumber());
        logClient = new LogClient("http://localhost:", mockServer.port());
    }

    
     



    @AfterEach
    public void stopWireMockServer(){
        mockServer.stop();
    }
    
}
