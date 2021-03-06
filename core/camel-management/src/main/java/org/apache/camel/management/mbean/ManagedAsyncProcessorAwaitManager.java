begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Collection
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|CompositeData
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|CompositeDataSupport
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|CompositeType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|TabularData
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|TabularDataSupport
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
name|RuntimeCamelException
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
name|mbean
operator|.
name|CamelOpenMBeanTypes
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
name|ManagedAsyncProcessorAwaitManagerMBean
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
name|AsyncProcessorAwaitManager
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Managed AsyncProcessorAwaitManager"
argument_list|)
DECL|class|ManagedAsyncProcessorAwaitManager
specifier|public
class|class
name|ManagedAsyncProcessorAwaitManager
extends|extends
name|ManagedService
implements|implements
name|ManagedAsyncProcessorAwaitManagerMBean
block|{
DECL|field|manager
specifier|private
specifier|final
name|AsyncProcessorAwaitManager
name|manager
decl_stmt|;
DECL|method|ManagedAsyncProcessorAwaitManager (CamelContext context, AsyncProcessorAwaitManager manager)
specifier|public
name|ManagedAsyncProcessorAwaitManager
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|AsyncProcessorAwaitManager
name|manager
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|manager
argument_list|)
expr_stmt|;
name|this
operator|.
name|manager
operator|=
name|manager
expr_stmt|;
block|}
DECL|method|getAsyncProcessorAwaitManager ()
specifier|public
name|AsyncProcessorAwaitManager
name|getAsyncProcessorAwaitManager
parameter_list|()
block|{
return|return
name|manager
return|;
block|}
annotation|@
name|Override
DECL|method|isInterruptThreadsWhileStopping ()
specifier|public
name|boolean
name|isInterruptThreadsWhileStopping
parameter_list|()
block|{
return|return
name|manager
operator|.
name|isInterruptThreadsWhileStopping
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|setInterruptThreadsWhileStopping (boolean interruptThreadsWhileStopping)
specifier|public
name|void
name|setInterruptThreadsWhileStopping
parameter_list|(
name|boolean
name|interruptThreadsWhileStopping
parameter_list|)
block|{
name|manager
operator|.
name|setInterruptThreadsWhileStopping
argument_list|(
name|interruptThreadsWhileStopping
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getSize ()
specifier|public
name|int
name|getSize
parameter_list|()
block|{
return|return
name|manager
operator|.
name|size
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|browse ()
specifier|public
name|TabularData
name|browse
parameter_list|()
block|{
try|try
block|{
name|TabularData
name|answer
init|=
operator|new
name|TabularDataSupport
argument_list|(
name|CamelOpenMBeanTypes
operator|.
name|listAwaitThreadsTabularType
argument_list|()
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|AsyncProcessorAwaitManager
operator|.
name|AwaitThread
argument_list|>
name|threads
init|=
name|manager
operator|.
name|browse
argument_list|()
decl_stmt|;
for|for
control|(
name|AsyncProcessorAwaitManager
operator|.
name|AwaitThread
name|entry
range|:
name|threads
control|)
block|{
name|CompositeType
name|ct
init|=
name|CamelOpenMBeanTypes
operator|.
name|listAwaitThreadsCompositeType
argument_list|()
decl_stmt|;
name|String
name|id
init|=
literal|""
operator|+
name|entry
operator|.
name|getBlockedThread
argument_list|()
operator|.
name|getId
argument_list|()
decl_stmt|;
name|String
name|name
init|=
name|entry
operator|.
name|getBlockedThread
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|String
name|exchangeId
init|=
name|entry
operator|.
name|getExchange
argument_list|()
operator|.
name|getExchangeId
argument_list|()
decl_stmt|;
name|String
name|routeId
init|=
name|entry
operator|.
name|getRouteId
argument_list|()
decl_stmt|;
name|String
name|nodeId
init|=
name|entry
operator|.
name|getNodeId
argument_list|()
decl_stmt|;
name|String
name|duration
init|=
literal|""
operator|+
name|entry
operator|.
name|getWaitDuration
argument_list|()
decl_stmt|;
name|CompositeData
name|data
init|=
operator|new
name|CompositeDataSupport
argument_list|(
name|ct
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"id"
block|,
literal|"name"
block|,
literal|"exchangeId"
block|,
literal|"routeId"
block|,
literal|"nodeId"
block|,
literal|"duration"
block|}
argument_list|,
operator|new
name|Object
index|[]
block|{
name|id
block|,
name|name
block|,
name|exchangeId
block|,
name|routeId
block|,
name|nodeId
block|,
name|duration
block|}
argument_list|)
decl_stmt|;
name|answer
operator|.
name|put
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|RuntimeCamelException
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|interrupt (String exchangeId)
specifier|public
name|void
name|interrupt
parameter_list|(
name|String
name|exchangeId
parameter_list|)
block|{
name|manager
operator|.
name|interrupt
argument_list|(
name|exchangeId
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getThreadsBlocked ()
specifier|public
name|long
name|getThreadsBlocked
parameter_list|()
block|{
return|return
name|manager
operator|.
name|getStatistics
argument_list|()
operator|.
name|getThreadsBlocked
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getThreadsInterrupted ()
specifier|public
name|long
name|getThreadsInterrupted
parameter_list|()
block|{
return|return
name|manager
operator|.
name|getStatistics
argument_list|()
operator|.
name|getThreadsInterrupted
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getTotalDuration ()
specifier|public
name|long
name|getTotalDuration
parameter_list|()
block|{
return|return
name|manager
operator|.
name|getStatistics
argument_list|()
operator|.
name|getTotalDuration
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getMinDuration ()
specifier|public
name|long
name|getMinDuration
parameter_list|()
block|{
return|return
name|manager
operator|.
name|getStatistics
argument_list|()
operator|.
name|getMinDuration
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getMaxDuration ()
specifier|public
name|long
name|getMaxDuration
parameter_list|()
block|{
return|return
name|manager
operator|.
name|getStatistics
argument_list|()
operator|.
name|getMaxDuration
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getMeanDuration ()
specifier|public
name|long
name|getMeanDuration
parameter_list|()
block|{
return|return
name|manager
operator|.
name|getStatistics
argument_list|()
operator|.
name|getMeanDuration
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|resetStatistics ()
specifier|public
name|void
name|resetStatistics
parameter_list|()
block|{
name|manager
operator|.
name|getStatistics
argument_list|()
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isStatisticsEnabled ()
specifier|public
name|boolean
name|isStatisticsEnabled
parameter_list|()
block|{
return|return
name|manager
operator|.
name|getStatistics
argument_list|()
operator|.
name|isStatisticsEnabled
argument_list|()
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
name|manager
operator|.
name|getStatistics
argument_list|()
operator|.
name|setStatisticsEnabled
argument_list|(
name|statisticsEnabled
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

