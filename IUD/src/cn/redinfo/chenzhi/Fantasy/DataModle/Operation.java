
package cn.redinfo.chenzhi.Fantasy.DataModle;

public class Operation {

  private String iudtime;

  private String iudType;

  private String peopleCard;

  private String peopleName;

  private int peopleAge;

  private String peoplePhone;

  private String doctorName;

  private String productBatch;

  private String productName;

  private String productCode;

  private String manufacturer;

  private String takeId;

  private String factoryCode;

  public Operation(String iudtime,
                   String iudType,
                   String peopleCard,
                   String peopleName,
                   int peopleAge,
                   String peoplePhone,
                   String doctorName,
                   String productBatch,
                   String productName,
                   String productCode,
                   String manufacturer,
                   String takeId,
                   String factoryCode)
  {
    this.iudtime = iudtime;
    this.iudType = iudType;
    this.peopleCard = peopleCard;
    this.peopleName = peopleName;
    this.peopleAge = peopleAge;
    this.peoplePhone = peoplePhone;
    this.doctorName = doctorName;
    this.productBatch = productBatch;
    this.productName = productName;
    this.productCode = productCode;
    this.manufacturer = manufacturer;
    this.takeId = takeId;
    this.factoryCode = factoryCode;
  }
}
