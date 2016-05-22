package request_handler;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class ApiRequestTask extends AsyncTask<String, String, String>{

	private int callType;
	
	public ApiRequestTask(Context context, int callType){
		this.callType = callType;
	}
	
	/* (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
	 */
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		if (callType==0) {
			// For test the server request;
			JSONObject obj = null;
			try {
				obj = getOnly(getTestRequestInfo());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			return convertJSONtoString(obj);
		}else{
			return "";
		}
	}
	
	private String convertJSONtoString(JSONObject obj){
		return obj.toString();
	}
	
	private JSONObject getOnly(RequestInfo info){
		String url = info.getUrl();
		String[] header = info.getHeader();
		HttpClient client = new DefaultHttpClient(); 
		HttpGet get = new HttpGet(url); 
		get.setHeader(header[0], header[1]);
		JSONObject result = null;
		try{
			HttpResponse response = client.execute(get);
			String content = EntityUtils.toString(response.getEntity()); 
			result = new JSONObject(content); 
		}catch(Exception e){
			Log.e("ERror", e.getMessage());
		}
		return result;
	}
	
	private JSONObject postOnly(RequestInfo info){
		String url = info.getUrl();
		String[] header = info.getHeader();
		HttpClient client = new DefaultHttpClient(); 
		HttpPost post = new HttpPost(url); 
		post.setHeader(header[0], header[1]);
		JSONObject result = null;
		try{
			HttpResponse response = client.execute(post);
			String content = EntityUtils.toString(response.getEntity()); 
			result = new JSONObject(content); 
		}catch(Exception e){
			Log.e("ERror", e.getMessage());
		}
		return result;
	}
	
	private RequestInfo getTestRequestInfo() throws JSONException{
		JSONObject testApiSettings = loadSettingsFromCfg();
		String url = (String)testApiSettings.get("base_url") + (String)testApiSettings.get("test_appendix");
		String header = (String)testApiSettings.get("base_header");
		Log.e("url", url);
		return new RequestInfo(url, header);
	}
	
	// Need to parse json file
	private JSONObject loadSettingsFromCfg() throws JSONException{
		JSONObject result = new JSONObject();
		result.put("base_url", "http://127.0.0.1:8000/");
		result.put("test_appendix", "api_v1/group_belong/");
		result.put("base_header", "Content-type application/json");
		
		return result;
	}
	
	class RequestInfo{
		private String url;
		private String header;
		private JSONObject data;
		
		public RequestInfo(String url, String header, JSONObject data) {
			this.url = url;
			this.header = header;
			this.data = data;
		}
		
		public RequestInfo(String url, String header){
			this.url = url;
			this.header = header;
		}

		public String getUrl() {
			return url;
		}

		public String[] getHeader() {
			String[] result = header.split(" ");
			return result;
		}

		public JSONObject getData() {
			return data;
		}
		
	}
}
