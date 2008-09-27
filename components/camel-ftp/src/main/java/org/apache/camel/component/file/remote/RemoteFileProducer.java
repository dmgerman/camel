begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file.remote
package|package
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
name|Expression
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
name|Message
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
name|FileComponent
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
name|impl
operator|.
name|DefaultProducer
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
name|language
operator|.
name|simple
operator|.
name|FileLanguage
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_class
DECL|class|RemoteFileProducer
specifier|public
specifier|abstract
class|class
name|RemoteFileProducer
parameter_list|<
name|T
extends|extends
name|RemoteFileExchange
parameter_list|>
extends|extends
name|DefaultProducer
argument_list|<
name|T
argument_list|>
block|{
DECL|field|log
specifier|protected
specifier|final
specifier|transient
name|Log
name|log
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|protected
name|RemoteFileEndpoint
argument_list|<
name|T
argument_list|>
name|endpoint
decl_stmt|;
DECL|method|RemoteFileProducer (RemoteFileEndpoint<T> endpoint)
specifier|protected
name|RemoteFileProducer
parameter_list|(
name|RemoteFileEndpoint
argument_list|<
name|T
argument_list|>
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
DECL|method|createFileName (Message message, RemoteFileConfiguration fileConfig)
specifier|protected
name|String
name|createFileName
parameter_list|(
name|Message
name|message
parameter_list|,
name|RemoteFileConfiguration
name|fileConfig
parameter_list|)
block|{
name|String
name|answer
decl_stmt|;
name|String
name|name
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|FileComponent
operator|.
name|HEADER_FILE_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// expression support
name|Expression
name|expression
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getExpression
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
comment|// the header name can be an expression too, that should override whatever configured on the endpoint
if|if
condition|(
name|name
operator|.
name|indexOf
argument_list|(
literal|"${"
argument_list|)
operator|>
operator|-
literal|1
condition|)
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
name|FileComponent
operator|.
name|HEADER_FILE_NAME
operator|+
literal|" contains a FileLanguage expression: "
operator|+
name|name
argument_list|)
expr_stmt|;
block|}
name|expression
operator|=
name|FileLanguage
operator|.
name|file
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|expression
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Filename evaluated as expression: "
operator|+
name|expression
argument_list|)
expr_stmt|;
block|}
name|Object
name|result
init|=
name|expression
operator|.
name|evaluate
argument_list|(
name|message
operator|.
name|getExchange
argument_list|()
argument_list|)
decl_stmt|;
name|name
operator|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
name|String
name|endpointFile
init|=
name|fileConfig
operator|.
name|getFile
argument_list|()
decl_stmt|;
if|if
condition|(
name|fileConfig
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
comment|// If the path isn't empty, we need to add a trailing / if it isn't already there
name|String
name|baseDir
init|=
literal|""
decl_stmt|;
if|if
condition|(
name|endpointFile
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|baseDir
operator|=
name|endpointFile
operator|+
operator|(
name|endpointFile
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|?
literal|""
else|:
literal|"/"
operator|)
expr_stmt|;
block|}
name|String
name|fileName
init|=
operator|(
name|name
operator|!=
literal|null
operator|)
condition|?
name|name
else|:
name|endpoint
operator|.
name|getGeneratedFileName
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|answer
operator|=
name|baseDir
operator|+
name|fileName
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
name|endpointFile
expr_stmt|;
block|}
comment|// lets store the name we really used in the header, so end-users can retrieve it
name|message
operator|.
name|setHeader
argument_list|(
name|FileComponent
operator|.
name|HEADER_FILE_NAME_PRODUCED
argument_list|,
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|remoteServer ()
specifier|protected
name|String
name|remoteServer
parameter_list|()
block|{
return|return
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|remoteServerInformation
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Starting"
argument_list|)
expr_stmt|;
comment|// do not connect when component starts, just wait until we process as we will
comment|// connect at that time if needed
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Stopping"
argument_list|)
expr_stmt|;
comment|// disconnect when stopping
try|try
block|{
name|disconnect
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore just log a warning
name|log
operator|.
name|warn
argument_list|(
literal|"Exception occured during disconecting from "
operator|+
name|remoteServer
argument_list|()
operator|+
literal|". "
operator|+
name|e
operator|.
name|getClass
argument_list|()
operator|.
name|getCanonicalName
argument_list|()
operator|+
literal|" message: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
DECL|method|connectIfNecessary ()
specifier|protected
specifier|abstract
name|void
name|connectIfNecessary
parameter_list|()
throws|throws
name|Exception
function_decl|;
DECL|method|disconnect ()
specifier|protected
specifier|abstract
name|void
name|disconnect
parameter_list|()
throws|throws
name|Exception
function_decl|;
block|}
end_class

end_unit

