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
comment|/**  * Consumes tweets in which the user has been mentioned.  */
end_comment

begin_class
DECL|class|MentionsConsumer
specifier|public
class|class
name|MentionsConsumer
extends|extends
name|AbstractStatusConsumer
block|{
DECL|method|MentionsConsumer (TwitterEndpoint endpoint)
specifier|public
name|MentionsConsumer
parameter_list|(
name|TwitterEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doPoll ()
specifier|protected
name|List
argument_list|<
name|Status
argument_list|>
name|doPoll
parameter_list|()
throws|throws
name|TwitterException
block|{
return|return
name|getTwitter
argument_list|()
operator|.
name|getMentionsTimeline
argument_list|(
name|getLastIdPaging
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|doDirect ()
specifier|protected
name|List
argument_list|<
name|Status
argument_list|>
name|doDirect
parameter_list|()
throws|throws
name|TwitterException
block|{
return|return
name|getTwitter
argument_list|()
operator|.
name|getMentionsTimeline
argument_list|()
return|;
block|}
block|}
end_class

end_unit

