package com.ciputramitra

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import java.io.File
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object FormatCustom {
	fun getCurrentIdr(price: Double): String {
		val format = DecimalFormat("#,###,###")
		return "Rp. " + format.format(price).replace(",", ".")
	}
	
	fun formatDate(dateString: String): String {
		val originalFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
		val date = LocalDateTime.parse(dateString, originalFormat)
		
		val outputFormat = DateTimeFormatter.ofPattern("EEEE , dd MMMM , yyyy , HH:mm", Locale("id", "ID"))
		return date.format(outputFormat)
	}
	
	fun Bitmap.saveImageToGallery(context: Context) : Uri? {
		val contentValues = ContentValues().apply {
			put(MediaStore.Images.Media.DISPLAY_NAME, "image_${System.currentTimeMillis()}.png")
			put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg/png/jpg")
			put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
		}
		val uri : Uri? = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
		
		uri?.let {
			context.contentResolver.openOutputStream(it)?.use { outputStream ->
				compress(Bitmap.CompressFormat.PNG, 100, outputStream)
			}
		}
		return uri
	}
	fun getFileFromUri(context : Context, uri : Uri): File? {
		val contentResolver = context.contentResolver
		val fileName = getFileName(context, uri) ?: return null

//        Buat file baru di direktori internal aplikasi
		val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName)
		
		try{
			contentResolver.openInputStream(uri)?.use { inputStream ->
				file.outputStream().use { outputStream ->
					inputStream.copyTo(outputStream)
				}
			}
			return file
		} catch (e: Exception) {
			e.printStackTrace()
		}
		return null
	}
	
	@SuppressLint("Recycle")
	private fun getFileName(context : Context, uri : Uri) : String? {
		var fileName : String? = null
		val cursor = context.contentResolver.query(uri , null, null ,null, null)
		cursor?.use {
			if (it.moveToFirst()) {
				val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
				if (nameIndex >= 0) {
					fileName = it.getString(nameIndex)
				}
			}
		}
		return fileName
	}
	
	const val LOCATION_INTERVAL : Long = 1000
	const val LOCATION_FAST_INTERVAL : Long = 2000
	const val LOCATION_MAX_WITH_TIME : Long = 12000
	
	
	
}