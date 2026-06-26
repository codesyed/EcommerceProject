package com.ecommerce.project.service;

import com.ecommerce.project.exception.APIException;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Cart;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.repositories.CartItemRepository;
import com.ecommerce.project.repositories.CartRepository;
import com.ecommerce.project.repositories.CategoryRepository;
import com.ecommerce.project.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private FileService fileService; //For updating Image of Product

    @Value("${project.images}")
    private String path;

    @Autowired
    private ModelMapper modelMapper; //do we have to install dependency?

    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {
        //Check whether the category for which new Product is coming already exists or not
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()->
                        new ResourceNotFoundException("Category", "CategoryId", categoryId));

        // Validation - Check -> Same product name should not repeat INSIDE SAME CATEGORY //
        boolean isAlreadyPresent = false;
        List<Product>products = category.getProducts();
        //This loop can be replaced by short codes
        for(int i=0; i<products.size(); i++){
            if(products.get(i).getProductName().equalsIgnoreCase(productDTO.getProductName())){
                isAlreadyPresent = true;
                break;
            }
        }

        if(isAlreadyPresent)
            throw new APIException("In category: "+categoryId+", This named Product already exists!");

        Product product = modelMapper.map(productDTO, Product.class);

        product.setImage("defaultImg.png");
        product.setCategory(category);
        double specialPrice = product.getPrice() - (product.getPrice() * (product.getDiscount()/100.0));
        product.setSpecialPrice(specialPrice);

        //what other things to set in product
        return modelMapper.map(productRepository.save(product), ProductDTO.class);
    }

    @Override
    public ProductResponse getProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sort = sortOrder.equalsIgnoreCase("asc") ?
                    Sort.by(sortBy).ascending() :
                    Sort.by(sortBy).descending() ;

        Pageable pageableObject = PageRequest.of(pageNumber, pageSize, sort);

        Page<Product> productPage = productRepository.findAll(pageableObject);

        List<Product> productList = productPage.getContent();
        if(productList.isEmpty())
            throw new APIException("No Product Exists!");

        List<ProductDTO> productDTOList =
                 productList.stream()
                .map(product->modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOList);
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setLastPage(productPage.isLast());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setPageSize (productPage.getSize());
        return productResponse;
    }

    @Override
    public ProductResponse getProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category", "CategoryId", categoryId));

        Sort sort = sortOrder.equalsIgnoreCase("asc")?
                    Sort.by(sortBy).ascending():
                    Sort.by(sortBy).descending();

        Pageable pageableObject = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product>productPage = productRepository.findByCategory( category, pageableObject);

        List<Product> productList = productPage.getContent();

        List<ProductDTO> productDTOList =
                 productList.stream()
                .map(product->modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOList);
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setLastPage(productPage.isLast());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setPageSize (productPage.getSize());

        return productResponse;
    }

    @Override
    public ProductResponse getProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sort = sortOrder.equalsIgnoreCase("Asc")?
                    Sort.by(sortBy).ascending():
                    Sort.by(sortBy).descending();

        Pageable pageableObject  = PageRequest.of(pageNumber, pageSize, sort);

        /*                                                  IMP                             */
        //Appending % to utilise Like of SQL
        Page<Product> productPage = productRepository.findByProductNameLikeIgnoreCase("%"+keyword+"%", pageableObject);

        List<Product> productList = productPage.getContent();

        if(productList.isEmpty())
            throw new APIException("No Product Exists with this keyword!");

        List<ProductDTO> productDTOList =
                productList.stream()
                        .map(product->modelMapper.map(product, ProductDTO.class))
                        .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOList);
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setLastPage(productPage.isLast());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setPageSize (productPage.getSize());
        return productResponse;
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        Product dbProduct = productRepository.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("Product", "ProductId", productId));

        Product product = modelMapper.map(productDTO, Product.class);
        dbProduct.setProductName(product.getProductName());
        dbProduct.setDescription(product.getDescription());
        dbProduct.setQuantityInStock(product.getQuantityInStock());
        dbProduct.setDiscount(product.getDiscount());
        dbProduct.setPrice(product.getPrice());
        //Let's here take special Price from user for simplicity
        dbProduct.setSpecialPrice(product.getSpecialPrice());
        Product savedProduct = productRepository.save(dbProduct);

        /* Edge Case - We have updated Our Product fields and attributes BUT*/
        /* This product is under the Cart -> CartItem -> product therefore
           each cartItem + Cart(price) that has this product needs to be updated.....
        */

        //Getting All carts that used this productId - Custom Query needed
        List<Cart> cartsList = cartRepository.findCartsByProductId(productId);

        cartsList.forEach(cart -> cartService.updateCartItemInCart(cart.getCartId(), savedProduct));

        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO deleteService(Long productId) {
        Product dbProduct = productRepository.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("Product", "ProductId", productId));

        /* Edge Case - We have updated Our Product fields and attributes BUT*/
        /* This product is under the Cart -> CartItem -> product therefore
           each cartItem + Cart(price) that has this product needs to be updated.....
        */

        //Getting All carts that used this productId - Custom Query needed
        List<Cart> cartsList = cartRepository.findCartsByProductId(productId);
        for(Cart cart : cartsList)
            cartService.deleteItemsFromCart(productId, cart.getCartId());

        productRepository.deleteById(productId);
        return modelMapper.map(dbProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        Product productFromDb = productRepository.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("Product", "ProductId", productId));

        //** path is taken from application.properties

        String newImageFileName = fileService.uploadImage(path, image);
        productFromDb.setImage(newImageFileName);

        return modelMapper.map(productRepository.save(productFromDb), ProductDTO.class);
    }
    /*                      ***Update Product Image***
        -> Get the product from Database
        -> Upload image to server: Image from Postman(API) to Server
        -> Get file-Name/Extension of user's uploaded image.
        -> Update the coming fileImage name to newUnique name.
        -> return DTO after mapping.
     */

}
