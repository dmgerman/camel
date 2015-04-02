begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.rx
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|rx
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
name|CamelContext
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
name|Message
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
name|Producer
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
name|ProducerTemplate
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
name|ServiceHelper
import|;
end_import

begin_import
import|import
name|rx
operator|.
name|Observable
import|;
end_import

begin_import
import|import
name|rx
operator|.
name|Subscriber
import|;
end_import

begin_class
DECL|class|CamelOperator
specifier|public
class|class
name|CamelOperator
implements|implements
name|Observable
operator|.
name|Operator
argument_list|<
name|Message
argument_list|,
name|Message
argument_list|>
block|{
DECL|field|producerTemplate
specifier|private
name|ProducerTemplate
name|producerTemplate
decl_stmt|;
DECL|field|endpoint
specifier|private
name|Endpoint
name|endpoint
decl_stmt|;
DECL|method|CamelOperator (CamelContext context, String uri)
specifier|public
name|CamelOperator
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|String
name|uri
parameter_list|)
throws|throws
name|Exception
block|{
name|producerTemplate
operator|=
name|context
operator|.
name|createProducerTemplate
argument_list|()
expr_stmt|;
name|endpoint
operator|=
name|context
operator|.
name|getEndpoint
argument_list|(
name|uri
argument_list|)
expr_stmt|;
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|producerTemplate
argument_list|)
expr_stmt|;
block|}
DECL|method|CamelOperator (Endpoint endpoint)
specifier|public
name|CamelOperator
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
throws|throws
name|Exception
block|{
name|this
operator|.
name|producerTemplate
operator|=
name|endpoint
operator|.
name|getCamelContext
argument_list|()
operator|.
name|createProducerTemplate
argument_list|()
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|producerTemplate
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|call (final Subscriber<? super Message> s)
specifier|public
name|Subscriber
argument_list|<
name|?
super|super
name|Message
argument_list|>
name|call
parameter_list|(
specifier|final
name|Subscriber
argument_list|<
name|?
super|super
name|Message
argument_list|>
name|s
parameter_list|)
block|{
return|return
operator|new
name|Subscriber
argument_list|<
name|Message
argument_list|>
argument_list|(
name|s
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|void
name|onCompleted
parameter_list|()
block|{
try|try
block|{
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|producerTemplate
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelRxException
argument_list|(
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|producerTemplate
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|s
operator|.
name|isUnsubscribed
argument_list|()
condition|)
block|{
name|s
operator|.
name|onCompleted
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|onError
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|// producer cannot handler the exception
comment|// so we just pass the exchange to the subscriber
if|if
condition|(
operator|!
name|s
operator|.
name|isUnsubscribed
argument_list|()
condition|)
block|{
name|s
operator|.
name|onError
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|onNext
parameter_list|(
name|Message
name|item
parameter_list|)
block|{
if|if
condition|(
operator|!
name|s
operator|.
name|isUnsubscribed
argument_list|()
condition|)
block|{
name|Exchange
name|exchange
init|=
name|process
argument_list|(
name|item
argument_list|)
decl_stmt|;
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|s
operator|.
name|onError
argument_list|(
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|exchange
operator|.
name|hasOut
argument_list|()
condition|)
block|{
name|s
operator|.
name|onNext
argument_list|(
name|exchange
operator|.
name|getOut
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|s
operator|.
name|onNext
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
return|;
block|}
DECL|method|process (Exchange exchange)
specifier|private
name|Exchange
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
try|try
block|{
name|exchange
operator|=
name|producerTemplate
operator|.
name|send
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
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
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|exchange
return|;
block|}
DECL|method|process (Message message)
specifier|private
name|Exchange
name|process
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setIn
argument_list|(
name|message
argument_list|)
expr_stmt|;
return|return
name|process
argument_list|(
name|exchange
argument_list|)
return|;
block|}
block|}
end_class

end_unit

