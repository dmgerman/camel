begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf
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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Proxy
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
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicBoolean
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|WebServiceProvider
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
name|CamelException
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
name|Exchange
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
name|ExchangePattern
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
name|component
operator|.
name|cxf
operator|.
name|feature
operator|.
name|MessageDataFormatFeature
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
name|feature
operator|.
name|PayLoadDataFormatFeature
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
name|util
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
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
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
name|common
operator|.
name|classloader
operator|.
name|ClassLoaderUtils
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
name|common
operator|.
name|util
operator|.
name|ClassHelper
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
name|Client
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
name|ClientImpl
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
name|Endpoint
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
name|frontend
operator|.
name|ClientFactoryBean
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
name|frontend
operator|.
name|ClientProxy
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
name|frontend
operator|.
name|ClientProxyFactoryBean
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
name|frontend
operator|.
name|ServerFactoryBean
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
name|headers
operator|.
name|Header
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
name|JaxWsClientFactoryBean
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
name|JaxWsProxyFactoryBean
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
name|message
operator|.
name|Message
import|;
end_import

begin_comment
comment|/**  * Defines the<a href="http://activemq.apache.org/camel/cxf.html">CXF Endpoint</a>.  * It contains a list of properties for CXF endpoint including {@link DataFormat},   * {@link CxfBinding}, and {@link HeaderFilterStrategy}.  The default DataFormat   * mode is {@link DataFormat#POJO}.    *  * @version $Revision$  */
end_comment

