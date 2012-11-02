import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.GenerationType
import javax.persistence.CascadeType
import groovy.transform.ToString
import groovy.transform.EqualsAndHashCode
import javax.persistence.Table
import javax.persistence.Column
import javax.persistence.OneToOne
import javax.persistence.OneToMany
import javax.persistence.PrimaryKeyJoinColumn
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.JoinTable

import org.hibernate.annotations.IndexColumn

@ToString
@EqualsAndHashCode
@Entity
@Table(name="invoice")
class Invoice {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name="invoiceId")
  Long id

 	@OneToOne(cascade=CascadeType.ALL)
  InvoiceMeta meta

  String invoicedate
  String invoicenumber
 
  String ponumber
  String salesrep
  String servicedate
  String terms
  String duedate

 	@OneToOne(cascade=CascadeType.ALL)
  Invoicecompany invoicecompany  
 
 	@OneToOne(cascade=CascadeType.ALL)
  Invoicebillto invoicebilledto
  
	@OneToOne(cascade=CascadeType.ALL)
  Invoicebillto invoiceservicedto


  @JoinTable(name = "Invoice_for_ProductDetail")
  @OneToMany(cascade=CascadeType.ALL)
  List<productdetail> productdetails

  String notes
  String subtotal
  String discountpercent
  String discountamount
  String total
  String paid
  String totaldue
 
}

@Entity
@ToString
@EqualsAndHashCode
@Table(name="invoice_meta")
class InvoiceMeta {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  Long id
  
  String sourcefilename
  String sheetno
}

@Entity
@ToString
@EqualsAndHashCode
@Table(name="invoice_company")
class Invoicecompany {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  Long id
  
  String addressline1
  String addressline2
  String contact
  String website
}

@Entity
@ToString
@EqualsAndHashCode
@Table(name="invoice_billto")
class Invoicebillto {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  Long id
  
  String name
  String addressline1
  String addressline2
  String addressline3

}

@Entity
@ToString
@EqualsAndHashCode
@Table(name="invoice_productdetail")
class productdetail {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "productdetailId")
  Long id
  
  String productid
  String productdesc

}