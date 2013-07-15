begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty
operator|.
name|http
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|security
operator|.
name|auth
operator|.
name|Subject
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|callback
operator|.
name|Callback
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|callback
operator|.
name|CallbackHandler
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|callback
operator|.
name|NameCallback
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|callback
operator|.
name|PasswordCallback
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|callback
operator|.
name|UnsupportedCallbackException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|login
operator|.
name|LoginException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|spi
operator|.
name|LoginModule
import|;
end_import

begin_class
DECL|class|MyLoginModule
specifier|public
class|class
name|MyLoginModule
implements|implements
name|LoginModule
block|{
DECL|field|subject
specifier|private
name|Subject
name|subject
decl_stmt|;
DECL|field|callbackHandler
specifier|private
name|CallbackHandler
name|callbackHandler
decl_stmt|;
annotation|@
name|Override
DECL|method|initialize (Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options)
specifier|public
name|void
name|initialize
parameter_list|(
name|Subject
name|subject
parameter_list|,
name|CallbackHandler
name|callbackHandler
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|sharedState
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|options
parameter_list|)
block|{
name|this
operator|.
name|subject
operator|=
name|subject
expr_stmt|;
name|this
operator|.
name|callbackHandler
operator|=
name|callbackHandler
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|login ()
specifier|public
name|boolean
name|login
parameter_list|()
throws|throws
name|LoginException
block|{
comment|// get username and password
name|Callback
index|[]
name|callbacks
init|=
operator|new
name|Callback
index|[
literal|2
index|]
decl_stmt|;
name|callbacks
index|[
literal|0
index|]
operator|=
operator|new
name|NameCallback
argument_list|(
literal|"username"
argument_list|)
expr_stmt|;
name|callbacks
index|[
literal|1
index|]
operator|=
operator|new
name|PasswordCallback
argument_list|(
literal|"password"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
try|try
block|{
name|callbackHandler
operator|.
name|handle
argument_list|(
name|callbacks
argument_list|)
expr_stmt|;
name|String
name|username
init|=
operator|(
operator|(
name|NameCallback
operator|)
name|callbacks
index|[
literal|0
index|]
operator|)
operator|.
name|getName
argument_list|()
decl_stmt|;
name|char
index|[]
name|tmpPassword
init|=
operator|(
operator|(
name|PasswordCallback
operator|)
name|callbacks
index|[
literal|1
index|]
operator|)
operator|.
name|getPassword
argument_list|()
decl_stmt|;
name|String
name|password
init|=
operator|new
name|String
argument_list|(
name|tmpPassword
argument_list|)
decl_stmt|;
operator|(
operator|(
name|PasswordCallback
operator|)
name|callbacks
index|[
literal|1
index|]
operator|)
operator|.
name|clearPassword
argument_list|()
expr_stmt|;
comment|// only allow login if password is secret
comment|// as this is just for testing purpose
if|if
condition|(
operator|!
literal|"secret"
operator|.
name|equals
argument_list|(
name|password
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|LoginException
argument_list|(
literal|"Login denied"
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|ioe
parameter_list|)
block|{
name|LoginException
name|le
init|=
operator|new
name|LoginException
argument_list|(
name|ioe
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|le
operator|.
name|initCause
argument_list|(
name|ioe
argument_list|)
expr_stmt|;
throw|throw
name|le
throw|;
block|}
catch|catch
parameter_list|(
name|UnsupportedCallbackException
name|uce
parameter_list|)
block|{
name|LoginException
name|le
init|=
operator|new
name|LoginException
argument_list|(
literal|"Error: "
operator|+
name|uce
operator|.
name|getCallback
argument_list|()
operator|.
name|toString
argument_list|()
operator|+
literal|" not available to gather authentication information from the user"
argument_list|)
decl_stmt|;
name|le
operator|.
name|initCause
argument_list|(
name|uce
argument_list|)
expr_stmt|;
throw|throw
name|le
throw|;
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|commit ()
specifier|public
name|boolean
name|commit
parameter_list|()
throws|throws
name|LoginException
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|abort ()
specifier|public
name|boolean
name|abort
parameter_list|()
throws|throws
name|LoginException
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|logout ()
specifier|public
name|boolean
name|logout
parameter_list|()
throws|throws
name|LoginException
block|{
name|subject
operator|=
literal|null
expr_stmt|;
name|callbackHandler
operator|=
literal|null
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

