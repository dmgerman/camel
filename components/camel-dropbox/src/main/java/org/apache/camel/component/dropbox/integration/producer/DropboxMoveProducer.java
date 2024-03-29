begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.dropbox.integration.producer
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
name|producer
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
name|DropboxMoveResult
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
name|DropboxHelper
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
name|validator
operator|.
name|DropboxConfigurationValidator
import|;
end_import

begin_class
DECL|class|DropboxMoveProducer
specifier|public
class|class
name|DropboxMoveProducer
extends|extends
name|DropboxProducer
block|{
DECL|method|DropboxMoveProducer (DropboxEndpoint endpoint, DropboxConfiguration configuration)
specifier|public
name|DropboxMoveProducer
parameter_list|(
name|DropboxEndpoint
name|endpoint
parameter_list|,
name|DropboxConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|remotePath
init|=
name|DropboxHelper
operator|.
name|getRemotePath
argument_list|(
name|configuration
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|String
name|newRemotePath
init|=
name|DropboxHelper
operator|.
name|getNewRemotePath
argument_list|(
name|configuration
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|DropboxConfigurationValidator
operator|.
name|validateMoveOp
argument_list|(
name|remotePath
argument_list|,
name|newRemotePath
argument_list|)
expr_stmt|;
name|DropboxMoveResult
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
name|move
argument_list|(
name|remotePath
argument_list|,
name|newRemotePath
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|DropboxResultHeader
operator|.
name|MOVED_PATH
operator|.
name|name
argument_list|()
argument_list|,
name|result
operator|.
name|getOldPath
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
name|result
operator|.
name|getNewPath
argument_list|()
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Moved from {} to {}"
argument_list|,
name|remotePath
argument_list|,
name|newRemotePath
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

