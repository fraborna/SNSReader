package nju.fraborna.sns21.netservice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.Environment;

public class DownHeadImage {

	public void download(String url, String name) {
		File path = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
	
		File file = new File(path, name);

		path.mkdirs();

		try {
			HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(
					url).openConnection();
			InputStream is = httpURLConnection.getInputStream();
			OutputStream os = new FileOutputStream(file);
			byte[] data = new byte[is.available()];
			is.read(data);
			os.write(data);
			is.close();
			os.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
