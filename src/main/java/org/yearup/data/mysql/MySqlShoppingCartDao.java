package org.yearup.data.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Category;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {

    public MySqlShoppingCartDao(DataSource dataSource) {
        super(dataSource);
        shoppingCarts = new HashMap<>();
    }

    private Map<Integer, ShoppingCart> shoppingCarts;

    @Override
    public ShoppingCart getByUserId(int userId) {
        return shoppingCarts.get(userId);
    }

    @Override
    public List<ShoppingCart> getAll() {
        return new ArrayList<>(shoppingCarts.values());
    }

    @Override
    public void addProduct(int userId, ShoppingCartItem item) {
        ShoppingCart shoppingCart = shoppingCarts.getOrDefault(userId, new ShoppingCart());
        shoppingCart.getItems().put(item.getProductId(), item);
        shoppingCarts.put(userId, shoppingCart);
    }


    @Override
    public void removeProduct(int userId, int productId) {
        ShoppingCart shoppingCart = shoppingCarts.get(userId);
        if (shoppingCart != null) {
            Map<Integer, ShoppingCartItem> items = shoppingCart.getItems();
            items.remove(productId);
        }
    }


    @Override
    public void updateProductQuantity(int userId, int productId, int quantity) {
        ShoppingCart shoppingCart = shoppingCarts.get(userId);
        if (shoppingCart != null) {
            Map<Integer, ShoppingCartItem> items = shoppingCart.getItems();
            for (ShoppingCartItem item : items.values()) {
                if (item.getProductId() == productId) {
                    item.setQuantity(quantity);
                    break;
                }
            }
        }
    }


    @Override
    public BigDecimal calculateTotalPrice(int userId) {
        ShoppingCart shoppingCart = shoppingCarts.get(userId);
        if (shoppingCart != null) {
            BigDecimal totalPrice = BigDecimal.ZERO;
            Map<Integer, ShoppingCartItem> items = shoppingCart.getItems();
            for (ShoppingCartItem item : items.values()) {
                BigDecimal lineTotal = item.getLineTotal();
                totalPrice = totalPrice.add(lineTotal);
            }
            return totalPrice;
        }
        return BigDecimal.ZERO;
    }

    @Override
    public void update(int userId, ShoppingCartItem item) {
        ShoppingCart shoppingCart = shoppingCarts.get(userId);
        if (shoppingCart != null) {
            Map<Integer, ShoppingCartItem> items = shoppingCart.getItems();
            for (ShoppingCartItem cartItem : items.values()) {
                if (cartItem.getProductId() == item.getProductId()) {
                    cartItem.setQuantity(item.getQuantity());
                    cartItem.setDiscountPercent(item.getDiscountPercent());
                    break;
                }
            }
        }
    }

    @Override
    public void delete(int userId) {
        shoppingCarts.remove(userId);
    }

    @Override
    public void clearCart(int userId) {
            ShoppingCart shoppingCart = shoppingCarts.get(userId);
            if (shoppingCart != null) {
                shoppingCart.getItems().clear();
            }
        }
}

