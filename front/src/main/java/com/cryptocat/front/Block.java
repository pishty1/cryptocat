package com.cryptocat.front;

import java.util.Date;

public record Block(Long id, String minerHash, Date created){}