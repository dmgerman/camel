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
name|lang
operator|.
name|reflect
operator|.
name|Proxy
import|;
end_import

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
name|java
operator|.
name|net
operator|.
name|URL
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
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
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
name|HTTPRequest
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
name|HTTPResponse
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
name|Exchange
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
name|Producer
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
name|HttpBindingInvocationHandler
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
name|gae
operator|.
name|bind
operator|.
name|OutboundBindingSupport
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
name|camel
operator|.
name|util
operator|.
name|URISupport
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
name|util
operator|.
name|UnsafeUriCharactersEncoder
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
comment|/**  * Represents a<a href="http://camel.apache.org/ghttp.html">Google App Engine  * HTTP endpoint</a>.  */
end_comment

begin_class
DECL|class|GHttpEndpoint
specifier|public
class|class
name|GHttpEndpoint
extends|extends
name|ServletEndpoint
implements|implements
name|OutboundBindingSupport
argument_list|<
name|GHttpEndpoint
argument_list|,
name|HTTPRequest
argument_list|,
name|HTTPResponse
argument_list|>
block|{
DECL|field|GHTTP_SCHEME
specifier|public
specifier|static
specifier|final
name|String
name|GHTTP_SCHEME
init|=
literal|"ghttp"
decl_stmt|;
DECL|field|GHTTPS_SCHEME
specifier|public
specifier|static
specifier|final
name|String
name|GHTTPS_SCHEME
init|=
literal|"ghttps"
decl_stmt|;
DECL|field|HTTP_SCHEME
specifier|public
specifier|static
specifier|final
name|String
name|HTTP_SCHEME
init|=
literal|"http"
decl_stmt|;
DECL|field|HTTPS_SCHEME
specifier|public
specifier|static
specifier|final
name|String
name|HTTPS_SCHEME
init|=
literal|"https"
decl_stmt|;
DECL|field|urlFetchService
specifier|private
name|URLFetchService
name|urlFetchService
decl_stmt|;
DECL|field|outboundBinding
specifier|private
name|OutboundBinding
argument_list|<
name|GHttpEndpoint
argument_list|,
name|HTTPRequest
argument_list|,
name|HTTPResponse
argument_list|>
name|outboundBinding
decl_stmt|;
DECL|field|inboundBinding
specifier|private
name|InboundBinding
argument_list|<
name|GHttpEndpoint
argument_list|,
name|HttpServletRequest
argument_list|,
name|HttpServletResponse
argument_list|>
name|inboundBinding
decl_stmt|;
DECL|method|GHttpEndpoint (String endpointUri, ServletComponent component, URI httpUri, HttpClientParams params, HttpConnectionManager httpConnectionManager, HttpClientConfigurer clientConfigurer)
specifier|public
name|GHttpEndpoint
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
name|URISyntaxException
block|{
comment|// set the endpoint uri with httpUri as we need to create http producer here
name|super
argument_list|(
name|httpUri
operator|.
name|toString
argument_list|()
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
expr_stmt|;
name|urlFetchService
operator|=
name|URLFetchServiceFactory
operator|.
name|getURLFetchService
argument_list|()
expr_stmt|;
block|}
comment|/**      * Constructs a {@link URL} from an<code>uri</code> and an optional      *<code>query</code> string. The encoding strategy follow those of the      * Camel HTTP component.      *       * @param uri      *            must be encoded with      *            {@link UnsafeUriCharactersEncoder#encode(String)}.      * @param query      *            decoded query string. Replaces the query part of      *<code>uri</code> if not<code>null</code>.      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|getEndpointUrl (String uri, String query)
specifier|static
name|URL
name|getEndpointUrl
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|query
parameter_list|)
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
init|=
literal|null
decl_stmt|;
name|URI
name|uriObj
init|=
operator|new
name|URI
argument_list|(
name|uri
argument_list|)
decl_stmt|;
if|if
condition|(
name|query
operator|==
literal|null
condition|)
block|{
name|parameters
operator|=
name|URISupport
operator|.
name|parseParameters
argument_list|(
name|uriObj
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|parameters
operator|=
name|URISupport
operator|.
name|parseQuery
argument_list|(
name|query
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|uriObj
operator|.
name|getScheme
argument_list|()
operator|.
name|equals
argument_list|(
name|GHTTPS_SCHEME
argument_list|)
condition|)
block|{
name|uriObj
operator|=
operator|new
name|URI
argument_list|(
name|HTTPS_SCHEME
operator|+
literal|":"
operator|+
name|uriObj
operator|.
name|getRawSchemeSpecificPart
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// ghttp or anything else
name|uriObj
operator|=
operator|new
name|URI
argument_list|(
name|HTTP_SCHEME
operator|+
literal|":"
operator|+
name|uriObj
operator|.
name|getRawSchemeSpecificPart
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|URISupport
operator|.
name|createRemainingURI
argument_list|(
name|uriObj
argument_list|,
operator|(
name|Map
operator|)
name|parameters
argument_list|)
operator|.
name|toURL
argument_list|()
return|;
block|}
DECL|method|getEndpointUrl ()
specifier|public
name|URL
name|getEndpointUrl
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|getEndpointUrl
argument_list|(
name|getEndpointUri
argument_list|()
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|getUrlFetchService ()
specifier|public
name|URLFetchService
name|getUrlFetchService
parameter_list|()
block|{
return|return
name|urlFetchService
return|;
block|}
DECL|method|setUrlFetchService (URLFetchService urlFetchService)
specifier|public
name|void
name|setUrlFetchService
parameter_list|(
name|URLFetchService
name|urlFetchService
parameter_list|)
block|{
name|this
operator|.
name|urlFetchService
operator|=
name|urlFetchService
expr_stmt|;
block|}
DECL|method|getOutboundBinding ()
specifier|public
name|OutboundBinding
argument_list|<
name|GHttpEndpoint
argument_list|,
name|HTTPRequest
argument_list|,
name|HTTPResponse
argument_list|>
name|getOutboundBinding
parameter_list|()
block|{
return|return
name|outboundBinding
return|;
block|}
DECL|method|setOutboundBinding (OutboundBinding<GHttpEndpoint, HTTPRequest, HTTPResponse> outboundBinding)
specifier|public
name|void
name|setOutboundBinding
parameter_list|(
name|OutboundBinding
argument_list|<
name|GHttpEndpoint
argument_list|,
name|HTTPRequest
argument_list|,
name|HTTPResponse
argument_list|>
name|outboundBinding
parameter_list|)
block|{
name|this
operator|.
name|outboundBinding
operator|=
name|outboundBinding
expr_stmt|;
block|}
DECL|method|getInboundBinding ()
specifier|public
name|InboundBinding
argument_list|<
name|GHttpEndpoint
argument_list|,
name|HttpServletRequest
argument_list|,
name|HttpServletResponse
argument_list|>
name|getInboundBinding
parameter_list|()
block|{
return|return
name|inboundBinding
return|;
block|}
DECL|method|setInboundBinding ( InboundBinding<GHttpEndpoint, HttpServletRequest, HttpServletResponse> inboundBinding)
specifier|public
name|void
name|setInboundBinding
parameter_list|(
name|InboundBinding
argument_list|<
name|GHttpEndpoint
argument_list|,
name|HttpServletRequest
argument_list|,
name|HttpServletResponse
argument_list|>
name|inboundBinding
parameter_list|)
block|{
name|this
operator|.
name|inboundBinding
operator|=
name|inboundBinding
expr_stmt|;
block|}
comment|/**      * Proxies the {@link HttpBinding} returned by {@link super#getBinding()}      * with a dynamic proxy. The proxy's invocation handler further delegates to      * {@link InboundBinding#readRequest(org.apache.camel.Endpoint, Exchange, Object)}      * .      *       * @return proxied {@link HttpBinding}.      */
annotation|@
name|Override
DECL|method|getBinding ()
specifier|public
name|HttpBinding
name|getBinding
parameter_list|()
block|{
return|return
operator|(
name|HttpBinding
operator|)
name|Proxy
operator|.
name|newProxyInstance
argument_list|(
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|,
operator|new
name|Class
index|[]
block|{
name|HttpBinding
operator|.
name|class
block|}
argument_list|,
operator|new
name|HttpBindingInvocationHandler
argument_list|<
name|GHttpEndpoint
argument_list|,
name|HttpServletRequest
argument_list|,
name|HttpServletResponse
argument_list|>
argument_list|(
name|this
argument_list|,
name|super
operator|.
name|getBinding
argument_list|()
argument_list|,
name|getInboundBinding
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|GHttpProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|isLenientProperties ()
specifier|public
name|boolean
name|isLenientProperties
parameter_list|()
block|{
comment|// GHttpEndpoint knows about all it's options on the passed URI
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

