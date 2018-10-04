begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.api.management.mbean
package|package
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
package|;
end_package

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
name|ManagedAttribute
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
name|ManagedOperation
import|;
end_import

begin_interface
DECL|interface|ManagedAsyncProcessorAwaitManagerMBean
specifier|public
interface|interface
name|ManagedAsyncProcessorAwaitManagerMBean
extends|extends
name|ManagedServiceMBean
block|{
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Whether to interrupt any blocking threads during stopping."
argument_list|)
DECL|method|isInterruptThreadsWhileStopping ()
name|boolean
name|isInterruptThreadsWhileStopping
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Whether to interrupt any blocking threads during stopping."
argument_list|)
DECL|method|setInterruptThreadsWhileStopping (boolean interruptThreadsWhileStopping)
name|void
name|setInterruptThreadsWhileStopping
parameter_list|(
name|boolean
name|interruptThreadsWhileStopping
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Number of threads that are blocked waiting for other threads to trigger the callback when they are done processing the exchange"
argument_list|)
DECL|method|getSize ()
name|int
name|getSize
parameter_list|()
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Lists all the exchanges which are currently inflight, having a blocked thread awaiting for other threads to trigger the callback when they are done"
argument_list|)
DECL|method|browse ()
name|TabularData
name|browse
parameter_list|()
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"To interrupt an exchange which may seem as stuck, to force the exchange to continue, allowing any blocking thread to be released."
argument_list|)
DECL|method|interrupt (String exchangeId)
name|void
name|interrupt
parameter_list|(
name|String
name|exchangeId
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Number of threads that has been blocked"
argument_list|)
DECL|method|getThreadsBlocked ()
name|long
name|getThreadsBlocked
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Number of threads that has been interrupted"
argument_list|)
DECL|method|getThreadsInterrupted ()
name|long
name|getThreadsInterrupted
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Total wait time in msec."
argument_list|)
DECL|method|getTotalDuration ()
name|long
name|getTotalDuration
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"The minimum wait time in msec."
argument_list|)
DECL|method|getMinDuration ()
name|long
name|getMinDuration
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"The maximum wait time in msec."
argument_list|)
DECL|method|getMaxDuration ()
name|long
name|getMaxDuration
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"The average wait time in msec."
argument_list|)
DECL|method|getMeanDuration ()
name|long
name|getMeanDuration
parameter_list|()
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Resets the statistics"
argument_list|)
DECL|method|resetStatistics ()
name|void
name|resetStatistics
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Utilization statistics enabled"
argument_list|)
DECL|method|isStatisticsEnabled ()
name|boolean
name|isStatisticsEnabled
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Utilization statistics enabled"
argument_list|)
DECL|method|setStatisticsEnabled (boolean statisticsEnabled)
name|void
name|setStatisticsEnabled
parameter_list|(
name|boolean
name|statisticsEnabled
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

