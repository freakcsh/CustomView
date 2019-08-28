package com.freak.customview.custom;

import android.os.FileObserver;
import android.util.Log;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 监听的事件类型：
 * <p>
 *     ACCESS，即文件被访问
 *     MODIFY，文件被 修改
 *     ATTRIB，文件属性被修改，如 chmod、chown、touch 等
 *     CLOSE_WRITE，可写文件被 close
 *     CLOSE_NOWRITE，不可写文件被 close
 *     OPEN，文件被 open
 *     MOVED_FROM，文件被移走,如 mv
 *     MOVED_TO，文件被移来，如 mv、cp
 *     CREATE，创建新文件
 *     DELETE，文件被删除，如 rm
 *     DELETE_SELF，自删除，即一个可执行文件在执行时删除自己
 *     MOVE_SELF，自移动，即一个可执行文件在执行时移动自己
 *     CLOSE，文件被关闭，等同于(IN_CLOSE_WRITE | IN_CLOSE_NOWRITE)
 *     ALL_EVENTS，包括上面的所有事件
 *
 * @author Freak
 * @date 2019/8/22.
 */
public class SDCardFileObserver extends FileObserver {
    private Timer mTimer;
    private Map<String, DeleteFileTimerTask> tasks;
    private String targetDirName, targetFileName;

    /**
     * @param path
     * @param fileName
     * @param mask     指定要监听的事件类型，默认为FileObserver.ALL_EVENTS
     */
    public SDCardFileObserver(String path, String fileName, int mask) {
        super(path, mask);
        mTimer = new Timer();
        tasks = new HashMap<String, DeleteFileTimerTask>();
        targetDirName = path;
        targetFileName = fileName;
    }

    @Override
    public void onEvent(int event, String path) {
        final int action = event & FileObserver.ALL_EVENTS;
        switch (action) {
            case FileObserver.OPEN:
                Log.d("FileObserver", "file open; path=" + path);
                Log.e("TAG", "file open; path= " + path);
                break;
            case FileObserver.CLOSE_NOWRITE:
            case FileObserver.CLOSE_WRITE:
                Log.d("FileObserver", "file close; path=" + path);
                Log.e("TAG", "file close; path= " + path);
                if (path == null) {
                    return;
                }
                if (!path.endsWith(targetFileName)) {
                    return;
                }
                if (tasks.containsKey(path)) {
                    DeleteFileTimerTask task = tasks.get(path);
                    if (task.cancel()) {
                        task = new DeleteFileTimerTask(path);
                        mTimer.schedule(task, 3000);
                    }
                } else {
                    DeleteFileTimerTask task = new DeleteFileTimerTask(path);
                    tasks.put(path, task);
                    mTimer.schedule(task, 3000);
                }
                break;
            case FileObserver.DELETE:
                Log.e("TAG", "文件被删除");
                break;
            default:
                break;
        }
    }

    class DeleteFileTimerTask extends TimerTask {

        private String path;

        public DeleteFileTimerTask(String path) {
            this.path = path;
        }

        @Override
        public void run() {
            String tmpFile = targetDirName + "/" + targetFileName;
            File f = new File(tmpFile);
            if (f.exists() && f.isFile()) {
                Log.d("FileObserver", "delete tmp file " + path);
                Log.e("TAG", "delete tmp file " + path);
                f.delete();
                SDCardFileObserver.this.stopWatching();
            }
            tasks.remove(path);
        }
    }

}
