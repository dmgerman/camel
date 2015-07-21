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
name|concurrent
operator|.
name|ExecutorService
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ScheduledExecutorService
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
name|model
operator|.
name|language
operator|.
name|ExpressionDefinition
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
name|Throttler
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
name|Metadata
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

begin_comment
comment|/**  * Controls the rate at which messages are passed to the next node in the route  *  * @version   */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"eip,routing"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"throttle"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|ThrottleDefinition
specifier|public
class|class
name|ThrottleDefinition
extends|extends
name|ExpressionNode
implements|implements
name|ExecutorServiceAwareDefinition
argument_list|<
name|ThrottleDefinition
argument_list|>
block|{
comment|// TODO: Camel 3.0 Should not support outputs
annotation|@
name|XmlTransient
DECL|field|executorService
specifier|private
name|ExecutorService
name|executorService
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|executorServiceRef
specifier|private
name|String
name|executorServiceRef
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"1000"
argument_list|)
DECL|field|timePeriodMillis
specifier|private
name|Long
name|timePeriodMillis
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|asyncDelayed
specifier|private
name|Boolean
name|asyncDelayed
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|callerRunsWhenRejected
specifier|private
name|Boolean
name|callerRunsWhenRejected
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|rejectExecution
specifier|private
name|Boolean
name|rejectExecution
decl_stmt|;
DECL|method|ThrottleDefinition ()
specifier|public
name|ThrottleDefinition
parameter_list|()
block|{     }
DECL|method|ThrottleDefinition (Expression maximumRequestsPerPeriod)
specifier|public
name|ThrottleDefinition
parameter_list|(
name|Expression
name|maximumRequestsPerPeriod
parameter_list|)
block|{
name|super
argument_list|(
name|maximumRequestsPerPeriod
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
literal|"Throttle["
operator|+
name|description
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
DECL|method|description ()
specifier|protected
name|String
name|description
parameter_list|()
block|{
return|return
name|getExpression
argument_list|()
operator|+
literal|" request per "
operator|+
name|getTimePeriodMillis
argument_list|()
operator|+
literal|" millis"
return|;
block|}
annotation|@
name|Override
DECL|method|getLabel ()
specifier|public
name|String
name|getLabel
parameter_list|()
block|{
return|return
literal|"throttle["
operator|+
name|description
argument_list|()
operator|+
literal|"]"
return|;
block|}
annotation|@
name|Override
DECL|method|createProcessor (RouteContext routeContext)
specifier|public
name|Processor
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
name|this
operator|.
name|createChildProcessor
argument_list|(
name|routeContext
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|boolean
name|async
init|=
name|getAsyncDelayed
argument_list|()
operator|!=
literal|null
operator|&&
name|getAsyncDelayed
argument_list|()
decl_stmt|;
name|boolean
name|shutdownThreadPool
init|=
name|ProcessorDefinitionHelper
operator|.
name|willCreateNewThreadPool
argument_list|(
name|routeContext
argument_list|,
name|this
argument_list|,
name|async
argument_list|)
decl_stmt|;
name|ScheduledExecutorService
name|threadPool
init|=
name|ProcessorDefinitionHelper
operator|.
name|getConfiguredScheduledExecutorService
argument_list|(
name|routeContext
argument_list|,
literal|"Throttle"
argument_list|,
name|this
argument_list|,
name|async
argument_list|)
decl_stmt|;
comment|// should be default 1000 millis
name|long
name|period
init|=
name|getTimePeriodMillis
argument_list|()
operator|!=
literal|null
condition|?
name|getTimePeriodMillis
argument_list|()
else|:
literal|1000L
decl_stmt|;
comment|// max requests per period is mandatory
name|Expression
name|maxRequestsExpression
init|=
name|createMaxRequestsPerPeriodExpression
argument_list|(
name|routeContext
argument_list|)
decl_stmt|;
if|if
condition|(
name|maxRequestsExpression
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"MaxRequestsPerPeriod expression must be provided on "
operator|+
name|this
argument_list|)
throw|;
block|}
name|boolean
name|reject
init|=
name|getRejectExecution
argument_list|()
operator|!=
literal|null
operator|&&
name|getRejectExecution
argument_list|()
decl_stmt|;
name|Throttler
name|answer
init|=
operator|new
name|Throttler
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|childProcessor
argument_list|,
name|maxRequestsExpression
argument_list|,
name|period
argument_list|,
name|threadPool
argument_list|,
name|shutdownThreadPool
argument_list|,
name|reject
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setAsyncDelayed
argument_list|(
name|async
argument_list|)
expr_stmt|;
if|if
condition|(
name|getCallerRunsWhenRejected
argument_list|()
operator|==
literal|null
condition|)
block|{
comment|// should be true by default
name|answer
operator|.
name|setCallerRunsWhenRejected
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|.
name|setCallerRunsWhenRejected
argument_list|(
name|getCallerRunsWhenRejected
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|createMaxRequestsPerPeriodExpression (RouteContext routeContext)
specifier|private
name|Expression
name|createMaxRequestsPerPeriodExpression
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
name|ExpressionDefinition
name|expr
init|=
name|getExpression
argument_list|()
decl_stmt|;
if|if
condition|(
name|expr
operator|!=
literal|null
condition|)
block|{
return|return
name|expr
operator|.
name|createExpression
argument_list|(
name|routeContext
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|// Fluent API
comment|// -------------------------------------------------------------------------
comment|/**      * Sets the time period during which the maximum request count is valid for      *      * @param timePeriodMillis  period in millis      * @return the builder      */
DECL|method|timePeriodMillis (long timePeriodMillis)
specifier|public
name|ThrottleDefinition
name|timePeriodMillis
parameter_list|(
name|long
name|timePeriodMillis
parameter_list|)
block|{
name|setTimePeriodMillis
argument_list|(
name|timePeriodMillis
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the time period during which the maximum request count per period      *      * @param maximumRequestsPerPeriod  the maximum request count number per time period      * @return the builder      */
DECL|method|maximumRequestsPerPeriod (Long maximumRequestsPerPeriod)
specifier|public
name|ThrottleDefinition
name|maximumRequestsPerPeriod
parameter_list|(
name|Long
name|maximumRequestsPerPeriod
parameter_list|)
block|{
name|setExpression
argument_list|(
name|ExpressionNodeHelper
operator|.
name|toExpressionDefinition
argument_list|(
name|ExpressionBuilder
operator|.
name|constantExpression
argument_list|(
name|maximumRequestsPerPeriod
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Whether or not the caller should run the task when it was rejected by the thread pool.      *<p/>      * Is by default<tt>true</tt>      *      * @param callerRunsWhenRejected whether or not the caller should run      * @return the builder      */
DECL|method|callerRunsWhenRejected (boolean callerRunsWhenRejected)
specifier|public
name|ThrottleDefinition
name|callerRunsWhenRejected
parameter_list|(
name|boolean
name|callerRunsWhenRejected
parameter_list|)
block|{
name|setCallerRunsWhenRejected
argument_list|(
name|callerRunsWhenRejected
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Enables asynchronous delay which means the thread will<b>not</b> block while delaying.      *      * @return the builder      */
DECL|method|asyncDelayed ()
specifier|public
name|ThrottleDefinition
name|asyncDelayed
parameter_list|()
block|{
name|setAsyncDelayed
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Whether or not throttler throws the ThrottlerRejectedExecutionException when the exchange exceeds the request limit      *<p/>      * Is by default<tt>false</tt>      *      * @param rejectExecution throw the RejectExecutionException if the exchange exceeds the request limit       * @return the builder      */
DECL|method|rejectExecution (boolean rejectExecution)
specifier|public
name|ThrottleDefinition
name|rejectExecution
parameter_list|(
name|boolean
name|rejectExecution
parameter_list|)
block|{
name|setRejectExecution
argument_list|(
name|rejectExecution
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the ExecutorService which could be used by throttle definition      *      * @param executorService        * @return the builder      */
DECL|method|executorService (ExecutorService executorService)
specifier|public
name|ThrottleDefinition
name|executorService
parameter_list|(
name|ExecutorService
name|executorService
parameter_list|)
block|{
name|setExecutorService
argument_list|(
name|executorService
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the ExecutorService which could be used by throttle definition      *      * @param executorServiceRef the reference id of the Executor Service        * @return the builder      */
DECL|method|executorServiceRef (String executorServiceRef)
specifier|public
name|ThrottleDefinition
name|executorServiceRef
parameter_list|(
name|String
name|executorServiceRef
parameter_list|)
block|{
name|setExecutorServiceRef
argument_list|(
name|executorServiceRef
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
comment|/**      * Expression to configure the maximum number of messages to throttle per request      */
annotation|@
name|Override
DECL|method|setExpression (ExpressionDefinition expression)
specifier|public
name|void
name|setExpression
parameter_list|(
name|ExpressionDefinition
name|expression
parameter_list|)
block|{
comment|// override to include javadoc what the expression is used for
name|super
operator|.
name|setExpression
argument_list|(
name|expression
argument_list|)
expr_stmt|;
block|}
DECL|method|getTimePeriodMillis ()
specifier|public
name|Long
name|getTimePeriodMillis
parameter_list|()
block|{
return|return
name|timePeriodMillis
return|;
block|}
DECL|method|setTimePeriodMillis (Long timePeriodMillis)
specifier|public
name|void
name|setTimePeriodMillis
parameter_list|(
name|Long
name|timePeriodMillis
parameter_list|)
block|{
name|this
operator|.
name|timePeriodMillis
operator|=
name|timePeriodMillis
expr_stmt|;
block|}
DECL|method|getAsyncDelayed ()
specifier|public
name|Boolean
name|getAsyncDelayed
parameter_list|()
block|{
return|return
name|asyncDelayed
return|;
block|}
DECL|method|setAsyncDelayed (Boolean asyncDelayed)
specifier|public
name|void
name|setAsyncDelayed
parameter_list|(
name|Boolean
name|asyncDelayed
parameter_list|)
block|{
name|this
operator|.
name|asyncDelayed
operator|=
name|asyncDelayed
expr_stmt|;
block|}
DECL|method|getCallerRunsWhenRejected ()
specifier|public
name|Boolean
name|getCallerRunsWhenRejected
parameter_list|()
block|{
return|return
name|callerRunsWhenRejected
return|;
block|}
DECL|method|setCallerRunsWhenRejected (Boolean callerRunsWhenRejected)
specifier|public
name|void
name|setCallerRunsWhenRejected
parameter_list|(
name|Boolean
name|callerRunsWhenRejected
parameter_list|)
block|{
name|this
operator|.
name|callerRunsWhenRejected
operator|=
name|callerRunsWhenRejected
expr_stmt|;
block|}
DECL|method|getExecutorService ()
specifier|public
name|ExecutorService
name|getExecutorService
parameter_list|()
block|{
return|return
name|executorService
return|;
block|}
DECL|method|setExecutorService (ExecutorService executorService)
specifier|public
name|void
name|setExecutorService
parameter_list|(
name|ExecutorService
name|executorService
parameter_list|)
block|{
name|this
operator|.
name|executorService
operator|=
name|executorService
expr_stmt|;
block|}
DECL|method|getExecutorServiceRef ()
specifier|public
name|String
name|getExecutorServiceRef
parameter_list|()
block|{
return|return
name|executorServiceRef
return|;
block|}
DECL|method|setExecutorServiceRef (String executorServiceRef)
specifier|public
name|void
name|setExecutorServiceRef
parameter_list|(
name|String
name|executorServiceRef
parameter_list|)
block|{
name|this
operator|.
name|executorServiceRef
operator|=
name|executorServiceRef
expr_stmt|;
block|}
DECL|method|getRejectExecution ()
specifier|public
name|Boolean
name|getRejectExecution
parameter_list|()
block|{
return|return
name|rejectExecution
return|;
block|}
DECL|method|setRejectExecution (Boolean rejectExecution)
specifier|public
name|void
name|setRejectExecution
parameter_list|(
name|Boolean
name|rejectExecution
parameter_list|)
block|{
name|this
operator|.
name|rejectExecution
operator|=
name|rejectExecution
expr_stmt|;
block|}
block|}
end_class

end_unit

