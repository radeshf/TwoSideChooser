package radesh.twosidechooser

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.IntRange
import android.support.annotation.RequiresApi
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.Interpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.SeekBar
import android.graphics.drawable.GradientDrawable
import android.widget.LinearLayout
import android.util.DisplayMetrics




/**
 * created by Radesh Farokh Manesh
 */
class Chooser : FrameLayout {
    private val tag = "Chooser"

    private lateinit var ivAccept : ImageView // icon imageView of accept
    private lateinit var ivIgnored : ImageView // icon imageView of reject
    private lateinit var ivArrowsLeft : ImageView // left arrow item
    private lateinit var ivArrowsRight : ImageView // right arrow item
    private lateinit var seekBar : SeekBar // progress view
    private lateinit var chooserBackground : LinearLayout // background of progress view

    private var chooserBackgroundColor : Int = 0 // background color of progress view


    private var acceptValue : Int = 85 //after this value will called accept function
    private var ignoreValue : Int = 15 //after this value will called ignore function
    private var acceptFinalValue : Int = 100 //after this value will called accept function
    private var ignoreFinalValue : Int = 0 //after this value will called ignore function
    private var acceptAnimationValue : Int = 60 //after this value will played animation for near button
    private var ignoreAnimationValue : Int = 30 //after this value will played animation for near button
    private var centerProgress : Int = 50 //going to center when leave the thumb
    private var maxValue : Int = 100 //maximum progress

    private lateinit var thumbDrawable : Drawable

    private lateinit var acceptDrawable : Drawable
    private lateinit var ignoreDrawable : Drawable
    private lateinit var ignoreBackgroundDrawable : Drawable
    private lateinit var acceptBackgroundDrawable : Drawable
    private var ignoreBackgroundColor : Int = 0
    private var acceptBackgroundColor : Int = 0
    private lateinit var arrowsDrawable : Drawable
    private var arrowsDrawableRotation : Float = 0f //this is rotation of arrows
    private var acceptDrawableRotation : Float = 0f // this is rotate image to accept imageView
    private var ignoreDrawableRotation : Float = 0f // this is rotate image to ignore imageView
    private var drawablePadding : Int = 10 // padding of drawables accept and ignore
    private var acceptDrawableSize : Int = 40 // size of accept drawable
    private var ignoreDrawableSize : Int = 40 // size of ignore drawable

    //use two animation for fix bug that start both animation when is one side
    private lateinit var animAccept : Animation//animation of movement

    private lateinit var animIgnore : Animation//animation of movement
    private var isAcceptAnimEnd : Boolean = true //when dismiss thumb return to center

    private var isIgnoreAnimEnd : Boolean = true //when dismiss thumb return to center

    private var doWithoutStopTracking : Boolean = false //fire interface with out stop Touching Thumb
    private var selectWithClick : Boolean = false //can fire interface and move thumb with click on view
    private var selectWithClickRange : Int = 30 //this range of click prevent

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

