/******************************************************************************
 * @author Wang Kun
 *         ID: DG1333031
 *         Email: nju.wangkun@gmail.com
 *         Date: 2014-7-26
 * @file MethodFactory.java
 * @brief Brief Description
 ******************************************************************************/
package util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class MethodFactory
{
    @SuppressWarnings(
    { "rawtypes" })
    public static List deepCopy(List src)
    {
        ByteArrayOutputStream byteOut = null;
        ObjectOutputStream out = null;
        ByteArrayInputStream byteIn = null;
        ObjectInputStream in = null;
        List dest = null;
        try
        {
            byteOut = new ByteArrayOutputStream();
            out = new ObjectOutputStream(byteOut);
            out.writeObject(src);
            byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            in = new ObjectInputStream(byteIn);
            dest = (List) in.readObject();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            // TODO: handle exception
            e.printStackTrace();
        }
        return dest;
    }
    
    public static void copyFile(String oldPath, String newPath)
    {
        int byteread = 0;
        byte[] buffer = new byte[1024];
        FileInputStream inStream = null;
        FileOutputStream outStream = null;
        try
        {
            inStream = new FileInputStream(oldPath);
            outStream = new FileOutputStream(newPath);
        }
        catch (FileNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try
        {
            while ((byteread = inStream.read(buffer)) != -1)
            {
                outStream.write(buffer, 0, byteread);
            }
            inStream.close();
            outStream.flush();
            outStream.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
