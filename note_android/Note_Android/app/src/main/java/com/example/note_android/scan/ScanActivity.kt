package com.example.note_android.scan

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import android.view.View
import com.example.note_android.R
import com.xuexiang.xqrcode.XQRCode
import com.xuexiang.xqrcode.ui.CaptureActivity
import com.xuexiang.xqrcode.util.QRCodeAnalyzeUtils
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog
import kotlinx.android.synthetic.main.activity_scan.*


class ScanActivity : CaptureActivity(),View.OnClickListener {

    private var ifFlashOn = false

    /**
     * 开始二维码扫描
     *
     * @param activity
     * @param requestCode 请求码
     * @param theme       主题
     */
    companion object{
        fun start(activity: Activity, requestCode: Int, theme: Int) {
            val intent = Intent(
                activity,
                ScanActivity::class.java
            )
            intent.putExtra(KEY_CAPTURE_THEME, theme)
            activity.startActivityForResult(intent, requestCode)
        }
    }

    override fun initCaptureFragment() {
        super.initCaptureFragment()
    }

    override fun getCaptureLayoutId(): Int {
        return R.layout.activity_scan
    }

    override fun onCameraInitFailed() {
        super.onCameraInitFailed()
    }

    override fun beforeCapture() {
        super.beforeCapture()
    }

    override fun onCameraInitSuccess() {
        iv_back.setOnClickListener(this)
        iv_photo.setOnClickListener(this)
        iv_flash_light.setOnClickListener(this)
    }

    override fun handleRequestPermissionDeny() {
        super.handleRequestPermissionDeny()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun handleAnalyzeFailed() {
        MaterialDialog.Builder(this).content("扫码失败").show()
    }

    override fun handleAnalyzeSuccess(bitmap: Bitmap?, result: String?) {
         MaterialDialog.Builder(this).content(result.toString()).show()
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.iv_back -> {
                finish()
            }
            R.id.iv_photo -> {
                //TODO 打开图库
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.type = "image/*"
                startActivityForResult(intent, 123)
            }
            R.id.iv_flash_light -> {
                if(ifFlashOn) {
                    iv_flash_light.setImageDrawable(resources.getDrawable(R.drawable.ico_flashlight_off))
                }else
                    iv_flash_light.setImageDrawable(resources.getDrawable(R.drawable.ico_flashlight_on))
                ifFlashOn = !ifFlashOn
                XQRCode.switchFlashLight(ifFlashOn)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 123) {
            if (data != null) {
                var uri = data.getData();
                var cr = getContentResolver();
                try {
                    var mBitmap = MediaStore.Images.Media.getBitmap(cr, uri);//显得到bitmap图片
                    XQRCode.analyzeQRCode(mBitmap, object : QRCodeAnalyzeUtils.AnalyzeCallback {
                        override fun onAnalyzeSuccess(bitmap: Bitmap?, result: String?) {
                            MaterialDialog.Builder(this@ScanActivity).content(result.toString()).show()
                        }

                        override fun onAnalyzeFailed() {
                            MaterialDialog.Builder(this@ScanActivity).content("扫码失败").show()
                        }
                    })
                    if (mBitmap != null) {
                        mBitmap.recycle();
                    }
                } catch (e: Exception) {
                    e.printStackTrace();
                }
            }
        }
    }
}