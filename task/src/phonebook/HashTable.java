package phonebook;

class HashTable<T> {
    private int size;
    private TableEntry[] table;

    public HashTable(int size) {
        this.size = size;
        table = new TableEntry[size];
    }

    private int findKey(int key) {
        int hash = key % size;

        while (!(table[hash] == null || table[hash].getKey() == key)) {
            hash = (hash + 1) % size;

            if (hash == key % size) {
                return -1;
            }
        }

        return hash;
    }

    private void rehash() {
        HashTable<T> newHashTable = new HashTable<>(size * 2);
        for (TableEntry<T> tableEntry : table) {
            newHashTable.put(tableEntry.getKey(), tableEntry.getValue());
        }
        this.size = newHashTable.size;
        this.table = newHashTable.table;
    }

    public T get(int key) {
        int idx = findKey(key);

        if (idx == -1 || table[idx] == null) {
            return null;
        }

        return (T) table[idx].getValue();
    }

    public boolean put(int key, T value) {
        int idx;

        while ((idx = findKey(key)) < 0) {
            rehash();
        }
        table[idx] = new TableEntry(key, value);
        return true;
    }

}
