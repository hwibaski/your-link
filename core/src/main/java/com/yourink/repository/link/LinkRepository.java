package com.yourink.repository.link;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yourink.domain.link.Link;
import com.yourink.repository.PageableRepository;

public interface LinkRepository extends JpaRepository<Link, Long>, PageableRepository {
    Boolean existsByIdLessThan(Long id);
}
