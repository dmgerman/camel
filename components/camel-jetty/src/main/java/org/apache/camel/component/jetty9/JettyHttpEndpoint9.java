begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jetty9
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jetty9
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
name|java
operator|.
name|net
operator|.
name|URISyntaxException
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
name|AsyncEndpoint
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
name|jetty
operator|.
name|JettyContentExchange
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
name|jetty
operator|.
name|JettyHttpComponent
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
name|jetty
operator|.
name|JettyHttpEndpoint
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
name|http
operator|.
name|common
operator|.
name|HttpBinding
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
name|http
operator|.
name|common
operator|.
name|HttpConsumer
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
name|spi
operator|.
name|UriEndpoint
import|;
end_import

begin_comment
comment|/**  * The jetty component provides HTTP-based endpoints for consuming and producing HTTP requests.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"1.2.0"
argument_list|,
name|scheme
operator|=
literal|"jetty"
argument_list|,
name|extendsScheme
operator|=
literal|"http"
argument_list|,
name|title
operator|=
literal|"Jetty"
argument_list|,
name|syntax
operator|=
literal|"jetty:httpUri"
argument_list|,
name|label
operator|=
literal|"http"
argument_list|,
name|consumerOnly
operator|=
literal|true
argument_list|,
name|lenientProperties
operator|=
literal|true
argument_list|,
name|excludeProperties
operator|=
literal|"authMethod,authMethodPriority,authUsername,authPassword,authDomain,authHost"
operator|+
literal|"proxyAuthScheme,proxyAuthMethod,proxyAuthUsername,proxyAuthPassword,proxyAuthHost,proxyAuthPort,proxyAuthDomain"
argument_list|)
DECL|class|JettyHttpEndpoint9
specifier|public
class|class
name|JettyHttpEndpoint9
extends|extends
name|JettyHttpEndpoint
implements|implements
name|AsyncEndpoint
block|{
DECL|field|binding
specifier|private
name|HttpBinding
name|binding
decl_stmt|;
DECL|method|JettyHttpEndpoint9 (JettyHttpComponent component, String uri, URI httpURL)
specifier|public
name|JettyHttpEndpoint9
parameter_list|(
name|JettyHttpComponent
name|component
parameter_list|,
name|String
name|uri
parameter_list|,
name|URI
name|httpURL
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|super
argument_list|(
name|component
argument_list|,
name|uri
argument_list|,
name|httpURL
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getHttpBinding ()
specifier|public
name|HttpBinding
name|getHttpBinding
parameter_list|()
block|{
comment|// make sure we include jetty9 variant of the http binding
if|if
condition|(
name|this
operator|.
name|binding
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|binding
operator|=
operator|new
name|AttachmentHttpBinding
argument_list|()
expr_stmt|;
name|this
operator|.
name|binding
operator|.
name|setTransferException
argument_list|(
name|isTransferException
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|binding
operator|.
name|setMuteException
argument_list|(
name|isMuteException
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|getComponent
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|binding
operator|.
name|setAllowJavaSerializedObject
argument_list|(
name|getComponent
argument_list|()
operator|.
name|isAllowJavaSerializedObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|binding
operator|.
name|setHeaderFilterStrategy
argument_list|(
name|getHeaderFilterStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|binding
operator|.
name|setEagerCheckContentAvailable
argument_list|(
name|isEagerCheckContentAvailable
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|binding
operator|.
name|setMapHttpMessageBody
argument_list|(
name|isMapHttpMessageBody
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|binding
operator|.
name|setMapHttpMessageHeaders
argument_list|(
name|isMapHttpMessageHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|binding
operator|.
name|setMapHttpMessageFormUrlEncodedBody
argument_list|(
name|isMapHttpMessageFormUrlEncodedBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|this
operator|.
name|binding
return|;
block|}
annotation|@
name|Override
DECL|method|setHttpBinding (HttpBinding binding)
specifier|public
name|void
name|setHttpBinding
parameter_list|(
name|HttpBinding
name|binding
parameter_list|)
block|{
name|super
operator|.
name|setHttpBinding
argument_list|(
name|binding
argument_list|)
expr_stmt|;
name|this
operator|.
name|binding
operator|=
name|binding
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createContentExchange ()
specifier|public
name|JettyContentExchange
name|createContentExchange
parameter_list|()
block|{
return|return
operator|new
name|JettyContentExchange9
argument_list|()
return|;
block|}
block|}
end_class

end_unit

