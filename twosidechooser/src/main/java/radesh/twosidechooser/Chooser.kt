package radesh.twosidechooser

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.SeekBar

//https://github.com/tcqq/SearchView/blob/master/searchview/src/main/java/com/tcqq/searchview/widget/SearchView.kt
class Chooser : FrameLayout {
    private val tag = "Chooser"

    private lateinit var ivAccept : ImageView // icon imageView of accept
    private lateinit var ivIgnored : ImageView // icon imageView of reject
    private lateinit var ivArrowsLeft : ImageView // left arrow item
    private lateinit var ivArrowsRight : ImageView // right arrow item
    private lateinit var seekBar : SeekBar // progress view


    private var acceptValue : Int = 80 //after this value will called accept function
    private var ignoreValue : Int = 20 //after this value will called ignore function
    private var acceptAnimationValue : Int = 60 //after this value will played animation for near button
    private var ignoreAnimationValue : Int = 30 //after this value will played animation for near button
    private var centerProgress : Int = 50 //going to center when leave the thumb

    private lateinit var acceptDrawable : Drawable
    private lateinit var ignoreDrawable : Drawable
    private lateinit var arrowsDrawable : Drawable

    private var ignoreDrawableRotate : Float = 135f // this is rotate image to for call image

    //use two animation for fix bug that start both animation when is one side
    private lateinit var animAccept : Animation//animation of movement
    private lateinit var animIgnore : Animation//animation of movement

    private var isAcceptAnimEnd : Boolean = true //when dismiss thumb return to center
    private var isIgnoreAnimEnd : Boolean = true //when dismiss thumb return to center

    private var nearbyAnimatedButtons : Boolean = true // animation when near to buttons
    private var returnToCenter : Boolean = true //when dismiss thumb return to center

