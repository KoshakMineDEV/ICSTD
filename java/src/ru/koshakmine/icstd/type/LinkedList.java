package ru.koshakmine.icstd.type;

public class LinkedList<T> {
    public static class Entry<T> {
        public Entry<T> pre;
        public Entry<T> next;
        public final T self;
        private final LinkedList<T> list;

        public Entry(Entry<T> pre, Entry<T> next, T self, LinkedList<T> list){
            this.pre = pre;
            this.next = next;

            this.self = self;
            this.list = list;
        }

        public void remove(){
            if(list.first == this){
                list.clear();
                return;
            }
            if (pre != null)
                pre.next = next;

            if (next != null)
                next.pre = pre;

            pre = null;
            next = null;
        }
    }

    public Entry<T> first;
    public Entry<T> end;

    public Entry<T> add(T value){
        if(first == null && end == null){
            first = new Entry<>(null, null, value, this);
            return first;
        }

        final Entry<T> newEntry = new Entry<>(end, null, value, this);
        if(end == null) {
            newEntry.pre = first;
            first.next = newEntry;
        }

        if(end != null){
            end.next = newEntry;
        }
        end = newEntry;

        return null;

        /*if(first != null && end == null){
            end = new Entry<>(first, null, value, this);
            first.next = end;
            return end;
        }

        final Entry<T> newEntry = new Entry<>(end, null, value, this);
        end.next = newEntry;
        end = newEntry;
        return newEntry;*/
    }

    public Entry<T> getFirst() {
        return first;
    }

    public void clear() {
        first = null;
        end = null;
    }

    public int size() {
        int size = 0;

        LinkedList.Entry<T> first = getFirst();
        while (first != null){
            size++;
            first = first.next;
        }

        return size;
    }
}
