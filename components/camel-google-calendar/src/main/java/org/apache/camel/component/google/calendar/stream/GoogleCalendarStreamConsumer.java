begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.google.calendar.stream
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|google
operator|.
name|calendar
operator|.
name|stream
package|;
end_package

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|DateFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
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
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|java
operator|.
name|util
operator|.
name|Queue
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
name|ScheduledBatchPollingConsumer
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

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|client
operator|.
name|util
operator|.
name|DateTime
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|calendar
operator|.
name|Calendar
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|calendar
operator|.
name|model
operator|.
name|Event
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|calendar
operator|.
name|model
operator|.
name|Events
import|;
end_import

begin_comment
comment|/**  * The GoogleCalendar consumer.  */
end_comment

begin_class
DECL|class|GoogleCalendarStreamConsumer
specifier|public
class|class
name|GoogleCalendarStreamConsumer
extends|extends
name|ScheduledBatchPollingConsumer
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|GoogleCalendarStreamConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|GoogleCalendarStreamConsumer (Endpoint endpoint, Processor processor)
specifier|public
name|GoogleCalendarStreamConsumer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
block|}
DECL|method|getConfiguration ()
specifier|protected
name|GoogleCalendarStreamConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
return|;
block|}
DECL|method|getClient ()
specifier|protected
name|Calendar
name|getClient
parameter_list|()
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getClient
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|GoogleCalendarStreamEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|GoogleCalendarStreamEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|poll ()
specifier|protected
name|int
name|poll
parameter_list|()
throws|throws
name|Exception
block|{
name|Date
name|date
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|calendar
operator|.
name|Calendar
operator|.
name|Events
operator|.
name|List
name|request
init|=
name|getClient
argument_list|()
operator|.
name|events
argument_list|()
operator|.
name|list
argument_list|(
literal|"primary"
argument_list|)
operator|.
name|setOrderBy
argument_list|(
literal|"updated"
argument_list|)
operator|.
name|setTimeMin
argument_list|(
operator|new
name|DateTime
argument_list|(
name|date
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getQuery
argument_list|()
argument_list|)
condition|)
block|{
name|request
operator|.
name|setQ
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getQuery
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getMaxResults
argument_list|()
argument_list|)
condition|)
block|{
name|request
operator|.
name|setMaxResults
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getMaxResults
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Queue
argument_list|<
name|Exchange
argument_list|>
name|answer
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
name|Events
name|c
init|=
name|request
operator|.
name|execute
argument_list|()
decl_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|Event
argument_list|>
name|list
init|=
name|c
operator|.
name|getItems
argument_list|()
decl_stmt|;
for|for
control|(
name|Event
name|event
range|:
name|list
control|)
block|{
name|Exchange
name|exchange
init|=
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getExchangePattern
argument_list|()
argument_list|,
name|event
argument_list|)
decl_stmt|;
name|answer
operator|.
name|add
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|processBatch
argument_list|(
name|CastUtils
operator|.
name|cast
argument_list|(
name|answer
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|processBatch (Queue<Object> exchanges)
specifier|public
name|int
name|processBatch
parameter_list|(
name|Queue
argument_list|<
name|Object
argument_list|>
name|exchanges
parameter_list|)
throws|throws
name|Exception
block|{
name|int
name|total
init|=
name|exchanges
operator|.
name|size
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|index
init|=
literal|0
init|;
name|index
operator|<
name|total
operator|&&
name|isBatchAllowed
argument_list|()
condition|;
name|index
operator|++
control|)
block|{
comment|// only loop if we are started (allowed to run)
specifier|final
name|Exchange
name|exchange
init|=
name|ObjectHelper
operator|.
name|cast
argument_list|(
name|Exchange
operator|.
name|class
argument_list|,
name|exchanges
operator|.
name|poll
argument_list|()
argument_list|)
decl_stmt|;
comment|// add current index and total as properties
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_INDEX
argument_list|,
name|index
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_SIZE
argument_list|,
name|total
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_COMPLETE
argument_list|,
name|index
operator|==
name|total
operator|-
literal|1
argument_list|)
expr_stmt|;
comment|// update pending number of exchanges
name|pendingExchanges
operator|=
name|total
operator|-
name|index
operator|-
literal|1
expr_stmt|;
name|getAsyncProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
operator|new
name|AsyncCallback
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|doneSync
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Processing exchange done"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
return|return
name|total
return|;
block|}
block|}
end_class

end_unit

