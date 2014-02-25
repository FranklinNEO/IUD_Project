package cn.redinfo.chenzhi.BarcodeScanner;

/**
 * Created with IntelliJ IDEA. User: orinchen Date: 13-1-6 Time: 下午1:55
 */
public final class BarcodeInfo {
	private String barcode = null;
	private String prodName = null;
	private String prodId = null;
	private String producers = null;
	private String producersId = null;
	private String batchcode=null;

	public String getBatchcode() {
		return batchcode;
	}

	public void setBatchcode(String batchcode) {
		this.batchcode = batchcode;
	}

	public String getProdId() {
		return prodId;
	}

	public void setProdId(String prodId) {
		this.prodId = prodId;
	}

	public String getProducersId() {
		return producersId;
	}

	public void setProducersId(String producersId) {
		this.producersId = producersId;
	}


	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	public String getProducers() {
		return producers;
	}

	public void setProducers(String producers) {
		this.producers = producers;
	}
}
