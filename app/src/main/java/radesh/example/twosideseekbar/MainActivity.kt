package radesh.example.twosideseekbar

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import radesh.twosidechooser.OnSwipeEndListener

class MainActivity : AppCompatActivity() {
    private val tag = "chooserExample"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chooser.setOnSwipeEndListener(object : OnSwipeEndListener{
            override fun onAccept() {
                Log.e(tag,"Accepted")
            }

            override fun onIgnore() {
                Log.e(tag,"Ignored")
            }
        })

        //approved promptly when you swipe to end or start
        chooser.enableDoWithoutStopTracking(true)
        //sets acceptValue to 100 and calculate ignore value (ignoreValue = 0)
        chooser.setAcceptFinalValue(100,true)

//        //sets acceptValue to 80 and calculate ignore value (ignoreValue = 20)
//        chooser.setAcceptValue(80,true)
//        // when enter this value animation will be start (when arrive above 60 and below 40)
//        chooser.setAcceptAnimationValue(60,true)
//        // change animation when arrive animation value(set above)
//        chooser.setMovementAnimation(customAnimation)
//        //if you want hide arrows use 0f
//        chooser.setArrowsImagesAlpha(0.8f)
//        //set drawable padding to look better
//        chooser.setDrawablePadding(resources.getDimensionPixelOffset(R.dimen.padding))
//        //set chooser background
//        chooser.setChooserBackgroundColor(resources.getColor(R.color.colorAccent))


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
