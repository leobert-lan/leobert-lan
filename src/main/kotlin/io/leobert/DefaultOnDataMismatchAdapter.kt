package io.leobert

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import java.lang.reflect.Type

class DefaultOnDataMismatchAdapter<T> private constructor(
  private val delegate: JsonAdapter<T>,
  private val defaultValue: T
) : JsonAdapter<T?>() {
  override fun fromJson(reader: JsonReader): T? {
    // Use a peeked reader to leave the reader in a known state even if there's an exception.
    val result = reader.peekJson().use { peeked ->
      try {
        // Attempt to decode to the target type with the peeked reader.
        delegate.fromJson(peeked)
      } catch (e: JsonDataException) {
        defaultValue
      }
    }
    // Skip the value back on the reader, no matter the state of the peeked reader.
    reader.skipValue()
    return result
  }

  override fun toJson(writer: JsonWriter, value: T?) {
    delegate.toJson(writer, value)
  }

  companion object {
    fun <T> newFactory(type: Class<T>, defaultValue: T): Factory {
      return object : Factory {
        override fun create(
          requestedType: Type, annotations: Set<Annotation>, moshi: Moshi
        ): JsonAdapter<*>? {
          if (type != requestedType) return null
          val delegate: JsonAdapter<T> = moshi.nextAdapter(this, type, annotations)
          return DefaultOnDataMismatchAdapter(delegate, defaultValue)
        }
      }
    }
  }
}