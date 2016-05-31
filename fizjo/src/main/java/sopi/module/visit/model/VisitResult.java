package sopi.module.visit.model;

public class VisitResult {

	Long visitId;
	String status;
	String result;
	
	public Long getVisitId(){
		return visitId;
	}
	
	public void setVisitId(Long visitId){
		this.visitId = visitId;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
	
}
