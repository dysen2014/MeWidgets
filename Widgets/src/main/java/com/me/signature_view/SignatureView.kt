package com.me.signature_view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Base64
import android.view.MotionEvent
import android.view.View
import java.io.ByteArrayOutputStream
import kotlin.math.abs


/**
 * @author dysen
 * dy.sen@qq.com     4/7/21 11:15 AM
 *
 * Info：
 */
class SignatureView : View {
    companion object {
        fun saveBitmapToBase64(bitmap: Bitmap):String{
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos)
            baos.flush()
            baos.close()
            return Base64.encodeToString(baos.toByteArray(), Base64.NO_WRAP)
        }
        fun base64ToBitmap(base64:String): Bitmap {
            val bytes = Base64.decode(base64, Base64.NO_WRAP)
            return BitmapFactory.decodeByteArray(bytes,0,bytes.size)
        }
    }
    var mPathPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG) // 笔划画笔
    lateinit var canvas: Canvas // 画布
    private lateinit var mPath: Path // 记录在屏幕上触摸移动时的点组成的路径
    private var mPreX: Float = 0.0f // 记录x坐标
    private var mPreY: Float = 0.0f // 记录y坐标
    private val touchTolerance:Float = 4F // 两点之间的差

    init {
        mPathPaint.isAntiAlias = true
        mPathPaint.isDither = true
        mPathPaint.style = Paint.Style.STROKE
        mPathPaint.strokeJoin = Paint.Join.ROUND
        mPathPaint.strokeCap = Paint.Cap.ROUND
        mPathPaint.strokeWidth = 10F
        mPathPaint.color = Color.BLACK
    }

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    @SuppressLint("NewApi")
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {}

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mPath = Path()
        canvas = Canvas()
    }
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN->{
                mPreX = event.x
                mPreY = event.y
                mPath.moveTo(mPreX, mPreY)
            }
            MotionEvent.ACTION_MOVE->{
                val x = event.x
                val y = event.y
                val dx = abs(mPreX-x)
                val dy = abs(mPreY - y)
                if(dx >= touchTolerance || dy >= touchTolerance){
                    mPath.quadTo(mPreX,mPreY,(x+mPreX)/2,(y+mPreY)/2)
                    mPreX = event.x
                    mPreY = event.y
                }
            }
            MotionEvent.ACTION_UP->{
                mPath.lineTo(event.x,event.y)
                canvas.drawPath(mPath,mPathPaint)
            }
        }
        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawPath(mPath,mPathPaint)
    }

    fun reset(){
        mPath = Path()
        canvas = Canvas()
        invalidate()
    }

    val mBitmap: Bitmap? get() {
        val bitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        if(background != null){
            background.draw(canvas)
        }else{
            canvas.drawColor(Color.WHITE)
            draw(canvas)
        }
        return bitmap
    }
}