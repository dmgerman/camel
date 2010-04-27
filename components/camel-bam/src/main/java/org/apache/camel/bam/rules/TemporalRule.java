begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.bam.rules
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|bam
operator|.
name|rules
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
name|Date
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
name|bam
operator|.
name|TimeExpression
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
name|bam
operator|.
name|model
operator|.
name|ActivityState
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
name|bam
operator|.
name|model
operator|.
name|ProcessInstance
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
name|DefaultExchange
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
name|DefaultRouteContext
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
name|OutputDefinition
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
name|Time
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
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ServiceHelper
operator|.
name|startServices
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
name|util
operator|.
name|ServiceHelper
operator|.
name|stopServices
import|;
end_import

begin_comment
comment|/**  * A temporal rule for use within BAM  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|TemporalRule
specifier|public
class|class
name|TemporalRule
extends|extends
name|ServiceSupport
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|TemporalRule
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|first
specifier|private
name|TimeExpression
name|first
decl_stmt|;
DECL|field|second
specifier|private
name|TimeExpression
name|second
decl_stmt|;
DECL|field|expectedMillis
specifier|private
name|long
name|expectedMillis
decl_stmt|;
DECL|field|overdueMillis
specifier|private
name|long
name|overdueMillis
decl_stmt|;
DECL|field|overdueAction
specifier|private
name|Processor
name|overdueAction
decl_stmt|;
DECL|field|overdueProcessors
specifier|private
name|OutputDefinition
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
name|overdueProcessors
init|=
operator|new
name|OutputDefinition
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|TemporalRule (TimeExpression first, TimeExpression second)
specifier|public
name|TemporalRule
parameter_list|(
name|TimeExpression
name|first
parameter_list|,
name|TimeExpression
name|second
parameter_list|)
block|{
name|this
operator|.
name|first
operator|=
name|first
expr_stmt|;
name|this
operator|.
name|second
operator|=
name|second
expr_stmt|;
block|}
DECL|method|expectWithin (Time builder)
specifier|public
name|TemporalRule
name|expectWithin
parameter_list|(
name|Time
name|builder
parameter_list|)
block|{
return|return
name|expectWithin
argument_list|(
name|builder
operator|.
name|toMillis
argument_list|()
argument_list|)
return|;
block|}
DECL|method|expectWithin (long millis)
specifier|public
name|TemporalRule
name|expectWithin
parameter_list|(
name|long
name|millis
parameter_list|)
block|{
name|expectedMillis
operator|=
name|millis
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|errorIfOver (Time builder)
specifier|public
name|OutputDefinition
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
name|errorIfOver
parameter_list|(
name|Time
name|builder
parameter_list|)
block|{
return|return
name|errorIfOver
argument_list|(
name|builder
operator|.
name|toMillis
argument_list|()
argument_list|)
return|;
block|}
DECL|method|errorIfOver (long millis)
specifier|public
name|OutputDefinition
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
name|errorIfOver
parameter_list|(
name|long
name|millis
parameter_list|)
block|{
name|overdueMillis
operator|=
name|millis
expr_stmt|;
return|return
name|overdueProcessors
return|;
block|}
DECL|method|getFirst ()
specifier|public
name|TimeExpression
name|getFirst
parameter_list|()
block|{
return|return
name|first
return|;
block|}
DECL|method|getSecond ()
specifier|public
name|TimeExpression
name|getSecond
parameter_list|()
block|{
return|return
name|second
return|;
block|}
DECL|method|getOverdueAction ()
specifier|public
name|Processor
name|getOverdueAction
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|overdueAction
operator|==
literal|null
operator|&&
name|overdueProcessors
operator|!=
literal|null
condition|)
block|{
name|RouteDefinition
name|route
init|=
operator|new
name|RouteDefinition
argument_list|()
decl_stmt|;
name|RouteContext
name|routeContext
init|=
operator|new
name|DefaultRouteContext
argument_list|(
name|first
operator|.
name|getBuilder
argument_list|()
operator|.
name|getProcessBuilder
argument_list|()
operator|.
name|getContext
argument_list|()
argument_list|,
name|route
argument_list|,
literal|null
argument_list|,
operator|new
name|ArrayList
argument_list|<
name|Route
argument_list|>
argument_list|()
argument_list|)
decl_stmt|;
name|overdueAction
operator|=
name|overdueProcessors
operator|.
name|createOutputsProcessor
argument_list|(
name|routeContext
argument_list|)
expr_stmt|;
block|}
return|return
name|overdueAction
return|;
block|}
DECL|method|processExchange (Exchange exchange, ProcessInstance instance)
specifier|public
name|void
name|processExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|ProcessInstance
name|instance
parameter_list|)
block|{
name|Date
name|firstTime
init|=
name|first
operator|.
name|evaluate
argument_list|(
name|instance
argument_list|)
decl_stmt|;
if|if
condition|(
name|firstTime
operator|==
literal|null
condition|)
block|{
comment|// ignore as first event has not occurred yet
return|return;
block|}
comment|// TODO now we might need to set the second activity state
comment|// to 'grey' to indicate it now could happen?
comment|// lets force the lazy creation of the second state
name|ActivityState
name|secondState
init|=
name|second
operator|.
name|getOrCreateActivityState
argument_list|(
name|instance
argument_list|)
decl_stmt|;
if|if
condition|(
name|expectedMillis
operator|>
literal|0L
condition|)
block|{
name|Date
name|expected
init|=
name|secondState
operator|.
name|getTimeExpected
argument_list|()
decl_stmt|;
if|if
condition|(
name|expected
operator|==
literal|null
condition|)
block|{
name|expected
operator|=
name|add
argument_list|(
name|firstTime
argument_list|,
name|expectedMillis
argument_list|)
expr_stmt|;
name|secondState
operator|.
name|setTimeExpected
argument_list|(
name|expected
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|overdueMillis
operator|>
literal|0L
condition|)
block|{
name|Date
name|overdue
init|=
name|secondState
operator|.
name|getTimeOverdue
argument_list|()
decl_stmt|;
if|if
condition|(
name|overdue
operator|==
literal|null
condition|)
block|{
name|overdue
operator|=
name|add
argument_list|(
name|firstTime
argument_list|,
name|overdueMillis
argument_list|)
expr_stmt|;
name|secondState
operator|.
name|setTimeOverdue
argument_list|(
name|overdue
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|processExpired (ActivityState activityState)
specifier|public
name|void
name|processExpired
parameter_list|(
name|ActivityState
name|activityState
parameter_list|)
throws|throws
name|Exception
block|{
name|Processor
name|processor
init|=
name|getOverdueAction
argument_list|()
decl_stmt|;
if|if
condition|(
name|processor
operator|!=
literal|null
condition|)
block|{
name|Date
name|now
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
comment|/*             TODO this doesn't work and returns null for some strange reason             ProcessInstance instance = activityState.getProcessInstance();             ActivityState secondState = second.getActivityState(instance);             if (secondState == null) {                 log.error("Could not find the second state! Process is: "                  + instance + " with first state: " + first.getActivityState(instance)                  + " and the state I was called with was: " + activityState);             } */
name|ActivityState
name|secondState
init|=
name|activityState
decl_stmt|;
name|Date
name|overdue
init|=
name|secondState
operator|.
name|getTimeOverdue
argument_list|()
decl_stmt|;
if|if
condition|(
name|now
operator|.
name|compareTo
argument_list|(
name|overdue
argument_list|)
operator|>=
literal|0
condition|)
block|{
name|Exchange
name|exchange
init|=
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|activityState
argument_list|)
expr_stmt|;
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Process has not actually expired; the time is: "
operator|+
name|now
operator|+
literal|" but the overdue time is: "
operator|+
name|overdue
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|createExchange ()
specifier|protected
name|Exchange
name|createExchange
parameter_list|()
block|{
return|return
operator|new
name|DefaultExchange
argument_list|(
name|second
operator|.
name|getBuilder
argument_list|()
operator|.
name|getProcessBuilder
argument_list|()
operator|.
name|getContext
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Returns the date in the future adding the given number of millis      *      * @return the date in the future      */
DECL|method|add (Date date, long millis)
specifier|protected
name|Date
name|add
parameter_list|(
name|Date
name|date
parameter_list|,
name|long
name|millis
parameter_list|)
block|{
return|return
operator|new
name|Date
argument_list|(
name|date
operator|.
name|getTime
argument_list|()
operator|+
name|millis
argument_list|)
return|;
block|}
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|startServices
argument_list|(
name|getOverdueAction
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|stopServices
argument_list|(
name|getOverdueAction
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

