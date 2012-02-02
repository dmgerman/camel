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
DECL|interface|ManagedPerformanceCounterMBean
specifier|public
interface|interface
name|ManagedPerformanceCounterMBean
block|{
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Reset counters"
argument_list|)
DECL|method|reset ()
name|void
name|reset
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Number of completed exchanges"
argument_list|)
DECL|method|getExchangesCompleted ()
name|long
name|getExchangesCompleted
parameter_list|()
throws|throws
name|Exception
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Number of failed exchanges"
argument_list|)
DECL|method|getExchangesFailed ()
name|long
name|getExchangesFailed
parameter_list|()
throws|throws
name|Exception
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Number of failures handled"
argument_list|)
DECL|method|getFailuresHandled ()
name|long
name|getFailuresHandled
parameter_list|()
throws|throws
name|Exception
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Number of redeliveries (Camel error handler)"
argument_list|)
DECL|method|getRedeliveries ()
name|long
name|getRedeliveries
parameter_list|()
throws|throws
name|Exception
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Number of transacted redeliveries (from external transacted source)"
argument_list|)
DECL|method|getTransactedRedeliveries ()
name|long
name|getTransactedRedeliveries
parameter_list|()
throws|throws
name|Exception
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Min Processing Time [milliseconds]"
argument_list|)
DECL|method|getMinProcessingTime ()
name|long
name|getMinProcessingTime
parameter_list|()
throws|throws
name|Exception
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Mean Processing Time [milliseconds]"
argument_list|)
DECL|method|getMeanProcessingTime ()
name|long
name|getMeanProcessingTime
parameter_list|()
throws|throws
name|Exception
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Max Processing Time [milliseconds]"
argument_list|)
DECL|method|getMaxProcessingTime ()
name|long
name|getMaxProcessingTime
parameter_list|()
throws|throws
name|Exception
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Total Processing Time [milliseconds]"
argument_list|)
DECL|method|getTotalProcessingTime ()
name|long
name|getTotalProcessingTime
parameter_list|()
throws|throws
name|Exception
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Last Processing Time [milliseconds]"
argument_list|)
DECL|method|getLastProcessingTime ()
name|long
name|getLastProcessingTime
parameter_list|()
throws|throws
name|Exception
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Last Exchange Completed Timestamp"
argument_list|)
DECL|method|getLastExchangeCompletedTimestamp ()
name|Date
name|getLastExchangeCompletedTimestamp
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Last Exchange Completed ExchangeId"
argument_list|)
DECL|method|getLastExchangeCompletedExchangeId ()
name|String
name|getLastExchangeCompletedExchangeId
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"First Exchange Completed Timestamp"
argument_list|)
DECL|method|getFirstExchangeCompletedTimestamp ()
name|Date
name|getFirstExchangeCompletedTimestamp
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"First Exchange Completed ExchangeId"
argument_list|)
DECL|method|getFirstExchangeCompletedExchangeId ()
name|String
name|getFirstExchangeCompletedExchangeId
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Last Exchange Failed Timestamp"
argument_list|)
DECL|method|getLastExchangeFailureTimestamp ()
name|Date
name|getLastExchangeFailureTimestamp
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Last Exchange Failed ExchangeId"
argument_list|)
DECL|method|getLastExchangeFailureExchangeId ()
name|String
name|getLastExchangeFailureExchangeId
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"First Exchange Failed Timestamp"
argument_list|)
DECL|method|getFirstExchangeFailureTimestamp ()
name|Date
name|getFirstExchangeFailureTimestamp
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"First Exchange Failed ExchangeId"
argument_list|)
DECL|method|getFirstExchangeFailureExchangeId ()
name|String
name|getFirstExchangeFailureExchangeId
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Statistics enabled"
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
literal|"Statistics enabled"
argument_list|)
DECL|method|setStatisticsEnabled (boolean statisticsEnabled)
name|void
name|setStatisticsEnabled
parameter_list|(
name|boolean
name|statisticsEnabled
parameter_list|)
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Dumps the statistics as XML"
argument_list|)
DECL|method|dumpStatsAsXml (boolean fullStats)
name|String
name|dumpStatsAsXml
parameter_list|(
name|boolean
name|fullStats
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

