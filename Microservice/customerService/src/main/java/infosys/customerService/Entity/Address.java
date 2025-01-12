//package infosys.customerService.Entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//
//
//@Entity
//@Table(name="address")
//public class Address {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long addressId;
//    private String street;
//    private String postalCode;
//    private String city;
//    private String country;
//
//    @OneToOne()
//    @JoinColumn(name = "customer_id", nullable = false)
//    private Customer customer;
//
//
//    public String getAddressId() {
//        return addressId;
//    }
//
//    public void setAddressId(String addressId) {
//        this.addressId = addressId;
//    }
//
//    public String getStreet() {
//        return street;
//    }
//
//    public void setStreet(String street) {
//        this.street = street;
//    }
//
//    public String getPostalCode() {
//        return postalCode;
//    }
//
//    public void setPostalCode(String postalCode) {
//        this.postalCode = postalCode;
//    }
//
//    public String getCity() {
//        return city;
//    }
//
//    public void setCity(String city) {
//        this.city = city;
//    }
//
//    public String getCountry() {
//        return country;
//    }
//
//    public void setCountry(String country) {
//        this.country = country;
//    }
//
//    public Customer getCustomer() {
//        return customer;
//    }
//
//    public void setCustomer(Customer customer) {
//        this.customer = customer;
//    }
//
//    @Override
//    public String toString() {
//        return "Address{" +
//                "addressId='" + addressId + '\'' +
//                ", street='" + street + '\'' +
//                ", postalCode='" + postalCode + '\'' +
//                ", city='" + city + '\'' +
//                ", country='" + country + '\'' +
//                ", customer=" + customer +
//                '}';
//    }
//}
