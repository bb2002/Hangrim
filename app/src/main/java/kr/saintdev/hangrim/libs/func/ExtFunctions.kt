package kr.saintdev.hangrim.libs.func

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Vibrator
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import com.fsn.cauly.CaulyAdInfoBuilder
import com.fsn.cauly.CaulyCloseAd
import com.fsn.cauly.CaulyInterstitialAd
import com.fsn.cauly.CaulyInterstitialAdListener
import kr.saintdev.hangrim.R
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

fun Int.str(context: Context) = context.resources.getString(this)


/**
 * 12.26 2018
 * Alert 창을 발생한다.
 */
fun String.alert(msg: String,
                 context: Context,
                 postiveListener: DialogInterface.OnClickListener = DialogInterface.OnClickListener{dialogInterface, _ ->  dialogInterface.dismiss() },
                 nagativeListener: DialogInterface.OnClickListener = DialogInterface.OnClickListener{dialogInterface, _ ->  dialogInterface.dismiss() }) {
    val builder = AlertDialog.Builder(context)
    builder.setTitle(this)
    builder.setMessage(msg)
    builder.setPositiveButton("Yes", postiveListener)
    builder.setNegativeButton("No", nagativeListener)
    builder.show()
}

fun Int.alert(msg: Int,
              context: Context,
              postiveListener: DialogInterface.OnClickListener = DialogInterface.OnClickListener{dialogInterface, _ ->  dialogInterface.dismiss() },
              nagativeListener: DialogInterface.OnClickListener = DialogInterface.OnClickListener{dialogInterface, _ ->  dialogInterface.dismiss() }) {
    val res = context.resources
    res.getString(this).alert(res.getString(msg), context, postiveListener, nagativeListener)
}

fun Bitmap.save(path: File) : Boolean {
    val fos: FileOutputStream

    return try {
        fos = FileOutputStream(path.absolutePath)
        this.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.close()
        true
    } catch(ex: Exception) {
        ex.printStackTrace()
        false
    }
}

fun File.share(context: Context) {
    val targetImage = FileProvider.getUriForFile(context, context.packageName + ".fileprovider", this)
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "image/*"
    intent.putExtra(Intent.EXTRA_STREAM, targetImage)
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    context.startActivity(Intent.createChooser(intent, "Select!"))
}

fun Context.saveInGallery(targetFile: File, newFile: File) {
    targetFile.copyTo(newFile, true, 1024)

    val values = ContentValues()
    values.put(MediaStore.Images.Media.DATA, newFile.absolutePath)
    values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
    this.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
}

fun getDateText() : String {
    val format = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.KOREA)
    return format.format(Date())
}

fun Int.vibration(context: Context)  = (context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).vibrate(this.toLong())

object Ads {
    const val AD_OPEN_RD = 30           // 광고 노출 확률
    fun isOpenAds() = Random().nextInt(99) < AD_OPEN_RD

    fun createAds(context: AppCompatActivity, listener: CaulyInterstitialAdListener) : CaulyInterstitialAd {
        val code = R.string.cauly_key.str(context)

        val adInfo = CaulyAdInfoBuilder(code).build()
        val interstial = CaulyInterstitialAd()
        interstial.setAdInfo(adInfo)
        interstial.setInterstialAdListener(listener)
        interstial.requestInterstitialAd(context)
        return interstial
    }

    fun createADRandom(context: AppCompatActivity, listener: CaulyInterstitialAdListener) = if(isOpenAds()) createAds(context, listener) else null
}

object Permission {
    fun isGratedPermission(context: Context) : Boolean {
        val perm = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return perm != PackageManager.PERMISSION_DENIED
    }

    fun requestPermission(context: Activity) {
        if(!isGratedPermission(context)) {
            ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 0x1)
        }
    }
}