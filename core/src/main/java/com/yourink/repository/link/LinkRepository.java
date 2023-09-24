package com.yourink.repository.link;

import com.yourink.domain.link.Link;
import com.yourink.repository.PageableRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface LinkRepository extends JpaRepository<Link, Long>, PageableRepository {
    Boolean existsByIdLessThan(Long id);

    @Modifying
    @Query("UPDATE Link l SET l.hitCount = l.hitCount + 1 WHERE l.id = :id")
    void updateHitCount(Long id);
}
