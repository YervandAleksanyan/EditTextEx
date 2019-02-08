package com.example.yervand.edittextex

import android.annotation.SuppressLint
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
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView

class EditTextEx : LinearLayout, OnFocusChangeListener {
    internal val TAG = javaClass.simpleName
    var editText: EditText? = null
        private set
    var title: TextView? = null
    var hint: String = ""
    private var inpuTextParams: LinearLayout.LayoutParams? = null
    private var displayTextParams: LinearLayout.LayoutParams? = null
    private var bottomUp: Animation? = null
    private var bottomDown: Animation? = null
    private val focusChangeListener: OnFloatingLableFocusChangeListener? = null

    var text: String?
        get() = editText!!.text.toString()
        set(string) {
            if (string != null) {
                editText!!.setText(string)
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
        editText = EditText(context)
        title = TextView(context)
        editText!!.onFocusChangeListener = this
        bottomUp = AnimationUtils.loadAnimation(context, R.anim.txt_bottom_up)
        bottomDown = AnimationUtils.loadAnimation(
            context,
            R.anim.txt_bottom_down
        )
        // Create Default Layout
        inpuTextParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        displayTextParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        editText!!.layoutParams = inpuTextParams
        title!!.layoutParams = displayTextParams
        orientation = LinearLayout.VERTICAL
        createDefaultLayout()

        if (attrs != null) {
            createCustomLayout(attrs)
        }

        addView(title)
        addView(editText)
        title!!.visibility = View.INVISIBLE

//        editText!!.addTextChangedListener(object : TextWatcher {
//
//            override fun onTextChanged(
//                s: CharSequence, start: Int, before: Int,
//                count: Int
//            ) {
//                if (editText!!.text.toString().isNotEmpty() && title!!.visibility == View.INVISIBLE) {
//                    showHint()
//                } else if (editText!!.text.toString().isEmpty() && title!!.visibility == View.VISIBLE) {
//                    hideHint()
//                }
//            }
//
//            override fun beforeTextChanged(
//                s: CharSequence, start: Int, count: Int,
//                after: Int
//            ) {
//                // TODO Auto-generated method stub
//            }
//
//            override fun afterTextChanged(s: Editable) {
//                // TODO Auto-generated method stub
//            }
//        })
    }

    @SuppressLint("ResourceType")
    private fun createDefaultLayout() {
        editText!!.gravity = Gravity.LEFT or Gravity.TOP
        title!!.gravity = Gravity.LEFT

        title!!.setTextColor(Color.BLACK)
        editText!!.setTextColor(Color.BLACK)

        val context = context
        editText!!.setTextAppearance(context, android.R.attr.textAppearanceMedium)
        title!!.setTextAppearance(context, android.R.attr.textAppearanceSmall)

        title!!.setPadding(5, 2, 5, 2)
    }

    private var floatTitleErrorColor: ColorStateList? = null

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
        hint = floatHintText
        floatHintTextColorFocused = attr
            .getColorStateList(R.styleable.EditTextEx_floatHintTextColorFocused)
        floatTitleErrorColor = attr
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

//        setFloatHintText(floatHintText)
        setFloatHintTextColor(
            getColorStateList(
                floatHintTextColorFocused,
                floatHintTextColorUnFocused
            )
        )
        setFloatHintTextSize(floatHintTextSize)
        setFloatHintTypeFace(floatHintTextTypefaceName, floatHintTextStyle)
        setFloatHintGravity(floatHintTextGravity)
        setFloatHintTextBackGround(floatHintTextBackground)

        setTextHint(floatHintText)
        setTextColor(textColor)
        setTextSize(textSize)
        setTextTypeFace(textTypefaceName, textStyle)
        setTextGravity(textGravity)
        setTextBackGround(textBackground)
        setPassword(isPassword)
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
        title!!.gravity = floatHintTextGravity
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
            title!!.typeface = face
        } catch (e: Exception) {
            title!!.setTypeface(null, floatHintTextStyle)
        }

    }

    fun setFloatHintText(msg: String) {
        val isError = !msg.isEmpty()
        if (isError) {
            setFloatHintTextColor(getColorStateList(floatTitleErrorColor, floatHintTextColorUnFocused))
            title!!.text = msg
        } else {
            if (!text!!.isEmpty()) {
                setFloatHintTextColor(getColorStateList(floatHintTextColorFocused, floatHintTextColorUnFocused))
                title!!.text = hint
            }
        }
        showHint()
    }

    private fun setFloatHintTextSize(floatHintTextSize: Int) {
        title!!.setTextSize(TypedValue.COMPLEX_UNIT_SP, floatHintTextSize.toFloat())
    }

    private fun setFloatHintTextColor(floatHintTextColor: ColorStateList?) {
        if (floatHintTextColor != null) {
            title!!.setTextColor(floatHintTextColor)
        }
    }

    private fun setFloatHintTextBackGround(textBackground: Drawable?) {
        editText!!.setBackgroundDrawable(textBackground)
    }

    /** FOR TEXT  */

    private fun setPassword(isPassword: Boolean) {
        // TODO Auto-generated method stub
        if (isPassword) {
            editText!!.transformationMethod = PasswordTransformationMethod
                .getInstance()
        }
    }

    private fun setTextBackGround(textBackground: Drawable?) {
        editText!!.setBackgroundDrawable(textBackground)
    }

    private fun setTextGravity(textGravity: Int) {
        editText!!.gravity = textGravity
    }

    private fun setTextTypeFace(textTypefaceName: String?, textStyle: Int) {
        try {
            val face = Typeface.createFromAsset(
                context.assets,
                textTypefaceName
            )
            editText!!.typeface = face
        } catch (e: Exception) {
            editText!!.setTypeface(null, textStyle)
        }

    }

    private fun setTextSize(textSize: Int) {
        editText!!.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize.toFloat())
    }

    private fun setTextColor(textColor: ColorStateList?) {
        if (textColor != null) {
            editText!!.setTextColor(textColor)
        }
    }

    fun setTextHint(hintText: String?) {
        if (hintText != null) {
            editText!!.hint = hintText
        }
    }

    private fun showHint(withAnimation: Boolean = true) {
        if (title!!.visibility != View.VISIBLE) {
            title!!.visibility = View.VISIBLE
            if (withAnimation) {
                title!!.startAnimation(bottomUp)
            }
        }
    }

    private fun hideHint(withAnimation: Boolean = true) {
        if (title!!.visibility != View.INVISIBLE) {
            title!!.visibility = View.INVISIBLE
            if (withAnimation)
                title!!.startAnimation(bottomDown)
        }
    }

    override fun onFocusChange(v: View, hasFocus: Boolean) {
        title!!.isSelected = hasFocus
        focusChangeListener?.onFocusChange(this, hasFocus)
    }
}

