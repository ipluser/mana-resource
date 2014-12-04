package org.mana.core.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author pluser
 * @version 0.5 2014/12/4
 */
public class UrlResource extends AbstractResource {

	private URL url;
	
	public UrlResource(URL url) {
		if (url == null) {
			throw new IllegalArgumentException("url must not be null");
		}
		
		this.url = url;
	}
	
	public UrlResource(URI uri) throws MalformedURLException {
		if (uri == null) {
			throw new IllegalArgumentException("uri must not be null");
		}
		
		this.url = uri.toURL();
	}
	
	public UrlResource(String path) throws MalformedURLException {
		if (path == null) {
			throw new IllegalArgumentException("path must not be null");
		}
		
		this.url = new URL(path);
	}
	
	@Override
	public URL getUrl() throws IOException {
		return url;
	}
	
	@Override
	public URI getUri() throws IOException {
		try {
			return url.toURI();
		} catch (URISyntaxException e) {
			throw new IOException("Invalid URI [" + url + "]", e);
		}
	}
	
	@Override
	public InputStream getInputStream() throws IOException {
		URLConnection con = this.url.openConnection();
		
		try {
			return con.getInputStream();
		}
		catch (IOException e) {
			if (con instanceof HttpURLConnection) {
				((HttpURLConnection)con).disconnect();
			}
			throw e;
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		return (obj == this || 
				(obj instanceof UrlResource 
						&& ((UrlResource) obj).url.equals(this.url)));
	}

	@Override
	public int hashCode() {
		return url.hashCode();
	}

	@Override
	public String getDescription() {
		return "url [" + url.toString() + "]";
	}
}