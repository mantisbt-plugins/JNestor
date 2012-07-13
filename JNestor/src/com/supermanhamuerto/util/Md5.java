package com.supermanhamuerto.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class Md5
{
	private static final char[] HEXADECIMAL = 
		{ '0', '1', '2', '3', '4', '5', 
		  '6', '7', '8', '9', 'a', 'b', 
		  'c', 'd', 'e', 'f' };

	private static Log log = LogFactory.getLog( Md5.class );
	
    public static String hash(String stringToHash)  
    {
        try 
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(stringToHash.getBytes());
            StringBuilder sb = new StringBuilder(2 * bytes.length);
            
            for (int i = 0; i < bytes.length; i++) 
            {
            	int low = (int)(bytes[i] & 0x0f);
                int high = (int)((bytes[i] & 0xf0) >> 4);
                sb.append(HEXADECIMAL[high]);
                sb.append(HEXADECIMAL[low]);
            }
            return sb.toString();
            
        } catch (NoSuchAlgorithmException e) 
        {
            log.error( e );
            return null;
        } // catch 
    } // hash
}
