package it.sephiroth.test

import it.sephiroth.android.library.cache.LruCache
import org.junit.Assert.*
import org.junit.Test

class LruCacheUnitTest {

    @Test
    fun testAddElements() {
        val cache = LruCache<String>(3)
        assertEquals(0, cache.size)

        cache[0] = "a"
        assertEquals(1, cache.size)

        cache[1] = "b"
        assertEquals(2, cache.size)

        cache[2] = "c"
        assertEquals(3, cache.size)

        cache[3] = "d"
        assertEquals(3, cache.size)

        cache[4] = "e"
        assertEquals(3, cache.size)
    }

    @Test
    fun testAddElements2() {
        val cache = LruCache<String>(3)
        assertEquals(0, cache.size)

        cache[0] = "a"
        assertArrayEquals(arrayOf(0, 0), cache.keys)

        cache[1] = "b"
        assertArrayEquals(arrayOf(0, 1), cache.keys)

        cache[2] = "c"
        assertArrayEquals(arrayOf(0, 2), cache.keys)

        cache[3] = "d"
        assertArrayEquals(arrayOf(1, 3), cache.keys)

        cache[4] = "e"
        assertArrayEquals(arrayOf(2, 4), cache.keys)

        assertEquals(3, cache.size)
    }

    @Test
    fun testAddElements3() {
        val cache = LruCache<String>(3)
        assertEquals(0, cache.size)

        cache[0] = "a" // 0
        assertEquals(0, cache.keys[0])
        assertEquals(0, cache.keys[1])

        cache[0] = "a" // 0
        assertEquals(0, cache.keys[0])
        assertEquals(0, cache.keys[1])

        cache[1] = "b" // 0,1
        assertEquals(0, cache.keys[0])
        assertEquals(1, cache.keys[1])
        assertEquals(2, cache.size)

        cache[0] = "a" // 1,0
        assertEquals(1, cache.keys[0])
        assertEquals(0, cache.keys[1])
        assertEquals(2, cache.size)

        cache[5] = "five" // 1,0,5
        assertEquals(1, cache.keys[0])
        assertEquals(5, cache.keys[1])
        assertEquals(3, cache.size)

        cache[2] = "two" // 0,5,2
        assertEquals(0, cache.keys[0])
        assertEquals(2, cache.keys[1])
        assertEquals(3, cache.size)

        cache[0] = "zero" // 5,2,0
        assertEquals(5, cache.keys[0])
        assertEquals(0, cache.keys[1])
        assertEquals(3, cache.size)

        cache[2] = "two" // 5,0,2
        assertEquals(5, cache.keys[0])
        assertEquals(2, cache.keys[1])
        assertEquals(3, cache.size)
    }


    @Test
    fun testEraseElements3() {
        val cache = LruCache<String>(4)
        cache[0] = "a" // 0
        cache[1] = "b" // 0,1
        cache[2] = "c" // 0,1,2
        cache[3] = "d" // 0,1,2,3
        assertEquals(4, cache.size)

        cache.erase(0) // 1,2,3
        assertEquals(3, cache.size)
        assertArrayEquals(arrayOf(1, 3), cache.keys)

        cache.erase(2) // 1,3
        assertEquals(2, cache.size)
        assertArrayEquals(arrayOf(1, 3), cache.keys)

        cache.erase(2) // 1,3
        assertEquals(2, cache.size)
        assertArrayEquals(arrayOf(1, 3), cache.keys)

        cache.erase(3) // 1
        assertEquals(1, cache.size)
        assertArrayEquals(arrayOf(1, 1), cache.keys)

        cache.erase(1) // ...
        assertEquals(0, cache.size)

        val (head, tail) = cache.keys
        assertNull(head)
        assertNull(tail)
    }


    @Test
    fun testGetElements() {
        val cache = LruCache<String>(3)

        cache[0] = "a" // 0
        cache[1] = "b" // 0,1
        cache[2] = "c" // 0,1,2

        assertEquals("a", cache[0]) // 1,2,0
        assertArrayEquals(arrayOf(1, 0), cache.keys)

        assertEquals("b", cache[1]) // 2,0,1
        assertArrayEquals(arrayOf(2, 1), cache.keys)

        assertEquals("c", cache[2]) // 0,1,2
        assertArrayEquals(arrayOf(0, 2), cache.keys)

        cache[4] = "d" // 1,2,4
        assertEquals(3, cache.size)
        assertArrayEquals(arrayOf(1, 4), cache.keys)
        assertEquals("d", cache[4])

        assertTrue(1 in cache)
        assertTrue(4 in cache)
        assertTrue(2 in cache)
        assertFalse(0 in cache)

        // keys should havent changed
        assertArrayEquals(arrayOf(1, 4), cache.keys)
    }
}