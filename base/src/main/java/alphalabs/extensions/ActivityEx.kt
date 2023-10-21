package alphalabs.extensions

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import alphalabs.base.R


fun AppCompatActivity.hasPermissions(vararg permissions: String): Boolean {
    var hasPermission = true
    permissions.forEach {
        if (ActivityCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED)
            hasPermission = false
    }
    return hasPermission
}

fun AppCompatActivity.hasPermission(permission: String): Boolean {
    return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

fun AppCompatActivity.createAppDetailLauncher(callback: (ActivityResult) -> Unit): ActivityResultLauncher<Intent> {
    return registerForActivityResult(ActivityResultContracts.StartActivityForResult(), callback)
}

fun AppCompatActivity.createPermissionLauncher(callback: (Boolean) -> Unit): ActivityResultLauncher<String> {
    return registerForActivityResult(ActivityResultContracts.RequestPermission(), callback)
}

fun AppCompatActivity.createResultLauncher(resultCallback: ((result: ActivityResult) -> Unit)): ActivityResultLauncher<Intent> {
    return registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        resultCallback
    )
}

fun AppCompatActivity.startActivity(
    className: Class<*>,
    launcher: ActivityResultLauncher<Intent>? = null
) {
    val intent = Intent(this, className)
    if (launcher == null) startActivity(intent)
    else launcher.launch(intent)
}

fun FragmentActivity.createResultLauncher(resultCallback: ((result: ActivityResult) -> Unit)): ActivityResultLauncher<Intent> {
    return registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        resultCallback
    )
}

fun FragmentActivity.startActivity(
    className: Class<*>,
    launcher: ActivityResultLauncher<Intent>? = null
) {
    val intent = Intent(this, className)
    if (launcher == null) startActivity(intent)
    else launcher.launch(intent)
}

fun AppCompatActivity.delay(duration: Long = 250, callback: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed({
        if (!isFinishing && !isDestroyed) callback.invoke()
    }, duration)
}

fun AppCompatActivity.shareText(subject: String, content: String) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "text/plain"
    intent.putExtra(Intent.EXTRA_SUBJECT, subject)
    intent.putExtra(Intent.EXTRA_TEXT, content)
    startActivity(Intent.createChooser(intent, getString(R.string.select_app_to_share)))
}

fun AppCompatActivity.openUrl(url: String) {
    runCatching {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }.getOrElse {
        Toast.makeText(this, getString(R.string.can_not_open_url), Toast.LENGTH_LONG).show()
    }
}