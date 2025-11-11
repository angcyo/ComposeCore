package com.angcyo.compose.basics.unit

import android.content.Context
import android.text.TextUtils
import com.angcyo.compose.basics.global.app
import com.angcyo.compose.basics.unit.FileUtils.appRootExternalFolder
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.security.DigestInputStream
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.text.toHexString

/**
 * @author <a href="mailto:angcyo@126.com">angcyo</a>
 * @date 2025/11/11
 */

/**获取存储文件的基础路径, 可以在应用详情中, 通过清理存储清除.
 * [folderName] 文件夹的名字
 *
 * [android.content.Context.getFilesDir] [/data/user/0/com.angcyo.uicore.demo/files/]
 * [android.content.Context.getExternalFilesDir] [/storage/emulated/0/Android/data/com.angcyo.uicore.demo/files/]
 *
 * [com.angcyo.core.component.file.appFilePath]
 * [com.angcyo.library.utils.appFolderPath]
 * */
fun libFileFolder(folderName: String = "", context: Context = app()): File {
    val folderFile = context.getExternalFilesDir(folderName) ?: File(context.filesDir, folderName)
    if (!folderFile.exists()) {
        folderFile.mkdirs()
    }
    return folderFile
}

fun libFolderPath(folderName: String = "", context: Context = app()): String {
    return libFileFolder(folderName, context).absolutePath
}

/**缓存目录, 可以在应用详情中, 通过清理缓存清除.*/
fun libCacheFolderPath(folderName: String = "", context: Context = app()): String {
    val folderFile = File(context.externalCacheDir ?: context.cacheDir, folderName)
    if (!folderFile.exists()) {
        folderFile.mkdirs()
    }
    return folderFile.absolutePath
}

/**获取一个存储文件*/
fun libFile(name: String = fileNameUUID(), folderName: String = ""): File {
    return File(libFolderPath(folderName), name)
}

/**[libAppFolderFile]*/
fun libAppFile(name: String = fileNameUUID(), folderName: String = ""): File =
    File(libAppFolderFile(folderName), name)

/**获取一个缓存文件*/
fun libCacheFile(name: String = fileNameUUID(), folderName: String = ""): File {
    return File(libCacheFolderPath(folderName), name)
}

/**获取一个app文件目录文件对象*/
fun libAppFolderFile(folderName: String = ""): File {
    return appRootExternalFolder(folderName)
}

/**获取一个缓存目录文件对象*/
fun libCacheFolderFile(folderName: String = ""): File {
    return File(libCacheFolderPath(folderName))
}

/**创建一个文件对象*/
fun String.file(): File = File(this)

/**
 * 获取文件的MD5校验码
 *
 * @param file 文件
 * @return 文件的MD5校验码
 */
fun File.getFileMD5(algorithm: String = "MD5"): ByteArray? {
    var dis: DigestInputStream? = null
    try {
        val fis = FileInputStream(this)
        var md = MessageDigest.getInstance(algorithm)
        dis = DigestInputStream(fis, md)
        val buffer = ByteArray(1024 * 256)
        while (dis.read(buffer) > 0);
        md = dis.messageDigest
        return md.digest()
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        dis?.close()
    }
    return null
}

/**获取文件md5值*/
fun File.md5(algorithm: String = "MD5"): String? {
    return getFileMD5(algorithm)?.toHexString()
}

/**从路径中直接获取文件名*/
fun String?.fileNameByPath(): String? {
    if (this == null) {
        return null
    }
    val index = lastIndexOf('/')
    return substring(index + 1)
}

/**文件路径获取文件大小*/
fun String?.fileSize(def: Long = 0L): Long {
    if (TextUtils.isEmpty(this)) {
        return def
    }
    val file = this?.file()
    return if (file?.exists() == true) {
        file.length()
    } else {
        def
    }
}

fun generateFileName(name: String?, directory: File): File? {
    var newName = name ?: return null
    var file = File(directory, newName)
    if (file.exists()) {
        var fileName: String = newName
        var extension = ""
        val dotIndex = newName.lastIndexOf('.')
        if (dotIndex > 0) {
            fileName = newName.substring(0, dotIndex)
            extension = newName.substring(dotIndex)
        }
        var index = 0
        while (file.exists()) {
            index++
            newName = "$fileName($index)$extension"
            file = File(directory, newName)
        }
    }
    try {
        if (!file.createNewFile()) {
            return null
        }
    } catch (e: IOException) {
        e.printStackTrace()
        return null
    }
    return file
}