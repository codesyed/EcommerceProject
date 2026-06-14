package com.ecommerce.project.service;

import com.ecommerce.project.exception.APIException;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Cart;
import com.ecommerce.project.model.CartItem;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.CartDTO;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.repositories.CartItemRepository;
import com.ecommerce.project.repositories.CartRepository;
import com.ecommerce.project.repositories.ProductRepository;
import com.ecommerce.project.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CartServiceImpl implements CartService{

    @Autowired
    AuthUtil authUtil;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public CartDTO addProductToUserCart(Long productId, Integer quantity) {
        /*
            -> check whether this user has already cart or not : if not create one if yes take out the existing one
            -> Retrieve the product details
                Then: perform quantity validations
            -> Create a CartItem & save it
            -> Update Cart of user and save updated cart
            -> Create CartDTO from Updated Cart
            -> return CartDTO
         */

        //Can we use UserId instead of email?

        //1) Check if Cart Exists for LoggedIn User if Not create.
        Cart userCart = createOrFindCart(authUtil.loggedInEmail());

        //2) Fetch product Details
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("Product", "productId", productId));

        //3) Checking whether this product already exists in Cart or Not
        //As we don't have productId and cartId directly in our CartItem class we need custom query
        //Can we use JPA here??
        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(
                productId,
                userCart.getCartId()
        );

        if(cartItem != null)
            throw new APIException("Product "+product.getProductName()+" already exists in your Cart!!!");

        if(product.getQuantity() <= 0)
            throw new APIException("Product "+product.getProductName()+" Is out of stock!!!");

        if(product.getQuantity() < quantity)
            throw new APIException("Max Quantity in Stock for Product "+product.getProductName()+" is: "+product.getQuantity());

        //4) Creating CartItem
        CartItem newCartItem= new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setCart(userCart);
        newCartItem.setQuantityPurchased(quantity);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProductPrice(product.getSpecialPrice());

        //5) SAVING new CartItem created
        cartItemRepository.save(newCartItem);

        //6) adding newcartItem into cart also for java (only not for DB)
        userCart.getCartItemList().add(newCartItem);

        //7) Updating cart
        userCart.setTotalPrice(userCart.getTotalPrice()+(product.getSpecialPrice()*quantity));

        //8) saving updated cart
        cartRepository.save(userCart);

        CartDTO cartDTO = modelMapper.map(userCart, CartDTO.class);

        List<CartItem> cartItemList = userCart.getCartItemList();

        //Inside CartDTO we want List of ProductDTO
        //Inside each CartItem we have Product filed/instance in our Entity Class
        //We can use that filed to Map->cartItem->Product->ProductDTO........
        List<ProductDTO> productDTOList = cartItemList.stream()
                .map(cartItem1 -> {
                    ProductDTO productDTOMapped = modelMapper.map(
                            cartItem1.getProduct(), ProductDTO.class
                    );

                    //This quantity is what buyer is ordering & Not Actual Stock count of this product.
                    //Quantity inside Product is what is in Stock NOT user carts Quantity
                    productDTOMapped.setQuantity(quantity);

                    return productDTOMapped;
                })
                .toList();

        cartDTO.setProducts(productDTOList);
        return cartDTO;
    }

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> cartList = cartRepository.findAll();

        if (cartList.isEmpty())
            throw new APIException("No Carts Exists!");

        //CartDTO has List<ProductDTO> while Cart has List<CartItem>
        List<CartDTO> cartDTOList =
                cartList.stream()
                .map((cart)-> {
                            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

                            //CartDto also have List<ProductDTO>
                            List<CartItem> cartItems = cart.getCartItemList();

                            //Each cartItem will be changed to ProductDTO
                            List <ProductDTO> productDTOList = cartItems.stream()
                                    .map((item)->{
                                        //Product mapped to that cartItem
                                        Product p = item.getProduct();

                                        //Product TO productDTO Conversion
                                        ProductDTO productDTO=modelMapper.map(p, ProductDTO.class);

                                        //Quantity inside Product is what is in Stock NOT user carts Quantity
                                        productDTO.setQuantity(item.getQuantityPurchased());
                                        return productDTO;
                                    }).collect(Collectors.toList());
                            cartDTO.setProducts(productDTOList);
                            return cartDTO;
                        }
                )
                .collect(Collectors.toList());
        return cartDTOList;
    }

    @Override
    public CartDTO getLoggedInUserCart(String emailId, Long cartId) {
        Cart cart=cartRepository.findCartByUserEmailAndCartId(emailId, cartId);
        if(cart==null)
            throw new ResourceNotFoundException("Cart", "cartId", cartId);

        CartDTO cartDTO=modelMapper.map(cart, CartDTO.class);
        //CartDTO has List<ProductDTO> while Cart has List<CartItem>
        List<CartItem> cartItems=cart.getCartItemList();

        Stream<Product> productStream =
                cartItems.stream()
                        .map(cartItem-> {
                                    Product p = cartItem.getProduct();
                                    //Quantity inside Product is what is in Stock NOT user carts Quantity
                                    p.setQuantity(cartItem.getQuantityPurchased());
                                    return p;
                                }
                        );

        List<ProductDTO> productDTOS =
                productStream
                        .map(product ->
                            modelMapper.map(product, ProductDTO.class)
                        )
                        .toList();

        cartDTO.setProducts(productDTOS);
        return cartDTO;
    }

    private Cart createOrFindCart(String s) {
        Cart usercart = cartRepository.findCartByUserEmail(s);
        if(usercart != null)return usercart;

        Cart cart = new Cart();
        cart.setUser(authUtil.loggedInUser());
        cart.setTotalPrice(0.0);
        return cartRepository.save(cart);
    }


}
