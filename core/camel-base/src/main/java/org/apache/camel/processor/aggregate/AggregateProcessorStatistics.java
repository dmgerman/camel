begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.aggregate
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|aggregate
package|;
end_package

begin_comment
comment|/**  * Various statistics of the aggregator  */
end_comment

begin_interface
DECL|interface|AggregateProcessorStatistics
specifier|public
interface|interface
name|AggregateProcessorStatistics
block|{
comment|/**      * Total number of exchanges arrived into the aggregator      */
DECL|method|getTotalIn ()
name|long
name|getTotalIn
parameter_list|()
function_decl|;
comment|/**      * Total number of exchanges completed and outgoing from the aggregator      */
DECL|method|getTotalCompleted ()
name|long
name|getTotalCompleted
parameter_list|()
function_decl|;
comment|/**      * Total number of exchanged completed by completion size trigger      */
DECL|method|getCompletedBySize ()
name|long
name|getCompletedBySize
parameter_list|()
function_decl|;
comment|/**      * Total number of exchanged completed by completion strategy trigger      */
DECL|method|getCompletedByStrategy ()
name|long
name|getCompletedByStrategy
parameter_list|()
function_decl|;
comment|/**      * Total number of exchanged completed by completion interval trigger      */
DECL|method|getCompletedByInterval ()
name|long
name|getCompletedByInterval
parameter_list|()
function_decl|;
comment|/**      * Total number of exchanged completed by completion timeout trigger      */
DECL|method|getCompletedByTimeout ()
name|long
name|getCompletedByTimeout
parameter_list|()
function_decl|;
comment|/**      * Total number of exchanged completed by completion predicate trigger      */
DECL|method|getCompletedByPredicate ()
name|long
name|getCompletedByPredicate
parameter_list|()
function_decl|;
comment|/**      * Total number of exchanged completed by completion batch consumer trigger      */
DECL|method|getCompletedByBatchConsumer ()
name|long
name|getCompletedByBatchConsumer
parameter_list|()
function_decl|;
comment|/**      * Total number of exchanged completed by completion force trigger      */
DECL|method|getCompletedByForce ()
name|long
name|getCompletedByForce
parameter_list|()
function_decl|;
comment|/**      * Total number of exchanged discarded      */
DECL|method|getDiscarded ()
name|long
name|getDiscarded
parameter_list|()
function_decl|;
comment|/**      * Reset the counters      */
DECL|method|reset ()
name|void
name|reset
parameter_list|()
function_decl|;
comment|/**      * Whether statistics is enabled.      */
DECL|method|isStatisticsEnabled ()
name|boolean
name|isStatisticsEnabled
parameter_list|()
function_decl|;
comment|/**      * Sets whether statistics is enabled.      *      * @param statisticsEnabled<tt>true</tt> to enable      */
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

