begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.web3j
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|web3j
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
name|builder
operator|.
name|RouteBuilder
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
name|mockito
operator|.
name|Mock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|Mockito
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|invocation
operator|.
name|InvocationOnMock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|stubbing
operator|.
name|Answer
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
name|Subscription
import|;
end_import

begin_import
import|import
name|rx
operator|.
name|functions
operator|.
name|Action0
import|;
end_import

begin_import
import|import
name|rx
operator|.
name|functions
operator|.
name|Action1
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
name|component
operator|.
name|web3j
operator|.
name|Web3jConstants
operator|.
name|ETH_BLOCK_HASH_OBSERVABLE
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
name|component
operator|.
name|web3j
operator|.
name|Web3jConstants
operator|.
name|OPERATION
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|ArgumentMatchers
operator|.
name|any
import|;
end_import

begin_class
DECL|class|Web3jConsumerEthBlockHashObservableMockTest
specifier|public
class|class
name|Web3jConsumerEthBlockHashObservableMockTest
extends|extends
name|Web3jMockTestSupport
block|{
annotation|@
name|Mock
DECL|field|observable
specifier|private
name|Observable
argument_list|<
name|String
argument_list|>
name|observable
decl_stmt|;
annotation|@
name|Test
DECL|method|successTest ()
specifier|public
name|void
name|successTest
parameter_list|()
throws|throws
name|Exception
block|{
name|mockError
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|mockResult
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|mockWeb3j
operator|.
name|ethBlockHashObservable
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|observable
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|observable
operator|.
name|subscribe
argument_list|(
name|any
argument_list|()
argument_list|,
name|any
argument_list|()
argument_list|,
name|any
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenAnswer
argument_list|(
operator|new
name|Answer
argument_list|()
block|{
specifier|public
name|Subscription
name|answer
parameter_list|(
name|InvocationOnMock
name|invocation
parameter_list|)
block|{
name|Object
index|[]
name|args
init|=
name|invocation
operator|.
name|getArguments
argument_list|()
decl_stmt|;
operator|(
operator|(
name|Action1
argument_list|<
name|String
argument_list|>
operator|)
name|args
index|[
literal|0
index|]
operator|)
operator|.
name|call
argument_list|(
operator|new
name|String
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|subscription
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|mockResult
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|mockError
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|errorTest ()
specifier|public
name|void
name|errorTest
parameter_list|()
throws|throws
name|Exception
block|{
name|mockResult
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|mockError
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|mockWeb3j
operator|.
name|ethBlockHashObservable
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|observable
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|observable
operator|.
name|subscribe
argument_list|(
name|any
argument_list|()
argument_list|,
name|any
argument_list|()
argument_list|,
name|any
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenAnswer
argument_list|(
operator|new
name|Answer
argument_list|()
block|{
specifier|public
name|Subscription
name|answer
parameter_list|(
name|InvocationOnMock
name|invocation
parameter_list|)
block|{
name|Object
index|[]
name|args
init|=
name|invocation
operator|.
name|getArguments
argument_list|()
decl_stmt|;
operator|(
operator|(
name|Action1
argument_list|<
name|Throwable
argument_list|>
operator|)
name|args
index|[
literal|1
index|]
operator|)
operator|.
name|call
argument_list|(
operator|new
name|RuntimeException
argument_list|(
literal|"Error"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|subscription
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|mockError
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|mockResult
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|doneTest ()
specifier|public
name|void
name|doneTest
parameter_list|()
throws|throws
name|Exception
block|{
name|mockResult
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mockResult
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"status"
argument_list|,
literal|"done"
argument_list|)
expr_stmt|;
name|mockError
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|mockWeb3j
operator|.
name|ethBlockHashObservable
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|observable
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|observable
operator|.
name|subscribe
argument_list|(
name|any
argument_list|()
argument_list|,
name|any
argument_list|()
argument_list|,
name|any
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenAnswer
argument_list|(
operator|new
name|Answer
argument_list|()
block|{
specifier|public
name|Subscription
name|answer
parameter_list|(
name|InvocationOnMock
name|invocation
parameter_list|)
block|{
name|Object
index|[]
name|args
init|=
name|invocation
operator|.
name|getArguments
argument_list|()
decl_stmt|;
operator|(
operator|(
name|Action0
operator|)
name|args
index|[
literal|2
index|]
operator|)
operator|.
name|call
argument_list|()
expr_stmt|;
return|return
name|subscription
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|mockError
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|mockResult
operator|.
name|assertIsSatisfied
argument_list|()
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
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|errorHandler
argument_list|(
name|deadLetterChannel
argument_list|(
literal|"mock:error"
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|getUrl
argument_list|()
operator|+
name|OPERATION
operator|.
name|toLowerCase
argument_list|()
operator|+
literal|"="
operator|+
name|ETH_BLOCK_HASH_OBSERVABLE
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

