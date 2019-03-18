begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.cxf.provider
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
name|provider
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|SOAPMessage
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
name|Provider
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
name|Service
operator|.
name|Mode
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
name|ServiceMode
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
name|WebServiceProvider
import|;
end_import

begin_comment
comment|// START SNIPPET: e1
end_comment

begin_class
annotation|@
name|WebServiceProvider
argument_list|()
annotation|@
name|ServiceMode
argument_list|(
name|Mode
operator|.
name|MESSAGE
argument_list|)
comment|// END SNIPPET: e1
comment|/**  * This class is used by Camel just for getting the endpoint configuration  * parameters. All the requests aimed at this class are intercepted and routed  * to the camel route specified. The route has to set the appropriate response  * message for the service to work.  */
comment|// START SNIPPET: e2
DECL|class|GreeterProvider
specifier|public
class|class
name|GreeterProvider
implements|implements
name|Provider
argument_list|<
name|SOAPMessage
argument_list|>
block|{
DECL|method|invoke (SOAPMessage message)
specifier|public
name|SOAPMessage
name|invoke
parameter_list|(
name|SOAPMessage
name|message
parameter_list|)
block|{
comment|// Requests should not come here as the Camel route will
comment|// intercept the call before this is invoked.
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Placeholder method"
argument_list|)
throw|;
block|}
block|}
end_class

begin_comment
comment|// END SNIPPET: e2
end_comment

end_unit