    /**
     * for get Attribute values from xml
     */
    private fun attributesHandle(typedArray: TypedArray) {
        acceptValue = typedArray.getInt(R.styleable.Chooser_acceptValue,acceptValue)
        ignoreValue = typedArray.getInt(R.styleable.Chooser_ignoreValue,ignoreValue)
        acceptAnimationValue = typedArray.getInt(R.styleable.Chooser_acceptAnimationValue,acceptAnimationValue)
        ignoreAnimationValue = typedArray.getInt(R.styleable.Chooser_ignoreAnimationValue,ignoreAnimationValue)
        centerProgress = typedArray.getInt(R.styleable.Chooser_centerProgress,centerProgress)
        nearbyAnimatedButtons = typedArray.getBoolean(R.styleable.Chooser_enableNearbyAnimation,nearbyAnimatedButtons)
        returnToCenter = typedArray.getBoolean(R.styleable.Chooser_enableReturnToCenter,returnToCenter)
        arrowsDrawableRotation = typedArray.getFloat(R.styleable.Chooser_arrowsDrawableRotation,arrowsDrawableRotation)
        acceptDrawableRotation = typedArray.getFloat(R.styleable.Chooser_acceptDrawableRotation,acceptDrawableRotation)
        ignoreDrawableRotation = typedArray.getFloat(R.styleable.Chooser_ignoreDrawableRotation,ignoreDrawableRotation)
        drawablePadding = typedArray.getDimensionPixelSize(R.styleable.Chooser_drawablesPadding,drawablePadding)
        acceptDrawableSize = typedArray.getDimensionPixelSize(R.styleable.Chooser_acceptDrawableSize,convertDpToPixel(acceptDrawableSize))
        ignoreDrawableSize = typedArray.getDimensionPixelSize(R.styleable.Chooser_ignoreDrawableSize,convertDpToPixel(ignoreDrawableSize))

        acceptDrawable = when {
            typedArray.getDrawable(R.styleable.Chooser_acceptDrawable)!=null -> typedArray.getDrawable(R.styleable.Chooser_acceptDrawable)!!
            else -> getDrawable(R.drawable.ic_accept)!!
        }

        ignoreDrawable = when {
            typedArray.getDrawable(R.styleable.Chooser_ignoreDrawable)!=null -> typedArray.getDrawable(R.styleable.Chooser_ignoreDrawable)!!
            else -> getDrawable(R.drawable.ic_accept)!!
        }

        arrowsDrawable = when {
            typedArray.getDrawable(R.styleable.Chooser_arrowsDrawable)!=null -> typedArray.getDrawable(R.styleable.Chooser_arrowsDrawable)!!
            else -> getDrawable(R.drawable.ic_arrows)!!
        }

        thumbDrawable = when {
            typedArray.getDrawable(R.styleable.Chooser_thumbDrawable)!=null -> typedArray.getDrawable(R.styleable.Chooser_thumbDrawable)!!
            else -> getDrawable(R.drawable.ic_pin)!!
        }

        ignoreBackgroundDrawable = when {
            typedArray.getDrawable(R.styleable.Chooser_ignoreBackgroundDrawable)!=null -> typedArray.getDrawable(R.styleable.Chooser_ignoreBackgroundDrawable)!!
            else -> getDrawable(R.drawable.chooser_border_ignore)!!
        }

        acceptBackgroundDrawable = when {
            typedArray.getDrawable(R.styleable.Chooser_acceptBackgroundDrawable)!=null -> typedArray.getDrawable(R.styleable.Chooser_acceptBackgroundDrawable)!!
            else -> getDrawable(R.drawable.border_accept)!!
        }
        ignoreBackgroundColor = typedArray.getColor(R.styleable.Chooser_ignoreBackgroundColor,getColor(R.color.red))
        acceptBackgroundColor = typedArray.getColor(R.styleable.Chooser_acceptBackgroundColor,getColor(R.color.green))
        chooserBackgroundColor = typedArray.getColor(R.styleable.Chooser_chooserBackgroundColor,getColor(R.color.seekbar_bg))
        typedArray.recycle()
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
        chooserBackground = findViewById(R.id.backgroundContainer)

        ivAccept.setImageDrawable(acceptDrawable)
        ivIgnored.setImageDrawable(ignoreDrawable)
        ivArrowsLeft.setImageDrawable(arrowsDrawable)
        ivArrowsRight.setImageDrawable(arrowsDrawable)
        ivIgnored.rotation = ignoreDrawableRotation
        ivAccept.rotation = acceptDrawableRotation

        ivArrowsLeft.rotation = arrowsDrawableRotation
        ivArrowsRight.rotation = arrowsDrawableRotation + 180

        seekBar.thumb = thumbDrawable

        setAcceptBackgroundDrawable(acceptBackgroundDrawable)
        setIgnoreBackgroundDrawable(ignoreBackgroundDrawable)
        setAcceptBackgroundColor(acceptBackgroundColor)
        setIgnoreBackgroundColor(ignoreBackgroundColor)
        setChooserBackgroundColor(chooserBackgroundColor)

        animAccept = AnimationUtils.loadAnimation(context, R.anim.shake)
        animIgnore = AnimationUtils.loadAnimation(context, R.anim.shake)

        setDrawablePadding(drawablePadding)
        setAcceptDrawableSize(acceptDrawableSize)
        setIgnoreDrawableSize(ignoreDrawableSize)


        animationHandle()

        moveThumbHandle()

    }

    private fun convertDpToPixel(dp: Int): Int {
        return Math.round(dp * (resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT))
    }

