begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mail
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mail
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
name|RuntimeCamelException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|Session
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
name|util
operator|.
name|Properties
import|;
end_import

begin_comment
comment|/**  * Represents the configuration data for communicating over email  *  * @version $Revision: 532790 $  */
end_comment

begin_class
DECL|class|MailConfiguration
specifier|public
class|class
name|MailConfiguration
implements|implements
name|Cloneable
block|{
DECL|field|defaultEncoding
specifier|private
name|String
name|defaultEncoding
decl_stmt|;
DECL|field|host
specifier|private
name|String
name|host
decl_stmt|;
DECL|field|javaMailProperties
specifier|private
name|Properties
name|javaMailProperties
decl_stmt|;
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
DECL|field|protocol
specifier|private
name|String
name|protocol
decl_stmt|;
DECL|field|session
specifier|private
name|Session
name|session
decl_stmt|;
DECL|field|username
specifier|private
name|String
name|username
decl_stmt|;
DECL|field|port
specifier|private
name|int
name|port
init|=
operator|-
literal|1
decl_stmt|;
DECL|field|destination
specifier|private
name|String
name|destination
decl_stmt|;
DECL|field|from
specifier|private
name|String
name|from
init|=
literal|"camel@localhost"
decl_stmt|;
DECL|method|MailConfiguration ()
specifier|public
name|MailConfiguration
parameter_list|()
block|{     }
comment|/**      * Returns a copy of this configuration      */
DECL|method|copy ()
specifier|public
name|MailConfiguration
name|copy
parameter_list|()
block|{
try|try
block|{
return|return
operator|(
name|MailConfiguration
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
DECL|method|configure (URI uri)
specifier|public
name|void
name|configure
parameter_list|(
name|URI
name|uri
parameter_list|)
block|{
name|String
name|value
init|=
name|uri
operator|.
name|getHost
argument_list|()
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|setHost
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
name|String
name|scheme
init|=
name|uri
operator|.
name|getScheme
argument_list|()
decl_stmt|;
if|if
condition|(
name|scheme
operator|!=
literal|null
condition|)
block|{
name|setProtocol
argument_list|(
name|scheme
argument_list|)
expr_stmt|;
block|}
name|String
name|userInfo
init|=
name|uri
operator|.
name|getUserInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|userInfo
operator|!=
literal|null
condition|)
block|{
name|setUsername
argument_list|(
name|userInfo
argument_list|)
expr_stmt|;
block|}
name|int
name|port
init|=
name|uri
operator|.
name|getPort
argument_list|()
decl_stmt|;
if|if
condition|(
name|port
operator|>=
literal|0
condition|)
block|{
name|setPort
argument_list|(
name|port
argument_list|)
expr_stmt|;
block|}
comment|// we can either be invoked with
comment|// mailto:address
comment|// or
comment|// smtp:user@host:port/name@address
name|String
name|fragment
init|=
name|uri
operator|.
name|getFragment
argument_list|()
decl_stmt|;
if|if
condition|(
name|fragment
operator|==
literal|null
operator|||
name|fragment
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|fragment
operator|=
name|userInfo
operator|+
literal|"@"
operator|+
name|host
expr_stmt|;
block|}
name|setDestination
argument_list|(
name|fragment
argument_list|)
expr_stmt|;
block|}
DECL|method|createJavaMailConnection (MailEndpoint mailEndpoint)
specifier|public
name|JavaMailConnection
name|createJavaMailConnection
parameter_list|(
name|MailEndpoint
name|mailEndpoint
parameter_list|)
block|{
name|JavaMailConnection
name|answer
init|=
operator|new
name|JavaMailConnection
argument_list|()
decl_stmt|;
if|if
condition|(
name|defaultEncoding
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setDefaultEncoding
argument_list|(
name|defaultEncoding
argument_list|)
expr_stmt|;
block|}
comment|//answer.setDefaultFileTypeMap(fileTypeMap);
if|if
condition|(
name|host
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setHost
argument_list|(
name|host
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|javaMailProperties
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setJavaMailProperties
argument_list|(
name|javaMailProperties
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|port
operator|>=
literal|0
condition|)
block|{
name|answer
operator|.
name|setPort
argument_list|(
name|port
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|password
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setPassword
argument_list|(
name|password
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|protocol
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setProtocol
argument_list|(
name|protocol
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|session
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setSession
argument_list|(
name|session
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|username
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setUsername
argument_list|(
name|username
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
comment|// Properties
comment|//-------------------------------------------------------------------------
DECL|method|getDefaultEncoding ()
specifier|public
name|String
name|getDefaultEncoding
parameter_list|()
block|{
return|return
name|defaultEncoding
return|;
block|}
DECL|method|setDefaultEncoding (String defaultEncoding)
specifier|public
name|void
name|setDefaultEncoding
parameter_list|(
name|String
name|defaultEncoding
parameter_list|)
block|{
name|this
operator|.
name|defaultEncoding
operator|=
name|defaultEncoding
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
DECL|method|getJavaMailProperties ()
specifier|public
name|Properties
name|getJavaMailProperties
parameter_list|()
block|{
return|return
name|javaMailProperties
return|;
block|}
DECL|method|setJavaMailProperties (Properties javaMailProperties)
specifier|public
name|void
name|setJavaMailProperties
parameter_list|(
name|Properties
name|javaMailProperties
parameter_list|)
block|{
name|this
operator|.
name|javaMailProperties
operator|=
name|javaMailProperties
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
DECL|method|getPort ()
specifier|public
name|int
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
DECL|method|setPort (int port)
specifier|public
name|void
name|setPort
parameter_list|(
name|int
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
DECL|method|getProtocol ()
specifier|public
name|String
name|getProtocol
parameter_list|()
block|{
return|return
name|protocol
return|;
block|}
DECL|method|setProtocol (String protocol)
specifier|public
name|void
name|setProtocol
parameter_list|(
name|String
name|protocol
parameter_list|)
block|{
name|this
operator|.
name|protocol
operator|=
name|protocol
expr_stmt|;
block|}
DECL|method|getSession ()
specifier|public
name|Session
name|getSession
parameter_list|()
block|{
return|return
name|session
return|;
block|}
DECL|method|setSession (Session session)
specifier|public
name|void
name|setSession
parameter_list|(
name|Session
name|session
parameter_list|)
block|{
name|this
operator|.
name|session
operator|=
name|session
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
DECL|method|getDestination ()
specifier|public
name|String
name|getDestination
parameter_list|()
block|{
return|return
name|destination
return|;
block|}
DECL|method|setDestination (String destination)
specifier|public
name|void
name|setDestination
parameter_list|(
name|String
name|destination
parameter_list|)
block|{
name|this
operator|.
name|destination
operator|=
name|destination
expr_stmt|;
block|}
DECL|method|getFrom ()
specifier|public
name|String
name|getFrom
parameter_list|()
block|{
return|return
name|from
return|;
block|}
DECL|method|setFrom (String from)
specifier|public
name|void
name|setFrom
parameter_list|(
name|String
name|from
parameter_list|)
block|{
name|this
operator|.
name|from
operator|=
name|from
expr_stmt|;
block|}
block|}
end_class

end_unit

