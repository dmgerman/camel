begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.thrift
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|thrift
package|;
end_package

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|ByteBuffer
import|;
end_import

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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|thrift
operator|.
name|generated
operator|.
name|InvalidOperation
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
name|thrift
operator|.
name|generated
operator|.
name|Operation
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
name|thrift
operator|.
name|generated
operator|.
name|Work
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
name|SynchronizationAdapter
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

begin_class
DECL|class|ThriftProducerAsyncTest
specifier|public
class|class
name|ThriftProducerAsyncTest
extends|extends
name|ThriftProducerBaseTest
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ThriftProducerAsyncTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|responseBody
specifier|private
name|Object
name|responseBody
decl_stmt|;
annotation|@
name|Test
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"rawtypes"
block|,
literal|"unchecked"
block|}
argument_list|)
DECL|method|testCalculateMethodInvocation ()
specifier|public
name|void
name|testCalculateMethodInvocation
parameter_list|()
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Thrift calculate method async test start"
argument_list|)
expr_stmt|;
name|List
name|requestBody
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
specifier|final
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|requestBody
operator|.
name|add
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|requestBody
operator|.
name|add
argument_list|(
operator|new
name|Work
argument_list|(
name|THRIFT_TEST_NUM1
argument_list|,
name|THRIFT_TEST_NUM2
argument_list|,
name|Operation
operator|.
name|MULTIPLY
argument_list|)
argument_list|)
expr_stmt|;
name|template
operator|.
name|asyncCallbackSendBody
argument_list|(
literal|"direct:thrift-calculate"
argument_list|,
name|requestBody
argument_list|,
operator|new
name|SynchronizationAdapter
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onComplete
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|responseBody
operator|=
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onFailure
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|responseBody
operator|=
name|exchange
operator|.
name|getException
argument_list|()
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|latch
operator|.
name|await
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|responseBody
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|responseBody
operator|instanceof
name|Integer
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|THRIFT_TEST_NUM1
operator|*
name|THRIFT_TEST_NUM2
argument_list|,
name|responseBody
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"rawtypes"
block|,
literal|"unchecked"
block|}
argument_list|)
DECL|method|testAddMethodInvocation ()
specifier|public
name|void
name|testAddMethodInvocation
parameter_list|()
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Thrift add method (primitive parameters only) async test start"
argument_list|)
expr_stmt|;
specifier|final
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|List
name|requestBody
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|responseBody
operator|=
literal|null
expr_stmt|;
name|requestBody
operator|.
name|add
argument_list|(
name|THRIFT_TEST_NUM1
argument_list|)
expr_stmt|;
name|requestBody
operator|.
name|add
argument_list|(
name|THRIFT_TEST_NUM2
argument_list|)
expr_stmt|;
name|template
operator|.
name|asyncCallbackSendBody
argument_list|(
literal|"direct:thrift-add"
argument_list|,
name|requestBody
argument_list|,
operator|new
name|SynchronizationAdapter
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onComplete
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|responseBody
operator|=
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onFailure
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|responseBody
operator|=
name|exchange
operator|.
name|getException
argument_list|()
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|latch
operator|.
name|await
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|responseBody
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|responseBody
operator|instanceof
name|Integer
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|THRIFT_TEST_NUM1
operator|+
name|THRIFT_TEST_NUM2
argument_list|,
name|responseBody
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"rawtypes"
block|,
literal|"unchecked"
block|}
argument_list|)
DECL|method|testCalculateWithException ()
specifier|public
name|void
name|testCalculateWithException
parameter_list|()
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Thrift calculate method with business exception async test start"
argument_list|)
expr_stmt|;
specifier|final
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|List
name|requestBody
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|requestBody
operator|.
name|add
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|requestBody
operator|.
name|add
argument_list|(
operator|new
name|Work
argument_list|(
name|THRIFT_TEST_NUM1
argument_list|,
literal|0
argument_list|,
name|Operation
operator|.
name|DIVIDE
argument_list|)
argument_list|)
expr_stmt|;
name|template
operator|.
name|asyncCallbackSendBody
argument_list|(
literal|"direct:thrift-calculate"
argument_list|,
name|requestBody
argument_list|,
operator|new
name|SynchronizationAdapter
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onComplete
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onFailure
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|responseBody
operator|=
name|exchange
operator|.
name|getException
argument_list|()
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|latch
operator|.
name|await
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Get an InvalidOperation exception"
argument_list|,
name|responseBody
operator|instanceof
name|InvalidOperation
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testVoidMethodInvocation ()
specifier|public
name|void
name|testVoidMethodInvocation
parameter_list|()
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Thrift method with empty parameters and void output async test start"
argument_list|)
expr_stmt|;
specifier|final
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
specifier|final
name|Object
name|requestBody
init|=
literal|null
decl_stmt|;
name|responseBody
operator|=
operator|new
name|Object
argument_list|()
expr_stmt|;
name|template
operator|.
name|asyncCallbackSendBody
argument_list|(
literal|"direct:thrift-ping"
argument_list|,
name|requestBody
argument_list|,
operator|new
name|SynchronizationAdapter
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onComplete
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|responseBody
operator|=
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onFailure
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|responseBody
operator|=
name|exchange
operator|.
name|getException
argument_list|()
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|latch
operator|.
name|await
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|responseBody
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testOneWayMethodInvocation ()
specifier|public
name|void
name|testOneWayMethodInvocation
parameter_list|()
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Thrift one-way method async test start"
argument_list|)
expr_stmt|;
specifier|final
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
specifier|final
name|Object
name|requestBody
init|=
literal|null
decl_stmt|;
name|responseBody
operator|=
operator|new
name|Object
argument_list|()
expr_stmt|;
name|template
operator|.
name|asyncCallbackSendBody
argument_list|(
literal|"direct:thrift-zip"
argument_list|,
name|requestBody
argument_list|,
operator|new
name|SynchronizationAdapter
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onComplete
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|responseBody
operator|=
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onFailure
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|responseBody
operator|=
name|exchange
operator|.
name|getException
argument_list|()
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|latch
operator|.
name|await
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|responseBody
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"rawtypes"
block|,
literal|"unchecked"
block|}
argument_list|)
DECL|method|testAllTypesMethodInvocation ()
specifier|public
name|void
name|testAllTypesMethodInvocation
parameter_list|()
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Thrift method with all possile types async test start"
argument_list|)
expr_stmt|;
specifier|final
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|List
name|requestBody
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|requestBody
operator|.
name|add
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|requestBody
operator|.
name|add
argument_list|(
operator|(
name|byte
operator|)
name|THRIFT_TEST_NUM1
argument_list|)
expr_stmt|;
name|requestBody
operator|.
name|add
argument_list|(
operator|(
name|short
operator|)
name|THRIFT_TEST_NUM1
argument_list|)
expr_stmt|;
name|requestBody
operator|.
name|add
argument_list|(
name|THRIFT_TEST_NUM1
argument_list|)
expr_stmt|;
name|requestBody
operator|.
name|add
argument_list|(
operator|(
name|long
operator|)
name|THRIFT_TEST_NUM1
argument_list|)
expr_stmt|;
name|requestBody
operator|.
name|add
argument_list|(
operator|(
name|double
operator|)
name|THRIFT_TEST_NUM1
argument_list|)
expr_stmt|;
name|requestBody
operator|.
name|add
argument_list|(
literal|"empty"
argument_list|)
expr_stmt|;
name|requestBody
operator|.
name|add
argument_list|(
name|ByteBuffer
operator|.
name|allocate
argument_list|(
literal|10
argument_list|)
argument_list|)
expr_stmt|;
name|requestBody
operator|.
name|add
argument_list|(
operator|new
name|Work
argument_list|(
name|THRIFT_TEST_NUM1
argument_list|,
name|THRIFT_TEST_NUM2
argument_list|,
name|Operation
operator|.
name|MULTIPLY
argument_list|)
argument_list|)
expr_stmt|;
name|requestBody
operator|.
name|add
argument_list|(
operator|new
name|ArrayList
argument_list|<
name|Integer
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
name|requestBody
operator|.
name|add
argument_list|(
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
name|requestBody
operator|.
name|add
argument_list|(
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Long
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
name|responseBody
operator|=
operator|new
name|Object
argument_list|()
expr_stmt|;
name|template
operator|.
name|asyncCallbackSendBody
argument_list|(
literal|"direct:thrift-alltypes"
argument_list|,
name|requestBody
argument_list|,
operator|new
name|SynchronizationAdapter
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onComplete
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|responseBody
operator|=
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onFailure
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|responseBody
operator|=
name|exchange
operator|.
name|getException
argument_list|()
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|latch
operator|.
name|await
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|responseBody
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|responseBody
operator|instanceof
name|Integer
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|responseBody
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"rawtypes"
block|,
literal|"unchecked"
block|}
argument_list|)
DECL|method|testEchoMethodInvocation ()
specifier|public
name|void
name|testEchoMethodInvocation
parameter_list|()
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Thrift echo method (return output as pass input parameter) async test start"
argument_list|)
expr_stmt|;
specifier|final
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|List
name|requestBody
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|requestBody
operator|.
name|add
argument_list|(
operator|new
name|Work
argument_list|(
name|THRIFT_TEST_NUM1
argument_list|,
name|THRIFT_TEST_NUM2
argument_list|,
name|Operation
operator|.
name|MULTIPLY
argument_list|)
argument_list|)
expr_stmt|;
name|responseBody
operator|=
operator|new
name|Object
argument_list|()
expr_stmt|;
name|template
operator|.
name|asyncCallbackSendBody
argument_list|(
literal|"direct:thrift-echo"
argument_list|,
name|requestBody
argument_list|,
operator|new
name|SynchronizationAdapter
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onComplete
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|responseBody
operator|=
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onFailure
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|responseBody
operator|=
name|exchange
operator|.
name|getException
argument_list|()
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|latch
operator|.
name|await
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|responseBody
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|responseBody
operator|instanceof
name|Work
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|THRIFT_TEST_NUM1
argument_list|,
operator|(
operator|(
name|Work
operator|)
name|responseBody
operator|)
operator|.
name|num1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Operation
operator|.
name|MULTIPLY
argument_list|,
operator|(
operator|(
name|Work
operator|)
name|responseBody
operator|)
operator|.
name|op
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
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
block|{
name|from
argument_list|(
literal|"direct:thrift-calculate"
argument_list|)
operator|.
name|to
argument_list|(
literal|"thrift://localhost:"
operator|+
name|THRIFT_TEST_PORT
operator|+
literal|"/org.apache.camel.component.thrift.generated.Calculator?method=calculate"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:thrift-add"
argument_list|)
operator|.
name|to
argument_list|(
literal|"thrift://localhost:"
operator|+
name|THRIFT_TEST_PORT
operator|+
literal|"/org.apache.camel.component.thrift.generated.Calculator?method=add"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:thrift-ping"
argument_list|)
operator|.
name|to
argument_list|(
literal|"thrift://localhost:"
operator|+
name|THRIFT_TEST_PORT
operator|+
literal|"/org.apache.camel.component.thrift.generated.Calculator?method=ping"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:thrift-zip"
argument_list|)
operator|.
name|to
argument_list|(
literal|"thrift://localhost:"
operator|+
name|THRIFT_TEST_PORT
operator|+
literal|"/org.apache.camel.component.thrift.generated.Calculator?method=zip"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:thrift-alltypes"
argument_list|)
operator|.
name|to
argument_list|(
literal|"thrift://localhost:"
operator|+
name|THRIFT_TEST_PORT
operator|+
literal|"/org.apache.camel.component.thrift.generated.Calculator?method=alltypes"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:thrift-echo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"thrift://localhost:"
operator|+
name|THRIFT_TEST_PORT
operator|+
literal|"/org.apache.camel.component.thrift.generated.Calculator?method=echo"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

