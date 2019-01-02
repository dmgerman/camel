begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.cxf.ws
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|cxf
operator|.
name|ws
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebMethod
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebParam
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebService
import|;
end_import

begin_interface
annotation|@
name|WebService
argument_list|(
name|name
operator|=
literal|"HelloWorld"
argument_list|,
name|targetNamespace
operator|=
literal|"http://cxf.apache.org/wsse/handler/helloworld"
argument_list|)
DECL|interface|HelloWorld
specifier|public
interface|interface
name|HelloWorld
block|{
annotation|@
name|WebMethod
DECL|method|sayHello (@ebParamname = R) String to)
name|String
name|sayHello
parameter_list|(
annotation|@
name|WebParam
argument_list|(
name|name
operator|=
literal|"toWhom"
argument_list|)
name|String
name|to
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

