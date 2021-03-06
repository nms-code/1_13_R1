package net.minecraft.server;

import it.unimi.dsi.fastutil.longs.Long2LongLinkedOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2LongMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2LongMap.Entry;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Map;

public class ExpiringMap<T> extends Long2ObjectOpenHashMap<T> {

    private final int a;
    private final Long2LongMap b = new Long2LongLinkedOpenHashMap();

    public ExpiringMap(int i, int j) {
        super(i);
        this.a = j;
    }

    private void a(long i) {
        long j = SystemUtils.b();

        this.b.put(i, j);
        ObjectIterator objectiterator = this.b.long2LongEntrySet().iterator();

        while (objectiterator.hasNext()) {
            Entry entry = (Entry) objectiterator.next();

            if (j - entry.getLongValue() <= (long) this.a || !this.a(super.get(entry.getLongKey()))) {
                break;
            }

            super.remove(entry.getLongKey());
            objectiterator.remove();
        }

    }

    protected boolean a(T t0) {
        return true;
    }

    public T put(long i, T t0) {
        this.a(i);
        return super.put(i, t0);
    }

    public T put(Long olong, T t0) {
        this.a(olong.longValue());
        return super.put(olong, t0);
    }

    public T get(long i) {
        this.a(i);
        return super.get(i);
    }

    public T get(Long olong) {
        this.a(olong.longValue());
        return super.get(olong);
    }

    public void putAll(Map<? extends Long, ? extends T> map) {
        throw new RuntimeException("Not implemented");
    }

    public T remove(long i) {
        throw new RuntimeException("Not implemented");
    }

    public T remove(Object object) {
        throw new RuntimeException("Not implemented");
    }
}
