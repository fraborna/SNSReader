package nju.fraborna.sns21.netservice;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class PostUtil {

	public String publish(String url, List<NameValuePair> params) {
		// TODO Auto-generated method stub

		// instantiate HttpPost object from the url address
		HttpEntityEnclosingRequestBase httpRequest = new HttpPost(url);

		// the post name and value must be used as NameValuePair
		try {
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

			// execute the post and get the response from servers
			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(httpRequest);

			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				// get the result
				String strResult = EntityUtils.toString(httpResponse
						.getEntity());

				return strResult;
			} else {
				Log.w("Publish Error", "Error Response"
						+ httpResponse.getStatusLine().toString());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "error";
	}

}
