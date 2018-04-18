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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_class
DECL|class|RssEndpointTest
specifier|public
class|class
name|RssEndpointTest
extends|extends
name|RssPollingConsumerTest
block|{
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|RssEndpoint
name|rss
init|=
operator|new
name|RssEndpoint
argument_list|()
decl_stmt|;
name|rss
operator|.
name|setCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|rss
operator|.
name|setFeedUri
argument_list|(
literal|"file:src/test/data/rss20.xml"
argument_list|)
expr_stmt|;
name|rss
operator|.
name|setSplitEntries
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"delay"
argument_list|,
literal|100
argument_list|)
expr_stmt|;
name|rss
operator|.
name|setConsumerProperties
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|context
operator|.
name|addEndpoint
argument_list|(
literal|"myrss"
argument_list|,
name|rss
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"myrss"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

