package com.cryptocat.web4;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.websocket.WebSocketClient;
import org.web3j.protocol.websocket.WebSocketService;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.*;
import java.security.cert.CertificateException;

@Component
class InitWeb3 {
    enum C_TYPE {
        HTTP,
        WEBSOCK
    }
    private static final String nodeUrl = System.getenv().getOrDefault("WEB3J_NODE_URL", "<node_url>");
    private static final String walletPassword = System.getenv().getOrDefault("WEB3J_WALLET_PASSWORD", "<wallet_password>");
    private static final String walletPath = System.getenv().getOrDefault("WEB3J_WALLET_PATH", "<wallet_path>");
    private final Web3j client;
    private final BlockRepository blockRepository;
    private final KafkaTemplate<String, Block> kafkaTemplate;

    public InitWeb3(BlockRepository blockRepository, KafkaTemplate<String, Block> kafkaTemplate) throws UnrecoverableKeyException, CertificateException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, IOException, KeyManagementException {
        this.kafkaTemplate = kafkaTemplate;
        this.blockRepository = blockRepository;
        this.client = createClient(C_TYPE.WEBSOCK);

        this.client.blockFlowable(false).subscribe(block -> {
            EthBlock.Block newBlock = block.getBlock();
            Block save = this.blockRepository.save(new Block(newBlock.getMiner()));
//            System.out.println("New block hash: " + newBlock.getMiner());
            System.out.println("Sending block : " + save.getId() + " hash: " + save.getMinerHash() + " Created: " + save.getCreated());
            this.kafkaTemplate.send("test_topic", save);
//            this.kafkaTemplate_2.send("test_topic_2", save);
            // Additional handling for the new block
        }, throwable -> {
            System.err.println("Error in subscription: " + throwable);
        });

        System.out.println("Web3j client is listening ....");
    }

    public Web3j createClient(C_TYPE type) throws UnrecoverableKeyException, CertificateException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, IOException, KeyManagementException {
        switch (type) {
            case HTTP -> {
                return createHttpClient();
            }
            case WEBSOCK -> {
                return createWebSocketClient();
            }
            default -> {return null;}
        }
    }

    private Web3j createHttpClient() {
        return Web3j.build(new HttpService(nodeUrl));
    }

    private Web3j createWebSocketClient() throws URISyntaxException, KeyStoreException, NoSuchAlgorithmException,
            KeyManagementException, IOException, CertificateException, UnrecoverableKeyException {

        var password = "password";
        WebSocketClient webSocketClient = new WebSocketClient(new URI("wss://mainnet.infura.io/ws/v3/f1b2825035ad4282841d2fc895b2fb1d"));

        WebSocketService webSocketService =
                new WebSocketService(webSocketClient, false);

        // Load the keystore
        KeyStore ks = KeyStore.getInstance("pkcs12");
        File kf = new File("/opt/homebrew/Cellar/openjdk/21.0.1/libexec/openjdk.jdk/Contents/Home/lib/security/cacerts");
        ks.load(new FileInputStream(kf), password.toCharArray());
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, password.toCharArray());
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ks);

        // Create SSL socket
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        // Use SSL socket in websocket client
        webSocketClient.setSocket(sslSocketFactory.createSocket());
        webSocketService.connect();

        return Web3j.build(webSocketService);
    }

    public Web3j getClient() {
        return client;
    }
}
