package com.libra.wallpaper;

import android.hardware.Camera;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import java.io.IOException;

/**
 * Created by libra on 2017/7/21.
 */

public class CameraLiveWallpaper extends WallpaperService {
    // 实现WallpaperService必须实现的抽象方法
    public Engine onCreateEngine() {
        // 返回自定义的CameraEngine
        return new CameraEngine();
    }


    class CameraEngine extends Engine implements Camera.PreviewCallback {
        private Camera camera;

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);

            startPreview();
            // 设置处理触摸事件
            setTouchEventsEnabled(true);

        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            super.onTouchEvent(event);
            // 时间处理:点击拍照,长按拍照
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            stopPreview();
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            if (visible) {
                startPreview();
            } else {
                stopPreview();
            }
        }

        /**
         * 开始预览
         */
        public void startPreview() {
            if (camera != null) {
                camera.release();
            }
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);

            camera.setDisplayOrientation(90);
            // 设置预览分辨率
            Camera.Parameters parameters = camera.getParameters();
            Camera.Size size = CameraUtil.getFullScreenSize(parameters.getSupportedPreviewSizes());
            parameters.setPreviewSize(size.width, size.height);
            // 设置自动对焦
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            camera.setParameters(parameters);

            try {
                camera.setPreviewDisplay(getSurfaceHolder());
            } catch (IOException e) {
                e.printStackTrace();
            }
            camera.startPreview();

        }

        /**
         * 停止预览
         */
        public void stopPreview() {
            if (camera != null) {
                try {
                    camera.stopPreview();
                    camera.setPreviewCallback(null);
                    // camera.lock();
                    camera.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                camera = null;
            }
        }

        @Override
        public void onPreviewFrame(byte[] bytes, Camera camera) {
            camera.addCallbackBuffer(bytes);
        }
    }
}
