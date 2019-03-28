package com.example.menglingshuai.addpicture.imagefileselector;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.webkit.MimeTypeMap;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CommonUtils {

    /**
     * copy file
     *
     * @param source source file
     * @param dest   dest file
     * @return true if success copied
     */
    public static boolean copy(File source, File dest) {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        boolean result = true;
        try {
            bis = new BufferedInputStream(new FileInputStream(source));
            bos = new BufferedOutputStream(new FileOutputStream(dest, false));

            byte[] buf = new byte[1024];
            bis.read(buf);

            do {
                bos.write(buf);
            } while (bis.read(buf) != -1);
        } catch (IOException e) {
            result = false;
        } finally {
            try {
                if (bis != null) bis.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                result = false;
            }
        }

        return result;
    }

    /**
     * 在图片缓存文件夹生成一个临时文件
     *
     * @param context context
     * @param ext     文件后缀名 e.g ".jpg"
     * @return 生成的临时文件
     */
    public static File generateExternalImageCacheFile(Context context, String ext) {
        String fileName = "img_" + System.currentTimeMillis();
        return generateExternalImageCacheFile(context, fileName, ext);
    }

    private static File generateExternalImageCacheFile(Context context, String fileName, String ext) {
        File cacheDir = getExternalImageCacheDir(context);
        String path = cacheDir.getPath() + File.separator + fileName + ext;
        return new File(path);
    }

    public static File getExternalImageCacheDir(Context context) {
        File externalCacheDir = getExternalCacheDir(context);
        if (externalCacheDir != null) {
            String path = externalCacheDir.getPath() + "/image/image_selector";
            File file = new File(path);
            if (file.exists() && file.isFile()) {
                file.delete();
            }
            if (!file.exists()) {
                file.mkdirs();
            }
            return file;
        }
        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache" + "/image";
        return new File(cacheDir);
    }

    public static File getExternalCacheDir(Context context) {
        File file = context.getExternalCacheDir();
        if (file == null) {
            final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache";
            file = new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
        }
        return file;
    }

    /** 只返回图片类型url */
    public static String[] filterImage(String[] urls) {
        List<String> imageUrls = new ArrayList<>();
        for (String url : urls) {
            if (url != null && isImageFile(url)) {
                imageUrls.add(url);
            }
        }
        return imageUrls.toArray(new String[0]);
    }

    // url = file path or whatever suitable URL you want.
    public static boolean isImageFile(String url) {
        String mimeType = getMimeType(url);
        return mimeType != null && mimeType.startsWith("image");
    }

    // url = file path or whatever suitable URL you want.
    @Nullable
    public static String getMimeType(String url) {
        String type = null;
        String extension = getFileExtension(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    /**
     *
     * @param path local path or remote url
     * @return extension
     */
    public static String getFileExtension(String path) {
        if(path == null) {
            return null;
        }
        String extension = MimeTypeMap.getFileExtensionFromUrl(path);
        if(extension == null || extension.isEmpty()) {
            extension = getFileExtensionFromLocalPath(path);
        }
        return extension;
    }

    public static String getFileExtensionFromLocalPath(String filePath) {
        int index = filePath.lastIndexOf(".");
        if (index != -1 && index + 1 < filePath.length()) {
            return filePath.substring(index + 1);
        }
        return null;
    }
}