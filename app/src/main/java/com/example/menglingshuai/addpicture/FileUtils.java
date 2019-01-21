package com.example.menglingshuai.addpicture;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import java.io.*;

public final class FileUtils {
    private FileUtils() {}

    public static String getFilePathByUri(Context context, Uri uri) {
        String path = null;
        // 以 file:// 开头的
        if (ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {
            path = uri.getPath();
            return path;
        }
        // 以 content:// 开头的，比如 content://media/extenral/images/media/17766
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme()) && Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    if (columnIndex > -1) {
                        path = cursor.getString(columnIndex);
                    }
                }
                cursor.close();
            }
            return path;
        }
        // 4.4及之后的 是以 content:// 开头的，比如 content://com.android.providers.media.documents/document/image%3A235700
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme()) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (DocumentsContract.isDocumentUri(context, uri)) {
                if (isExternalStorageDocument(uri)) {
                    // ExternalStorageProvider
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    if ("primary".equalsIgnoreCase(type)) {
                        path = Environment.getExternalStorageDirectory() + "/" + split[1];
                        return path;
                    }
                } else if (isDownloadsDocument(uri)) {
                    // DownloadsProvider
                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                            Long.valueOf(id));
                    path = getDataColumn(context, contentUri, null, null);
                    return path;
                } else if (isMediaDocument(uri)) {
                    // MediaProvider
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{split[1]};
                    path = getDataColumn(context, contentUri, selection, selectionArgs);
                    return path;
                }
            } else {
                String[] projection = { MediaStore.Images.Media.DATA };
                Cursor cursor = null;
                try {
                    cursor = context.getContentResolver().query(uri, projection, null, null, null);
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    if (cursor.moveToFirst()) {
                        return cursor.getString(columnIndex);
                    }
                } catch (Exception e) {
                    Log.e("aaaddd", String.valueOf(e));
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            }
        }
        return null;
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

	/**
	 * 通知相册，在相册中显示
	 */
	public static void notifyGallery(Context context, String path) {
		context.sendBroadcast(new Intent(
				Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
				Uri.fromFile(new File(path))
				));
    }

    /**
     * 删除目录下所有文件
     * @param dir file
     */
    public static void deleteFileDir(final File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory()) return;
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isFile()) {
                    file.delete();
                } else if (file.isDirectory()) {
                    deleteFileDir(file);
                }
            }
        }
        dir.delete();
    }

    /**
     *  将bitmap保存到SD卡
     */
    public static String saveBitmapToSDAndGetFilePath(Bitmap bt, String target_path) {
        if(bt == null || target_path == null || target_path.isEmpty()) {
            return "";
        }
        File file = new File(target_path);
        if (file.exists()){
            return target_path;
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            bt.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            return target_path;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     *  将video从assets文件夹保存到SD卡
     */
    public static String saveVideoToSDAndGetFilePath(Context context, String fileName, String target_path) {
        if(fileName == null || target_path == null || target_path.isEmpty()) {
            return "";
        }
        File file = new File(target_path);
        if (file.exists()){
            return target_path;
        }
        try {
            InputStream is = context.getAssets().open(fileName);
            FileOutputStream fos = new FileOutputStream(new File(target_path));
            byte[] buffer = new byte[1024];
            int byteCount=0;
            while((byteCount=is.read(buffer))!=-1) {
                fos.write(buffer, 0, byteCount);
            }
            fos.flush();
            is.close();
            fos.close();
            return target_path;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}