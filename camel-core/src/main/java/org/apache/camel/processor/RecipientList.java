begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutorService
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
name|AsyncProcessor
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
name|Expression
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
name|ProducerCache
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
name|ServiceSupport
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
name|processor
operator|.
name|aggregate
operator|.
name|AggregationStrategy
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
name|processor
operator|.
name|aggregate
operator|.
name|UseLatestAggregationStrategy
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
name|AsyncProcessorHelper
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
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
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
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|notNull
import|;
end_import

begin_comment
comment|/**  * Implements a dynamic<a  * href="http://camel.apache.org/recipient-list.html">Recipient List</a>  * pattern where the list of actual endpoints to send a message exchange to are  * dependent on some dynamic expression.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|RecipientList
specifier|public
class|class
name|RecipientList
extends|extends
name|ServiceSupport
implements|implements
name|AsyncProcessor
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|RecipientList
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|camelContext
specifier|private
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|producerCache
specifier|private
name|ProducerCache
name|producerCache
decl_stmt|;
DECL|field|expression
specifier|private
name|Expression
name|expression
decl_stmt|;
DECL|field|delimiter
specifier|private
specifier|final
name|String
name|delimiter
decl_stmt|;
DECL|field|parallelProcessing
specifier|private
name|boolean
name|parallelProcessing
decl_stmt|;
DECL|field|stopOnException
specifier|private
name|boolean
name|stopOnException
decl_stmt|;
DECL|field|ignoreInvalidEndpoints
specifier|private
name|boolean
name|ignoreInvalidEndpoints
decl_stmt|;
DECL|field|executorService
specifier|private
name|ExecutorService
name|executorService
decl_stmt|;
DECL|field|aggregationStrategy
specifier|private
name|AggregationStrategy
name|aggregationStrategy
init|=
operator|new
name|UseLatestAggregationStrategy
argument_list|()
decl_stmt|;
DECL|method|RecipientList (CamelContext camelContext)
specifier|public
name|RecipientList
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
comment|// use comma by default as delimiter
name|this
argument_list|(
name|camelContext
argument_list|,
literal|","
argument_list|)
expr_stmt|;
block|}
DECL|method|RecipientList (CamelContext camelContext, String delimiter)
specifier|public
name|RecipientList
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|delimiter
parameter_list|)
block|{
name|notNull
argument_list|(
name|camelContext
argument_list|,
literal|"camelContext"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|delimiter
argument_list|,
literal|"delimiter"
argument_list|)
expr_stmt|;
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|this
operator|.
name|delimiter
operator|=
name|delimiter
expr_stmt|;
block|}
DECL|method|RecipientList (CamelContext camelContext, Expression expression)
specifier|public
name|RecipientList
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Expression
name|expression
parameter_list|)
block|{
comment|// use comma by default as delimiter
name|this
argument_list|(
name|camelContext
argument_list|,
name|expression
argument_list|,
literal|","
argument_list|)
expr_stmt|;
block|}
DECL|method|RecipientList (CamelContext camelContext, Expression expression, String delimiter)
specifier|public
name|RecipientList
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Expression
name|expression
parameter_list|,
name|String
name|delimiter
parameter_list|)
block|{
name|notNull
argument_list|(
name|camelContext
argument_list|,
literal|"camelContext"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|expression
argument_list|,
literal|"expression"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|delimiter
argument_list|,
literal|"delimiter"
argument_list|)
expr_stmt|;
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|this
operator|.
name|expression
operator|=
name|expression
expr_stmt|;
name|this
operator|.
name|delimiter
operator|=
name|delimiter
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"RecipientList["
operator|+
operator|(
name|expression
operator|!=
literal|null
condition|?
name|expression
else|:
literal|""
operator|)
operator|+
literal|"]"
return|;
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
name|AsyncProcessorHelper
operator|.
name|process
argument_list|(
name|this
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|process (Exchange exchange, AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isStarted
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"RecipientList has not been started: "
operator|+
name|this
argument_list|)
throw|;
block|}
name|Object
name|recipientList
init|=
name|expression
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|Object
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|sendToRecipientList
argument_list|(
name|exchange
argument_list|,
name|recipientList
argument_list|,
name|callback
argument_list|)
return|;
block|}
comment|/**      * Sends the given exchange to the recipient list      */
DECL|method|sendToRecipientList (Exchange exchange, Object recipientList, AsyncCallback callback)
specifier|public
name|boolean
name|sendToRecipientList
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|recipientList
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
name|Iterator
argument_list|<
name|Object
argument_list|>
name|iter
init|=
name|ObjectHelper
operator|.
name|createIterator
argument_list|(
name|recipientList
argument_list|,
name|delimiter
argument_list|)
decl_stmt|;
name|RecipientListProcessor
name|rlp
init|=
operator|new
name|RecipientListProcessor
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|,
name|producerCache
argument_list|,
name|iter
argument_list|,
name|getAggregationStrategy
argument_list|()
argument_list|,
name|isParallelProcessing
argument_list|()
argument_list|,
name|getExecutorService
argument_list|()
argument_list|,
literal|false
argument_list|,
name|isStopOnException
argument_list|()
argument_list|)
decl_stmt|;
name|rlp
operator|.
name|setIgnoreInvalidEndpoints
argument_list|(
name|isIgnoreInvalidEndpoints
argument_list|()
argument_list|)
expr_stmt|;
comment|// now let the multicast process the exchange
return|return
name|AsyncProcessorHelper
operator|.
name|process
argument_list|(
name|rlp
argument_list|,
name|exchange
argument_list|,
name|callback
argument_list|)
return|;
block|}
DECL|method|resolveEndpoint (Exchange exchange, Object recipient)
specifier|protected
name|Endpoint
name|resolveEndpoint
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|recipient
parameter_list|)
block|{
comment|// trim strings as end users might have added spaces between separators
if|if
condition|(
name|recipient
operator|instanceof
name|String
condition|)
block|{
name|recipient
operator|=
operator|(
operator|(
name|String
operator|)
name|recipient
operator|)
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
return|return
name|ExchangeHelper
operator|.
name|resolveEndpoint
argument_list|(
name|exchange
argument_list|,
name|recipient
argument_list|)
return|;
block|}
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|producerCache
operator|==
literal|null
condition|)
block|{
name|producerCache
operator|=
operator|new
name|ProducerCache
argument_list|(
name|this
argument_list|,
name|camelContext
argument_list|)
expr_stmt|;
comment|// add it as a service so we can manage it
name|camelContext
operator|.
name|addService
argument_list|(
name|producerCache
argument_list|)
expr_stmt|;
block|}
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|producerCache
argument_list|)
expr_stmt|;
block|}
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|producerCache
argument_list|)
expr_stmt|;
block|}
DECL|method|isIgnoreInvalidEndpoints ()
specifier|public
name|boolean
name|isIgnoreInvalidEndpoints
parameter_list|()
block|{
return|return
name|ignoreInvalidEndpoints
return|;
block|}
DECL|method|setIgnoreInvalidEndpoints (boolean ignoreInvalidEndpoints)
specifier|public
name|void
name|setIgnoreInvalidEndpoints
parameter_list|(
name|boolean
name|ignoreInvalidEndpoints
parameter_list|)
block|{
name|this
operator|.
name|ignoreInvalidEndpoints
operator|=
name|ignoreInvalidEndpoints
expr_stmt|;
block|}
DECL|method|isParallelProcessing ()
specifier|public
name|boolean
name|isParallelProcessing
parameter_list|()
block|{
return|return
name|parallelProcessing
return|;
block|}
DECL|method|setParallelProcessing (boolean parallelProcessing)
specifier|public
name|void
name|setParallelProcessing
parameter_list|(
name|boolean
name|parallelProcessing
parameter_list|)
block|{
name|this
operator|.
name|parallelProcessing
operator|=
name|parallelProcessing
expr_stmt|;
block|}
DECL|method|isStopOnException ()
specifier|public
name|boolean
name|isStopOnException
parameter_list|()
block|{
return|return
name|stopOnException
return|;
block|}
DECL|method|setStopOnException (boolean stopOnException)
specifier|public
name|void
name|setStopOnException
parameter_list|(
name|boolean
name|stopOnException
parameter_list|)
block|{
name|this
operator|.
name|stopOnException
operator|=
name|stopOnException
expr_stmt|;
block|}
DECL|method|getExecutorService ()
specifier|public
name|ExecutorService
name|getExecutorService
parameter_list|()
block|{
return|return
name|executorService
return|;
block|}
DECL|method|setExecutorService (ExecutorService executorService)
specifier|public
name|void
name|setExecutorService
parameter_list|(
name|ExecutorService
name|executorService
parameter_list|)
block|{
name|this
operator|.
name|executorService
operator|=
name|executorService
expr_stmt|;
block|}
DECL|method|getAggregationStrategy ()
specifier|public
name|AggregationStrategy
name|getAggregationStrategy
parameter_list|()
block|{
return|return
name|aggregationStrategy
return|;
block|}
DECL|method|setAggregationStrategy (AggregationStrategy aggregationStrategy)
specifier|public
name|void
name|setAggregationStrategy
parameter_list|(
name|AggregationStrategy
name|aggregationStrategy
parameter_list|)
block|{
name|this
operator|.
name|aggregationStrategy
operator|=
name|aggregationStrategy
expr_stmt|;
block|}
block|}
end_class

end_unit

