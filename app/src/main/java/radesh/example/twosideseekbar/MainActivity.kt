package radesh.example.twosideseekbar

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_main.*
import radesh.twosidechooser.onSwipeEndListener

class MainActivity : AppCompatActivity() {
    private val tag = "chooserExample"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        chooser.setOnSwipeEndListener(object : onSwipeEndListener{

            override fun onIgnore() {
                Log.e(tag,"onIgnore")
            }

            override fun onAccept() {
                Log.e(tag,"onAccept")
            }

        })

    }
}
