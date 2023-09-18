package com.yourink.repository;

public interface PageableRepository {
    Boolean existsByIdLessThan(Long id);
}
