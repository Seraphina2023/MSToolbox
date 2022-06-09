package com.msop.core.prometheus.data;

import lombok.Getter;

@Getter
public class ChangeItem<T> {
    private final T item;
    private final long changeIndex;

    public ChangeItem(final T item,final long changeIndex) {
        this.item = item;
        this.changeIndex = changeIndex;
    }
}
