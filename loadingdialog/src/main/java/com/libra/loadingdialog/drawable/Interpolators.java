/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package com.libra.loadingdialog.drawable;

import android.graphics.Path;
import android.support.v4.view.animation.PathInterpolatorCompat;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

/**
 * Interpolators backported for Animators in this library.
 */
class Interpolators {

    /**
     * Backported Interpolator for {@code @android:interpolator/trim_start_interpolator}.
     */
    public static class TRIM_PATH_START {
        // L 0.5,0
        // C 0.7,0 0.6,1 1,1
        private static final Path PATH_TRIM_PATH_START;

        static {
            PATH_TRIM_PATH_START = new Path();
            PATH_TRIM_PATH_START.lineTo(0.5f, 0);
            PATH_TRIM_PATH_START.cubicTo(0.7f, 0, 0.6f, 1, 1, 1);
        }

        public static final Interpolator INSTANCE =
                PathInterpolatorCompat.create(PATH_TRIM_PATH_START);
    }

    /**
     * Backported Interpolator for {@code @android:interpolator/trim_end_interpolator}.
     */
    public static class TRIM_PATH_END {
        // C 0.2,0 0.1,1 0.5,1
        // L 1,1
        private static final Path PATH_TRIM_PATH_END;

        static {
            PATH_TRIM_PATH_END = new Path();
            PATH_TRIM_PATH_END.cubicTo(0.2f, 0, 0.1f, 1, 0.5f, 1);
            PATH_TRIM_PATH_END.lineTo(1, 1);
        }

        public static final Interpolator INSTANCE =
                PathInterpolatorCompat.create(PATH_TRIM_PATH_END);
    }

    /**
     * Lazy-initialized singleton Interpolator for {@code @android:interpolator/linear}.
     */
    public static class LINEAR {
        public static final Interpolator INSTANCE = new LinearInterpolator();
    }

    private Interpolators() {
    }
}
