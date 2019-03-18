begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sparkrest
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sparkrest
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
name|Message
import|;
end_import

begin_import
import|import
name|spark
operator|.
name|Request
import|;
end_import

begin_import
import|import
name|spark
operator|.
name|Response
import|;
end_import

begin_interface
DECL|interface|SparkBinding
specifier|public
interface|interface
name|SparkBinding
block|{
comment|/**      * Binds from Spark {@link Request} to Camel {@link org.apache.camel.Message}.      *      * @param request       the Spark request      * @param exchange      the exchange that should contain the returned message.      * @param configuration configuration      * @return the message to store on the given exchange      * @throws Exception is thrown if error during binding      */
DECL|method|toCamelMessage (Request request, Exchange exchange, SparkConfiguration configuration)
name|Message
name|toCamelMessage
parameter_list|(
name|Request
name|request
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|SparkConfiguration
name|configuration
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Binds from Spark {@link Request} to Camel headers as a {@link Map}.      *      * @param request       the Spark request      * @param headers       the Camel headers that should be populated      * @param exchange      the exchange that should contain the returned message.      * @param configuration the endpoint configuration      * @throws Exception is thrown if error during binding      */
DECL|method|populateCamelHeaders (Request request, Map<String, Object> headers, Exchange exchange, SparkConfiguration configuration)
name|void
name|populateCamelHeaders
parameter_list|(
name|Request
name|request
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|SparkConfiguration
name|configuration
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Binds from Camel {@link Message} to Spark {@link Response}.      *      * @param message       the Camel message      * @param response      the Spark response to bind to      * @param configuration the endpoint configuration      * @throws Exception is thrown if error during binding      */
DECL|method|toSparkResponse (Message message, Response response, SparkConfiguration configuration)
name|void
name|toSparkResponse
parameter_list|(
name|Message
name|message
parameter_list|,
name|Response
name|response
parameter_list|,
name|SparkConfiguration
name|configuration
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

