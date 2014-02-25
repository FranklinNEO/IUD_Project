package cn.redinfo.chenzhi.Fantasy.DataModle;

/**
 * Created with IntelliJ IDEA. User: orinchen Date: 13-1-5 Time: 上午11:10
 */
public class BarcodeInfo {
	private String barcode = null;
	private String prodName = null;
	private String prodId = null;
	private String producers = null;
	private String producersId = null;
	private String batchcode = null;

	public BarcodeInfo(String barcode, String prodName, String prodId,
			String producers, String producersId, String batchcode) {
		this.barcode = barcode;
		this.prodName = prodName;
		this.prodId = prodId;
		this.producers = producers;
		this.producersId = producersId;
		this.batchcode = batchcode;
	}

	public String getBarcode() {
		return barcode;
	}

	public String getBatchcode() {
		return batchcode;
	}

	public void setBatchcode(String batchcode) {
		this.batchcode = batchcode;
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

	public String getProdId() {
		return prodId;
	}

	public void setProdId(String prodId) {
		this.prodId = prodId;
	}

	public String getProducers() {
		return producers;
	}

	public void setProducers(String producers) {
		this.producers = producers;
	}

	public String getProducersId() {
		return producersId;
	}

	public void setProducersId(String producersId) {
		this.producersId = producersId;
	}

}
