package com.contactlab.attributehandlers;

import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.contactlab.model.EmailSubscriptionModel;


/**
 * @author Techedge Group
 *
 */
public class CustomFieldDynamicAttributeHandler
        implements DynamicAttributeHandler<String, EmailSubscriptionModel>, ApplicationContextAware
{
    private static final Logger LOG = Logger.getLogger(CustomFieldDynamicAttributeHandler.class);

    private static final ExpressionParser expressionParser = new SpelExpressionParser(new SpelParserConfiguration(true, true));

    private FlexibleSearchService flexibleSearchService;

    private ApplicationContext applicationContext;

    private String query;

    private Map<String, String> parameters;

    @Override
    public String get(final EmailSubscriptionModel emailSubscriptionModel)
    {
        if (StringUtils.isEmpty(getQuery()))
        {
            return null;
        }

        if ((getParameters() == null) || (getParameters().isEmpty()))
        {
            return null;
        }

        final FlexibleSearchQuery query = new FlexibleSearchQuery(getQuery());
        query.addQueryParameters(prepareParameters(emailSubscriptionModel, getParameters()));

        final List resultList = new ArrayList();
        resultList.add(String.class);
        query.setResultClassList(resultList);
        LOG.info("Running query: " + query.getQuery());

        final SearchResult searchResult = getFlexibleSearchService().search(query);
        LOG.info("Query executed with " + searchResult.getCount() + " records");
        // It is expected only one result
        return (String) searchResult.getResult().get(0);
    }

    protected Map<String, Object> prepareParameters(final EmailSubscriptionModel emailSubscriptionModel,
            final Map<String, String> parameters)
    {
        final Map<String, Object> result = new HashMap<String, Object>();

        for (final Map.Entry<String, String> entry : parameters.entrySet())
        {
            result.put(entry.getKey(), evalExpression(emailSubscriptionModel, entry.getValue()));
        }

        return result;
    }

    protected Object evalExpression(final EmailSubscriptionModel emailSubscriptionModel, final String expressionString)
    {
        final Expression expression = expressionParser.parseExpression(expressionString);
        final StandardEvaluationContext evaluationContext = new StandardEvaluationContext(emailSubscriptionModel);
        if (applicationContext != null)
        {
            evaluationContext.setBeanResolver(new BeanFactoryResolver(applicationContext));
        }
        evaluationContext.setVariable("item", emailSubscriptionModel);
        return expression.getValue(evaluationContext);
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException
    {
        this.applicationContext = applicationContext;
    }

    @Override
    public void set(final EmailSubscriptionModel emailSubscriptionModel, final String value)
    {
        throw new UnsupportedOperationException("CustomField dynamic attributes cannot be set!");
    }

    protected FlexibleSearchService getFlexibleSearchService()
    {
        return flexibleSearchService;
    }

    @Required
    public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }

    protected String getQuery()
    {
        return query;
    }

    @Required
    public void setQuery(final String query)
    {
        this.query = query;
    }

    protected Map<String, String> getParameters()
    {
        return parameters;
    }

    @Required
    public void setParameters(final Map<String, String> parameters)
    {
        this.parameters = parameters;
    }
}
