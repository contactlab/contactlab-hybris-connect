package com.contactlab.services;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.store.BaseStoreModel;

import java.util.List;

public interface ContactlabCartService
{

    List<CartModel> getAbandonedCarts(List<BaseStoreModel> stores, Integer hours, Integer notificationNumber,
            Boolean exportGuest);

    CartModel recalculateCart(CartModel cartModel);

    CartData getCartData(CartModel cartModel);

    List<ProductData> getCrosssellingProducts(final CartModel cartModel, final int limit);

}
