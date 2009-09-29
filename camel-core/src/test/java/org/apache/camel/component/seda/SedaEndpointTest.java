begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.seda
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|seda
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
name|ArrayBlockingQueue
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
name|BlockingQueue
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
name|ContextTestSupport
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
name|Producer
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|SedaEndpointTest
specifier|public
class|class
name|SedaEndpointTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|queue
specifier|private
name|BlockingQueue
argument_list|<
name|Exchange
argument_list|>
name|queue
init|=
operator|new
name|ArrayBlockingQueue
argument_list|<
name|Exchange
argument_list|>
argument_list|(
literal|1000
argument_list|)
decl_stmt|;
DECL|method|testSedaEndpoint ()
specifier|public
name|void
name|testSedaEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|SedaEndpoint
name|seda
init|=
operator|new
name|SedaEndpoint
argument_list|(
literal|"seda://foo"
argument_list|,
name|queue
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|seda
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1000
argument_list|,
name|seda
operator|.
name|getSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|queue
argument_list|,
name|seda
operator|.
name|getQueue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|seda
operator|.
name|getConcurrentConsumers
argument_list|()
argument_list|)
expr_stmt|;
name|Producer
name|prod
init|=
name|seda
operator|.
name|createProducer
argument_list|()
decl_stmt|;
name|seda
operator|.
name|onStarted
argument_list|(
operator|(
name|SedaProducer
operator|)
name|prod
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|seda
operator|.
name|getProducers
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Consumer
name|cons
init|=
name|seda
operator|.
name|createConsumer
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
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
comment|// do nothing
block|}
block|}
argument_list|)
decl_stmt|;
name|seda
operator|.
name|onStarted
argument_list|(
operator|(
name|SedaConsumer
operator|)
name|cons
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|seda
operator|.
name|getConsumers
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|seda
operator|.
name|getExchanges
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testSedaEndpointTwo ()
specifier|public
name|void
name|testSedaEndpointTwo
parameter_list|()
throws|throws
name|Exception
block|{
name|SedaEndpoint
name|seda
init|=
operator|new
name|SedaEndpoint
argument_list|(
literal|"seda://foo"
argument_list|,
name|queue
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|seda
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1000
argument_list|,
name|seda
operator|.
name|getSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|queue
argument_list|,
name|seda
operator|.
name|getQueue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|seda
operator|.
name|getConcurrentConsumers
argument_list|()
argument_list|)
expr_stmt|;
name|Producer
name|prod
init|=
name|seda
operator|.
name|createProducer
argument_list|()
decl_stmt|;
name|seda
operator|.
name|onStarted
argument_list|(
operator|(
name|SedaProducer
operator|)
name|prod
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|seda
operator|.
name|getProducers
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Consumer
name|cons
init|=
name|seda
operator|.
name|createConsumer
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
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
comment|// do nothing
block|}
block|}
argument_list|)
decl_stmt|;
name|seda
operator|.
name|onStarted
argument_list|(
operator|(
name|SedaConsumer
operator|)
name|cons
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|seda
operator|.
name|getConsumers
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|seda
operator|.
name|getExchanges
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testSedaEndpointSetQueue ()
specifier|public
name|void
name|testSedaEndpointSetQueue
parameter_list|()
throws|throws
name|Exception
block|{
name|SedaEndpoint
name|seda
init|=
operator|new
name|SedaEndpoint
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|seda
argument_list|)
expr_stmt|;
name|seda
operator|.
name|setQueue
argument_list|(
operator|new
name|ArrayBlockingQueue
argument_list|<
name|Exchange
argument_list|>
argument_list|(
literal|1000
argument_list|)
argument_list|)
expr_stmt|;
name|seda
operator|.
name|setConcurrentConsumers
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1000
argument_list|,
name|seda
operator|.
name|getSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|queue
argument_list|,
name|seda
operator|.
name|getQueue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|seda
operator|.
name|getConcurrentConsumers
argument_list|()
argument_list|)
expr_stmt|;
name|Producer
name|prod
init|=
name|seda
operator|.
name|createProducer
argument_list|()
decl_stmt|;
name|seda
operator|.
name|onStarted
argument_list|(
operator|(
name|SedaProducer
operator|)
name|prod
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|seda
operator|.
name|getProducers
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Consumer
name|cons
init|=
name|seda
operator|.
name|createConsumer
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
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
comment|// do nothing
block|}
block|}
argument_list|)
decl_stmt|;
name|seda
operator|.
name|onStarted
argument_list|(
operator|(
name|SedaConsumer
operator|)
name|cons
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|seda
operator|.
name|getConsumers
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|seda
operator|.
name|getExchanges
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

