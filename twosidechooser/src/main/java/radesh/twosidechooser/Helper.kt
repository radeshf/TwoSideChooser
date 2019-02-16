package radesh.twosidechooser

import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.v4.content.res.ResourcesCompat
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.view.View
import android.view.animation.BounceInterpolator
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import android.widget.SeekBar
import java.time.Duration

fun SeekBar.smoothSetProgress(progress: Int,duration: Long=150 , interpolator:Interpolator = FastOutSlowInInterpolator()){
    val anim = ObjectAnimator.ofInt(this,"progress",this.progress,progress)
    anim.repeatCount = 0
    anim.duration = duration
    anim.interpolator = interpolator
    anim.start()
}

fun View.getColor(id: Int) : Int {
    return ResourcesCompat.getColor(resources,id,null)
}

fun View.getDrawable(id: Int) : Drawable? {
    return ResourcesCompat.getDrawable(resources,id,null)
}
