begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ws
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ws
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
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
name|component
operator|.
name|ahc
operator|.
name|AhcComponent
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
name|component
operator|.
name|ahc
operator|.
name|AhcEndpoint
import|;
end_import

begin_comment
comment|/**  *  Defines the<a href="http://camel.apache.org/ws.html">WebSocket Client Component</a>  */
end_comment

begin_class
DECL|class|WsComponent
specifier|public
class|class
name|WsComponent
extends|extends
name|AhcComponent
block|{
comment|/* (non-Javadoc)      * @see org.apache.camel.component.ahc.AhcComponent#createAddressUri(java.lang.String, java.lang.String)      */
annotation|@
name|Override
DECL|method|createAddressUri (String uri, String remaining)
specifier|protected
name|String
name|createAddressUri
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|)
block|{
return|return
name|uri
return|;
block|}
comment|/* (non-Javadoc)      * @see org.apache.camel.component.ahc.AhcComponent#createAhcEndpoint(java.lang.String, org.apache.camel.component.ahc.AhcComponent, java.net.URI)      */
annotation|@
name|Override
DECL|method|createAhcEndpoint (String endpointUri, AhcComponent component, URI httpUri)
specifier|protected
name|AhcEndpoint
name|createAhcEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|AhcComponent
name|component
parameter_list|,
name|URI
name|httpUri
parameter_list|)
block|{
return|return
operator|new
name|WsEndpoint
argument_list|(
name|endpointUri
argument_list|,
operator|(
name|WsComponent
operator|)
name|component
argument_list|)
return|;
block|}
block|}
end_class

end_unit

