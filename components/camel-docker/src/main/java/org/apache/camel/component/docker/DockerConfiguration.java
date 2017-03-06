begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.docker
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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|RuntimeCamelException
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
name|Metadata
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
name|UriParams
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

begin_class
annotation|@
name|UriParams
DECL|class|DockerConfiguration
specifier|public
class|class
name|DockerConfiguration
implements|implements
name|Cloneable
block|{
annotation|@
name|UriPath
argument_list|(
name|enums
operator|=
literal|"events,stats,auth,info,ping,version,imagebuild,imagecreate,imageinspect,imagelist,imagepull,imagepush"
operator|+
literal|"imageremove,imagesearch,imagetag,containerattach,containercommit,containercopyfile,containercreate,containerdiff"
operator|+
literal|"inspectcontainer,containerkill,containerlist,containerlog,containerpause,containerrestart,containerremove,containerstart"
operator|+
literal|"containerstop,containertop,containerunpause,containerwait,execcreate,execstart"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|operation
specifier|private
name|DockerOperation
name|operation
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"localhost"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|host
specifier|private
name|String
name|host
init|=
literal|"localhost"
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"2375"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|port
specifier|private
name|Integer
name|port
init|=
literal|2375
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
DECL|field|username
specifier|private
name|String
name|username
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
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
annotation|@
name|UriParam
DECL|field|email
specifier|private
name|String
name|email
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|defaultValue
operator|=
literal|"https://index.docker.io/v1/"
argument_list|)
DECL|field|serverAddress
specifier|private
name|String
name|serverAddress
init|=
literal|"https://index.docker.io/v1/"
decl_stmt|;
annotation|@
name|UriParam
DECL|field|requestTimeout
specifier|private
name|Integer
name|requestTimeout
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|)
DECL|field|secure
specifier|private
name|boolean
name|secure
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|)
DECL|field|certPath
specifier|private
name|String
name|certPath
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|defaultValue
operator|=
literal|"100"
argument_list|)
DECL|field|maxTotalConnections
specifier|private
name|Integer
name|maxTotalConnections
init|=
literal|100
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|defaultValue
operator|=
literal|"100"
argument_list|)
DECL|field|maxPerRouteConnections
specifier|private
name|Integer
name|maxPerRouteConnections
init|=
literal|100
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|loggingFilter
specifier|private
name|boolean
name|loggingFilter
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|followRedirectFilter
specifier|private
name|boolean
name|followRedirectFilter
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|tlsVerify
specifier|private
name|boolean
name|tlsVerify
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|socket
specifier|private
name|boolean
name|socket
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|defaultValue
operator|=
literal|"com.github.dockerjava.jaxrs.JerseyDockerCmdExecFactory"
argument_list|)
DECL|field|cmdExecFactory
specifier|private
name|String
name|cmdExecFactory
init|=
literal|"com.github.dockerjava.jaxrs.JerseyDockerCmdExecFactory"
decl_stmt|;
DECL|field|parameters
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
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
comment|/**      * Docker host      */
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
comment|/**      * Docker port      */
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
comment|/**      * User name to authenticate with      */
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
comment|/**      * Password to authenticate with      */
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
comment|/**      * Email address associated with the user      */
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
comment|/**      * Server address for docker registry.      */
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
comment|/**      * Request timeout for response (in seconds)      */
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
DECL|method|isSecure ()
specifier|public
name|boolean
name|isSecure
parameter_list|()
block|{
return|return
name|secure
return|;
block|}
comment|/**      * Use HTTPS communication      */
DECL|method|setSecure (boolean secure)
specifier|public
name|void
name|setSecure
parameter_list|(
name|boolean
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
comment|/**      * Location containing the SSL certificate chain      */
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
comment|/**      * Maximum total connections      */
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
comment|/**      * Maximum route connections      */
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
DECL|method|isLoggingFilterEnabled ()
specifier|public
name|boolean
name|isLoggingFilterEnabled
parameter_list|()
block|{
return|return
name|loggingFilter
return|;
block|}
comment|/**      * Whether to use logging filter      */
DECL|method|setLoggingFilter (boolean loggingFilterEnabled)
specifier|public
name|void
name|setLoggingFilter
parameter_list|(
name|boolean
name|loggingFilterEnabled
parameter_list|)
block|{
name|this
operator|.
name|loggingFilter
operator|=
name|loggingFilterEnabled
expr_stmt|;
block|}
DECL|method|isFollowRedirectFilterEnabled ()
specifier|public
name|boolean
name|isFollowRedirectFilterEnabled
parameter_list|()
block|{
return|return
name|followRedirectFilter
return|;
block|}
comment|/**      * Whether to follow redirect filter      */
DECL|method|setFollowRedirectFilter (boolean followRedirectFilterEnabled)
specifier|public
name|void
name|setFollowRedirectFilter
parameter_list|(
name|boolean
name|followRedirectFilterEnabled
parameter_list|)
block|{
name|this
operator|.
name|followRedirectFilter
operator|=
name|followRedirectFilterEnabled
expr_stmt|;
block|}
DECL|method|getParameters ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getParameters
parameter_list|()
block|{
return|return
name|parameters
return|;
block|}
comment|/**      * Additional configuration parameters as key/value pairs      */
DECL|method|setParameters (Map<String, Object> parameters)
specifier|public
name|void
name|setParameters
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
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
comment|/**      * Which operation to use      */
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
DECL|method|isTlsVerify ()
specifier|public
name|boolean
name|isTlsVerify
parameter_list|()
block|{
return|return
name|tlsVerify
return|;
block|}
comment|/**      * Check TLS       */
DECL|method|setTlsVerify (boolean tlsVerify)
specifier|public
name|void
name|setTlsVerify
parameter_list|(
name|boolean
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
DECL|method|isSocket ()
specifier|public
name|boolean
name|isSocket
parameter_list|()
block|{
return|return
name|socket
return|;
block|}
comment|/**      * Socket connection mode      */
DECL|method|setSocket (boolean socket)
specifier|public
name|void
name|setSocket
parameter_list|(
name|boolean
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
DECL|method|getCmdExecFactory ()
specifier|public
name|String
name|getCmdExecFactory
parameter_list|()
block|{
return|return
name|cmdExecFactory
return|;
block|}
comment|/**      * The fully qualified class name of the DockerCmdExecFactory implementation to use      */
DECL|method|setCmdExecFactory (String cmdExecFactory)
specifier|public
name|void
name|setCmdExecFactory
parameter_list|(
name|String
name|cmdExecFactory
parameter_list|)
block|{
name|this
operator|.
name|cmdExecFactory
operator|=
name|cmdExecFactory
expr_stmt|;
block|}
DECL|method|copy ()
specifier|public
name|DockerConfiguration
name|copy
parameter_list|()
block|{
try|try
block|{
return|return
operator|(
name|DockerConfiguration
operator|)
name|clone
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|CloneNotSupportedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

