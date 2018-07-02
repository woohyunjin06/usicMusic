package com.narsha2018.usicmusic.util

import android.content.Context
import org.jetbrains.anko.doAsync
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import com.narsha2018.usicmusic.R

class RestUtils(val context: Context){
    fun getResult(requestLocation: String, body: String) : String?{
        var result: String?= null
        doAsync {
            try {
                val server = context.resources.getString(R.string.server_url)

                val u = URL(server+requestLocation)
                val huc = (u.openConnection() as HttpURLConnection).apply {
                    readTimeout = 4000
                    connectTimeout = 4000
                    requestMethod = "POST"
                    doInput = true
                    doOutput = true
                    setRequestProperty("utf-8", "application/x-www-form-urlencoded")
                    outputStream.apply {
                        write(body.toByteArray(charset("utf-8")))
                        flush()
                        close()
                    }
                }


                val br = BufferedReader(InputStreamReader(huc.inputStream, "utf-8"))
                var ch = br.read()
                val sb = StringBuffer()
                while (ch != -1) {
                    sb.append(ch.toChar())
                    ch = br.read()
                }

                br.close()

                result = sb.toString()


            } catch (e: Exception) {
                result = "ERR"
            } finally {

            }
        }
        return result
    }

}