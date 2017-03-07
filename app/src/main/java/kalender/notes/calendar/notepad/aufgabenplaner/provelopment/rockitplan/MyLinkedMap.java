package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Eric on 26.02.2017.
 */

public class MyLinkedMap<K, V> extends LinkedHashMap<K, V> {

    public V getValue(int i)
    {

        Map.Entry<K, V>entry = this.getEntry(i);
        if(entry == null) return null;

        return entry.getValue();
    }

    public Map.Entry<K, V> getEntry(int i)
    {
        // check if negetive index provided
        Set<Entry<K,V>> entries = entrySet();
        int j = 0;

        for(Map.Entry<K, V>entry : entries)
            if(j++ == i)return entry;

        return null;

    }
}
