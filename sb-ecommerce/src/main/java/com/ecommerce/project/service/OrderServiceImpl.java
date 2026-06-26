package com.ecommerce.project.service;

import com.ecommerce.project.exception.APIException;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.*;
import com.ecommerce.project.payload.OrderDTO;
import com.ecommerce.project.payload.OrderItemDTO;
import com.ecommerce.project.payload.PaymentDTO;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.repositories.*;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CartServiceImpl cartService;

    @Override
    @Transactional
    public OrderDTO placeOrder(String emailId, String paymentMethod, Long addressId, String pgName, String pgPaymentId,
                               String pgStatus, String pgResponseMessage) {
        /*
            -> For this user find the cart, if not exists throw exception
            -> Create Payment object with given details....
                -> save payment (as its independent of Order)
            -> Create an Order object with given details....
                -> add payment object in Order object for relationship
                -> add Address object in Order object
            -> Save order
            -> Fetch all cartItems for that cart
            -> Create list of All -- OrderItem --
            -> save orderItems

           => Post order Placing logic
                -> product stock quantity updated
                -> Clear Cart of user
                -> Create a OrderDTO
                -> return orderDto
         */

        Cart cart = cartRepository.findCartByUserEmail(emailId);
        if(cart==null)throw new ResourceNotFoundException("Cart", "email", emailId);


        Address address = addressRepository.findById(addressId)
                .orElseThrow(()-> new ResourceNotFoundException("Address", "addressId", addressId));

        List<CartItem> cartItemList = cart.getCartItemList();
        if(cartItemList.isEmpty()) throw new APIException("Please add some product in your Cart First");

        Payment payment = new Payment(paymentMethod, pgPaymentId, pgStatus, pgResponseMessage, pgName);
        Payment savedPayment = paymentRepository.save(payment);


        Order order = new Order();
        order.setOrderDate(LocalDate.now());
        order.setOrderStatus("Order-Placed");
        order.setEmail(emailId);
        order.setPayment(savedPayment);
        order.setAddress(address);

        //Creating OrderItem using CartItem
        List<OrderItem> orderItemList = new ArrayList<>();

        cartItemList.forEach(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setQuantityOrdered(cartItem.getQuantityPurchased());
            orderItem.setOrderedProductPrice(cartItem.getProductPrice());
            orderItem.setDiscount(cartItem.getDiscount());
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItemList.add(orderItem);

            System.out.println("** Check Point 1 **");

            order.setTotalAmount(
                    (order.getTotalAmount()==null ? 0.0 : order.getTotalAmount())
                            +
                    (orderItem.getOrderedProductPrice()*orderItem.getQuantityOrdered()));

            System.out.println("** Check Point 2 **");
        });
        order.setOrderItemList(orderItemList);
        Order savedOrder = orderRepository.save(order);


        //Post order placed tasks
        //Making copy of cartItemList to avoid deletion of cartItem while traversing them
        List<CartItem>tempList = new ArrayList<>(cartItemList);

        tempList.forEach(cartItem -> {
            Product product = cartItem.getProduct();

            //Updating stock of Sold product
            product.setQuantityInStock(product.getQuantityInStock() - cartItem.getQuantityPurchased());

            //Removing cartItem from cart
            //cartItemRepository.deleteById(cartItem.getCartItemId());
            //Taking help of one of our service method
            cartService.deleteItemsFromCart(product.getProductId(), cart.getCartId());
        });

        System.out.println("**--Checkpoint 3");

        OrderDTO orderDTO = modelMapper.map(savedOrder, OrderDTO.class);
        List<OrderItemDTO> orderItemDTOList = orderItemList
                .stream()
                .map(orderItem -> {
                    OrderItemDTO orderItemDTO = modelMapper.map(orderItem, OrderItemDTO.class);
                    orderItemDTO.setProductDTO(modelMapper.map(orderItem.getProduct(), ProductDTO.class));
                    return orderItemDTO;
                })
                .toList();
        orderDTO.setOrderItemDTOList(orderItemDTOList);

        System.out.println();
        PaymentDTO paymentDTO = modelMapper.map(savedPayment, PaymentDTO.class);
        orderDTO.setPaymentDTO(paymentDTO);
        return orderDTO;
    }
}
