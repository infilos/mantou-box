package com.infilos.mantou.utils;

import javafx.beans.property.SimpleBooleanProperty;

import java.util.*;

public class ObservableListIterator<T> implements ListIterator<T> {

    private List<T> list;
    private ListIterator<T> delegate;
    private final SimpleBooleanProperty hasNext = new SimpleBooleanProperty();
    private final SimpleBooleanProperty hasPrevious = new SimpleBooleanProperty();

    public ObservableListIterator() {
        this(new ArrayList<T>(0).listIterator());
    }

    public ObservableListIterator(ListIterator<T> delegate) {
        setIterator(delegate);
    }

    public void setIterator(ListIterator<T> delegate) {
        this.delegate = delegate;
        this.hasNext.set(delegate.hasNext());
        this.hasPrevious.set(delegate.hasPrevious());
    }

    public void setIterator(List<T> delegate) {
        this.list = delegate;
        this.delegate = delegate.listIterator();
        this.hasNext.set(this.delegate.hasNext());
        this.hasPrevious.set(this.delegate.hasPrevious());
    }

    public SimpleBooleanProperty hasNextProperty() {
        return this.hasNext;
    }

    public SimpleBooleanProperty hasPreviousProperty() {
        return hasPrevious;
    }

    @Override
    public boolean hasNext() {
        return hasNext.get();
    }

    @Override
    public T next() {
        T value = delegate.next();
        this.hasNext.set(delegate.hasNext());
        this.hasPrevious.set(delegate.hasPrevious());
        return value;
    }

    @Override
    public boolean hasPrevious() {
        return hasPrevious.get();
    }

    @Override
    public T previous() {
        T value = delegate.previous();
        this.hasNext.set(delegate.hasNext());
        this.hasPrevious.set(delegate.hasPrevious());
        return value;
    }

    @Override
    public int nextIndex() {
        return delegate.nextIndex();
    }

    @Override
    public int previousIndex() {
        return delegate.previousIndex();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void set(T t) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(T t) {
        throw new UnsupportedOperationException();
    }
    
    public int indexOf(T t) {
        return list.indexOf(t);
    }
}
