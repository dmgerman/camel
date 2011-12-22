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
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|common
operator|.
name|message
operator|.
name|CxfConstants
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
name|bus
operator|.
name|spring
operator|.
name|BusWiringBeanFactoryPostProcessor
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
name|bus
operator|.
name|spring
operator|.
name|SpringBusFactory
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
name|version
operator|.
name|Version
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|BeansException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContextAware
import|;
end_import

begin_comment
comment|/**  * Defines the<a href="http://camel.apache.org/cxf.html">CXF Endpoint</a>  *  * @version   */
end_comment

begin_class
DECL|class|CxfSpringEndpoint
specifier|public
class|class
name|CxfSpringEndpoint
extends|extends
name|CxfEndpoint
implements|implements
name|ApplicationContextAware
block|{
DECL|field|beanId
specifier|private
name|String
name|beanId
decl_stmt|;
DECL|field|applicationContext
specifier|private
name|ApplicationContext
name|applicationContext
decl_stmt|;
DECL|method|CxfSpringEndpoint (CxfComponent component, String address)
specifier|public
name|CxfSpringEndpoint
parameter_list|(
name|CxfComponent
name|component
parameter_list|,
name|String
name|address
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|address
argument_list|,
name|component
argument_list|)
expr_stmt|;
block|}
DECL|method|CxfSpringEndpoint ()
specifier|public
name|CxfSpringEndpoint
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
comment|/**      *       * A help to get the service class.  The serviceClass classname in URI       * query takes precedence over the serviceClass in CxfEndpointBean.      */
DECL|method|getSEIClass ()
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|getSEIClass
parameter_list|()
throws|throws
name|ClassNotFoundException
block|{
comment|// get service class
name|Class
argument_list|<
name|?
argument_list|>
name|answer
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|getServiceClass
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// classname is specified in URI which overrides the bean properties
name|answer
operator|=
name|getServiceClass
argument_list|()
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
comment|// Package private methods
comment|// -------------------------------------------------------------------------
comment|/**      * Create a CXF Client      */
annotation|@
name|Override
DECL|method|createClient ()
name|Client
name|createClient
parameter_list|()
throws|throws
name|Exception
block|{
comment|// get service class
name|Class
argument_list|<
name|?
argument_list|>
name|cls
init|=
name|getSEIClass
argument_list|()
decl_stmt|;
if|if
condition|(
name|getDataFormat
argument_list|()
operator|.
name|equals
argument_list|(
name|DataFormat
operator|.
name|POJO
argument_list|)
condition|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|cls
argument_list|,
name|CxfConstants
operator|.
name|SERVICE_CLASS
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getWsdlURL
argument_list|()
operator|==
literal|null
operator|&&
name|cls
operator|==
literal|null
condition|)
block|{
comment|// no WSDL and serviceClass specified, set our default serviceClass
name|setServiceClass
argument_list|(
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
name|DefaultSEI
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|setDefaultOperationNamespace
argument_list|(
name|CxfConstants
operator|.
name|DISPATCH_NAMESPACE
argument_list|)
expr_stmt|;
name|setDefaultOperationName
argument_list|(
name|CxfConstants
operator|.
name|DISPATCH_DEFAULT_OPERATION_NAMESPACE
argument_list|)
expr_stmt|;
if|if
condition|(
name|getDataFormat
argument_list|()
operator|.
name|equals
argument_list|(
name|DataFormat
operator|.
name|PAYLOAD
argument_list|)
condition|)
block|{
name|setSkipPayloadMessagePartCheck
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|cls
operator|=
name|getSEIClass
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|cls
operator|!=
literal|null
condition|)
block|{
comment|// create client factory bean
name|ClientFactoryBean
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
comment|// fill in values that have not been filled.
name|QName
name|serviceQName
init|=
literal|null
decl_stmt|;
try|try
block|{
name|serviceQName
operator|=
name|factoryBean
operator|.
name|getServiceName
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|e
parameter_list|)
block|{
comment|// It throws IllegalStateException if serviceName has not been set.
block|}
if|if
condition|(
name|serviceQName
operator|==
literal|null
operator|&&
name|getServiceLocalName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|factoryBean
operator|.
name|setServiceName
argument_list|(
operator|new
name|QName
argument_list|(
name|getServiceNamespace
argument_list|()
argument_list|,
name|getServiceLocalName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|factoryBean
operator|.
name|getEndpointName
argument_list|()
operator|==
literal|null
operator|&&
name|getEndpointLocalName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|factoryBean
operator|.
name|setEndpointName
argument_list|(
operator|new
name|QName
argument_list|(
name|getEndpointNamespace
argument_list|()
argument_list|,
name|getEndpointLocalName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Client
name|client
init|=
name|factoryBean
operator|.
name|create
argument_list|()
decl_stmt|;
comment|// setup the handlers
name|setupHandlers
argument_list|(
name|factoryBean
argument_list|,
name|client
argument_list|)
expr_stmt|;
return|return
name|client
return|;
block|}
else|else
block|{
name|ClientFactoryBean
name|factoryBean
init|=
name|createClientFactoryBean
argument_list|()
decl_stmt|;
comment|// setup client factory bean
name|setupClientFactoryBean
argument_list|(
name|factoryBean
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// fill in values that have not been filled.
name|QName
name|serviceQName
init|=
literal|null
decl_stmt|;
try|try
block|{
name|serviceQName
operator|=
name|factoryBean
operator|.
name|getServiceName
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|e
parameter_list|)
block|{
comment|// It throws IllegalStateException if serviceName has not been set.
block|}
if|if
condition|(
name|serviceQName
operator|==
literal|null
operator|&&
name|getServiceLocalName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|factoryBean
operator|.
name|setServiceName
argument_list|(
operator|new
name|QName
argument_list|(
name|getServiceNamespace
argument_list|()
argument_list|,
name|getServiceLocalName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|factoryBean
operator|.
name|getEndpointName
argument_list|()
operator|==
literal|null
operator|&&
name|getEndpointLocalName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|factoryBean
operator|.
name|setEndpointName
argument_list|(
operator|new
name|QName
argument_list|(
name|getEndpointNamespace
argument_list|()
argument_list|,
name|getEndpointLocalName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|checkName
argument_list|(
name|factoryBean
operator|.
name|getEndpointName
argument_list|()
argument_list|,
literal|"endpoint/port name"
argument_list|)
expr_stmt|;
name|checkName
argument_list|(
name|factoryBean
operator|.
name|getServiceName
argument_list|()
argument_list|,
literal|"service name"
argument_list|)
expr_stmt|;
return|return
operator|(
name|Client
operator|)
name|factoryBean
operator|.
name|create
argument_list|()
return|;
block|}
block|}
comment|/**      * Create a service factory bean      */
annotation|@
name|Override
DECL|method|createServerFactoryBean ()
name|ServerFactoryBean
name|createServerFactoryBean
parameter_list|()
throws|throws
name|Exception
block|{
comment|// get service class
name|Class
argument_list|<
name|?
argument_list|>
name|cls
init|=
name|getSEIClass
argument_list|()
decl_stmt|;
comment|// create server factory bean
comment|// Shouldn't use CxfEndpointUtils.getServerFactoryBean(cls) as it is for
comment|// CxfSoapComponent
name|ServerFactoryBean
name|answer
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|cls
operator|==
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|getDataFormat
argument_list|()
operator|.
name|equals
argument_list|(
name|DataFormat
operator|.
name|POJO
argument_list|)
condition|)
block|{
name|answer
operator|=
operator|new
name|ServerFactoryBean
argument_list|(
operator|new
name|WSDLServiceFactoryBean
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|cls
argument_list|,
name|CxfConstants
operator|.
name|SERVICE_CLASS
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|CxfEndpointUtils
operator|.
name|hasWebServiceAnnotation
argument_list|(
name|cls
argument_list|)
condition|)
block|{
name|answer
operator|=
operator|new
name|JaxWsServerFactoryBean
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
operator|new
name|ServerFactoryBean
argument_list|()
expr_stmt|;
block|}
comment|// setup server factory bean
name|setupServerFactoryBean
argument_list|(
name|answer
argument_list|,
name|cls
argument_list|)
expr_stmt|;
comment|// fill in values that have not been filled.
if|if
condition|(
name|answer
operator|.
name|getServiceName
argument_list|()
operator|==
literal|null
operator|&&
name|getServiceLocalName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setServiceName
argument_list|(
operator|new
name|QName
argument_list|(
name|getServiceNamespace
argument_list|()
argument_list|,
name|getServiceLocalName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|answer
operator|.
name|getEndpointName
argument_list|()
operator|==
literal|null
operator|&&
name|getEndpointLocalName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setEndpointName
argument_list|(
operator|new
name|QName
argument_list|(
name|getEndpointNamespace
argument_list|()
argument_list|,
name|getEndpointLocalName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|cls
operator|==
literal|null
condition|)
block|{
name|checkName
argument_list|(
name|answer
operator|.
name|getEndpointName
argument_list|()
argument_list|,
literal|"endpoint/port name"
argument_list|)
expr_stmt|;
name|checkName
argument_list|(
name|answer
operator|.
name|getServiceName
argument_list|()
argument_list|,
literal|"service name"
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
DECL|method|getBeanId ()
specifier|public
name|String
name|getBeanId
parameter_list|()
block|{
return|return
name|beanId
return|;
block|}
comment|// this property will be set by spring
DECL|method|setBeanId (String id)
specifier|public
name|void
name|setBeanId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|beanId
operator|=
name|id
expr_stmt|;
block|}
DECL|method|setServiceNamespace (String serviceNamespace)
specifier|public
name|void
name|setServiceNamespace
parameter_list|(
name|String
name|serviceNamespace
parameter_list|)
block|{
name|QName
name|qn
init|=
name|getServiceName
argument_list|()
decl_stmt|;
if|if
condition|(
name|qn
operator|==
literal|null
condition|)
block|{
name|setServiceName
argument_list|(
operator|new
name|QName
argument_list|(
name|serviceNamespace
argument_list|,
literal|"local"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setServiceName
argument_list|(
operator|new
name|QName
argument_list|(
name|serviceNamespace
argument_list|,
name|qn
operator|.
name|getLocalPart
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getServiceNamespace ()
specifier|public
name|String
name|getServiceNamespace
parameter_list|()
block|{
name|QName
name|qn
init|=
name|getServiceName
argument_list|()
decl_stmt|;
if|if
condition|(
name|qn
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|qn
operator|.
name|getNamespaceURI
argument_list|()
return|;
block|}
DECL|method|setServiceLocalName (String serviceLocalName)
specifier|public
name|void
name|setServiceLocalName
parameter_list|(
name|String
name|serviceLocalName
parameter_list|)
block|{
name|QName
name|qn
init|=
name|getServiceName
argument_list|()
decl_stmt|;
if|if
condition|(
name|qn
operator|==
literal|null
condition|)
block|{
name|setServiceName
argument_list|(
operator|new
name|QName
argument_list|(
literal|""
argument_list|,
name|serviceLocalName
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setServiceName
argument_list|(
operator|new
name|QName
argument_list|(
name|qn
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|serviceLocalName
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getServiceLocalName ()
specifier|public
name|String
name|getServiceLocalName
parameter_list|()
block|{
name|QName
name|qn
init|=
name|getServiceName
argument_list|()
decl_stmt|;
if|if
condition|(
name|qn
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|qn
operator|.
name|getLocalPart
argument_list|()
return|;
block|}
DECL|method|getEndpointLocalName ()
specifier|public
name|String
name|getEndpointLocalName
parameter_list|()
block|{
name|QName
name|qn
init|=
name|getPortName
argument_list|()
decl_stmt|;
if|if
condition|(
name|qn
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|qn
operator|.
name|getLocalPart
argument_list|()
return|;
block|}
DECL|method|setEndpointLocalName (String endpointLocalName)
specifier|public
name|void
name|setEndpointLocalName
parameter_list|(
name|String
name|endpointLocalName
parameter_list|)
block|{
name|QName
name|qn
init|=
name|getPortName
argument_list|()
decl_stmt|;
if|if
condition|(
name|qn
operator|==
literal|null
condition|)
block|{
name|setPortName
argument_list|(
operator|new
name|QName
argument_list|(
literal|""
argument_list|,
name|endpointLocalName
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setPortName
argument_list|(
operator|new
name|QName
argument_list|(
name|qn
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|endpointLocalName
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|setEndpointNamespace (String endpointNamespace)
specifier|public
name|void
name|setEndpointNamespace
parameter_list|(
name|String
name|endpointNamespace
parameter_list|)
block|{
name|QName
name|qn
init|=
name|getPortName
argument_list|()
decl_stmt|;
if|if
condition|(
name|qn
operator|==
literal|null
condition|)
block|{
name|setPortName
argument_list|(
operator|new
name|QName
argument_list|(
name|endpointNamespace
argument_list|,
literal|"local"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setPortName
argument_list|(
operator|new
name|QName
argument_list|(
name|endpointNamespace
argument_list|,
name|qn
operator|.
name|getLocalPart
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getEndpointNamespace ()
specifier|public
name|String
name|getEndpointNamespace
parameter_list|()
block|{
name|QName
name|qn
init|=
name|getPortName
argument_list|()
decl_stmt|;
if|if
condition|(
name|qn
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|qn
operator|.
name|getNamespaceURI
argument_list|()
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
DECL|method|setApplicationContext (ApplicationContext ctx)
specifier|public
name|void
name|setApplicationContext
parameter_list|(
name|ApplicationContext
name|ctx
parameter_list|)
throws|throws
name|BeansException
block|{
name|applicationContext
operator|=
name|ctx
expr_stmt|;
if|if
condition|(
name|bus
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|Version
operator|.
name|getCurrentVersion
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"2.3"
argument_list|)
condition|)
block|{
comment|// Don't relate on the DefaultBus
name|BusFactory
name|factory
init|=
operator|new
name|SpringBusFactory
argument_list|(
name|ctx
argument_list|)
decl_stmt|;
name|bus
operator|=
name|factory
operator|.
name|createBus
argument_list|()
expr_stmt|;
name|BusWiringBeanFactoryPostProcessor
operator|.
name|updateBusReferencesInContext
argument_list|(
name|bus
argument_list|,
name|ctx
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|bus
operator|=
name|BusWiringBeanFactoryPostProcessor
operator|.
name|addDefaultBus
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|getApplicationContext ()
specifier|public
name|ApplicationContext
name|getApplicationContext
parameter_list|()
block|{
return|return
name|applicationContext
return|;
block|}
block|}
end_class

end_unit

