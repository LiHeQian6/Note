package com.example.note_android.setting.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.note_android.R
import com.example.note_android.bean.UserInfo
import com.example.note_android.util.HttpAddressUtil
import com.example.note_android.util.StateUtil
import com.google.gson.Gson
import com.xuexiang.xui.widget.dialog.DialogLoader
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog.SingleButtonCallback
import com.xuexiang.xui.widget.dialog.strategy.InputInfo
import kotlinx.android.synthetic.main.change_fragment.*
import kotlinx.android.synthetic.main.fragment_follow.view.*
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.IOException

class ChangeFragment : Fragment(),View.OnClickListener {

    companion object {
        const val CHANGE_NAME = 0
        const val CHANGE_GRAPH = 1

        fun newInstance() = ChangeFragment()
    }

    private lateinit var viewModel: ChangeViewModel
    private lateinit var handler: Handler
    private lateinit var newUserInfo: UserInfo

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.change_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(ChangeViewModel::class.java)
        handler = object: Handler(){
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                var result = JSONObject(msg.obj.toString())
                if (result != null && result.get("status") == "SUCCESS"){
                    Toast.makeText(requireContext(), "修改成功", Toast.LENGTH_SHORT).show()
                    StateUtil.SYSTEM_USER_INFO = newUserInfo
                    requireActivity().finish()
                }else{
                    Toast.makeText(requireContext(), "网络出了点问题", Toast.LENGTH_SHORT).show()
                }
            }
        }
        initView()
        initListener()
    }

    private fun initView() {
        change_user_name.text = StateUtil.SYSTEM_USER_INFO?.nickName
        change_user_autograph.text = StateUtil.SYSTEM_USER_INFO?.introduction
    }

    private fun initListener() {
        change_user_name.setOnClickListener(this)
        change_user_autograph.setOnClickListener(this)
        save_change_button.setOnClickListener(this)
    }

    private fun showDialog(type: Int){

        var hint:String
        var title:String

        when(type){
            CHANGE_NAME -> {
                hint = "请输入新的昵称"
                title = "修改昵称"
            }
            CHANGE_GRAPH -> {
                hint = "请输入新的签名"
                title = "修改签名"
            }
            else -> {
                hint = "参数不合法"
                title = "参数不合法"
            }
        }
        MaterialDialog.Builder(requireContext())
                .iconRes(R.drawable.rv_divider)
                .title(title)
                .content("")
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input(
                        hint,
                        ""
                ) { _, _ -> }
                .inputRange(1, 15)
                .positiveText("确定")
                .negativeText("取消")
                .onPositive(SingleButtonCallback {
                    dialog: MaterialDialog, which: DialogAction? ->
                    if (type == CHANGE_NAME)
                        change_user_name.text = dialog.inputEditText?.text
                    else
                        change_user_autograph.text = dialog.inputEditText?.text
                    dialog.dismiss()
                })
                .show();
//        DialogLoader.getInstance().showInputDialog(
//                requireContext(),
//                R.drawable.rv_divider,
//                title,
//                "",
//                InputInfo(InputType.TYPE_TEXT_FLAG_CAP_WORDS, hint),
//                null,
//                "确定",
//                DialogInterface.OnClickListener { dialog, which ->
//
//                },
//                "取消",
//                null)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.change_user_name -> {
                showDialog(CHANGE_NAME)
            }
            R.id.change_user_autograph -> {
                showDialog(CHANGE_GRAPH)
            }
            R.id.save_change_button -> {
                save()
            }
        }
    }

    private fun save() {
        val httpClient = OkHttpClient()
        val urlBuilder = HttpAddressUtil.getChangeInfo().toHttpUrlOrNull()!!.newBuilder()
        var mediaType = "application/json".toMediaTypeOrNull()
        var json = JSONObject()
        newUserInfo = StateUtil.SYSTEM_USER_INFO!!
        newUserInfo.nickName = change_user_name.text.toString()
        newUserInfo.introduction = change_user_autograph.text.toString()
        var gson = Gson()
        json.put("user", gson.toJson(newUserInfo))
        var requestBody = RequestBody.create(mediaType, json.get("user").toString())
        val request = Request.Builder()
                .addHeader(resources.getString(R.string.Authorization), StateUtil.AUTHORIZATION)
                .addHeader(resources.getString(R.string.Authorization_Header), StateUtil.AUTHORIZATION_HEADERS)
                .post(requestBody)
                .url(urlBuilder.build()).build()
        httpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Looper.prepare()
                Toast.makeText(requireContext(), "网络出了点问题", Toast.LENGTH_SHORT).show()
                Looper.loop()
            }

            override fun onResponse(call: Call, response: Response) {
                var result: String? = response.body?.string()
                if (result == null || result == "") {
                    Toast.makeText(requireContext(), "网络出了点问题,请重试", Toast.LENGTH_SHORT).show()
                    return
                }
                var mess = Message()
                mess.obj = result
                handler.sendMessage(mess)
            }
        })
    }
}