package com.example.findpath

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import java.io.File
import java.io.FileOutputStream

class Screenshot {
    fun takeScreenshotOfView(view: View, height: Int, width: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val bgDrawable = view.background
        if (bgDrawable != null) {
            bgDrawable.draw(canvas)
        } else {
            canvas.drawColor(Color.WHITE)
        }
        view.draw(canvas)
        return bitmap
    }

    fun saveImage(finalBitmap: Bitmap, imageName: String, contentResolver: ContentResolver) {
        val root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()
        val myDir = File(root)
        myDir.mkdirs()
        val fileName = "screenshot-$imageName.jpg"
        val file = File(myDir, fileName)
        if (file.exists()) {
            println("File with that name exists")
            return
        }
        try {
            val out = FileOutputStream(file)
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            MediaStore.Images.Media.insertImage(
                contentResolver,
                finalBitmap,
                imageName,
                "Image of $imageName"
            )
            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}