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
argument_list|,
operator|(
name|CxfComponent
operator|)
literal|null
argument_list|)
expr_stmt|;
name|bundleContext
operator|=
name|context
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

