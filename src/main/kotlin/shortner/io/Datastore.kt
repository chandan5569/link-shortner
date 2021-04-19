package shortner.io

import java.io.File
import java.util.*

object Datastore {
   private val properties=Properties()
    private val propFile = File("server.prop")

    val allKeys get() = properties.keys.map { it.toString() }
    val allValue get() = properties.values.map { it.toString() }

    init {
       propFile.reader().use {
           properties.load(it)
       }
   }

   operator fun get(key:String): String? {
        return properties.getProperty(key)
    }

    operator fun set(key: String,value:String){
        properties.setProperty(key,value)
        propFile.delete();
        propFile.writer().use {
            properties.store(it,System.currentTimeMillis().toString())
        }
    }




}