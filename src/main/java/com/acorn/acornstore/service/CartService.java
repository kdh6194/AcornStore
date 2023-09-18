package com.acorn.acornstore.service;

import com.acorn.acornstore.domain.Cart;
import com.acorn.acornstore.domain.CartItem;
import com.acorn.acornstore.domain.Product;
import com.acorn.acornstore.domain.User;
import com.acorn.acornstore.domain.repository.CartItemRepository;
import com.acorn.acornstore.domain.repository.ProductRepository;
import com.acorn.acornstore.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;  // 추가

    @Transactional
    public void addToCart(Long userId, Long productId, int quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + userId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + productId));

        Cart cart = user.getCart();

        Optional<CartItem> optionalCartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().equals(product))
                .findFirst();

        if (optionalCartItem.isPresent()) {
            // 이미 장바구니에 있는 상품인 경우 수량만 변경한다.
            CartItem cartItem = optionalCartItem.get();
            int updatedQuantity = cartItem.getItemQuantity() + quantity;
            if (updatedQuantity > 0) {
                cartItem.setItemQuantity(updatedQuantity);
            } else {
                // 수량이 0 이하일 경우 아이템 삭제
                cart.getItems().remove(cartItem);
                cartItemRepository.delete(cartItem);  // 추가
            }
        } else {
            // 새로운 상품인 경우 장바구니에 추가한다.
            if (quantity > 0) {
                CartItem newItem = new CartItem();
                newItem.setProduct(product);
                newItem.setItemQuantity(quantity);

                cart.getItems().add(newItem);
            }
        }

        // 총 가격 업데이트
        updateTotalPrice(cart);

        userRepository.save(user); // 변경된 내용 저장

    }

    public void removeProductFromCard(Long userId, Long productId){
        User user= userRepository.findById(userId).orElseThrow(()->new RuntimeException("User not found"));
        Cart cart=user.getCart();
        if(cart==null){
            throw new RuntimeException("No products in the cart");
        }

        List<CartItem> items=cart.getItems();

        Optional<CartItem> optionalItem=items.stream().filter(i->i.getProduct().getProductId()==productId).findFirst();

        if(optionalItem.isPresent()){
            CartItem itemToRemove=optionalItem.get();  // 변경: optionalItems -> optionalItem

            items.remove(itemToRemove);
            cartItemRepository.delete(itemToRemove);  // 추가

            // Update total price and quantity after removing an item
            updateTotalPrice(cart);

            int totalQunatityAfterRemove=cart.getCartCount()-itemToRemove.getItemQuantity();  // 오타 수정
            cart.setCartCount(totalQunatityAfterRemove);

            // Save the updated state to the database
            userRepository.save(user);  // 변경

        }else{
            throw new RuntimeException("Product not found in the cart");
        }
    }

    private void updateTotalPrice(Cart cart){
        int totalPrice=cart.getItems().stream()
                .mapToInt(item->item.getProduct().getProductPrice()*item.getItemQuantity())
                .sum();

        cart.setTotalPrice(totalPrice);
    }

    @Transactional
    public void checkout(Long userId, int userBudget) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + userId));

        Cart cart = user.getCart();

        // Check if the total price of the cart is within the user's budget.
        if (cart.getTotalPrice() > userBudget) {
            throw new RuntimeException("Insufficient funds for checkout.");
        }

        // Check if there is enough stock for each item in the cart.
        for (CartItem item : cart.getItems()) {
            Product product = item.getProduct();
            if (product.getProductQuantity() < item.getItemQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getProductId());
            }
        }

        // If all checks pass, proceed with the checkout process.
        // This would involve reducing the stock quantity of each purchased product,
        // and clearing out the items in the cart.
        // Depending on your business requirements, you might also need to create an order record
        // and perform other related tasks.

        for (CartItem item : cart.getItems()) {
            Product product = item.getProduct();
            int remainingQuantity = product.getProductQuantity() - item.getItemQuantity();
            product.setProductQuantity(remainingQuantity);
            productRepository.save(product);
        }

        // Clear out items in the cart after successful purchase
        List<CartItem> itemsToClearOut = new ArrayList<>(cart.getItems());
        for(CartItem item : itemsToClearOut){
            removeProductFromCard(userId,item.getProduct().getProductId());
        }

    }


}

//    public void removeProductFromCard(Long userId, Long productId){
//        User user= userRepository.findById(userId).orElseThrow(()->new RuntimeException("User not found"));
//        Cart cart=user.getCart();
//        if(cart==null){
//            throw new RuntimeException("No products in the cart");
//        }
//
//        List<CartItem> items=cart.getItems();
//
//        Optional<CartItem> optionalItem=items.stream().filter(i->i.getProduct().getProductId()==productId).findFirst();
//
//        if(optionalItem.isPresent()){
//            CartItem itemToRemove=optionalItem.get();  // 변경: optionalItems -> optionalItem
//            int removedPrice=itemToRemove.getProduct().getProductPrice()*itemToRemove.getItemQuantity();
//
//            items.remove(itemToRemove);
//            cartItemRepository.delete(itemToRemove);  // 추가
//            int totalPriceAfterRemove=cart.getTotalPrice()-removedPrice;
//            int totalQunatityAfterRemove=cart.getCartCount()-itemToRemove.getItemQuantity();  // 오타 수정
//
//            cart.setTotalPrice(totalPriceAfterRemove);
//            cart.setCartCount(totalQunatityAfterRemove);
//
//            // Save the updated state to the database
//            userRepository.save(user);  // 변경
//
//        }else{
//            throw new RuntimeException("Product not found in the cart");
//        }
//    }