begin_class
DECL|class|CxfEndpoint
specifier|public
class|class
name|CxfEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|CxfEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|wsdlURL
specifier|private
name|String
name|wsdlURL
decl_stmt|;
DECL|field|serviceClass
specifier|private
name|String
name|serviceClass
decl_stmt|;
DECL|field|portName
specifier|private
name|String
name|portName
decl_stmt|;
DECL|field|serviceName
specifier|private
name|String
name|serviceName
decl_stmt|;
DECL|field|dataFormat
specifier|private
name|DataFormat
name|dataFormat
init|=
name|DataFormat
operator|.
name|POJO
decl_stmt|;
DECL|field|isWrapped
specifier|private
name|boolean
name|isWrapped
decl_stmt|;
DECL|field|inOut
specifier|private
name|boolean
name|inOut
init|=
literal|true
decl_stmt|;
DECL|field|bus
specifier|private
name|Bus
name|bus
decl_stmt|;
DECL|field|cxfBinding
specifier|private
name|CxfBinding
name|cxfBinding
decl_stmt|;
DECL|field|headerFilterStrategy
specifier|private
name|HeaderFilterStrategy
name|headerFilterStrategy
decl_stmt|;
DECL|field|hasWSProviderAnnotation
specifier|private
name|boolean
name|hasWSProviderAnnotation
decl_stmt|;
DECL|field|hasWebServiceAnnotation
specifier|private
name|boolean
name|hasWebServiceAnnotation
decl_stmt|;
DECL|field|cxfBindingInitialized
specifier|private
name|AtomicBoolean
name|cxfBindingInitialized
init|=
operator|new
name|AtomicBoolean
argument_list|(
literal|false
argument_list|)
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
DECL|method|CxfEndpoint (CxfComponent cxfComponent, String remaining)
specifier|public
name|CxfEndpoint
parameter_list|(
name|CxfComponent
name|cxfComponent
parameter_list|,
name|String
name|remaining
parameter_list|)
block|{
name|super
argument_list|(
name|remaining
argument_list|,
name|cxfComponent
argument_list|)
expr_stmt|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|CxfProducer
argument_list|(
name|this
argument_list|)
return|;
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
return|return
operator|new
name|CxfConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createExchange (ExchangePattern pattern)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|ExchangePattern
name|pattern
parameter_list|)
block|{
return|return
operator|new
name|CxfExchange
argument_list|(
name|this
argument_list|,
name|pattern
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
literal|true
return|;
block|}
comment|/**      * Populate server factory bean      */
DECL|method|setupServerFactoryBean (ServerFactoryBean sfb, Class<?> cls)
specifier|protected
name|void
name|setupServerFactoryBean
parameter_list|(
name|ServerFactoryBean
name|sfb
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|)
block|{
name|hasWSProviderAnnotation
operator|=
name|CxfEndpointUtils
operator|.
name|hasAnnotation
argument_list|(
name|cls
argument_list|,
name|WebServiceProvider
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// address
name|sfb
operator|.
name|setAddress
argument_list|(
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
comment|// service class
name|sfb
operator|.
name|setServiceClass
argument_list|(
name|cls
argument_list|)
expr_stmt|;
comment|// wsdl url
if|if
condition|(
name|getWsdlURL
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|sfb
operator|.
name|setWsdlURL
argument_list|(
name|getWsdlURL
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// service  name qname
if|if
condition|(
name|getServiceName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|sfb
operator|.
name|setServiceName
argument_list|(
name|CxfEndpointUtils
operator|.
name|getQName
argument_list|(
name|getServiceName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// port qname
if|if
condition|(
name|getPortName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|sfb
operator|.
name|setEndpointName
argument_list|(
name|CxfEndpointUtils
operator|.
name|getQName
argument_list|(
name|getPortName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// apply feature here
if|if
condition|(
operator|!
name|webServiceProviderAnnotated
argument_list|()
condition|)
block|{
if|if
condition|(
name|getDataFormat
argument_list|()
operator|==
name|DataFormat
operator|.
name|PAYLOAD
condition|)
block|{
name|sfb
operator|.
name|getFeatures
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|PayLoadDataFormatFeature
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|getDataFormat
argument_list|()
operator|==
name|DataFormat
operator|.
name|MESSAGE
condition|)
block|{
name|sfb
operator|.
name|getFeatures
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|MessageDataFormatFeature
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Ignore DataFormat mode "
operator|+
name|getDataFormat
argument_list|()
operator|+
literal|" since SEI class is annotated with WebServiceProvider"
argument_list|)
expr_stmt|;
block|}
block|}
name|sfb
operator|.
name|setBus
argument_list|(
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
name|sfb
operator|.
name|setStart
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
comment|/**      *       * Create a client factory bean object.  Notice that the serviceClass<b>must</b> be      * an interface.      */
DECL|method|createClientFactoryBean (Class<?> cls)
specifier|protected
name|ClientProxyFactoryBean
name|createClientFactoryBean
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|)
throws|throws
name|CamelException
block|{
comment|// quick null point check for serviceClass
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|cls
argument_list|,
literal|"Please provide endpoint service interface class"
argument_list|)
expr_stmt|;
name|hasWebServiceAnnotation
operator|=
name|CxfEndpointUtils
operator|.
name|hasWebServiceAnnotation
argument_list|(
name|cls
argument_list|)
expr_stmt|;
if|if
condition|(
name|hasWebServiceAnnotation
condition|)
block|{
return|return
operator|new
name|JaxWsProxyFactoryBean
argument_list|(
operator|new
name|JaxWsClientFactoryBean
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|createClient
parameter_list|(
name|Endpoint
name|ep
parameter_list|)
block|{
name|setClient
argument_list|(
operator|new
name|CamelCxfClientImpl
argument_list|(
name|getBus
argument_list|()
argument_list|,
name|ep
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|ClientProxyFactoryBean
argument_list|(
operator|new
name|ClientFactoryBean
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|createClient
parameter_list|(
name|Endpoint
name|ep
parameter_list|)
block|{
name|setClient
argument_list|(
operator|new
name|CamelCxfClientImpl
argument_list|(
name|getBus
argument_list|()
argument_list|,
name|ep
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
return|;
block|}
block|}
DECL|method|doGetBus ()
specifier|protected
name|Bus
name|doGetBus
parameter_list|()
block|{
return|return
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
return|;
block|}
comment|/**      *       * Populate a client factory bean      */
DECL|method|setupClientFactoryBean (ClientProxyFactoryBean factoryBean, Class<?> cls)
specifier|protected
name|void
name|setupClientFactoryBean
parameter_list|(
name|ClientProxyFactoryBean
name|factoryBean
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|)
block|{
comment|// quick null point check for serviceClass
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|cls
argument_list|,
literal|"Please provide endpoint service interface class"
argument_list|)
expr_stmt|;
comment|// service class
name|factoryBean
operator|.
name|setServiceClass
argument_list|(
name|cls
argument_list|)
expr_stmt|;
comment|// address
name|factoryBean
operator|.
name|setAddress
argument_list|(
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
comment|// wsdl url
if|if
condition|(
name|getWsdlURL
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|factoryBean
operator|.
name|setWsdlURL
argument_list|(
name|getWsdlURL
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// service name qname
if|if
condition|(
name|getServiceName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|factoryBean
operator|.
name|setServiceName
argument_list|(
name|CxfEndpointUtils
operator|.
name|getQName
argument_list|(
name|getServiceName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// port name qname
if|if
condition|(
name|getPortName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|factoryBean
operator|.
name|setEndpointName
argument_list|(
name|CxfEndpointUtils
operator|.
name|getQName
argument_list|(
name|getPortName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// apply feature here
if|if
condition|(
name|getDataFormat
argument_list|()
operator|==
name|DataFormat
operator|.
name|MESSAGE
condition|)
block|{
name|factoryBean
operator|.
name|getFeatures
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|MessageDataFormatFeature
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|getDataFormat
argument_list|()
operator|==
name|DataFormat
operator|.
name|PAYLOAD
condition|)
block|{
name|factoryBean
operator|.
name|getFeatures
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|PayLoadDataFormatFeature
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|factoryBean
operator|.
name|setBus
argument_list|(
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// Package private methods
comment|// -------------------------------------------------------------------------
comment|/**      * Create a CXF client object      */
DECL|method|createClient ()
name|Client
name|createClient
parameter_list|()
throws|throws
name|Exception
block|{
comment|// get service class
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|getServiceClass
argument_list|()
argument_list|,
name|CxfConstants
operator|.
name|SERVICE_CLASS
argument_list|)
expr_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|cls
init|=
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
name|getServiceClass
argument_list|()
argument_list|,
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
comment|// create client factory bean
name|ClientProxyFactoryBean
name|factoryBean
init|=
name|createClientFactoryBean
argument_list|(
name|cls
argument_list|)
decl_stmt|;
comment|// setup client factory bean
name|setupClientFactoryBean
argument_list|(
name|factoryBean
argument_list|,
name|cls
argument_list|)
expr_stmt|;
return|return
operator|(
operator|(
name|ClientProxy
operator|)
name|Proxy
operator|.
name|getInvocationHandler
argument_list|(
name|factoryBean
operator|.
name|create
argument_list|()
argument_list|)
operator|)
operator|.
name|getClient
argument_list|()
return|;
block|}
comment|/**      * Create a CXF server factory bean      */
DECL|method|createServerFactoryBean ()
name|ServerFactoryBean
name|createServerFactoryBean
parameter_list|()
throws|throws
name|Exception
block|{
comment|// get service class
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|getServiceClass
argument_list|()
argument_list|,
name|CxfConstants
operator|.
name|SERVICE_CLASS
argument_list|)
expr_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|cls
init|=
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
name|getServiceClass
argument_list|()
argument_list|,
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
comment|// create server factory bean
name|ServerFactoryBean
name|answer
init|=
name|CxfEndpointUtils
operator|.
name|getServerFactoryBean
argument_list|(
name|cls
argument_list|)
decl_stmt|;
comment|// setup server factory bean
name|setupServerFactoryBean
argument_list|(
name|answer
argument_list|,
name|cls
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|webServiceProviderAnnotated ()
name|boolean
name|webServiceProviderAnnotated
parameter_list|()
block|{
return|return
name|hasWSProviderAnnotation
return|;
block|}
DECL|method|webServiceAnnotated ()
name|boolean
name|webServiceAnnotated
parameter_list|()
block|{
return|return
name|hasWebServiceAnnotation
return|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
DECL|method|getDataFormat ()
specifier|public
name|DataFormat
name|getDataFormat
parameter_list|()
block|{
return|return
name|dataFormat
return|;
block|}
DECL|method|setDataFormat (DataFormat format)
specifier|public
name|void
name|setDataFormat
parameter_list|(
name|DataFormat
name|format
parameter_list|)
block|{
name|dataFormat
operator|=
name|format
expr_stmt|;
block|}
DECL|method|getWsdlURL ()
specifier|public
name|String
name|getWsdlURL
parameter_list|()
block|{
return|return
name|wsdlURL
return|;
block|}
DECL|method|setWsdlURL (String url)
specifier|public
name|void
name|setWsdlURL
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|wsdlURL
operator|=
name|url
expr_stmt|;
block|}
DECL|method|getServiceClass ()
specifier|public
name|String
name|getServiceClass
parameter_list|()
block|{
return|return
name|serviceClass
return|;
block|}
DECL|method|setServiceClass (String className)
specifier|public
name|void
name|setServiceClass
parameter_list|(
name|String
name|className
parameter_list|)
block|{
name|serviceClass
operator|=
name|className
expr_stmt|;
block|}
DECL|method|setServiceClass (Object instance)
specifier|public
name|void
name|setServiceClass
parameter_list|(
name|Object
name|instance
parameter_list|)
block|{
name|serviceClass
operator|=
name|ClassHelper
operator|.
name|getRealClass
argument_list|(
name|instance
argument_list|)
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
DECL|method|setServiceName (String service)
specifier|public
name|void
name|setServiceName
parameter_list|(
name|String
name|service
parameter_list|)
block|{
name|serviceName
operator|=
name|service
expr_stmt|;
block|}
DECL|method|getServiceName ()
specifier|public
name|String
name|getServiceName
parameter_list|()
block|{
return|return
name|serviceName
return|;
block|}
DECL|method|getPortName ()
specifier|public
name|String
name|getPortName
parameter_list|()
block|{
return|return
name|portName
return|;
block|}
DECL|method|setPortName (String port)
specifier|public
name|void
name|setPortName
parameter_list|(
name|String
name|port
parameter_list|)
block|{
name|portName
operator|=
name|port
expr_stmt|;
block|}
DECL|method|isInOut ()
specifier|public
name|boolean
name|isInOut
parameter_list|()
block|{
return|return
name|inOut
return|;
block|}
DECL|method|setInOut (boolean inOut)
specifier|public
name|void
name|setInOut
parameter_list|(
name|boolean
name|inOut
parameter_list|)
block|{
name|this
operator|.
name|inOut
operator|=
name|inOut
expr_stmt|;
block|}
DECL|method|isWrapped ()
specifier|public
name|boolean
name|isWrapped
parameter_list|()
block|{
return|return
name|isWrapped
return|;
block|}
DECL|method|setWrapped (boolean wrapped)
specifier|public
name|void
name|setWrapped
parameter_list|(
name|boolean
name|wrapped
parameter_list|)
block|{
name|isWrapped
operator|=
name|wrapped
expr_stmt|;
block|}
DECL|method|setCxfBinding (CxfBinding cxfBinding)
specifier|public
name|void
name|setCxfBinding
parameter_list|(
name|CxfBinding
name|cxfBinding
parameter_list|)
block|{
name|this
operator|.
name|cxfBinding
operator|=
name|cxfBinding
expr_stmt|;
block|}
DECL|method|getCxfBinding ()
specifier|public
name|CxfBinding
name|getCxfBinding
parameter_list|()
block|{
if|if
condition|(
name|cxfBinding
operator|==
literal|null
condition|)
block|{
name|cxfBinding
operator|=
operator|new
name|DefaultCxfBinding
argument_list|()
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Create default CXF Binding "
operator|+
name|cxfBinding
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|cxfBindingInitialized
operator|.
name|getAndSet
argument_list|(
literal|true
argument_list|)
operator|&&
name|cxfBinding
operator|instanceof
name|HeaderFilterStrategyAware
condition|)
block|{
operator|(
operator|(
name|HeaderFilterStrategyAware
operator|)
name|cxfBinding
operator|)
operator|.
name|setHeaderFilterStrategy
argument_list|(
name|getHeaderFilterStrategy
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|cxfBinding
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
name|CxfHeaderFilterStrategy
argument_list|()
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Create CXF default header filter strategy "
operator|+
name|headerFilterStrategy
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|headerFilterStrategy
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
name|doGetBus
argument_list|()
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Using DefaultBus "
operator|+
name|bus
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Set bus "
operator|+
name|bus
operator|+
literal|" as thread default bus"
argument_list|)
expr_stmt|;
block|}
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
comment|/**      * We need to override the {@link ClientImpl#setParameters} method      * to insert parameters into CXF Message for {@link DataFormat#PAYLOAD}      * mode.      */
DECL|class|CamelCxfClientImpl
specifier|private
class|class
name|CamelCxfClientImpl
extends|extends
name|ClientImpl
block|{
DECL|method|CamelCxfClientImpl (Bus bus, Endpoint ep)
specifier|public
name|CamelCxfClientImpl
parameter_list|(
name|Bus
name|bus
parameter_list|,
name|Endpoint
name|ep
parameter_list|)
block|{
name|super
argument_list|(
name|bus
argument_list|,
name|ep
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setParameters (Object[] params, Message message)
specifier|protected
name|void
name|setParameters
parameter_list|(
name|Object
index|[]
name|params
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
if|if
condition|(
name|DataFormat
operator|.
name|PAYLOAD
operator|==
name|message
operator|.
name|get
argument_list|(
name|DataFormat
operator|.
name|class
argument_list|)
condition|)
block|{
name|CxfPayload
argument_list|<
name|?
argument_list|>
name|payload
init|=
operator|(
name|CxfPayload
argument_list|<
name|?
argument_list|>
operator|)
name|params
index|[
literal|0
index|]
decl_stmt|;
name|message
operator|.
name|put
argument_list|(
name|List
operator|.
name|class
argument_list|,
name|payload
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Header
operator|.
name|HEADER_LIST
argument_list|,
name|payload
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|super
operator|.
name|setParameters
argument_list|(
name|params
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

