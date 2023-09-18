package com.yourink.dto.page;

import java.util.List;

public record CursorResult<T>(List<T> data, Boolean hasNext) {
}
