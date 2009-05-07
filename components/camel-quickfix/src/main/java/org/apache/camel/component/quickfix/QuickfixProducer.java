begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.quickfix
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|quickfix
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|InvalidPayloadException
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
name|ExchangeHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|log4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|Message
import|;
end_import

begin_comment
comment|/**  * QuickfixProducer is intended to be used as an initiator instance.  *<p/>  * The initiator will send the FIX messages to the configured acceptors via session object.  *  * @author Anton Arhipov  */
end_comment

begin_class
DECL|class|QuickfixProducer
specifier|public
class|class
name|QuickfixProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|Logger
operator|.
name|getLogger
argument_list|(
name|QuickfixProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|private
name|QuickfixEndpoint
name|endpoint
decl_stmt|;
DECL|method|QuickfixProducer (QuickfixEndpoint endpoint)
specifier|public
name|QuickfixProducer
parameter_list|(
name|QuickfixEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
comment|/**      * send the exchange further to acceptor side      *       * @param exchange the normalized message      * @throws Exception      */
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
name|Message
name|message
init|=
name|toQuickMessage
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Sending FIX message : "
operator|+
name|message
argument_list|)
expr_stmt|;
block|}
name|endpoint
operator|.
name|getSession
argument_list|()
operator|.
name|send
argument_list|(
name|message
argument_list|)
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Sent FIX message : "
operator|+
name|message
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * invokes the converter logic      *       * @param exchange the exchange      * @return quickfixj's message      * @throws IOException      * @throws InvalidPayloadException      */
DECL|method|toQuickMessage (Exchange exchange)
specifier|protected
name|Message
name|toQuickMessage
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|InvalidPayloadException
throws|,
name|IOException
block|{
return|return
name|ExchangeHelper
operator|.
name|getMandatoryInBody
argument_list|(
name|exchange
argument_list|,
name|Message
operator|.
name|class
argument_list|)
return|;
block|}
block|}
end_class

end_unit

