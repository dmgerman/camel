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
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|Definition
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|Service
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
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Provider
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
name|RuntimeCamelException
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
name|endpoint
operator|.
name|EndpointException
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
name|EndpointImpl
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
name|helpers
operator|.
name|CastUtils
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
name|support
operator|.
name|JaxWsServiceFactoryBean
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
name|service
operator|.
name|factory
operator|.
name|FactoryBeanListener
operator|.
name|Event
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
name|service
operator|.
name|invoker
operator|.
name|Invoker
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
name|service
operator|.
name|model
operator|.
name|EndpointInfo
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
name|wsdl
operator|.
name|WSDLManager
import|;
end_import

begin_comment
comment|/**  * A service factory bean class that create a service factory without requiring a service class  * (SEI).  * It will pick the first one service name and first one port/endpoint name in the WSDL, if   * there is service name or port/endpoint name setted.  * @version   */
end_comment

begin_class
DECL|class|WSDLServiceFactoryBean
specifier|public
class|class
name|WSDLServiceFactoryBean
extends|extends
name|JaxWsServiceFactoryBean
block|{
DECL|field|definition
specifier|private
name|Definition
name|definition
decl_stmt|;
DECL|method|WSDLServiceFactoryBean ()
specifier|public
name|WSDLServiceFactoryBean
parameter_list|()
block|{
name|setServiceClass
argument_list|(
name|Provider
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|setServiceClass (Class<?> serviceClass)
specifier|public
name|void
name|setServiceClass
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|serviceClass
parameter_list|)
block|{
if|if
condition|(
name|serviceClass
operator|!=
literal|null
condition|)
block|{
name|super
operator|.
name|setServiceClass
argument_list|(
name|serviceClass
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getDefinition (String url)
specifier|protected
name|Definition
name|getDefinition
parameter_list|(
name|String
name|url
parameter_list|)
block|{
if|if
condition|(
name|definition
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|definition
operator|=
name|getBus
argument_list|()
operator|.
name|getExtension
argument_list|(
name|WSDLManager
operator|.
name|class
argument_list|)
operator|.
name|getDefinition
argument_list|(
name|url
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|this
operator|.
name|getServiceQName
argument_list|(
literal|false
argument_list|)
operator|==
literal|null
condition|)
block|{
name|Map
argument_list|<
name|QName
argument_list|,
name|?
argument_list|>
name|services
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|definition
operator|.
name|getServices
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|services
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"There is no service in the WSDL"
operator|+
name|url
argument_list|)
throw|;
block|}
if|if
condition|(
name|services
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"service name must be specified, there is more than one service in the WSDL"
operator|+
name|url
argument_list|)
throw|;
block|}
name|QName
name|serviceQName
init|=
name|services
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|this
operator|.
name|setServiceName
argument_list|(
name|serviceQName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|this
operator|.
name|getEndpointName
argument_list|(
literal|false
argument_list|)
operator|==
literal|null
condition|)
block|{
name|Service
name|service
init|=
name|definition
operator|.
name|getService
argument_list|(
name|getServiceQName
argument_list|(
literal|false
argument_list|)
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|ports
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|service
operator|.
name|getPorts
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|ports
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"There is no port/endpoint in the service "
operator|+
name|getServiceQName
argument_list|()
operator|+
literal|"of WSDL"
operator|+
name|url
argument_list|)
throw|;
block|}
if|if
condition|(
name|ports
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Port/endpoint name must be specified, There is more than one port in the service"
operator|+
name|service
operator|.
name|getQName
argument_list|()
operator|+
literal|" of the WSDL"
operator|+
name|url
argument_list|)
throw|;
block|}
name|QName
name|endpointQName
init|=
operator|new
name|QName
argument_list|(
name|service
operator|.
name|getQName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|ports
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
name|setEndpointName
argument_list|(
name|endpointQName
argument_list|)
expr_stmt|;
block|}
return|return
name|definition
return|;
block|}
DECL|method|buildServiceFromWSDL (String url)
specifier|protected
name|void
name|buildServiceFromWSDL
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|getDefinition
argument_list|(
name|url
argument_list|)
expr_stmt|;
name|super
operator|.
name|buildServiceFromWSDL
argument_list|(
name|url
argument_list|)
expr_stmt|;
block|}
DECL|method|createEndpoint (EndpointInfo ei)
specifier|public
name|Endpoint
name|createEndpoint
parameter_list|(
name|EndpointInfo
name|ei
parameter_list|)
throws|throws
name|EndpointException
block|{
name|Endpoint
name|ep
init|=
operator|new
name|EndpointImpl
argument_list|(
name|getBus
argument_list|()
argument_list|,
name|getService
argument_list|()
argument_list|,
name|ei
argument_list|)
decl_stmt|;
name|sendEvent
argument_list|(
name|Event
operator|.
name|ENDPOINT_CREATED
argument_list|,
name|ei
argument_list|,
name|ep
argument_list|,
name|getServiceClass
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|ep
return|;
block|}
annotation|@
name|Override
DECL|method|initializeWSDLOperations ()
specifier|protected
name|void
name|initializeWSDLOperations
parameter_list|()
block|{
comment|// skip this operation that requires service class
block|}
annotation|@
name|Override
DECL|method|checkServiceClassAnnotations (Class<?> sc)
specifier|protected
name|void
name|checkServiceClassAnnotations
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|sc
parameter_list|)
block|{
comment|// skip this operation that requires service class
block|}
annotation|@
name|Override
DECL|method|createInvoker ()
specifier|protected
name|Invoker
name|createInvoker
parameter_list|()
block|{
comment|// Camel specific invoker will be set
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

