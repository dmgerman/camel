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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
operator|.
name|constant
operator|.
name|ConstantLanguage
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
name|ObjectHelper
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
DECL|class|ExceptionType
specifier|public
class|class
name|ExceptionType
extends|extends
name|ProcessorType
argument_list|<
name|ProcessorType
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
literal|"redeliveryPolicy"
argument_list|,
name|required
operator|=
literal|false
argument_list|)
DECL|field|redeliveryPolicy
specifier|private
name|RedeliveryPolicyType
name|redeliveryPolicy
decl_stmt|;
annotation|@
name|XmlElementRef
DECL|field|outputs
specifier|private
name|List
argument_list|<
name|ProcessorType
argument_list|<
name|?
argument_list|>
argument_list|>
name|outputs
init|=
operator|new
name|ArrayList
argument_list|<
name|ProcessorType
argument_list|<
name|?
argument_list|>
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
DECL|method|ExceptionType ()
specifier|public
name|ExceptionType
parameter_list|()
block|{     }
DECL|method|ExceptionType (List<Class> exceptionClasses)
specifier|public
name|ExceptionType
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
DECL|method|ExceptionType (Class exceptionType)
specifier|public
name|ExceptionType
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
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Exception[ "
operator|+
name|getExceptionClasses
argument_list|()
operator|+
literal|" -> "
operator|+
name|getOutputs
argument_list|()
operator|+
literal|"]"
return|;
block|}
comment|/**      * Catches an exception type.      */
annotation|@
name|Override
DECL|method|onException (Class exceptionType)
specifier|public
name|ExceptionType
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
comment|/**      * Allows an exception handler to create a new redelivery policy for this exception type      * @param parentPolicy the current redelivery policy      * @return a newly created redelivery policy, or return the original policy if no customization is required      * for this exception handler.      */
DECL|method|createRedeliveryPolicy (RedeliveryPolicy parentPolicy)
specifier|public
name|RedeliveryPolicy
name|createRedeliveryPolicy
parameter_list|(
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
comment|// lets attach a processor to an error handler
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
return|return
operator|new
name|CatchProcessor
argument_list|(
name|getExceptionClasses
argument_list|()
argument_list|,
name|childProcessor
argument_list|)
return|;
block|}
comment|// Fluent API
comment|//-------------------------------------------------------------------------
DECL|method|handled (boolean handled)
specifier|public
name|ExceptionType
name|handled
parameter_list|(
name|boolean
name|handled
parameter_list|)
block|{
name|ConstantLanguage
name|constant
init|=
operator|new
name|ConstantLanguage
argument_list|()
decl_stmt|;
return|return
name|handled
argument_list|(
name|constant
operator|.
name|createPredicate
argument_list|(
name|Boolean
operator|.
name|toString
argument_list|(
name|handled
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
DECL|method|handled (Predicate handled)
specifier|public
name|ExceptionType
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
DECL|method|handled (Expression handled)
specifier|public
name|ExceptionType
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
DECL|method|backOffMultiplier (double backOffMultiplier)
specifier|public
name|ExceptionType
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
DECL|method|collisionAvoidanceFactor (double collisionAvoidanceFactor)
specifier|public
name|ExceptionType
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
DECL|method|collisionAvoidancePercent (short collisionAvoidancePercent)
specifier|public
name|ExceptionType
name|collisionAvoidancePercent
parameter_list|(
name|short
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
DECL|method|initialRedeliveryDelay (long initialRedeliveryDelay)
specifier|public
name|ExceptionType
name|initialRedeliveryDelay
parameter_list|(
name|long
name|initialRedeliveryDelay
parameter_list|)
block|{
name|getOrCreateRedeliveryPolicy
argument_list|()
operator|.
name|initialRedeliveryDelay
argument_list|(
name|initialRedeliveryDelay
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|maximumRedeliveries (int maximumRedeliveries)
specifier|public
name|ExceptionType
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
DECL|method|useCollisionAvoidance ()
specifier|public
name|ExceptionType
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
DECL|method|useExponentialBackOff ()
specifier|public
name|ExceptionType
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
DECL|method|maximumRedeliveryDelay (long maximumRedeliveryDelay)
specifier|public
name|ExceptionType
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
comment|// Properties
comment|//-------------------------------------------------------------------------
DECL|method|getOutputs ()
specifier|public
name|List
argument_list|<
name|ProcessorType
argument_list|<
name|?
argument_list|>
argument_list|>
name|getOutputs
parameter_list|()
block|{
return|return
name|outputs
return|;
block|}
DECL|method|setOutputs (List<ProcessorType<?>> outputs)
specifier|public
name|void
name|setOutputs
parameter_list|(
name|List
argument_list|<
name|ProcessorType
argument_list|<
name|?
argument_list|>
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
name|RedeliveryPolicyType
name|getRedeliveryPolicy
parameter_list|()
block|{
return|return
name|redeliveryPolicy
return|;
block|}
DECL|method|setRedeliveryPolicy (RedeliveryPolicyType redeliveryPolicy)
specifier|public
name|void
name|setRedeliveryPolicy
parameter_list|(
name|RedeliveryPolicyType
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
comment|// Implementation methods
comment|//-------------------------------------------------------------------------
DECL|method|getOrCreateRedeliveryPolicy ()
specifier|protected
name|RedeliveryPolicyType
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
name|RedeliveryPolicyType
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
name|type
init|=
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
block|}
end_class

end_unit

