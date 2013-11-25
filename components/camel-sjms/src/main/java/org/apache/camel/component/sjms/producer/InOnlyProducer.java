begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sjms.producer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sjms
operator|.
name|producer
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

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
name|javax
operator|.
name|jms
operator|.
name|Connection
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|MessageProducer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Session
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
name|AsyncCallback
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
name|sjms
operator|.
name|BatchMessage
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
name|sjms
operator|.
name|SjmsEndpoint
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
name|sjms
operator|.
name|SjmsProducer
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
name|sjms
operator|.
name|TransactionCommitStrategy
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
name|sjms
operator|.
name|jms
operator|.
name|JmsMessageHelper
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
name|sjms
operator|.
name|jms
operator|.
name|JmsObjectFactory
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
name|sjms
operator|.
name|tx
operator|.
name|DefaultTransactionCommitStrategy
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
name|sjms
operator|.
name|tx
operator|.
name|SessionTransactionSynchronization
import|;
end_import

begin_comment
comment|/**  * A Camel Producer that provides the InOnly Exchange pattern..  */
end_comment

begin_class
DECL|class|InOnlyProducer
specifier|public
class|class
name|InOnlyProducer
extends|extends
name|SjmsProducer
block|{
DECL|method|InOnlyProducer (SjmsEndpoint endpoint)
specifier|public
name|InOnlyProducer
parameter_list|(
name|SjmsEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
comment|/*      * @see org.apache.camel.component.sjms.SjmsProducer#doCreateProducerModel()      * @return      * @throws Exception      */
annotation|@
name|Override
DECL|method|doCreateProducerModel ()
specifier|public
name|MessageProducerResources
name|doCreateProducerModel
parameter_list|()
throws|throws
name|Exception
block|{
name|MessageProducerResources
name|answer
init|=
literal|null
decl_stmt|;
name|Connection
name|conn
init|=
literal|null
decl_stmt|;
try|try
block|{
name|conn
operator|=
name|getConnectionResource
argument_list|()
operator|.
name|borrowConnection
argument_list|()
expr_stmt|;
name|TransactionCommitStrategy
name|commitStrategy
init|=
literal|null
decl_stmt|;
name|Session
name|session
init|=
literal|null
decl_stmt|;
name|MessageProducer
name|messageProducer
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|isEndpointTransacted
argument_list|()
condition|)
block|{
if|if
condition|(
name|getCommitStrategy
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|commitStrategy
operator|=
name|getCommitStrategy
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|commitStrategy
operator|=
operator|new
name|DefaultTransactionCommitStrategy
argument_list|()
expr_stmt|;
block|}
name|session
operator|=
name|conn
operator|.
name|createSession
argument_list|(
literal|true
argument_list|,
name|getAcknowledgeMode
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|session
operator|=
name|conn
operator|.
name|createSession
argument_list|(
literal|false
argument_list|,
name|getAcknowledgeMode
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isTopic
argument_list|()
condition|)
block|{
name|messageProducer
operator|=
name|JmsObjectFactory
operator|.
name|createMessageProducer
argument_list|(
name|session
argument_list|,
name|getDestinationName
argument_list|()
argument_list|,
name|isTopic
argument_list|()
argument_list|,
name|isPersistent
argument_list|()
argument_list|,
name|getTtl
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|messageProducer
operator|=
name|JmsObjectFactory
operator|.
name|createQueueProducer
argument_list|(
name|session
argument_list|,
name|getDestinationName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|answer
operator|=
operator|new
name|MessageProducerResources
argument_list|(
name|session
argument_list|,
name|messageProducer
argument_list|,
name|commitStrategy
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Unable to create the MessageProducer: "
operator|+
name|e
operator|.
name|getLocalizedMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|conn
operator|!=
literal|null
condition|)
block|{
name|getConnectionResource
argument_list|()
operator|.
name|returnConnection
argument_list|(
name|conn
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
comment|/*      * @see      * org.apache.camel.component.sjms.SjmsProducer#sendMessage(org.apache.camel.Exchange, org.apache.camel.AsyncCallback)      * @param exchange      * @param callback      * @throws Exception      */
annotation|@
name|Override
DECL|method|sendMessage (Exchange exchange, AsyncCallback callback)
specifier|public
name|void
name|sendMessage
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Message
argument_list|>
name|messages
init|=
operator|new
name|ArrayList
argument_list|<
name|Message
argument_list|>
argument_list|()
decl_stmt|;
name|MessageProducerResources
name|producer
init|=
name|getProducers
argument_list|()
operator|.
name|borrowObject
argument_list|()
decl_stmt|;
try|try
block|{
if|if
condition|(
name|getProducers
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|instanceof
name|List
condition|)
block|{
name|List
argument_list|<
name|?
argument_list|>
name|payload
init|=
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|object
range|:
name|payload
control|)
block|{
name|Message
name|message
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|BatchMessage
operator|.
name|class
operator|.
name|isInstance
argument_list|(
name|object
argument_list|)
condition|)
block|{
name|BatchMessage
argument_list|<
name|?
argument_list|>
name|batchMessage
init|=
operator|(
name|BatchMessage
argument_list|<
name|?
argument_list|>
operator|)
name|object
decl_stmt|;
name|message
operator|=
name|JmsMessageHelper
operator|.
name|createMessage
argument_list|(
name|producer
operator|.
name|getSession
argument_list|()
argument_list|,
name|batchMessage
operator|.
name|getPayload
argument_list|()
argument_list|,
name|batchMessage
operator|.
name|getHeaders
argument_list|()
argument_list|,
name|getSjmsEndpoint
argument_list|()
operator|.
name|getJmsKeyFormatStrategy
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|message
operator|=
name|JmsMessageHelper
operator|.
name|createMessage
argument_list|(
name|producer
operator|.
name|getSession
argument_list|()
argument_list|,
name|object
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|,
name|getSjmsEndpoint
argument_list|()
operator|.
name|getJmsKeyFormatStrategy
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|messages
operator|.
name|add
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|Object
name|payload
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|Message
name|message
init|=
name|JmsMessageHelper
operator|.
name|createMessage
argument_list|(
name|producer
operator|.
name|getSession
argument_list|()
argument_list|,
name|payload
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|,
name|getSjmsEndpoint
argument_list|()
operator|.
name|getJmsKeyFormatStrategy
argument_list|()
argument_list|)
decl_stmt|;
name|messages
operator|.
name|add
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|isEndpointTransacted
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
operator|.
name|addSynchronization
argument_list|(
operator|new
name|SessionTransactionSynchronization
argument_list|(
name|producer
operator|.
name|getSession
argument_list|()
argument_list|,
name|producer
operator|.
name|getCommitStrategy
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Message
name|message
range|:
name|messages
control|)
block|{
name|producer
operator|.
name|getMessageProducer
argument_list|()
operator|.
name|send
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|Exception
argument_list|(
literal|"Unable to complete sending the message: "
operator|+
name|e
operator|.
name|getLocalizedMessage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|getProducers
argument_list|()
operator|.
name|returnObject
argument_list|(
name|producer
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
name|isSynchronous
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

