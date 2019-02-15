package com.example.yervand.edittextex

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.yervand.edittextex.utils.convertDpToPixel

class EditTextEx : ConstraintLayout, OnFocusChangeListener {
    internal val TAG = javaClass.simpleName

    //View
    lateinit var editText: EditText
    private lateinit var title: TextView
    private lateinit var errorMsg: TextView


    private val fontIncreaseValueAnimator = ValueAnimator.ofFloat(0f, 12F)
    private val fontDecreaseValueAnimator = ValueAnimator.ofFloat(12f, 0F)

    private var focusChangeListener: OnFloatingLableFocusChangeListener? = null

    var text: String?
        get() = editText.text.toString()
        set(string) {
            if (string != null) {
                editText.setText(string)
            }
        }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        if (!isInEditMode) {
            createLayout(attrs)
        }
    }

    constructor(context: Context) : super(context) {
        createLayout(null)
    }

    private fun createLayout(attrs: AttributeSet?) {
        val context = context
        val view = inflate(context, R.layout.edit_text_ex_layout, this)
        editText = view.findViewById(R.id.input)
        title = view.findViewById(R.id.title)
        errorMsg = view.findViewById(R.id.error_msg)
        editText.onFocusChangeListener = this


        if (attrs != null) {
            createCustomLayout(attrs)
        }
        editText.measure(rootView.width, rootView.height)

        fontIncreaseValueAnimator.duration = 1000
        fontDecreaseValueAnimator.duration = 0
    }

    private var errorMsgTextColor: ColorStateList? = null

    private var floatHintTextColorFocused: ColorStateList? = null

    private var floatHintTextColorUnFocused: ColorStateList? = null

    private fun createCustomLayout(attrs: AttributeSet) {
        val attr = context.obtainStyledAttributes(
            attrs,
            R.styleable.EditTextEx, 0, 0
        )
        // For Floating Hint
        val floatHintText = attr
            .getString(R.styleable.EditTextEx_floatHintText)
        floatHintTextColorFocused = attr
            .getColorStateList(R.styleable.EditTextEx_floatHintTextColorFocused)
        errorMsgTextColor = attr
            .getColorStateList(R.styleable.EditTextEx_floatTitleErrorColor)
        floatHintTextColorUnFocused = attr
            .getColorStateList(R.styleable.EditTextEx_floatHintTextColorUnFocused)
        val floatHintTextSize = attr.getInt(
            R.styleable.EditTextEx_floatHintTextSize, 15
        )
        val floatHintTextTypefaceName = attr
            .getString(R.styleable.EditTextEx_floatHintTextTypeface)
        val floatHintTextStyle = attr.getInt(
            R.styleable.EditTextEx_floatHintTextStyle, Typeface.NORMAL
        )
        val floatHintTextGravity = attr.getInt(
            R.styleable.EditTextEx_floatHintTextGravity, Gravity.LEFT
        )
        val floatHintTextBackground = attr
            .getDrawable(R.styleable.EditTextEx_floatHintTextBackground)

        // For Actual Text
        var text = attr.getString(R.styleable.EditTextEx_text)
        val textColor = attr
            .getColorStateList(R.styleable.EditTextEx_textColor)
        val textSize = attr.getInt(R.styleable.EditTextEx_textSize, 15)
        val textTypefaceName = attr
            .getString(R.styleable.EditTextEx_textTypeface)
        val textStyle = attr.getInt(
            R.styleable.EditTextEx_textStyle,
            Typeface.NORMAL
        )
        val textGravity = attr.getInt(
            R.styleable.EditTextEx_textGravity,
            Gravity.LEFT
        )

        val textBackground = attr
            .getDrawable(R.styleable.EditTextEx_textBackground)
        val isPassword = attr.getBoolean(
            R.styleable.EditTextEx_isPassword,
            false
        )

        attr.recycle()
        setFloatHintTextColor(
            getColorStateList(
                floatHintTextColorFocused,
                floatHintTextColorUnFocused
            )
        )

        setErrorMsgTextColor(getColorStateList(errorMsgTextColor, floatHintTextColorUnFocused))
        setErrorMsgTextSize(12F)
        setFloatHintTextSize(floatHintTextSize.toFloat())
        setFloatHintTypeFace(floatHintTextTypefaceName, floatHintTextStyle)
        setFloatHintGravity(floatHintTextGravity)
//        setFloatHintTextBackGround(floatHintTextBackground)
        setTextHint(floatHintText)
        setTitleText(floatHintText)
        setTextColor(textColor)
        setTextSize(textSize)
        setTextTypeFace(textTypefaceName, textStyle)
        setTextGravity(textGravity)
//        setTextBackGround(textBackground)
//        setPassword(isPassword)
    }

    private fun setTitleText(floatHintText: String?) {
        floatHintText?.let {
            title.text = it
        }
    }

    private fun getColorStateList(
        focused: ColorStateList?,
        unfocused: ColorStateList?
    ): ColorStateList {
        val states = arrayOf(
            intArrayOf(android.R.attr.state_selected), // selected
            intArrayOf() // default
        )

        val colors = intArrayOf(focused?.defaultColor ?: Color.BLACK, unfocused?.defaultColor ?: Color.GRAY)
        return ColorStateList(states, colors)
    }

    /** FOR LABEL  */
    private fun setFloatHintGravity(floatHintTextGravity: Int) {
        title.gravity = floatHintTextGravity
    }

    private fun setFloatHintTypeFace(
        floatHintTextTypefaceName: String?,
        floatHintTextStyle: Int
    ) {
        try {
            val face = Typeface.createFromAsset(
                context.assets,
                floatHintTextTypefaceName
            )
            title.typeface = face
        } catch (e: Exception) {
            title.setTypeface(null, floatHintTextStyle)
        }
    }

    fun setErrorMsg(msg: String) {
        errorMsg.text = msg
        showErrorMsg()
    }

    private fun setFloatHintTextSize(floatHintTextSize: Float) {
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, floatHintTextSize)
    }

    private fun setErrorMsgTextSize(errorMsgTextSize: Float) {
        errorMsg.setTextSize(TypedValue.COMPLEX_UNIT_SP, errorMsgTextSize)
    }

    private fun setFloatHintTextColor(floatHintTextColor: ColorStateList?) {
        floatHintTextColor?.let {
            title.setTextColor(it)
        }
    }

    private fun setErrorMsgTextColor(errorMsgTextColor: ColorStateList?) {
        errorMsgTextColor?.let {
            errorMsg.setTextColor(it)
        }
    }

    private fun setFloatHintTextBackGround(textBackground: Drawable?) {
        editText.setBackgroundDrawable(textBackground)
    }

    /** FOR TEXT  */

    private fun setPassword(isPassword: Boolean) {
        // TODO Auto-generated method stub
        if (isPassword) {
            editText.transformationMethod = PasswordTransformationMethod
                .getInstance()
        }
    }

    private fun setTextBackGround(textBackground: Drawable?) {
        editText.setBackgroundDrawable(textBackground)
    }

    private fun setTextGravity(textGravity: Int) {
        editText.gravity = textGravity
    }

    private fun setTextTypeFace(textTypefaceName: String?, textStyle: Int) {
        try {
            val face = Typeface.createFromAsset(
                context.assets,
                textTypefaceName
            )
            editText.typeface = face
        } catch (e: Exception) {
            editText.setTypeface(null, textStyle)
        }

    }

    private fun setTextSize(textSize: Int) {
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize.toFloat())
    }

    private fun setTextColor(textColor: ColorStateList?) {
        if (textColor != null) {
            editText.setTextColor(textColor)
        }
    }

    fun setTextHint(hintText: String?) {
        hintText?.let {
            editText.hint = hintText
        }
    }

    fun showTitle() {
        if (errorMsg.visibility == View.VISIBLE) {
            animateTitleShow()
        }
    }

    private fun showErrorMsg() {
        if (title.visibility == View.VISIBLE) {
            animateErrorMsgShow()
        }
    }


    private fun hideErrorMsg() {
        if (errorMsg.visibility != View.INVISIBLE) {
            errorMsg.visibility = View.INVISIBLE
        }
    }

    private fun hideTitle() {
        if (title.visibility != View.INVISIBLE) {
            title.visibility = View.INVISIBLE
        }
    }

    override fun onFocusChange(v: View, hasFocus: Boolean) {
        title.isSelected = hasFocus
        errorMsg.isSelected = hasFocus
        if (text?.isEmpty()!!) {
            if (title.visibility == View.INVISIBLE && errorMsg.visibility == View.INVISIBLE) {
                animateTitleDown()
                title.visibility = View.VISIBLE
                animateTitleUp()
            }
        }
        focusChangeListener?.onFocusChange(this, hasFocus)
    }

    private fun animateTitleDown() {
        title
            .animate()
            .apply {
                this.translationY(
                    editText.measuredHeight.toFloat() / 2 + convertDpToPixel(title.paddingBottom.toFloat())
                )
                this.duration = 0
                this.alpha(0F)
            }.start()
        decreaseTitleFont()
    }

    private fun animateTitleUp() {
        title
            .animate()
            .apply {
                this.duration = 1000
                this.alpha(1F)
                this.translationY(0F)
            }.start()
        increaseTitleFont()
    }

    private fun animateTitleShow() {
        title.rotationX = -180F
        title.alpha = 0f
        hideErrorMsg()
        title.visibility = View.VISIBLE
        title.animate()
            .apply {
                this.duration = 500
                this.rotationX(0F)
                this.alpha(1F)
            }.start()
    }

    private fun animateErrorMsgShow() {
        errorMsg.rotationX = -180F
        errorMsg.alpha = 0f
        hideTitle()
        errorMsg.visibility = View.VISIBLE
        errorMsg.animate()
            .apply {
                this.duration = 500
                this.rotationX(0F)
                this.alpha(1F)
            }
    }

    private fun increaseTitleFont() {
        fontIncreaseValueAnimator.start()
        fontIncreaseValueAnimator.addUpdateListener {
            val value = it.animatedValue as Float
            setFloatHintTextSize(value)
        }
    }

    private fun decreaseTitleFont() {
        fontDecreaseValueAnimator.start()
        fontDecreaseValueAnimator.addUpdateListener {
            val value = it.animatedValue as Float
            setFloatHintTextSize(value)
        }
    }
}

