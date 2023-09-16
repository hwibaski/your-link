package com.yourink.repository.link;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yourink.domain.link.Link;

public interface LinkRepository extends JpaRepository<Link, Long> {
}
