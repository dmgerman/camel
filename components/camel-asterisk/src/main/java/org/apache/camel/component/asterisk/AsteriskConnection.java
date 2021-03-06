begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.asterisk
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|asterisk
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
name|org
operator|.
name|asteriskjava
operator|.
name|manager
operator|.
name|AuthenticationFailedException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|asteriskjava
operator|.
name|manager
operator|.
name|ManagerConnection
import|;
end_import

begin_import
import|import
name|org
operator|.
name|asteriskjava
operator|.
name|manager
operator|.
name|ManagerConnectionFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|asteriskjava
operator|.
name|manager
operator|.
name|ManagerConnectionState
import|;
end_import

begin_import
import|import
name|org
operator|.
name|asteriskjava
operator|.
name|manager
operator|.
name|ManagerEventListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|asteriskjava
operator|.
name|manager
operator|.
name|TimeoutException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|asteriskjava
operator|.
name|manager
operator|.
name|action
operator|.
name|ManagerAction
import|;
end_import

begin_import
import|import
name|org
operator|.
name|asteriskjava
operator|.
name|manager
operator|.
name|response
operator|.
name|ManagerResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
DECL|class|AsteriskConnection
specifier|public
specifier|final
class|class
name|AsteriskConnection
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|AsteriskConnection
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|host
specifier|private
specifier|final
name|String
name|host
decl_stmt|;
DECL|field|username
specifier|private
specifier|final
name|String
name|username
decl_stmt|;
DECL|field|password
specifier|private
specifier|final
name|String
name|password
decl_stmt|;
DECL|field|managerConnection
specifier|private
name|ManagerConnection
name|managerConnection
decl_stmt|;
DECL|method|AsteriskConnection (String host, String username, String password)
specifier|public
name|AsteriskConnection
parameter_list|(
name|String
name|host
parameter_list|,
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|)
block|{
name|this
operator|.
name|host
operator|=
name|host
expr_stmt|;
name|this
operator|.
name|username
operator|=
name|username
expr_stmt|;
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
block|}
DECL|method|connect ()
specifier|public
name|void
name|connect
parameter_list|()
block|{
if|if
condition|(
name|managerConnection
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"asterisk connection attempt to {} username: {}"
argument_list|,
name|host
argument_list|,
name|username
argument_list|)
expr_stmt|;
name|ManagerConnectionFactory
name|factory
init|=
operator|new
name|ManagerConnectionFactory
argument_list|(
name|host
argument_list|,
name|username
argument_list|,
name|password
argument_list|)
decl_stmt|;
name|managerConnection
operator|=
name|factory
operator|.
name|createManagerConnection
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"asterisk connection established!"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|login ()
specifier|public
name|void
name|login
parameter_list|()
throws|throws
name|IllegalStateException
throws|,
name|IOException
throws|,
name|AuthenticationFailedException
throws|,
name|TimeoutException
throws|,
name|CamelAsteriskException
block|{
comment|// Lazy connect if not done before
name|connect
argument_list|()
expr_stmt|;
if|if
condition|(
name|managerConnection
operator|!=
literal|null
operator|&&
operator|(
name|managerConnection
operator|.
name|getState
argument_list|()
operator|==
name|ManagerConnectionState
operator|.
name|DISCONNECTED
operator|||
name|managerConnection
operator|.
name|getState
argument_list|()
operator|==
name|ManagerConnectionState
operator|.
name|INITIAL
operator|)
condition|)
block|{
name|managerConnection
operator|.
name|login
argument_list|(
literal|"on"
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"asterisk login done!"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|CamelAsteriskException
argument_list|(
literal|"Login operation, managerConnection is empty!"
argument_list|)
throw|;
block|}
block|}
DECL|method|logoff ()
specifier|public
name|void
name|logoff
parameter_list|()
throws|throws
name|CamelAsteriskException
block|{
if|if
condition|(
name|managerConnection
operator|!=
literal|null
operator|&&
name|managerConnection
operator|.
name|getState
argument_list|()
operator|==
name|ManagerConnectionState
operator|.
name|CONNECTED
condition|)
block|{
name|managerConnection
operator|.
name|logoff
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"asterisk logoff done!"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|CamelAsteriskException
argument_list|(
literal|"Logoff operation, managerConnection is empty!"
argument_list|)
throw|;
block|}
block|}
DECL|method|addListener (ManagerEventListener listener)
specifier|public
name|void
name|addListener
parameter_list|(
name|ManagerEventListener
name|listener
parameter_list|)
throws|throws
name|CamelAsteriskException
block|{
if|if
condition|(
name|managerConnection
operator|!=
literal|null
condition|)
block|{
name|managerConnection
operator|.
name|addEventListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"asterisk added listener {}"
argument_list|,
name|listener
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|CamelAsteriskException
argument_list|(
literal|"Add listener operation, managerConnection is empty!"
argument_list|)
throw|;
block|}
block|}
DECL|method|removeListener (ManagerEventListener listener)
specifier|public
name|void
name|removeListener
parameter_list|(
name|ManagerEventListener
name|listener
parameter_list|)
throws|throws
name|CamelAsteriskException
block|{
if|if
condition|(
name|managerConnection
operator|!=
literal|null
condition|)
block|{
name|managerConnection
operator|.
name|removeEventListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"asterisk removed listener {}"
argument_list|,
name|listener
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|CamelAsteriskException
argument_list|(
literal|"Add listener operation, managerConnection is empty!"
argument_list|)
throw|;
block|}
block|}
DECL|method|sendAction (ManagerAction action)
specifier|public
name|ManagerResponse
name|sendAction
parameter_list|(
name|ManagerAction
name|action
parameter_list|)
throws|throws
name|IllegalArgumentException
throws|,
name|IllegalStateException
throws|,
name|IOException
throws|,
name|TimeoutException
block|{
name|ManagerResponse
name|response
init|=
name|managerConnection
operator|.
name|sendAction
argument_list|(
name|action
argument_list|)
decl_stmt|;
return|return
name|response
return|;
block|}
block|}
end_class

end_unit

