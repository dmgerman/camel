begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.azure.blob
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|azure
operator|.
name|blob
package|;
end_package

begin_import
import|import
name|com
operator|.
name|microsoft
operator|.
name|azure
operator|.
name|storage
operator|.
name|StorageException
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
name|NoFactoryAvailableException
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
name|impl
operator|.
name|ScheduledPollConsumer
import|;
end_import

begin_comment
comment|/**  * A Consumer of the blob content from the Azure Blob Service  */
end_comment

begin_comment
comment|// Extending DefaultConsumer is simpler if the blob must exist before this consumer is started,
end_comment

begin_comment
comment|// polling makes it easier to get the consumer working if no blob exists yet.
end_comment

begin_class
DECL|class|BlobServiceConsumer
specifier|public
class|class
name|BlobServiceConsumer
extends|extends
name|ScheduledPollConsumer
block|{
DECL|method|BlobServiceConsumer (BlobServiceEndpoint endpoint, Processor processor)
specifier|public
name|BlobServiceConsumer
parameter_list|(
name|BlobServiceEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
throws|throws
name|NoFactoryAvailableException
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
block|}
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
name|super
operator|.
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|()
decl_stmt|;
try|try
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Getting the blob content"
argument_list|)
expr_stmt|;
name|getBlob
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|super
operator|.
name|getAsyncProcessor
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
block|}
catch|catch
parameter_list|(
name|StorageException
name|ex
parameter_list|)
block|{
if|if
condition|(
literal|404
operator|==
name|ex
operator|.
name|getHttpStatusCode
argument_list|()
condition|)
block|{
return|return
literal|0
return|;
block|}
else|else
block|{
throw|throw
name|ex
throw|;
block|}
block|}
block|}
DECL|method|getBlob (Exchange exchange)
specifier|private
name|void
name|getBlob
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|BlobServiceUtil
operator|.
name|getBlob
argument_list|(
name|exchange
argument_list|,
name|getConfiguration
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|getConfiguration ()
specifier|protected
name|BlobServiceConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|BlobServiceEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|BlobServiceEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
block|}
end_class

end_unit

