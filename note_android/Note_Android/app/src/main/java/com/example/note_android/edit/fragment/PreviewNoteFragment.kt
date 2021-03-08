package com.example.note_android.edit.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.note_android.R
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.SoftBreakAddsNewLinePlugin
import io.noties.markwon.core.MarkwonTheme
import io.noties.markwon.editor.MarkwonEditor
import kotlinx.android.synthetic.main.fragment_preview_note.view.*

class PreviewNoteFragment(var noteContent: String): Fragment() {

    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_preview_note,container,false)

        initMarkDown()

        return rootView
    }

    private fun initMarkDown() {
        var markwon = Markwon.builder(requireContext())
            .usePlugin(SoftBreakAddsNewLinePlugin())
            .usePlugin(object : AbstractMarkwonPlugin() {
            override fun configureTheme(builder: MarkwonTheme.Builder) {
                builder.headingBreakHeight(0)
                    .bulletWidth(resources.getDimension(R.dimen.dp_6).toInt())
            }
        }).build()
        markwon.setMarkdown(rootView.show_markdown,noteContent)
    }
}