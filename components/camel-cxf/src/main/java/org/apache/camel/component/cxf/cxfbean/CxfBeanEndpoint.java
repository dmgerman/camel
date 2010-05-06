begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.cxfbean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
operator|.
name|cxfbean
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
operator|.
name|CxfHeaderFilterStrategy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|ProcessorEndpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|HeaderFilterStrategy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|HeaderFilterStrategyAware
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|CamelContextHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|Bus
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|BusFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|endpoint
operator|.
name|Server
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|feature
operator|.
name|LoggingFeature
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|JAXRSServerFactoryBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxws
operator|.
name|JaxWsServerFactoryBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|ConduitInitiatorManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|DestinationFactoryManager
import|;
end_import

begin_comment
comment|/**  * CXF Bean Endpoint is a {@link ProcessorEndpoint} which associated with   * a {@link CxfBeanDestination}.  It delegates the processing of Camel   * Exchanges to the associated CxfBeanDestination.  *    * @version $Revision$  */
end_comment

begin_class
DECL|class|CxfBeanEndpoint
specifier|public
class|class
name|CxfBeanEndpoint
extends|extends
name|ProcessorEndpoint
implements|implements
name|HeaderFilterStrategyAware
block|{
DECL|field|URI_PREFIX
specifier|private
specifier|static
specifier|final
name|String
name|URI_PREFIX
init|=
literal|"cxfbean"
decl_stmt|;
DECL|field|server
specifier|private
name|Server
name|server
decl_stmt|;
DECL|field|bus
specifier|private
name|Bus
name|bus
decl_stmt|;
DECL|field|isSetDefaultBus
specifier|private
name|boolean
name|isSetDefaultBus
decl_stmt|;
DECL|field|cxfBeanBinding
specifier|private
name|CxfBeanBinding
name|cxfBeanBinding
init|=
operator|new
name|DefaultCxfBeanBinding
argument_list|()
decl_stmt|;
DECL|field|headerFilterStrategy
specifier|private
name|HeaderFilterStrategy
name|headerFilterStrategy
init|=
operator|new
name|CxfHeaderFilterStrategy
argument_list|()
decl_stmt|;
DECL|field|loggingFeatureEnabled
specifier|private
name|boolean
name|loggingFeatureEnabled
decl_stmt|;
DECL|field|populateFromClass
specifier|private
name|boolean
name|populateFromClass
init|=
literal|true
decl_stmt|;
DECL|method|CxfBeanEndpoint (String remaining, CxfBeanComponent component)
specifier|public
name|CxfBeanEndpoint
parameter_list|(
name|String
name|remaining
parameter_list|,
name|CxfBeanComponent
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|remaining
argument_list|,
name|component
argument_list|)
expr_stmt|;
block|}
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
block|{
name|server
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
block|{
name|server
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|init ()
specifier|public
name|void
name|init
parameter_list|()
block|{
name|Object
name|obj
init|=
name|CamelContextHelper
operator|.
name|mandatoryLookup
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|getEndpointUri
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|serviceBeans
decl_stmt|;
if|if
condition|(
name|obj
operator|instanceof
name|List
condition|)
block|{
name|serviceBeans
operator|=
operator|(
name|List
operator|)
name|obj
expr_stmt|;
block|}
else|else
block|{
name|serviceBeans
operator|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
expr_stmt|;
name|serviceBeans
operator|.
name|add
argument_list|(
name|obj
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|bus
operator|==
literal|null
condition|)
block|{
name|bus
operator|=
name|BusFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|createBus
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|isSetDefaultBus
condition|)
block|{
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
name|registerTransportFactory
argument_list|(
operator|(
name|CxfBeanComponent
operator|)
name|this
operator|.
name|getComponent
argument_list|()
argument_list|)
expr_stmt|;
name|createServer
argument_list|(
name|serviceBeans
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createEndpointUri ()
specifier|protected
name|String
name|createEndpointUri
parameter_list|()
block|{
return|return
name|URI_PREFIX
operator|+
literal|":"
operator|+
name|getEndpointUri
argument_list|()
return|;
block|}
DECL|method|createServer (List<Object> serviceBeans)
specifier|private
name|void
name|createServer
parameter_list|(
name|List
argument_list|<
name|Object
argument_list|>
name|serviceBeans
parameter_list|)
block|{
name|Object
name|obj
init|=
name|serviceBeans
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getClass
argument_list|()
operator|.
name|getAnnotation
argument_list|(
name|WebService
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|obj
operator|!=
literal|null
condition|)
block|{
name|JaxWsServerFactoryBean
name|bean
init|=
operator|new
name|JaxWsServerFactoryBean
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setTransportId
argument_list|(
name|CxfBeanTransportFactory
operator|.
name|TRANSPORT_ID
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setServiceClass
argument_list|(
name|serviceBeans
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|bean
operator|.
name|getServiceFactory
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|bean
operator|.
name|getServiceFactory
argument_list|()
operator|.
name|setPopulateFromClass
argument_list|(
name|isPopulateFromClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|bean
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setStart
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setAddress
argument_list|(
literal|"camel://"
operator|+
name|createEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|loggingFeatureEnabled
condition|)
block|{
name|bean
operator|.
name|getFeatures
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingFeature
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|server
operator|=
name|bean
operator|.
name|create
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|JAXRSServerFactoryBean
name|bean
init|=
operator|new
name|JAXRSServerFactoryBean
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setServiceBeans
argument_list|(
name|serviceBeans
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setAddress
argument_list|(
literal|"camel://"
operator|+
name|createEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setStart
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setTransportId
argument_list|(
name|CxfBeanTransportFactory
operator|.
name|TRANSPORT_ID
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
if|if
condition|(
name|loggingFeatureEnabled
condition|)
block|{
name|bean
operator|.
name|getFeatures
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingFeature
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|server
operator|=
name|bean
operator|.
name|create
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * @param cxfBeanComponent       *       */
DECL|method|registerTransportFactory (CxfBeanComponent cxfBeanComponent)
specifier|private
name|void
name|registerTransportFactory
parameter_list|(
name|CxfBeanComponent
name|cxfBeanComponent
parameter_list|)
block|{
name|CxfBeanTransportFactory
name|transportFactory
init|=
operator|new
name|CxfBeanTransportFactory
argument_list|()
decl_stmt|;
name|transportFactory
operator|.
name|setCxfBeanComponent
argument_list|(
name|cxfBeanComponent
argument_list|)
expr_stmt|;
name|transportFactory
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
comment|// register the conduit initiator
name|ConduitInitiatorManager
name|cim
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|ConduitInitiatorManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|cim
operator|.
name|registerConduitInitiator
argument_list|(
name|CxfBeanTransportFactory
operator|.
name|TRANSPORT_ID
argument_list|,
name|transportFactory
argument_list|)
expr_stmt|;
comment|// register the destination factory
name|DestinationFactoryManager
name|dfm
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|DestinationFactoryManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|dfm
operator|.
name|registerDestinationFactory
argument_list|(
name|CxfBeanTransportFactory
operator|.
name|TRANSPORT_ID
argument_list|,
name|transportFactory
argument_list|)
expr_stmt|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
DECL|method|getBus ()
specifier|public
name|Bus
name|getBus
parameter_list|()
block|{
return|return
name|bus
return|;
block|}
DECL|method|setBus (Bus bus)
specifier|public
name|void
name|setBus
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
name|this
operator|.
name|bus
operator|=
name|bus
expr_stmt|;
block|}
DECL|method|setSetDefaultBus (boolean isSetDefaultBus)
specifier|public
name|void
name|setSetDefaultBus
parameter_list|(
name|boolean
name|isSetDefaultBus
parameter_list|)
block|{
name|this
operator|.
name|isSetDefaultBus
operator|=
name|isSetDefaultBus
expr_stmt|;
block|}
DECL|method|isSetDefaultBus ()
specifier|public
name|boolean
name|isSetDefaultBus
parameter_list|()
block|{
return|return
name|isSetDefaultBus
return|;
block|}
DECL|method|setCxfBeanBinding (CxfBeanBinding cxfBeanBinding)
specifier|public
name|void
name|setCxfBeanBinding
parameter_list|(
name|CxfBeanBinding
name|cxfBeanBinding
parameter_list|)
block|{
name|this
operator|.
name|cxfBeanBinding
operator|=
name|cxfBeanBinding
expr_stmt|;
block|}
DECL|method|getCxfBeanBinding ()
specifier|public
name|CxfBeanBinding
name|getCxfBeanBinding
parameter_list|()
block|{
return|return
name|cxfBeanBinding
return|;
block|}
DECL|method|setHeaderFilterStrategy (HeaderFilterStrategy headerFilterStrategy)
specifier|public
name|void
name|setHeaderFilterStrategy
parameter_list|(
name|HeaderFilterStrategy
name|headerFilterStrategy
parameter_list|)
block|{
name|this
operator|.
name|headerFilterStrategy
operator|=
name|headerFilterStrategy
expr_stmt|;
block|}
DECL|method|getHeaderFilterStrategy ()
specifier|public
name|HeaderFilterStrategy
name|getHeaderFilterStrategy
parameter_list|()
block|{
return|return
name|headerFilterStrategy
return|;
block|}
DECL|method|setLoggingFeatureEnabled (boolean loggingFeatureEnabled)
specifier|public
name|void
name|setLoggingFeatureEnabled
parameter_list|(
name|boolean
name|loggingFeatureEnabled
parameter_list|)
block|{
name|this
operator|.
name|loggingFeatureEnabled
operator|=
name|loggingFeatureEnabled
expr_stmt|;
block|}
DECL|method|isLoggingFeatureEnabled ()
specifier|public
name|boolean
name|isLoggingFeatureEnabled
parameter_list|()
block|{
return|return
name|loggingFeatureEnabled
return|;
block|}
DECL|method|setPopulateFromClass (boolean populateFromClass)
specifier|public
name|void
name|setPopulateFromClass
parameter_list|(
name|boolean
name|populateFromClass
parameter_list|)
block|{
name|this
operator|.
name|populateFromClass
operator|=
name|populateFromClass
expr_stmt|;
block|}
DECL|method|isPopulateFromClass ()
specifier|public
name|boolean
name|isPopulateFromClass
parameter_list|()
block|{
return|return
name|populateFromClass
return|;
block|}
block|}
end_class

end_unit

