package phonebook;

class TableEntry<T> {
    private final int key;
    private final T value;
    private boolean removed;

    public TableEntry(int key, T value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return this.key;
    }

    public T getValue() {
        return this.value;
    }

    public void remove() {
        removed = true;
    }

    public boolean isRemoved() {
        return removed;
    }
}
