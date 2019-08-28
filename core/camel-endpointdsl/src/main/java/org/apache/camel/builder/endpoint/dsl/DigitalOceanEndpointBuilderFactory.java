begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder.endpoint.dsl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|endpoint
operator|.
name|dsl
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Generated
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
name|builder
operator|.
name|EndpointConsumerBuilder
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
name|builder
operator|.
name|EndpointProducerBuilder
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
name|builder
operator|.
name|endpoint
operator|.
name|AbstractEndpointBuilder
import|;
end_import

begin_comment
comment|/**  * The DigitalOcean component allows you to manage Droplets and resources within  * the DigitalOcean cloud.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|DigitalOceanEndpointBuilderFactory
specifier|public
interface|interface
name|DigitalOceanEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the DigitalOcean component.      */
DECL|interface|DigitalOceanEndpointBuilder
specifier|public
interface|interface
name|DigitalOceanEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedDigitalOceanEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedDigitalOceanEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Use for pagination. Force the page number.          *           * The option is a:<code>java.lang.Integer</code> type.          *           * Group: producer          */
DECL|method|page (Integer page)
specifier|default
name|DigitalOceanEndpointBuilder
name|page
parameter_list|(
name|Integer
name|page
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"page"
argument_list|,
name|page
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Use for pagination. Force the page number.          *           * The option will be converted to a<code>java.lang.Integer</code>          * type.          *           * Group: producer          */
DECL|method|page (String page)
specifier|default
name|DigitalOceanEndpointBuilder
name|page
parameter_list|(
name|String
name|page
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"page"
argument_list|,
name|page
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Use for pagination. Set the number of item per request. The maximum          * number of results per page is 200.          *           * The option is a:<code>java.lang.Integer</code> type.          *           * Group: producer          */
DECL|method|perPage (Integer perPage)
specifier|default
name|DigitalOceanEndpointBuilder
name|perPage
parameter_list|(
name|Integer
name|perPage
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"perPage"
argument_list|,
name|perPage
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Use for pagination. Set the number of item per request. The maximum          * number of results per page is 200.          *           * The option will be converted to a<code>java.lang.Integer</code>          * type.          *           * Group: producer          */
DECL|method|perPage (String perPage)
specifier|default
name|DigitalOceanEndpointBuilder
name|perPage
parameter_list|(
name|String
name|perPage
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"perPage"
argument_list|,
name|perPage
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The DigitalOcean resource type on which perform the operation.          *           * The option is a:          *<code>org.apache.camel.component.digitalocean.constants.DigitalOceanResources</code> type.          *           * Required: true          * Group: producer          */
DECL|method|resource ( DigitalOceanResources resource)
specifier|default
name|DigitalOceanEndpointBuilder
name|resource
parameter_list|(
name|DigitalOceanResources
name|resource
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"resource"
argument_list|,
name|resource
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The DigitalOcean resource type on which perform the operation.          *           * The option will be converted to a          *<code>org.apache.camel.component.digitalocean.constants.DigitalOceanResources</code> type.          *           * Required: true          * Group: producer          */
DECL|method|resource (String resource)
specifier|default
name|DigitalOceanEndpointBuilder
name|resource
parameter_list|(
name|String
name|resource
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"resource"
argument_list|,
name|resource
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Set a proxy host if needed.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: proxy          */
DECL|method|httpProxyHost (String httpProxyHost)
specifier|default
name|DigitalOceanEndpointBuilder
name|httpProxyHost
parameter_list|(
name|String
name|httpProxyHost
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"httpProxyHost"
argument_list|,
name|httpProxyHost
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Set a proxy password if needed.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: proxy          */
DECL|method|httpProxyPassword ( String httpProxyPassword)
specifier|default
name|DigitalOceanEndpointBuilder
name|httpProxyPassword
parameter_list|(
name|String
name|httpProxyPassword
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"httpProxyPassword"
argument_list|,
name|httpProxyPassword
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Set a proxy port if needed.          *           * The option is a:<code>java.lang.Integer</code> type.          *           * Group: proxy          */
DECL|method|httpProxyPort (Integer httpProxyPort)
specifier|default
name|DigitalOceanEndpointBuilder
name|httpProxyPort
parameter_list|(
name|Integer
name|httpProxyPort
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"httpProxyPort"
argument_list|,
name|httpProxyPort
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Set a proxy port if needed.          *           * The option will be converted to a<code>java.lang.Integer</code>          * type.          *           * Group: proxy          */
DECL|method|httpProxyPort (String httpProxyPort)
specifier|default
name|DigitalOceanEndpointBuilder
name|httpProxyPort
parameter_list|(
name|String
name|httpProxyPort
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"httpProxyPort"
argument_list|,
name|httpProxyPort
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Set a proxy host if needed.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: proxy          */
DECL|method|httpProxyUser (String httpProxyUser)
specifier|default
name|DigitalOceanEndpointBuilder
name|httpProxyUser
parameter_list|(
name|String
name|httpProxyUser
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"httpProxyUser"
argument_list|,
name|httpProxyUser
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * DigitalOcean OAuth Token.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: security          */
DECL|method|oAuthToken (String oAuthToken)
specifier|default
name|DigitalOceanEndpointBuilder
name|oAuthToken
parameter_list|(
name|String
name|oAuthToken
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"oAuthToken"
argument_list|,
name|oAuthToken
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the DigitalOcean component.      */
DECL|interface|AdvancedDigitalOceanEndpointBuilder
specifier|public
interface|interface
name|AdvancedDigitalOceanEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|DigitalOceanEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|DigitalOceanEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedDigitalOceanEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( String basicPropertyBinding)
specifier|default
name|AdvancedDigitalOceanEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|String
name|basicPropertyBinding
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use a existing configured DigitalOceanClient as client.          *           * The option is a:          *<code>com.myjeeva.digitalocean.impl.DigitalOceanClient</code> type.          *           * Group: advanced          */
DECL|method|digitalOceanClient ( Object digitalOceanClient)
specifier|default
name|AdvancedDigitalOceanEndpointBuilder
name|digitalOceanClient
parameter_list|(
name|Object
name|digitalOceanClient
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"digitalOceanClient"
argument_list|,
name|digitalOceanClient
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use a existing configured DigitalOceanClient as client.          *           * The option will be converted to a          *<code>com.myjeeva.digitalocean.impl.DigitalOceanClient</code> type.          *           * Group: advanced          */
DECL|method|digitalOceanClient ( String digitalOceanClient)
specifier|default
name|AdvancedDigitalOceanEndpointBuilder
name|digitalOceanClient
parameter_list|(
name|String
name|digitalOceanClient
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"digitalOceanClient"
argument_list|,
name|digitalOceanClient
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous ( boolean synchronous)
specifier|default
name|AdvancedDigitalOceanEndpointBuilder
name|synchronous
parameter_list|(
name|boolean
name|synchronous
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous ( String synchronous)
specifier|default
name|AdvancedDigitalOceanEndpointBuilder
name|synchronous
parameter_list|(
name|String
name|synchronous
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Proxy enum for      *<code>org.apache.camel.component.digitalocean.constants.DigitalOceanResources</code> enum.      */
DECL|enum|DigitalOceanResources
enum|enum
name|DigitalOceanResources
block|{
DECL|enumConstant|account
name|account
block|,
DECL|enumConstant|actions
name|actions
block|,
DECL|enumConstant|blockStorages
name|blockStorages
block|,
DECL|enumConstant|droplets
name|droplets
block|,
DECL|enumConstant|images
name|images
block|,
DECL|enumConstant|snapshots
name|snapshots
block|,
DECL|enumConstant|keys
name|keys
block|,
DECL|enumConstant|regions
name|regions
block|,
DECL|enumConstant|sizes
name|sizes
block|,
DECL|enumConstant|floatingIPs
name|floatingIPs
block|,
DECL|enumConstant|tags
name|tags
block|;     }
comment|/**      * DigitalOcean (camel-digitalocean)      * The DigitalOcean component allows you to manage Droplets and resources      * within the DigitalOcean cloud.      *       * Category: cloud,management      * Available as of version: 2.19      * Maven coordinates: org.apache.camel:camel-digitalocean      *       * Syntax:<code>digitalocean:operation</code>      *       * Path parameter: operation      * The operation to perform to the given resource.      * The value can be one of: create, update, delete, list, ownList, get,      * listBackups, listActions, listNeighbors, listSnapshots, listKernels,      * listAllNeighbors, enableBackups, disableBackups, reboot, powerCycle,      * shutdown, powerOn, powerOff, restore, resetPassword, resize, rebuild,      * rename, changeKernel, enableIpv6, enablePrivateNetworking, takeSnapshot,      * transfer, convert, attach, detach, assign, unassign, tag, untag      */
DECL|method|digitalocean (String path)
specifier|default
name|DigitalOceanEndpointBuilder
name|digitalocean
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|DigitalOceanEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|DigitalOceanEndpointBuilder
implements|,
name|AdvancedDigitalOceanEndpointBuilder
block|{
specifier|public
name|DigitalOceanEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"digitalocean"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|DigitalOceanEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

