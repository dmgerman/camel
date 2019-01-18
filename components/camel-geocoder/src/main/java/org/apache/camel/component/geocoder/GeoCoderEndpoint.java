begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.geocoder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|geocoder
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|InetSocketAddress
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|Proxy
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|maps
operator|.
name|GeoApiContext
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
name|Consumer
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
name|Processor
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
name|geocoder
operator|.
name|http
operator|.
name|AuthenticationMethod
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
name|UriParam
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
name|UriPath
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
name|support
operator|.
name|DefaultEndpoint
import|;
end_import

begin_comment
comment|/**  * The geocoder component is used for looking up geocodes (latitude and longitude) for a given address, or reverse lookup.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.12.0"
argument_list|,
name|scheme
operator|=
literal|"geocoder"
argument_list|,
name|title
operator|=
literal|"Geocoder"
argument_list|,
name|syntax
operator|=
literal|"geocoder:address:latlng"
argument_list|,
name|producerOnly
operator|=
literal|true
argument_list|,
name|label
operator|=
literal|"api,location"
argument_list|)
DECL|class|GeoCoderEndpoint
specifier|public
class|class
name|GeoCoderEndpoint
extends|extends
name|DefaultEndpoint
block|{
annotation|@
name|UriPath
DECL|field|address
specifier|private
name|String
name|address
decl_stmt|;
annotation|@
name|UriPath
DECL|field|latlng
specifier|private
name|String
name|latlng
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"en"
argument_list|)
DECL|field|language
specifier|private
name|String
name|language
init|=
literal|"en"
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|clientId
specifier|private
name|String
name|clientId
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|clientKey
specifier|private
name|String
name|clientKey
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|apiKey
specifier|private
name|String
name|apiKey
decl_stmt|;
annotation|@
name|UriParam
DECL|field|headersOnly
specifier|private
name|boolean
name|headersOnly
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"proxy"
argument_list|)
DECL|field|proxyHost
specifier|private
name|String
name|proxyHost
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"proxy"
argument_list|)
DECL|field|proxyPort
specifier|private
name|Integer
name|proxyPort
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"proxy"
argument_list|)
DECL|field|proxyAuthMethod
specifier|private
name|String
name|proxyAuthMethod
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"proxy"
argument_list|)
DECL|field|proxyAuthUsername
specifier|private
name|String
name|proxyAuthUsername
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"proxy"
argument_list|)
DECL|field|proxyAuthPassword
specifier|private
name|String
name|proxyAuthPassword
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"proxy"
argument_list|)
DECL|field|proxyAuthDomain
specifier|private
name|String
name|proxyAuthDomain
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"proxy"
argument_list|)
DECL|field|proxyAuthHost
specifier|private
name|String
name|proxyAuthHost
decl_stmt|;
DECL|method|GeoCoderEndpoint ()
specifier|public
name|GeoCoderEndpoint
parameter_list|()
block|{     }
DECL|method|GeoCoderEndpoint (String uri, GeoCoderComponent component)
specifier|public
name|GeoCoderEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|GeoCoderComponent
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
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
name|GeoCoderProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Cannot consume from this component"
argument_list|)
throw|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|getLanguage ()
specifier|public
name|String
name|getLanguage
parameter_list|()
block|{
return|return
name|language
return|;
block|}
comment|/**      * The language to use.      */
DECL|method|setLanguage (String language)
specifier|public
name|void
name|setLanguage
parameter_list|(
name|String
name|language
parameter_list|)
block|{
name|this
operator|.
name|language
operator|=
name|language
expr_stmt|;
block|}
DECL|method|getAddress ()
specifier|public
name|String
name|getAddress
parameter_list|()
block|{
return|return
name|address
return|;
block|}
comment|/**      * The geo address which should be prefixed with<tt>address:</tt>      */
DECL|method|setAddress (String address)
specifier|public
name|void
name|setAddress
parameter_list|(
name|String
name|address
parameter_list|)
block|{
name|this
operator|.
name|address
operator|=
name|address
expr_stmt|;
block|}
DECL|method|getLatlng ()
specifier|public
name|String
name|getLatlng
parameter_list|()
block|{
return|return
name|latlng
return|;
block|}
comment|/**      * The geo latitude and longitude which should be prefixed with<tt>latlng:</tt>      */
DECL|method|setLatlng (String latlng)
specifier|public
name|void
name|setLatlng
parameter_list|(
name|String
name|latlng
parameter_list|)
block|{
name|this
operator|.
name|latlng
operator|=
name|latlng
expr_stmt|;
block|}
DECL|method|isHeadersOnly ()
specifier|public
name|boolean
name|isHeadersOnly
parameter_list|()
block|{
return|return
name|headersOnly
return|;
block|}
comment|/**      * Whether to only enrich the Exchange with headers, and leave the body as-is.      */
DECL|method|setHeadersOnly (boolean headersOnly)
specifier|public
name|void
name|setHeadersOnly
parameter_list|(
name|boolean
name|headersOnly
parameter_list|)
block|{
name|this
operator|.
name|headersOnly
operator|=
name|headersOnly
expr_stmt|;
block|}
DECL|method|getClientId ()
specifier|public
name|String
name|getClientId
parameter_list|()
block|{
return|return
name|clientId
return|;
block|}
comment|/**      * To use google premium with this client id      */
DECL|method|setClientId (String clientId)
specifier|public
name|void
name|setClientId
parameter_list|(
name|String
name|clientId
parameter_list|)
block|{
name|this
operator|.
name|clientId
operator|=
name|clientId
expr_stmt|;
block|}
DECL|method|getClientKey ()
specifier|public
name|String
name|getClientKey
parameter_list|()
block|{
return|return
name|clientKey
return|;
block|}
comment|/**      * To use google premium with this client key      */
DECL|method|setClientKey (String clientKey)
specifier|public
name|void
name|setClientKey
parameter_list|(
name|String
name|clientKey
parameter_list|)
block|{
name|this
operator|.
name|clientKey
operator|=
name|clientKey
expr_stmt|;
block|}
DECL|method|getApiKey ()
specifier|private
name|String
name|getApiKey
parameter_list|()
block|{
return|return
name|apiKey
return|;
block|}
comment|/**      * To use google apiKey      */
DECL|method|setApiKey (String apiKey)
specifier|public
name|void
name|setApiKey
parameter_list|(
name|String
name|apiKey
parameter_list|)
block|{
name|this
operator|.
name|apiKey
operator|=
name|apiKey
expr_stmt|;
block|}
comment|/**      * The proxy host name      */
DECL|method|setProxyHost (String proxyHost)
specifier|public
name|void
name|setProxyHost
parameter_list|(
name|String
name|proxyHost
parameter_list|)
block|{
name|this
operator|.
name|proxyHost
operator|=
name|proxyHost
expr_stmt|;
block|}
DECL|method|getProxyPort ()
specifier|public
name|int
name|getProxyPort
parameter_list|()
block|{
return|return
name|proxyPort
return|;
block|}
comment|/**      * The proxy port number      */
DECL|method|setProxyPort (int proxyPort)
specifier|public
name|void
name|setProxyPort
parameter_list|(
name|int
name|proxyPort
parameter_list|)
block|{
name|this
operator|.
name|proxyPort
operator|=
name|proxyPort
expr_stmt|;
block|}
DECL|method|getProxyAuthMethod ()
specifier|public
name|String
name|getProxyAuthMethod
parameter_list|()
block|{
return|return
name|proxyAuthMethod
return|;
block|}
comment|/**      * Authentication method for proxy, either as Basic, Digest or NTLM.      */
DECL|method|setProxyAuthMethod (String proxyAuthMethod)
specifier|public
name|void
name|setProxyAuthMethod
parameter_list|(
name|String
name|proxyAuthMethod
parameter_list|)
block|{
name|this
operator|.
name|proxyAuthMethod
operator|=
name|proxyAuthMethod
expr_stmt|;
block|}
DECL|method|getProxyAuthUsername ()
specifier|public
name|String
name|getProxyAuthUsername
parameter_list|()
block|{
return|return
name|proxyAuthUsername
return|;
block|}
comment|/**      * Username for proxy authentication      */
DECL|method|setProxyAuthUsername (String proxyAuthUsername)
specifier|public
name|void
name|setProxyAuthUsername
parameter_list|(
name|String
name|proxyAuthUsername
parameter_list|)
block|{
name|this
operator|.
name|proxyAuthUsername
operator|=
name|proxyAuthUsername
expr_stmt|;
block|}
DECL|method|getProxyAuthPassword ()
specifier|public
name|String
name|getProxyAuthPassword
parameter_list|()
block|{
return|return
name|proxyAuthPassword
return|;
block|}
comment|/**      * Password for proxy authentication      */
DECL|method|setProxyAuthPassword (String proxyAuthPassword)
specifier|public
name|void
name|setProxyAuthPassword
parameter_list|(
name|String
name|proxyAuthPassword
parameter_list|)
block|{
name|this
operator|.
name|proxyAuthPassword
operator|=
name|proxyAuthPassword
expr_stmt|;
block|}
DECL|method|getProxyAuthDomain ()
specifier|public
name|String
name|getProxyAuthDomain
parameter_list|()
block|{
return|return
name|proxyAuthDomain
return|;
block|}
comment|/**      * Domain for proxy NTML authentication      */
DECL|method|setProxyAuthDomain (String proxyAuthDomain)
specifier|public
name|void
name|setProxyAuthDomain
parameter_list|(
name|String
name|proxyAuthDomain
parameter_list|)
block|{
name|this
operator|.
name|proxyAuthDomain
operator|=
name|proxyAuthDomain
expr_stmt|;
block|}
DECL|method|getProxyAuthHost ()
specifier|public
name|String
name|getProxyAuthHost
parameter_list|()
block|{
return|return
name|proxyAuthHost
return|;
block|}
comment|/**      * Optional host for proxy NTML authentication      */
DECL|method|setProxyAuthHost (String proxyAuthHost)
specifier|public
name|void
name|setProxyAuthHost
parameter_list|(
name|String
name|proxyAuthHost
parameter_list|)
block|{
name|this
operator|.
name|proxyAuthHost
operator|=
name|proxyAuthHost
expr_stmt|;
block|}
DECL|method|createGeoApiContext ()
name|GeoApiContext
name|createGeoApiContext
parameter_list|()
block|{
name|GeoApiContext
operator|.
name|Builder
name|builder
init|=
operator|new
name|GeoApiContext
operator|.
name|Builder
argument_list|()
decl_stmt|;
if|if
condition|(
name|clientId
operator|!=
literal|null
condition|)
block|{
name|builder
operator|=
name|builder
operator|.
name|enterpriseCredentials
argument_list|(
name|clientId
argument_list|,
name|clientKey
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|builder
operator|=
name|builder
operator|.
name|apiKey
argument_list|(
name|getApiKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isProxyDefined
argument_list|()
condition|)
block|{
name|builder
operator|=
name|builder
operator|.
name|proxy
argument_list|(
name|createProxy
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|isProxyAuthDefined
argument_list|()
condition|)
block|{
name|builder
operator|=
name|configureProxyAuth
argument_list|(
name|builder
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|configureProxyAuth (GeoApiContext.Builder builder)
specifier|private
name|GeoApiContext
operator|.
name|Builder
name|configureProxyAuth
parameter_list|(
name|GeoApiContext
operator|.
name|Builder
name|builder
parameter_list|)
block|{
name|AuthenticationMethod
name|auth
init|=
name|getCamelContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|AuthenticationMethod
operator|.
name|class
argument_list|,
name|proxyAuthMethod
argument_list|)
decl_stmt|;
if|if
condition|(
name|auth
operator|==
name|AuthenticationMethod
operator|.
name|Basic
operator|||
name|auth
operator|==
name|AuthenticationMethod
operator|.
name|Digest
condition|)
block|{
name|builder
operator|=
name|builder
operator|.
name|proxyAuthentication
argument_list|(
name|proxyAuthUsername
argument_list|,
name|proxyAuthPassword
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unknown proxyAuthMethod "
operator|+
name|proxyAuthMethod
argument_list|)
throw|;
block|}
return|return
name|builder
return|;
block|}
DECL|method|createProxy ()
specifier|private
name|Proxy
name|createProxy
parameter_list|()
block|{
return|return
operator|new
name|Proxy
argument_list|(
name|Proxy
operator|.
name|Type
operator|.
name|HTTP
argument_list|,
operator|new
name|InetSocketAddress
argument_list|(
name|proxyHost
argument_list|,
name|proxyPort
argument_list|)
argument_list|)
return|;
block|}
DECL|method|isProxyDefined ()
specifier|private
name|boolean
name|isProxyDefined
parameter_list|()
block|{
return|return
name|proxyHost
operator|!=
literal|null
operator|&&
name|proxyPort
operator|!=
literal|null
return|;
block|}
DECL|method|isProxyAuthDefined ()
specifier|private
name|boolean
name|isProxyAuthDefined
parameter_list|()
block|{
return|return
name|proxyAuthMethod
operator|!=
literal|null
return|;
block|}
block|}
end_class

end_unit

