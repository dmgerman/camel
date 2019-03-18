begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_class
DECL|class|OnCompletionContainsTest
specifier|public
class|class
name|OnCompletionContainsTest
extends|extends
name|ContextTestSupport
block|{
DECL|class|SimpleSynchronizationAdapter
class|class
name|SimpleSynchronizationAdapter
extends|extends
name|SynchronizationAdapter
block|{
DECL|field|endPoint
specifier|private
specifier|final
name|String
name|endPoint
decl_stmt|;
DECL|field|body
specifier|private
specifier|final
name|String
name|body
decl_stmt|;
DECL|method|SimpleSynchronizationAdapter (String endPoint, String body)
name|SimpleSynchronizationAdapter
parameter_list|(
name|String
name|endPoint
parameter_list|,
name|String
name|body
parameter_list|)
block|{
name|this
operator|.
name|endPoint
operator|=
name|endPoint
expr_stmt|;
name|this
operator|.
name|body
operator|=
name|body
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onDone (Exchange exchange)
specifier|public
name|void
name|onDone
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|template
operator|.
name|sendBody
argument_list|(
name|endPoint
argument_list|,
name|body
argument_list|)
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
name|body
return|;
block|}
block|}
annotation|@
name|Test
DECL|method|testOnCompletionContainsTest ()
specifier|public
name|void
name|testOnCompletionContainsTest
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:sync"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"C"
argument_list|,
literal|"B"
argument_list|,
literal|"B"
argument_list|,
literal|"A"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
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
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|onCompletion
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:sync"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
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
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|SynchronizationAdapter
name|adapter
init|=
operator|new
name|SimpleSynchronizationAdapter
argument_list|(
literal|"mock:sync"
argument_list|,
literal|"A"
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|addOnCompletion
argument_list|(
name|adapter
argument_list|)
expr_stmt|;
comment|// should not add the adapter again as we already have it
if|if
condition|(
operator|!
name|exchange
operator|.
name|containsOnCompletion
argument_list|(
name|adapter
argument_list|)
condition|)
block|{
name|exchange
operator|.
name|addOnCompletion
argument_list|(
name|adapter
argument_list|)
expr_stmt|;
block|}
name|adapter
operator|=
operator|new
name|SimpleSynchronizationAdapter
argument_list|(
literal|"mock:sync"
argument_list|,
literal|"B"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|addOnCompletion
argument_list|(
name|adapter
argument_list|)
expr_stmt|;
comment|// now add the B again as we want to test that this also work
if|if
condition|(
name|exchange
operator|.
name|containsOnCompletion
argument_list|(
name|adapter
argument_list|)
condition|)
block|{
name|exchange
operator|.
name|addOnCompletion
argument_list|(
name|adapter
argument_list|)
expr_stmt|;
block|}
comment|// add a C that is no a SimpleSynchronizationAdapter class
name|exchange
operator|.
name|addOnCompletion
argument_list|(
operator|new
name|SynchronizationAdapter
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onDone
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"mock:sync"
argument_list|,
literal|"C"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"C"
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
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

