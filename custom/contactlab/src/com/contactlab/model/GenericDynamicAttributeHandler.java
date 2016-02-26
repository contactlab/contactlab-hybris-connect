package com.contactlab.model;

import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;


/**
 * @author Techedge Group
 * 
 */
public class GenericDynamicAttributeHandler implements DynamicAttributeHandler<Object, AbstractItemModel>, ApplicationContextAware
{

    private static final Logger LOG = Logger.getLogger(GenericDynamicAttributeHandler.class);

    private final String expression;

    private ApplicationContext applicationContext;

    public GenericDynamicAttributeHandler(final String expression)
    {
        this.expression = expression;
    }

    @Override
    public Object get(final AbstractItemModel model)
    {
        if (LOG.isTraceEnabled())
        {
            LOG.trace("Reading " + expression);
        }
        final ExpressionParser parser = new SpelExpressionParser();
        final Expression exp = parser.parseExpression(expression);
        final StandardEvaluationContext evaluationContext = new StandardEvaluationContext(model);
        if (applicationContext != null)
        {
            evaluationContext.setBeanResolver(new BeanFactoryResolver(applicationContext));
        }
        evaluationContext.setVariable("item", model);
        return exp.getValue(evaluationContext);
    }

    @Override
    public void set(final AbstractItemModel model, final Object value)
    {
        throw new RuntimeException("Cannot set property");
    }

    @Override
    public void setApplicationContext(final ApplicationContext arg0) throws BeansException
    {
        this.applicationContext = arg0;
    }

}
