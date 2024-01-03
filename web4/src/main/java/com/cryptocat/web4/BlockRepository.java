package com.cryptocat.web4;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

interface BlockRepository extends ListCrudRepository<Block, Long>, PagingAndSortingRepository<Block, Long> {
}
