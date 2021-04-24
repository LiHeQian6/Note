package com.example.note_android.edit.fragment

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.note_android.R
import com.example.note_android.edit.util.SoftKeyBoardListener
import com.xuexiang.xui.widget.dialog.DialogLoader
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.core.MarkwonTheme
import io.noties.markwon.editor.MarkwonEditor
import io.noties.markwon.editor.MarkwonEditorTextWatcher
import kotlinx.android.synthetic.main.fragment_edit_note.view.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors

class EditNoteFragment: Fragment(),View.OnClickListener {

    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.fragment_edit_note,container,false)
        initTool()
        initEditor()
        initView()
        return rootView
    }

    private fun initEditor() {
        var markwon = Markwon.builder(requireContext()).usePlugin(object : AbstractMarkwonPlugin() {
            override fun configureTheme(builder: MarkwonTheme.Builder) {
                builder.headingBreakHeight(0)
            }
        }).build()
        var editor = MarkwonEditor.create(markwon)
        rootView.edit_markdown.addTextChangedListener(MarkwonEditorTextWatcher.withProcess(editor))
    }

    private fun initView() {
        rootView.preview_note.setOnClickListener(this)
        rootView.publish_note.setOnClickListener(this)
        rootView.mk_head.setOnClickListener(this)
        rootView.mk_bold.setOnClickListener(this)
        rootView.mk_list.setOnClickListener(this)

        rootView.edit_markdown.setText("## 什么是模式？\n" +
                "\n" +
                "- 模式是一条由三部分组成的规则\n" +
                "- 一个特定的环境，一个问题，一个解决方案\n" +
                "- 模式的核心思想：进行设计的复用\n" +
                "\n" +
                "<!--more-->\n" +
                "\n" +
                "## 六大原则\n" +
                "\n" +
                "### 开闭原则（Open Close Principle）\n" +
                "\n" +
                "> 由勃兰特·梅耶（Bertrand Meyer）提出：\"Software entities should be open for extension,but closed for modification\"\n" +
                "\n" +
                "- 含义：软件模块应该对扩展开放，对修改关闭\n" +
                "- 举例：当程序需要扩展新功能时，不应该修改原有代码，而是去增加新的代码\n" +
                "- 目的：使程序更具有扩展性，易于维护和升级\n" +
                "\n" +
                "### 里氏代换原则（Liskov Substitution Principle）\n" +
                "\n" +
                "- 含义：里氏代换原则是继承复用的基石，只有当衍生类可以替换掉基类，**软件单位的功能不受到影响时**，基类才能真正被复用，而衍生类也能够在基类的基础上增加新的行为\n" +
                "- 举例：球类，原本是一种体育用品，它的衍生类有篮球、足球、排球、羽毛球等等，如果衍生类替换了基类的原本方法，如把体育用品改成了食用品（那么软件单位的功能受到影响），就不符合里氏代换原则。\n" +
                "- 目的：对实现抽象化的具体步骤的规范\n" +
                "\n" +
                "### 依赖倒转原则（Dependence Inversion Principle）\n" +
                "\n" +
                "- 含义：针对接口编程，而不是针对实现编程\n" +
                "- 举例：以电脑计算机系统为例，主板，CPU，硬盘等都是根据接口设计的，如果根据实现来设计，将会出现跟换CPU同时需要更换主板的问题。\n" +
                "- 目的：降低模块间的耦合\n" +
                "\n" +
                "### 接口隔离原则（Interface Segregation Principle）\n" +
                "\n" +
                "- 含义：使用多个隔离的接口，比使用一个接口好\n" +
                "- 举例：登陆，注册应当写成两个接口，不应写成一个接口\n" +
                "- 目的：提高程序设计灵活性\n" +
                "\n" +
                "### 迪米特法则（最少知道原则）（Demeter Principle）\n" +
                "\n" +
                "- 含义：一个实体应当尽可能少的与其他实体产生联系，使得系统功能模块相对独立\n" +
                "- 举例：一个类publish成员越多，与其他类产生的联系与作用越多越强烈\n" +
                "- 目的：降低类与类之间的耦合，减少类之间的依赖\n" +
                "\n" +
                "### 单一职责原则（ Single responsibility principle ）\n" +
                "\n" +
                "- 含义：一个类理应只负责一个模块的功能\n" +
                "- 目的：降低类的复杂性，提高代码可读性，可维护性")
        rootView.edit_markdown.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                rootView.edit_note_subtitle_text.text = "最近保存 " + getSystemTime()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
    }

    private fun getSystemTime(): String {
        var simpleDateFormat = SimpleDateFormat("HH:mm:ss");
        //获取当前时间戳
        var date = Date(System.currentTimeMillis());
        return simpleDateFormat.format(date)
    }

    private fun initTool() {
        SoftKeyBoardListener.setListener(requireActivity(),object : SoftKeyBoardListener.OnSoftKeyBoardChangeListener{
            override fun keyBoardShow(height: Int) {
                rootView.input_tool.animate().translationY((-height).toFloat()).setDuration(300).start()
//                Toast.makeText(this@EditActivity, "键盘显示 高度" + height, Toast.LENGTH_SHORT).show();
            }
            override fun keyBoardHide(height: Int) {
                rootView.input_tool.animate().translationY((0).toFloat()).setDuration(0).start()
//                Toast.makeText(this@EditActivity, "键盘隐藏 高度" + height, Toast.LENGTH_SHORT).show();
            }
        })
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.preview_note -> {
                var manager =  requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(rootView.edit_markdown.windowToken,InputMethodManager.HIDE_NOT_ALWAYS)
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .addToBackStack(null)
                    .add(R.id.fragment_edit_note,PreviewNoteFragment(rootView.edit_markdown.text.toString()))
                    .commit()
            }
            R.id.publish_note -> {
                DialogLoader.getInstance().showConfirmDialog(
                    requireContext(),
                    "确定发布这篇文章吗",
                    "发布",
                    { dialog: DialogInterface?, which: Int ->
                        //TODO 去发布
                    },
                    "再想想",
                    { dialog: DialogInterface, which: Int ->
                        dialog.dismiss()
                    }
                )
            }
            R.id.mk_head -> {
                rootView.edit_markdown.editableText.insert(rootView.edit_markdown.selectionStart,"#")
            }
            R.id.mk_bold -> {
                rootView.edit_markdown.editableText.insert(rootView.edit_markdown.selectionStart,"****")
                rootView.edit_markdown.setSelection(rootView.edit_markdown.text.length - 2)
            }
            R.id.mk_list -> {
                rootView.edit_markdown.editableText.insert(rootView.edit_markdown.selectionStart,"-")
            }
        }
    }
}