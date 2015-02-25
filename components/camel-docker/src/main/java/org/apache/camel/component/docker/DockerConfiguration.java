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
DECL|field|port
specifier|private
name|Integer
name|port
init|=
literal|2375
decl_stmt|;
annotation|@
name|UriParam
DECL|field|username
specifier|private
name|String
name|username
decl_stmt|;
annotation|@
name|UriParam
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
DECL|field|secure
specifier|private
name|Boolean
name|secure
decl_stmt|;
annotation|@
name|UriParam
DECL|field|certPath
specifier|private
name|String
name|certPath
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
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
DECL|method|isSecure ()
specifier|public
name|Boolean
name|isSecure
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

