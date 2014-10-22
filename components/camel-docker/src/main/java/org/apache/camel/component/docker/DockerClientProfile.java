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
name|net
operator|.
name|MalformedURLException
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
name|exception
operator|.
name|DockerException
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
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * The elements representing a client initiating a connection to Docker  */
end_comment

begin_class
DECL|class|DockerClientProfile
specifier|public
class|class
name|DockerClientProfile
block|{
DECL|field|host
specifier|private
name|String
name|host
decl_stmt|;
DECL|field|port
specifier|private
name|Integer
name|port
decl_stmt|;
DECL|field|username
specifier|private
name|String
name|username
decl_stmt|;
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
DECL|field|email
specifier|private
name|String
name|email
decl_stmt|;
DECL|field|serverAddress
specifier|private
name|String
name|serverAddress
decl_stmt|;
DECL|field|requestTimeout
specifier|private
name|Integer
name|requestTimeout
decl_stmt|;
DECL|field|secure
specifier|private
name|boolean
name|secure
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
name|boolean
name|isSecure
parameter_list|()
block|{
return|return
name|secure
return|;
block|}
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
DECL|method|toUrl ()
specifier|public
name|String
name|toUrl
parameter_list|()
throws|throws
name|DockerException
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|this
operator|.
name|host
argument_list|,
literal|"host"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|this
operator|.
name|port
argument_list|,
literal|"port"
argument_list|)
expr_stmt|;
name|URL
name|uri
decl_stmt|;
name|String
name|secure
init|=
name|this
operator|.
name|secure
condition|?
literal|"https"
else|:
literal|"http"
decl_stmt|;
try|try
block|{
name|uri
operator|=
operator|new
name|URL
argument_list|(
name|secure
argument_list|,
name|this
operator|.
name|host
argument_list|,
name|this
operator|.
name|port
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|DockerException
argument_list|(
name|e
argument_list|)
throw|;
block|}
return|return
name|uri
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
specifier|final
name|int
name|prime
init|=
literal|31
decl_stmt|;
name|int
name|result
init|=
literal|1
decl_stmt|;
name|result
operator|=
name|prime
operator|*
name|result
operator|+
operator|(
operator|(
name|email
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|email
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
name|result
operator|=
name|prime
operator|*
name|result
operator|+
operator|(
operator|(
name|host
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|host
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
name|result
operator|=
name|prime
operator|*
name|result
operator|+
operator|(
operator|(
name|password
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|password
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
name|result
operator|=
name|prime
operator|*
name|result
operator|+
operator|(
operator|(
name|port
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|port
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
name|result
operator|=
name|prime
operator|*
name|result
operator|+
operator|(
operator|(
name|requestTimeout
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|requestTimeout
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
name|result
operator|=
name|prime
operator|*
name|result
operator|+
operator|(
name|secure
condition|?
literal|1231
else|:
literal|1237
operator|)
expr_stmt|;
name|result
operator|=
name|prime
operator|*
name|result
operator|+
operator|(
operator|(
name|serverAddress
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|serverAddress
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
name|result
operator|=
name|prime
operator|*
name|result
operator|+
operator|(
operator|(
name|username
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|username
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|equals (Object obj)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|obj
condition|)
return|return
literal|true
return|;
if|if
condition|(
name|obj
operator|==
literal|null
condition|)
return|return
literal|false
return|;
if|if
condition|(
name|getClass
argument_list|()
operator|!=
name|obj
operator|.
name|getClass
argument_list|()
condition|)
return|return
literal|false
return|;
name|DockerClientProfile
name|other
init|=
operator|(
name|DockerClientProfile
operator|)
name|obj
decl_stmt|;
if|if
condition|(
name|email
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|other
operator|.
name|email
operator|!=
literal|null
condition|)
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
operator|!
name|email
operator|.
name|equals
argument_list|(
name|other
operator|.
name|email
argument_list|)
condition|)
return|return
literal|false
return|;
if|if
condition|(
name|host
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|other
operator|.
name|host
operator|!=
literal|null
condition|)
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
operator|!
name|host
operator|.
name|equals
argument_list|(
name|other
operator|.
name|host
argument_list|)
condition|)
return|return
literal|false
return|;
if|if
condition|(
name|password
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|other
operator|.
name|password
operator|!=
literal|null
condition|)
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
operator|!
name|password
operator|.
name|equals
argument_list|(
name|other
operator|.
name|password
argument_list|)
condition|)
return|return
literal|false
return|;
if|if
condition|(
name|port
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|other
operator|.
name|port
operator|!=
literal|null
condition|)
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
operator|!
name|port
operator|.
name|equals
argument_list|(
name|other
operator|.
name|port
argument_list|)
condition|)
return|return
literal|false
return|;
if|if
condition|(
name|requestTimeout
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|other
operator|.
name|requestTimeout
operator|!=
literal|null
condition|)
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
operator|!
name|requestTimeout
operator|.
name|equals
argument_list|(
name|other
operator|.
name|requestTimeout
argument_list|)
condition|)
return|return
literal|false
return|;
if|if
condition|(
name|secure
operator|!=
name|other
operator|.
name|secure
condition|)
return|return
literal|false
return|;
if|if
condition|(
name|serverAddress
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|other
operator|.
name|serverAddress
operator|!=
literal|null
condition|)
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
operator|!
name|serverAddress
operator|.
name|equals
argument_list|(
name|other
operator|.
name|serverAddress
argument_list|)
condition|)
return|return
literal|false
return|;
if|if
condition|(
name|username
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|other
operator|.
name|username
operator|!=
literal|null
condition|)
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
operator|!
name|username
operator|.
name|equals
argument_list|(
name|other
operator|.
name|username
argument_list|)
condition|)
return|return
literal|false
return|;
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

