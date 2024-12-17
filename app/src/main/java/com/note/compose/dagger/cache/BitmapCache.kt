package com.note.compose.dagger.cache

import android.graphics.Bitmap
import android.util.LruCache

class BitmapCache(maxSize: Int) : LruCache<String?, Bitmap>(maxSize) {
    override fun sizeOf(key: String?, value: Bitmap): Int {
        return value.byteCount / 1024
    }

    private fun getBitmap(key: String?): Bitmap? {
        return this[key]
    }

    private fun hasBitmap(key: String?): Boolean {
        return getBitmap(key) != null
    }

    companion object {
        val cacheSize: Int
            get() {
                val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
                // use 1/8th of the available memory for this memory cache.
                return maxMemory / 8
            }
    }
}