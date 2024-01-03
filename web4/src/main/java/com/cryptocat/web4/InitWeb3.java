package com.cryptocat.web4;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.http.HttpService;

@Component
class InitWeb3 {
    private static final String nodeUrl = System.getenv().getOrDefault("WEB3J_NODE_URL", "<node_url>");
    private static final String walletPassword = System.getenv().getOrDefault("WEB3J_WALLET_PASSWORD", "<wallet_password>");
    private static final String walletPath = System.getenv().getOrDefault("WEB3J_WALLET_PATH", "<wallet_path>");
    private final Web3j client;
    private final BlockRepository blockRepository;
    private final KafkaTemplate<String, Block> kafkaTemplate;
    private final KafkaTemplate<String, Block> kafkaTemplate_2;


    public InitWeb3(BlockRepository blockRepository, KafkaTemplate<String, Block> kafkaTemplate) {
        this.kafkaTemplate_2 = kafkaTemplate;
        this.kafkaTemplate = kafkaTemplate;
        this.blockRepository = blockRepository;
        this.client = Web3j.build(new HttpService(nodeUrl));

        this.client.blockFlowable(false).subscribe(block -> {
            EthBlock.Block newBlock = block.getBlock();
            Block save = this.blockRepository.save(new Block(newBlock.getMiner()));
            System.out.println("New block hash: " + newBlock.getMiner());
            System.out.println("sending block : " + save.getId() + " hash: " + save.getMinerHash() + " Created: " + save.getCreated());
            this.kafkaTemplate.send("test_topic", save);
            this.kafkaTemplate_2.send("test_topic_2", save);
            // Additional handling for the new block
        }, throwable -> {
            System.err.println("Error in subscription: " + throwable);
        });

        System.out.println("Web3j client is listening ....");
    }

    public Web3j getClient() {
        return client;
    }
}
