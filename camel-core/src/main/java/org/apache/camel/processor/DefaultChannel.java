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
name|Channel
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
name|impl
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
name|spi
operator|.
name|RouteContext
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
name|ServiceHelper
import|;
end_import

begin_comment
comment|/**  * DefaultChannel is the default {@link Channel}.  *<p/>  * The current implementation is just a composite containing the interceptors and error handler  * that beforehand was added to the route graph directly.  *<br/>  * With this {@link Channel} we can in the future implement better strategies for routing the  * {@link Exchange} in the route graph, as we have a {@link Channel} between each and every node  * in the graph.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|DefaultChannel
specifier|public
class|class
name|DefaultChannel
extends|extends
name|ServiceSupport
implements|implements
name|Processor
implements|,
name|Channel
block|{
DECL|field|interceptors
specifier|private
specifier|final
name|List
argument_list|<
name|InterceptStrategy
argument_list|>
name|interceptors
init|=
operator|new
name|ArrayList
argument_list|<
name|InterceptStrategy
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|errorHandler
specifier|private
name|Processor
name|errorHandler
decl_stmt|;
comment|// the next processor (non wrapped)
DECL|field|nextProcessor
specifier|private
name|Processor
name|nextProcessor
decl_stmt|;
comment|// the real output to invoke that has been wrapped
DECL|field|output
specifier|private
name|Processor
name|output
decl_stmt|;
DECL|field|definition
specifier|private
name|ProcessorDefinition
name|definition
decl_stmt|;
DECL|method|next ()
specifier|public
name|List
argument_list|<
name|Processor
argument_list|>
name|next
parameter_list|()
block|{
name|List
argument_list|<
name|Processor
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<
name|Processor
argument_list|>
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|answer
operator|.
name|add
argument_list|(
name|nextProcessor
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|hasNext ()
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|nextProcessor
operator|!=
literal|null
return|;
block|}
DECL|method|setNextProcessor (Processor next)
specifier|public
name|void
name|setNextProcessor
parameter_list|(
name|Processor
name|next
parameter_list|)
block|{
name|this
operator|.
name|nextProcessor
operator|=
name|next
expr_stmt|;
block|}
DECL|method|getOutput ()
specifier|public
name|Processor
name|getOutput
parameter_list|()
block|{
comment|// the errorHandler is already decorated with interceptors
comment|// so it cointain the entire chain of processors, so we can safely use it directly as output
comment|// if no error handler provided we can use the output direcly
return|return
name|errorHandler
operator|!=
literal|null
condition|?
name|errorHandler
else|:
name|output
return|;
block|}
DECL|method|setOutput (Processor output)
specifier|public
name|void
name|setOutput
parameter_list|(
name|Processor
name|output
parameter_list|)
block|{
name|this
operator|.
name|output
operator|=
name|output
expr_stmt|;
block|}
DECL|method|getNextProcessor ()
specifier|public
name|Processor
name|getNextProcessor
parameter_list|()
block|{
return|return
name|nextProcessor
return|;
block|}
DECL|method|hasInterceptorStrategy (Class type)
specifier|public
name|boolean
name|hasInterceptorStrategy
parameter_list|(
name|Class
name|type
parameter_list|)
block|{
for|for
control|(
name|InterceptStrategy
name|strategy
range|:
name|interceptors
control|)
block|{
if|if
condition|(
name|type
operator|.
name|isInstance
argument_list|(
name|strategy
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
DECL|method|setErrorHandler (Processor errorHandler)
specifier|public
name|void
name|setErrorHandler
parameter_list|(
name|Processor
name|errorHandler
parameter_list|)
block|{
name|this
operator|.
name|errorHandler
operator|=
name|errorHandler
expr_stmt|;
block|}
DECL|method|getErrorHandler ()
specifier|public
name|Processor
name|getErrorHandler
parameter_list|()
block|{
return|return
name|errorHandler
return|;
block|}
DECL|method|addInterceptStrategy (InterceptStrategy strategy)
specifier|public
name|void
name|addInterceptStrategy
parameter_list|(
name|InterceptStrategy
name|strategy
parameter_list|)
block|{
name|interceptors
operator|.
name|add
argument_list|(
name|strategy
argument_list|)
expr_stmt|;
block|}
DECL|method|addInterceptStrategies (List<InterceptStrategy> strategies)
specifier|public
name|void
name|addInterceptStrategies
parameter_list|(
name|List
argument_list|<
name|InterceptStrategy
argument_list|>
name|strategies
parameter_list|)
block|{
name|interceptors
operator|.
name|addAll
argument_list|(
name|strategies
argument_list|)
expr_stmt|;
block|}
DECL|method|getInterceptStrategies ()
specifier|public
name|List
argument_list|<
name|InterceptStrategy
argument_list|>
name|getInterceptStrategies
parameter_list|()
block|{
return|return
name|interceptors
return|;
block|}
DECL|method|getProcessorDefinition ()
specifier|public
name|ProcessorDefinition
name|getProcessorDefinition
parameter_list|()
block|{
return|return
name|definition
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
name|ServiceHelper
operator|.
name|startServices
argument_list|(
name|errorHandler
argument_list|,
name|output
argument_list|)
expr_stmt|;
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
name|ServiceHelper
operator|.
name|stopServices
argument_list|(
name|output
argument_list|,
name|errorHandler
argument_list|)
expr_stmt|;
block|}
DECL|method|initChannel (ProcessorDefinition outputDefinition, RouteContext routeContext)
specifier|public
name|void
name|initChannel
parameter_list|(
name|ProcessorDefinition
name|outputDefinition
parameter_list|,
name|RouteContext
name|routeContext
parameter_list|)
throws|throws
name|Exception
block|{
name|this
operator|.
name|definition
operator|=
name|outputDefinition
expr_stmt|;
comment|// TODO: Support ordering of interceptors
comment|// wrap the output with the interceptors
name|Processor
name|target
init|=
name|nextProcessor
decl_stmt|;
for|for
control|(
name|InterceptStrategy
name|strategy
range|:
name|interceptors
control|)
block|{
name|target
operator|=
name|strategy
operator|.
name|wrapProcessorInInterceptors
argument_list|(
name|outputDefinition
argument_list|,
name|target
argument_list|,
name|nextProcessor
argument_list|)
expr_stmt|;
block|}
comment|// sets the delegate to our wrapped output
name|output
operator|=
name|target
expr_stmt|;
block|}
DECL|method|process (Exchange exchange)
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
name|Processor
name|processor
init|=
name|getOutput
argument_list|()
decl_stmt|;
if|if
condition|(
name|processor
operator|!=
literal|null
condition|)
block|{
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
comment|// just output the next processor as all the interceptors and error handler is just too verbose
return|return
literal|"Channel["
operator|+
name|nextProcessor
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

