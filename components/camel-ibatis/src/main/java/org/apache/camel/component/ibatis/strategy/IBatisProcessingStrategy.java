begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ibatis.strategy
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ibatis
operator|.
name|strategy
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|component
operator|.
name|ibatis
operator|.
name|IBatisEndpoint
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
name|component
operator|.
name|ibatis
operator|.
name|IBatisPollingConsumer
import|;
end_import

begin_comment
comment|/**  * Processing strategy for dealing with IBatis records   *  */
end_comment

begin_interface
DECL|interface|IBatisProcessingStrategy
specifier|public
interface|interface
name|IBatisProcessingStrategy
block|{
comment|/** 	 * Called when record is being queried. 	 * @param consumer     The Ibatis Polling Consumer 	 * @param endpoint     The Ibatis Endpoint 	 * @return             Results of the query as a java.util.List 	 * @throws Exception 	 */
DECL|method|poll (IBatisPollingConsumer consumer, IBatisEndpoint endpoint)
name|List
name|poll
parameter_list|(
name|IBatisPollingConsumer
name|consumer
parameter_list|,
name|IBatisEndpoint
name|endpoint
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/** 	 * Called if there is a statement to be run after processing 	 * @param endpoint     The Ibatis Enpoint 	 * @param exchange     The exchange after it has been processed 	 * @param data         The original data delivered to the route 	 * @param consumeStatement The update statement to run 	 * @throws Exception 	 */
DECL|method|commit (IBatisEndpoint endpoint, Exchange exchange, Object data, String consumeStatement)
name|void
name|commit
parameter_list|(
name|IBatisEndpoint
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Object
name|data
parameter_list|,
name|String
name|consumeStatement
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

