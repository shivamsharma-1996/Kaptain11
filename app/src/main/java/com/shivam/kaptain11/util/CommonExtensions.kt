package com.shivam.kaptain11.util

import android.Manifest
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar


fun Activity.goToActivity(newActivity: Class<*>) {
    val intent = Intent(this, newActivity)
    startActivity(intent)
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.shareIntent(){
    val shareIntent = Intent()
    shareIntent.action = Intent.ACTION_SEND
    shareIntent.type = "text/plain"
    shareIntent.putExtra(Intent.EXTRA_TEXT, "Share via");
    this.startActivity(Intent.createChooser(shareIntent, ""))
}


fun Context.dismissKeyboard(view: View?) {
    view?.let {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(it.windowToken, 0)
    }
}

fun Activity.hasPermissionForImage(permissionCodeRead: Int, permissionCodeWrite: Int) : Boolean{
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if ((this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
            && (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
        ) {
            val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            val permissionCoarse = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

            this.requestPermissions(permission, permissionCodeRead) // GIVE AN INTEGER VALUE FOR PERMISSION_CODE_READ LIKE 1001
            this.requestPermissions(permissionCoarse, permissionCodeWrite) // GIVE AN INTEGER VALUE FOR PERMISSION_CODE_WRITE LIKE 1002
            return false
        } else {
            return true
        }
    }
    return false
}


fun View.snackbar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).also { snackbar ->
        snackbar.setAction("Ok") {
            snackbar.dismiss()
        }
    }.show()
}

fun Context.copyToClipboard(text: String){
    var myClipboard = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    var myClip: ClipData = ClipData.newPlainText("note_copy", text)
    myClipboard.setPrimaryClip(myClip)
    myClipboard.addPrimaryClipChangedListener {
        showToast("Code Copied")
    }
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.hide() {
    visibility = View.GONE
}
