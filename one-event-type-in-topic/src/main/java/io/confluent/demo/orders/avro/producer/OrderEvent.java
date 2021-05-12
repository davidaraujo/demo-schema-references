package io.confluent.demo.orders.avro.producer;

import io.confluent.demo.orders.avro.pojo.Customer;
import io.confluent.demo.orders.avro.pojo.Order;
import io.confluent.demo.orders.avro.pojo.Payment;
import io.confluent.demo.orders.avro.pojo.Product;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OrderEvent {

    public static Order getOrder() {
        Random r = new Random();
        // order
        Order order = new Order();
        order.setOrderId(r.nextLong());
        order.setOrderDate(Instant.now().getEpochSecond());
        order.setOrderAmount(r.nextDouble());
        // products
        List<Product> listProducts = new ArrayList<Product>();
        int n = 0;
        while (n < 10) {
            Product product = new Product();
            long productId = r.nextLong();
            product.setProductId(productId);
            product.setProductName("product-" + productId);
            product.setProductPrice(r.nextDouble());
            listProducts.add(product);
            n++;
        }
        // payment method
        Payment paymentMethod = new Payment();
        paymentMethod.setPaymentMethodCode(1);
        paymentMethod.setCardNumber(r.nextLong());
        paymentMethod.setCvv(r.nextInt());
        paymentMethod.setExpirationDate("06/22");
        // customer
        Customer customer = new Customer();
        long customerId = r.nextLong();
        customer.setCustomerId(customerId);
        customer.setCustomerName("David Araujo " + customerId);
        customer.setCustomerAddress("Palo Alto " + customerId);
        customer.setCustomerEmail("araujo" + customerId + "@confluent.io");

        // add nested object to the Order
        order.setProducts(listProducts);
        order.setPaymentMethod(paymentMethod);
        order.setCustomer(customer);

        return order;
    }
}