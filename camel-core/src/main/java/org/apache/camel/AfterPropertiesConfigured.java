begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
package|;
end_package

begin_comment
comment|/**  * To perform optional initialization on an element after its properties has been configured.  *<p/>  * Currently only languages is supported using this callback.  */
end_comment

begin_interface
DECL|interface|AfterPropertiesConfigured
specifier|public
interface|interface
name|AfterPropertiesConfigured
block|{
comment|/**      * Callback invoked after the element have configured its properties.      *<p/>      * This allows to perform any post init work.      *      * @param camelContext  the Camel Context      */
DECL|method|afterPropertiesConfigured (CamelContext camelContext)
name|void
name|afterPropertiesConfigured
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

