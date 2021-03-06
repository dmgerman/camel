begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.rss
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|rss
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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
name|org
operator|.
name|apache
operator|.
name|camel
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
name|camel
operator|.
name|component
operator|.
name|feed
operator|.
name|FeedComponent
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
name|feed
operator|.
name|FeedEndpoint
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
name|annotations
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
name|util
operator|.
name|URISupport
import|;
end_import

begin_comment
comment|/**  * An<a href="http://camel.apache.org/rss.html">RSS Component</a>.  *<p/>  * Camel uses<a href="https://rome.dev.java.net/">ROME</a> as the RSS implementation.    */
end_comment

begin_class
annotation|@
name|Component
argument_list|(
literal|"rss"
argument_list|)
DECL|class|RssComponent
specifier|public
class|class
name|RssComponent
extends|extends
name|FeedComponent
block|{
DECL|method|RssComponent ()
specifier|public
name|RssComponent
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|FeedEndpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|RssEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
literal|null
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|afterConfiguration (String uri, String remaining, Endpoint endpoint, Map<String, Object> parameters)
specifier|protected
name|void
name|afterConfiguration
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|RssEndpoint
name|rss
init|=
operator|(
name|RssEndpoint
operator|)
name|endpoint
decl_stmt|;
if|if
condition|(
name|rss
operator|.
name|getFeedUri
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// already set so do not change it
return|return;
block|}
comment|// recreate feed uri after we have configured the endpoint so we can use the left over parameters
comment|// for the http feed
name|String
name|feedUri
decl_stmt|;
if|if
condition|(
operator|!
name|parameters
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|options
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|(
name|parameters
argument_list|)
decl_stmt|;
name|String
name|query
init|=
name|URISupport
operator|.
name|createQueryString
argument_list|(
name|options
argument_list|)
decl_stmt|;
name|feedUri
operator|=
name|remaining
operator|+
literal|"?"
operator|+
name|query
expr_stmt|;
block|}
else|else
block|{
name|feedUri
operator|=
name|remaining
expr_stmt|;
block|}
name|rss
operator|.
name|setFeedUri
argument_list|(
name|feedUri
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

