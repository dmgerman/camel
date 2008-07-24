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
name|concurrent
operator|.
name|CountDownLatch
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|builder
operator|.
name|RouteBuilder
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

begin_class
DECL|class|Camel715ThreadProcessorTest
specifier|public
class|class
name|Camel715ThreadProcessorTest
extends|extends
name|TestCase
block|{
DECL|field|ITERS
specifier|private
specifier|static
specifier|final
name|int
name|ITERS
init|=
literal|50000
decl_stmt|;
DECL|class|SendingProcessor
class|class
name|SendingProcessor
implements|implements
name|Processor
block|{
DECL|field|iterationNumber
name|int
name|iterationNumber
decl_stmt|;
DECL|method|SendingProcessor (int iter)
specifier|public
name|SendingProcessor
parameter_list|(
name|int
name|iter
parameter_list|)
block|{
name|iterationNumber
operator|=
name|iter
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
name|setBody
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
comment|// may set the property here
name|exchange
operator|.
name|setProperty
argument_list|(
literal|"iterationNumber"
argument_list|,
name|iterationNumber
argument_list|)
expr_stmt|;
block|}
block|}
empty_stmt|;
DECL|method|testThreadProcessor ()
specifier|public
name|void
name|testThreadProcessor
parameter_list|()
block|{
try|try
block|{
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
specifier|final
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
name|ITERS
argument_list|)
decl_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:a"
argument_list|)
operator|.
name|thread
argument_list|(
literal|4
argument_list|)
operator|.
name|process
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
name|ex
parameter_list|)
throws|throws
name|Exception
block|{
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
specifier|final
name|ProducerTemplate
argument_list|<
name|Exchange
argument_list|>
name|template
init|=
name|context
operator|.
name|createProducerTemplate
argument_list|()
decl_stmt|;
specifier|final
name|Endpoint
name|e
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"direct:a"
argument_list|)
decl_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|Long
name|startTime
init|=
name|System
operator|.
name|nanoTime
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|ITERS
condition|;
name|i
operator|++
control|)
block|{
name|template
operator|.
name|send
argument_list|(
name|e
argument_list|,
operator|new
name|SendingProcessor
argument_list|(
name|i
argument_list|)
argument_list|,
operator|new
name|AsyncCallback
argument_list|()
block|{
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|arg0
parameter_list|)
block|{
comment|// Do nothing here
block|}
block|}
argument_list|)
expr_stmt|;
block|}
name|latch
operator|.
name|await
argument_list|()
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|fail
argument_list|(
literal|"Get the exception "
operator|+
name|ex
operator|+
literal|"here"
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

