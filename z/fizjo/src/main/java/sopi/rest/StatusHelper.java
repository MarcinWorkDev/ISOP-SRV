package sopi.rest;

public class StatusHelper {

	boolean ok;
	String status;
	Object body;
	
	public StatusHelper(boolean ok) {
		this.ok = ok;
	}
	
	public StatusHelper(boolean ok, String status) {
		super();
		this.ok = ok;
		this.status = status;
	}
	
	public StatusHelper(boolean ok, String status, Object body) {
		this.ok = ok;
		this.status = status;
		this.body = body;
	}

	public boolean isOk() {
		return ok;
	}

	public void setOk(boolean ok) {
		this.ok = ok;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}
	
	
}
