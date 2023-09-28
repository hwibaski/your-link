package com.yourink.service;

import com.yourink.domain.base.BaseEntity;
import com.yourink.repository.PageableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaginationService {
    private final PageableRepository pageableRepository;

    public <T extends BaseEntity> Boolean hasNext(List<T> dataList) {
        if (dataList.isEmpty()) return false;

        return this.pageableRepository.existsByIdLessThan(dataList.get(dataList.size() - 1)
                                                                  .getId());
    }
}
