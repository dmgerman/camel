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
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

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
comment|/**  * The rss component is used for consuming RSS feeds.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|RssEndpointBuilderFactory
specifier|public
interface|interface
name|RssEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the RSS component.      */
DECL|interface|RssEndpointBuilder
specifier|public
specifier|static
interface|interface
name|RssEndpointBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|advanced ()
specifier|public
specifier|default
name|AdvancedRssEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedRssEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * The URI to the feed to poll.          * The option is a<code>java.lang.String</code> type.          * @group consumer          */
DECL|method|feedUri (String feedUri)
specifier|public
specifier|default
name|RssEndpointBuilder
name|feedUri
parameter_list|(
name|String
name|feedUri
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"feedUri"
argument_list|,
name|feedUri
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether to add the feed object as a header.          * The option is a<code>boolean</code> type.          * @group consumer          */
DECL|method|feedHeader (boolean feedHeader)
specifier|public
specifier|default
name|RssEndpointBuilder
name|feedHeader
parameter_list|(
name|boolean
name|feedHeader
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"feedHeader"
argument_list|,
name|feedHeader
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether to add the feed object as a header.          * The option will be converted to a<code>boolean</code> type.          * @group consumer          */
DECL|method|feedHeader (String feedHeader)
specifier|public
specifier|default
name|RssEndpointBuilder
name|feedHeader
parameter_list|(
name|String
name|feedHeader
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"feedHeader"
argument_list|,
name|feedHeader
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether to use filtering or not of the entries.          * The option is a<code>boolean</code> type.          * @group consumer          */
DECL|method|filter (boolean filter)
specifier|public
specifier|default
name|RssEndpointBuilder
name|filter
parameter_list|(
name|boolean
name|filter
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"filter"
argument_list|,
name|filter
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether to use filtering or not of the entries.          * The option will be converted to a<code>boolean</code> type.          * @group consumer          */
DECL|method|filter (String filter)
specifier|public
specifier|default
name|RssEndpointBuilder
name|filter
parameter_list|(
name|String
name|filter
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"filter"
argument_list|,
name|filter
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the timestamp to be used for filtering entries from the atom          * feeds. This options is only in conjunction with the splitEntries.          * The option is a<code>java.util.Date</code> type.          * @group consumer          */
DECL|method|lastUpdate (Date lastUpdate)
specifier|public
specifier|default
name|RssEndpointBuilder
name|lastUpdate
parameter_list|(
name|Date
name|lastUpdate
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"lastUpdate"
argument_list|,
name|lastUpdate
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the timestamp to be used for filtering entries from the atom          * feeds. This options is only in conjunction with the splitEntries.          * The option will be converted to a<code>java.util.Date</code> type.          * @group consumer          */
DECL|method|lastUpdate (String lastUpdate)
specifier|public
specifier|default
name|RssEndpointBuilder
name|lastUpdate
parameter_list|(
name|String
name|lastUpdate
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"lastUpdate"
argument_list|,
name|lastUpdate
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the password to be used for basic authentication when polling          * from a HTTP feed.          * The option is a<code>java.lang.String</code> type.          * @group consumer          */
DECL|method|password (String password)
specifier|public
specifier|default
name|RssEndpointBuilder
name|password
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"password"
argument_list|,
name|password
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether to sort entries by published date. Only works when          * splitEntries = true.          * The option is a<code>boolean</code> type.          * @group consumer          */
DECL|method|sortEntries (boolean sortEntries)
specifier|public
specifier|default
name|RssEndpointBuilder
name|sortEntries
parameter_list|(
name|boolean
name|sortEntries
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"sortEntries"
argument_list|,
name|sortEntries
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether to sort entries by published date. Only works when          * splitEntries = true.          * The option will be converted to a<code>boolean</code> type.          * @group consumer          */
DECL|method|sortEntries (String sortEntries)
specifier|public
specifier|default
name|RssEndpointBuilder
name|sortEntries
parameter_list|(
name|String
name|sortEntries
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"sortEntries"
argument_list|,
name|sortEntries
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether or not entries should be sent individually or whether          * the entire feed should be sent as a single message.          * The option is a<code>boolean</code> type.          * @group consumer          */
DECL|method|splitEntries (boolean splitEntries)
specifier|public
specifier|default
name|RssEndpointBuilder
name|splitEntries
parameter_list|(
name|boolean
name|splitEntries
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"splitEntries"
argument_list|,
name|splitEntries
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether or not entries should be sent individually or whether          * the entire feed should be sent as a single message.          * The option will be converted to a<code>boolean</code> type.          * @group consumer          */
DECL|method|splitEntries (String splitEntries)
specifier|public
specifier|default
name|RssEndpointBuilder
name|splitEntries
parameter_list|(
name|String
name|splitEntries
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"splitEntries"
argument_list|,
name|splitEntries
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether all entries identified in a single feed poll should be          * delivered immediately. If true, only one entry is processed per          * consumer.delay. Only applicable when splitEntries = true.          * The option is a<code>boolean</code> type.          * @group consumer          */
DECL|method|throttleEntries ( boolean throttleEntries)
specifier|public
specifier|default
name|RssEndpointBuilder
name|throttleEntries
parameter_list|(
name|boolean
name|throttleEntries
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"throttleEntries"
argument_list|,
name|throttleEntries
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether all entries identified in a single feed poll should be          * delivered immediately. If true, only one entry is processed per          * consumer.delay. Only applicable when splitEntries = true.          * The option will be converted to a<code>boolean</code> type.          * @group consumer          */
DECL|method|throttleEntries (String throttleEntries)
specifier|public
specifier|default
name|RssEndpointBuilder
name|throttleEntries
parameter_list|(
name|String
name|throttleEntries
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"throttleEntries"
argument_list|,
name|throttleEntries
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the username to be used for basic authentication when polling          * from a HTTP feed.          * The option is a<code>java.lang.String</code> type.          * @group consumer          */
DECL|method|username (String username)
specifier|public
specifier|default
name|RssEndpointBuilder
name|username
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"username"
argument_list|,
name|username
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the RSS component.      */
DECL|interface|AdvancedRssEndpointBuilder
specifier|public
specifier|static
interface|interface
name|AdvancedRssEndpointBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|basic ()
specifier|public
specifier|default
name|RssEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|RssEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|public
specifier|default
name|AdvancedRssEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|setProperty
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
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option will be converted to a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( String basicPropertyBinding)
specifier|public
specifier|default
name|AdvancedRssEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|String
name|basicPropertyBinding
parameter_list|)
block|{
name|setProperty
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
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|synchronous ( boolean synchronous)
specifier|public
specifier|default
name|AdvancedRssEndpointBuilder
name|synchronous
parameter_list|(
name|boolean
name|synchronous
parameter_list|)
block|{
name|setProperty
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
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          * The option will be converted to a<code>boolean</code> type.          * @group advanced          */
DECL|method|synchronous (String synchronous)
specifier|public
specifier|default
name|AdvancedRssEndpointBuilder
name|synchronous
parameter_list|(
name|String
name|synchronous
parameter_list|)
block|{
name|setProperty
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
comment|/**      * The rss component is used for consuming RSS feeds. Creates a builder to      * build endpoints for the RSS component.      */
DECL|method|rss (String path)
specifier|public
specifier|default
name|RssEndpointBuilder
name|rss
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|RssEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|RssEndpointBuilder
implements|,
name|AdvancedRssEndpointBuilder
block|{
specifier|public
name|RssEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"rss"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|RssEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

