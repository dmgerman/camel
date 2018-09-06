begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.issues
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|issues
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
name|model
operator|.
name|ProcessorDefinition
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
name|SplitDefinition
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
name|DelegateAsyncProcessor
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
name|InterceptStrategy
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
DECL|class|SplitWithInterceptorTest
specifier|public
class|class
name|SplitWithInterceptorTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|interceptStrategy
specifier|private
specifier|final
name|MyInterceptStrategy
name|interceptStrategy
init|=
operator|new
name|MyInterceptStrategy
argument_list|()
decl_stmt|;
DECL|class|MyInterceptStrategy
specifier|public
specifier|static
class|class
name|MyInterceptStrategy
implements|implements
name|InterceptStrategy
block|{
DECL|field|LOGGER
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|MyInterceptStrategy
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|doneCount
specifier|private
specifier|static
name|int
name|doneCount
decl_stmt|;
DECL|method|wrapProcessorInInterceptors (final CamelContext context, final ProcessorDefinition<?> definition, final Processor target, final Processor nextTarget)
specifier|public
name|Processor
name|wrapProcessorInInterceptors
parameter_list|(
specifier|final
name|CamelContext
name|context
parameter_list|,
specifier|final
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|definition
parameter_list|,
specifier|final
name|Processor
name|target
parameter_list|,
specifier|final
name|Processor
name|nextTarget
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|definition
operator|instanceof
name|SplitDefinition
condition|)
block|{
specifier|final
name|DelegateAsyncProcessor
name|delegateAsyncProcessor
init|=
operator|new
name|DelegateAsyncProcessor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|)
block|{
name|LOGGER
operator|.
name|info
argument_list|(
literal|"I'm doing someting"
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
operator|new
name|AsyncCallback
argument_list|()
block|{
specifier|public
name|void
name|done
parameter_list|(
specifier|final
name|boolean
name|doneSync
parameter_list|)
block|{
name|LOGGER
operator|.
name|info
argument_list|(
literal|"I'm done"
argument_list|)
expr_stmt|;
name|doneCount
operator|++
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
name|doneSync
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
return|;
block|}
block|}
decl_stmt|;
name|delegateAsyncProcessor
operator|.
name|setProcessor
argument_list|(
name|target
argument_list|)
expr_stmt|;
return|return
name|delegateAsyncProcessor
return|;
block|}
return|return
operator|new
name|DelegateAsyncProcessor
argument_list|(
name|target
argument_list|)
return|;
block|}
DECL|method|reset ()
specifier|public
name|void
name|reset
parameter_list|()
block|{
name|doneCount
operator|=
literal|0
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testSplitOne ()
specifier|public
name|void
name|testSplitOne
parameter_list|()
throws|throws
name|Exception
block|{
name|interceptStrategy
operator|.
name|reset
argument_list|()
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:line"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
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
name|assertEquals
argument_list|(
literal|"Done method shall be called only once"
argument_list|,
literal|1
argument_list|,
name|MyInterceptStrategy
operator|.
name|doneCount
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSplitTwo ()
specifier|public
name|void
name|testSplitTwo
parameter_list|()
throws|throws
name|Exception
block|{
name|interceptStrategy
operator|.
name|reset
argument_list|()
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:line"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World,Bye World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Done method shall be called only once"
argument_list|,
literal|1
argument_list|,
name|MyInterceptStrategy
operator|.
name|doneCount
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
name|context
operator|.
name|addInterceptStrategy
argument_list|(
name|interceptStrategy
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|split
argument_list|(
name|body
argument_list|()
operator|.
name|tokenize
argument_list|(
literal|","
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:line"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:line"
argument_list|)
operator|.
name|end
argument_list|()
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

