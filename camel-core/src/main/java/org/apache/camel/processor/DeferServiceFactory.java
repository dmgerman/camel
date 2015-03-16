begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
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
name|impl
operator|.
name|DeferProducer
import|;
end_import

begin_comment
comment|/**  * Factory to create {@link org.apache.camel.DeferStartService} services such as {@link Producer}s  * and {@link org.apache.camel.PollingConsumer}s  */
end_comment

begin_class
DECL|class|DeferServiceFactory
specifier|public
class|class
name|DeferServiceFactory
block|{
comment|/**      * Creates the {@link Producer} which is deferred started until {@link org.apache.camel.CamelContext} is being started.      *<p/>      * When the producer is started, it re-lookup the endpoint to capture any changes such as the endpoint has been intercepted.      * This allows the producer to react and send messages to the updated endpoint.      *      * @param endpoint  the endpoint      * @return the producer which will be deferred started until {@link org.apache.camel.CamelContext} has been started      * @throws Exception can be thrown if there is an error starting the producer      * @see org.apache.camel.impl.DeferProducer      */
DECL|method|createProducer (Endpoint endpoint)
specifier|public
specifier|static
name|Producer
name|createProducer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
throws|throws
name|Exception
block|{
name|Producer
name|producer
init|=
operator|new
name|DeferProducer
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|getCamelContext
argument_list|()
operator|.
name|deferStartService
argument_list|(
name|producer
argument_list|,
literal|true
argument_list|)
expr_stmt|;
return|return
name|producer
return|;
block|}
block|}
end_class

end_unit

