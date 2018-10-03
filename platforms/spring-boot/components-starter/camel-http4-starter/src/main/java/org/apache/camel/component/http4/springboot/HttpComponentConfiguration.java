begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.http4.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|http4
operator|.
name|springboot
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Generated
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
name|spring
operator|.
name|boot
operator|.
name|ComponentConfigurationPropertiesCommon
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_comment
comment|/**  * For calling out to external HTTP servers using Apache HTTP Client 4.x.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo"
argument_list|)
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.http4"
argument_list|)
DECL|class|HttpComponentConfiguration
specifier|public
class|class
name|HttpComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * Whether to enable auto configuration of the http4 component. This is      * enabled by default.      */
DECL|field|enabled
specifier|private
name|Boolean
name|enabled
decl_stmt|;
comment|/**      * To use the custom HttpClientConfigurer to perform configuration of the      * HttpClient that will be used. The option is a      * org.apache.camel.component.http4.HttpClientConfigurer type.      */
DECL|field|httpClientConfigurer
specifier|private
name|String
name|httpClientConfigurer
decl_stmt|;
comment|/**      * To use a custom and shared HttpClientConnectionManager to manage      * connections. If this has been configured then this is always used for all      * endpoints created by this component. The option is a      * org.apache.http.conn.HttpClientConnectionManager type.      */
DECL|field|clientConnectionManager
specifier|private
name|String
name|clientConnectionManager
decl_stmt|;
comment|/**      * To use a custom org.apache.http.protocol.HttpContext when executing      * requests. The option is a org.apache.http.protocol.HttpContext type.      */
DECL|field|httpContext
specifier|private
name|String
name|httpContext
decl_stmt|;
comment|/**      * To configure security using SSLContextParameters. Important: Only one      * instance of org.apache.camel.support.jsse.SSLContextParameters is      * supported per HttpComponent. If you need to use 2 or more different      * instances, you need to define a new HttpComponent per instance you need.      * The option is a org.apache.camel.support.jsse.SSLContextParameters type.      */
DECL|field|sslContextParameters
specifier|private
name|String
name|sslContextParameters
decl_stmt|;
comment|/**      * Enable usage of global SSL context parameters.      */
DECL|field|useGlobalSslContextParameters
specifier|private
name|Boolean
name|useGlobalSslContextParameters
init|=
literal|false
decl_stmt|;
comment|/**      * To use a custom X509HostnameVerifier such as DefaultHostnameVerifier or      * org.apache.http.conn.ssl.NoopHostnameVerifier. The option is a      * javax.net.ssl.HostnameVerifier type.      */
DECL|field|x509HostnameVerifier
specifier|private
name|String
name|x509HostnameVerifier
decl_stmt|;
comment|/**      * The maximum number of connections.      */
DECL|field|maxTotalConnections
specifier|private
name|Integer
name|maxTotalConnections
init|=
literal|200
decl_stmt|;
comment|/**      * The maximum number of connections per route.      */
DECL|field|connectionsPerRoute
specifier|private
name|Integer
name|connectionsPerRoute
init|=
literal|20
decl_stmt|;
comment|/**      * The time for connection to live, the time unit is millisecond, the      * default value is always keep alive.      */
DECL|field|connectionTimeToLive
specifier|private
name|Long
name|connectionTimeToLive
decl_stmt|;
comment|/**      * To use a custom org.apache.http.client.CookieStore. By default the      * org.apache.http.impl.client.BasicCookieStore is used which is an      * in-memory only cookie store. Notice if bridgeEndpoint=true then the      * cookie store is forced to be a noop cookie store as cookie shouldn't be      * stored as we are just bridging (eg acting as a proxy). The option is a      * org.apache.http.client.CookieStore type.      */
DECL|field|cookieStore
specifier|private
name|String
name|cookieStore
decl_stmt|;
comment|/**      * The timeout in milliseconds used when requesting a connection from the      * connection manager. A timeout value of zero is interpreted as an infinite      * timeout. A timeout value of zero is interpreted as an infinite timeout. A      * negative value is interpreted as undefined (system default). Default:      * code -1      */
DECL|field|connectionRequestTimeout
specifier|private
name|Integer
name|connectionRequestTimeout
init|=
operator|-
literal|1
decl_stmt|;
comment|/**      * Determines the timeout in milliseconds until a connection is established.      * A timeout value of zero is interpreted as an infinite timeout. A timeout      * value of zero is interpreted as an infinite timeout. A negative value is      * interpreted as undefined (system default). Default: code -1      */
DECL|field|connectTimeout
specifier|private
name|Integer
name|connectTimeout
init|=
operator|-
literal|1
decl_stmt|;
comment|/**      * Defines the socket timeout (SO_TIMEOUT) in milliseconds, which is the      * timeout for waiting for data or, put differently, a maximum period      * inactivity between two consecutive data packets). A timeout value of zero      * is interpreted as an infinite timeout. A negative value is interpreted as      * undefined (system default). Default: code -1      */
DECL|field|socketTimeout
specifier|private
name|Integer
name|socketTimeout
init|=
operator|-
literal|1
decl_stmt|;
comment|/**      * To use a custom HttpBinding to control the mapping between Camel message      * and HttpClient. The option is a org.apache.camel.http.common.HttpBinding      * type.      */
DECL|field|httpBinding
specifier|private
name|String
name|httpBinding
decl_stmt|;
comment|/**      * To use the shared HttpConfiguration as base configuration. The option is      * a org.apache.camel.http.common.HttpConfiguration type.      */
DECL|field|httpConfiguration
specifier|private
name|String
name|httpConfiguration
decl_stmt|;
comment|/**      * Whether to allow java serialization when a request uses      * context-type=application/x-java-serialized-object. This is by default      * turned off. If you enable this then be aware that Java will deserialize      * the incoming data from the request to Java and that can be a potential      * security risk.      */
DECL|field|allowJavaSerializedObject
specifier|private
name|Boolean
name|allowJavaSerializedObject
init|=
literal|false
decl_stmt|;
comment|/**      * To use a custom org.apache.camel.spi.HeaderFilterStrategy to filter      * header to and from Camel message. The option is a      * org.apache.camel.spi.HeaderFilterStrategy type.      */
DECL|field|headerFilterStrategy
specifier|private
name|String
name|headerFilterStrategy
decl_stmt|;
comment|/**      * Whether the component should resolve property placeholders on itself when      * starting. Only properties which are of String type can use property      * placeholders.      */
DECL|field|resolvePropertyPlaceholders
specifier|private
name|Boolean
name|resolvePropertyPlaceholders
init|=
literal|true
decl_stmt|;
DECL|method|getHttpClientConfigurer ()
specifier|public
name|String
name|getHttpClientConfigurer
parameter_list|()
block|{
return|return
name|httpClientConfigurer
return|;
block|}
DECL|method|setHttpClientConfigurer (String httpClientConfigurer)
specifier|public
name|void
name|setHttpClientConfigurer
parameter_list|(
name|String
name|httpClientConfigurer
parameter_list|)
block|{
name|this
operator|.
name|httpClientConfigurer
operator|=
name|httpClientConfigurer
expr_stmt|;
block|}
DECL|method|getClientConnectionManager ()
specifier|public
name|String
name|getClientConnectionManager
parameter_list|()
block|{
return|return
name|clientConnectionManager
return|;
block|}
DECL|method|setClientConnectionManager (String clientConnectionManager)
specifier|public
name|void
name|setClientConnectionManager
parameter_list|(
name|String
name|clientConnectionManager
parameter_list|)
block|{
name|this
operator|.
name|clientConnectionManager
operator|=
name|clientConnectionManager
expr_stmt|;
block|}
DECL|method|getHttpContext ()
specifier|public
name|String
name|getHttpContext
parameter_list|()
block|{
return|return
name|httpContext
return|;
block|}
DECL|method|setHttpContext (String httpContext)
specifier|public
name|void
name|setHttpContext
parameter_list|(
name|String
name|httpContext
parameter_list|)
block|{
name|this
operator|.
name|httpContext
operator|=
name|httpContext
expr_stmt|;
block|}
DECL|method|getSslContextParameters ()
specifier|public
name|String
name|getSslContextParameters
parameter_list|()
block|{
return|return
name|sslContextParameters
return|;
block|}
DECL|method|setSslContextParameters (String sslContextParameters)
specifier|public
name|void
name|setSslContextParameters
parameter_list|(
name|String
name|sslContextParameters
parameter_list|)
block|{
name|this
operator|.
name|sslContextParameters
operator|=
name|sslContextParameters
expr_stmt|;
block|}
DECL|method|getUseGlobalSslContextParameters ()
specifier|public
name|Boolean
name|getUseGlobalSslContextParameters
parameter_list|()
block|{
return|return
name|useGlobalSslContextParameters
return|;
block|}
DECL|method|setUseGlobalSslContextParameters ( Boolean useGlobalSslContextParameters)
specifier|public
name|void
name|setUseGlobalSslContextParameters
parameter_list|(
name|Boolean
name|useGlobalSslContextParameters
parameter_list|)
block|{
name|this
operator|.
name|useGlobalSslContextParameters
operator|=
name|useGlobalSslContextParameters
expr_stmt|;
block|}
DECL|method|getX509HostnameVerifier ()
specifier|public
name|String
name|getX509HostnameVerifier
parameter_list|()
block|{
return|return
name|x509HostnameVerifier
return|;
block|}
DECL|method|setX509HostnameVerifier (String x509HostnameVerifier)
specifier|public
name|void
name|setX509HostnameVerifier
parameter_list|(
name|String
name|x509HostnameVerifier
parameter_list|)
block|{
name|this
operator|.
name|x509HostnameVerifier
operator|=
name|x509HostnameVerifier
expr_stmt|;
block|}
DECL|method|getMaxTotalConnections ()
specifier|public
name|Integer
name|getMaxTotalConnections
parameter_list|()
block|{
return|return
name|maxTotalConnections
return|;
block|}
DECL|method|setMaxTotalConnections (Integer maxTotalConnections)
specifier|public
name|void
name|setMaxTotalConnections
parameter_list|(
name|Integer
name|maxTotalConnections
parameter_list|)
block|{
name|this
operator|.
name|maxTotalConnections
operator|=
name|maxTotalConnections
expr_stmt|;
block|}
DECL|method|getConnectionsPerRoute ()
specifier|public
name|Integer
name|getConnectionsPerRoute
parameter_list|()
block|{
return|return
name|connectionsPerRoute
return|;
block|}
DECL|method|setConnectionsPerRoute (Integer connectionsPerRoute)
specifier|public
name|void
name|setConnectionsPerRoute
parameter_list|(
name|Integer
name|connectionsPerRoute
parameter_list|)
block|{
name|this
operator|.
name|connectionsPerRoute
operator|=
name|connectionsPerRoute
expr_stmt|;
block|}
DECL|method|getConnectionTimeToLive ()
specifier|public
name|Long
name|getConnectionTimeToLive
parameter_list|()
block|{
return|return
name|connectionTimeToLive
return|;
block|}
DECL|method|setConnectionTimeToLive (Long connectionTimeToLive)
specifier|public
name|void
name|setConnectionTimeToLive
parameter_list|(
name|Long
name|connectionTimeToLive
parameter_list|)
block|{
name|this
operator|.
name|connectionTimeToLive
operator|=
name|connectionTimeToLive
expr_stmt|;
block|}
DECL|method|getCookieStore ()
specifier|public
name|String
name|getCookieStore
parameter_list|()
block|{
return|return
name|cookieStore
return|;
block|}
DECL|method|setCookieStore (String cookieStore)
specifier|public
name|void
name|setCookieStore
parameter_list|(
name|String
name|cookieStore
parameter_list|)
block|{
name|this
operator|.
name|cookieStore
operator|=
name|cookieStore
expr_stmt|;
block|}
DECL|method|getConnectionRequestTimeout ()
specifier|public
name|Integer
name|getConnectionRequestTimeout
parameter_list|()
block|{
return|return
name|connectionRequestTimeout
return|;
block|}
DECL|method|setConnectionRequestTimeout (Integer connectionRequestTimeout)
specifier|public
name|void
name|setConnectionRequestTimeout
parameter_list|(
name|Integer
name|connectionRequestTimeout
parameter_list|)
block|{
name|this
operator|.
name|connectionRequestTimeout
operator|=
name|connectionRequestTimeout
expr_stmt|;
block|}
DECL|method|getConnectTimeout ()
specifier|public
name|Integer
name|getConnectTimeout
parameter_list|()
block|{
return|return
name|connectTimeout
return|;
block|}
DECL|method|setConnectTimeout (Integer connectTimeout)
specifier|public
name|void
name|setConnectTimeout
parameter_list|(
name|Integer
name|connectTimeout
parameter_list|)
block|{
name|this
operator|.
name|connectTimeout
operator|=
name|connectTimeout
expr_stmt|;
block|}
DECL|method|getSocketTimeout ()
specifier|public
name|Integer
name|getSocketTimeout
parameter_list|()
block|{
return|return
name|socketTimeout
return|;
block|}
DECL|method|setSocketTimeout (Integer socketTimeout)
specifier|public
name|void
name|setSocketTimeout
parameter_list|(
name|Integer
name|socketTimeout
parameter_list|)
block|{
name|this
operator|.
name|socketTimeout
operator|=
name|socketTimeout
expr_stmt|;
block|}
DECL|method|getHttpBinding ()
specifier|public
name|String
name|getHttpBinding
parameter_list|()
block|{
return|return
name|httpBinding
return|;
block|}
DECL|method|setHttpBinding (String httpBinding)
specifier|public
name|void
name|setHttpBinding
parameter_list|(
name|String
name|httpBinding
parameter_list|)
block|{
name|this
operator|.
name|httpBinding
operator|=
name|httpBinding
expr_stmt|;
block|}
DECL|method|getHttpConfiguration ()
specifier|public
name|String
name|getHttpConfiguration
parameter_list|()
block|{
return|return
name|httpConfiguration
return|;
block|}
DECL|method|setHttpConfiguration (String httpConfiguration)
specifier|public
name|void
name|setHttpConfiguration
parameter_list|(
name|String
name|httpConfiguration
parameter_list|)
block|{
name|this
operator|.
name|httpConfiguration
operator|=
name|httpConfiguration
expr_stmt|;
block|}
DECL|method|getAllowJavaSerializedObject ()
specifier|public
name|Boolean
name|getAllowJavaSerializedObject
parameter_list|()
block|{
return|return
name|allowJavaSerializedObject
return|;
block|}
DECL|method|setAllowJavaSerializedObject (Boolean allowJavaSerializedObject)
specifier|public
name|void
name|setAllowJavaSerializedObject
parameter_list|(
name|Boolean
name|allowJavaSerializedObject
parameter_list|)
block|{
name|this
operator|.
name|allowJavaSerializedObject
operator|=
name|allowJavaSerializedObject
expr_stmt|;
block|}
DECL|method|getHeaderFilterStrategy ()
specifier|public
name|String
name|getHeaderFilterStrategy
parameter_list|()
block|{
return|return
name|headerFilterStrategy
return|;
block|}
DECL|method|setHeaderFilterStrategy (String headerFilterStrategy)
specifier|public
name|void
name|setHeaderFilterStrategy
parameter_list|(
name|String
name|headerFilterStrategy
parameter_list|)
block|{
name|this
operator|.
name|headerFilterStrategy
operator|=
name|headerFilterStrategy
expr_stmt|;
block|}
DECL|method|getResolvePropertyPlaceholders ()
specifier|public
name|Boolean
name|getResolvePropertyPlaceholders
parameter_list|()
block|{
return|return
name|resolvePropertyPlaceholders
return|;
block|}
DECL|method|setResolvePropertyPlaceholders ( Boolean resolvePropertyPlaceholders)
specifier|public
name|void
name|setResolvePropertyPlaceholders
parameter_list|(
name|Boolean
name|resolvePropertyPlaceholders
parameter_list|)
block|{
name|this
operator|.
name|resolvePropertyPlaceholders
operator|=
name|resolvePropertyPlaceholders
expr_stmt|;
block|}
block|}
end_class

end_unit

