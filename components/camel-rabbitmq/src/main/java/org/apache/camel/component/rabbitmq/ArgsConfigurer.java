begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.rabbitmq
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|rabbitmq
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
comment|/**  * @deprecated The endpoint uri properties  *<ul>  *<li>{@link RabbitMQEndpoint#setExchangeArgs(Map)}</li>  *<li>{@link RabbitMQEndpoint#setQueueArgs(Map)}</li>  *<li>{@link RabbitMQEndpoint#setBindingArgs(Map)}</li>  *</ul>  *  * are favoured over their configurer counterparts.  */
end_comment

begin_interface
annotation|@
name|Deprecated
DECL|interface|ArgsConfigurer
specifier|public
interface|interface
name|ArgsConfigurer
block|{
comment|/**      * Configure the args maps for RabbitMQ to use      * @param args the map need to be configured      */
DECL|method|configurArgs (Map<String, Object> args)
name|void
name|configurArgs
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|args
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

