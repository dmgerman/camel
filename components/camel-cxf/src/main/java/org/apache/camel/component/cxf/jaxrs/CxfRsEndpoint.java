begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.jaxrs
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
name|jaxrs
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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicBoolean
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
name|CamelContext
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
name|Component
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
name|Consumer
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
name|Processor
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
name|Producer
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
name|Service
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
name|CxfEndpointUtils
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
name|NullFaultListener
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
name|DefaultEndpoint
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
name|ObjectHelper
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
name|jaxrs
operator|.
name|client
operator|.
name|JAXRSClientFactoryBean
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
name|logging
operator|.
name|FaultListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
DECL|class|CxfRsEndpoint
specifier|public
class|class
name|CxfRsEndpoint
extends|extends
name|DefaultEndpoint
implements|implements
name|HeaderFilterStrategyAware
implements|,
name|Service
block|{
DECL|enum|BindingStyle
specifier|public
enum|enum
name|BindingStyle
block|{
comment|/**          *<i>Only available for consumers.</i>          * This binding style processes request parameters, multiparts, etc. and maps them to IN headers, IN attachments and to the message body.          * It aims to eliminate low-level processing of {@link org.apache.cxf.message.MessageContentsList}.          * It also also adds more flexibility and simplicity to the response mapping.          */
DECL|enumConstant|SimpleConsumer
name|SimpleConsumer
block|,
comment|/**          * This is the traditional binding style, which simply dumps the {@link org.apache.cxf.message.MessageContentsList} coming in from the CXF stack          * onto the IN message body. The user is then responsible for processing it according to the contract defined by the JAX-RS method signature.          */
DECL|enumConstant|Default
name|Default
block|}
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|CxfRsEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|bus
specifier|protected
name|Bus
name|bus
decl_stmt|;
DECL|field|entityProviders
specifier|protected
name|List
argument_list|<
name|Object
argument_list|>
name|entityProviders
init|=
operator|new
name|LinkedList
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|schemaLocations
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|schemaLocations
decl_stmt|;
DECL|field|parameters
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|parameters
decl_stmt|;
DECL|field|resourceClasses
specifier|private
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|resourceClasses
decl_stmt|;
DECL|field|headerFilterStrategy
specifier|private
name|HeaderFilterStrategy
name|headerFilterStrategy
decl_stmt|;
DECL|field|binding
specifier|private
name|CxfRsBinding
name|binding
decl_stmt|;
DECL|field|httpClientAPI
specifier|private
name|boolean
name|httpClientAPI
init|=
literal|true
decl_stmt|;
DECL|field|address
specifier|private
name|String
name|address
decl_stmt|;
DECL|field|throwExceptionOnFailure
specifier|private
name|boolean
name|throwExceptionOnFailure
init|=
literal|true
decl_stmt|;
DECL|field|maxClientCacheSize
specifier|private
name|int
name|maxClientCacheSize
init|=
literal|10
decl_stmt|;
DECL|field|loggingFeatureEnabled
specifier|private
name|boolean
name|loggingFeatureEnabled
decl_stmt|;
DECL|field|loggingSizeLimit
specifier|private
name|int
name|loggingSizeLimit
decl_stmt|;
DECL|field|skipFaultLogging
specifier|private
name|boolean
name|skipFaultLogging
decl_stmt|;
DECL|field|bindingStyle
specifier|private
name|BindingStyle
name|bindingStyle
init|=
name|BindingStyle
operator|.
name|Default
decl_stmt|;
DECL|field|getBusHasBeenCalled
specifier|private
name|AtomicBoolean
name|getBusHasBeenCalled
init|=
operator|new
name|AtomicBoolean
argument_list|(
literal|false
argument_list|)
decl_stmt|;
DECL|field|isSetDefaultBus
specifier|private
name|boolean
name|isSetDefaultBus
decl_stmt|;
annotation|@
name|Deprecated
DECL|method|CxfRsEndpoint (String endpointUri, CamelContext camelContext)
specifier|public
name|CxfRsEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|camelContext
argument_list|)
expr_stmt|;
name|setAddress
argument_list|(
name|endpointUri
argument_list|)
expr_stmt|;
block|}
DECL|method|CxfRsEndpoint (String endpointUri, Component component)
specifier|public
name|CxfRsEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|setAddress
argument_list|(
name|endpointUri
argument_list|)
expr_stmt|;
block|}
comment|// This method is for CxfRsComponent setting the EndpointUri
DECL|method|updateEndpointUri (String endpointUri)
specifier|protected
name|void
name|updateEndpointUri
parameter_list|(
name|String
name|endpointUri
parameter_list|)
block|{
name|super
operator|.
name|setEndpointUri
argument_list|(
name|endpointUri
argument_list|)
expr_stmt|;
block|}
DECL|method|setParameters (Map<String, String> param)
specifier|public
name|void
name|setParameters
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|param
parameter_list|)
block|{
name|parameters
operator|=
name|param
expr_stmt|;
block|}
DECL|method|getParameters ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getParameters
parameter_list|()
block|{
return|return
name|parameters
return|;
block|}
DECL|method|setHttpClientAPI (boolean clientAPI)
specifier|public
name|void
name|setHttpClientAPI
parameter_list|(
name|boolean
name|clientAPI
parameter_list|)
block|{
name|httpClientAPI
operator|=
name|clientAPI
expr_stmt|;
block|}
DECL|method|isHttpClientAPI ()
specifier|public
name|boolean
name|isHttpClientAPI
parameter_list|()
block|{
return|return
name|httpClientAPI
return|;
block|}
annotation|@
name|Override
DECL|method|isLenientProperties ()
specifier|public
name|boolean
name|isLenientProperties
parameter_list|()
block|{
return|return
literal|true
return|;
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
DECL|method|setHeaderFilterStrategy (HeaderFilterStrategy strategy)
specifier|public
name|void
name|setHeaderFilterStrategy
parameter_list|(
name|HeaderFilterStrategy
name|strategy
parameter_list|)
block|{
name|headerFilterStrategy
operator|=
name|strategy
expr_stmt|;
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|CxfRsConsumer
name|answer
init|=
operator|new
name|CxfRsConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|bindingStyle
operator|==
name|BindingStyle
operator|.
name|SimpleConsumer
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The SimpleConsumer Binding Style cannot be used in a camel-cxfrs producer"
argument_list|)
throw|;
block|}
return|return
operator|new
name|CxfRsProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
DECL|method|setBinding (CxfRsBinding binding)
specifier|public
name|void
name|setBinding
parameter_list|(
name|CxfRsBinding
name|binding
parameter_list|)
block|{
name|this
operator|.
name|binding
operator|=
name|binding
expr_stmt|;
block|}
DECL|method|getBinding ()
specifier|public
name|CxfRsBinding
name|getBinding
parameter_list|()
block|{
return|return
name|binding
return|;
block|}
DECL|method|isSkipFaultLogging ()
specifier|public
name|boolean
name|isSkipFaultLogging
parameter_list|()
block|{
return|return
name|skipFaultLogging
return|;
block|}
DECL|method|setSkipFaultLogging (boolean skipFaultLogging)
specifier|public
name|void
name|setSkipFaultLogging
parameter_list|(
name|boolean
name|skipFaultLogging
parameter_list|)
block|{
name|this
operator|.
name|skipFaultLogging
operator|=
name|skipFaultLogging
expr_stmt|;
block|}
DECL|method|checkBeanType (Object object, Class<?> clazz)
specifier|protected
name|void
name|checkBeanType
parameter_list|(
name|Object
name|object
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
block|{
if|if
condition|(
operator|!
name|clazz
operator|.
name|isAssignableFrom
argument_list|(
name|object
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The configure bean is not the instance of "
operator|+
name|clazz
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
DECL|method|setupJAXRSServerFactoryBean (JAXRSServerFactoryBean sfb)
specifier|protected
name|void
name|setupJAXRSServerFactoryBean
parameter_list|(
name|JAXRSServerFactoryBean
name|sfb
parameter_list|)
block|{
comment|// address
if|if
condition|(
name|getAddress
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|sfb
operator|.
name|setAddress
argument_list|(
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getResourceClasses
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|res
init|=
name|getResourceClasses
argument_list|()
decl_stmt|;
comment|// setup the resource providers
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
range|:
name|res
control|)
block|{
name|sfb
operator|.
name|setResourceProvider
argument_list|(
name|clazz
argument_list|,
operator|new
name|CamelResourceProvider
argument_list|(
name|clazz
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|sfb
operator|.
name|setResourceClasses
argument_list|(
name|res
argument_list|)
expr_stmt|;
block|}
name|sfb
operator|.
name|setStart
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|setupJAXRSClientFactoryBean (JAXRSClientFactoryBean cfb, String address)
specifier|protected
name|void
name|setupJAXRSClientFactoryBean
parameter_list|(
name|JAXRSClientFactoryBean
name|cfb
parameter_list|,
name|String
name|address
parameter_list|)
block|{
comment|// address
if|if
condition|(
name|address
operator|!=
literal|null
condition|)
block|{
name|cfb
operator|.
name|setAddress
argument_list|(
name|address
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getResourceClasses
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|getResourceClasses
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|cfb
operator|.
name|setResourceClass
argument_list|(
name|getResourceClasses
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|cfb
operator|.
name|getServiceFactory
argument_list|()
operator|.
name|setResourceClasses
argument_list|(
name|getResourceClasses
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isLoggingFeatureEnabled
argument_list|()
condition|)
block|{
if|if
condition|(
name|getLoggingSizeLimit
argument_list|()
operator|>
literal|0
condition|)
block|{
name|cfb
operator|.
name|getFeatures
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingFeature
argument_list|(
name|getLoggingSizeLimit
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cfb
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
block|}
if|if
condition|(
name|this
operator|.
name|isSkipFaultLogging
argument_list|()
condition|)
block|{
if|if
condition|(
name|cfb
operator|.
name|getProperties
argument_list|()
operator|==
literal|null
condition|)
block|{
name|cfb
operator|.
name|setProperties
argument_list|(
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|cfb
operator|.
name|getProperties
argument_list|()
operator|.
name|put
argument_list|(
name|FaultListener
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|NullFaultListener
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|cfb
operator|.
name|setThreadSafe
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|newJAXRSServerFactoryBean ()
specifier|protected
name|JAXRSServerFactoryBean
name|newJAXRSServerFactoryBean
parameter_list|()
block|{
return|return
operator|new
name|JAXRSServerFactoryBean
argument_list|()
return|;
block|}
DECL|method|newJAXRSClientFactoryBean ()
specifier|protected
name|JAXRSClientFactoryBean
name|newJAXRSClientFactoryBean
parameter_list|()
block|{
return|return
operator|new
name|JAXRSClientFactoryBean
argument_list|()
return|;
block|}
DECL|method|resolvePropertyPlaceholders (String str)
specifier|protected
name|String
name|resolvePropertyPlaceholders
parameter_list|(
name|String
name|str
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|getCamelContext
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|getCamelContext
argument_list|()
operator|.
name|resolvePropertyPlaceholders
argument_list|(
name|str
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|str
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
DECL|method|createJAXRSServerFactoryBean ()
specifier|public
name|JAXRSServerFactoryBean
name|createJAXRSServerFactoryBean
parameter_list|()
block|{
name|JAXRSServerFactoryBean
name|answer
init|=
name|newJAXRSServerFactoryBean
argument_list|()
decl_stmt|;
name|setupJAXRSServerFactoryBean
argument_list|(
name|answer
argument_list|)
expr_stmt|;
comment|// let customer to override the default setting of provider
if|if
condition|(
operator|!
name|getProviders
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|answer
operator|.
name|setProviders
argument_list|(
name|getProviders
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|schemaLocations
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setSchemaLocations
argument_list|(
name|schemaLocations
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isLoggingFeatureEnabled
argument_list|()
condition|)
block|{
if|if
condition|(
name|getLoggingSizeLimit
argument_list|()
operator|>
literal|0
condition|)
block|{
name|answer
operator|.
name|getFeatures
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingFeature
argument_list|(
name|getLoggingSizeLimit
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|answer
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
block|}
if|if
condition|(
name|this
operator|.
name|isSkipFaultLogging
argument_list|()
condition|)
block|{
if|if
condition|(
name|answer
operator|.
name|getProperties
argument_list|()
operator|==
literal|null
condition|)
block|{
name|answer
operator|.
name|setProperties
argument_list|(
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|answer
operator|.
name|getProperties
argument_list|()
operator|.
name|put
argument_list|(
name|FaultListener
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|NullFaultListener
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|createJAXRSClientFactoryBean ()
specifier|public
name|JAXRSClientFactoryBean
name|createJAXRSClientFactoryBean
parameter_list|()
block|{
return|return
name|createJAXRSClientFactoryBean
argument_list|(
name|getAddress
argument_list|()
argument_list|)
return|;
block|}
DECL|method|createJAXRSClientFactoryBean (String address)
specifier|public
name|JAXRSClientFactoryBean
name|createJAXRSClientFactoryBean
parameter_list|(
name|String
name|address
parameter_list|)
block|{
name|JAXRSClientFactoryBean
name|answer
init|=
name|newJAXRSClientFactoryBean
argument_list|()
decl_stmt|;
name|setupJAXRSClientFactoryBean
argument_list|(
name|answer
argument_list|,
name|address
argument_list|)
expr_stmt|;
comment|// let customer to override the default setting of provider
if|if
condition|(
operator|!
name|getProviders
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|answer
operator|.
name|setProviders
argument_list|(
name|getProviders
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|schemaLocations
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setSchemaLocations
argument_list|(
name|schemaLocations
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isLoggingFeatureEnabled
argument_list|()
condition|)
block|{
if|if
condition|(
name|getLoggingSizeLimit
argument_list|()
operator|>
literal|0
condition|)
block|{
name|answer
operator|.
name|getFeatures
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingFeature
argument_list|(
name|getLoggingSizeLimit
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|answer
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
block|}
if|if
condition|(
name|this
operator|.
name|isSkipFaultLogging
argument_list|()
condition|)
block|{
if|if
condition|(
name|answer
operator|.
name|getProperties
argument_list|()
operator|==
literal|null
condition|)
block|{
name|answer
operator|.
name|setProperties
argument_list|(
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|answer
operator|.
name|getProperties
argument_list|()
operator|.
name|put
argument_list|(
name|FaultListener
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|NullFaultListener
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|getResourceClasses ()
specifier|public
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|getResourceClasses
parameter_list|()
block|{
return|return
name|resourceClasses
return|;
block|}
DECL|method|addResourceClass (Class<?> resourceClass)
specifier|public
name|void
name|addResourceClass
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|resourceClass
parameter_list|)
block|{
if|if
condition|(
name|resourceClasses
operator|==
literal|null
condition|)
block|{
name|resourceClasses
operator|=
operator|new
name|ArrayList
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|resourceClasses
operator|.
name|add
argument_list|(
name|resourceClass
argument_list|)
expr_stmt|;
block|}
DECL|method|setResourceClasses (List<Class<?>> resourceClasses)
specifier|public
name|void
name|setResourceClasses
parameter_list|(
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|resourceClasses
parameter_list|)
block|{
name|this
operator|.
name|resourceClasses
operator|=
name|resourceClasses
expr_stmt|;
block|}
DECL|method|setResourceClasses (Class<?>.... classes)
specifier|public
name|void
name|setResourceClasses
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
modifier|...
name|classes
parameter_list|)
block|{
name|setResourceClasses
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|classes
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|setAddress (String address)
specifier|public
name|void
name|setAddress
parameter_list|(
name|String
name|address
parameter_list|)
block|{
name|this
operator|.
name|address
operator|=
name|address
expr_stmt|;
block|}
DECL|method|getAddress ()
specifier|public
name|String
name|getAddress
parameter_list|()
block|{
return|return
name|resolvePropertyPlaceholders
argument_list|(
name|address
argument_list|)
return|;
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
DECL|method|getLoggingSizeLimit ()
specifier|public
name|int
name|getLoggingSizeLimit
parameter_list|()
block|{
return|return
name|loggingSizeLimit
return|;
block|}
DECL|method|setLoggingSizeLimit (int loggingSizeLimit)
specifier|public
name|void
name|setLoggingSizeLimit
parameter_list|(
name|int
name|loggingSizeLimit
parameter_list|)
block|{
name|this
operator|.
name|loggingSizeLimit
operator|=
name|loggingSizeLimit
expr_stmt|;
block|}
DECL|method|isThrowExceptionOnFailure ()
specifier|public
name|boolean
name|isThrowExceptionOnFailure
parameter_list|()
block|{
return|return
name|throwExceptionOnFailure
return|;
block|}
DECL|method|setThrowExceptionOnFailure (boolean throwExceptionOnFailure)
specifier|public
name|void
name|setThrowExceptionOnFailure
parameter_list|(
name|boolean
name|throwExceptionOnFailure
parameter_list|)
block|{
name|this
operator|.
name|throwExceptionOnFailure
operator|=
name|throwExceptionOnFailure
expr_stmt|;
block|}
comment|/**      * @param maxClientCacheSize the maxClientCacheSize to set      */
DECL|method|setMaxClientCacheSize (int maxClientCacheSize)
specifier|public
name|void
name|setMaxClientCacheSize
parameter_list|(
name|int
name|maxClientCacheSize
parameter_list|)
block|{
name|this
operator|.
name|maxClientCacheSize
operator|=
name|maxClientCacheSize
expr_stmt|;
block|}
comment|/**      * @return the maxClientCacheSize      */
DECL|method|getMaxClientCacheSize ()
specifier|public
name|int
name|getMaxClientCacheSize
parameter_list|()
block|{
return|return
name|maxClientCacheSize
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
DECL|method|getBus ()
specifier|public
name|Bus
name|getBus
parameter_list|()
block|{
if|if
condition|(
name|bus
operator|==
literal|null
condition|)
block|{
name|bus
operator|=
name|CxfEndpointUtils
operator|.
name|createBus
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Using DefaultBus {}"
argument_list|,
name|bus
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|getBusHasBeenCalled
operator|.
name|getAndSet
argument_list|(
literal|true
argument_list|)
operator|&&
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
name|LOG
operator|.
name|debug
argument_list|(
literal|"Set bus {} as thread default bus"
argument_list|,
name|bus
argument_list|)
expr_stmt|;
block|}
return|return
name|bus
return|;
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
DECL|method|getBindingStyle ()
specifier|public
name|BindingStyle
name|getBindingStyle
parameter_list|()
block|{
return|return
name|bindingStyle
return|;
block|}
DECL|method|getProviders ()
specifier|public
name|List
argument_list|<
name|?
argument_list|>
name|getProviders
parameter_list|()
block|{
return|return
name|entityProviders
return|;
block|}
DECL|method|setProviders (List<? extends Object> providers)
specifier|public
name|void
name|setProviders
parameter_list|(
name|List
argument_list|<
name|?
extends|extends
name|Object
argument_list|>
name|providers
parameter_list|)
block|{
name|this
operator|.
name|entityProviders
operator|.
name|addAll
argument_list|(
name|providers
argument_list|)
expr_stmt|;
block|}
DECL|method|setProvider (Object provider)
specifier|public
name|void
name|setProvider
parameter_list|(
name|Object
name|provider
parameter_list|)
block|{
name|entityProviders
operator|.
name|add
argument_list|(
name|provider
argument_list|)
expr_stmt|;
block|}
DECL|method|setSchemaLocation (String schema)
specifier|public
name|void
name|setSchemaLocation
parameter_list|(
name|String
name|schema
parameter_list|)
block|{
name|setSchemaLocations
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|schema
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|setSchemaLocations (List<String> schemas)
specifier|public
name|void
name|setSchemaLocations
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|schemas
parameter_list|)
block|{
name|this
operator|.
name|schemaLocations
operator|=
name|schemas
expr_stmt|;
block|}
comment|/**      * See documentation of {@link BindingStyle}.      */
DECL|method|setBindingStyle (BindingStyle bindingStyle)
specifier|public
name|void
name|setBindingStyle
parameter_list|(
name|BindingStyle
name|bindingStyle
parameter_list|)
block|{
name|this
operator|.
name|bindingStyle
operator|=
name|bindingStyle
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|headerFilterStrategy
operator|==
literal|null
condition|)
block|{
name|headerFilterStrategy
operator|=
operator|new
name|CxfRsHeaderFilterStrategy
argument_list|()
expr_stmt|;
block|}
name|binding
operator|=
name|bindingStyle
operator|==
literal|null
operator|||
name|bindingStyle
operator|==
name|BindingStyle
operator|.
name|Default
condition|?
operator|new
name|DefaultCxfRsBinding
argument_list|()
else|:
operator|new
name|SimpleCxfRsBinding
argument_list|()
expr_stmt|;
if|if
condition|(
name|binding
operator|instanceof
name|HeaderFilterStrategyAware
condition|)
block|{
operator|(
operator|(
name|HeaderFilterStrategyAware
operator|)
name|binding
operator|)
operator|.
name|setHeaderFilterStrategy
argument_list|(
name|getHeaderFilterStrategy
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
block|}
end_class

end_unit

