package com.contactlab.daos;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.store.BaseStoreModel;

import java.util.List;

/**
 * @author openmind
 */
public interface ContactlabCartDao
{

    List<CartModel> searchAbandonedCarts(List<BaseStoreModel> stores, Integer hours, Integer notificationNumber,
            Boolean exportGuest);

}
