begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Service
import|;
end_import

begin_comment
comment|/**  * A repository which tracks in flight {@link Exchange}s.  *  * @version   */
end_comment

begin_interface
DECL|interface|InflightRepository
specifier|public
interface|interface
name|InflightRepository
extends|extends
name|Service
block|{
comment|/**      * Adds the exchange to the inflight registry      *      * @param exchange  the exchange      */
DECL|method|add (Exchange exchange)
name|void
name|add
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
function_decl|;
comment|/**      * Removes the exchange from the inflight registry      *      * @param exchange  the exchange      */
DECL|method|remove (Exchange exchange)
name|void
name|remove
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
function_decl|;
comment|/**      * Current size of inflight exchanges.      *<p/>      * Will return 0 if there are no inflight exchanges.      *      * @return number of exchanges currently in flight.      */
DECL|method|size ()
name|int
name|size
parameter_list|()
function_decl|;
comment|/**      * Current size of inflight exchanges which are from the given endpoint.      *<p/>      * Will return 0 if there are no inflight exchanges.      *      * @param endpoint the endpoint where the {@link Exchange} are from.      * @return number of exchanges currently in flight.      */
DECL|method|size (Endpoint endpoint)
name|int
name|size
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

