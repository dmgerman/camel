begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
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
name|PollingConsumer
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
name|Service
import|;
end_import

begin_interface
DECL|interface|ConsumerCache
specifier|public
interface|interface
name|ConsumerCache
extends|extends
name|Service
block|{
DECL|method|acquirePollingConsumer (Endpoint endpoint)
name|PollingConsumer
name|acquirePollingConsumer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
function_decl|;
DECL|method|releasePollingConsumer (Endpoint endpoint, PollingConsumer pollingConsumer)
name|void
name|releasePollingConsumer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|PollingConsumer
name|pollingConsumer
parameter_list|)
function_decl|;
DECL|method|receive (Endpoint endpoint)
name|Exchange
name|receive
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
function_decl|;
DECL|method|receive (Endpoint endpoint, long timeout)
name|Exchange
name|receive
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|long
name|timeout
parameter_list|)
function_decl|;
DECL|method|receiveNoWait (Endpoint endpoint)
name|Exchange
name|receiveNoWait
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
function_decl|;
DECL|method|getSource ()
name|Object
name|getSource
parameter_list|()
function_decl|;
DECL|method|size ()
name|int
name|size
parameter_list|()
function_decl|;
DECL|method|getCapacity ()
name|int
name|getCapacity
parameter_list|()
function_decl|;
DECL|method|getHits ()
name|long
name|getHits
parameter_list|()
function_decl|;
DECL|method|getMisses ()
name|long
name|getMisses
parameter_list|()
function_decl|;
DECL|method|getEvicted ()
name|long
name|getEvicted
parameter_list|()
function_decl|;
DECL|method|resetCacheStatistics ()
name|void
name|resetCacheStatistics
parameter_list|()
function_decl|;
DECL|method|purge ()
name|void
name|purge
parameter_list|()
function_decl|;
DECL|method|cleanUp ()
name|void
name|cleanUp
parameter_list|()
function_decl|;
DECL|method|getEndpointUtilizationStatistics ()
name|EndpointUtilizationStatistics
name|getEndpointUtilizationStatistics
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

