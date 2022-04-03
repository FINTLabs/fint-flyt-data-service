package no.fintlabs.cache;

import org.ehcache.event.CacheEventListener;

public interface FintCacheEventListener<K, V> {

    void onEvent(FintCacheEvent<K, V> event);

}
