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
comment|/**  * The ldap component allows you to perform searches in LDAP servers using  * filters as the message payload.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|LdapEndpointBuilderFactory
specifier|public
interface|interface
name|LdapEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the LDAP component.      */
DECL|interface|LdapEndpointBuilder
specifier|public
interface|interface
name|LdapEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedLdapEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedLdapEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * The base DN for searches.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|base (String base)
specifier|default
name|LdapEndpointBuilder
name|base
parameter_list|(
name|String
name|base
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"base"
argument_list|,
name|base
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the producer should be started lazy (on the first message).          * By starting lazy you can use this to allow CamelContext and routes to          * startup in situations where a producer may otherwise fail during          * starting and cause the route to fail being started. By deferring this          * startup to be lazy then the startup failure can be handled during          * routing messages via Camel's routing error handlers. Beware that when          * the first message is processed then creating and starting the          * producer may take a little time and prolong the total processing time          * of the processing.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|lazyStartProducer (boolean lazyStartProducer)
specifier|default
name|LdapEndpointBuilder
name|lazyStartProducer
parameter_list|(
name|boolean
name|lazyStartProducer
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"lazyStartProducer"
argument_list|,
name|lazyStartProducer
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the producer should be started lazy (on the first message).          * By starting lazy you can use this to allow CamelContext and routes to          * startup in situations where a producer may otherwise fail during          * starting and cause the route to fail being started. By deferring this          * startup to be lazy then the startup failure can be handled during          * routing messages via Camel's routing error handlers. Beware that when          * the first message is processed then creating and starting the          * producer may take a little time and prolong the total processing time          * of the processing.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: producer          */
DECL|method|lazyStartProducer (String lazyStartProducer)
specifier|default
name|LdapEndpointBuilder
name|lazyStartProducer
parameter_list|(
name|String
name|lazyStartProducer
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"lazyStartProducer"
argument_list|,
name|lazyStartProducer
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * When specified the ldap module uses paging to retrieve all results          * (most LDAP Servers throw an exception when trying to retrieve more          * than 1000 entries in one query). To be able to use this a LdapContext          * (subclass of DirContext) has to be passed in as ldapServerBean          * (otherwise an exception is thrown).          *           * The option is a:<code>java.lang.Integer</code> type.          *           * Group: producer          */
DECL|method|pageSize (Integer pageSize)
specifier|default
name|LdapEndpointBuilder
name|pageSize
parameter_list|(
name|Integer
name|pageSize
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"pageSize"
argument_list|,
name|pageSize
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * When specified the ldap module uses paging to retrieve all results          * (most LDAP Servers throw an exception when trying to retrieve more          * than 1000 entries in one query). To be able to use this a LdapContext          * (subclass of DirContext) has to be passed in as ldapServerBean          * (otherwise an exception is thrown).          *           * The option will be converted to a<code>java.lang.Integer</code>          * type.          *           * Group: producer          */
DECL|method|pageSize (String pageSize)
specifier|default
name|LdapEndpointBuilder
name|pageSize
parameter_list|(
name|String
name|pageSize
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"pageSize"
argument_list|,
name|pageSize
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Comma-separated list of attributes that should be set in each entry          * of the result.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|returnedAttributes (String returnedAttributes)
specifier|default
name|LdapEndpointBuilder
name|returnedAttributes
parameter_list|(
name|String
name|returnedAttributes
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"returnedAttributes"
argument_list|,
name|returnedAttributes
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Specifies how deeply to search the tree of entries, starting at the          * base DN.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|scope (String scope)
specifier|default
name|LdapEndpointBuilder
name|scope
parameter_list|(
name|String
name|scope
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"scope"
argument_list|,
name|scope
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the LDAP component.      */
DECL|interface|AdvancedLdapEndpointBuilder
specifier|public
interface|interface
name|AdvancedLdapEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|LdapEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|LdapEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedLdapEndpointBuilder
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
name|AdvancedLdapEndpointBuilder
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
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous (boolean synchronous)
specifier|default
name|AdvancedLdapEndpointBuilder
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
DECL|method|synchronous (String synchronous)
specifier|default
name|AdvancedLdapEndpointBuilder
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
comment|/**      * LDAP (camel-ldap)      * The ldap component allows you to perform searches in LDAP servers using      * filters as the message payload.      *       * Category: ldap      * Since: 1.5      * Maven coordinates: org.apache.camel:camel-ldap      *       * Syntax:<code>ldap:dirContextName</code>      *       * Path parameter: dirContextName (required)      * Name of either a javax.naming.directory.DirContext, or      * java.util.Hashtable, or Map bean to lookup in the registry. If the bean      * is either a Hashtable or Map then a new javax.naming.directory.DirContext      * instance is created for each use. If the bean is a      * javax.naming.directory.DirContext then the bean is used as given. The      * latter may not be possible in all situations where the      * javax.naming.directory.DirContext must not be shared, and in those      * situations it can be better to use java.util.Hashtable or Map instead.      */
DECL|method|ldap (String path)
specifier|default
name|LdapEndpointBuilder
name|ldap
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|LdapEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|LdapEndpointBuilder
implements|,
name|AdvancedLdapEndpointBuilder
block|{
specifier|public
name|LdapEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"ldap"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|LdapEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

