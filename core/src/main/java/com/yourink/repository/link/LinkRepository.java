package com.yourink.repository.link;

import com.yourink.domain.link.Link;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkRepository extends JpaRepository<Link, Long> {
}
