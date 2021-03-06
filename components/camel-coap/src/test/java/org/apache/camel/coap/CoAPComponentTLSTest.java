begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.coap
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|coap
package|;
end_package

begin_comment
comment|/**  * UDP + DTLS tests  */
end_comment

begin_class
DECL|class|CoAPComponentTLSTest
specifier|public
class|class
name|CoAPComponentTLSTest
extends|extends
name|CoAPComponentTLSTestBase
block|{
annotation|@
name|Override
DECL|method|getProtocol ()
specifier|protected
name|String
name|getProtocol
parameter_list|()
block|{
return|return
literal|"coaps"
return|;
block|}
annotation|@
name|Override
DECL|method|isPSKSupported ()
specifier|protected
name|boolean
name|isPSKSupported
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|isRPKSupported ()
specifier|protected
name|boolean
name|isRPKSupported
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

