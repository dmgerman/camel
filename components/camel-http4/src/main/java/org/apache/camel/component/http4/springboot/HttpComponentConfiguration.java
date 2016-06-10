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
name|HttpConfiguration
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
name|HeaderFilterStrategy
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
name|jsse
operator|.
name|SSLContextParameters
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|client
operator|.
name|CookieStore
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|conn
operator|.
name|HttpClientConnectionManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|conn
operator|.
name|ssl
operator|.
name|X509HostnameVerifier
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|protocol
operator|.
name|HttpContext
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
block|{
comment|/**      * To use the custom HttpClientConfigurer to perform configuration of the      * HttpClient that will be used.      */
DECL|field|httpClientConfigurer
specifier|private
name|HttpClientConfigurer
name|httpClientConfigurer
decl_stmt|;
comment|/**      * To use a custom HttpClientConnectionManager to manage connections      */
DECL|field|clientConnectionManager
specifier|private
name|HttpClientConnectionManager
name|clientConnectionManager
decl_stmt|;
comment|/**      * To use a custom HttpBinding to control the mapping between Camel message      * and HttpClient.      */
DECL|field|httpBinding
specifier|private
name|HttpBinding
name|httpBinding
decl_stmt|;
comment|/**      * To use the shared HttpConfiguration as base configuration.      */
DECL|field|httpConfiguration
specifier|private
name|HttpConfiguration
name|httpConfiguration
decl_stmt|;
comment|/**      * Whether to allow java serialization when a request uses      * context-type=application/x-java-serialized-object This is by default      * turned off. If you enable this then be aware that Java will deserialize      * the incoming data from the request to Java and that can be a potential      * security risk.      */
DECL|field|allowJavaSerializedObject
specifier|private
name|Boolean
name|allowJavaSerializedObject
init|=
literal|false
decl_stmt|;
comment|/**      * To use a custom org.apache.http.protocol.HttpContext when executing      * requests.      */
DECL|field|httpContext
specifier|private
name|HttpContext
name|httpContext
decl_stmt|;
comment|/**      * To configure security using SSLContextParameters. Important: Only one      * instance of org.apache.camel.util.jsse.SSLContextParameters is supported      * per HttpComponent. If you need to use 2 or more different instances you      * need to define a new HttpComponent per instance you need.      */
DECL|field|sslContextParameters
specifier|private
name|SSLContextParameters
name|sslContextParameters
decl_stmt|;
comment|/**      * To use a custom X509HostnameVerifier such as      * org.apache.http.conn.ssl.StrictHostnameVerifier or      * org.apache.http.conn.ssl.AllowAllHostnameVerifier.      */
DECL|field|x509HostnameVerifier
specifier|private
name|X509HostnameVerifier
name|x509HostnameVerifier
decl_stmt|;
comment|/**      * The maximum number of connections.      */
DECL|field|maxTotalConnections
specifier|private
name|int
name|maxTotalConnections
decl_stmt|;
comment|/**      * The maximum number of connections per route.      */
DECL|field|connectionsPerRoute
specifier|private
name|int
name|connectionsPerRoute
decl_stmt|;
comment|/**      * The time for connection to live the time unit is millisecond the default      * value is always keep alive.      */
DECL|field|connectionTimeToLive
specifier|private
name|long
name|connectionTimeToLive
decl_stmt|;
comment|/**      * To use a custom org.apache.http.client.CookieStore. By default the      * org.apache.http.impl.client.BasicCookieStore is used which is an      * in-memory only cookie store. Notice if bridgeEndpoint=true then the      * cookie store is forced to be a noop cookie store as cookie shouldn't be      * stored as we are just bridging (eg acting as a proxy).      */
DECL|field|cookieStore
specifier|private
name|CookieStore
name|cookieStore
decl_stmt|;
comment|/**      * To use a custom HeaderFilterStrategy to filter header to and from Camel      * message.      */
DECL|field|headerFilterStrategy
specifier|private
name|HeaderFilterStrategy
name|headerFilterStrategy
decl_stmt|;
DECL|method|getHttpClientConfigurer ()
specifier|public
name|HttpClientConfigurer
name|getHttpClientConfigurer
parameter_list|()
block|{
return|return
name|httpClientConfigurer
return|;
block|}
DECL|method|setHttpClientConfigurer ( HttpClientConfigurer httpClientConfigurer)
specifier|public
name|void
name|setHttpClientConfigurer
parameter_list|(
name|HttpClientConfigurer
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
name|HttpClientConnectionManager
name|getClientConnectionManager
parameter_list|()
block|{
return|return
name|clientConnectionManager
return|;
block|}
DECL|method|setClientConnectionManager ( HttpClientConnectionManager clientConnectionManager)
specifier|public
name|void
name|setClientConnectionManager
parameter_list|(
name|HttpClientConnectionManager
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
DECL|method|getHttpBinding ()
specifier|public
name|HttpBinding
name|getHttpBinding
parameter_list|()
block|{
return|return
name|httpBinding
return|;
block|}
DECL|method|setHttpBinding (HttpBinding httpBinding)
specifier|public
name|void
name|setHttpBinding
parameter_list|(
name|HttpBinding
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
name|HttpConfiguration
name|getHttpConfiguration
parameter_list|()
block|{
return|return
name|httpConfiguration
return|;
block|}
DECL|method|setHttpConfiguration (HttpConfiguration httpConfiguration)
specifier|public
name|void
name|setHttpConfiguration
parameter_list|(
name|HttpConfiguration
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
DECL|method|getHttpContext ()
specifier|public
name|HttpContext
name|getHttpContext
parameter_list|()
block|{
return|return
name|httpContext
return|;
block|}
DECL|method|setHttpContext (HttpContext httpContext)
specifier|public
name|void
name|setHttpContext
parameter_list|(
name|HttpContext
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
name|SSLContextParameters
name|getSslContextParameters
parameter_list|()
block|{
return|return
name|sslContextParameters
return|;
block|}
DECL|method|setSslContextParameters ( SSLContextParameters sslContextParameters)
specifier|public
name|void
name|setSslContextParameters
parameter_list|(
name|SSLContextParameters
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
DECL|method|getX509HostnameVerifier ()
specifier|public
name|X509HostnameVerifier
name|getX509HostnameVerifier
parameter_list|()
block|{
return|return
name|x509HostnameVerifier
return|;
block|}
DECL|method|setX509HostnameVerifier ( X509HostnameVerifier x509HostnameVerifier)
specifier|public
name|void
name|setX509HostnameVerifier
parameter_list|(
name|X509HostnameVerifier
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
name|int
name|getMaxTotalConnections
parameter_list|()
block|{
return|return
name|maxTotalConnections
return|;
block|}
DECL|method|setMaxTotalConnections (int maxTotalConnections)
specifier|public
name|void
name|setMaxTotalConnections
parameter_list|(
name|int
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
name|int
name|getConnectionsPerRoute
parameter_list|()
block|{
return|return
name|connectionsPerRoute
return|;
block|}
DECL|method|setConnectionsPerRoute (int connectionsPerRoute)
specifier|public
name|void
name|setConnectionsPerRoute
parameter_list|(
name|int
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
name|long
name|getConnectionTimeToLive
parameter_list|()
block|{
return|return
name|connectionTimeToLive
return|;
block|}
DECL|method|setConnectionTimeToLive (long connectionTimeToLive)
specifier|public
name|void
name|setConnectionTimeToLive
parameter_list|(
name|long
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
name|CookieStore
name|getCookieStore
parameter_list|()
block|{
return|return
name|cookieStore
return|;
block|}
DECL|method|setCookieStore (CookieStore cookieStore)
specifier|public
name|void
name|setCookieStore
parameter_list|(
name|CookieStore
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
DECL|method|getHeaderFilterStrategy ()
specifier|public
name|HeaderFilterStrategy
name|getHeaderFilterStrategy
parameter_list|()
block|{
return|return
name|headerFilterStrategy
return|;
block|}
DECL|method|setHeaderFilterStrategy ( HeaderFilterStrategy headerFilterStrategy)
specifier|public
name|void
name|setHeaderFilterStrategy
parameter_list|(
name|HeaderFilterStrategy
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
block|}
end_class

end_unit

