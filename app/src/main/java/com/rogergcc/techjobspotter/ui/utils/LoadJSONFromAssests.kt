package com.rogergcc.techjobspotter.ui.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.Charset


/**
 * Created on agosto.
 * year 2023 .
 */
/* Android: Load JSON from `src/main/assets/` */

/* Extension function where `Context` passed explicitly */
fun loadJSONFromAsset(context: Context, fileName: String): String {
    val inputStream: InputStream = context.assets.open(fileName)
    val size: Int = inputStream.available()
    val buffer = ByteArray(size)
    inputStream.read(buffer)
    inputStream.close()
    return String(buffer, Charset.defaultCharset())
}

///* Extension function of `Context` class */
//@JvmName("loadJSONFromAsset1")
//fun Context.loadJSONFromAsset(fileName: String): String {
//    val inputStream: InputStream = this.assets.open(fileName)
//    val size: Int = inputStream.available()
//    val buffer = ByteArray(size)
//    inputStream.read(buffer)
//    inputStream.close()
//    return String(buffer, Charset.defaultCharset())
//}

/**
 * Carga un archivo JSON desde la carpeta "assets" y lo convierte en un objeto del tipo especificado por [T].
 * @param fileName El nombre del archivo JSON en la carpeta "assets".
 * @return El objeto convertido del tipo [T], o nulo si ocurrió un error.
 */
inline fun <reified T> Context.loadJSONFromAsset(fileName: String): T? {
    return try {
        val inputStream = assets.open(fileName)
        val reader = InputStreamReader(inputStream, Charset.defaultCharset())
        val gson = Gson()
        gson.fromJson(reader, T::class.java)
    } catch (ioException: IOException) {
        ioException.printStackTrace()
        null
    } catch (jsonSyntaxException: JsonSyntaxException) {
        jsonSyntaxException.printStackTrace()
        null
    }
}
///* Usages */
//
//loadJSONFromAsset(context,"mock_response.json")
//
///* Alternative */
//context.assets.open(fileName).bufferedReader().use(JsonParser::parseReader).asJsonObject.toString()