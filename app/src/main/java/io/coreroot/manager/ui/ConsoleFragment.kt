package io.coreroot.manager.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import io.coreroot.manager.R
import io.coreroot.manager.nat.NativeBridge

class ConsoleFragment : Fragment(R.layout.fragment_console) {

    private lateinit var input: TextInputEditText
    private lateinit var output: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        input = view.findViewById(R.id.console_input)
        output = view.findViewById(R.id.console_output)
        val run = view.findViewById<MaterialButton>(R.id.console_run)
        val clear = view.findViewById<MaterialButton>(R.id.console_clear)

        run.setOnClickListener { exec() }
        clear.setOnClickListener {
            output.text = getString(R.string.console_output)
        }
    }

    private fun exec() {
        val cmd = input.text.toString().trim()
        if (cmd.isEmpty()) return

        // 原生调用放到后台线程，避免阻塞 UI；C 层已做异常兜底。
        Thread {
            val result = runCatching { NativeBridge.nativeExec(cmd) }
                .getOrDefault("rc=-1\nnative bridge error")
            val rc = NativeBridge.parseExit(result)
            val out = NativeBridge.parseOutput(result).ifEmpty { "(无输出)" }

            activity?.runOnUiThread {
                val block = "$ $cmd\n[rc=$rc] $out\n\n"
                output.append(block)
                input.setText("")
            }
        }.start()
    }
}
