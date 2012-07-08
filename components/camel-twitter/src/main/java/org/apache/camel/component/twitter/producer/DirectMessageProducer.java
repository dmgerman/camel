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
name|CamelExchangeException
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
name|Processor
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
name|impl
operator|.
name|DefaultProducer
import|;
end_import

begin_comment
comment|/**  * Produces text as a direct message.  *   */
end_comment

begin_class
DECL|class|DirectMessageProducer
specifier|public
class|class
name|DirectMessageProducer
extends|extends
name|DefaultProducer
implements|implements
name|Processor
block|{
DECL|field|te
specifier|private
name|TwitterEndpoint
name|te
decl_stmt|;
DECL|method|DirectMessageProducer (TwitterEndpoint te)
specifier|public
name|DirectMessageProducer
parameter_list|(
name|TwitterEndpoint
name|te
parameter_list|)
block|{
name|super
argument_list|(
name|te
argument_list|)
expr_stmt|;
name|this
operator|.
name|te
operator|=
name|te
expr_stmt|;
block|}
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
comment|// send direct message
name|String
name|toUsername
init|=
name|te
operator|.
name|getProperties
argument_list|()
operator|.
name|getUser
argument_list|()
decl_stmt|;
name|String
name|text
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|toUsername
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"Username not configured on TwitterEndpoint"
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
else|else
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Sending to: {} message: {}"
argument_list|,
name|toUsername
argument_list|,
name|text
argument_list|)
expr_stmt|;
name|te
operator|.
name|getProperties
argument_list|()
operator|.
name|getTwitter
argument_list|()
operator|.
name|sendDirectMessage
argument_list|(
name|toUsername
argument_list|,
name|text
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

