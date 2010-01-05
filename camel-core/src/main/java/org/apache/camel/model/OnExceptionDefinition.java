begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAttribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlElementRef
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlTransient
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
name|LoggingLevel
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
name|ErrorHandlerBuilder
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
name|builder
operator|.
name|ExpressionClause
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
name|CatchProcessor
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
name|RedeliveryPolicy
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
name|CastUtils
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
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|PredicateBuilder
operator|.
name|toPredicate
import|;
end_import

begin_comment
comment|/**  * Represents an XML&lt;onException/&gt; element  *  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"onException"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|OnExceptionDefinition
specifier|public
class|class
name|OnExceptionDefinition
extends|extends
name|ProcessorDefinition
argument_list|<
name|ProcessorDefinition
argument_list|>
block|{
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"exception"
argument_list|)
DECL|field|exceptions
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|exceptions
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"onWhen"
argument_list|,
name|required
operator|=
literal|false
argument_list|)
DECL|field|onWhen
specifier|private
name|WhenDefinition
name|onWhen
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"retryUntil"
argument_list|,
name|required
operator|=
literal|false
argument_list|)
DECL|field|retryUntil
specifier|private
name|ExpressionSubElementDefinition
name|retryUntil
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"redeliveryPolicy"
argument_list|,
name|required
operator|=
literal|false
argument_list|)
DECL|field|redeliveryPolicy
specifier|private
name|RedeliveryPolicyDefinition
name|redeliveryPolicy
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"handled"
argument_list|,
name|required
operator|=
literal|false
argument_list|)
DECL|field|handled
specifier|private
name|ExpressionSubElementDefinition
name|handled
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|name
operator|=
literal|"onRedeliveryRef"
argument_list|,
name|required
operator|=
literal|false
argument_list|)
DECL|field|onRedeliveryRef
specifier|private
name|String
name|onRedeliveryRef
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|name
operator|=
literal|"useOriginalMessage"
argument_list|,
name|required
operator|=
literal|false
argument_list|)
DECL|field|useOriginalMessagePolicy
specifier|private
name|Boolean
name|useOriginalMessagePolicy
init|=
name|Boolean
operator|.
name|FALSE
decl_stmt|;
annotation|@
name|XmlElementRef
DECL|field|outputs
specifier|private
name|List
argument_list|<
name|ProcessorDefinition
argument_list|>
name|outputs
init|=
operator|new
name|ArrayList
argument_list|<
name|ProcessorDefinition
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|exceptionClasses
specifier|private
name|List
argument_list|<
name|Class
argument_list|>
name|exceptionClasses
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|errorHandler
specifier|private
name|Processor
name|errorHandler
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|handledPolicy
specifier|private
name|Predicate
name|handledPolicy
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|retryUntilPolicy
specifier|private
name|Predicate
name|retryUntilPolicy
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|onRedelivery
specifier|private
name|Processor
name|onRedelivery
decl_stmt|;
DECL|method|OnExceptionDefinition ()
specifier|public
name|OnExceptionDefinition
parameter_list|()
block|{     }
DECL|method|OnExceptionDefinition (List<Class> exceptionClasses)
specifier|public
name|OnExceptionDefinition
parameter_list|(
name|List
argument_list|<
name|Class
argument_list|>
name|exceptionClasses
parameter_list|)
block|{
name|this
operator|.
name|exceptionClasses
operator|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|exceptionClasses
argument_list|)
expr_stmt|;
block|}
DECL|method|OnExceptionDefinition (Class exceptionType)
specifier|public
name|OnExceptionDefinition
parameter_list|(
name|Class
name|exceptionType
parameter_list|)
block|{
name|exceptionClasses
operator|=
operator|new
name|ArrayList
argument_list|<
name|Class
argument_list|>
argument_list|()
expr_stmt|;
name|exceptionClasses
operator|.
name|add
argument_list|(
name|exceptionType
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getShortName ()
specifier|public
name|String
name|getShortName
parameter_list|()
block|{
return|return
literal|"onException"
return|;
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
literal|"OnException["
operator|+
name|getExceptionClasses
argument_list|()
operator|+
operator|(
name|onWhen
operator|!=
literal|null
condition|?
literal|" "
operator|+
name|onWhen
else|:
literal|""
operator|)
operator|+
literal|" -> "
operator|+
name|getOutputs
argument_list|()
operator|+
literal|"]"
return|;
block|}
annotation|@
name|Override
DECL|method|isAbstract ()
specifier|public
name|boolean
name|isAbstract
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
comment|/**      * Allows an exception handler to create a new redelivery policy for this exception type      * @param context the camel context      * @param parentPolicy the current redelivery policy      * @return a newly created redelivery policy, or return the original policy if no customization is required      * for this exception handler.      */
DECL|method|createRedeliveryPolicy (CamelContext context, RedeliveryPolicy parentPolicy)
specifier|public
name|RedeliveryPolicy
name|createRedeliveryPolicy
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|RedeliveryPolicy
name|parentPolicy
parameter_list|)
block|{
if|if
condition|(
name|redeliveryPolicy
operator|!=
literal|null
condition|)
block|{
return|return
name|redeliveryPolicy
operator|.
name|createRedeliveryPolicy
argument_list|(
name|context
argument_list|,
name|parentPolicy
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|errorHandler
operator|!=
literal|null
condition|)
block|{
comment|// lets create a new error handler that has no retries
name|RedeliveryPolicy
name|answer
init|=
name|parentPolicy
operator|.
name|copy
argument_list|()
decl_stmt|;
name|answer
operator|.
name|setMaximumRedeliveries
argument_list|(
literal|0
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
return|return
name|parentPolicy
return|;
block|}
DECL|method|addRoutes (RouteContext routeContext, Collection<Route> routes)
specifier|public
name|void
name|addRoutes
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|Collection
argument_list|<
name|Route
argument_list|>
name|routes
parameter_list|)
throws|throws
name|Exception
block|{
name|setHandledFromExpressionType
argument_list|(
name|routeContext
argument_list|)
expr_stmt|;
name|setRetryUntilFromExpressionType
argument_list|(
name|routeContext
argument_list|)
expr_stmt|;
comment|// lookup onRedelivery if ref is provided
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|onRedeliveryRef
argument_list|)
condition|)
block|{
name|setOnRedelivery
argument_list|(
name|routeContext
operator|.
name|lookup
argument_list|(
name|onRedeliveryRef
argument_list|,
name|Processor
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// lets attach this on exception to the route error handler
name|errorHandler
operator|=
name|routeContext
operator|.
name|createProcessor
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|ErrorHandlerBuilder
name|builder
init|=
name|routeContext
operator|.
name|getRoute
argument_list|()
operator|.
name|getErrorHandlerBuilder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|addErrorHandlers
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProcessor (RouteContext routeContext)
specifier|public
name|CatchProcessor
name|createProcessor
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
throws|throws
name|Exception
block|{
name|Processor
name|childProcessor
init|=
name|routeContext
operator|.
name|createProcessor
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|Predicate
name|when
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|onWhen
operator|!=
literal|null
condition|)
block|{
name|when
operator|=
name|onWhen
operator|.
name|getExpression
argument_list|()
operator|.
name|createPredicate
argument_list|(
name|routeContext
argument_list|)
expr_stmt|;
block|}
name|Predicate
name|handle
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|handled
operator|!=
literal|null
condition|)
block|{
name|handle
operator|=
name|handled
operator|.
name|createPredicate
argument_list|(
name|routeContext
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|CatchProcessor
argument_list|(
name|getExceptionClasses
argument_list|()
argument_list|,
name|childProcessor
argument_list|,
name|when
argument_list|,
name|handle
argument_list|)
return|;
block|}
comment|// Fluent API
comment|//-------------------------------------------------------------------------
annotation|@
name|Override
DECL|method|onException (Class exceptionType)
specifier|public
name|OnExceptionDefinition
name|onException
parameter_list|(
name|Class
name|exceptionType
parameter_list|)
block|{
name|getExceptionClasses
argument_list|()
operator|.
name|add
argument_list|(
name|exceptionType
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets whether the exchange should be marked as handled or not.      *      * @param handled  handled or not      * @return the builder      */
DECL|method|handled (boolean handled)
specifier|public
name|OnExceptionDefinition
name|handled
parameter_list|(
name|boolean
name|handled
parameter_list|)
block|{
name|Expression
name|expression
init|=
name|ExpressionBuilder
operator|.
name|constantExpression
argument_list|(
name|Boolean
operator|.
name|toString
argument_list|(
name|handled
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|handled
argument_list|(
name|expression
argument_list|)
return|;
block|}
comment|/**      * Sets whether the exchange should be marked as handled or not.      *      * @param handled  predicate that determines true or false      * @return the builder      */
DECL|method|handled (Predicate handled)
specifier|public
name|OnExceptionDefinition
name|handled
parameter_list|(
name|Predicate
name|handled
parameter_list|)
block|{
name|setHandledPolicy
argument_list|(
name|handled
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets whether the exchange should be marked as handled or not.      *      * @param handled  expression that determines true or false      * @return the builder      */
DECL|method|handled (Expression handled)
specifier|public
name|OnExceptionDefinition
name|handled
parameter_list|(
name|Expression
name|handled
parameter_list|)
block|{
name|setHandledPolicy
argument_list|(
name|toPredicate
argument_list|(
name|handled
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets an additional predicate that should be true before the onException is triggered.      *<p/>      * To be used for fine grained controlling whether a thrown exception should be intercepted      * by this exception type or not.      *      * @param predicate  predicate that determines true or false      * @return the builder      */
DECL|method|onWhen (Predicate predicate)
specifier|public
name|OnExceptionDefinition
name|onWhen
parameter_list|(
name|Predicate
name|predicate
parameter_list|)
block|{
name|setOnWhen
argument_list|(
operator|new
name|WhenDefinition
argument_list|(
name|predicate
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Creates an expression to configure an additional predicate that should be true before the      * onException is triggered.      *<p/>      * To be used for fine grained controlling whether a thrown exception should be intercepted      * by this exception type or not.      *      * @return the expression clause to configure      */
DECL|method|onWhen ()
specifier|public
name|ExpressionClause
argument_list|<
name|OnExceptionDefinition
argument_list|>
name|onWhen
parameter_list|()
block|{
name|onWhen
operator|=
operator|new
name|WhenDefinition
argument_list|()
expr_stmt|;
name|ExpressionClause
argument_list|<
name|OnExceptionDefinition
argument_list|>
name|clause
init|=
operator|new
name|ExpressionClause
argument_list|<
name|OnExceptionDefinition
argument_list|>
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|onWhen
operator|.
name|setExpression
argument_list|(
name|clause
argument_list|)
expr_stmt|;
return|return
name|clause
return|;
block|}
comment|/**      * Sets the retry until predicate.      *      * @param until predicate that determines when to stop retrying      * @return the builder      */
DECL|method|retryUntil (Predicate until)
specifier|public
name|OnExceptionDefinition
name|retryUntil
parameter_list|(
name|Predicate
name|until
parameter_list|)
block|{
name|setRetryUntilPolicy
argument_list|(
name|until
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the retry until expression.      *      * @param until expression that determines when to stop retrying      * @return the builder      */
DECL|method|retryUntil (Expression until)
specifier|public
name|OnExceptionDefinition
name|retryUntil
parameter_list|(
name|Expression
name|until
parameter_list|)
block|{
name|setRetryUntilPolicy
argument_list|(
name|toPredicate
argument_list|(
name|until
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the delay      *      * @param delay  the redeliver delay      * @return the builder      */
DECL|method|redeliverDelay (long delay)
specifier|public
name|OnExceptionDefinition
name|redeliverDelay
parameter_list|(
name|long
name|delay
parameter_list|)
block|{
name|getOrCreateRedeliveryPolicy
argument_list|()
operator|.
name|redeliveryDelay
argument_list|(
name|delay
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the back off multiplier      *      * @param backOffMultiplier  the back off multiplier      * @return the builder      */
DECL|method|backOffMultiplier (double backOffMultiplier)
specifier|public
name|OnExceptionDefinition
name|backOffMultiplier
parameter_list|(
name|double
name|backOffMultiplier
parameter_list|)
block|{
name|getOrCreateRedeliveryPolicy
argument_list|()
operator|.
name|backOffMultiplier
argument_list|(
name|backOffMultiplier
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the collision avoidance factor      *      * @param collisionAvoidanceFactor  the factor      * @return the builder      */
DECL|method|collisionAvoidanceFactor (double collisionAvoidanceFactor)
specifier|public
name|OnExceptionDefinition
name|collisionAvoidanceFactor
parameter_list|(
name|double
name|collisionAvoidanceFactor
parameter_list|)
block|{
name|getOrCreateRedeliveryPolicy
argument_list|()
operator|.
name|collisionAvoidanceFactor
argument_list|(
name|collisionAvoidanceFactor
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the collision avoidance percentage      *      * @param collisionAvoidancePercent  the percentage      * @return the builder      */
DECL|method|collisionAvoidancePercent (double collisionAvoidancePercent)
specifier|public
name|OnExceptionDefinition
name|collisionAvoidancePercent
parameter_list|(
name|double
name|collisionAvoidancePercent
parameter_list|)
block|{
name|getOrCreateRedeliveryPolicy
argument_list|()
operator|.
name|collisionAvoidancePercent
argument_list|(
name|collisionAvoidancePercent
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the fixed delay between redeliveries      *      * @param delay  delay in millis      * @return the builder      */
DECL|method|redeliveryDelay (long delay)
specifier|public
name|OnExceptionDefinition
name|redeliveryDelay
parameter_list|(
name|long
name|delay
parameter_list|)
block|{
name|getOrCreateRedeliveryPolicy
argument_list|()
operator|.
name|redeliveryDelay
argument_list|(
name|delay
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the logging level to use when retries has exhausted      *      * @param retriesExhaustedLogLevel  the logging level      * @return the builder      */
DECL|method|retriesExhaustedLogLevel (LoggingLevel retriesExhaustedLogLevel)
specifier|public
name|OnExceptionDefinition
name|retriesExhaustedLogLevel
parameter_list|(
name|LoggingLevel
name|retriesExhaustedLogLevel
parameter_list|)
block|{
name|getOrCreateRedeliveryPolicy
argument_list|()
operator|.
name|retriesExhaustedLogLevel
argument_list|(
name|retriesExhaustedLogLevel
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the logging level to use for logging retry attempts      *      * @param retryAttemptedLogLevel  the logging level      * @return the builder      */
DECL|method|retryAttemptedLogLevel (LoggingLevel retryAttemptedLogLevel)
specifier|public
name|OnExceptionDefinition
name|retryAttemptedLogLevel
parameter_list|(
name|LoggingLevel
name|retryAttemptedLogLevel
parameter_list|)
block|{
name|getOrCreateRedeliveryPolicy
argument_list|()
operator|.
name|retryAttemptedLogLevel
argument_list|(
name|retryAttemptedLogLevel
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the maximum redeliveries      *<ul>      *<li>5 = default value</li>      *<li>0 = no redeliveries</li>      *<li>-1 = redeliver forever</li>      *</ul>      *      * @param maximumRedeliveries  the value      * @return the builder      */
DECL|method|maximumRedeliveries (int maximumRedeliveries)
specifier|public
name|OnExceptionDefinition
name|maximumRedeliveries
parameter_list|(
name|int
name|maximumRedeliveries
parameter_list|)
block|{
name|getOrCreateRedeliveryPolicy
argument_list|()
operator|.
name|maximumRedeliveries
argument_list|(
name|maximumRedeliveries
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Turn on collision avoidance.      *      * @return the builder      */
DECL|method|useCollisionAvoidance ()
specifier|public
name|OnExceptionDefinition
name|useCollisionAvoidance
parameter_list|()
block|{
name|getOrCreateRedeliveryPolicy
argument_list|()
operator|.
name|useCollisionAvoidance
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Turn on exponential backk off      *      * @return the builder      */
DECL|method|useExponentialBackOff ()
specifier|public
name|OnExceptionDefinition
name|useExponentialBackOff
parameter_list|()
block|{
name|getOrCreateRedeliveryPolicy
argument_list|()
operator|.
name|useExponentialBackOff
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the maximum delay between redelivery      *      * @param maximumRedeliveryDelay  the delay in millis      * @return the builder      */
DECL|method|maximumRedeliveryDelay (long maximumRedeliveryDelay)
specifier|public
name|OnExceptionDefinition
name|maximumRedeliveryDelay
parameter_list|(
name|long
name|maximumRedeliveryDelay
parameter_list|)
block|{
name|getOrCreateRedeliveryPolicy
argument_list|()
operator|.
name|maximumRedeliveryDelay
argument_list|(
name|maximumRedeliveryDelay
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Will use the original input body when an {@link org.apache.camel.Exchange} is moved to the dead letter queue.      *<p/>      *<b>Notice:</b> this only applies when all redeliveries attempt have failed and the {@link org.apache.camel.Exchange} is doomed for failure.      *<br/>      * Instead of using the current inprogress {@link org.apache.camel.Exchange} IN body we use the original IN body instead. This allows      * you to store the original input in the dead letter queue instead of the inprogress snapshot of the IN body.      * For instance if you route transform the IN body during routing and then failed. With the original exchange      * store in the dead letter queue it might be easier to manually re submit the {@link org.apache.camel.Exchange} again as the IN body      * is the same as when Camel received it. So you should be able to send the {@link org.apache.camel.Exchange} to the same input.      *<p/>      * By default this feature is off.      *      * @return the builder      */
DECL|method|useOriginalBody ()
specifier|public
name|OnExceptionDefinition
name|useOriginalBody
parameter_list|()
block|{
name|setUseOriginalMessagePolicy
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets a processor that should be processed<b>before</b> a redelivey attempt.      *<p/>      * Can be used to change the {@link org.apache.camel.Exchange}<b>before</b> its being redelivered.      */
DECL|method|onRedelivery (Processor processor)
specifier|public
name|OnExceptionDefinition
name|onRedelivery
parameter_list|(
name|Processor
name|processor
parameter_list|)
block|{
name|setOnRedelivery
argument_list|(
name|processor
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|// Properties
comment|//-------------------------------------------------------------------------
DECL|method|getOutputs ()
specifier|public
name|List
argument_list|<
name|ProcessorDefinition
argument_list|>
name|getOutputs
parameter_list|()
block|{
return|return
name|outputs
return|;
block|}
DECL|method|setOutputs (List<ProcessorDefinition> outputs)
specifier|public
name|void
name|setOutputs
parameter_list|(
name|List
argument_list|<
name|ProcessorDefinition
argument_list|>
name|outputs
parameter_list|)
block|{
name|this
operator|.
name|outputs
operator|=
name|outputs
expr_stmt|;
block|}
DECL|method|getExceptionClasses ()
specifier|public
name|List
argument_list|<
name|Class
argument_list|>
name|getExceptionClasses
parameter_list|()
block|{
if|if
condition|(
name|exceptionClasses
operator|==
literal|null
condition|)
block|{
name|exceptionClasses
operator|=
name|createExceptionClasses
argument_list|()
expr_stmt|;
block|}
return|return
name|exceptionClasses
return|;
block|}
DECL|method|setExceptionClasses (List<Class> exceptionClasses)
specifier|public
name|void
name|setExceptionClasses
parameter_list|(
name|List
argument_list|<
name|Class
argument_list|>
name|exceptionClasses
parameter_list|)
block|{
name|this
operator|.
name|exceptionClasses
operator|=
name|exceptionClasses
expr_stmt|;
block|}
DECL|method|getExceptions ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getExceptions
parameter_list|()
block|{
return|return
name|exceptions
return|;
block|}
DECL|method|setExceptions (List<String> exceptions)
specifier|public
name|void
name|setExceptions
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|exceptions
parameter_list|)
block|{
name|this
operator|.
name|exceptions
operator|=
name|exceptions
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
DECL|method|getRedeliveryPolicy ()
specifier|public
name|RedeliveryPolicyDefinition
name|getRedeliveryPolicy
parameter_list|()
block|{
return|return
name|redeliveryPolicy
return|;
block|}
DECL|method|setRedeliveryPolicy (RedeliveryPolicyDefinition redeliveryPolicy)
specifier|public
name|void
name|setRedeliveryPolicy
parameter_list|(
name|RedeliveryPolicyDefinition
name|redeliveryPolicy
parameter_list|)
block|{
name|this
operator|.
name|redeliveryPolicy
operator|=
name|redeliveryPolicy
expr_stmt|;
block|}
DECL|method|getHandledPolicy ()
specifier|public
name|Predicate
name|getHandledPolicy
parameter_list|()
block|{
return|return
name|handledPolicy
return|;
block|}
DECL|method|setHandled (ExpressionSubElementDefinition handled)
specifier|public
name|void
name|setHandled
parameter_list|(
name|ExpressionSubElementDefinition
name|handled
parameter_list|)
block|{
name|this
operator|.
name|handled
operator|=
name|handled
expr_stmt|;
block|}
DECL|method|getHandled ()
specifier|public
name|ExpressionSubElementDefinition
name|getHandled
parameter_list|()
block|{
return|return
name|handled
return|;
block|}
DECL|method|setHandledPolicy (Predicate handledPolicy)
specifier|public
name|void
name|setHandledPolicy
parameter_list|(
name|Predicate
name|handledPolicy
parameter_list|)
block|{
name|this
operator|.
name|handledPolicy
operator|=
name|handledPolicy
expr_stmt|;
block|}
DECL|method|getOnWhen ()
specifier|public
name|WhenDefinition
name|getOnWhen
parameter_list|()
block|{
return|return
name|onWhen
return|;
block|}
DECL|method|setOnWhen (WhenDefinition onWhen)
specifier|public
name|void
name|setOnWhen
parameter_list|(
name|WhenDefinition
name|onWhen
parameter_list|)
block|{
name|this
operator|.
name|onWhen
operator|=
name|onWhen
expr_stmt|;
block|}
DECL|method|getRetryUntil ()
specifier|public
name|ExpressionSubElementDefinition
name|getRetryUntil
parameter_list|()
block|{
return|return
name|retryUntil
return|;
block|}
DECL|method|setRetryUntil (ExpressionSubElementDefinition retryUntil)
specifier|public
name|void
name|setRetryUntil
parameter_list|(
name|ExpressionSubElementDefinition
name|retryUntil
parameter_list|)
block|{
name|this
operator|.
name|retryUntil
operator|=
name|retryUntil
expr_stmt|;
block|}
DECL|method|getRetryUntilPolicy ()
specifier|public
name|Predicate
name|getRetryUntilPolicy
parameter_list|()
block|{
return|return
name|retryUntilPolicy
return|;
block|}
DECL|method|setRetryUntilPolicy (Predicate retryUntilPolicy)
specifier|public
name|void
name|setRetryUntilPolicy
parameter_list|(
name|Predicate
name|retryUntilPolicy
parameter_list|)
block|{
name|this
operator|.
name|retryUntilPolicy
operator|=
name|retryUntilPolicy
expr_stmt|;
block|}
DECL|method|getOnRedelivery ()
specifier|public
name|Processor
name|getOnRedelivery
parameter_list|()
block|{
return|return
name|onRedelivery
return|;
block|}
DECL|method|setOnRedelivery (Processor onRedelivery)
specifier|public
name|void
name|setOnRedelivery
parameter_list|(
name|Processor
name|onRedelivery
parameter_list|)
block|{
name|this
operator|.
name|onRedelivery
operator|=
name|onRedelivery
expr_stmt|;
block|}
DECL|method|getOnRedeliveryRef ()
specifier|public
name|String
name|getOnRedeliveryRef
parameter_list|()
block|{
return|return
name|onRedeliveryRef
return|;
block|}
DECL|method|setOnRedeliveryRef (String onRedeliveryRef)
specifier|public
name|void
name|setOnRedeliveryRef
parameter_list|(
name|String
name|onRedeliveryRef
parameter_list|)
block|{
name|this
operator|.
name|onRedeliveryRef
operator|=
name|onRedeliveryRef
expr_stmt|;
block|}
DECL|method|getUseOriginalMessagePolicy ()
specifier|public
name|Boolean
name|getUseOriginalMessagePolicy
parameter_list|()
block|{
return|return
name|useOriginalMessagePolicy
return|;
block|}
DECL|method|setUseOriginalMessagePolicy (Boolean useOriginalMessagePolicy)
specifier|public
name|void
name|setUseOriginalMessagePolicy
parameter_list|(
name|Boolean
name|useOriginalMessagePolicy
parameter_list|)
block|{
name|this
operator|.
name|useOriginalMessagePolicy
operator|=
name|useOriginalMessagePolicy
expr_stmt|;
block|}
comment|// Implementation methods
comment|//-------------------------------------------------------------------------
DECL|method|getOrCreateRedeliveryPolicy ()
specifier|protected
name|RedeliveryPolicyDefinition
name|getOrCreateRedeliveryPolicy
parameter_list|()
block|{
if|if
condition|(
name|redeliveryPolicy
operator|==
literal|null
condition|)
block|{
name|redeliveryPolicy
operator|=
operator|new
name|RedeliveryPolicyDefinition
argument_list|()
expr_stmt|;
block|}
return|return
name|redeliveryPolicy
return|;
block|}
DECL|method|createExceptionClasses ()
specifier|protected
name|List
argument_list|<
name|Class
argument_list|>
name|createExceptionClasses
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
name|getExceptions
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Class
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<
name|Class
argument_list|>
argument_list|(
name|list
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|name
range|:
name|list
control|)
block|{
name|Class
argument_list|<
name|Throwable
argument_list|>
name|type
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|ObjectHelper
operator|.
name|loadClass
argument_list|(
name|name
argument_list|,
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
argument_list|,
name|Throwable
operator|.
name|class
argument_list|)
decl_stmt|;
name|answer
operator|.
name|add
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|setHandledFromExpressionType (RouteContext routeContext)
specifier|private
name|void
name|setHandledFromExpressionType
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
if|if
condition|(
name|getHandled
argument_list|()
operator|!=
literal|null
operator|&&
name|handledPolicy
operator|==
literal|null
operator|&&
name|routeContext
operator|!=
literal|null
condition|)
block|{
name|handled
argument_list|(
name|getHandled
argument_list|()
operator|.
name|createPredicate
argument_list|(
name|routeContext
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|setRetryUntilFromExpressionType (RouteContext routeContext)
specifier|private
name|void
name|setRetryUntilFromExpressionType
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
if|if
condition|(
name|getRetryUntil
argument_list|()
operator|!=
literal|null
operator|&&
name|retryUntilPolicy
operator|==
literal|null
operator|&&
name|routeContext
operator|!=
literal|null
condition|)
block|{
name|retryUntil
argument_list|(
name|getRetryUntil
argument_list|()
operator|.
name|createPredicate
argument_list|(
name|routeContext
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

