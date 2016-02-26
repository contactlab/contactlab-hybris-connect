package com.contactlab.delegate;

import static org.mockito.BDDMockito.given;

import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.contactlab.webservices.constants.ContactlabwebservicesConstants;
import com.contactlab.webservices.delegate.impl.DefaultContactlabWebserviceDelegate;


/**
 * JUnit Tests for the Contactlabwebservices extension
 */
public class ContactlabWebservicesDelegateTest extends HybrisJUnit4TransactionalTest
{
    /** Edit the local|project.properties to change logging behaviour (properties log4j.*). */
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(ContactlabWebservicesDelegateTest.class.getName());

    private DefaultContactlabWebserviceDelegate contactlabWebserviceDelegate;

    @Mock
    private ConfigurationService configurationService;

    @Mock
    private Configuration configuration;

    @Before
    public void setUp()
    {
        // implement here code executed before each test
        Registry.activateMasterTenant();
        MockitoAnnotations.initMocks(this);

        contactlabWebserviceDelegate = new DefaultContactlabWebserviceDelegate();

        contactlabWebserviceDelegate.setConfigurationService(configurationService);
        given(configurationService.getConfiguration()).willReturn(configuration);
        given(configuration.getString(
                ContactlabwebservicesConstants.EXTENSIONNAME.toLowerCase() + "." + ContactlabwebservicesConstants.APIKEY))
                        .willReturn("99999999b31431881e95291061439b677429ac34");
        given(configuration
                .getString(ContactlabwebservicesConstants.EXTENSIONNAME.toLowerCase() + "." + ContactlabwebservicesConstants.UID))
                        .willReturn("00000000e85157120ff559b7d7db2488650f91e5");
        given(configuration.getString(
                ContactlabwebservicesConstants.EXTENSIONNAME.toLowerCase() + "." + ContactlabwebservicesConstants.ENDPOINT_URL))
                        .willReturn("https://soap.contactlab.it/soap/services");
    }

    @After
    public void tearDown()
    {
        // implement here code executed after each test
    }

}
