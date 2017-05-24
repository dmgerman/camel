begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.twitter.consumer
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
name|Exchange
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
name|Paging
import|;
end_import

begin_import
import|import
name|twitter4j
operator|.
name|Twitter
import|;
end_import

begin_import
import|import
name|twitter4j
operator|.
name|TwitterException
import|;
end_import

begin_class
DECL|class|AbstractTwitterConsumerHandler
specifier|public
specifier|abstract
class|class
name|AbstractTwitterConsumerHandler
block|{
comment|/**      * Instance of TwitterEndpoint.      */
DECL|field|endpoint
specifier|protected
specifier|final
name|TwitterEndpoint
name|endpoint
decl_stmt|;
comment|/**      * The last tweet ID received.      */
DECL|field|lastId
specifier|private
name|long
name|lastId
decl_stmt|;
DECL|method|AbstractTwitterConsumerHandler (TwitterEndpoint endpoint)
specifier|protected
name|AbstractTwitterConsumerHandler
parameter_list|(
name|TwitterEndpoint
name|endpoint
parameter_list|)
block|{
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|lastId
operator|=
operator|-
literal|1
expr_stmt|;
block|}
comment|/**      * Called by polling consumers during each poll.  It needs to be separate      * from directConsume() since, as an example, streaming API polling allows      * tweets to build up between polls.      */
DECL|method|pollConsume ()
specifier|public
specifier|abstract
name|List
argument_list|<
name|Exchange
argument_list|>
name|pollConsume
parameter_list|()
throws|throws
name|TwitterException
function_decl|;
comment|/**      * Called by direct consumers.      */
DECL|method|directConsume ()
specifier|public
specifier|abstract
name|List
argument_list|<
name|Exchange
argument_list|>
name|directConsume
parameter_list|()
throws|throws
name|TwitterException
function_decl|;
comment|/**      * Can't assume that the end of the list will be the most recent ID.      * The Twitter API sometimes returns them slightly out of order.      */
DECL|method|setLastIdIfGreater (long newId)
specifier|protected
name|void
name|setLastIdIfGreater
parameter_list|(
name|long
name|newId
parameter_list|)
block|{
if|if
condition|(
name|newId
operator|>
name|lastId
condition|)
block|{
name|lastId
operator|=
name|newId
expr_stmt|;
block|}
block|}
comment|/**      * Support to update the Consumer's lastId when starting the consumer      */
DECL|method|setLastId (long sinceId)
specifier|public
name|void
name|setLastId
parameter_list|(
name|long
name|sinceId
parameter_list|)
block|{
name|lastId
operator|=
name|sinceId
expr_stmt|;
block|}
DECL|method|getTwitter ()
specifier|protected
name|Twitter
name|getTwitter
parameter_list|()
block|{
return|return
name|endpoint
operator|.
name|getProperties
argument_list|()
operator|.
name|getTwitter
argument_list|()
return|;
block|}
DECL|method|getLastId ()
specifier|protected
name|long
name|getLastId
parameter_list|()
block|{
return|return
name|lastId
return|;
block|}
DECL|method|getLastIdPaging ()
specifier|protected
name|Paging
name|getLastIdPaging
parameter_list|()
block|{
return|return
operator|new
name|Paging
argument_list|(
name|lastId
argument_list|)
return|;
block|}
block|}
end_class

end_unit

