package radesh.example.twosideseekbar

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import radesh.twosidechooser.onSwipeEndListener

class MainActivity : AppCompatActivity() {
    private val tag = "chooserExample"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chooser.setOnSwipeEndListener(object : onSwipeEndListener{
            override fun onIgnore() {
                Log.e(tag,"Ignored")
                showToast("Ignored")
            }

            override fun onAccept() {
                showToast("Accepted")
                Log.e(tag,"Accepted")
            }

        })
        chooser.enableDoWithoutStopTracking(false)
        btnSmoothReturnToCenter.setOnClickListener { chooser.smoothReturnToCenter() }
        btnChangeBgColor.setOnClickListener {
            chooser.setChooserBackgroundColor(Color.BLACK)
            chooser.setAcceptBackgroundColor(Color.BLACK)
            chooser.setIgnoreBackgroundColor(Color.BLACK)
        }
        btnChangeThumb.setOnClickListener {
            chooser.setThumbSize(180,180)
        }


        btnClicks()

    }

    private fun btnClicks() {
        btnExample1.setOnClickListener {
            goTo(Example1Activity::class.java)
        }
        btnExample2.setOnClickListener {
            goTo(Example2Activity::class.java)
        }
    }

    public fun Activity.showToast(msg:String){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
    }

    fun <T> Activity.goTo(cls:Class<T>){
        startActivity(Intent(this,cls))
    }
}
