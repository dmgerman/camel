begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.websocket.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|websocket
operator|.
name|springboot
package|;
end_package

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
name|component
operator|.
name|websocket
operator|.
name|WebSocketFactory
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
comment|/**  * The websocket component provides websocket endpoints with Jetty for  * communicating with clients using websocket.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
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
literal|"camel.component.websocket"
argument_list|)
DECL|class|WebsocketComponentConfiguration
specifier|public
class|class
name|WebsocketComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * Whether to enable auto configuration of the websocket component. This is      * enabled by default.      */
DECL|field|enabled
specifier|private
name|Boolean
name|enabled
decl_stmt|;
comment|/**      * Set a resource path for static resources (such as .html files etc). The      * resources can be loaded from classpath, if you prefix with classpath:,      * otherwise the resources is loaded from file system or from JAR files. For      * example to load from root classpath use classpath:., or      * classpath:WEB-INF/static If not configured (eg null) then no static      * resource is in use.      */
DECL|field|staticResources
specifier|private
name|String
name|staticResources
decl_stmt|;
comment|/**      * The hostname. The default value is 0.0.0.0      */
DECL|field|host
specifier|private
name|String
name|host
init|=
literal|"0.0.0.0"
decl_stmt|;
comment|/**      * The port number. The default value is 9292      */
DECL|field|port
specifier|private
name|Integer
name|port
init|=
literal|9292
decl_stmt|;
comment|/**      * The password for the keystore when using SSL.      */
DECL|field|sslKeyPassword
specifier|private
name|String
name|sslKeyPassword
decl_stmt|;
comment|/**      * The password when using SSL.      */
DECL|field|sslPassword
specifier|private
name|String
name|sslPassword
decl_stmt|;
comment|/**      * The path to the keystore.      */
DECL|field|sslKeystore
specifier|private
name|String
name|sslKeystore
decl_stmt|;
comment|/**      * If this option is true, Jetty JMX support will be enabled for this      * endpoint. See Jetty JMX support for more details.      */
DECL|field|enableJmx
specifier|private
name|Boolean
name|enableJmx
init|=
literal|false
decl_stmt|;
comment|/**      * To set a value for minimum number of threads in server thread pool.      * MaxThreads/minThreads or threadPool fields are required due to switch to      * Jetty9. The default values for minThreads is 1.      */
DECL|field|minThreads
specifier|private
name|Integer
name|minThreads
decl_stmt|;
comment|/**      * To set a value for maximum number of threads in server thread pool.      * MaxThreads/minThreads or threadPool fields are required due to switch to      * Jetty9. The default values for maxThreads is 1 2 noCores.      */
DECL|field|maxThreads
specifier|private
name|Integer
name|maxThreads
decl_stmt|;
comment|/**      * To use a custom thread pool for the server. MaxThreads/minThreads or      * threadPool fields are required due to switch to Jetty9. The option is a      * org.eclipse.jetty.util.thread.ThreadPool type.      */
DECL|field|threadPool
specifier|private
name|String
name|threadPool
decl_stmt|;
comment|/**      * To configure security using SSLContextParameters. The option is a      * org.apache.camel.support.jsse.SSLContextParameters type.      */
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
comment|/**      * To configure a map which contains custom WebSocketFactory for sub      * protocols. The key in the map is the sub protocol. The default key is      * reserved for the default implementation.      */
DECL|field|socketFactory
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|WebSocketFactory
argument_list|>
name|socketFactory
decl_stmt|;
comment|/**      * Whether the component should resolve property placeholders on itself when      * starting. Only properties which are of String type can use property      * placeholders.      */
DECL|field|resolvePropertyPlaceholders
specifier|private
name|Boolean
name|resolvePropertyPlaceholders
init|=
literal|true
decl_stmt|;
comment|/**      * Whether the component should use basic property binding (Camel 2.x) or      * the newer property binding with additional capabilities      */
DECL|field|basicPropertyBinding
specifier|private
name|Boolean
name|basicPropertyBinding
init|=
literal|false
decl_stmt|;
DECL|method|getStaticResources ()
specifier|public
name|String
name|getStaticResources
parameter_list|()
block|{
return|return
name|staticResources
return|;
block|}
DECL|method|setStaticResources (String staticResources)
specifier|public
name|void
name|setStaticResources
parameter_list|(
name|String
name|staticResources
parameter_list|)
block|{
name|this
operator|.
name|staticResources
operator|=
name|staticResources
expr_stmt|;
block|}
DECL|method|getHost ()
specifier|public
name|String
name|getHost
parameter_list|()
block|{
return|return
name|host
return|;
block|}
DECL|method|setHost (String host)
specifier|public
name|void
name|setHost
parameter_list|(
name|String
name|host
parameter_list|)
block|{
name|this
operator|.
name|host
operator|=
name|host
expr_stmt|;
block|}
DECL|method|getPort ()
specifier|public
name|Integer
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
DECL|method|setPort (Integer port)
specifier|public
name|void
name|setPort
parameter_list|(
name|Integer
name|port
parameter_list|)
block|{
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
block|}
DECL|method|getSslKeyPassword ()
specifier|public
name|String
name|getSslKeyPassword
parameter_list|()
block|{
return|return
name|sslKeyPassword
return|;
block|}
DECL|method|setSslKeyPassword (String sslKeyPassword)
specifier|public
name|void
name|setSslKeyPassword
parameter_list|(
name|String
name|sslKeyPassword
parameter_list|)
block|{
name|this
operator|.
name|sslKeyPassword
operator|=
name|sslKeyPassword
expr_stmt|;
block|}
DECL|method|getSslPassword ()
specifier|public
name|String
name|getSslPassword
parameter_list|()
block|{
return|return
name|sslPassword
return|;
block|}
DECL|method|setSslPassword (String sslPassword)
specifier|public
name|void
name|setSslPassword
parameter_list|(
name|String
name|sslPassword
parameter_list|)
block|{
name|this
operator|.
name|sslPassword
operator|=
name|sslPassword
expr_stmt|;
block|}
DECL|method|getSslKeystore ()
specifier|public
name|String
name|getSslKeystore
parameter_list|()
block|{
return|return
name|sslKeystore
return|;
block|}
DECL|method|setSslKeystore (String sslKeystore)
specifier|public
name|void
name|setSslKeystore
parameter_list|(
name|String
name|sslKeystore
parameter_list|)
block|{
name|this
operator|.
name|sslKeystore
operator|=
name|sslKeystore
expr_stmt|;
block|}
DECL|method|getEnableJmx ()
specifier|public
name|Boolean
name|getEnableJmx
parameter_list|()
block|{
return|return
name|enableJmx
return|;
block|}
DECL|method|setEnableJmx (Boolean enableJmx)
specifier|public
name|void
name|setEnableJmx
parameter_list|(
name|Boolean
name|enableJmx
parameter_list|)
block|{
name|this
operator|.
name|enableJmx
operator|=
name|enableJmx
expr_stmt|;
block|}
DECL|method|getMinThreads ()
specifier|public
name|Integer
name|getMinThreads
parameter_list|()
block|{
return|return
name|minThreads
return|;
block|}
DECL|method|setMinThreads (Integer minThreads)
specifier|public
name|void
name|setMinThreads
parameter_list|(
name|Integer
name|minThreads
parameter_list|)
block|{
name|this
operator|.
name|minThreads
operator|=
name|minThreads
expr_stmt|;
block|}
DECL|method|getMaxThreads ()
specifier|public
name|Integer
name|getMaxThreads
parameter_list|()
block|{
return|return
name|maxThreads
return|;
block|}
DECL|method|setMaxThreads (Integer maxThreads)
specifier|public
name|void
name|setMaxThreads
parameter_list|(
name|Integer
name|maxThreads
parameter_list|)
block|{
name|this
operator|.
name|maxThreads
operator|=
name|maxThreads
expr_stmt|;
block|}
DECL|method|getThreadPool ()
specifier|public
name|String
name|getThreadPool
parameter_list|()
block|{
return|return
name|threadPool
return|;
block|}
DECL|method|setThreadPool (String threadPool)
specifier|public
name|void
name|setThreadPool
parameter_list|(
name|String
name|threadPool
parameter_list|)
block|{
name|this
operator|.
name|threadPool
operator|=
name|threadPool
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
DECL|method|getSocketFactory ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|WebSocketFactory
argument_list|>
name|getSocketFactory
parameter_list|()
block|{
return|return
name|socketFactory
return|;
block|}
DECL|method|setSocketFactory (Map<String, WebSocketFactory> socketFactory)
specifier|public
name|void
name|setSocketFactory
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|WebSocketFactory
argument_list|>
name|socketFactory
parameter_list|)
block|{
name|this
operator|.
name|socketFactory
operator|=
name|socketFactory
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
DECL|method|getBasicPropertyBinding ()
specifier|public
name|Boolean
name|getBasicPropertyBinding
parameter_list|()
block|{
return|return
name|basicPropertyBinding
return|;
block|}
DECL|method|setBasicPropertyBinding (Boolean basicPropertyBinding)
specifier|public
name|void
name|setBasicPropertyBinding
parameter_list|(
name|Boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|this
operator|.
name|basicPropertyBinding
operator|=
name|basicPropertyBinding
expr_stmt|;
block|}
block|}
end_class

end_unit

