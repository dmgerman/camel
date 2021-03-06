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
name|Navigate
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
name|Route
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|ChoiceWithEndTest
specifier|public
class|class
name|ChoiceWithEndTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testRouteIsCorrectAtRuntime ()
specifier|public
name|void
name|testRouteIsCorrectAtRuntime
parameter_list|()
throws|throws
name|Exception
block|{
comment|// use navigate to find that the end works as expected
name|Navigate
argument_list|<
name|Processor
argument_list|>
name|nav
init|=
name|getRoute
argument_list|(
literal|"direct://start"
argument_list|)
operator|.
name|navigate
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Processor
argument_list|>
name|node
init|=
name|nav
operator|.
name|next
argument_list|()
decl_stmt|;
comment|// there should be 4 outputs as the end in the otherwise should
comment|// ensure that the transform and last send is not within the choice
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|node
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// the navigate API is a bit simple at this time of writing so it does
comment|// take a little
comment|// bit of ugly code to find the correct processor in the runtime route
name|assertIsInstanceOf
argument_list|(
name|SendProcessor
operator|.
name|class
argument_list|,
name|unwrapChannel
argument_list|(
name|node
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|getNextProcessor
argument_list|()
argument_list|)
expr_stmt|;
name|assertIsInstanceOf
argument_list|(
name|ChoiceProcessor
operator|.
name|class
argument_list|,
name|unwrapChannel
argument_list|(
name|node
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|getNextProcessor
argument_list|()
argument_list|)
expr_stmt|;
name|assertIsInstanceOf
argument_list|(
name|TransformProcessor
operator|.
name|class
argument_list|,
name|unwrapChannel
argument_list|(
name|node
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
operator|.
name|getNextProcessor
argument_list|()
argument_list|)
expr_stmt|;
name|assertIsInstanceOf
argument_list|(
name|SendProcessor
operator|.
name|class
argument_list|,
name|unwrapChannel
argument_list|(
name|node
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|)
operator|.
name|getNextProcessor
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|getRoute (String routeEndpointURI)
specifier|private
name|Route
name|getRoute
parameter_list|(
name|String
name|routeEndpointURI
parameter_list|)
block|{
name|Route
name|answer
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Route
name|route
range|:
name|context
operator|.
name|getRoutes
argument_list|()
control|)
block|{
if|if
condition|(
name|routeEndpointURI
operator|.
name|equals
argument_list|(
name|route
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
condition|)
block|{
name|answer
operator|=
name|route
expr_stmt|;
break|break;
block|}
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Test
DECL|method|testChoiceHello ()
specifier|public
name|void
name|testChoiceHello
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:start"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:echo"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"echo Hello World"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:last"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"last echo Hello World"
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
name|Test
DECL|method|testChoiceBye ()
specifier|public
name|void
name|testChoiceBye
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:start"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Bye World"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:bye"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"We do not care"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:last"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"last We do not care"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Bye World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testChoiceOther ()
specifier|public
name|void
name|testChoiceOther
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:start"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Camel"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:other"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"other Camel"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:last"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"last other Camel"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Camel"
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
name|MyChoiceBean
name|bean
init|=
operator|new
name|MyChoiceBean
argument_list|()
decl_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:start"
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|(
name|body
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Hello"
argument_list|)
argument_list|)
operator|.
name|bean
argument_list|(
name|bean
argument_list|,
literal|"echo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:echo"
argument_list|)
operator|.
name|when
argument_list|(
name|body
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Bye"
argument_list|)
argument_list|)
comment|// must use another route as the Java DSL
comment|// will lose its scope so you cannot call otherwise later
operator|.
name|to
argument_list|(
literal|"direct:bye"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:bye"
argument_list|)
operator|.
name|otherwise
argument_list|()
operator|.
name|bean
argument_list|(
name|bean
argument_list|,
literal|"other"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:other"
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|transform
argument_list|(
name|body
argument_list|()
operator|.
name|prepend
argument_list|(
literal|"last "
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:last"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:bye"
argument_list|)
operator|.
name|doTry
argument_list|()
operator|.
name|bean
argument_list|(
name|bean
argument_list|,
literal|"bye"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:bye"
argument_list|)
operator|.
name|doCatch
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
operator|.
name|setBody
argument_list|(
name|constant
argument_list|(
literal|"We do not care"
argument_list|)
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|MyChoiceBean
specifier|public
class|class
name|MyChoiceBean
block|{
DECL|method|echo (String s)
specifier|public
name|String
name|echo
parameter_list|(
name|String
name|s
parameter_list|)
block|{
return|return
literal|"echo "
operator|+
name|s
return|;
block|}
DECL|method|bye (String s)
specifier|public
name|String
name|bye
parameter_list|(
name|String
name|s
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Damn does not work"
argument_list|)
throw|;
block|}
DECL|method|other (String s)
specifier|public
name|String
name|other
parameter_list|(
name|String
name|s
parameter_list|)
block|{
return|return
literal|"other "
operator|+
name|s
return|;
block|}
block|}
block|}
end_class

end_unit

