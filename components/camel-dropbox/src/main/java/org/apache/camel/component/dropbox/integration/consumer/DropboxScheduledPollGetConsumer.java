begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.dropbox.integration.consumer
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
operator|.
name|integration
operator|.
name|consumer
package|;
end_package

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
name|component
operator|.
name|dropbox
operator|.
name|DropboxConfiguration
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
name|DropboxEndpoint
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
name|core
operator|.
name|DropboxAPIFacade
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
name|dto
operator|.
name|DropboxFileDownloadResult
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
name|DropboxResultHeader
import|;
end_import

begin_class
DECL|class|DropboxScheduledPollGetConsumer
specifier|public
class|class
name|DropboxScheduledPollGetConsumer
extends|extends
name|DropboxScheduledPollConsumer
block|{
DECL|method|DropboxScheduledPollGetConsumer (DropboxEndpoint endpoint, Processor processor, DropboxConfiguration configuration)
specifier|public
name|DropboxScheduledPollGetConsumer
parameter_list|(
name|DropboxEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|DropboxConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
block|}
comment|/**      * Poll from a dropbox remote path and put the result in the message exchange      * @return number of messages polled      * @throws Exception      */
annotation|@
name|Override
DECL|method|poll ()
specifier|protected
name|int
name|poll
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|DropboxFileDownloadResult
name|result
init|=
operator|new
name|DropboxAPIFacade
argument_list|(
name|configuration
operator|.
name|getClient
argument_list|()
argument_list|,
name|exchange
argument_list|)
operator|.
name|get
argument_list|(
name|configuration
operator|.
name|getRemotePath
argument_list|()
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|result
operator|.
name|getEntries
argument_list|()
decl_stmt|;
if|if
condition|(
name|map
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|DropboxResultHeader
operator|.
name|DOWNLOADED_FILE
operator|.
name|name
argument_list|()
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|StringBuilder
name|pathsExtracted
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|pathsExtracted
operator|.
name|append
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|DropboxResultHeader
operator|.
name|DOWNLOADED_FILES
operator|.
name|name
argument_list|()
argument_list|,
name|pathsExtracted
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Downloaded: {}"
argument_list|,
name|result
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
comment|// send message to next processor in the route
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
return|return
literal|1
return|;
comment|// number of messages polled
block|}
finally|finally
block|{
comment|// log exception if an exception occurred and was not handled
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Error processing exchange"
argument_list|,
name|exchange
argument_list|,
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

