package com.phillipcalvin.iconbutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;

public class IconButton extends Button {
  protected int drawableWidth;
  protected DrawablePositions drawablePosition;
  protected int iconPadding;

  // Cached to prevent allocation during onLayout
  Rect bounds;
 
  private enum DrawablePositions {
    NONE,
    LEFT,
    RIGHT
  }

  public IconButton(Context context) {
    super(context);
    bounds = new Rect();
  }

  public IconButton(Context context, AttributeSet attrs) {
    super(context, attrs);
    bounds = new Rect();
    applyAttributes(attrs);
  }

  public IconButton(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    bounds = new Rect();
    applyAttributes(attrs);
  }

  protected void applyAttributes(AttributeSet attrs) {
    // Slight contortion to prevent allocating in onLayout
    if (null == bounds) {
      bounds = new Rect();
    }

    SafeParcelHelper parcel = new SafeParcelHelper("com.phillipcalvin.iconbutton", 
                                                   getContext(), 
                                                   "com.phillipcalvin.iconbutton");

    int[] styleable = parcel.getStyleableArray("IconButton");
    TypedArray typedAttrs = getContext().obtainStyledAttributes(attrs,
        styleable,
        0, 0);
    int paddingId = parcel.getStyleableId("IconButton", "iconPadding");
    setIconPadding(typedAttrs.getDimensionPixelSize(paddingId, 0));
    typedAttrs.recycle();
  }

  public void setIconPadding(int padding) {
    iconPadding = padding;
    requestLayout();
  }

  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    super.onLayout(changed, left, top, right, bottom);

    Paint textPaint = getPaint();
    String text = getText().toString();
    textPaint.getTextBounds(text, 0, text.length(), bounds);

    int textWidth = bounds.width();
    int contentWidth = drawableWidth + iconPadding + textWidth;

    int contentLeft = (int)((getWidth() / 2.0) - (contentWidth / 2.0));
    setCompoundDrawablePadding(-contentLeft + iconPadding);
    switch (drawablePosition) {
    case LEFT:
      setPadding(contentLeft, 0, 0, 0);
      break;
    case RIGHT:
      setPadding(0, 0, contentLeft, 0);
      break;
    default:
      setPadding(0, 0, 0, 0);
    }
  }

  @Override
  public void setCompoundDrawablesWithIntrinsicBounds(Drawable left, Drawable top, Drawable right, Drawable bottom) {
    super.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
    if (null != left) {
      drawableWidth = left.getIntrinsicWidth();
      drawablePosition = DrawablePositions.LEFT;
    } else if (null != right) {
      drawableWidth = right.getIntrinsicWidth();
      drawablePosition = DrawablePositions.RIGHT;
    } else {
      drawablePosition = DrawablePositions.NONE;
    }
    requestLayout();
  }
}