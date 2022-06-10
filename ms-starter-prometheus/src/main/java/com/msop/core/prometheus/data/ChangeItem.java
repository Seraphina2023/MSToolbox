package com.msop.core.prometheus.data;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChangeItem<T> {
    private final T item;
    private final long changeIndex;

}
