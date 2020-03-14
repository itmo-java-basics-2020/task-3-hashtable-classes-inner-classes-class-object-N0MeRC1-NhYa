package ru.itmo.java;

import org.w3c.dom.ls.LSOutput;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Map;

public class HashTable {

    private Entry[] elements;

    private double loadFactor = 0.5;

    private int size = 0;

    private static final int INIT_TABLE = 8;

    public HashTable() {
        elements = new Entry[INIT_TABLE];
    }

    public HashTable(int InitialCapacity) {
        int initSize = 2;
        while (initSize < InitialCapacity) {
            initSize *= 2;

        }
        elements = new Entry[initSize];
    }

    public HashTable(int InitialCapacity, double lF) {
        int initSize = 2;
        while (initSize < InitialCapacity) {
            initSize *= 2;

        }
        elements = new Entry[initSize];
        loadFactor = lF;
    }

    @Override
    public String toString() {
        return Arrays.toString(elements);
    }

    private int search(Object key, boolean exists) {
        int i = 1;
        int hash = key.hashCode();
        int place = Math.abs(hash % elements.length);
        while (true) {
            place = (place + i * i) % elements.length;
            i++;
            if (elements[place] == null) {
                if (exists) {
                    return -1;
                } else {
                    return place;
                }
            }
            else if (elements[place].key.equals(key)) {
                if (exists) {
                    if (!elements[place].getDel()) {
                        return place;
                    } else {
                        return -1;
                    }
                } else {
                    return place;
                }
            } else if (!exists && elements[place].getDel()) {
                return place;
            }
        }
    }

    public Object put(Object key, Object value) {
        if ((double) (size() + 1) / elements.length >= loadFactor) {
            ensureCapacity();
        }
        Object ans = null;
        int place = search(key, true);
        if (place != -1) {
            Entry newEntry = new Entry(key, value);
            ans = elements[place].getValue();
            elements[place] = newEntry;
        } else {
            place = search(key, false);
            elements[place] = new Entry(key, value);
            size++;
        }
        return ans;
    }

    public Object get(Object key) {
        int place = search(key, true);
        if (place != -1){
            return elements[place].getValue();
        }
        return null;
    }

    public Object remove(Object key) {
        int place = search(key, true);
        if (place != -1){
            elements[place].setDel(true);
            size--;
            return elements[place].getValue();
        }
        return null;
    }

    public int size() {
        return size;
    }

    private void ensureCapacity() {
        Entry[] newElements = elements;
        int newSize = 2 * elements.length;
        size = 0;
        elements = new Entry[newSize];
        for (int i = 0; i < newElements.length; i++) {
            if (newElements[i] != null) {
                if (!newElements[i].getDel()) {
                    put(newElements[i].getKey(), newElements[i].getValue());
                }
            }
        }
    }

    private static class Entry {
        private Object key;
        private Object value;
        private boolean del;

        private Entry(Object key, Object value) {
            this.key = key;
            this.value = value;
            this.del = false;
        }

        private Object getValue() {
            return value;
        }

        private Object getKey() {
            return key;
        }

        private boolean getDel() {
            return del;
        }

        private void setDel(boolean val) {
            del = val;
        }
    }
}