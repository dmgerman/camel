begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.holder
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
operator|.
name|holder
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebService
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Holder
import|;
end_import

begin_interface
annotation|@
name|WebService
DECL|interface|MyOrderEndpoint
specifier|public
interface|interface
name|MyOrderEndpoint
block|{
DECL|method|myOrder (Holder<String> strPart, int iAmount, Holder<String> strCustomer)
name|String
name|myOrder
parameter_list|(
name|Holder
argument_list|<
name|String
argument_list|>
name|strPart
parameter_list|,
name|int
name|iAmount
parameter_list|,
name|Holder
argument_list|<
name|String
argument_list|>
name|strCustomer
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

