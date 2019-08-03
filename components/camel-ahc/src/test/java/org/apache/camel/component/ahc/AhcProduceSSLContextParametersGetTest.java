begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ahc
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ahc
package|;
end_package

begin_class
DECL|class|AhcProduceSSLContextParametersGetTest
specifier|public
class|class
name|AhcProduceSSLContextParametersGetTest
extends|extends
name|AhcProduceGetTest
block|{
annotation|@
name|Override
DECL|method|getTestServerEndpointUri ()
specifier|protected
name|String
name|getTestServerEndpointUri
parameter_list|()
block|{
return|return
name|super
operator|.
name|getTestServerEndpointUri
argument_list|()
operator|+
literal|"?sslContextParameters=#sslContextParameters"
return|;
block|}
annotation|@
name|Override
DECL|method|getAhcEndpointUri ()
specifier|protected
name|String
name|getAhcEndpointUri
parameter_list|()
block|{
return|return
name|super
operator|.
name|getAhcEndpointUri
argument_list|()
operator|+
literal|"?sslContextParameters=#sslContextParameters"
return|;
block|}
annotation|@
name|Override
DECL|method|isHttps ()
specifier|protected
name|boolean
name|isHttps
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

