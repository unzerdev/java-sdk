package com.heidelpay.payment.communication.json;

/**
 * Alipay business object 
 * 
 * @author rene.felder
 *
 */
public class JsonApplepay extends JsonIdObject implements JsonObject  {
	private String version;
	private String data;
	private String signature;
	private JsonApplepayHeader header;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public JsonApplepayHeader getHeader() {
		return header;
	}

	public void setHeader(JsonApplepayHeader header) {
		this.header = header;
	}

}
