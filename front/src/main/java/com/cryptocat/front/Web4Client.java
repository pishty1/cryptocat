package com.cryptocat.front;

import org.springframework.web.service.annotation.GetExchange;

import java.util.List;

public interface Web4Client {

    @GetExchange("/blocks")
    List<Block> getBlocks();

}
