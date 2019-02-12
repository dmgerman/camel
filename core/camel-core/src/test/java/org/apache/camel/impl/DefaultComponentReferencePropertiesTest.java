begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|java
operator|.
name|util
operator|.
name|Map
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
name|Expression
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|ResolveEndpointFailedException
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
name|ExpressionBuilder
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
name|DefaultComponent
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
name|DefaultEndpoint
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
name|DefaultExchange
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
comment|/**  * Unit test for reference properties  */
end_comment

begin_class
DECL|class|DefaultComponentReferencePropertiesTest
specifier|public
class|class
name|DefaultComponentReferencePropertiesTest
extends|extends
name|ContextTestSupport
block|{
DECL|class|MyEndpoint
specifier|public
specifier|final
class|class
name|MyEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|expression
specifier|private
name|Expression
name|expression
decl_stmt|;
DECL|field|stringExpression
specifier|private
name|String
name|stringExpression
decl_stmt|;
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
DECL|field|special
specifier|private
name|Expression
name|special
decl_stmt|;
DECL|method|MyEndpoint (String endpointUri, Component component)
specifier|private
name|MyEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
literal|null
return|;
block|}
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
return|return
literal|null
return|;
block|}
DECL|method|setExpression (List<?> expressions)
specifier|public
name|void
name|setExpression
parameter_list|(
name|List
argument_list|<
name|?
argument_list|>
name|expressions
parameter_list|)
block|{
comment|// do nothing
block|}
DECL|method|setExpression (Expression expression)
specifier|public
name|void
name|setExpression
parameter_list|(
name|Expression
name|expression
parameter_list|)
block|{
name|this
operator|.
name|expression
operator|=
name|expression
expr_stmt|;
block|}
DECL|method|setExpression (String expression)
specifier|public
name|void
name|setExpression
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
name|stringExpression
operator|=
name|expression
expr_stmt|;
block|}
DECL|method|setName (String name)
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
DECL|method|setSpecial (Expression special)
specifier|public
name|void
name|setSpecial
parameter_list|(
name|Expression
name|special
parameter_list|)
block|{
name|this
operator|.
name|special
operator|=
name|special
expr_stmt|;
block|}
block|}
DECL|class|MyComponent
specifier|public
specifier|final
class|class
name|MyComponent
extends|extends
name|DefaultComponent
block|{
DECL|method|MyComponent (CamelContext context)
specifier|private
name|MyComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|MyEndpoint
name|result
init|=
operator|new
name|MyEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|setProperties
argument_list|(
name|result
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
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
literal|"myExpression"
argument_list|,
name|ExpressionBuilder
operator|.
name|bodyExpression
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
block|}
annotation|@
name|Test
DECL|method|testEmptyPath ()
specifier|public
name|void
name|testEmptyPath
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultComponent
name|component
init|=
operator|new
name|DefaultComponent
argument_list|(
name|context
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"foo://?name=Christian"
argument_list|,
name|uri
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|remaining
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|parameters
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Christian"
argument_list|,
name|parameters
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
decl_stmt|;
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"foo://?name=Christian"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testOnlyStringSetter ()
specifier|public
name|void
name|testOnlyStringSetter
parameter_list|()
throws|throws
name|Exception
block|{
name|MyComponent
name|component
init|=
operator|new
name|MyComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|MyEndpoint
name|endpoint
init|=
operator|(
name|MyEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"foo://?name=Claus"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Claus"
argument_list|,
name|endpoint
operator|.
name|name
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|expression
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|stringExpression
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCallStringSetter ()
specifier|public
name|void
name|testCallStringSetter
parameter_list|()
throws|throws
name|Exception
block|{
name|MyComponent
name|component
init|=
operator|new
name|MyComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|MyEndpoint
name|endpoint
init|=
operator|(
name|MyEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"foo://?expression=hello"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"hello"
argument_list|,
name|endpoint
operator|.
name|stringExpression
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|expression
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|name
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNoBeanInRegistryThenCallStringSetter ()
specifier|public
name|void
name|testNoBeanInRegistryThenCallStringSetter
parameter_list|()
throws|throws
name|Exception
block|{
name|MyComponent
name|component
init|=
operator|new
name|MyComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|MyEndpoint
name|endpoint
init|=
operator|(
name|MyEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"foo://?expression=#hello"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"#hello"
argument_list|,
name|endpoint
operator|.
name|stringExpression
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|expression
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|name
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCallExpressionSetter ()
specifier|public
name|void
name|testCallExpressionSetter
parameter_list|()
throws|throws
name|Exception
block|{
name|MyComponent
name|component
init|=
operator|new
name|MyComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|MyEndpoint
name|endpoint
init|=
operator|(
name|MyEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"foo://?expression=#myExpression"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|stringExpression
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|endpoint
operator|.
name|expression
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|name
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|endpoint
operator|.
name|expression
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCallSingleExpressionSetter ()
specifier|public
name|void
name|testCallSingleExpressionSetter
parameter_list|()
throws|throws
name|Exception
block|{
name|MyComponent
name|component
init|=
operator|new
name|MyComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|MyEndpoint
name|endpoint
init|=
operator|(
name|MyEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"foo://?special=#myExpression"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|stringExpression
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|expression
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|name
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|endpoint
operator|.
name|special
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|endpoint
operator|.
name|special
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testTypoInParameter ()
specifier|public
name|void
name|testTypoInParameter
parameter_list|()
throws|throws
name|Exception
block|{
name|MyComponent
name|component
init|=
operator|new
name|MyComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
try|try
block|{
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"foo://?xxxexpression=#hello"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have throw a ResolveEndpointFailedException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ResolveEndpointFailedException
name|e
parameter_list|)
block|{
comment|// ok
block|}
block|}
annotation|@
name|Test
DECL|method|testTypoInParameterValue ()
specifier|public
name|void
name|testTypoInParameterValue
parameter_list|()
throws|throws
name|Exception
block|{
name|MyComponent
name|component
init|=
operator|new
name|MyComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
try|try
block|{
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"foo://?special=#dummy"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have throw a IllegalArgumentException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
comment|// ok
block|}
block|}
block|}
end_class

end_unit
