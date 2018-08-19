package it.sephiroth.android.library.cache

import java.util.*


/**
 * Very Simple LruCache implementation.
 * It's a fixed size lru cache, as it doesn't support resizing.
 * It only supports add, get and remove.
 *
 * Alessandro Crugnola
 * alessandro.crugnola@gmail.com
 */

class LruCache<T>(val maxSize: Int) {

    private data class Entry<T>(val key: Int, val value: T) {
        var prev: Entry<T>? = null
        var next: Entry<T>? = null
    }

    private val lock = Any()
    private val cache: HashMap<Int, Entry<T>> = hashMapOf()
    private var first: Entry<T>? = null
    private var last: Entry<T>? = null

    /**
     * Returns the number of elements currently in cache
     */
    val size: Int
        get() = cache.size

    /**
     * Returns an array containing the first and the last key
     */
    val keys: Array<Int?>
        get() = arrayOf(first?.key, last?.key)

    /**
     * Add a new element or replace an existing one (and putting on top of the stack)
     */
    operator fun set(key: Int, value: T) {
        synchronized(lock) {
            val search = remove(key)
            search?.let {
                add(key, search)
            } ?: run {
                add(key, value)
            }
        }
    }

    /**
     * Returns an element (and put on top of the stack) from the queue, if exists
     */
    operator fun get(key: Int): T? {
        synchronized(lock) {
            var value: T? = null
            val search = remove(key)
            search?.let {
                value = it.value
                add(key, search)
            }
            return value
        }
    }

    operator fun contains(key: Int): Boolean {
        return cache.contains(key)
    }

    /**
     * Remove an element from the cache
     */
    fun erase(key: Int): T? {
        synchronized(lock) {
            val search = remove(key)
            search?.let {
                return it.value
            } ?: run {
                return null
            }
        }
    }

    // private methods

    private fun remove(key: Int): Entry<T>? {
        val entry = cache.remove(key) ?: return null

        entry.next?.prev = entry.prev
        entry.prev?.next = entry.next

        if (first?.key == entry.key) {
            first = first!!.next
            first?.prev = null
        }

        if (last?.key == entry.key) {
            last = entry.prev
            last?.next = null
        }

        entry.next = null
        entry.prev = null
        return entry
    }

    private fun add(key: Int, value: T) {
        add(key, Entry(key, value))
    }

    private fun add(key: Int, entry: Entry<T>) {
        cache[key] = entry

        entry.prev = last

        if (first == null) first = entry
        last?.next = entry

        last = entry

        if (cache.size > maxSize) {
            remove(first!!.key)
        }

    }

}