    /**
     * for fix bug mess up animations
     */
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
            }
        })
    }

    /**
     * for handling thumb movement
     * call interface function, start animation, return to center
     */
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
//                if (!selectWithClick){
//                    if (doWithoutStopTracking){
//                        when {
//                            Math.abs(startProgress - seekBar.progress) < maxValue - acceptFinalValue -> seekBar.progress = startProgress
//                            Math.abs(startProgress - seekBar.progress) < ignoreFinalValue -> seekBar.progress = startProgress
//                            else -> startProgress = seekBar.progress
//                        }
//                    }else{
//                        when {
//                            startProgress - seekBar.progress >= centerProgress - ignoreValue -> seekBar.progress = startProgress
//                            startProgress - seekBar.progress <=  centerProgress - acceptValue -> seekBar.progress = startProgress
//                            else -> startProgress = seekBar.progress
//                        }
//                    }
//                }
                when {
                    !selectWithClick -> when {
                        Math.abs(startProgress - seekBar.progress) > selectWithClickRange -> seekBar.progress = startProgress
                        else -> startProgress = seekBar.progress
                    }
                }

                when {
                    doWithoutStopTracking -> when {
                        seekBar.progress >= acceptFinalValue -> if (listener!=null) listener?.onAccept()
                        seekBar.progress <= ignoreFinalValue -> if (listener!=null) listener?.onIgnore()
                    }
                }
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                when {
                    !doWithoutStopTracking -> when {
                        seekBar.progress >= acceptValue ->
                            if (listener!=null) listener?.onAccept()
                        seekBar.progress <= ignoreValue ->
                            if (listener!=null) listener?.onIgnore()
                        else -> if (returnToCenter) smoothReturnToCenter()
                    }
                    else -> {
                        if (seekBar.progress >= acceptFinalValue || seekBar.progress <= ignoreFinalValue || !returnToCenter) {

                        } else smoothReturnToCenter()
                    }

                }
            }
        })
    }

    /**
     * @param acceptValue when arrives to this value fire onAccept()
     * @param mirror if set it True we change [ignoreValue]  base on [acceptValue]
     */
    public fun setAcceptValue(@IntRange(from = 55, to = 100) acceptValue: Int, mirror: Boolean = false){
        this.acceptValue = acceptValue
        when { mirror-> ignoreValue = maxValue - acceptValue }
    }

    /**
     * @param ignoreValue when arrives to this value fire onIgnore()
     * @param mirror if set it True we change [acceptValue]  base on [ignoreValue]
     */
    public fun setIgnoreValue(@IntRange(from = 0, to = 45) ignoreValue: Int, mirror: Boolean = false){
        this.ignoreValue = ignoreValue
        when { mirror-> acceptValue = maxValue - ignoreValue }
    }

    /**
     * @param acceptAnimationValue when arrives to this value start animation for accept container
     * @param mirror if set it True we change [ignoreAnimationValue]  base on [acceptAnimationValue]
     */
    public fun setAcceptAnimationValue(@IntRange(from = 55, to = 100) acceptAnimationValue: Int, mirror: Boolean = false){
        this.acceptAnimationValue = acceptAnimationValue
        when { mirror-> ignoreAnimationValue = maxValue - acceptAnimationValue }
    }

    /**
     * @param ignoreAnimationValue when arrives to this value start animation for ignore container
     * @param mirror if set it True we change [acceptAnimationValue]  base on [ignoreAnimationValue]
     */
    public fun setIgnoreAnimationValue(@IntRange(from = 0, to = 45) ignoreAnimationValue: Int, mirror: Boolean = false){
        this.ignoreAnimationValue = ignoreAnimationValue
        when { mirror-> acceptAnimationValue = maxValue - ignoreAnimationValue }
    }

    /**
     *  @param acceptFinalValue when arrives to this value fire onAccept()
     *  @param mirror if set it True we change [ignoreFinalValue]  base on [acceptFinalValue]
     *  for use this you must set [doWithoutStopTracking] TRUE
     *  @see doWithoutStopTracking()
     */
    public fun setAcceptFinalValue(@IntRange(from = 55, to = 100) acceptFinalValue: Int, mirror: Boolean = false){
        this.acceptFinalValue = acceptFinalValue
        when { mirror-> ignoreFinalValue = maxValue - acceptFinalValue }
    }

    /**
     *  @param ignoreFinalValue when arrives to this value fire onIgnore()
     *  @param mirror if set it True we change [acceptFinalValue]  base on [ignoreFinalValue]
     *  for use this you must set doWithoutStopTracking TRUE
     *  @see doWithoutStopTracking()
     */
    public fun setIgnoreFinalValue(@IntRange(from = 0, to = 45) ignoreFinalValue: Int, mirror: Boolean = false){
        this.ignoreFinalValue = ignoreFinalValue
        when { mirror-> acceptFinalValue = maxValue - ignoreFinalValue }
    }

    /**
     * change right image with res
     */
    public fun setAcceptImage(res: Int){
        ivAccept.setImageResource(res)
    }

    /**
     * change right image with bitmap
     */
    public fun setAcceptImage(bitmap: Bitmap){
        ivAccept.setImageBitmap(bitmap)
    }

    /**
     * change left image with res
     */
    public fun setIgnoreImage(res: Int){
        ivIgnored.setImageResource(res)
    }

    /**
     * change left image with bitmap
     */
    public fun setIgnoreImage(bitmap: Bitmap){
        ivIgnored.setImageBitmap(bitmap)
    }

    /**
     * change Arrows image with res
     */
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
    public fun returnToCenter(){
        seekBar.progress = centerProgress
    }
    /**
     * set progress to center and move thumb to center with animation
     */
    public fun smoothReturnToCenter(animDuration: Long=150,interpolator: Interpolator = FastOutSlowInInterpolator()){
        seekBar.smoothSetProgress(centerProgress,animDuration,interpolator)
    }

    /**
     * for disable rotate ignore imageView
     */
