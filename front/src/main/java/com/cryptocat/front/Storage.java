package com.cryptocat.front;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Storage {
    Block latestBlock;

    public Storage() {
        this.latestBlock = new Block(0L, "gen", new Date());
    }

    public void setLatestBlock(Block block) {
        this.latestBlock = block;
    }
}
