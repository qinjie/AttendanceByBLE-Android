package edu.np.ece.attendancetakingapplication;


import android.os.Environment;

import de.mindpipe.android.logging.log4j.LogConfigurator;

/**
 * Created by dellpc on 11/16/2017.
 */

public class Log4jHelper {


    private final static LogConfigurator mLogConfigrator = new LogConfigurator();
    static {
        ConfigureLog4j();
    }

    private static void ConfigureLog4j() {
        String fileName = Environment.getExternalStorageDirectory() + "/" + "log4j.txt";
        String filePattern = "%d - [%c] - %p : %m%n";
        int maxBackupSize = 100;
        long maxFileSize = 1024 * 1024;

        configure1( fileName, filePattern, maxBackupSize, maxFileSize );

    }

    private static void configure1( String fileName, String filePattern, int maxBackupSize, long maxFileSize){
        mLogConfigrator.setFileName( fileName);
        mLogConfigrator.setFilePattern(filePattern);
        mLogConfigrator.setMaxBackupSize(maxBackupSize);
        mLogConfigrator.setMaxFileSize(maxFileSize);
        mLogConfigrator.configure();
    }

    public static org.apache.log4j.Logger getLogger( String name){
        org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(name);
        return logger;
    }
}
