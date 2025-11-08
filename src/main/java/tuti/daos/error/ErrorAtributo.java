package tuti.daos.error;

public class ErrorAtributo {

	
	/**
	 * Atributo al que se asocia el error
	 */
	private String atributo;
	/**
	 * Descripción del error
	 */
	private String error;
	
	public ErrorAtributo(String field, String errorMessage) {
		atributo=field;
		error=errorMessage;
	}
	
	public String getAtributo() {
		return atributo;
	}
	public void setAtributo(String atributo) {
		this.atributo = atributo;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	

	
}
