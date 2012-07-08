begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.twitter.consumer.timeline
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|twitter
operator|.
name|consumer
operator|.
name|timeline
package|;
end_package

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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|twitter
operator|.
name|TwitterEndpoint
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
name|twitter
operator|.
name|consumer
operator|.
name|Twitter4JConsumer
import|;
end_import

begin_import
import|import
name|twitter4j
operator|.
name|Paging
import|;
end_import

begin_import
import|import
name|twitter4j
operator|.
name|Status
import|;
end_import

begin_import
import|import
name|twitter4j
operator|.
name|TwitterException
import|;
end_import

begin_comment
comment|/**  * Consumes the user's home timeline.  *   */
end_comment

begin_class
DECL|class|HomeConsumer
specifier|public
class|class
name|HomeConsumer
extends|extends
name|Twitter4JConsumer
block|{
DECL|field|te
name|TwitterEndpoint
name|te
decl_stmt|;
DECL|method|HomeConsumer (TwitterEndpoint te)
specifier|public
name|HomeConsumer
parameter_list|(
name|TwitterEndpoint
name|te
parameter_list|)
block|{
name|this
operator|.
name|te
operator|=
name|te
expr_stmt|;
block|}
DECL|method|pollConsume ()
specifier|public
name|List
argument_list|<
name|Status
argument_list|>
name|pollConsume
parameter_list|()
throws|throws
name|TwitterException
block|{
name|List
argument_list|<
name|Status
argument_list|>
name|list
init|=
name|te
operator|.
name|getProperties
argument_list|()
operator|.
name|getTwitter
argument_list|()
operator|.
name|getHomeTimeline
argument_list|(
operator|new
name|Paging
argument_list|(
name|lastId
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|Status
name|s
range|:
name|list
control|)
block|{
name|checkLastId
argument_list|(
name|s
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|list
return|;
block|}
DECL|method|directConsume ()
specifier|public
name|List
argument_list|<
name|Status
argument_list|>
name|directConsume
parameter_list|()
throws|throws
name|TwitterException
block|{
return|return
name|te
operator|.
name|getProperties
argument_list|()
operator|.
name|getTwitter
argument_list|()
operator|.
name|getHomeTimeline
argument_list|()
return|;
block|}
block|}
end_class

end_unit

