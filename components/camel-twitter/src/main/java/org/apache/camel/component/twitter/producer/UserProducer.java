begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.twitter.producer
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
name|producer
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
name|Status
import|;
end_import

begin_import
import|import
name|twitter4j
operator|.
name|StatusUpdate
import|;
end_import

begin_comment
comment|/**  * Produces text as a status update.  */
end_comment

begin_class
DECL|class|UserProducer
specifier|public
class|class
name|UserProducer
extends|extends
name|TwitterProducer
block|{
DECL|method|UserProducer (TwitterEndpoint endpoint)
specifier|public
name|UserProducer
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
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
comment|// update user's status
name|Object
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|Status
name|response
decl_stmt|;
if|if
condition|(
name|in
operator|instanceof
name|StatusUpdate
condition|)
block|{
name|response
operator|=
name|updateStatus
argument_list|(
operator|(
name|StatusUpdate
operator|)
name|in
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|s
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMandatoryBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|response
operator|=
name|updateStatus
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
comment|/*          * Support the InOut exchange pattern in order to provide access to          * the unique identifier for the published tweet which is returned in the response          * by the Twitter REST API: https://dev.twitter.com/docs/api/1/post/statuses/update          */
if|if
condition|(
name|exchange
operator|.
name|getPattern
argument_list|()
operator|.
name|isOutCapable
argument_list|()
condition|)
block|{
comment|// here we just copy the header of in message to the out message
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|copyFrom
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|response
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|updateStatus (StatusUpdate status)
specifier|private
name|Status
name|updateStatus
parameter_list|(
name|StatusUpdate
name|status
parameter_list|)
throws|throws
name|Exception
block|{
name|Status
name|response
init|=
name|endpoint
operator|.
name|getProperties
argument_list|()
operator|.
name|getTwitter
argument_list|()
operator|.
name|updateStatus
argument_list|(
name|status
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Updated status: {}"
argument_list|,
name|status
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Status id: {}"
argument_list|,
name|response
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|response
return|;
block|}
DECL|method|updateStatus (String status)
specifier|private
name|Status
name|updateStatus
parameter_list|(
name|String
name|status
parameter_list|)
throws|throws
name|Exception
block|{
name|Status
name|response
init|=
name|endpoint
operator|.
name|getProperties
argument_list|()
operator|.
name|getTwitter
argument_list|()
operator|.
name|updateStatus
argument_list|(
name|status
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Updated status: {}"
argument_list|,
name|status
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Status id: {}"
argument_list|,
name|response
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|response
return|;
block|}
block|}
end_class

end_unit

