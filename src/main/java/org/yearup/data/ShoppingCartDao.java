package org.yearup.data;

import org.yearup.models.Category;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import java.math.BigDecimal;
import java.util.List;

public interface ShoppingCartDao {
    ShoppingCart getByUserId(int userId);

    List<ShoppingCart> getAll();

    void addProduct(int userId, ShoppingCartItem item);

    void removeProduct(int userId, int productId);

    void updateProductQuantity(int userId, int productId, int quantity);

    BigDecimal calculateTotalPrice(int userId);

    void update(int userId, ShoppingCartItem item);

    void delete(int userId);
    void clearCart(int userId);
}
