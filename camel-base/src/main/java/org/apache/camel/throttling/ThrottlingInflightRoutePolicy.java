begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.throttling
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|throttling
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|locks
operator|.
name|Lock
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
name|locks
operator|.
name|ReentrantLock
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
name|CamelContextAware
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
name|spi
operator|.
name|CamelEvent
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
name|CamelEvent
operator|.
name|ExchangeCompletedEvent
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
name|CamelLogger
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
name|EventNotifierSupport
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
name|RoutePolicySupport
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
name|service
operator|.
name|ServiceHelper
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
comment|/**  * A throttle based {@link org.apache.camel.spi.RoutePolicy} which is capable of dynamic  * throttling a route based on number of current inflight exchanges.  *<p/>  * This implementation supports two scopes {@link ThrottlingScope#Context} and {@link ThrottlingScope#Route} (is default).  * If context scope is selected then this implementation will use a {@link org.apache.camel.spi.EventNotifier} to listen  * for events when {@link Exchange}s is done, and trigger the {@link #throttle(org.apache.camel.Route, org.apache.camel.Exchange)}  * method. If the route scope is selected then<b>no</b> {@link org.apache.camel.spi.EventNotifier} is in use, as there is already  * a {@link org.apache.camel.spi.Synchronization} callback on the current {@link Exchange} which triggers the  * {@link #throttle(org.apache.camel.Route, org.apache.camel.Exchange)} when the current {@link Exchange} is done.  */
end_comment

