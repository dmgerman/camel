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
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|EventDrivenConsumerRoute
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
name|model
operator|.
name|RouteDefinition
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
name|processor
operator|.
name|interceptor
operator|.
name|StreamCachingInterceptor
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
name|ObjectHelper
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
name|ProcessorDefinitionHelper
import|;
end_import

begin_comment
comment|/**  * Unit test based on user forum problem - CAMEL-1463.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|ChoiceNoErrorHandlerTest
specifier|public
class|class
name|ChoiceNoErrorHandlerTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|jmx
specifier|private
specifier|static
name|boolean
name|jmx
init|=
literal|true
decl_stmt|;
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
comment|// we must enable/disable JMX in this setUp
if|if
condition|(
name|jmx
condition|)
block|{
name|enableJMX
argument_list|()
expr_stmt|;
name|jmx
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|disableJMX
argument_list|()
expr_stmt|;
block|}
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
DECL|method|testChoiceNoErrorHandler ()
specifier|public
name|void
name|testChoiceNoErrorHandler
parameter_list|()
throws|throws
name|Exception
block|{
name|doTest
argument_list|()
expr_stmt|;
block|}
DECL|method|testChoiceNoErrorHandlerJMXDisabled ()
specifier|public
name|void
name|testChoiceNoErrorHandlerJMXDisabled
parameter_list|()
throws|throws
name|Exception
block|{
name|doTest
argument_list|()
expr_stmt|;
block|}
DECL|method|doTest ()
specifier|private
name|void
name|doTest
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|,
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// there should be no error handlers and no stream cache
for|for
control|(
name|RouteDefinition
name|route
range|:
name|context
operator|.
name|getRouteDefinitions
argument_list|()
control|)
block|{
name|ErrorHandler
name|error
init|=
name|ProcessorDefinitionHelper
operator|.
name|findFirstTypeInOutputs
argument_list|(
name|route
operator|.
name|getOutputs
argument_list|()
argument_list|,
name|DeadLetterChannel
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
literal|"There should be no error handler"
argument_list|,
name|error
argument_list|)
expr_stmt|;
block|}
comment|// there should be no error handlers and no stream cache
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
name|route
operator|instanceof
name|EventDrivenConsumerRoute
condition|)
block|{
name|EventDrivenConsumerRoute
name|consumer
init|=
operator|(
name|EventDrivenConsumerRoute
operator|)
name|route
decl_stmt|;
name|StreamCachingInterceptor
name|cache
init|=
name|findProceesorInRoute
argument_list|(
name|consumer
operator|.
name|getProcessor
argument_list|()
argument_list|,
name|StreamCachingInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"There should be stream cache found: "
operator|+
name|cache
argument_list|,
name|cache
argument_list|)
expr_stmt|;
name|ErrorHandler
name|error
init|=
name|findProceesorInRoute
argument_list|(
name|consumer
operator|.
name|getProcessor
argument_list|()
argument_list|,
name|ErrorHandler
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
literal|"There should be no error handler found: "
operator|+
name|error
argument_list|,
name|error
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|findProceesorInRoute (Processor route, Class<T> type)
specifier|private
parameter_list|<
name|T
parameter_list|>
name|T
name|findProceesorInRoute
parameter_list|(
name|Processor
name|route
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
if|if
condition|(
name|route
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|type
operator|.
name|isInstance
argument_list|(
name|route
argument_list|)
condition|)
block|{
return|return
name|type
operator|.
name|cast
argument_list|(
name|route
argument_list|)
return|;
block|}
try|try
block|{
name|Method
name|m
init|=
name|route
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"getProcessor"
argument_list|)
decl_stmt|;
name|Processor
name|child
init|=
operator|(
name|Processor
operator|)
name|ObjectHelper
operator|.
name|invokeMethod
argument_list|(
name|m
argument_list|,
name|route
argument_list|)
decl_stmt|;
comment|// look its children
return|return
name|findProceesorInRoute
argument_list|(
name|child
argument_list|,
name|type
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
try|try
block|{
name|Method
name|m
init|=
name|route
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"getProcessors"
argument_list|)
decl_stmt|;
comment|// look its children
name|Collection
argument_list|<
name|Processor
argument_list|>
name|children
init|=
operator|(
name|Collection
argument_list|<
name|Processor
argument_list|>
operator|)
name|ObjectHelper
operator|.
name|invokeMethod
argument_list|(
name|m
argument_list|,
name|route
argument_list|)
decl_stmt|;
for|for
control|(
name|Processor
name|child
range|:
name|children
control|)
block|{
name|T
name|out
init|=
name|findProceesorInRoute
argument_list|(
name|child
argument_list|,
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|out
operator|!=
literal|null
condition|)
block|{
return|return
name|out
return|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
try|try
block|{
name|Method
name|m
init|=
name|route
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"getFilters"
argument_list|)
decl_stmt|;
comment|// look its children
name|List
argument_list|<
name|FilterProcessor
argument_list|>
name|children
init|=
operator|(
name|List
argument_list|<
name|FilterProcessor
argument_list|>
operator|)
name|ObjectHelper
operator|.
name|invokeMethod
argument_list|(
name|m
argument_list|,
name|route
argument_list|)
decl_stmt|;
for|for
control|(
name|Processor
name|child
range|:
name|children
control|)
block|{
name|T
name|out
init|=
name|findProceesorInRoute
argument_list|(
name|child
argument_list|,
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|out
operator|!=
literal|null
condition|)
block|{
return|return
name|out
return|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
try|try
block|{
name|Method
name|m
init|=
name|route
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"getOtherwise"
argument_list|)
decl_stmt|;
name|Processor
name|child
init|=
operator|(
name|Processor
operator|)
name|ObjectHelper
operator|.
name|invokeMethod
argument_list|(
name|m
argument_list|,
name|route
argument_list|)
decl_stmt|;
comment|// look its children
return|return
name|findProceesorInRoute
argument_list|(
name|child
argument_list|,
name|type
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
return|return
literal|null
return|;
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
name|errorHandler
argument_list|(
name|noErrorHandler
argument_list|()
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|(
name|header
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:end"
argument_list|)
operator|.
name|otherwise
argument_list|()
operator|.
name|end
argument_list|()
expr_stmt|;
name|from
argument_list|(
literal|"direct:end"
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
block|{
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
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

