package com.cryptocat.web4;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class BlockController {
    private final BlockRepository blockRepository;

    BlockController(BlockRepository blockRepository) {
        this.blockRepository = blockRepository;
    }

    @GetMapping("/blocks")
    Iterable<Block> getBlocks() {
        return this.blockRepository.findAll(PageRequest.of(0, 30, Sort.by("created").descending())).getContent();
    }
}
