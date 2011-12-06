begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.management.mbean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|management
operator|.
name|mbean
package|;
end_package

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
name|api
operator|.
name|management
operator|.
name|ManagedResource
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
name|api
operator|.
name|management
operator|.
name|PerformanceCounter
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
name|api
operator|.
name|management
operator|.
name|mbean
operator|.
name|ManagedPerformanceCounterMBean
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
name|ManagementStrategy
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
name|ExchangeHelper
import|;
end_import

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"PerformanceCounter"
argument_list|)
DECL|class|ManagedPerformanceCounter
specifier|public
specifier|abstract
class|class
name|ManagedPerformanceCounter
extends|extends
name|ManagedCounter
implements|implements
name|PerformanceCounter
implements|,
name|ManagedPerformanceCounterMBean
block|{
DECL|field|exchangesCompleted
specifier|private
name|Statistic
name|exchangesCompleted
decl_stmt|;
DECL|field|exchangesFailed
specifier|private
name|Statistic
name|exchangesFailed
decl_stmt|;
DECL|field|failuresHandled
specifier|private
name|Statistic
name|failuresHandled
decl_stmt|;
DECL|field|redeliveries
specifier|private
name|Statistic
name|redeliveries
decl_stmt|;
DECL|field|minProcessingTime
specifier|private
name|Statistic
name|minProcessingTime
decl_stmt|;
DECL|field|maxProcessingTime
specifier|private
name|Statistic
name|maxProcessingTime
decl_stmt|;
DECL|field|totalProcessingTime
specifier|private
name|Statistic
name|totalProcessingTime
decl_stmt|;
DECL|field|lastProcessingTime
specifier|private
name|Statistic
name|lastProcessingTime
decl_stmt|;
DECL|field|meanProcessingTime
specifier|private
name|Statistic
name|meanProcessingTime
decl_stmt|;
DECL|field|firstExchangeCompletedTimestamp
specifier|private
name|Statistic
name|firstExchangeCompletedTimestamp
decl_stmt|;
DECL|field|firstExchangeFailureTimestamp
specifier|private
name|Statistic
name|firstExchangeFailureTimestamp
decl_stmt|;
DECL|field|lastExchangeCompletedTimestamp
specifier|private
name|Statistic
name|lastExchangeCompletedTimestamp
decl_stmt|;
DECL|field|lastExchangeFailureTimestamp
specifier|private
name|Statistic
name|lastExchangeFailureTimestamp
decl_stmt|;
DECL|field|statisticsEnabled
specifier|private
name|boolean
name|statisticsEnabled
init|=
literal|true
decl_stmt|;
DECL|method|init (ManagementStrategy strategy)
specifier|public
name|void
name|init
parameter_list|(
name|ManagementStrategy
name|strategy
parameter_list|)
block|{
name|super
operator|.
name|init
argument_list|(
name|strategy
argument_list|)
expr_stmt|;
name|this
operator|.
name|exchangesCompleted
operator|=
operator|new
name|Statistic
argument_list|(
literal|"org.apache.camel.exchangesCompleted"
argument_list|,
name|this
argument_list|,
name|Statistic
operator|.
name|UpdateMode
operator|.
name|COUNTER
argument_list|)
expr_stmt|;
name|this
operator|.
name|exchangesFailed
operator|=
operator|new
name|Statistic
argument_list|(
literal|"org.apache.camel.exchangesFailed"
argument_list|,
name|this
argument_list|,
name|Statistic
operator|.
name|UpdateMode
operator|.
name|COUNTER
argument_list|)
expr_stmt|;
name|this
operator|.
name|failuresHandled
operator|=
operator|new
name|Statistic
argument_list|(
literal|"org.apache.camel.failuresHandled"
argument_list|,
name|this
argument_list|,
name|Statistic
operator|.
name|UpdateMode
operator|.
name|COUNTER
argument_list|)
expr_stmt|;
name|this
operator|.
name|redeliveries
operator|=
operator|new
name|Statistic
argument_list|(
literal|"org.apache.camel.redeliveries"
argument_list|,
name|this
argument_list|,
name|Statistic
operator|.
name|UpdateMode
operator|.
name|COUNTER
argument_list|)
expr_stmt|;
name|this
operator|.
name|minProcessingTime
operator|=
operator|new
name|Statistic
argument_list|(
literal|"org.apache.camel.minimumProcessingTime"
argument_list|,
name|this
argument_list|,
name|Statistic
operator|.
name|UpdateMode
operator|.
name|MINIMUM
argument_list|)
expr_stmt|;
name|this
operator|.
name|maxProcessingTime
operator|=
operator|new
name|Statistic
argument_list|(
literal|"org.apache.camel.maximumProcessingTime"
argument_list|,
name|this
argument_list|,
name|Statistic
operator|.
name|UpdateMode
operator|.
name|MAXIMUM
argument_list|)
expr_stmt|;
name|this
operator|.
name|totalProcessingTime
operator|=
operator|new
name|Statistic
argument_list|(
literal|"org.apache.camel.totalProcessingTime"
argument_list|,
name|this
argument_list|,
name|Statistic
operator|.
name|UpdateMode
operator|.
name|COUNTER
argument_list|)
expr_stmt|;
name|this
operator|.
name|lastProcessingTime
operator|=
operator|new
name|Statistic
argument_list|(
literal|"org.apache.camel.lastProcessingTime"
argument_list|,
name|this
argument_list|,
name|Statistic
operator|.
name|UpdateMode
operator|.
name|VALUE
argument_list|)
expr_stmt|;
name|this
operator|.
name|meanProcessingTime
operator|=
operator|new
name|Statistic
argument_list|(
literal|"org.apache.camel.meanProcessingTime"
argument_list|,
name|this
argument_list|,
name|Statistic
operator|.
name|UpdateMode
operator|.
name|VALUE
argument_list|)
expr_stmt|;
name|this
operator|.
name|firstExchangeCompletedTimestamp
operator|=
operator|new
name|Statistic
argument_list|(
literal|"org.apache.camel.firstExchangeCompletedTimestamp"
argument_list|,
name|this
argument_list|,
name|Statistic
operator|.
name|UpdateMode
operator|.
name|VALUE
argument_list|)
expr_stmt|;
name|this
operator|.
name|firstExchangeFailureTimestamp
operator|=
operator|new
name|Statistic
argument_list|(
literal|"org.apache.camel.firstExchangeFailureTimestamp"
argument_list|,
name|this
argument_list|,
name|Statistic
operator|.
name|UpdateMode
operator|.
name|VALUE
argument_list|)
expr_stmt|;
name|this
operator|.
name|lastExchangeCompletedTimestamp
operator|=
operator|new
name|Statistic
argument_list|(
literal|"org.apache.camel.lastExchangeCompletedTimestamp"
argument_list|,
name|this
argument_list|,
name|Statistic
operator|.
name|UpdateMode
operator|.
name|VALUE
argument_list|)
expr_stmt|;
name|this
operator|.
name|lastExchangeFailureTimestamp
operator|=
operator|new
name|Statistic
argument_list|(
literal|"org.apache.camel.lastExchangeFailureTimestamp"
argument_list|,
name|this
argument_list|,
name|Statistic
operator|.
name|UpdateMode
operator|.
name|VALUE
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|reset ()
specifier|public
specifier|synchronized
name|void
name|reset
parameter_list|()
block|{
name|super
operator|.
name|reset
argument_list|()
expr_stmt|;
name|exchangesCompleted
operator|.
name|reset
argument_list|()
expr_stmt|;
name|exchangesFailed
operator|.
name|reset
argument_list|()
expr_stmt|;
name|failuresHandled
operator|.
name|reset
argument_list|()
expr_stmt|;
name|redeliveries
operator|.
name|reset
argument_list|()
expr_stmt|;
name|minProcessingTime
operator|.
name|reset
argument_list|()
expr_stmt|;
name|maxProcessingTime
operator|.
name|reset
argument_list|()
expr_stmt|;
name|totalProcessingTime
operator|.
name|reset
argument_list|()
expr_stmt|;
name|lastProcessingTime
operator|.
name|reset
argument_list|()
expr_stmt|;
name|meanProcessingTime
operator|.
name|reset
argument_list|()
expr_stmt|;
name|firstExchangeCompletedTimestamp
operator|.
name|reset
argument_list|()
expr_stmt|;
name|firstExchangeFailureTimestamp
operator|.
name|reset
argument_list|()
expr_stmt|;
name|lastExchangeCompletedTimestamp
operator|.
name|reset
argument_list|()
expr_stmt|;
name|lastExchangeFailureTimestamp
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
DECL|method|getExchangesCompleted ()
specifier|public
name|long
name|getExchangesCompleted
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|exchangesCompleted
operator|.
name|getValue
argument_list|()
return|;
block|}
DECL|method|getExchangesFailed ()
specifier|public
name|long
name|getExchangesFailed
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|exchangesFailed
operator|.
name|getValue
argument_list|()
return|;
block|}
DECL|method|getFailuresHandled ()
specifier|public
name|long
name|getFailuresHandled
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|failuresHandled
operator|.
name|getValue
argument_list|()
return|;
block|}
DECL|method|getRedeliveries ()
specifier|public
name|long
name|getRedeliveries
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|redeliveries
operator|.
name|getValue
argument_list|()
return|;
block|}
DECL|method|getMinProcessingTime ()
specifier|public
name|long
name|getMinProcessingTime
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|minProcessingTime
operator|.
name|getValue
argument_list|()
return|;
block|}
DECL|method|getMeanProcessingTime ()
specifier|public
name|long
name|getMeanProcessingTime
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|meanProcessingTime
operator|.
name|getValue
argument_list|()
return|;
block|}
DECL|method|getMaxProcessingTime ()
specifier|public
name|long
name|getMaxProcessingTime
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|maxProcessingTime
operator|.
name|getValue
argument_list|()
return|;
block|}
DECL|method|getTotalProcessingTime ()
specifier|public
name|long
name|getTotalProcessingTime
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|totalProcessingTime
operator|.
name|getValue
argument_list|()
return|;
block|}
DECL|method|getLastProcessingTime ()
specifier|public
name|long
name|getLastProcessingTime
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|lastProcessingTime
operator|.
name|getValue
argument_list|()
return|;
block|}
DECL|method|getLastExchangeCompletedTimestamp ()
specifier|public
name|Date
name|getLastExchangeCompletedTimestamp
parameter_list|()
block|{
name|long
name|value
init|=
name|lastExchangeCompletedTimestamp
operator|.
name|getValue
argument_list|()
decl_stmt|;
return|return
name|value
operator|>
literal|0
condition|?
operator|new
name|Date
argument_list|(
name|value
argument_list|)
else|:
literal|null
return|;
block|}
DECL|method|getFirstExchangeCompletedTimestamp ()
specifier|public
name|Date
name|getFirstExchangeCompletedTimestamp
parameter_list|()
block|{
name|long
name|value
init|=
name|firstExchangeCompletedTimestamp
operator|.
name|getValue
argument_list|()
decl_stmt|;
return|return
name|value
operator|>
literal|0
condition|?
operator|new
name|Date
argument_list|(
name|value
argument_list|)
else|:
literal|null
return|;
block|}
DECL|method|getLastExchangeFailureTimestamp ()
specifier|public
name|Date
name|getLastExchangeFailureTimestamp
parameter_list|()
block|{
name|long
name|value
init|=
name|lastExchangeFailureTimestamp
operator|.
name|getValue
argument_list|()
decl_stmt|;
return|return
name|value
operator|>
literal|0
condition|?
operator|new
name|Date
argument_list|(
name|value
argument_list|)
else|:
literal|null
return|;
block|}
DECL|method|getFirstExchangeFailureTimestamp ()
specifier|public
name|Date
name|getFirstExchangeFailureTimestamp
parameter_list|()
block|{
name|long
name|value
init|=
name|firstExchangeFailureTimestamp
operator|.
name|getValue
argument_list|()
decl_stmt|;
return|return
name|value
operator|>
literal|0
condition|?
operator|new
name|Date
argument_list|(
name|value
argument_list|)
else|:
literal|null
return|;
block|}
DECL|method|isStatisticsEnabled ()
specifier|public
name|boolean
name|isStatisticsEnabled
parameter_list|()
block|{
return|return
name|statisticsEnabled
return|;
block|}
DECL|method|setStatisticsEnabled (boolean statisticsEnabled)
specifier|public
name|void
name|setStatisticsEnabled
parameter_list|(
name|boolean
name|statisticsEnabled
parameter_list|)
block|{
name|this
operator|.
name|statisticsEnabled
operator|=
name|statisticsEnabled
expr_stmt|;
block|}
DECL|method|completedExchange (Exchange exchange, long time)
specifier|public
specifier|synchronized
name|void
name|completedExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|long
name|time
parameter_list|)
block|{
name|increment
argument_list|()
expr_stmt|;
name|exchangesCompleted
operator|.
name|increment
argument_list|()
expr_stmt|;
if|if
condition|(
name|ExchangeHelper
operator|.
name|isFailureHandled
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
name|failuresHandled
operator|.
name|increment
argument_list|()
expr_stmt|;
block|}
name|minProcessingTime
operator|.
name|updateValue
argument_list|(
name|time
argument_list|)
expr_stmt|;
name|maxProcessingTime
operator|.
name|updateValue
argument_list|(
name|time
argument_list|)
expr_stmt|;
name|totalProcessingTime
operator|.
name|updateValue
argument_list|(
name|time
argument_list|)
expr_stmt|;
name|lastProcessingTime
operator|.
name|updateValue
argument_list|(
name|time
argument_list|)
expr_stmt|;
name|long
name|now
init|=
operator|new
name|Date
argument_list|()
operator|.
name|getTime
argument_list|()
decl_stmt|;
if|if
condition|(
name|firstExchangeCompletedTimestamp
operator|.
name|getUpdateCount
argument_list|()
operator|==
literal|0
condition|)
block|{
name|firstExchangeCompletedTimestamp
operator|.
name|updateValue
argument_list|(
name|now
argument_list|)
expr_stmt|;
block|}
name|lastExchangeCompletedTimestamp
operator|.
name|updateValue
argument_list|(
name|now
argument_list|)
expr_stmt|;
comment|// update mean
name|long
name|count
init|=
name|exchangesCompleted
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|long
name|mean
init|=
name|count
operator|>
literal|0
condition|?
name|totalProcessingTime
operator|.
name|getValue
argument_list|()
operator|/
name|count
else|:
literal|0
decl_stmt|;
name|meanProcessingTime
operator|.
name|updateValue
argument_list|(
name|mean
argument_list|)
expr_stmt|;
block|}
DECL|method|failedExchange (Exchange exchange)
specifier|public
specifier|synchronized
name|void
name|failedExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|increment
argument_list|()
expr_stmt|;
name|exchangesFailed
operator|.
name|increment
argument_list|()
expr_stmt|;
if|if
condition|(
name|ExchangeHelper
operator|.
name|isRedelivered
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
name|redeliveries
operator|.
name|increment
argument_list|()
expr_stmt|;
block|}
name|long
name|now
init|=
operator|new
name|Date
argument_list|()
operator|.
name|getTime
argument_list|()
decl_stmt|;
if|if
condition|(
name|firstExchangeFailureTimestamp
operator|.
name|getUpdateCount
argument_list|()
operator|==
literal|0
condition|)
block|{
name|firstExchangeFailureTimestamp
operator|.
name|updateValue
argument_list|(
name|now
argument_list|)
expr_stmt|;
block|}
name|lastExchangeFailureTimestamp
operator|.
name|updateValue
argument_list|(
name|now
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

