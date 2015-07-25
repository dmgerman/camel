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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * Various statistics about endpoint utilization, such as from EIP patterns that uses dynamic endpoints.  */
end_comment

begin_interface
DECL|interface|EndpointUtilizationStatistics
specifier|public
interface|interface
name|EndpointUtilizationStatistics
block|{
comment|// TODO: document me
DECL|method|maxCapacity ()
name|int
name|maxCapacity
parameter_list|()
function_decl|;
DECL|method|size ()
name|int
name|size
parameter_list|()
function_decl|;
DECL|method|onHit (String key)
name|void
name|onHit
parameter_list|(
name|String
name|key
parameter_list|)
function_decl|;
DECL|method|remove (String key)
name|void
name|remove
parameter_list|(
name|String
name|key
parameter_list|)
function_decl|;
DECL|method|getStatistics ()
name|Map
argument_list|<
name|String
argument_list|,
name|Long
argument_list|>
name|getStatistics
parameter_list|()
function_decl|;
DECL|method|clear ()
name|void
name|clear
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

