begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|file
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
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
name|component
operator|.
name|bean
operator|.
name|MethodNotFoundException
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
name|JndiRegistry
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
name|PollingConsumerPollStrategy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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

begin_import
import|import static
name|org
operator|.
name|awaitility
operator|.
name|Awaitility
operator|.
name|await
import|;
end_import

begin_comment
comment|/**  * Unit test for expression option for file consumer.  */
end_comment

begin_class
DECL|class|FileConsumerFileExpressionThrowExceptionTest
specifier|public
class|class
name|FileConsumerFileExpressionThrowExceptionTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|event
specifier|private
specifier|static
specifier|volatile
name|String
name|event
init|=
literal|""
decl_stmt|;
DECL|field|rollbackCause
specifier|private
specifier|static
specifier|volatile
name|Exception
name|rollbackCause
decl_stmt|;
DECL|field|LATCH
specifier|private
specifier|static
specifier|final
name|CountDownLatch
name|LATCH
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteDirectory
argument_list|(
literal|"target/data/filelanguage"
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|jndi
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"counter"
argument_list|,
operator|new
name|MyGuidGenerator
argument_list|()
argument_list|)
expr_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"myPoll"
argument_list|,
operator|new
name|MyPollStrategy
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
block|}
annotation|@
name|Test
DECL|method|testConsumeExpressionThrowException ()
specifier|public
name|void
name|testConsumeExpressionThrowException
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file://target/data/filelanguage/bean"
argument_list|,
literal|"Bye World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"123.txt"
argument_list|)
expr_stmt|;
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
literal|"file://target/data/filelanguage/bean/"
operator|+
literal|"?pollStrategy=#myPoll&initialDelay=0&delay=10&fileName=${bean:counter?method=doNotExistMethod}.txt&delete=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// specify a method name that does not exists
block|}
block|}
argument_list|)
expr_stmt|;
name|await
argument_list|()
operator|.
name|atMost
argument_list|(
literal|2
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
operator|.
name|until
argument_list|(
parameter_list|()
lambda|->
name|LATCH
operator|.
name|getCount
argument_list|()
operator|==
literal|0
argument_list|)
expr_stmt|;
comment|// and we should rollback X number of times
name|assertTrue
argument_list|(
name|event
operator|.
name|startsWith
argument_list|(
literal|"rollback"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|rollbackCause
argument_list|)
expr_stmt|;
name|MethodNotFoundException
name|e
init|=
name|assertIsInstanceOf
argument_list|(
name|MethodNotFoundException
operator|.
name|class
argument_list|,
name|rollbackCause
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"doNotExistMethod"
argument_list|,
name|e
operator|.
name|getMethodName
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|class|MyGuidGenerator
specifier|public
class|class
name|MyGuidGenerator
block|{
DECL|method|next ()
specifier|public
name|String
name|next
parameter_list|()
block|{
return|return
literal|"123"
return|;
block|}
block|}
DECL|class|MyPollStrategy
specifier|private
specifier|static
class|class
name|MyPollStrategy
implements|implements
name|PollingConsumerPollStrategy
block|{
annotation|@
name|Override
DECL|method|begin (Consumer consumer, Endpoint endpoint)
specifier|public
name|boolean
name|begin
parameter_list|(
name|Consumer
name|consumer
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|commit (Consumer consumer, Endpoint endpoint, int polledMessages)
specifier|public
name|void
name|commit
parameter_list|(
name|Consumer
name|consumer
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|,
name|int
name|polledMessages
parameter_list|)
block|{
name|event
operator|+=
literal|"commit"
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|rollback (Consumer consumer, Endpoint endpoint, int retryCounter, Exception cause)
specifier|public
name|boolean
name|rollback
parameter_list|(
name|Consumer
name|consumer
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|,
name|int
name|retryCounter
parameter_list|,
name|Exception
name|cause
parameter_list|)
throws|throws
name|Exception
block|{
name|event
operator|+=
literal|"rollback"
expr_stmt|;
name|rollbackCause
operator|=
name|cause
expr_stmt|;
name|LATCH
operator|.
name|countDown
argument_list|()
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
block|}
end_class

end_unit

