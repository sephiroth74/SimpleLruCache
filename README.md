# SimpleLruCache

Simple LruCache for Android


[![Build Status](https://travis-ci.org/sephiroth74/SimpleLruCache.svg?branch=master)](https://travis-ci.org/sephiroth74/SimpleLruCache)

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/it.sephiroth.android.library.cache/simple-lru-cache/badge.svg)](https://search.maven.org/search?q=g:it.sephiroth.android.library.cache%20a:simple-lru-cache)



# Installation

Add the library dependency:

    implementation 'it.sephiroth.android.library.cache:simple-lru-cache:**version**'

# Usage

```
    // creates a new string lru-cache of 3 elements max 
    val cache = LruCache<String>(3)
    
    cache[0] = "first"
    cache[1] = "second"
    cache[2] = "third"
    cache[3] = "fourth" // key `0` is removed
    
    val entry = cache[1] // returns `second`
    val entry2 = cache[0] // returns null
    
    cache.erase(1) // key `1` is removed (now size is 2)
    
```
