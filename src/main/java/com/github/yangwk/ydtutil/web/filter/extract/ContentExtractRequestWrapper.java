package com.github.yangwk.ydtutil.web.filter.extract;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 提取http请求InputStream内容
 * @author yangwk
 */
public abstract class ContentExtractRequestWrapper extends HttpServletRequestWrapper{
	private static final Logger LOG = LoggerFactory.getLogger(ContentExtractRequestWrapper.class);
	
	private boolean canExtract = false;
	private byte[] content;
	
	public ContentExtractRequestWrapper(HttpServletRequest request) {
		super(request);
		
		if( canExtractContent(request.getContentType()) ) {
			this.canExtract = true;
		}
		
		if(this.canExtract) {
			LOG.debug("HttpServletRequest can extract inputStream content");
			
			extractContent();
		}
	}
	
	
	protected abstract boolean canExtractContent(String contentType);
	
	
	protected byte[] getContent() {
		if(! this.canExtract) {
			throw new IllegalStateException("HttpServletRequest can not extract inputStream content");
		}
		return this.content;
	}
	

	@Override
	public ServletInputStream getInputStream() throws IOException {
		if(! this.canExtract) {
			return super.getInputStream();
		}
		
		return new DelegatingServletInputStream(new ByteArrayInputStream(this.content));
	}

	private void extractContent() {
		ServletInputStream input = null;
		ByteArrayOutputStream output = null;
		try {
			input = super.getInputStream();
			
			output = new ByteArrayOutputStream();
			IOUtils.copy(input, output);
			this.content = output.toByteArray();	//never be null
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}finally {
			IOUtils.closeQuietly(input, output);
		}
	}
	
	public boolean isCanExtract() {
		return canExtract;
	}

	/**
	 * @see org.springframework.mock.web.DelegatingServletInputStream
	 * @author yangwk
	 *
	 */
	public static class DelegatingServletInputStream extends ServletInputStream {
		private final InputStream sourceStream;
		
		
		public DelegatingServletInputStream(InputStream sourceStream) {
			this.sourceStream = sourceStream;
		}

		@Override
		public boolean isFinished() {
			return true;
		}

		@Override
		public boolean isReady() {
			return true;
		}

		@Override
		public void setReadListener(ReadListener readListener) {
			throw new UnsupportedOperationException("ReadListener is not supported");
		}

		@Override
		public int read() throws IOException {
			return this.sourceStream.read();
		}
		
		@Override
		public void close() throws IOException {
			super.close();
			this.sourceStream.close();
		}
		
	}
	
	
}
