begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|Producer
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
name|ServicePoolAware
import|;
end_import

begin_comment
comment|/**  * {@link InterceptSendToEndpointProcessor} used for producers that are {@link ServicePoolAware}.  */
end_comment

begin_class
DECL|class|InterceptSendToEndpointServicePoolProcessor
specifier|public
class|class
name|InterceptSendToEndpointServicePoolProcessor
extends|extends
name|InterceptSendToEndpointProcessor
implements|implements
name|ServicePoolAware
block|{
DECL|method|InterceptSendToEndpointServicePoolProcessor (InterceptSendToEndpoint endpoint, Endpoint delegate, Producer producer, boolean skip)
specifier|public
name|InterceptSendToEndpointServicePoolProcessor
parameter_list|(
name|InterceptSendToEndpoint
name|endpoint
parameter_list|,
name|Endpoint
name|delegate
parameter_list|,
name|Producer
name|producer
parameter_list|,
name|boolean
name|skip
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|delegate
argument_list|,
name|producer
argument_list|,
name|skip
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

