begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.stomp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|stomp
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|SSLContextParametersAware
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
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_class
DECL|class|StompGlobalSslConsumerTest
specifier|public
class|class
name|StompGlobalSslConsumerTest
extends|extends
name|StompConsumerTest
block|{
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|setSSLContextParameters
argument_list|(
name|getClientSSLContextParameters
argument_list|()
argument_list|)
expr_stmt|;
operator|(
operator|(
name|SSLContextParametersAware
operator|)
name|context
operator|.
name|getComponent
argument_list|(
literal|"stomp"
argument_list|)
operator|)
operator|.
name|setUseGlobalSslContextParameters
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
annotation|@
name|Override
DECL|method|isUseSsl ()
specifier|protected
name|boolean
name|isUseSsl
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|fromF
argument_list|(
literal|"stomp:queue:test?brokerURL=ssl://localhost:%d"
argument_list|,
name|getPort
argument_list|()
argument_list|)
operator|.
name|transform
argument_list|(
name|body
argument_list|()
operator|.
name|convertToString
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

