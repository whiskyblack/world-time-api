package alphalabs.utils

import android.os.Environment
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

suspend fun saveFile(srcFile: File, dstFile: File, callback: suspend (Boolean) -> Unit) {
    runCatching {
        val inputStream = FileInputStream(srcFile)
        val outputStream = FileOutputStream(dstFile)
        val bytes = ByteArray(2048)
        var length = inputStream.read(bytes)
        while (length > -1) {
            outputStream.write(bytes, 0, length)
            length = inputStream.read(bytes)
        }
        outputStream.flush()
        outputStream.close()
        inputStream.close()
        callback(true)
    }.getOrElse {
        callback(false)
    }
}

fun getChildDirInPictures(childDirName: String): File {
    val picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
    val childDir = File(picturesDir, childDirName)
    if (childDir.exists().not()) childDir.mkdirs()
    return childDir
}