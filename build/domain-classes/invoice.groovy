
class invoice {
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
  String sourcefilename
  String sheetno
}

class Invoicecompany {
  String addressline1
  String addressline2
  String contact
  String website
}

class Invoicebillto {
  String name
  String addressline1
  String addressline2
  String addressline3
}

class productdetail {
  String productid
  String productdesc
}
