import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.GenerationType
import groovy.transform.ToString
import groovy.transform.EqualsAndHashCode
import javax.persistence.Table
import javax.persistence.Column

@ToString
@EqualsAndHashCode
@Entity
@Table(name="invoice")
class invoice {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  Long id

  Meta meta

  String invoicedate
  String invoicenumber

  Invoicecompany invoicecompany  
 
  Invoicebillto invoicebillto
  Invoicebillto invoiceserviceto
 
  String ponumber
  String salesrep
  String servicedate
  String terms
  String duedate

  List productdetails
  
  String notes
  String subtotal
  String discountpercent
  String discountamount
  String total
  String paid
  String totaldue
  
}

class Meta {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  Long id
  
  String sourcefilename
  String sheetno
}

class Invoicecompany {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  Long id
  
  String addressline1
  String addressline2
  String contact
  String website
}

class Invoicebillto {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  Long id
  
  String name
  String addressline1
  String addressline2
  String addressline3
}

class productdetail {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  Long id
  
  String productid
  String productdesc
}
