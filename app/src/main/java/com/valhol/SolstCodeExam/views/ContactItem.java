package com.valhol.SolstCodeExam.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.valhol.SolstCodeExam.R;

/**
 * Custom view class representing a ContactModel in a RecyclerView
 */
public class ContactItem extends LinearLayout {

    TextView mContactNameView;
    TextView mPhoneNumberView;
    ImageView mThumbnailView;

    public ContactItem(Context context) {
        super(context);
        init(null, 0);
    }

    public ContactItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ContactItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        LayoutInflater.from(getContext()).inflate(R.layout.contact_item_layout, this, true);
        setPadding(6,6,6,6);
        mContactNameView = (TextView) findViewById(R.id.contactName);
        mPhoneNumberView = (TextView) findViewById(R.id.phoneNumber);
        mThumbnailView = (ImageView) findViewById(R.id.thumbnailImage);
    }

    public void setThumbnailUri(String thumbnailUri) {
        Picasso.with(getContext())
                .load(thumbnailUri)
                .into(mThumbnailView);
    }

    public void setContactName(String contactName) {
        mContactNameView.setText(contactName);
    }

    public void setPhoneNumber(String phoneNumber) {
        mPhoneNumberView.setText(phoneNumber);
    }

}
