begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.dropbox
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|dropbox
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
name|Consumer
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
name|Processor
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
name|Producer
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
name|dropbox
operator|.
name|integration
operator|.
name|consumer
operator|.
name|DropboxScheduledPollConsumer
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
name|dropbox
operator|.
name|integration
operator|.
name|consumer
operator|.
name|DropboxScheduledPollGetConsumer
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
name|dropbox
operator|.
name|integration
operator|.
name|consumer
operator|.
name|DropboxScheduledPollSearchConsumer
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
name|dropbox
operator|.
name|integration
operator|.
name|producer
operator|.
name|DropboxDelProducer
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
name|dropbox
operator|.
name|integration
operator|.
name|producer
operator|.
name|DropboxGetProducer
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
name|dropbox
operator|.
name|integration
operator|.
name|producer
operator|.
name|DropboxMoveProducer
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
name|dropbox
operator|.
name|integration
operator|.
name|producer
operator|.
name|DropboxPutProducer
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
name|dropbox
operator|.
name|integration
operator|.
name|producer
operator|.
name|DropboxSearchProducer
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
name|dropbox
operator|.
name|util
operator|.
name|DropboxConstants
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
name|dropbox
operator|.
name|util
operator|.
name|DropboxException
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
name|dropbox
operator|.
name|util
operator|.
name|DropboxOperation
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
name|DefaultEndpoint
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
name|UriEndpoint
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
comment|/**  * For uploading, downloading and managing files, folders, groups, collaborations, etc on dropbox DOT com.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.14.0"
argument_list|,
name|scheme
operator|=
literal|"dropbox"
argument_list|,
name|title
operator|=
literal|"Dropbox"
argument_list|,
name|syntax
operator|=
literal|"dropbox:operation"
argument_list|,
name|consumerClass
operator|=
name|DropboxScheduledPollConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"api,file"
argument_list|)
DECL|class|DropboxEndpoint
specifier|public
class|class
name|DropboxEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|DropboxEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|UriParam
DECL|field|configuration
specifier|private
name|DropboxConfiguration
name|configuration
decl_stmt|;
DECL|method|DropboxEndpoint ()
specifier|public
name|DropboxEndpoint
parameter_list|()
block|{     }
DECL|method|DropboxEndpoint (String uri, DropboxComponent component, DropboxConfiguration configuration)
specifier|public
name|DropboxEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|DropboxComponent
name|component
parameter_list|,
name|DropboxConfiguration
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
DECL|method|DropboxEndpoint (String endpointUri)
specifier|public
name|DropboxEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|)
expr_stmt|;
block|}
comment|/**      * Create one of the camel producer available based on the configuration      * @return the camel producer      * @throws Exception      */
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Resolve producer dropbox endpoint {{}}"
argument_list|,
name|configuration
operator|.
name|getOperation
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Resolve producer dropbox attached client: {}"
argument_list|,
name|configuration
operator|.
name|getClient
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|configuration
operator|.
name|getOperation
argument_list|()
operator|==
name|DropboxOperation
operator|.
name|put
condition|)
block|{
return|return
operator|new
name|DropboxPutProducer
argument_list|(
name|this
argument_list|,
name|configuration
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|this
operator|.
name|configuration
operator|.
name|getOperation
argument_list|()
operator|==
name|DropboxOperation
operator|.
name|search
condition|)
block|{
return|return
operator|new
name|DropboxSearchProducer
argument_list|(
name|this
argument_list|,
name|configuration
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|this
operator|.
name|configuration
operator|.
name|getOperation
argument_list|()
operator|==
name|DropboxOperation
operator|.
name|del
condition|)
block|{
return|return
operator|new
name|DropboxDelProducer
argument_list|(
name|this
argument_list|,
name|configuration
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|this
operator|.
name|configuration
operator|.
name|getOperation
argument_list|()
operator|==
name|DropboxOperation
operator|.
name|get
condition|)
block|{
return|return
operator|new
name|DropboxGetProducer
argument_list|(
name|this
argument_list|,
name|configuration
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|this
operator|.
name|configuration
operator|.
name|getOperation
argument_list|()
operator|==
name|DropboxOperation
operator|.
name|move
condition|)
block|{
return|return
operator|new
name|DropboxMoveProducer
argument_list|(
name|this
argument_list|,
name|configuration
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|DropboxException
argument_list|(
literal|"Operation specified is not valid for producer!"
argument_list|)
throw|;
block|}
block|}
comment|/**      * Create one of the camel consumer available based on the configuration      * @param processor  the given processor      * @return the camel consumer      * @throws Exception      */
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Resolve consumer dropbox endpoint {{}}"
argument_list|,
name|configuration
operator|.
name|getOperation
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Resolve consumer dropbox attached client: {}"
argument_list|,
name|configuration
operator|.
name|getClient
argument_list|()
argument_list|)
expr_stmt|;
name|DropboxScheduledPollConsumer
name|consumer
decl_stmt|;
if|if
condition|(
name|this
operator|.
name|configuration
operator|.
name|getOperation
argument_list|()
operator|==
name|DropboxOperation
operator|.
name|search
condition|)
block|{
name|consumer
operator|=
operator|new
name|DropboxScheduledPollSearchConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|setDelay
argument_list|(
name|DropboxConstants
operator|.
name|POLL_CONSUMER_DELAY
argument_list|)
expr_stmt|;
return|return
name|consumer
return|;
block|}
elseif|else
if|if
condition|(
name|this
operator|.
name|configuration
operator|.
name|getOperation
argument_list|()
operator|==
name|DropboxOperation
operator|.
name|get
condition|)
block|{
name|consumer
operator|=
operator|new
name|DropboxScheduledPollGetConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|setDelay
argument_list|(
name|DropboxConstants
operator|.
name|POLL_CONSUMER_DELAY
argument_list|)
expr_stmt|;
return|return
name|consumer
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|DropboxException
argument_list|(
literal|"Operation specified is not valid for consumer!"
argument_list|)
throw|;
block|}
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
block|}
end_class

end_unit

