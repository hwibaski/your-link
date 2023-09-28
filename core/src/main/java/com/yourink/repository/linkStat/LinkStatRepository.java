package com.yourink.repository.linkStat;

import com.yourink.domain.link.Link;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkStatRepository extends JpaRepository<Link, Long> {
}
