begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *<p>  * http://www.apache.org/licenses/LICENSE-2.0  *<p>  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|CamelContext
import|;
end_import

begin_comment
comment|/**  * Property configurer for Camel {@link org.apache.camel.Endpoint}  * which allows fast configurations without using Java reflection.  */
end_comment

begin_interface
DECL|interface|EndpointPropertyConfigurer
specifier|public
interface|interface
name|EndpointPropertyConfigurer
extends|extends
name|PropertyConfigurer
argument_list|<
name|Object
argument_list|>
block|{
comment|/**      * Configures the endpoint.      *      * @param endpoint      the endpoint      * @param camelContext  the camel context      */
DECL|method|configure (Object endpoint, CamelContext camelContext)
name|void
name|configure
parameter_list|(
name|Object
name|endpoint
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

