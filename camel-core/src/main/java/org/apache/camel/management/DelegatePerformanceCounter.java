begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  * Delegates to another {@link org.apache.camel.api.management.PerformanceCounter}.  *<p/>  * This is used to allow Camel to pre initialize these delegate performance counters  * when Camel creates the actual route from the model. Then later as the various  * processors, routes etc. is created and registered in the {@link org.apache.camel.spi.LifecycleStrategy}  * then we link this to the real {@link org.apache.camel.management.mbean.ManagedPerformanceCounter} mbean  * so the mbean can gather statistics.  *<p/>  * This delegation is needed as how Camel is designed to register services in the  * {@link org.apache.camel.spi.LifecycleStrategy} in various stages.  *  * @version   */
end_comment

begin_class
DECL|class|DelegatePerformanceCounter
specifier|public
class|class
name|DelegatePerformanceCounter
implements|implements
name|PerformanceCounter
block|{
DECL|field|counter
specifier|private
name|PerformanceCounter
name|counter
decl_stmt|;
DECL|field|statisticsEnabled
specifier|private
name|boolean
name|statisticsEnabled
decl_stmt|;
DECL|method|DelegatePerformanceCounter ()
specifier|public
name|DelegatePerformanceCounter
parameter_list|()
block|{     }
DECL|method|setCounter (PerformanceCounter counter)
specifier|public
name|void
name|setCounter
parameter_list|(
name|PerformanceCounter
name|counter
parameter_list|)
block|{
name|this
operator|.
name|counter
operator|=
name|counter
expr_stmt|;
comment|// init statistics based on the real counter based on how we got initialized
name|this
operator|.
name|counter
operator|.
name|setStatisticsEnabled
argument_list|(
name|statisticsEnabled
argument_list|)
expr_stmt|;
block|}
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
name|counter
operator|.
name|completedExchange
argument_list|(
name|exchange
argument_list|,
name|time
argument_list|)
expr_stmt|;
block|}
DECL|method|failedExchange (Exchange exchange)
specifier|public
name|void
name|failedExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|counter
operator|.
name|failedExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|isStatisticsEnabled ()
specifier|public
name|boolean
name|isStatisticsEnabled
parameter_list|()
block|{
comment|// statistics is only considered enabled if we have a counter to delegate to
comment|// otherwise we do not want to gather statistics (we are just a delegate with none to delegate to)
return|return
name|counter
operator|!=
literal|null
operator|&&
name|counter
operator|.
name|isStatisticsEnabled
argument_list|()
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
if|if
condition|(
name|counter
operator|!=
literal|null
condition|)
block|{
name|counter
operator|.
name|setStatisticsEnabled
argument_list|(
name|statisticsEnabled
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|statisticsEnabled
operator|=
name|statisticsEnabled
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

