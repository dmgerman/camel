begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.disruptor.vm
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|disruptor
operator|.
name|vm
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|BindToRegistry
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
name|mock
operator|.
name|MockEndpoint
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
DECL|class|DisruptorVmSplitterTest
specifier|public
class|class
name|DisruptorVmSplitterTest
extends|extends
name|AbstractVmTestSupport
block|{
annotation|@
name|BindToRegistry
argument_list|(
literal|"splitterBean"
argument_list|)
DECL|field|swb
specifier|private
name|SplitWordsBean
name|swb
init|=
operator|new
name|SplitWordsBean
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testSplitUsingMethodCall ()
specifier|public
name|void
name|testSplitUsingMethodCall
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|resultEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Claus"
argument_list|,
literal|"James"
argument_list|,
literal|"Willem"
argument_list|)
expr_stmt|;
name|template2
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Claus@James@Willem"
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
name|from
argument_list|(
literal|"disruptor-vm:server"
argument_list|)
operator|.
name|split
argument_list|()
operator|.
name|method
argument_list|(
literal|"splitterBean"
argument_list|,
literal|"splitWords"
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
annotation|@
name|Override
DECL|method|createRouteBuilderForSecondContext ()
specifier|protected
name|RouteBuilder
name|createRouteBuilderForSecondContext
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
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"disruptor-vm:server"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|SplitWordsBean
specifier|public
specifier|static
specifier|final
class|class
name|SplitWordsBean
block|{
DECL|method|SplitWordsBean ()
specifier|private
name|SplitWordsBean
parameter_list|()
block|{
comment|// Helper Class
block|}
DECL|method|splitWords (String body)
specifier|public
specifier|static
name|List
argument_list|<
name|String
argument_list|>
name|splitWords
parameter_list|(
name|String
name|body
parameter_list|)
block|{
comment|// here we split the payload using java code
comment|// we have the true power of Java to do the splitting
comment|// as we like. As this is based on a unit test we just do it easy
return|return
name|Arrays
operator|.
name|asList
argument_list|(
name|body
operator|.
name|split
argument_list|(
literal|"@"
argument_list|)
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

