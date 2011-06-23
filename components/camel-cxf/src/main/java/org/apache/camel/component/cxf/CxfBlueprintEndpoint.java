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
name|Map
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
name|handler
operator|.
name|Handler
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
name|blueprint
operator|.
name|BlueprintCamelContext
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
name|jaxws
operator|.
name|JaxWsServerFactoryBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|blueprint
operator|.
name|container
operator|.
name|BlueprintContainer
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
DECL|class|CxfBlueprintEndpoint
specifier|public
class|class
name|CxfBlueprintEndpoint
extends|extends
name|CxfEndpoint
block|{
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
name|CxfBlueprintEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|properties
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
decl_stmt|;
DECL|field|handlers
specifier|private
name|List
argument_list|<
name|Handler
argument_list|>
name|handlers
decl_stmt|;
DECL|field|schemaLocations
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|schemaLocations
decl_stmt|;
DECL|field|blueprintContainer
specifier|private
name|BlueprintContainer
name|blueprintContainer
decl_stmt|;
DECL|field|bundleContext
specifier|private
name|BundleContext
name|bundleContext
decl_stmt|;
DECL|field|blueprintCamelContext
specifier|private
name|BlueprintCamelContext
name|blueprintCamelContext
decl_stmt|;
DECL|method|CxfBlueprintEndpoint (String address, BundleContext context)
specifier|public
name|CxfBlueprintEndpoint
parameter_list|(
name|String
name|address
parameter_list|,
name|BundleContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|bundleContext
operator|=
name|context
expr_stmt|;
block|}
DECL|method|getHandlers ()
specifier|public
name|List
argument_list|<
name|Handler
argument_list|>
name|getHandlers
parameter_list|()
block|{
return|return
name|handlers
return|;
block|}
DECL|method|setHandlers (List<Handler> handlers)
specifier|public
name|void
name|setHandlers
parameter_list|(
name|List
argument_list|<
name|Handler
argument_list|>
name|handlers
parameter_list|)
block|{
name|this
operator|.
name|handlers
operator|=
name|handlers
expr_stmt|;
block|}
DECL|method|destroy ()
specifier|public
name|void
name|destroy
parameter_list|()
block|{
comment|// Clean up the BusFactory's defaultBus
comment|// This method is not called magically, blueprint
comment|// needs you to set the destroy-method.
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|setServiceClass (String n)
specifier|public
name|void
name|setServiceClass
parameter_list|(
name|String
name|n
parameter_list|)
throws|throws
name|ClassNotFoundException
block|{
name|setServiceClass
argument_list|(
name|bundleContext
operator|.
name|getBundle
argument_list|()
operator|.
name|loadClass
argument_list|(
name|n
argument_list|)
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
name|getServiceClass
argument_list|()
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
name|getServiceClass
argument_list|()
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
block|}
name|Class
argument_list|<
name|?
argument_list|>
name|cls
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
comment|//Fool CXF classes to load their settings and bindings from the CXF bundle
name|cls
operator|=
name|getServiceClass
argument_list|()
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
else|else
block|{
name|checkName
argument_list|(
name|getPortName
argument_list|()
argument_list|,
literal|"endpoint/port name"
argument_list|)
expr_stmt|;
name|checkName
argument_list|(
name|getServiceName
argument_list|()
argument_list|,
literal|"service name"
argument_list|)
expr_stmt|;
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
argument_list|)
expr_stmt|;
return|return
name|factoryBean
operator|.
name|create
argument_list|()
return|;
block|}
block|}
DECL|method|checkName (Object value, String name)
specifier|protected
name|void
name|checkName
parameter_list|(
name|Object
name|value
parameter_list|,
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|value
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"The "
operator|+
name|name
operator|+
literal|" of "
operator|+
name|this
operator|.
name|getEndpointUri
argument_list|()
operator|+
literal|" is empty, cxf will try to load the first one in wsdl for you."
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Create a CXF server factory bean      */
DECL|method|createServerFactoryBean ()
name|ServerFactoryBean
name|createServerFactoryBean
parameter_list|()
throws|throws
name|Exception
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|cls
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|getDataFormat
argument_list|()
operator|==
name|DataFormat
operator|.
name|POJO
operator|||
name|getServiceClass
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// get service class
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|getServiceClass
argument_list|()
argument_list|,
name|CxfConstants
operator|.
name|SERVICE_CLASS
argument_list|)
expr_stmt|;
name|cls
operator|=
name|getServiceClass
argument_list|()
expr_stmt|;
block|}
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
name|checkName
argument_list|(
name|getPortName
argument_list|()
argument_list|,
literal|" endpoint/port name"
argument_list|)
expr_stmt|;
name|checkName
argument_list|(
name|getServiceName
argument_list|()
argument_list|,
literal|" service name"
argument_list|)
expr_stmt|;
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
return|return
name|answer
return|;
block|}
DECL|method|setSchemaLocations (List<String> schemaLocations)
specifier|public
name|void
name|setSchemaLocations
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|schemaLocations
parameter_list|)
block|{
name|this
operator|.
name|schemaLocations
operator|=
name|schemaLocations
expr_stmt|;
block|}
DECL|method|getSchemaLocations ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getSchemaLocations
parameter_list|()
block|{
return|return
name|schemaLocations
return|;
block|}
DECL|method|getBlueprintContainer ()
specifier|public
name|BlueprintContainer
name|getBlueprintContainer
parameter_list|()
block|{
return|return
name|blueprintContainer
return|;
block|}
DECL|method|setBlueprintContainer (BlueprintContainer blueprintContainer)
specifier|public
name|void
name|setBlueprintContainer
parameter_list|(
name|BlueprintContainer
name|blueprintContainer
parameter_list|)
block|{
name|this
operator|.
name|blueprintContainer
operator|=
name|blueprintContainer
expr_stmt|;
block|}
DECL|method|getBundleContext ()
specifier|public
name|BundleContext
name|getBundleContext
parameter_list|()
block|{
return|return
name|bundleContext
return|;
block|}
DECL|method|setBundleContext (BundleContext bundleContext)
specifier|public
name|void
name|setBundleContext
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|)
block|{
name|this
operator|.
name|bundleContext
operator|=
name|bundleContext
expr_stmt|;
block|}
DECL|method|getBlueprintCamelContext ()
specifier|public
name|BlueprintCamelContext
name|getBlueprintCamelContext
parameter_list|()
block|{
return|return
name|blueprintCamelContext
return|;
block|}
DECL|method|setBlueprintCamelContext (BlueprintCamelContext blueprintCamelContext)
specifier|public
name|void
name|setBlueprintCamelContext
parameter_list|(
name|BlueprintCamelContext
name|blueprintCamelContext
parameter_list|)
block|{
name|this
operator|.
name|blueprintCamelContext
operator|=
name|blueprintCamelContext
expr_stmt|;
block|}
DECL|method|getProperties ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getProperties
parameter_list|()
block|{
return|return
name|properties
return|;
block|}
DECL|method|setProperties (Map<String, Object> properties)
specifier|public
name|void
name|setProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|)
block|{
name|this
operator|.
name|properties
operator|=
name|properties
expr_stmt|;
block|}
DECL|method|getBean ()
specifier|public
name|CxfBlueprintEndpoint
name|getBean
parameter_list|()
block|{
return|return
name|this
return|;
block|}
block|}
end_class

end_unit