begin_class
DECL|class|ThrottlingInflightRoutePolicy
specifier|public
class|class
name|ThrottlingInflightRoutePolicy
extends|extends
name|RoutePolicySupport
implements|implements
name|CamelContextAware
block|{
DECL|enum|ThrottlingScope
specifier|public
enum|enum
name|ThrottlingScope
block|{
DECL|enumConstant|Context
DECL|enumConstant|Route
name|Context
block|,
name|Route
block|}
DECL|field|routes
specifier|private
specifier|final
name|Set
argument_list|<
name|Route
argument_list|>
name|routes
init|=
operator|new
name|LinkedHashSet
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|eventNotifier
specifier|private
name|ContextScopedEventNotifier
name|eventNotifier
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|lock
specifier|private
specifier|final
name|Lock
name|lock
init|=
operator|new
name|ReentrantLock
argument_list|()
decl_stmt|;
DECL|field|scope
specifier|private
name|ThrottlingScope
name|scope
init|=
name|ThrottlingScope
operator|.
name|Route
decl_stmt|;
DECL|field|maxInflightExchanges
specifier|private
name|int
name|maxInflightExchanges
init|=
literal|1000
decl_stmt|;
DECL|field|resumePercentOfMax
specifier|private
name|int
name|resumePercentOfMax
init|=
literal|70
decl_stmt|;
DECL|field|resumeInflightExchanges
specifier|private
name|int
name|resumeInflightExchanges
init|=
literal|700
decl_stmt|;
DECL|field|loggingLevel
specifier|private
name|LoggingLevel
name|loggingLevel
init|=
name|LoggingLevel
operator|.
name|INFO
decl_stmt|;
DECL|field|logger
specifier|private
name|CamelLogger
name|logger
decl_stmt|;
DECL|method|ThrottlingInflightRoutePolicy ()
specifier|public
name|ThrottlingInflightRoutePolicy
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"ThrottlingInflightRoutePolicy["
operator|+
name|maxInflightExchanges
operator|+
literal|" / "
operator|+
name|resumePercentOfMax
operator|+
literal|"% using scope "
operator|+
name|scope
operator|+
literal|"]"
return|;
block|}
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onInit (Route route)
specifier|public
name|void
name|onInit
parameter_list|(
name|Route
name|route
parameter_list|)
block|{
comment|// we need to remember the routes we apply for
name|routes
operator|.
name|add
argument_list|(
name|route
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onExchangeDone (Route route, Exchange exchange)
specifier|public
name|void
name|onExchangeDone
parameter_list|(
name|Route
name|route
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// if route scoped then throttle directly
comment|// as context scoped is handled using an EventNotifier instead
if|if
condition|(
name|scope
operator|==
name|ThrottlingScope
operator|.
name|Route
condition|)
block|{
name|throttle
argument_list|(
name|route
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Throttles the route when {@link Exchange}s is done.      *      * @param route  the route      * @param exchange the exchange      */
DECL|method|throttle (Route route, Exchange exchange)
specifier|protected
name|void
name|throttle
parameter_list|(
name|Route
name|route
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// this works the best when this logic is executed when the exchange is done
name|Consumer
name|consumer
init|=
name|route
operator|.
name|getConsumer
argument_list|()
decl_stmt|;
name|int
name|size
init|=
name|getSize
argument_list|(
name|route
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|boolean
name|stop
init|=
name|maxInflightExchanges
operator|>
literal|0
operator|&&
name|size
operator|>
name|maxInflightExchanges
decl_stmt|;
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"{}> 0&& {}> {} evaluated as {}"
argument_list|,
name|maxInflightExchanges
argument_list|,
name|size
argument_list|,
name|maxInflightExchanges
argument_list|,
name|stop
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|stop
condition|)
block|{
try|try
block|{
name|lock
operator|.
name|lock
argument_list|()
expr_stmt|;
name|stopConsumer
argument_list|(
name|size
argument_list|,
name|consumer
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|handleException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|lock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
comment|// reload size in case a race condition with too many at once being invoked
comment|// so we need to ensure that we read the most current size and start the consumer if we are already to low
name|size
operator|=
name|getSize
argument_list|(
name|route
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|boolean
name|start
init|=
name|size
operator|<=
name|resumeInflightExchanges
decl_stmt|;
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"{}<= {} evaluated as {}"
argument_list|,
name|size
argument_list|,
name|resumeInflightExchanges
argument_list|,
name|start
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|start
condition|)
block|{
try|try
block|{
name|lock
operator|.
name|lock
argument_list|()
expr_stmt|;
name|startConsumer
argument_list|(
name|size
argument_list|,
name|consumer
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|handleException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|lock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|method|getMaxInflightExchanges ()
specifier|public
name|int
name|getMaxInflightExchanges
parameter_list|()
block|{
return|return
name|maxInflightExchanges
return|;
block|}
comment|/**      * Sets the upper limit of number of concurrent inflight exchanges at which point reached      * the throttler should suspend the route.      *<p/>      * Is default 1000.      *      * @param maxInflightExchanges the upper limit of concurrent inflight exchanges      */
DECL|method|setMaxInflightExchanges (int maxInflightExchanges)
specifier|public
name|void
name|setMaxInflightExchanges
parameter_list|(
name|int
name|maxInflightExchanges
parameter_list|)
block|{
name|this
operator|.
name|maxInflightExchanges
operator|=
name|maxInflightExchanges
expr_stmt|;
comment|// recalculate, must be at least at 1
name|this
operator|.
name|resumeInflightExchanges
operator|=
name|Math
operator|.
name|max
argument_list|(
name|resumePercentOfMax
operator|*
name|maxInflightExchanges
operator|/
literal|100
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
DECL|method|getResumePercentOfMax ()
specifier|public
name|int
name|getResumePercentOfMax
parameter_list|()
block|{
return|return
name|resumePercentOfMax
return|;
block|}
comment|/**      * Sets at which percentage of the max the throttler should start resuming the route.      *<p/>      * Will by default use 70%.      *      * @param resumePercentOfMax the percentage must be between 0 and 100      */
DECL|method|setResumePercentOfMax (int resumePercentOfMax)
specifier|public
name|void
name|setResumePercentOfMax
parameter_list|(
name|int
name|resumePercentOfMax
parameter_list|)
block|{
if|if
condition|(
name|resumePercentOfMax
argument_list|<
literal|0
operator|||
name|resumePercentOfMax
argument_list|>
literal|100
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Must be a percentage between 0 and 100, was: "
operator|+
name|resumePercentOfMax
argument_list|)
throw|;
block|}
name|this
operator|.
name|resumePercentOfMax
operator|=
name|resumePercentOfMax
expr_stmt|;
comment|// recalculate, must be at least at 1
name|this
operator|.
name|resumeInflightExchanges
operator|=
name|Math
operator|.
name|max
argument_list|(
name|resumePercentOfMax
operator|*
name|maxInflightExchanges
operator|/
literal|100
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
DECL|method|getScope ()
specifier|public
name|ThrottlingScope
name|getScope
parameter_list|()
block|{
return|return
name|scope
return|;
block|}
comment|/**      * Sets which scope the throttling should be based upon, either route or total scoped.      *      * @param scope the scope      */
DECL|method|setScope (ThrottlingScope scope)
specifier|public
name|void
name|setScope
parameter_list|(
name|ThrottlingScope
name|scope
parameter_list|)
block|{
name|this
operator|.
name|scope
operator|=
name|scope
expr_stmt|;
block|}
DECL|method|getLoggingLevel ()
specifier|public
name|LoggingLevel
name|getLoggingLevel
parameter_list|()
block|{
return|return
name|loggingLevel
return|;
block|}
DECL|method|getLogger ()
specifier|public
name|CamelLogger
name|getLogger
parameter_list|()
block|{
if|if
condition|(
name|logger
operator|==
literal|null
condition|)
block|{
name|logger
operator|=
name|createLogger
argument_list|()
expr_stmt|;
block|}
return|return
name|logger
return|;
block|}
comment|/**      * Sets the logger to use for logging throttling activity.      *      * @param logger the logger      */
DECL|method|setLogger (CamelLogger logger)
specifier|public
name|void
name|setLogger
parameter_list|(
name|CamelLogger
name|logger
parameter_list|)
block|{
name|this
operator|.
name|logger
operator|=
name|logger
expr_stmt|;
block|}
comment|/**      * Sets the logging level to report the throttling activity.      *<p/>      * Is default<tt>INFO</tt> level.      *      * @param loggingLevel the logging level      */
DECL|method|setLoggingLevel (LoggingLevel loggingLevel)
specifier|public
name|void
name|setLoggingLevel
parameter_list|(
name|LoggingLevel
name|loggingLevel
parameter_list|)
block|{
name|this
operator|.
name|loggingLevel
operator|=
name|loggingLevel
expr_stmt|;
block|}
DECL|method|createLogger ()
specifier|protected
name|CamelLogger
name|createLogger
parameter_list|()
block|{
return|return
operator|new
name|CamelLogger
argument_list|(
name|log
argument_list|,
name|getLoggingLevel
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getSize (Route route, Exchange exchange)
specifier|private
name|int
name|getSize
parameter_list|(
name|Route
name|route
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|scope
operator|==
name|ThrottlingScope
operator|.
name|Context
condition|)
block|{
return|return
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getInflightRepository
argument_list|()
operator|.
name|size
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getInflightRepository
argument_list|()
operator|.
name|size
argument_list|(
name|route
operator|.
name|getId
argument_list|()
argument_list|)
return|;
block|}
block|}
DECL|method|startConsumer (int size, Consumer consumer)
specifier|private
name|void
name|startConsumer
parameter_list|(
name|int
name|size
parameter_list|,
name|Consumer
name|consumer
parameter_list|)
throws|throws
name|Exception
block|{
name|boolean
name|started
init|=
name|resumeOrStartConsumer
argument_list|(
name|consumer
argument_list|)
decl_stmt|;
if|if
condition|(
name|started
condition|)
block|{
name|getLogger
argument_list|()
operator|.
name|log
argument_list|(
literal|"Throttling consumer: "
operator|+
name|size
operator|+
literal|"<= "
operator|+
name|resumeInflightExchanges
operator|+
literal|" inflight exchange by resuming consumer: "
operator|+
name|consumer
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|stopConsumer (int size, Consumer consumer)
specifier|private
name|void
name|stopConsumer
parameter_list|(
name|int
name|size
parameter_list|,
name|Consumer
name|consumer
parameter_list|)
throws|throws
name|Exception
block|{
name|boolean
name|stopped
init|=
name|suspendOrStopConsumer
argument_list|(
name|consumer
argument_list|)
decl_stmt|;
if|if
condition|(
name|stopped
condition|)
block|{
name|getLogger
argument_list|()
operator|.
name|log
argument_list|(
literal|"Throttling consumer: "
operator|+
name|size
operator|+
literal|"> "
operator|+
name|maxInflightExchanges
operator|+
literal|" inflight exchange by suspending consumer: "
operator|+
name|consumer
argument_list|)
expr_stmt|;
block|}
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
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|camelContext
argument_list|,
literal|"CamelContext"
argument_list|,
name|this
argument_list|)
expr_stmt|;
if|if
condition|(
name|scope
operator|==
name|ThrottlingScope
operator|.
name|Context
condition|)
block|{
name|eventNotifier
operator|=
operator|new
name|ContextScopedEventNotifier
argument_list|()
expr_stmt|;
comment|// must start the notifier before it can be used
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|eventNotifier
argument_list|)
expr_stmt|;
comment|// we are in context scope, so we need to use an event notifier to keep track
comment|// when any exchanges is done on the camel context.
comment|// This ensures we can trigger accordingly to context scope
name|camelContext
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|addEventNotifier
argument_list|(
name|eventNotifier
argument_list|)
expr_stmt|;
block|}
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
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|camelContext
argument_list|,
literal|"CamelContext"
argument_list|,
name|this
argument_list|)
expr_stmt|;
if|if
condition|(
name|scope
operator|==
name|ThrottlingScope
operator|.
name|Context
condition|)
block|{
name|camelContext
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|removeEventNotifier
argument_list|(
name|eventNotifier
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * {@link org.apache.camel.spi.EventNotifier} to keep track on when {@link Exchange}      * is done, so we can throttle accordingly.      */
DECL|class|ContextScopedEventNotifier
specifier|private
class|class
name|ContextScopedEventNotifier
extends|extends
name|EventNotifierSupport
block|{
annotation|@
name|Override
DECL|method|notify (CamelEvent event)
specifier|public
name|void
name|notify
parameter_list|(
name|CamelEvent
name|event
parameter_list|)
throws|throws
name|Exception
block|{
name|ExchangeCompletedEvent
name|completedEvent
init|=
operator|(
name|ExchangeCompletedEvent
operator|)
name|event
decl_stmt|;
for|for
control|(
name|Route
name|route
range|:
name|routes
control|)
block|{
name|throttle
argument_list|(
name|route
argument_list|,
name|completedEvent
operator|.
name|getExchange
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|isEnabled (CamelEvent event)
specifier|public
name|boolean
name|isEnabled
parameter_list|(
name|CamelEvent
name|event
parameter_list|)
block|{
return|return
name|event
operator|instanceof
name|ExchangeCompletedEvent
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
literal|"ContextScopedEventNotifier"
return|;
block|}
block|}
block|}
end_class

end_unit

