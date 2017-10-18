package com.nick.jikelikedemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class LikeViewActivity extends AppCompatActivity implements View.OnClickListener {
    private RippView mRippView;
    private AnimatorSet mAnimatorSet;
    private ImageView mImgUnLike;
    private ImageView mImgLike;
    private ImageView mImgLikeShining;
    private TextScrollView mLikeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like_view);
        mLikeView = (TextScrollView) findViewById(R.id.lv_test);
        mImgUnLike = (ImageView) findViewById(R.id.img_un_like);
        mImgLike = (ImageView) findViewById(R.id.img_like);
        mImgLikeShining = (ImageView) findViewById(R.id.img_like_shining);
        mRippView = (RippView) findViewById(R.id.rv_test);
        mLikeView.setText("9999");

        findViewById(R.id.bt_up).setOnClickListener(this);
        findViewById(R.id.bt_down).setOnClickListener(this);
        findViewById(R.id.bt_set).setOnClickListener(this);

        findViewById(R.id.rl_like).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        scaleAnim(0.7f);
                        mAnimatorSet.start();
                        return true;
                    case MotionEvent.ACTION_UP:
                        if (mImgLike.getVisibility() == View.VISIBLE) {
                            //开启动画是判断是那个效果
                            likeClick(0.7f);
                            mLikeView.down();
                            mAnimatorSet.playTogether(mLikeView.downAnim());
                        } else {
                            unLikeClick();
                            mLikeView.up();
                            mAnimatorSet.playTogether(mLikeView.upAnim());
                        }
                        mAnimatorSet.start();
                }

                return false;
            }
        });
    }

    private void scaleAnim(float scale) {
        mAnimatorSet = new AnimatorSet();
        View view = mImgUnLike.getVisibility() == View.VISIBLE ? mImgUnLike : mImgLike;
        //开启缩放动画，此时缩放倍率0.5
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "scaleX", 1, scale);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, "scaleY", 1, scale);
        if (view == mImgLike) {
            ObjectAnimator animator3 = ObjectAnimator.ofFloat(mImgLikeShining, "scaleX", 1, scale);
            ObjectAnimator animator4 = ObjectAnimator.ofFloat(mImgLikeShining, "scaleY", 1, scale);
            mAnimatorSet.playTogether(animator, animator2, animator3, animator4);
        } else {
            mAnimatorSet.playTogether(animator, animator2);
        }
    }

    /**
     * 整体动画思路：通过gif分解发下动画按下的时候有个缩放的动画，抬起时，
     *
     * @param scale
     */
    private void likeClick(float scale) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mImgLike, "alpha", 1, 0.5f);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mImgLike.setVisibility(View.GONE);
                mImgLike.setAlpha(1f);
            }
        });
        animator.setDuration(100);
        mImgUnLike.setScaleY(scale);
        mImgUnLike.setScaleX(scale);
        mImgUnLike.setVisibility(View.VISIBLE);
        ObjectAnimator animator5 = ObjectAnimator.ofFloat(mImgLikeShining, "alpha", 0);
        Keyframe keyframe1 = Keyframe.ofFloat(0, scale);
        Keyframe keyframe4 = Keyframe.ofFloat(1, 1);
        PropertyValuesHolder holder = PropertyValuesHolder.ofKeyframe("scaleX", keyframe1, keyframe4);
        PropertyValuesHolder holder2 = PropertyValuesHolder.ofKeyframe("scaleY", keyframe1, keyframe4);
        ObjectAnimator animator7 = ObjectAnimator.ofPropertyValuesHolder(mImgUnLike, holder, holder2);
        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(animator, animator7, animator5);
    }

    private void unLikeClick() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mImgUnLike, "alpha", 1, 0.5f);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mImgUnLike.setVisibility(View.GONE);
                mImgUnLike.setAlpha(1f);
            }
        });
        animator.setDuration(100);

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mRippView, "radius", mRippView.getMeasuredWidth() / 5, mRippView.getMeasuredWidth() / 2);
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(mRippView, "alpha", 0.4f, 0);
        mImgLike.setVisibility(View.VISIBLE);
        mImgLike.setScaleY(0.7f);
        mImgLike.setScaleX(0.7f);
        mImgLikeShining.setVisibility(View.VISIBLE);
        mImgLikeShining.setScaleY(0.2f);
        mImgLikeShining.setScaleX(0.2f);
        mImgLikeShining.setAlpha(1f);
        //开启另一个动画
        ObjectAnimator animator5 = ObjectAnimator.ofFloat(mImgLikeShining, "scaleX", 1);
        ObjectAnimator animator6 = ObjectAnimator.ofFloat(mImgLikeShining, "scaleY", 1);
        Keyframe keyframe1 = Keyframe.ofFloat(0, 0.7f);
        Keyframe keyframe2 = Keyframe.ofFloat(0.5f, 1);
        Keyframe keyframe3 = Keyframe.ofFloat(0.75f, 0.8f);
        Keyframe keyframe4 = Keyframe.ofFloat(1, 1);
        PropertyValuesHolder holder = PropertyValuesHolder.ofKeyframe("scaleX", keyframe1, keyframe2, keyframe3, keyframe4);
        PropertyValuesHolder holder2 = PropertyValuesHolder.ofKeyframe("scaleY", keyframe1, keyframe2, keyframe3, keyframe4);
        ObjectAnimator animator7 = ObjectAnimator.ofPropertyValuesHolder(mImgLike, holder, holder2);
        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(animator, animator5, animator6, objectAnimator, objectAnimator2, animator7);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_up:
                mLikeView.up();
                unLikeClick();
                mAnimatorSet.playTogether(mLikeView.upAnim());
                mAnimatorSet.start();
                break;
            case R.id.bt_down:
                mLikeView.down();
                likeClick(0.8f);
                mAnimatorSet.playTogether(mLikeView.downAnim());
                mAnimatorSet.start();
                break;
            case R.id.bt_set:
                String number = ((EditText) findViewById(R.id.et_set)).getText().toString();
                mLikeView.setInitState();
                mLikeView.setText(number);
                break;
        }
    }
}
