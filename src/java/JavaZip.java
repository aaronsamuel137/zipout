package java;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

// Our code must be able to do this
public class JavaZip
{
	public static void main(String[] args)
	{
		byte[] buffer = new byte[1024];

		try
		{
			FileOutputStream outFile = new FileOutputStream("java.zip");
			ZipOutputStream outZip = new ZipOutputStream(outFile);
			FileInputStream inFile = new FileInputStream("test.txt");

			ZipEntry entry = new ZipEntry("test.txt");
			outZip.putNextEntry(entry);

			int len = 0;
			while ((len = inFile.read(buffer)) > 0)
			{
				outZip.write(buffer, 0, len);
			}

			inFile.close();
			outZip.closeEntry();
			outZip.close();
		}
		catch (Exception e)
		{

		}
	}
}