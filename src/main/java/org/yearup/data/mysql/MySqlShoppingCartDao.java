package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.ShoppingCartDao;

import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.math.BigDecimal;

import java.util.HashMap;

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
    public void addItem(int userId, ShoppingCartItem item) {
        ShoppingCart shoppingCart = shoppingCarts.getOrDefault(userId, new ShoppingCart());
        shoppingCart.getItems().put(item.getProductId(), item);
        shoppingCarts.put(userId, shoppingCart);
    }


    @Override
    public void removeItem(int userId, int productId) {
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
    public void clearCart(int userId) {
        ShoppingCart shoppingCart = shoppingCarts.get(userId);
        if (shoppingCart != null) {
            shoppingCart.getItems().clear();
        }
    }
}

