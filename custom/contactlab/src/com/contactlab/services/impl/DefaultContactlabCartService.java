package com.contactlab.services.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.catalog.enums.ProductReferenceTypeEnum;
import de.hybris.platform.catalog.model.ProductReferenceModel;
import de.hybris.platform.catalog.references.ProductReferenceService;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.product.CommerceProductReferenceService;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.variants.model.VariantProductModel;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.contactlab.daos.ContactlabCartDao;
import com.contactlab.services.ContactlabCartService;
import com.google.common.collect.Lists;

/**
 * @author openmind
 */
public class DefaultContactlabCartService implements ContactlabCartService
{

    private static Logger log = LoggerFactory.getLogger(DefaultContactlabCartService.class);

    private ContactlabCartDao contactlabCartDao;

    private SessionService sessionService;

    private BaseSiteService baseSiteService;

    private CatalogVersionService catalogVersionService;

    private CommerceCartService commerceCartService;

    private CommerceProductReferenceService commerceProductReferenceService;

    private ProductReferenceService productReferenceService;

    private Converter<ProductModel, ProductData> productConverter;

    private Converter<CartModel, CartData> cartConverter;

    protected ContactlabCartDao getContactlabCartDao()
    {
        return contactlabCartDao;
    }

    public void setContactlabCartDao(final ContactlabCartDao contactlabCartDao)
    {
        this.contactlabCartDao = contactlabCartDao;
    }

    protected SessionService getSessionService()
    {
        return sessionService;
    }

    public void setSessionService(final SessionService sessionService)
    {
        this.sessionService = sessionService;
    }

    protected BaseSiteService getBaseSiteService()
    {
        return baseSiteService;
    }

    public void setBaseSiteService(final BaseSiteService baseSiteService)
    {
        this.baseSiteService = baseSiteService;
    }

    protected CatalogVersionService getCatalogVersionService()
    {
        return catalogVersionService;
    }

    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }

    protected CommerceCartService getCommerceCartService()
    {
        return commerceCartService;
    }

    public void setCommerceCartService(final CommerceCartService commerceCartService)
    {
        this.commerceCartService = commerceCartService;
    }

    public CommerceProductReferenceService getCommerceProductReferenceService()
    {
        return commerceProductReferenceService;
    }

    public void setCommerceProductReferenceService(CommerceProductReferenceService commerceProductReferenceService)
    {
        this.commerceProductReferenceService = commerceProductReferenceService;
    }

    protected Converter<ProductModel, ProductData> getProductConverter()
    {
        return productConverter;
    }

    public void setProductConverter(final Converter<ProductModel, ProductData> productConverter)
    {
        this.productConverter = productConverter;
    }

    protected Converter<CartModel, CartData> getCartConverter()
    {
        return cartConverter;
    }

    public void setCartConverter(final Converter<CartModel, CartData> cartConverter)
    {
        this.cartConverter = cartConverter;
    }

    protected ProductReferenceService getProductReferenceService()
    {
        return productReferenceService;
    }

    public void setProductReferenceService(ProductReferenceService productReferenceService)
    {
        this.productReferenceService = productReferenceService;
    }

    @Override
    public List<CartModel> getAbandonedCarts(List<BaseStoreModel> stores, Integer days, Integer notificationNumber,
            Boolean exportGuest)
    {
        return getContactlabCartDao().searchAbandonedCarts(stores, days, notificationNumber, exportGuest);
    }

    @Override
    public CartModel recalculateCart(final CartModel cartModel)
    {
        CartModel recalculatedCartModel = getSessionService().executeInLocalView(new SessionExecutionBody()
        {
            @Override
            public CartModel execute()
            {
                getBaseSiteService().setCurrentBaseSite(cartModel.getSite(), true);
                CommerceCartParameter commerceCartParam = new CommerceCartParameter();
                commerceCartParam.setCart(cartModel);
                try
                {

                    getCommerceCartService().recalculateCart(commerceCartParam);

                    return commerceCartParam.getCart();
                }
                catch (CalculationException e)
                {
                    log.error("Error in recalculateCart {}", cartModel.getCode(), e);
                    return null;
                }
            }
        });
        log.info("{}", recalculatedCartModel);

        return recalculatedCartModel;
    }

    public CartData getCartData(final CartModel cartModel)
    {

        CartData cartData = getSessionService().executeInLocalView(new SessionExecutionBody()
        {
            @Override
            public CartData execute()
            {
                getBaseSiteService().setCurrentBaseSite(cartModel.getSite(), true);
                return getCartConverter().convert(cartModel);
            }
        });

        return cartData;
    }

    @Override
    public List<ProductData> getCrosssellingProducts(final CartModel cartModel, final int limit)
    {

        final List<ProductData> references = Lists.newArrayList();

        getSessionService().executeInLocalView(new SessionExecutionBody()
        {
            @Override
            public void executeWithoutResult()
            {

                getBaseSiteService().setCurrentBaseSite(cartModel.getSite(), true);

                final List<ProductModel> targetProducts = Lists.newArrayList();
                final List<String> targetProductCodes = Lists.newArrayList();
                final List<String> sourceProductCodes = Lists.newArrayList();

                List<ProductModel> sourceProducts = extractProductForReferencesFromCart(cartModel);

                for (ProductModel sourceProduct : sourceProducts)
                {
                    sourceProductCodes.add(sourceProduct.getCode());

                    Collection<ProductReferenceModel> productReferenceModel = getProductReferenceService()
                            .getProductReferencesForSourceProduct(sourceProduct,
                                    ProductReferenceTypeEnum.valueOf("CROSSELLING"), true);

                    if (!productReferenceModel.isEmpty())
                    {
                        for (ProductReferenceModel reference : productReferenceModel)
                        {
                            if (reference.getTarget().getApprovalStatus() == ArticleApprovalStatus.APPROVED
                                    && !targetProductCodes.contains(reference.getTarget().getCode()))
                            {
                                targetProductCodes.add(reference.getTarget().getCode());
                                targetProducts.add(reference.getTarget());
                            }
                        }
                    }
                }

                for (ProductModel productModel : targetProducts)
                {
                    if (references.size() >= limit)
                    {
                        break;
                    }
                    else if (!sourceProductCodes.contains(productModel.getCode()))
                    {
                        references.add(getProductConverter().convert(productModel));
                    }
                }

            }

        });

        return references;
    }

    /**
     * Extracts products that will be used for finding crosselling references
     *
     * @param cart
     *            Cart Model
     * @return List of products
     */
    protected List<ProductModel> extractProductForReferencesFromCart(CartModel cart)
    {

        List<ProductModel> sourceProducts = Lists.newArrayList();
        for (AbstractOrderEntryModel entry : cart.getEntries())
        {
            ProductModel sourceProduct = entry.getProduct();

            if (sourceProduct instanceof VariantProductModel)
            {
                sourceProduct = ((VariantProductModel) entry.getProduct()).getBaseProduct();
            }

            sourceProducts.add(sourceProduct);
        }
        return sourceProducts;
    }

}
