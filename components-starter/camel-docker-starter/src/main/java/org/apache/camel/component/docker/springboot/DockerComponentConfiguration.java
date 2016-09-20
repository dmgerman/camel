begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.docker.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|docker
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|docker
operator|.
name|DockerOperation
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
comment|/**  * The docker component is used for managing Docker containers.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.docker"
argument_list|)
DECL|class|DockerComponentConfiguration
specifier|public
class|class
name|DockerComponentConfiguration
block|{
comment|/**      * To use the shared docker configuration      */
DECL|field|configuration
specifier|private
name|DockerConfigurationNestedConfiguration
name|configuration
decl_stmt|;
DECL|method|getConfiguration ()
specifier|public
name|DockerConfigurationNestedConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration ( DockerConfigurationNestedConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|DockerConfigurationNestedConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|class|DockerConfigurationNestedConfiguration
specifier|public
specifier|static
class|class
name|DockerConfigurationNestedConfiguration
block|{
DECL|field|CAMEL_NESTED_CLASS
specifier|public
specifier|static
specifier|final
name|Class
name|CAMEL_NESTED_CLASS
init|=
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|docker
operator|.
name|DockerConfiguration
operator|.
name|class
decl_stmt|;
comment|/**          * Docker host          */
DECL|field|host
specifier|private
name|String
name|host
init|=
literal|"localhost"
decl_stmt|;
comment|/**          * Docker port          */
DECL|field|port
specifier|private
name|Integer
name|port
decl_stmt|;
comment|/**          * User name to authenticate with          */
DECL|field|username
specifier|private
name|String
name|username
decl_stmt|;
comment|/**          * Password to authenticate with          */
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
comment|/**          * Email address associated with the user          */
DECL|field|email
specifier|private
name|String
name|email
decl_stmt|;
comment|/**          * Server address for docker registry.          */
DECL|field|serverAddress
specifier|private
name|String
name|serverAddress
init|=
literal|"https://index.docker.io/v1/"
decl_stmt|;
comment|/**          * Request timeout for response (in seconds)          */
DECL|field|requestTimeout
specifier|private
name|Integer
name|requestTimeout
decl_stmt|;
comment|/**          * Use HTTPS communication          */
DECL|field|secure
specifier|private
name|Boolean
name|secure
decl_stmt|;
comment|/**          * Location containing the SSL certificate chain          */
DECL|field|certPath
specifier|private
name|String
name|certPath
decl_stmt|;
comment|/**          * Maximum total connections          */
DECL|field|maxTotalConnections
specifier|private
name|Integer
name|maxTotalConnections
decl_stmt|;
comment|/**          * Maximum route connections          */
DECL|field|maxPerRouteConnections
specifier|private
name|Integer
name|maxPerRouteConnections
decl_stmt|;
comment|/**          * Whether to use logging filter          */
DECL|field|loggingFilter
specifier|private
name|Boolean
name|loggingFilter
decl_stmt|;
comment|/**          * Whether to follow redirect filter          */
DECL|field|followRedirectFilter
specifier|private
name|Boolean
name|followRedirectFilter
decl_stmt|;
comment|/**          * Additional configuration parameters as key/value pairs          */
DECL|field|parameters
specifier|private
name|Map
name|parameters
decl_stmt|;
comment|/**          * Which operation to use          */
DECL|field|operation
specifier|private
name|DockerOperation
name|operation
decl_stmt|;
comment|/**          * Check TLS          */
DECL|field|tlsVerify
specifier|private
name|Boolean
name|tlsVerify
decl_stmt|;
comment|/**          * Socket connection mode          */
DECL|field|socket
specifier|private
name|Boolean
name|socket
decl_stmt|;
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
DECL|method|getUsername ()
specifier|public
name|String
name|getUsername
parameter_list|()
block|{
return|return
name|username
return|;
block|}
DECL|method|setUsername (String username)
specifier|public
name|void
name|setUsername
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|this
operator|.
name|username
operator|=
name|username
expr_stmt|;
block|}
DECL|method|getPassword ()
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|password
return|;
block|}
DECL|method|setPassword (String password)
specifier|public
name|void
name|setPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
block|}
DECL|method|getEmail ()
specifier|public
name|String
name|getEmail
parameter_list|()
block|{
return|return
name|email
return|;
block|}
DECL|method|setEmail (String email)
specifier|public
name|void
name|setEmail
parameter_list|(
name|String
name|email
parameter_list|)
block|{
name|this
operator|.
name|email
operator|=
name|email
expr_stmt|;
block|}
DECL|method|getServerAddress ()
specifier|public
name|String
name|getServerAddress
parameter_list|()
block|{
return|return
name|serverAddress
return|;
block|}
DECL|method|setServerAddress (String serverAddress)
specifier|public
name|void
name|setServerAddress
parameter_list|(
name|String
name|serverAddress
parameter_list|)
block|{
name|this
operator|.
name|serverAddress
operator|=
name|serverAddress
expr_stmt|;
block|}
DECL|method|getRequestTimeout ()
specifier|public
name|Integer
name|getRequestTimeout
parameter_list|()
block|{
return|return
name|requestTimeout
return|;
block|}
DECL|method|setRequestTimeout (Integer requestTimeout)
specifier|public
name|void
name|setRequestTimeout
parameter_list|(
name|Integer
name|requestTimeout
parameter_list|)
block|{
name|this
operator|.
name|requestTimeout
operator|=
name|requestTimeout
expr_stmt|;
block|}
DECL|method|getSecure ()
specifier|public
name|Boolean
name|getSecure
parameter_list|()
block|{
return|return
name|secure
return|;
block|}
DECL|method|setSecure (Boolean secure)
specifier|public
name|void
name|setSecure
parameter_list|(
name|Boolean
name|secure
parameter_list|)
block|{
name|this
operator|.
name|secure
operator|=
name|secure
expr_stmt|;
block|}
DECL|method|getCertPath ()
specifier|public
name|String
name|getCertPath
parameter_list|()
block|{
return|return
name|certPath
return|;
block|}
DECL|method|setCertPath (String certPath)
specifier|public
name|void
name|setCertPath
parameter_list|(
name|String
name|certPath
parameter_list|)
block|{
name|this
operator|.
name|certPath
operator|=
name|certPath
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
DECL|method|getMaxPerRouteConnections ()
specifier|public
name|Integer
name|getMaxPerRouteConnections
parameter_list|()
block|{
return|return
name|maxPerRouteConnections
return|;
block|}
DECL|method|setMaxPerRouteConnections (Integer maxPerRouteConnections)
specifier|public
name|void
name|setMaxPerRouteConnections
parameter_list|(
name|Integer
name|maxPerRouteConnections
parameter_list|)
block|{
name|this
operator|.
name|maxPerRouteConnections
operator|=
name|maxPerRouteConnections
expr_stmt|;
block|}
DECL|method|getLoggingFilter ()
specifier|public
name|Boolean
name|getLoggingFilter
parameter_list|()
block|{
return|return
name|loggingFilter
return|;
block|}
DECL|method|setLoggingFilter (Boolean loggingFilter)
specifier|public
name|void
name|setLoggingFilter
parameter_list|(
name|Boolean
name|loggingFilter
parameter_list|)
block|{
name|this
operator|.
name|loggingFilter
operator|=
name|loggingFilter
expr_stmt|;
block|}
DECL|method|getFollowRedirectFilter ()
specifier|public
name|Boolean
name|getFollowRedirectFilter
parameter_list|()
block|{
return|return
name|followRedirectFilter
return|;
block|}
DECL|method|setFollowRedirectFilter (Boolean followRedirectFilter)
specifier|public
name|void
name|setFollowRedirectFilter
parameter_list|(
name|Boolean
name|followRedirectFilter
parameter_list|)
block|{
name|this
operator|.
name|followRedirectFilter
operator|=
name|followRedirectFilter
expr_stmt|;
block|}
DECL|method|getParameters ()
specifier|public
name|Map
name|getParameters
parameter_list|()
block|{
return|return
name|parameters
return|;
block|}
DECL|method|setParameters (Map parameters)
specifier|public
name|void
name|setParameters
parameter_list|(
name|Map
name|parameters
parameter_list|)
block|{
name|this
operator|.
name|parameters
operator|=
name|parameters
expr_stmt|;
block|}
DECL|method|getOperation ()
specifier|public
name|DockerOperation
name|getOperation
parameter_list|()
block|{
return|return
name|operation
return|;
block|}
DECL|method|setOperation (DockerOperation operation)
specifier|public
name|void
name|setOperation
parameter_list|(
name|DockerOperation
name|operation
parameter_list|)
block|{
name|this
operator|.
name|operation
operator|=
name|operation
expr_stmt|;
block|}
DECL|method|getTlsVerify ()
specifier|public
name|Boolean
name|getTlsVerify
parameter_list|()
block|{
return|return
name|tlsVerify
return|;
block|}
DECL|method|setTlsVerify (Boolean tlsVerify)
specifier|public
name|void
name|setTlsVerify
parameter_list|(
name|Boolean
name|tlsVerify
parameter_list|)
block|{
name|this
operator|.
name|tlsVerify
operator|=
name|tlsVerify
expr_stmt|;
block|}
DECL|method|getSocket ()
specifier|public
name|Boolean
name|getSocket
parameter_list|()
block|{
return|return
name|socket
return|;
block|}
DECL|method|setSocket (Boolean socket)
specifier|public
name|void
name|setSocket
parameter_list|(
name|Boolean
name|socket
parameter_list|)
block|{
name|this
operator|.
name|socket
operator|=
name|socket
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

