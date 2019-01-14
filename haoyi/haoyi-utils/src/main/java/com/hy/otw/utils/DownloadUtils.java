
package com.hy.otw.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;


/**
 * @author ds
 */
public abstract class DownloadUtils {

    private static final Logger logger = Logger.getLogger( DownloadUtils.class );

    /**
     * @param request
     * @param response
     * @param file
     * @param fileName
     */
    public static void downloadExcel( HttpServletRequest request, HttpServletResponse response, File file,
            String fileName ) {
        OutputStream outputStream = null;
        try {
            byte[] data;
            data = FileUtils.readFileToByteArray( file );
            String userAgent = request.getHeader( "User-Agent" );
            response.reset();
            response.setHeader( "Content-Disposition",
                    "attachment; filename=" + StringUtil.encodeFileName( fileName + ".xls", userAgent ) );
            response.addHeader( "Content-Length", Integer.toString( data.length ) );
            response.setContentType( "application/octet-stream;charset=UTF-8" );
            outputStream = new BufferedOutputStream( response.getOutputStream() );
            outputStream.write( data );
            outputStream.flush();
            FileUtils.deleteQuietly( file );
        } catch ( Exception ex ) {
            logger.error( "method is  downloadExcel=========" + ex.getMessage() );
        } finally {
            if ( null != outputStream ) {
                try {
                    outputStream.close();
                } catch ( IOException ex ) {
                    logger.error( "method is  downloadExcel=========" + ex.getMessage() );
                }
            }
        }
    }

}
