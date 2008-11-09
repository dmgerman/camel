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
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
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
name|Exchange
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
name|ExchangePattern
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
name|impl
operator|.
name|ScheduledPollEndpoint
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
name|UuidGenerator
import|;
end_import

begin_class
DECL|class|RemoteFileEndpoint
specifier|public
specifier|abstract
class|class
name|RemoteFileEndpoint
parameter_list|<
name|T
extends|extends
name|RemoteFileExchange
parameter_list|>
extends|extends
name|ScheduledPollEndpoint
argument_list|<
name|T
argument_list|>
block|{
DECL|field|binding
specifier|private
name|RemoteFileBinding
name|binding
decl_stmt|;
DECL|field|configuration
specifier|private
name|RemoteFileConfiguration
name|configuration
decl_stmt|;
DECL|method|RemoteFileEndpoint (String uri, RemoteFileComponent component, RemoteFileConfiguration configuration)
specifier|public
name|RemoteFileEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|RemoteFileComponent
name|component
parameter_list|,
name|RemoteFileConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|RemoteFileEndpoint (String endpointUri, RemoteFileConfiguration configuration)
specifier|protected
name|RemoteFileEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|RemoteFileConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|RemoteFileEndpoint (String endpointUri)
specifier|protected
name|RemoteFileEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|)
block|{
name|this
argument_list|(
name|endpointUri
argument_list|,
operator|new
name|RemoteFileConfiguration
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|createRemoteFileBinding ()
specifier|protected
name|RemoteFileBinding
name|createRemoteFileBinding
parameter_list|()
block|{
return|return
operator|new
name|RemoteFileBinding
argument_list|()
return|;
block|}
DECL|method|createExchange ()
specifier|public
name|Exchange
name|createExchange
parameter_list|()
block|{
return|return
operator|(
name|T
operator|)
operator|new
name|RemoteFileExchange
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|getExchangePattern
argument_list|()
argument_list|,
name|getBinding
argument_list|()
argument_list|)
return|;
block|}
DECL|method|createExchange (ExchangePattern pattern)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|ExchangePattern
name|pattern
parameter_list|)
block|{
return|return
operator|(
name|T
operator|)
operator|new
name|RemoteFileExchange
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|pattern
argument_list|,
name|getBinding
argument_list|()
argument_list|)
return|;
block|}
DECL|method|createExchange (String fullFileName, String fileName, long fileLength, ByteArrayOutputStream outputStream)
specifier|public
name|T
name|createExchange
parameter_list|(
name|String
name|fullFileName
parameter_list|,
name|String
name|fileName
parameter_list|,
name|long
name|fileLength
parameter_list|,
name|ByteArrayOutputStream
name|outputStream
parameter_list|)
block|{
return|return
operator|(
name|T
operator|)
operator|new
name|RemoteFileExchange
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|getExchangePattern
argument_list|()
argument_list|,
name|getBinding
argument_list|()
argument_list|,
name|getConfiguration
argument_list|()
operator|.
name|getHost
argument_list|()
argument_list|,
name|fullFileName
argument_list|,
name|fileName
argument_list|,
name|fileLength
argument_list|,
name|outputStream
argument_list|)
return|;
block|}
DECL|method|getBinding ()
specifier|public
name|RemoteFileBinding
name|getBinding
parameter_list|()
block|{
if|if
condition|(
name|binding
operator|==
literal|null
condition|)
block|{
name|binding
operator|=
name|createRemoteFileBinding
argument_list|()
expr_stmt|;
block|}
return|return
name|binding
return|;
block|}
DECL|method|setBinding (RemoteFileBinding binding)
specifier|public
name|void
name|setBinding
parameter_list|(
name|RemoteFileBinding
name|binding
parameter_list|)
block|{
name|this
operator|.
name|binding
operator|=
name|binding
expr_stmt|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|RemoteFileConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration (RemoteFileConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|RemoteFileConfiguration
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
comment|/**       * Return the file name that will be auto-generated for the given message if none is provided       */
DECL|method|getGeneratedFileName (Message message)
specifier|public
name|String
name|getGeneratedFileName
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
return|return
name|getFileFriendlyMessageId
argument_list|(
name|message
operator|.
name|getMessageId
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getFileFriendlyMessageId (String id)
specifier|protected
name|String
name|getFileFriendlyMessageId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
return|return
name|UuidGenerator
operator|.
name|generateSanitizedId
argument_list|(
name|id
argument_list|)
return|;
block|}
block|}
end_class

end_unit

