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
name|blueprint
operator|.
name|BlueprintSupport
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
name|blueprint
operator|.
name|RsClientBlueprintBean
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
name|blueprint
operator|.
name|RsServerBlueprintBean
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
name|AbstractJAXRSFactoryBean
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

begin_class
DECL|class|CxfRsBlueprintEndpoint
specifier|public
class|class
name|CxfRsBlueprintEndpoint
extends|extends
name|CxfRsEndpoint
block|{
DECL|field|bean
specifier|private
name|AbstractJAXRSFactoryBean
name|bean
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
annotation|@
name|Deprecated
comment|/**      * It will be removed in Camel 3.0      * @param comp      * @param bean      */
DECL|method|CxfRsBlueprintEndpoint (Component comp, AbstractJAXRSFactoryBean bean)
specifier|public
name|CxfRsBlueprintEndpoint
parameter_list|(
name|Component
name|comp
parameter_list|,
name|AbstractJAXRSFactoryBean
name|bean
parameter_list|)
block|{
name|super
argument_list|(
name|bean
operator|.
name|getAddress
argument_list|()
argument_list|,
name|comp
argument_list|)
expr_stmt|;
name|this
operator|.
name|bean
operator|=
name|bean
expr_stmt|;
name|BlueprintSupport
name|support
init|=
operator|(
name|BlueprintSupport
operator|)
name|bean
decl_stmt|;
name|setBlueprintContainer
argument_list|(
name|support
operator|.
name|getBlueprintContainer
argument_list|()
argument_list|)
expr_stmt|;
name|setBundleContext
argument_list|(
name|support
operator|.
name|getBundleContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|CxfRsBlueprintEndpoint (Component comp, String uri, AbstractJAXRSFactoryBean bean)
specifier|public
name|CxfRsBlueprintEndpoint
parameter_list|(
name|Component
name|comp
parameter_list|,
name|String
name|uri
parameter_list|,
name|AbstractJAXRSFactoryBean
name|bean
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|comp
argument_list|)
expr_stmt|;
name|this
operator|.
name|bean
operator|=
name|bean
expr_stmt|;
name|setAddress
argument_list|(
name|bean
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
comment|// update the sfb address by resolving the properties
name|bean
operator|.
name|setAddress
argument_list|(
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
name|BlueprintSupport
name|support
init|=
operator|(
name|BlueprintSupport
operator|)
name|bean
decl_stmt|;
name|setBlueprintContainer
argument_list|(
name|support
operator|.
name|getBlueprintContainer
argument_list|()
argument_list|)
expr_stmt|;
name|setBundleContext
argument_list|(
name|support
operator|.
name|getBundleContext
argument_list|()
argument_list|)
expr_stmt|;
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
annotation|@
name|Override
DECL|method|newJAXRSServerFactoryBean ()
specifier|protected
name|JAXRSServerFactoryBean
name|newJAXRSServerFactoryBean
parameter_list|()
block|{
name|checkBeanType
argument_list|(
name|bean
argument_list|,
name|JAXRSServerFactoryBean
operator|.
name|class
argument_list|)
expr_stmt|;
return|return
operator|(
name|RsServerBlueprintBean
operator|)
name|bean
return|;
block|}
annotation|@
name|Override
DECL|method|newJAXRSClientFactoryBean ()
specifier|protected
name|JAXRSClientFactoryBean
name|newJAXRSClientFactoryBean
parameter_list|()
block|{
name|checkBeanType
argument_list|(
name|bean
argument_list|,
name|JAXRSClientFactoryBean
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// TODO Need to find a way to setup the JAXRSClientFactory Bean, as the JAXRSClientFactoryBean properties could be changed by the configurer
return|return
operator|(
name|RsClientBlueprintBean
operator|)
name|bean
return|;
block|}
block|}
end_class

end_unit

