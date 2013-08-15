begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jsch
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jsch
package|;
end_package

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
name|Map
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jcraft
operator|.
name|jsch
operator|.
name|JSch
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
name|CamelContext
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
name|file
operator|.
name|GenericFileEndpoint
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
name|file
operator|.
name|remote
operator|.
name|RemoteFileComponent
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

begin_comment
comment|/**  *  Component providing secure messaging using JSch  */
end_comment

begin_class
DECL|class|JschComponent
specifier|public
class|class
name|JschComponent
extends|extends
name|RemoteFileComponent
argument_list|<
name|ScpFile
argument_list|>
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
name|JschComponent
operator|.
name|class
argument_list|)
decl_stmt|;
static|static
block|{
name|JSch
operator|.
name|setConfig
argument_list|(
literal|"StrictHostKeyChecking"
argument_list|,
literal|"yes"
argument_list|)
expr_stmt|;
name|JSch
operator|.
name|setLogger
argument_list|(
operator|new
name|com
operator|.
name|jcraft
operator|.
name|jsch
operator|.
name|Logger
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|isEnabled
parameter_list|(
name|int
name|level
parameter_list|)
block|{
return|return
name|level
operator|==
name|FATAL
operator|||
name|level
operator|==
name|ERROR
condition|?
name|LOG
operator|.
name|isErrorEnabled
argument_list|()
else|:
name|level
operator|==
name|WARN
condition|?
name|LOG
operator|.
name|isWarnEnabled
argument_list|()
else|:
name|level
operator|==
name|INFO
condition|?
name|LOG
operator|.
name|isInfoEnabled
argument_list|()
else|:
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|log
parameter_list|(
name|int
name|level
parameter_list|,
name|String
name|message
parameter_list|)
block|{
if|if
condition|(
name|level
operator|==
name|FATAL
operator|||
name|level
operator|==
name|ERROR
condition|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"[JSCH] {}"
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|level
operator|==
name|WARN
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"[JSCH] {}"
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|level
operator|==
name|INFO
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"[JSCH] {}"
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"[JSCH] {}"
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|JschComponent ()
specifier|public
name|JschComponent
parameter_list|()
block|{     }
DECL|method|JschComponent (CamelContext context)
specifier|public
name|JschComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|buildFileEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|GenericFileEndpoint
argument_list|<
name|ScpFile
argument_list|>
name|buildFileEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
comment|// TODO: revisit stripping the query part; should not be needed with valid uris
name|int
name|query
init|=
name|uri
operator|.
name|indexOf
argument_list|(
literal|"?"
argument_list|)
decl_stmt|;
return|return
operator|new
name|ScpEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
operator|new
name|ScpConfiguration
argument_list|(
operator|new
name|URI
argument_list|(
name|query
operator|>=
literal|0
condition|?
name|uri
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|query
argument_list|)
else|:
name|uri
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
DECL|method|afterPropertiesSet (GenericFileEndpoint<ScpFile> endpoint)
specifier|protected
name|void
name|afterPropertiesSet
parameter_list|(
name|GenericFileEndpoint
argument_list|<
name|ScpFile
argument_list|>
name|endpoint
parameter_list|)
throws|throws
name|Exception
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|public
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// TODO: close all sessions
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

