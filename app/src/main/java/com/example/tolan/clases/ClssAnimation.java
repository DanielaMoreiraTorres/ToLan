package com.example.tolan.clases;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;

public class ClssAnimation {

    private static ClssAnimation instanciaAnimation;
    AnimationSet set;
    Animation animationDown, animationUp;
    LayoutAnimationController controller;


    private ClssAnimation() {
        set = getAnimationSet();
        controller = getLayoutAnimationController();
    }

    public static synchronized ClssAnimation getInstanciaAnimation() {
        if (instanciaAnimation == null) {
            instanciaAnimation = new ClssAnimation();
        }
        return instanciaAnimation;
    }

    public AnimationSet getAnimationSet() {
        if (set == null) {
            set = new AnimationSet(true);
        }
        return set;
    }

    public Animation getAnimationDown() {
        if (animationDown == null) {
            animationDown = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 1.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f);
        }
        animationDown.setDuration(500);
        return animationDown;
    }

    public Animation getAnimationUp() {
        if (animationUp == null) {
            animationUp = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 1.0f);
        }
        animationUp.setDuration(500);
        return animationUp;
    }

    public LayoutAnimationController getLayoutAnimationController() {
        if (controller == null) {
            controller = new LayoutAnimationController(set, 0.25f);
        }
        return controller;
    }

}
