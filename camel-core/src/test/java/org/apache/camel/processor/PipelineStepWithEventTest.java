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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EventObject
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
name|NamedNode
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
name|impl
operator|.
name|event
operator|.
name|AbstractExchangeEvent
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
name|PipelineDefinition
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
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|ServiceSupport
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
name|StopWatch
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
comment|/**  * Test showing how you can use pipeline to group together statistics and implement your own event listener.  */
end_comment

begin_class
DECL|class|PipelineStepWithEventTest
specifier|public
class|class
name|PipelineStepWithEventTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|listener
specifier|private
specifier|final
name|MyStepEventListener
name|listener
init|=
operator|new
name|MyStepEventListener
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testPipelineStep ()
specifier|public
name|void
name|testPipelineStep
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:a"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:a2"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:b"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:b2"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
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
literal|4
argument_list|,
name|listener
operator|.
name|getEvents
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|BeforeStepEvent
name|event
init|=
operator|(
name|BeforeStepEvent
operator|)
name|listener
operator|.
name|getEvents
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"step-a"
argument_list|,
name|event
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|AfterStepEvent
name|event2
init|=
operator|(
name|AfterStepEvent
operator|)
name|listener
operator|.
name|getEvents
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"step-a"
argument_list|,
name|event2
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should take a little time"
argument_list|,
name|event2
operator|.
name|getTimeTaken
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
name|BeforeStepEvent
name|event3
init|=
operator|(
name|BeforeStepEvent
operator|)
name|listener
operator|.
name|getEvents
argument_list|()
operator|.
name|get
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"step-b"
argument_list|,
name|event3
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|AfterStepEvent
name|event4
init|=
operator|(
name|AfterStepEvent
operator|)
name|listener
operator|.
name|getEvents
argument_list|()
operator|.
name|get
argument_list|(
literal|3
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"step-b"
argument_list|,
name|event4
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should take a little time"
argument_list|,
name|event4
operator|.
name|getTimeTaken
argument_list|()
operator|>
literal|0
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
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|pipeline
argument_list|()
operator|.
name|id
argument_list|(
literal|"step-a"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:a"
argument_list|)
operator|.
name|delay
argument_list|(
name|constant
argument_list|(
literal|10
argument_list|)
argument_list|)
operator|.
name|end
argument_list|()
comment|// a bit ugly by need to end delay
operator|.
name|to
argument_list|(
literal|"mock:a2"
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|pipeline
argument_list|()
operator|.
name|id
argument_list|(
literal|"step-b"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:b"
argument_list|)
operator|.
name|delay
argument_list|(
name|constant
argument_list|(
literal|20
argument_list|)
argument_list|)
operator|.
name|end
argument_list|()
comment|// a bit ugly by need to end delay
operator|.
name|to
argument_list|(
literal|"mock:b2"
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
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|addInterceptStrategy
argument_list|(
operator|new
name|MyInterceptStrategy
argument_list|()
argument_list|)
expr_stmt|;
comment|// register the event listener
name|context
operator|.
name|addService
argument_list|(
name|listener
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
DECL|interface|StepEventListener
specifier|private
interface|interface
name|StepEventListener
block|{
DECL|method|beforeStep (BeforeStepEvent event)
name|void
name|beforeStep
parameter_list|(
name|BeforeStepEvent
name|event
parameter_list|)
function_decl|;
DECL|method|afterStep (AfterStepEvent event)
name|void
name|afterStep
parameter_list|(
name|AfterStepEvent
name|event
parameter_list|)
function_decl|;
block|}
DECL|class|MyStepEventListener
specifier|private
class|class
name|MyStepEventListener
extends|extends
name|ServiceSupport
implements|implements
name|StepEventListener
block|{
DECL|field|events
specifier|private
specifier|final
name|List
argument_list|<
name|EventObject
argument_list|>
name|events
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|beforeStep (BeforeStepEvent event)
specifier|public
name|void
name|beforeStep
parameter_list|(
name|BeforeStepEvent
name|event
parameter_list|)
block|{
name|events
operator|.
name|add
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|afterStep (AfterStepEvent event)
specifier|public
name|void
name|afterStep
parameter_list|(
name|AfterStepEvent
name|event
parameter_list|)
block|{
name|events
operator|.
name|add
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
DECL|method|getEvents ()
specifier|public
name|List
argument_list|<
name|EventObject
argument_list|>
name|getEvents
parameter_list|()
block|{
return|return
name|events
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
block|}
DECL|class|MyInterceptStrategy
specifier|private
class|class
name|MyInterceptStrategy
implements|implements
name|InterceptStrategy
block|{
annotation|@
name|Override
DECL|method|wrapProcessorInInterceptors (CamelContext context, NamedNode definition, Processor target, Processor nextTarget)
specifier|public
name|Processor
name|wrapProcessorInInterceptors
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|NamedNode
name|definition
parameter_list|,
name|Processor
name|target
parameter_list|,
name|Processor
name|nextTarget
parameter_list|)
throws|throws
name|Exception
block|{
comment|// grab the listener
name|StepEventListener
name|listener
init|=
name|context
operator|.
name|hasService
argument_list|(
name|StepEventListener
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// wrap the pipelines so we can emit events
if|if
condition|(
name|definition
operator|instanceof
name|PipelineDefinition
condition|)
block|{
return|return
operator|new
name|MyStepEventProcessor
argument_list|(
name|definition
operator|.
name|getId
argument_list|()
argument_list|,
name|target
argument_list|,
name|listener
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|target
return|;
block|}
block|}
block|}
DECL|class|MyStepEventProcessor
specifier|private
class|class
name|MyStepEventProcessor
extends|extends
name|DelegateAsyncProcessor
block|{
DECL|field|listener
specifier|private
specifier|final
name|StepEventListener
name|listener
decl_stmt|;
DECL|field|id
specifier|private
specifier|final
name|String
name|id
decl_stmt|;
DECL|method|MyStepEventProcessor (String id, Processor processor, StepEventListener listener)
specifier|public
name|MyStepEventProcessor
parameter_list|(
name|String
name|id
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|StepEventListener
name|listener
parameter_list|)
block|{
name|super
argument_list|(
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
name|this
operator|.
name|listener
operator|=
name|listener
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|process (final Exchange exchange, final AsyncCallback callback)
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
specifier|final
name|StopWatch
name|watch
init|=
operator|new
name|StopWatch
argument_list|()
decl_stmt|;
if|if
condition|(
name|listener
operator|!=
literal|null
condition|)
block|{
name|listener
operator|.
name|beforeStep
argument_list|(
operator|new
name|BeforeStepEvent
argument_list|(
name|exchange
argument_list|,
name|id
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|super
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|doneSync
lambda|->
block|{
if|if
condition|(
name|listener
operator|!=
literal|null
condition|)
block|{
name|listener
operator|.
name|afterStep
argument_list|(
operator|new
name|AfterStepEvent
argument_list|(
name|exchange
argument_list|,
name|id
argument_list|,
name|watch
operator|.
name|taken
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|callback
operator|.
name|done
argument_list|(
name|doneSync
argument_list|)
expr_stmt|;
block|}
argument_list|)
return|;
block|}
block|}
DECL|class|BeforeStepEvent
specifier|private
class|class
name|BeforeStepEvent
extends|extends
name|AbstractExchangeEvent
block|{
DECL|field|id
specifier|private
specifier|final
name|String
name|id
decl_stmt|;
DECL|method|BeforeStepEvent (Exchange source, String id)
specifier|public
name|BeforeStepEvent
parameter_list|(
name|Exchange
name|source
parameter_list|,
name|String
name|id
parameter_list|)
block|{
name|super
argument_list|(
name|source
argument_list|)
expr_stmt|;
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getType ()
specifier|public
name|Type
name|getType
parameter_list|()
block|{
return|return
name|Type
operator|.
name|Custom
return|;
block|}
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
block|}
DECL|class|AfterStepEvent
specifier|private
class|class
name|AfterStepEvent
extends|extends
name|AbstractExchangeEvent
block|{
DECL|field|id
specifier|private
specifier|final
name|String
name|id
decl_stmt|;
DECL|field|timeTaken
specifier|private
specifier|final
name|long
name|timeTaken
decl_stmt|;
DECL|method|AfterStepEvent (Exchange source, String id, long timeTaken)
specifier|public
name|AfterStepEvent
parameter_list|(
name|Exchange
name|source
parameter_list|,
name|String
name|id
parameter_list|,
name|long
name|timeTaken
parameter_list|)
block|{
name|super
argument_list|(
name|source
argument_list|)
expr_stmt|;
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
name|this
operator|.
name|timeTaken
operator|=
name|timeTaken
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getType ()
specifier|public
name|Type
name|getType
parameter_list|()
block|{
return|return
name|Type
operator|.
name|Custom
return|;
block|}
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
DECL|method|getTimeTaken ()
specifier|public
name|long
name|getTimeTaken
parameter_list|()
block|{
return|return
name|timeTaken
return|;
block|}
block|}
block|}
end_class

end_unit

