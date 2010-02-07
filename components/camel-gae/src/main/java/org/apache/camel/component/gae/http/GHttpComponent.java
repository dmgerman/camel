begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.gae.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|gae
operator|.
name|http
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
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|appengine
operator|.
name|api
operator|.
name|urlfetch
operator|.
name|URLFetchService
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|appengine
operator|.
name|api
operator|.
name|urlfetch
operator|.
name|URLFetchServiceFactory
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
name|Endpoint
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
name|gae
operator|.
name|bind
operator|.
name|InboundBinding
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
name|gae
operator|.
name|bind
operator|.
name|OutboundBinding
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
name|http
operator|.
name|HttpClientConfigurer
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
name|servlet
operator|.
name|ServletComponent
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
name|servlet
operator|.
name|ServletEndpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|HttpConnectionManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|params
operator|.
name|HttpClientParams
import|;
end_import

begin_comment
comment|/**  * The<a href="http://camel.apache.org/ghttp.html">Google App Engine HTTP  * Component</a> supports HTTP-based inbound and outbound communication. Inbound  * HTTP communication is realized in terms of the<a  * href="http://camel.apache.org/servlet.html"> Servlet Component</a> component.  * Outbound HTTP communication uses the URL fetch service of the Google App  * Engine.  */
end_comment

begin_class
DECL|class|GHttpComponent
specifier|public
class|class
name|GHttpComponent
extends|extends
name|ServletComponent
block|{
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|boolean
name|throwException
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"throwExceptionOnFailure"
argument_list|,
name|Boolean
operator|.
name|class
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|boolean
name|bridgeEndpoint
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"bridgeEndpoint"
argument_list|,
name|Boolean
operator|.
name|class
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|OutboundBinding
name|outboundBinding
init|=
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"outboundBindingRef"
argument_list|,
name|OutboundBinding
operator|.
name|class
argument_list|,
operator|new
name|GHttpBinding
argument_list|()
argument_list|)
decl_stmt|;
name|InboundBinding
name|inboundBinding
init|=
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"inboundBindingRef"
argument_list|,
name|InboundBinding
operator|.
name|class
argument_list|,
operator|new
name|GHttpBinding
argument_list|()
argument_list|)
decl_stmt|;
name|URLFetchService
name|service
init|=
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"urlFetchServiceRef"
argument_list|,
name|URLFetchService
operator|.
name|class
argument_list|,
name|URLFetchServiceFactory
operator|.
name|getURLFetchService
argument_list|()
argument_list|)
decl_stmt|;
name|GHttpEndpoint
name|endpoint
init|=
operator|(
name|GHttpEndpoint
operator|)
name|super
operator|.
name|createEndpoint
argument_list|(
name|uri
argument_list|,
name|remaining
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setThrowExceptionOnFailure
argument_list|(
name|throwException
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setBridgeEndpoint
argument_list|(
name|bridgeEndpoint
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setOutboundBinding
argument_list|(
name|outboundBinding
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setInboundBinding
argument_list|(
name|inboundBinding
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setUrlFetchService
argument_list|(
name|service
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
annotation|@
name|Override
DECL|method|createServletEndpoint (String endpointUri, ServletComponent component, URI httpUri, HttpClientParams params, HttpConnectionManager httpConnectionManager, HttpClientConfigurer clientConfigurer)
specifier|protected
name|ServletEndpoint
name|createServletEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|ServletComponent
name|component
parameter_list|,
name|URI
name|httpUri
parameter_list|,
name|HttpClientParams
name|params
parameter_list|,
name|HttpConnectionManager
name|httpConnectionManager
parameter_list|,
name|HttpClientConfigurer
name|clientConfigurer
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|GHttpEndpoint
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|,
name|httpUri
argument_list|,
name|params
argument_list|,
name|httpConnectionManager
argument_list|,
name|clientConfigurer
argument_list|)
return|;
block|}
block|}
end_class

end_unit

