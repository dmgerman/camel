begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|net
operator|.
name|URI
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
name|util
operator|.
name|URISupport
import|;
end_import

begin_comment
comment|/**  * An<a href="http://camel.apache.org/rss.html">RSS Component</a>.  *<p/>  * Camel uses<a href="https://rome.dev.java.net/">ROME</a> as the RSS implementation.    */
end_comment

begin_class
DECL|class|RssComponent
specifier|public
class|class
name|RssComponent
extends|extends
name|FeedComponent
block|{
DECL|method|createEndpoint (String uri, String remaining, Map parameters)
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
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
comment|// Parameters should be kept in the remaining path, since they might be needed to get the actual RSS feed
name|URI
name|remainingUri
init|=
name|URISupport
operator|.
name|createRemainingURI
argument_list|(
operator|new
name|URI
argument_list|(
name|remaining
argument_list|)
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
if|if
condition|(
name|remainingUri
operator|.
name|getScheme
argument_list|()
operator|.
name|equals
argument_list|(
literal|"http"
argument_list|)
operator|||
name|remainingUri
operator|.
name|getScheme
argument_list|()
operator|.
name|equals
argument_list|(
literal|"https"
argument_list|)
condition|)
block|{
return|return
operator|new
name|RssEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|remainingUri
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
return|return
operator|new
name|RssEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|remaining
argument_list|)
return|;
block|}
block|}
end_class

end_unit

