begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
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
name|Predicate
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
name|processor
operator|.
name|CompositeProcessor
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
name|InterceptorProcessor
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
name|MulticastProcessor
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
name|Pipeline
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
name|RecipientList
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

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|FromBuilder
specifier|public
class|class
name|FromBuilder
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
extends|extends
name|BuilderSupport
argument_list|<
name|E
argument_list|>
implements|implements
name|ProcessorFactory
argument_list|<
name|E
argument_list|>
block|{
DECL|field|builder
specifier|private
name|RouteBuilder
argument_list|<
name|E
argument_list|>
name|builder
decl_stmt|;
DECL|field|from
specifier|private
name|Endpoint
argument_list|<
name|E
argument_list|>
name|from
decl_stmt|;
DECL|field|processors
specifier|private
name|List
argument_list|<
name|Processor
argument_list|<
name|E
argument_list|>
argument_list|>
name|processors
init|=
operator|new
name|ArrayList
argument_list|<
name|Processor
argument_list|<
name|E
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|processFactories
specifier|private
name|List
argument_list|<
name|ProcessorFactory
argument_list|<
name|E
argument_list|>
argument_list|>
name|processFactories
init|=
operator|new
name|ArrayList
argument_list|<
name|ProcessorFactory
argument_list|<
name|E
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|FromBuilder (RouteBuilder<E> builder, Endpoint<E> from)
specifier|public
name|FromBuilder
parameter_list|(
name|RouteBuilder
argument_list|<
name|E
argument_list|>
name|builder
parameter_list|,
name|Endpoint
argument_list|<
name|E
argument_list|>
name|from
parameter_list|)
block|{
name|super
argument_list|(
name|builder
argument_list|)
expr_stmt|;
name|this
operator|.
name|builder
operator|=
name|builder
expr_stmt|;
name|this
operator|.
name|from
operator|=
name|from
expr_stmt|;
block|}
DECL|method|FromBuilder (FromBuilder<E> parent)
specifier|public
name|FromBuilder
parameter_list|(
name|FromBuilder
argument_list|<
name|E
argument_list|>
name|parent
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|)
expr_stmt|;
name|this
operator|.
name|builder
operator|=
name|parent
operator|.
name|getBuilder
argument_list|()
expr_stmt|;
name|this
operator|.
name|from
operator|=
name|parent
operator|.
name|getFrom
argument_list|()
expr_stmt|;
block|}
comment|/**      * Sends the exchange to the given endpoint URI      */
DECL|method|to (String uri)
specifier|public
name|ProcessorFactory
argument_list|<
name|E
argument_list|>
name|to
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
return|return
name|to
argument_list|(
name|endpoint
argument_list|(
name|uri
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Sends the exchange to the given endpoint      */
DECL|method|to (Endpoint<E> endpoint)
specifier|public
name|ProcessorFactory
argument_list|<
name|E
argument_list|>
name|to
parameter_list|(
name|Endpoint
argument_list|<
name|E
argument_list|>
name|endpoint
parameter_list|)
block|{
name|ToBuilder
argument_list|<
name|E
argument_list|>
name|answer
init|=
operator|new
name|ToBuilder
argument_list|<
name|E
argument_list|>
argument_list|(
name|this
argument_list|,
name|endpoint
argument_list|)
decl_stmt|;
name|addProcessBuilder
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
comment|/**      * Sends the exchange to a list of endpoints using the {@link MulticastProcessor} pattern      */
DECL|method|to (String... uris)
specifier|public
name|ProcessorFactory
argument_list|<
name|E
argument_list|>
name|to
parameter_list|(
name|String
modifier|...
name|uris
parameter_list|)
block|{
return|return
name|to
argument_list|(
name|endpoints
argument_list|(
name|uris
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Sends the exchange to a list of endpoints using the {@link MulticastProcessor} pattern      */
DECL|method|to (Endpoint<E>.... endpoints)
specifier|public
name|ProcessorFactory
argument_list|<
name|E
argument_list|>
name|to
parameter_list|(
name|Endpoint
argument_list|<
name|E
argument_list|>
modifier|...
name|endpoints
parameter_list|)
block|{
return|return
name|to
argument_list|(
name|endpoints
argument_list|(
name|endpoints
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Sends the exchange to a list of endpoint using the {@link MulticastProcessor} pattern      */
DECL|method|to (Collection<Endpoint<E>> endpoints)
specifier|public
name|ProcessorFactory
argument_list|<
name|E
argument_list|>
name|to
parameter_list|(
name|Collection
argument_list|<
name|Endpoint
argument_list|<
name|E
argument_list|>
argument_list|>
name|endpoints
parameter_list|)
block|{
return|return
name|addProcessBuilder
argument_list|(
operator|new
name|MulticastBuilder
argument_list|<
name|E
argument_list|>
argument_list|(
name|this
argument_list|,
name|endpoints
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Creates a {@link Pipeline} of the list of endpoints so that the message will get processed by each endpoint in turn      * and for request/response the output of one endpoint will be the input of the next endpoint      */
DECL|method|pipeline (String... uris)
specifier|public
name|ProcessorFactory
argument_list|<
name|E
argument_list|>
name|pipeline
parameter_list|(
name|String
modifier|...
name|uris
parameter_list|)
block|{
return|return
name|pipeline
argument_list|(
name|endpoints
argument_list|(
name|uris
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Creates a {@link Pipeline} of the list of endpoints so that the message will get processed by each endpoint in turn      * and for request/response the output of one endpoint will be the input of the next endpoint      */
DECL|method|pipeline (Endpoint<E>.... endpoints)
specifier|public
name|ProcessorFactory
argument_list|<
name|E
argument_list|>
name|pipeline
parameter_list|(
name|Endpoint
argument_list|<
name|E
argument_list|>
modifier|...
name|endpoints
parameter_list|)
block|{
return|return
name|pipeline
argument_list|(
name|endpoints
argument_list|(
name|endpoints
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Creates a {@link Pipeline} of the list of endpoints so that the message will get processed by each endpoint in turn      * and for request/response the output of one endpoint will be the input of the next endpoint      */
DECL|method|pipeline (Collection<Endpoint<E>> endpoints)
specifier|public
name|ProcessorFactory
argument_list|<
name|E
argument_list|>
name|pipeline
parameter_list|(
name|Collection
argument_list|<
name|Endpoint
argument_list|<
name|E
argument_list|>
argument_list|>
name|endpoints
parameter_list|)
block|{
return|return
name|addProcessBuilder
argument_list|(
operator|new
name|PipelineBuilder
argument_list|<
name|E
argument_list|>
argument_list|(
name|this
argument_list|,
name|endpoints
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Adds the custom processor to this destination      */
DECL|method|process (Processor<E> processor)
specifier|public
name|ConstantProcessorBuilder
argument_list|<
name|E
argument_list|>
name|process
parameter_list|(
name|Processor
argument_list|<
name|E
argument_list|>
name|processor
parameter_list|)
block|{
name|ConstantProcessorBuilder
argument_list|<
name|E
argument_list|>
name|answer
init|=
operator|new
name|ConstantProcessorBuilder
argument_list|<
name|E
argument_list|>
argument_list|(
name|processor
argument_list|)
decl_stmt|;
name|addProcessBuilder
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
comment|/**      * Creates a predicate which is applied and only if it is true then      * the exchange is forwarded to the destination      *      * @return the builder for a predicate      */
DECL|method|filter (Predicate<E> predicate)
specifier|public
name|FilterBuilder
argument_list|<
name|E
argument_list|>
name|filter
parameter_list|(
name|Predicate
argument_list|<
name|E
argument_list|>
name|predicate
parameter_list|)
block|{
name|FilterBuilder
argument_list|<
name|E
argument_list|>
name|answer
init|=
operator|new
name|FilterBuilder
argument_list|<
name|E
argument_list|>
argument_list|(
name|this
argument_list|,
name|predicate
argument_list|)
decl_stmt|;
name|addProcessBuilder
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
comment|/**      * Creates a choice of one or more predicates with an otherwise clause      *      * @return the builder for a choice expression      */
DECL|method|choice ()
specifier|public
name|ChoiceBuilder
argument_list|<
name|E
argument_list|>
name|choice
parameter_list|()
block|{
name|ChoiceBuilder
argument_list|<
name|E
argument_list|>
name|answer
init|=
operator|new
name|ChoiceBuilder
argument_list|<
name|E
argument_list|>
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|addProcessBuilder
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
comment|/**      * Creates a dynamic<a href="http://activemq.apache.org/camel/recipient-list.html">Recipient List</a> pattern.      *      * @param valueBuilder is the builder of the expression used in the {@link RecipientList} to decide the destinations      */
DECL|method|recipientList (ValueBuilder<E> valueBuilder)
specifier|public
name|RecipientListBuilder
argument_list|<
name|E
argument_list|>
name|recipientList
parameter_list|(
name|ValueBuilder
argument_list|<
name|E
argument_list|>
name|valueBuilder
parameter_list|)
block|{
name|RecipientListBuilder
argument_list|<
name|E
argument_list|>
name|answer
init|=
operator|new
name|RecipientListBuilder
argument_list|<
name|E
argument_list|>
argument_list|(
name|this
argument_list|,
name|valueBuilder
argument_list|)
decl_stmt|;
name|addProcessBuilder
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
comment|/**      * A builder for the<a href="http://activemq.apache.org/camel/splitter.html">Splitter</a> pattern      * where an expression is evaluated to iterate through each of the parts of a message and then each part is then send to some endpoint.      *      * @param valueBuilder the builder for the value used as the expression on which to split      * @return the builder      */
DECL|method|splitter (ValueBuilder<E> valueBuilder)
specifier|public
name|SplitterBuilder
argument_list|<
name|E
argument_list|>
name|splitter
parameter_list|(
name|ValueBuilder
argument_list|<
name|E
argument_list|>
name|valueBuilder
parameter_list|)
block|{
name|SplitterBuilder
argument_list|<
name|E
argument_list|>
name|answer
init|=
operator|new
name|SplitterBuilder
argument_list|<
name|E
argument_list|>
argument_list|(
name|this
argument_list|,
name|valueBuilder
argument_list|)
decl_stmt|;
name|addProcessBuilder
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
comment|/**      * Installs the given error handler builder      *      * @param errorHandlerBuilder the error handler to be used by default for all child routes      * @return the current builder with the error handler configured      */
DECL|method|errorHandler (ErrorHandlerBuilder errorHandlerBuilder)
specifier|public
name|FromBuilder
argument_list|<
name|E
argument_list|>
name|errorHandler
parameter_list|(
name|ErrorHandlerBuilder
name|errorHandlerBuilder
parameter_list|)
block|{
name|setErrorHandlerBuilder
argument_list|(
name|errorHandlerBuilder
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|// Properties
comment|//-------------------------------------------------------------------------
DECL|method|getBuilder ()
specifier|public
name|RouteBuilder
argument_list|<
name|E
argument_list|>
name|getBuilder
parameter_list|()
block|{
return|return
name|builder
return|;
block|}
DECL|method|getFrom ()
specifier|public
name|Endpoint
argument_list|<
name|E
argument_list|>
name|getFrom
parameter_list|()
block|{
return|return
name|from
return|;
block|}
DECL|method|addProcessBuilder (ProcessorFactory<E> processFactory)
specifier|public
name|ProcessorFactory
argument_list|<
name|E
argument_list|>
name|addProcessBuilder
parameter_list|(
name|ProcessorFactory
argument_list|<
name|E
argument_list|>
name|processFactory
parameter_list|)
block|{
name|processFactories
operator|.
name|add
argument_list|(
name|processFactory
argument_list|)
expr_stmt|;
return|return
name|processFactory
return|;
block|}
DECL|method|addProcessor (Processor<E> processor)
specifier|public
name|void
name|addProcessor
parameter_list|(
name|Processor
argument_list|<
name|E
argument_list|>
name|processor
parameter_list|)
block|{
name|processors
operator|.
name|add
argument_list|(
name|processor
argument_list|)
expr_stmt|;
block|}
DECL|method|createProcessor ()
specifier|public
name|Processor
argument_list|<
name|E
argument_list|>
name|createProcessor
parameter_list|()
block|{
name|List
argument_list|<
name|Processor
argument_list|<
name|E
argument_list|>
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<
name|Processor
argument_list|<
name|E
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|ProcessorFactory
argument_list|<
name|E
argument_list|>
name|processFactory
range|:
name|processFactories
control|)
block|{
name|Processor
argument_list|<
name|E
argument_list|>
name|processor
init|=
name|makeProcessor
argument_list|(
name|processFactory
argument_list|)
decl_stmt|;
if|if
condition|(
name|processor
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No processor created for processBuilder: "
operator|+
name|processFactory
argument_list|)
throw|;
block|}
name|answer
operator|.
name|add
argument_list|(
name|processor
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|answer
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|answer
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
return|return
name|answer
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|CompositeProcessor
argument_list|<
name|E
argument_list|>
argument_list|(
name|answer
argument_list|)
return|;
block|}
block|}
comment|/**      * Creates the processor and wraps it in any necessary interceptors and error handlers      */
DECL|method|makeProcessor (ProcessorFactory<E> processFactory)
specifier|protected
name|Processor
argument_list|<
name|E
argument_list|>
name|makeProcessor
parameter_list|(
name|ProcessorFactory
argument_list|<
name|E
argument_list|>
name|processFactory
parameter_list|)
block|{
name|Processor
argument_list|<
name|E
argument_list|>
name|processor
init|=
name|processFactory
operator|.
name|createProcessor
argument_list|()
decl_stmt|;
return|return
name|getErrorHandlerBuilder
argument_list|()
operator|.
name|createErrorHandler
argument_list|(
name|processor
argument_list|)
return|;
block|}
DECL|method|getProcessors ()
specifier|public
name|List
argument_list|<
name|Processor
argument_list|<
name|E
argument_list|>
argument_list|>
name|getProcessors
parameter_list|()
block|{
return|return
name|processors
return|;
block|}
DECL|method|intercept ()
specifier|public
name|InterceptorBuilder
argument_list|<
name|E
argument_list|>
name|intercept
parameter_list|()
block|{
name|InterceptorBuilder
argument_list|<
name|E
argument_list|>
name|answer
init|=
operator|new
name|InterceptorBuilder
argument_list|<
name|E
argument_list|>
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|addProcessBuilder
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|intercept (InterceptorProcessor<E> interceptor)
specifier|public
name|InterceptorBuilder
argument_list|<
name|E
argument_list|>
name|intercept
parameter_list|(
name|InterceptorProcessor
argument_list|<
name|E
argument_list|>
name|interceptor
parameter_list|)
block|{
name|InterceptorBuilder
argument_list|<
name|E
argument_list|>
name|answer
init|=
operator|new
name|InterceptorBuilder
argument_list|<
name|E
argument_list|>
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|answer
operator|.
name|add
argument_list|(
name|interceptor
argument_list|)
expr_stmt|;
name|addProcessBuilder
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

