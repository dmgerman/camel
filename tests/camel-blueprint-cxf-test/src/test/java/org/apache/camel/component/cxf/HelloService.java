begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
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

begin_interface
DECL|interface|HelloService
specifier|public
interface|interface
name|HelloService
block|{
DECL|method|sayHello ()
name|String
name|sayHello
parameter_list|()
function_decl|;
DECL|method|ping ()
name|void
name|ping
parameter_list|()
function_decl|;
DECL|method|getInvocationCount ()
name|int
name|getInvocationCount
parameter_list|()
function_decl|;
DECL|method|echo (String text)
name|String
name|echo
parameter_list|(
name|String
name|text
parameter_list|)
throws|throws
name|Exception
function_decl|;
DECL|method|echoBoolean (Boolean bool)
name|Boolean
name|echoBoolean
parameter_list|(
name|Boolean
name|bool
parameter_list|)
function_decl|;
DECL|method|complexParameters (List<String> par1, List<String> par2)
name|String
name|complexParameters
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|par1
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|par2
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

