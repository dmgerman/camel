begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.management
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|management
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
name|Exchange
import|;
end_import

begin_comment
comment|/**  * A composite {@link PerformanceCounter} is used for tracking performance statistics on both a per  * context and route level, by issuing callbacks on both when an event happens.  *<p/>  * This implementation is used so the {@link org.apache.camel.management.mbean.ManagedCamelContext}  * can aggregate all stats from the routes.  */
end_comment

begin_class
DECL|class|CompositePerformanceCounter
specifier|public
class|class
name|CompositePerformanceCounter
implements|implements
name|PerformanceCounter
block|{
DECL|field|counter1
specifier|private
specifier|final
name|PerformanceCounter
name|counter1
decl_stmt|;
DECL|field|counter2
specifier|private
specifier|final
name|PerformanceCounter
name|counter2
decl_stmt|;
DECL|method|CompositePerformanceCounter (PerformanceCounter counter1, PerformanceCounter counter2)
specifier|public
name|CompositePerformanceCounter
parameter_list|(
name|PerformanceCounter
name|counter1
parameter_list|,
name|PerformanceCounter
name|counter2
parameter_list|)
block|{
name|this
operator|.
name|counter1
operator|=
name|counter1
expr_stmt|;
name|this
operator|.
name|counter2
operator|=
name|counter2
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|processExchange (Exchange exchange)
specifier|public
name|void
name|processExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|counter1
operator|.
name|isStatisticsEnabled
argument_list|()
condition|)
block|{
name|counter1
operator|.
name|processExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|counter2
operator|.
name|isStatisticsEnabled
argument_list|()
condition|)
block|{
name|counter2
operator|.
name|processExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|completedExchange (Exchange exchange, long time)
specifier|public
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
if|if
condition|(
name|counter1
operator|.
name|isStatisticsEnabled
argument_list|()
condition|)
block|{
name|counter1
operator|.
name|completedExchange
argument_list|(
name|exchange
argument_list|,
name|time
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|counter2
operator|.
name|isStatisticsEnabled
argument_list|()
condition|)
block|{
name|counter2
operator|.
name|completedExchange
argument_list|(
name|exchange
argument_list|,
name|time
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|failedExchange (Exchange exchange)
specifier|public
name|void
name|failedExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|counter1
operator|.
name|isStatisticsEnabled
argument_list|()
condition|)
block|{
name|counter1
operator|.
name|failedExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|counter2
operator|.
name|isStatisticsEnabled
argument_list|()
condition|)
block|{
name|counter2
operator|.
name|failedExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|isStatisticsEnabled ()
specifier|public
name|boolean
name|isStatisticsEnabled
parameter_list|()
block|{
comment|// this method is not used
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|setStatisticsEnabled (boolean statisticsEnabled)
specifier|public
name|void
name|setStatisticsEnabled
parameter_list|(
name|boolean
name|statisticsEnabled
parameter_list|)
block|{
comment|// this method is not used
block|}
block|}
end_class

end_unit

