package tuwien.aic.crowdsourcing.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public abstract class HttpUtil {
	private static final String USER_AGENT = "tuwien-crowdsourcing";
	
	public static final int DEFAULT_TIMEOUT = 20000;
	public static final Charset UTF8 = Charset.forName("UTF-8");
	
	public static HttpResponse request(String url, HttpRequestMethod method, Map<String, String> headers, Map<String, String> parameters, InputStream body, int timeout, boolean encodeParameters) {

        String completeUrl = url;
       
        String appendToken = "?";
        if(url.contains("?")){
        	appendToken = "&";
        }
        if (parameters != null) {
            completeUrl = url + appendToken + buildQueryString(parameters, encodeParameters);
        }
      
//        System.out.println("URL sent: " + completeUrl);

        try {
            URL u = new URL(completeUrl);
            HttpURLConnection uc = (HttpURLConnection) u.openConnection();
            uc.setConnectTimeout(timeout);
            uc.setReadTimeout(timeout);
            uc.setRequestProperty("User-Agent", USER_AGENT);
            uc.setRequestMethod(method.toString());
            
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    uc.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            uc.setUseCaches(false);
            uc.setDoInput(true);
            uc.setDoOutput(true);
            
            if(body != null) {
            	IOUtil.copy(body, uc.getOutputStream(), false);
            }
            
            uc.connect();
//            if (!(uc.getResponseCode() == HttpURLConnection.HTTP_OK)) {
//                System.out.println("HTTP Response Code: " + uc.getResponseCode() + " for URL: " + uc.getURL());
//                return null;
//            }

            InputStream is = null;
            try {
            	is = uc.getInputStream();
            } catch(IOException io) {
            	is = uc.getErrorStream();
            }
            
            String contentEncoding = uc.getContentEncoding();
            if (contentEncoding != null && contentEncoding.equalsIgnoreCase("gzip")) {
                is = new GZIPInputStream(is);
            }
            
            return new HttpResponse(uc.getResponseCode(), is);
            
        } catch (SocketTimeoutException e) {
            System.out.println("Socket timed out (" + e.getClass() + ") for URL " + completeUrl);
        } catch (FileNotFoundException e) {
            System.out.println("File not found (" + e.getClass() + ") for URL " + completeUrl);
        } catch (IOException e) {
            System.out.println("IOException (" + e.getClass() + ") for URL " + completeUrl);
        }

        return null;
	}
    
    public static String buildQueryString(Map<String, String> parameters) {
    	return buildQueryString(parameters, true);
    }

    public static String buildQueryString(Map<String, String> parameters, Boolean encode) {
        if (parameters == null || encode == null) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }

            String value = entry.getValue();
            if (value != null && encode) {
                value = urlEncode(value);
            }
            builder.append(entry.getKey()).append("=").append(value);
        }
        return builder.toString();
    }
    
    public static String urlEncode(String string) {
        if(string == null){
            return null;
        }
        try {
            return URLEncoder.encode(string, UTF8.name());
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Impossible UTF-8 not available?", e);
        }
    }

    public static String urlDecode(String string) {
        if(string == null){
            return null;
        }
        try {
            return URLDecoder.decode(string, UTF8.name());
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Impossible UTF-8 not available?", e);
        }
    }
	
	public static enum HttpRequestMethod {
		POST, GET, PUT, DELETE
	}
	
	public static class HttpResponse {
		
		private InputStream body;
		private int responseCode;
		
		private HttpResponse(int responseCode, InputStream body) {
			this.body = body;
			this.responseCode = responseCode;
		}
		
		public InputStream getBody() {
			return body;
		}
		
		public int getResponseCode() {
			return responseCode;
		}
	}
	
	public static class HeaderFields {
		public static class Request {
			public static final String CONTENT_TYPE = "Content-Type";
			public static final String CONTENT_LENGTH = "Content-Length";
			public static final String AUTHORIZATION = "Authorization";
		}
	}
}