    private var listener: onSwipeEndListener? = null // listener for when return to end of progress



    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context,attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context,attrs)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context,attrs)
    }

    init {
        Log.e(tag,"fire init of view")

    }

    private fun init(context: Context,attrs: AttributeSet?=null){
        if (attrs!=null){
            attributesHandle(context.obtainStyledAttributes(attrs,R.styleable.Chooser))
        }
        LayoutInflater.from(context).inflate(R.layout.main,this,true)
        ivAccept = findViewById(R.id.ivAccept)
        ivIgnored = findViewById(R.id.ivIgnored)
        ivArrowsLeft = findViewById(R.id.ivArrowsLeft)
        ivArrowsRight = findViewById(R.id.ivArrowsRight)
        seekBar = findViewById(R.id.seekBar)

        ivAccept.setImageDrawable(acceptDrawable)
        ivIgnored.setImageDrawable(ignoreDrawable)
        ivArrowsLeft.setImageDrawable(arrowsDrawable)
        ivArrowsRight.setImageDrawable(arrowsDrawable)
        ivIgnored.rotation = ignoreDrawableRotate

        animAccept = AnimationUtils.loadAnimation(context, R.anim.shake)
        animIgnore = AnimationUtils.loadAnimation(context, R.anim.shake)

        animationHandle()

        moveThumbHandle()

    }

    /**
     * for get Attribute values from xml
     */
    private fun attributesHandle(typedArray: TypedArray) {
        Log.e(tag,"attributesHandle")
        acceptValue = typedArray.getInt(R.styleable.Chooser_acceptValue,acceptValue)
        ignoreValue = typedArray.getInt(R.styleable.Chooser_ignoreValue,ignoreValue)
        acceptAnimationValue = typedArray.getInt(R.styleable.Chooser_acceptAnimationValue,acceptAnimationValue)
        ignoreAnimationValue = typedArray.getInt(R.styleable.Chooser_ignoreAnimationValue,ignoreAnimationValue)
        centerProgress = typedArray.getInt(R.styleable.Chooser_centerProgress,centerProgress)
        nearbyAnimatedButtons = typedArray.getBoolean(R.styleable.Chooser_enableNearbyAnimation,nearbyAnimatedButtons)
        returnToCenter = typedArray.getBoolean(R.styleable.Chooser_enableReturnToCenter,returnToCenter)
        returnToCenter = typedArray.getBoolean(R.styleable.Chooser_enableReturnToCenter,returnToCenter)

        acceptDrawable = when {
            typedArray.getDrawable(R.styleable.Chooser_acceptDrawable)!=null -> typedArray.getDrawable(R.styleable.Chooser_acceptDrawable)!!
            else -> ResourcesCompat.getDrawable(resources,R.drawable.ic_accept,null)!!
        }
        ignoreDrawable = when {
            typedArray.getDrawable(R.styleable.Chooser_ignoreDrawable)!=null -> typedArray.getDrawable(R.styleable.Chooser_ignoreDrawable)!!
            else -> ResourcesCompat.getDrawable(resources,R.drawable.ic_accept,null)!!
        }
        arrowsDrawable = when {
            typedArray.getDrawable(R.styleable.Chooser_arrowsDrawable)!=null -> typedArray.getDrawable(R.styleable.Chooser_arrowsDrawable)!!
            else -> ResourcesCompat.getDrawable(resources,R.drawable.ic_arrows,null)!!
        }
        typedArray.recycle()
    }

    private fun animationHandle() {
        animAccept.setAnimationListener(object : AnimationListener{
            override fun onAnimationStart(p0: Animation?) {
                isAcceptAnimEnd = false
            }
            override fun onAnimationEnd(p0: Animation?) {
                isAcceptAnimEnd = true
            }
        })
        animIgnore.setAnimationListener(object : AnimationListener{
            override fun onAnimationStart(p0: Animation?) {
                isIgnoreAnimEnd = false
            }
            override fun onAnimationEnd(p0: Animation?) {
                isIgnoreAnimEnd = true
                Log.e(tag,"ended")
            }
        })
    }


    interface AnimationListener : Animation.AnimationListener {
        override fun onAnimationRepeat(p0: Animation?) {

        }

        override fun onAnimationEnd(p0: Animation?) {

        }

        override fun onAnimationStart(p0: Animation?) {

        }
    }

    private fun moveThumbHandle(){
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            private var startProgress = centerProgress

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                startProgress = seekBar.progress
            }

            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                when {
                    nearbyAnimatedButtons -> when {
                        seekBar.progress > acceptAnimationValue -> if (isAcceptAnimEnd) ivAccept.startAnimation(animAccept)
                        seekBar.progress < ignoreAnimationValue -> if (isIgnoreAnimEnd) ivIgnored.startAnimation(animIgnore)
                    }
                }

                when {
                    returnToCenter -> when {
                        Math.abs(startProgress - seekBar.progress) > 30 -> seekBar.progress = startProgress
                        else -> startProgress = seekBar.progress
                    }
                }
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                when {
                    seekBar.progress > acceptValue ->
                        if (listener!=null) listener?.onAccept()
                    seekBar.progress < ignoreValue ->
                        if (listener!=null) listener?.onIgnore()
                    else -> if (returnToCenter) seekBar.progress = centerProgress
                }


            }
        })
    }



    //change right image with res
    public fun setAcceptImage(res: Int){
        ivAccept.setImageResource(res)
    }
    //change right image with bitmap
    public fun setAcceptImage(bitmap: Bitmap){
        ivAccept.setImageBitmap(bitmap)
    }

    //change left image with res
    public fun setIgnoreImage(res: Int){
        ivIgnored.setImageResource(res)
    }
    //change left image with bitmap
    public fun setIgnoreImage(bitmap: Bitmap){
        ivIgnored.setImageBitmap(bitmap)
    }

    //change Arrows image with res
    public fun setArrowsImage(res: Int){
        ivArrowsLeft.setImageResource(res)
        ivArrowsRight.setImageResource(res)
    }
    /**
     * change Arrows image with bitmap
     */
    public fun setArrowsImage(bitmap: Bitmap){
        ivArrowsLeft.setImageBitmap(bitmap)
        ivArrowsRight.setImageBitmap(bitmap)
    }

    /**
     * for changing animation of Left and Right container
     */
    public fun setMovementAnimation(anim: Animation){
        this.animAccept = anim
        this.animIgnore = anim
    }

    /**
     * for showing animation when swipe near to buttons
     */
    public fun enableNearbyAnimtion(showAnimation: Boolean){
        this.nearbyAnimatedButtons = showAnimation
    }
    /**
     * for showing animation when swipe near to buttons
     */
    public fun enableReturnToCenter(returnToCenter: Boolean){
        this.returnToCenter = returnToCenter
    }

    /**
     * for feed listener when swipe to end or right
     */
    public fun setOnSwipeEndListener(onSwipeEndListener: onSwipeEndListener){
        listener = onSwipeEndListener
    }

    /**
     * set progress to center and move thumb to center
     */
    public fun goToCenter(){
        seekBar.progress = centerProgress
    }

    /**
     * for disable rotate ignore imageView
     */
    public fun disableRotateImage(){
        ignoreDrawableRotate = 0f
        invalidate()
    }


}