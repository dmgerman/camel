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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|LinkedBlockingQueue
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
name|impl
operator|.
name|DefaultCamelContext
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
name|support
operator|.
name|SimpleRegistry
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
name|SedaConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|SedaQueueFactoryTest
specifier|public
class|class
name|SedaQueueFactoryTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|arrayQueueFactory
specifier|private
specifier|final
name|ArrayBlockingQueueFactory
argument_list|<
name|Exchange
argument_list|>
name|arrayQueueFactory
init|=
operator|new
name|ArrayBlockingQueueFactory
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|SimpleRegistry
name|simpleRegistry
init|=
operator|new
name|SimpleRegistry
argument_list|()
decl_stmt|;
name|simpleRegistry
operator|.
name|put
argument_list|(
literal|"arrayQueueFactory"
argument_list|,
name|arrayQueueFactory
argument_list|)
expr_stmt|;
return|return
operator|new
name|DefaultCamelContext
argument_list|(
name|simpleRegistry
argument_list|)
return|;
block|}
annotation|@
name|Test
DECL|method|testArrayBlockingQueueFactory ()
specifier|public
name|void
name|testArrayBlockingQueueFactory
parameter_list|()
throws|throws
name|Exception
block|{
name|SedaEndpoint
name|endpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"seda:arrayQueue?queueFactory=#arrayQueueFactory"
argument_list|,
name|SedaEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|BlockingQueue
argument_list|<
name|Exchange
argument_list|>
name|queue
init|=
name|endpoint
operator|.
name|getQueue
argument_list|()
decl_stmt|;
name|ArrayBlockingQueue
argument_list|<
name|Exchange
argument_list|>
name|blockingQueue
init|=
name|assertIsInstanceOf
argument_list|(
name|ArrayBlockingQueue
operator|.
name|class
argument_list|,
name|queue
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"remainingCapacity - default"
argument_list|,
name|SedaConstants
operator|.
name|QUEUE_SIZE
argument_list|,
name|blockingQueue
operator|.
name|remainingCapacity
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Test
DECL|method|testArrayBlockingQueueFactoryAndSize ()
specifier|public
name|void
name|testArrayBlockingQueueFactoryAndSize
parameter_list|()
throws|throws
name|Exception
block|{
name|SedaEndpoint
name|endpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"seda:arrayQueue100?queueFactory=#arrayQueueFactory&size=100"
argument_list|,
name|SedaEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|BlockingQueue
argument_list|<
name|Exchange
argument_list|>
name|queue
init|=
name|endpoint
operator|.
name|getQueue
argument_list|()
decl_stmt|;
name|ArrayBlockingQueue
argument_list|<
name|Exchange
argument_list|>
name|blockingQueue
init|=
name|assertIsInstanceOf
argument_list|(
name|ArrayBlockingQueue
operator|.
name|class
argument_list|,
name|queue
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"remainingCapacity - custom"
argument_list|,
literal|100
argument_list|,
name|blockingQueue
operator|.
name|remainingCapacity
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDefaultBlockingQueueFactory ()
specifier|public
name|void
name|testDefaultBlockingQueueFactory
parameter_list|()
throws|throws
name|Exception
block|{
name|SedaEndpoint
name|endpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"seda:linkedQueue"
argument_list|,
name|SedaEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|BlockingQueue
argument_list|<
name|Exchange
argument_list|>
name|queue
init|=
name|endpoint
operator|.
name|getQueue
argument_list|()
decl_stmt|;
name|assertIsInstanceOf
argument_list|(
name|LinkedBlockingQueue
operator|.
name|class
argument_list|,
name|queue
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

