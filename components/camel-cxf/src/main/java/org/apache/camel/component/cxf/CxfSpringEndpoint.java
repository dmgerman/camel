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
name|HashMap
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
name|spring
operator|.
name|CxfEndpointBean
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
name|spring
operator|.
name|SpringCamelContext
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
name|IntrospectionSupport
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
name|configuration
operator|.
name|spring
operator|.
name|ConfigurerImpl
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

begin_comment
comment|/**  * Defines the<a href="http://activemq.apache.org/camel/cxf.html">CXF Endpoint</a>  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|CxfSpringEndpoint
specifier|public
class|class
name|CxfSpringEndpoint
extends|extends
name|CxfEndpoint
block|{
DECL|field|bean
specifier|private
name|CxfEndpointBean
name|bean
decl_stmt|;
DECL|field|beanId
specifier|private
name|String
name|beanId
decl_stmt|;
DECL|field|configurer
specifier|private
name|ConfigurerImpl
name|configurer
decl_stmt|;
DECL|field|serviceNamespace
specifier|private
name|String
name|serviceNamespace
decl_stmt|;
DECL|field|serviceLocalName
specifier|private
name|String
name|serviceLocalName
decl_stmt|;
DECL|field|endpointLocalName
specifier|private
name|String
name|endpointLocalName
decl_stmt|;
DECL|field|endpointNamespace
specifier|private
name|String
name|endpointNamespace
decl_stmt|;
DECL|method|CxfSpringEndpoint (CxfComponent cxfComponent, String beanId, CxfEndpointBean bean)
specifier|public
name|CxfSpringEndpoint
parameter_list|(
name|CxfComponent
name|cxfComponent
parameter_list|,
name|String
name|beanId
parameter_list|,
name|CxfEndpointBean
name|bean
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|cxfComponent
argument_list|,
name|bean
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|beanId
operator|=
name|beanId
expr_stmt|;
name|this
operator|.
name|bean
operator|=
name|bean
expr_stmt|;
comment|// set properties from bean which can be overridden by endpoint URI
name|setPropertiesBean
argument_list|()
expr_stmt|;
comment|// create configurer
name|configurer
operator|=
operator|new
name|ConfigurerImpl
argument_list|(
operator|(
operator|(
name|SpringCamelContext
operator|)
name|getCamelContext
argument_list|()
operator|)
operator|.
name|getApplicationContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Read properties from the CxfEndpointBean and copy them to the       * properties of this class.  Note that the properties values can       * be overridden by values in URI query as the DefaultComponent       * will perform "setProperties" later (after the constructor).       */
DECL|method|setPropertiesBean ()
specifier|private
name|void
name|setPropertiesBean
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|bean
operator|.
name|getProperties
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|copy
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|copy
operator|.
name|putAll
argument_list|(
name|bean
operator|.
name|getProperties
argument_list|()
argument_list|)
expr_stmt|;
comment|// pass the copy the method modifies the properties map
name|IntrospectionSupport
operator|.
name|setProperties
argument_list|(
name|getCamelContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
argument_list|,
name|this
argument_list|,
name|copy
argument_list|)
expr_stmt|;
block|}
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
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
name|bean
operator|.
name|getServiceClass
argument_list|()
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|doGetBus ()
specifier|protected
name|Bus
name|doGetBus
parameter_list|()
block|{
return|return
name|bean
operator|.
name|getBus
argument_list|()
return|;
block|}
DECL|method|getBean ()
specifier|public
name|CxfEndpointBean
name|getBean
parameter_list|()
block|{
return|return
name|bean
return|;
block|}
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
comment|// create client factory bean
name|ClientProxyFactoryBean
name|factoryBean
init|=
name|createClientFactoryBean
argument_list|(
name|cls
argument_list|)
decl_stmt|;
comment|// configure client factory bean by CXF configurer
name|configure
argument_list|(
name|factoryBean
argument_list|)
expr_stmt|;
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
comment|// configure server factory bean by CXF configurer
name|configure
argument_list|(
name|answer
argument_list|)
expr_stmt|;
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
return|return
name|answer
return|;
block|}
DECL|method|configure (Object beanInstance)
name|void
name|configure
parameter_list|(
name|Object
name|beanInstance
parameter_list|)
block|{
name|configurer
operator|.
name|configureBean
argument_list|(
name|beanId
argument_list|,
name|beanInstance
argument_list|)
expr_stmt|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
DECL|method|setServiceNamespace (String serviceNamespace)
specifier|public
name|void
name|setServiceNamespace
parameter_list|(
name|String
name|serviceNamespace
parameter_list|)
block|{
name|this
operator|.
name|serviceNamespace
operator|=
name|serviceNamespace
expr_stmt|;
block|}
DECL|method|getServiceNamespace ()
specifier|public
name|String
name|getServiceNamespace
parameter_list|()
block|{
return|return
name|serviceNamespace
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
name|this
operator|.
name|serviceLocalName
operator|=
name|serviceLocalName
expr_stmt|;
block|}
DECL|method|getServiceLocalName ()
specifier|public
name|String
name|getServiceLocalName
parameter_list|()
block|{
return|return
name|serviceLocalName
return|;
block|}
DECL|method|getEndpointLocalName ()
specifier|public
name|String
name|getEndpointLocalName
parameter_list|()
block|{
return|return
name|endpointLocalName
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
name|this
operator|.
name|endpointLocalName
operator|=
name|endpointLocalName
expr_stmt|;
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
name|this
operator|.
name|endpointNamespace
operator|=
name|endpointNamespace
expr_stmt|;
block|}
DECL|method|getEndpointNamespace ()
specifier|public
name|String
name|getEndpointNamespace
parameter_list|()
block|{
return|return
name|endpointNamespace
return|;
block|}
block|}
end_class

end_unit

