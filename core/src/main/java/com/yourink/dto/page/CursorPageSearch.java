package com.yourink.dto.page;


public record CursorPageSearch(Long id, Integer size) {
    public static final int DEFAULT_SIZE = 10;

    public CursorPageSearch {
        if (size == null) {
            size = DEFAULT_SIZE;
        }
    }
}