//    public fun disableRotateImage(){
//        ignoreDrawableRotate = 0f
//        invalidate()
//    }
    /**
     * change arrows imageViews alpha
     */
    public fun setArrowsImagesAlpha(alpha: Float){
        ivArrowsLeft.alpha = alpha
        ivArrowsRight.alpha = alpha
        invalidate()
    }
    /**
     * fire listener functions when swipe to end or start without Stop Touching thumb
     * be careful, if set it true, only when move to end of view your function fire so
     * for better experience use higher value for [acceptFinalValue] and [ignoreFinalValue]
     * see [setAcceptFinalValue] and [setIgnoreFinalValue]
     */
    public fun enableDoWithoutStopTracking(doWithoutStopTracking: Boolean){
        this.doWithoutStopTracking = doWithoutStopTracking
    }

    /**
     *  @param selectWithClick if true fire interface and move thumb with click on view
     *  default is false
     */
    public fun enableSelectWithClick(selectWithClick: Boolean){
        this.selectWithClick = selectWithClick
    }
    /**
     *  @param selectWithClickRange whatsoever smaller make clickable range bigger
     *  default is 30
     */
    public fun setSelectWithClickRange(@IntRange(from = 0, to = 50) selectWithClickRange: Int){
        this.selectWithClickRange = selectWithClickRange
    }

    /**
     * for change background color
     */
    public fun setChooserBackgroundColor(color: Int){
        val gradient = chooserBackground.background.mutate() as GradientDrawable
        gradient.setColor(color)
    }
    /**
     * for change Accept background color
     */
    public fun setAcceptBackgroundColor(color: Int){
        val gradient = ivAccept.background.mutate() as GradientDrawable
        gradient.setColor(color)
    }
    /**
     * for change Ignore background color
     */
    public fun setIgnoreBackgroundColor(color: Int){
        val gradient = ivIgnored.background.mutate() as GradientDrawable
        gradient.setColor(color)
    }
    /**
     * for change Accept background drawable
     */
    public fun setAcceptBackgroundDrawable(drawable: Drawable){
        ivAccept.background = drawable
    }
    /**
     * for change Ignore background drawable
     */
    public fun setIgnoreBackgroundDrawable(drawable: Drawable){
        ivIgnored.background = drawable
    }

    /**
     *  change Thumb view
     */
    public fun setThumb(drawable: Drawable){
        seekBar.thumb = drawable
    }

    /**
     *  change Right and left drawable padding together
     */
    public fun setDrawablePadding(pedding: Int){
        ivAccept.setPadding(pedding,pedding,pedding,pedding)
        ivIgnored.setPadding(pedding,pedding,pedding,pedding)
    }


    /**
     *  change Accept ImageView Size
     */
    public fun setAcceptDrawableSize(size: Int){
        val lp = ivAccept.layoutParams
        lp.height = size
        lp.width = size
        ivAccept.layoutParams = lp
    }

    /**
     *  change ignore ImageView Size
     */
    public fun setIgnoreDrawableSize(size: Int){
        val lp = ivIgnored.layoutParams
        lp.height = size
        lp.width = size
        ivIgnored.layoutParams = lp
    }

    /**
     *  change Thumb size
     */
    public fun setThumbSize(width:Int, height: Int){
        val bmpOrg = (seekBar.thumb as BitmapDrawable).bitmap
        val bmpScaled = Bitmap.createScaledBitmap(bmpOrg, width, height, true)
        val newThumb = BitmapDrawable(resources, bmpScaled)
        newThumb.setBounds(0, 0, newThumb.intrinsicWidth, newThumb.intrinsicHeight)
        seekBar.thumb = newThumb

    }





}