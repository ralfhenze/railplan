package com.ralfhenze.railplan.domain.common;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.javatuples.Pair;

import java.util.Deque;
import java.util.LinkedList;

public class Combinations {

    public <T> ImmutableList<Pair<T, T>> getUniqueCombinations(ImmutableList<T> elements) {

        final MutableList<Pair<T, T>> uniqueCombinations = Lists.mutable.empty();
        final Deque<T> sourceElements = new LinkedList<>(elements.castToList());
        final Deque<T> otherElements = new LinkedList<>(elements.castToList());

        sourceElements.removeLast();

        for (final T sourceElement : sourceElements) {
            otherElements.removeFirst();
            for (final T otherElement : otherElements) {
                uniqueCombinations.add(new Pair<>(sourceElement, otherElement));
            }
        }

        return uniqueCombinations.toImmutable();
    }
}
