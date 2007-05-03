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
name|DelegateProcessor
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|idempotent
operator|.
name|IdempotentConsumer
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
name|idempotent
operator|.
name|MessageIdRepository
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
name|Policy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
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
extends|extends
name|BuilderSupport
implements|implements
name|ProcessorFactory
block|{
DECL|field|DEFAULT_TRACE_CATEGORY
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_TRACE_CATEGORY
init|=
literal|"org.apache.camel.TRACE"
decl_stmt|;
DECL|field|builder
specifier|private
name|RouteBuilder
name|builder
decl_stmt|;
DECL|field|from
specifier|private
name|Endpoint
name|from
decl_stmt|;
DECL|field|processors
specifier|private
name|List
argument_list|<
name|Processor
argument_list|>
name|processors
init|=
operator|new
name|ArrayList
argument_list|<
name|Processor
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|processFactories
specifier|private
name|List
argument_list|<
name|ProcessorFactory
argument_list|>
name|processFactories
init|=
operator|new
name|ArrayList
argument_list|<
name|ProcessorFactory
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|FromBuilder (RouteBuilder builder, Endpoint from)
specifier|public
name|FromBuilder
parameter_list|(
name|RouteBuilder
name|builder
parameter_list|,
name|Endpoint
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
DECL|method|FromBuilder (FromBuilder parent)
specifier|public
name|FromBuilder
parameter_list|(
name|FromBuilder
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
annotation|@
name|Fluent
DECL|method|to (@luentArgR)String uri)
specifier|public
name|ProcessorFactory
name|to
parameter_list|(
annotation|@
name|FluentArg
argument_list|(
literal|"uri"
argument_list|)
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
annotation|@
name|Fluent
DECL|method|to (@luentArgR)Endpoint endpoint)
specifier|public
name|ProcessorFactory
name|to
parameter_list|(
annotation|@
name|FluentArg
argument_list|(
literal|"ref"
argument_list|)
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|ToBuilder
name|answer
init|=
operator|new
name|ToBuilder
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
annotation|@
name|Fluent
DECL|method|to (String... uris)
specifier|public
name|ProcessorFactory
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
annotation|@
name|Fluent
DECL|method|to ( @luentArgvalue = R, attribute = false, element = true) Endpoint... endpoints)
specifier|public
name|ProcessorFactory
name|to
parameter_list|(
annotation|@
name|FluentArg
argument_list|(
name|value
operator|=
literal|"endpoint"
argument_list|,
name|attribute
operator|=
literal|false
argument_list|,
name|element
operator|=
literal|true
argument_list|)
name|Endpoint
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
annotation|@
name|Fluent
DECL|method|to (@luentArgvalue = R, attribute = false, element = true) Collection<Endpoint> endpoints)
specifier|public
name|ProcessorFactory
name|to
parameter_list|(
annotation|@
name|FluentArg
argument_list|(
name|value
operator|=
literal|"endpoint"
argument_list|,
name|attribute
operator|=
literal|false
argument_list|,
name|element
operator|=
literal|true
argument_list|)
name|Collection
argument_list|<
name|Endpoint
argument_list|>
name|endpoints
parameter_list|)
block|{
return|return
name|addProcessBuilder
argument_list|(
operator|new
name|MulticastBuilder
argument_list|(
name|this
argument_list|,
name|endpoints
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Creates a {@link Pipeline} of the list of endpoints so that the message will get processed by each endpoint in turn      * and for request/response the output of one endpoint will be the input of the next endpoint      */
annotation|@
name|Fluent
DECL|method|pipeline (@luentArgR)String... uris)
specifier|public
name|ProcessorFactory
name|pipeline
parameter_list|(
annotation|@
name|FluentArg
argument_list|(
literal|"uris"
argument_list|)
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
annotation|@
name|Fluent
DECL|method|pipeline (@luentArgR)Endpoint... endpoints)
specifier|public
name|ProcessorFactory
name|pipeline
parameter_list|(
annotation|@
name|FluentArg
argument_list|(
literal|"endpoints"
argument_list|)
name|Endpoint
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
annotation|@
name|Fluent
DECL|method|pipeline (@luentArgR)Collection<Endpoint> endpoints)
specifier|public
name|ProcessorFactory
name|pipeline
parameter_list|(
annotation|@
name|FluentArg
argument_list|(
literal|"endpoints"
argument_list|)
name|Collection
argument_list|<
name|Endpoint
argument_list|>
name|endpoints
parameter_list|)
block|{
return|return
name|addProcessBuilder
argument_list|(
operator|new
name|PipelineBuilder
argument_list|(
name|this
argument_list|,
name|endpoints
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Creates an {@link IdempotentConsumer} to avoid duplicate messages      */
annotation|@
name|Fluent
DECL|method|idempotentConsumer ( @luentArgR)Expression messageIdExpression, @FluentArg(R)MessageIdRepository messageIdRepository)
specifier|public
name|IdempotentConsumerBuilder
name|idempotentConsumer
parameter_list|(
annotation|@
name|FluentArg
argument_list|(
literal|"messageIdExpression"
argument_list|)
name|Expression
name|messageIdExpression
parameter_list|,
annotation|@
name|FluentArg
argument_list|(
literal|"MessageIdRepository"
argument_list|)
name|MessageIdRepository
name|messageIdRepository
parameter_list|)
block|{
return|return
operator|(
name|IdempotentConsumerBuilder
operator|)
name|addProcessBuilder
argument_list|(
operator|new
name|IdempotentConsumerBuilder
argument_list|(
name|this
argument_list|,
name|messageIdExpression
argument_list|,
name|messageIdRepository
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Creates a predicate which is applied and only if it is true then      * the exchange is forwarded to the destination      *      * @return the builder for a predicate      */
annotation|@
name|Fluent
DECL|method|filter ( @luentArgvalue = R, element = true) Predicate predicate)
specifier|public
name|FilterBuilder
name|filter
parameter_list|(
annotation|@
name|FluentArg
argument_list|(
name|value
operator|=
literal|"predicate"
argument_list|,
name|element
operator|=
literal|true
argument_list|)
name|Predicate
name|predicate
parameter_list|)
block|{
name|FilterBuilder
name|answer
init|=
operator|new
name|FilterBuilder
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
annotation|@
name|Fluent
argument_list|(
name|nestedActions
operator|=
literal|true
argument_list|)
DECL|method|choice ()
specifier|public
name|ChoiceBuilder
name|choice
parameter_list|()
block|{
name|ChoiceBuilder
name|answer
init|=
operator|new
name|ChoiceBuilder
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
comment|/**      * Creates a dynamic<a href="http://activemq.apache.org/camel/recipient-list.html">Recipient List</a> pattern.      *      * @param receipients is the builder of the expression used in the {@link RecipientList} to decide the destinations      */
annotation|@
name|Fluent
DECL|method|recipientList ( @luentArgvalue = R, element = true) ValueBuilder receipients)
specifier|public
name|RecipientListBuilder
name|recipientList
parameter_list|(
annotation|@
name|FluentArg
argument_list|(
name|value
operator|=
literal|"recipients"
argument_list|,
name|element
operator|=
literal|true
argument_list|)
name|ValueBuilder
name|receipients
parameter_list|)
block|{
name|RecipientListBuilder
name|answer
init|=
operator|new
name|RecipientListBuilder
argument_list|(
name|this
argument_list|,
name|receipients
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
comment|/**      * A builder for the<a href="http://activemq.apache.org/camel/splitter.html">Splitter</a> pattern      * where an expression is evaluated to iterate through each of the parts of a message and then each part is then send to some endpoint.      *      * @param receipients the builder for the value used as the expression on which to split      * @return the builder      */
annotation|@
name|Fluent
DECL|method|splitter (@luentArgvalue = R, element = true)ValueBuilder receipients)
specifier|public
name|SplitterBuilder
name|splitter
parameter_list|(
annotation|@
name|FluentArg
argument_list|(
name|value
operator|=
literal|"recipients"
argument_list|,
name|element
operator|=
literal|true
argument_list|)
name|ValueBuilder
name|receipients
parameter_list|)
block|{
name|SplitterBuilder
name|answer
init|=
operator|new
name|SplitterBuilder
argument_list|(
name|this
argument_list|,
name|receipients
operator|.
name|getExpression
argument_list|()
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
comment|/**      * A builder for the<a href="http://activemq.apache.org/camel/splitter.html">Splitter</a> pattern      * where an expression is evaluated to iterate through each of the parts of a message and then each part is then send to some endpoint.      *      * @param receipients the expression on which to split      * @return the builder      */
annotation|@
name|Fluent
DECL|method|splitter (@luentArgvalue = R, element = true)Expression receipients)
specifier|public
name|SplitterBuilder
name|splitter
parameter_list|(
annotation|@
name|FluentArg
argument_list|(
name|value
operator|=
literal|"recipients"
argument_list|,
name|element
operator|=
literal|true
argument_list|)
name|Expression
name|receipients
parameter_list|)
block|{
name|SplitterBuilder
name|answer
init|=
operator|new
name|SplitterBuilder
argument_list|(
name|this
argument_list|,
name|receipients
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
annotation|@
name|Fluent
DECL|method|errorHandler (@luentArgR)ErrorHandlerBuilder errorHandlerBuilder)
specifier|public
name|FromBuilder
name|errorHandler
parameter_list|(
annotation|@
name|FluentArg
argument_list|(
literal|"handler"
argument_list|)
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
comment|/**      * Configures whether or not the error handler is inherited by every processing node (or just the top most one)      *      * @param condition the falg as to whether error handlers should be inherited or not      * @return the current builder      */
annotation|@
name|Fluent
DECL|method|inheritErrorHandler (@luentArgR)boolean condition)
specifier|public
name|FromBuilder
name|inheritErrorHandler
parameter_list|(
annotation|@
name|FluentArg
argument_list|(
literal|"condition"
argument_list|)
name|boolean
name|condition
parameter_list|)
block|{
name|setInheritErrorHandler
argument_list|(
name|condition
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Fluent
argument_list|(
name|nestedActions
operator|=
literal|true
argument_list|)
DECL|method|intercept ()
specifier|public
name|InterceptorBuilder
name|intercept
parameter_list|()
block|{
name|InterceptorBuilder
name|answer
init|=
operator|new
name|InterceptorBuilder
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
comment|/**      * Trace logs the exchange before it goes to the next processing step using the {@link #DEFAULT_TRACE_CATEGORY} logging      * category.      *      * @return      */
annotation|@
name|Fluent
DECL|method|trace ()
specifier|public
name|FromBuilder
name|trace
parameter_list|()
block|{
return|return
name|trace
argument_list|(
name|DEFAULT_TRACE_CATEGORY
argument_list|)
return|;
block|}
comment|/**      * Trace logs the exchange before it goes to the next processing step using the specified logging      * category.      *      * @param category the logging category trace messages will sent to.      * @return      */
annotation|@
name|Fluent
DECL|method|trace (@luentArgR)String category)
specifier|public
name|FromBuilder
name|trace
parameter_list|(
annotation|@
name|FluentArg
argument_list|(
literal|"category"
argument_list|)
name|String
name|category
parameter_list|)
block|{
specifier|final
name|Log
name|log
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|category
argument_list|)
decl_stmt|;
return|return
name|intercept
argument_list|(
operator|new
name|DelegateProcessor
argument_list|()
block|{
annotation|@
name|Override
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
name|log
operator|.
name|trace
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|processNext
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
return|;
block|}
annotation|@
name|Fluent
DECL|method|intercept (@luentArgR)DelegateProcessor interceptor)
specifier|public
name|FromBuilder
name|intercept
parameter_list|(
annotation|@
name|FluentArg
argument_list|(
literal|"interceptor"
argument_list|)
name|DelegateProcessor
name|interceptor
parameter_list|)
block|{
name|InterceptorBuilder
name|answer
init|=
operator|new
name|InterceptorBuilder
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
operator|.
name|target
argument_list|()
return|;
block|}
annotation|@
name|Fluent
argument_list|(
name|nestedActions
operator|=
literal|true
argument_list|)
DECL|method|policies ()
specifier|public
name|PolicyBuilder
name|policies
parameter_list|()
block|{
name|PolicyBuilder
name|answer
init|=
operator|new
name|PolicyBuilder
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
annotation|@
name|Fluent
DECL|method|policy (@luentArgR)Policy policy)
specifier|public
name|FromBuilder
name|policy
parameter_list|(
annotation|@
name|FluentArg
argument_list|(
literal|"policy"
argument_list|)
name|Policy
name|policy
parameter_list|)
block|{
name|PolicyBuilder
name|answer
init|=
operator|new
name|PolicyBuilder
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|answer
operator|.
name|add
argument_list|(
name|policy
argument_list|)
expr_stmt|;
name|addProcessBuilder
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
operator|.
name|target
argument_list|()
return|;
block|}
comment|// Transformers
comment|//-------------------------------------------------------------------------
comment|/**      * Adds the custom processor to this destination which could be a final destination, or could be a transformation in a pipeline      */
annotation|@
name|Fluent
DECL|method|process (@luentArgR)Processor processor)
specifier|public
name|FromBuilder
name|process
parameter_list|(
annotation|@
name|FluentArg
argument_list|(
literal|"ref"
argument_list|)
name|Processor
name|processor
parameter_list|)
block|{
name|addProcessorBuilder
argument_list|(
name|processor
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Adds a processor which sets the body on the IN message      */
annotation|@
name|Fluent
DECL|method|setBody (Expression expression)
specifier|public
name|FromBuilder
name|setBody
parameter_list|(
name|Expression
name|expression
parameter_list|)
block|{
name|addProcessorBuilder
argument_list|(
name|ProcessorBuilder
operator|.
name|setBody
argument_list|(
name|expression
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Adds a processor which sets the body on the OUT message      */
annotation|@
name|Fluent
DECL|method|setOutBody (Expression expression)
specifier|public
name|FromBuilder
name|setOutBody
parameter_list|(
name|Expression
name|expression
parameter_list|)
block|{
name|addProcessorBuilder
argument_list|(
name|ProcessorBuilder
operator|.
name|setOutBody
argument_list|(
name|expression
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Adds a processor which sets the header on the IN message      */
annotation|@
name|Fluent
DECL|method|setHeader (String name, Expression expression)
specifier|public
name|FromBuilder
name|setHeader
parameter_list|(
name|String
name|name
parameter_list|,
name|Expression
name|expression
parameter_list|)
block|{
name|addProcessorBuilder
argument_list|(
name|ProcessorBuilder
operator|.
name|setHeader
argument_list|(
name|name
argument_list|,
name|expression
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Adds a processor which sets the header on the OUT message      */
annotation|@
name|Fluent
DECL|method|setOutHeader (String name, Expression expression)
specifier|public
name|FromBuilder
name|setOutHeader
parameter_list|(
name|String
name|name
parameter_list|,
name|Expression
name|expression
parameter_list|)
block|{
name|addProcessorBuilder
argument_list|(
name|ProcessorBuilder
operator|.
name|setOutHeader
argument_list|(
name|name
argument_list|,
name|expression
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Adds a processor which sets the exchange property      */
annotation|@
name|Fluent
DECL|method|setProperty (String name, Expression expression)
specifier|public
name|FromBuilder
name|setProperty
parameter_list|(
name|String
name|name
parameter_list|,
name|Expression
name|expression
parameter_list|)
block|{
name|addProcessorBuilder
argument_list|(
name|ProcessorBuilder
operator|.
name|setProperty
argument_list|(
name|name
argument_list|,
name|expression
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Converts the IN message body to the specified type      */
annotation|@
name|Fluent
DECL|method|convertBodyTo (Class type)
specifier|public
name|FromBuilder
name|convertBodyTo
parameter_list|(
name|Class
name|type
parameter_list|)
block|{
name|addProcessorBuilder
argument_list|(
name|ProcessorBuilder
operator|.
name|setBody
argument_list|(
name|Builder
operator|.
name|body
argument_list|()
operator|.
name|convertTo
argument_list|(
name|type
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Converts the OUT message body to the specified type      */
annotation|@
name|Fluent
DECL|method|convertOutBodyTo (Class type)
specifier|public
name|FromBuilder
name|convertOutBodyTo
parameter_list|(
name|Class
name|type
parameter_list|)
block|{
name|addProcessorBuilder
argument_list|(
name|ProcessorBuilder
operator|.
name|setOutBody
argument_list|(
name|Builder
operator|.
name|outBody
argument_list|()
operator|.
name|convertTo
argument_list|(
name|type
argument_list|)
argument_list|)
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
name|getFrom
parameter_list|()
block|{
return|return
name|from
return|;
block|}
DECL|method|addProcessBuilder (ProcessorFactory processFactory)
specifier|public
name|ProcessorFactory
name|addProcessBuilder
parameter_list|(
name|ProcessorFactory
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
DECL|method|addProcessorBuilder (Processor processor)
specifier|protected
name|void
name|addProcessorBuilder
parameter_list|(
name|Processor
name|processor
parameter_list|)
block|{
name|addProcessBuilder
argument_list|(
operator|new
name|ConstantProcessorBuilder
argument_list|(
name|processor
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|addProcessor (Processor processor)
specifier|public
name|void
name|addProcessor
parameter_list|(
name|Processor
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
name|createProcessor
parameter_list|()
throws|throws
name|Exception
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
argument_list|()
decl_stmt|;
for|for
control|(
name|ProcessorFactory
name|processFactory
range|:
name|processFactories
control|)
block|{
name|Processor
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
name|Processor
name|processor
init|=
literal|null
decl_stmt|;
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
name|processor
operator|=
name|answer
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|processor
operator|=
operator|new
name|CompositeProcessor
argument_list|(
name|answer
argument_list|)
expr_stmt|;
block|}
return|return
name|processor
return|;
block|}
comment|/**      * Creates the processor and wraps it in any necessary interceptors and error handlers      */
DECL|method|makeProcessor (ProcessorFactory processFactory)
specifier|protected
name|Processor
name|makeProcessor
parameter_list|(
name|ProcessorFactory
name|processFactory
parameter_list|)
throws|throws
name|Exception
block|{
name|Processor
name|processor
init|=
name|processFactory
operator|.
name|createProcessor
argument_list|()
decl_stmt|;
name|processor
operator|=
name|wrapProcessor
argument_list|(
name|processor
argument_list|)
expr_stmt|;
return|return
name|wrapInErrorHandler
argument_list|(
name|processor
argument_list|)
return|;
block|}
comment|/**      * A strategy method to allow newly created processors to be wrapped in an error handler. This feature      * could be disabled for child builders such as {@link IdempotentConsumerBuilder} which will rely on the      * {@link FromBuilder} to perform the error handling to avoid doubly-wrapped processors with 2 nested error handlers      */
DECL|method|wrapInErrorHandler (Processor processor)
specifier|protected
name|Processor
name|wrapInErrorHandler
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
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
comment|/**      * A strategy method which allows derived classes to wrap the child processor in some kind of interceptor such as      * a filter for the {@link IdempotentConsumerBuilder}.      *      * @param processor the processor which can be wrapped      * @return the original processor or a new wrapped interceptor      */
DECL|method|wrapProcessor (Processor processor)
specifier|protected
name|Processor
name|wrapProcessor
parameter_list|(
name|Processor
name|processor
parameter_list|)
block|{
return|return
name|processor
return|;
block|}
DECL|method|getProcessors ()
specifier|public
name|List
argument_list|<
name|Processor
argument_list|>
name|getProcessors
parameter_list|()
block|{
return|return
name|processors
return|;
block|}
block|}
end_class

end_unit

