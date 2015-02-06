begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.dataset
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|dataset
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicInteger
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
name|Component
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
name|Consumer
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
name|Service
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
name|mock
operator|.
name|MockEndpoint
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
name|ThroughputLogger
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
name|spi
operator|.
name|UriEndpoint
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
name|spi
operator|.
name|UriParam
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
name|spi
operator|.
name|UriPath
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
name|CamelLogger
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
name|URISupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Endpoint for DataSet.  *  * @version   */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"dataset"
argument_list|,
name|consumerClass
operator|=
name|DataSetConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"core,testing"
argument_list|)
DECL|class|DataSetEndpoint
specifier|public
class|class
name|DataSetEndpoint
extends|extends
name|MockEndpoint
implements|implements
name|Service
block|{
DECL|field|log
specifier|private
specifier|final
specifier|transient
name|Logger
name|log
decl_stmt|;
DECL|field|receivedCounter
specifier|private
specifier|final
name|AtomicInteger
name|receivedCounter
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|name
operator|=
literal|"name"
argument_list|,
name|description
operator|=
literal|"name of DataSet to lookup in the registry"
argument_list|)
DECL|field|dataSet
specifier|private
specifier|volatile
name|DataSet
name|dataSet
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"0"
argument_list|)
DECL|field|minRate
specifier|private
name|int
name|minRate
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"3"
argument_list|)
DECL|field|produceDelay
specifier|private
name|long
name|produceDelay
init|=
literal|3
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"0"
argument_list|)
DECL|field|consumeDelay
specifier|private
name|long
name|consumeDelay
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"0"
argument_list|)
DECL|field|preloadSize
specifier|private
name|long
name|preloadSize
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"1000"
argument_list|)
DECL|field|initialDelay
specifier|private
name|long
name|initialDelay
init|=
literal|1000
decl_stmt|;
annotation|@
name|Deprecated
DECL|method|DataSetEndpoint ()
specifier|public
name|DataSetEndpoint
parameter_list|()
block|{
name|this
operator|.
name|log
operator|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|DataSetEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// optimize as we dont need to copy the exchange
name|setCopyOnExchange
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|DataSetEndpoint (String endpointUri, Component component, DataSet dataSet)
specifier|public
name|DataSetEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|,
name|DataSet
name|dataSet
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|dataSet
operator|=
name|dataSet
expr_stmt|;
name|this
operator|.
name|log
operator|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|endpointUri
argument_list|)
expr_stmt|;
comment|// optimize as we dont need to copy the exchange
name|setCopyOnExchange
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|assertEquals (String description, Object expected, Object actual, Exchange exchange)
specifier|public
specifier|static
name|void
name|assertEquals
parameter_list|(
name|String
name|description
parameter_list|,
name|Object
name|expected
parameter_list|,
name|Object
name|actual
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
operator|!
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|expected
argument_list|,
name|actual
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|(
name|description
operator|+
literal|" does not match. Expected: "
operator|+
name|expected
operator|+
literal|" but was: "
operator|+
name|actual
operator|+
literal|" on "
operator|+
name|exchange
operator|+
literal|" with headers: "
operator|+
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|Consumer
name|answer
init|=
operator|new
name|DataSetConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|reset ()
specifier|public
name|void
name|reset
parameter_list|()
block|{
name|super
operator|.
name|reset
argument_list|()
expr_stmt|;
name|receivedCounter
operator|.
name|set
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getReceivedCounter ()
specifier|public
name|int
name|getReceivedCounter
parameter_list|()
block|{
return|return
name|receivedCounter
operator|.
name|get
argument_list|()
return|;
block|}
comment|/**      * Creates a message exchange for the given index in the {@link DataSet}      */
DECL|method|createExchange (long messageIndex)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|long
name|messageIndex
parameter_list|)
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|createExchange
argument_list|()
decl_stmt|;
name|getDataSet
argument_list|()
operator|.
name|populateMessage
argument_list|(
name|exchange
argument_list|,
name|messageIndex
argument_list|)
expr_stmt|;
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|DATASET_INDEX
argument_list|,
name|messageIndex
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
DECL|method|getMinRate ()
specifier|public
name|int
name|getMinRate
parameter_list|()
block|{
return|return
name|minRate
return|;
block|}
DECL|method|setMinRate (int minRate)
specifier|public
name|void
name|setMinRate
parameter_list|(
name|int
name|minRate
parameter_list|)
block|{
name|this
operator|.
name|minRate
operator|=
name|minRate
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|waitForCompleteLatch (long timeout)
specifier|protected
name|void
name|waitForCompleteLatch
parameter_list|(
name|long
name|timeout
parameter_list|)
throws|throws
name|InterruptedException
block|{
name|super
operator|.
name|waitForCompleteLatch
argument_list|(
name|timeout
argument_list|)
expr_stmt|;
if|if
condition|(
name|minRate
operator|>
literal|0
condition|)
block|{
name|int
name|count
init|=
name|getReceivedCounter
argument_list|()
decl_stmt|;
do|do
block|{
comment|// wait as long as we get a decent message rate
name|super
operator|.
name|waitForCompleteLatch
argument_list|(
literal|1000L
argument_list|)
expr_stmt|;
name|count
operator|=
name|getReceivedCounter
argument_list|()
operator|-
name|count
expr_stmt|;
block|}
do|while
condition|(
name|count
operator|>=
name|minRate
condition|)
do|;
block|}
block|}
comment|// Properties
comment|//-------------------------------------------------------------------------
DECL|method|getDataSet ()
specifier|public
name|DataSet
name|getDataSet
parameter_list|()
block|{
return|return
name|dataSet
return|;
block|}
DECL|method|setDataSet (DataSet dataSet)
specifier|public
name|void
name|setDataSet
parameter_list|(
name|DataSet
name|dataSet
parameter_list|)
block|{
name|this
operator|.
name|dataSet
operator|=
name|dataSet
expr_stmt|;
block|}
DECL|method|getPreloadSize ()
specifier|public
name|long
name|getPreloadSize
parameter_list|()
block|{
return|return
name|preloadSize
return|;
block|}
comment|/**      * Sets how many messages should be preloaded (sent) before the route completes its initialization      */
DECL|method|setPreloadSize (long preloadSize)
specifier|public
name|void
name|setPreloadSize
parameter_list|(
name|long
name|preloadSize
parameter_list|)
block|{
name|this
operator|.
name|preloadSize
operator|=
name|preloadSize
expr_stmt|;
block|}
DECL|method|getConsumeDelay ()
specifier|public
name|long
name|getConsumeDelay
parameter_list|()
block|{
return|return
name|consumeDelay
return|;
block|}
comment|/**      * Allows a delay to be specified which causes consumers to pause - to simulate slow consumers      */
DECL|method|setConsumeDelay (long consumeDelay)
specifier|public
name|void
name|setConsumeDelay
parameter_list|(
name|long
name|consumeDelay
parameter_list|)
block|{
name|this
operator|.
name|consumeDelay
operator|=
name|consumeDelay
expr_stmt|;
block|}
DECL|method|getProduceDelay ()
specifier|public
name|long
name|getProduceDelay
parameter_list|()
block|{
return|return
name|produceDelay
return|;
block|}
comment|/**      * Allows a delay to be specified which causes producers to pause - to simulate slow producers      */
DECL|method|setProduceDelay (long produceDelay)
specifier|public
name|void
name|setProduceDelay
parameter_list|(
name|long
name|produceDelay
parameter_list|)
block|{
name|this
operator|.
name|produceDelay
operator|=
name|produceDelay
expr_stmt|;
block|}
DECL|method|getInitialDelay ()
specifier|public
name|long
name|getInitialDelay
parameter_list|()
block|{
return|return
name|initialDelay
return|;
block|}
DECL|method|setInitialDelay (long initialDelay)
specifier|public
name|void
name|setInitialDelay
parameter_list|(
name|long
name|initialDelay
parameter_list|)
block|{
name|this
operator|.
name|initialDelay
operator|=
name|initialDelay
expr_stmt|;
block|}
comment|// Implementation methods
comment|//-------------------------------------------------------------------------
annotation|@
name|Override
DECL|method|performAssertions (Exchange actual, Exchange copy)
specifier|protected
name|void
name|performAssertions
parameter_list|(
name|Exchange
name|actual
parameter_list|,
name|Exchange
name|copy
parameter_list|)
throws|throws
name|Exception
block|{
name|int
name|receivedCount
init|=
name|receivedCounter
operator|.
name|incrementAndGet
argument_list|()
decl_stmt|;
name|long
name|index
init|=
name|receivedCount
operator|-
literal|1
decl_stmt|;
name|Exchange
name|expected
init|=
name|createExchange
argument_list|(
name|index
argument_list|)
decl_stmt|;
comment|// now let's assert that they are the same
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Received message: {} (DataSet index={}) = {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|index
block|,
name|copy
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|DATASET_INDEX
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
block|,
name|copy
block|}
argument_list|)
expr_stmt|;
block|}
name|assertMessageExpected
argument_list|(
name|index
argument_list|,
name|expected
argument_list|,
name|copy
argument_list|)
expr_stmt|;
if|if
condition|(
name|consumeDelay
operator|>
literal|0
condition|)
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|consumeDelay
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|assertMessageExpected (long index, Exchange expected, Exchange actual)
specifier|protected
name|void
name|assertMessageExpected
parameter_list|(
name|long
name|index
parameter_list|,
name|Exchange
name|expected
parameter_list|,
name|Exchange
name|actual
parameter_list|)
throws|throws
name|Exception
block|{
name|long
name|actualCounter
init|=
name|ExchangeHelper
operator|.
name|getMandatoryHeader
argument_list|(
name|actual
argument_list|,
name|Exchange
operator|.
name|DATASET_INDEX
argument_list|,
name|Long
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Header: "
operator|+
name|Exchange
operator|.
name|DATASET_INDEX
argument_list|,
name|index
argument_list|,
name|actualCounter
argument_list|,
name|actual
argument_list|)
expr_stmt|;
name|getDataSet
argument_list|()
operator|.
name|assertMessageExpected
argument_list|(
name|this
argument_list|,
name|expected
argument_list|,
name|actual
argument_list|,
name|index
argument_list|)
expr_stmt|;
block|}
DECL|method|createReporter ()
specifier|protected
name|ThroughputLogger
name|createReporter
parameter_list|()
block|{
comment|// must sanitize uri to avoid logging sensitive information
name|String
name|uri
init|=
name|URISupport
operator|.
name|sanitizeUri
argument_list|(
name|getEndpointUri
argument_list|()
argument_list|)
decl_stmt|;
name|CamelLogger
name|logger
init|=
operator|new
name|CamelLogger
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|ThroughputLogger
name|answer
init|=
operator|new
name|ThroughputLogger
argument_list|(
name|logger
argument_list|,
operator|(
name|int
operator|)
name|this
operator|.
name|getDataSet
argument_list|()
operator|.
name|getReportCount
argument_list|()
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setAction
argument_list|(
literal|"Received"
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|long
name|size
init|=
name|getDataSet
argument_list|()
operator|.
name|getSize
argument_list|()
decl_stmt|;
name|expectedMessageCount
argument_list|(
operator|(
name|int
operator|)
name|size
argument_list|)
expr_stmt|;
if|if
condition|(
name|reporter
operator|==
literal|null
condition|)
block|{
name|reporter
operator|=
name|createReporter
argument_list|()
expr_stmt|;
block|}
name|log
operator|.
name|info
argument_list|(
name|this
operator|+
literal|" expecting "
operator|+
name|size
operator|+
literal|" messages"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

