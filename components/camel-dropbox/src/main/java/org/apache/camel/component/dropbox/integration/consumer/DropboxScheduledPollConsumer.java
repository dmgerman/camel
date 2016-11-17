begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*   Licensed to the Apache Software Foundation (ASF) under one or more   contributor license agreements.  See the NOTICE file distributed with   this work for additional information regarding copyright ownership.   The ASF licenses this file to You under the Apache License, Version 2.0   (the "License"); you may not use this file except in compliance with   the License.  You may obtain a copy of the License at         http://www.apache.org/licenses/LICENSE-2.0    Unless required by applicable law or agreed to in writing, software   distributed under the License is distributed on an "AS IS" BASIS,   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.   See the License for the specific language governing permissions and   limitations under the License.  */
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
name|impl
operator|.
name|ScheduledPollConsumer
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
DECL|class|DropboxScheduledPollConsumer
specifier|public
specifier|abstract
class|class
name|DropboxScheduledPollConsumer
extends|extends
name|ScheduledPollConsumer
block|{
DECL|field|LOG
specifier|protected
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
name|DropboxScheduledPollConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|protected
specifier|final
name|DropboxEndpoint
name|endpoint
decl_stmt|;
DECL|field|configuration
specifier|protected
specifier|final
name|DropboxConfiguration
name|configuration
decl_stmt|;
DECL|method|DropboxScheduledPollConsumer (DropboxEndpoint endpoint, Processor processor, DropboxConfiguration configuration)
specifier|public
name|DropboxScheduledPollConsumer
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
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|poll ()
specifier|protected
specifier|abstract
name|int
name|poll
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**      * Lifecycle method invoked when the consumer has created.      * Internally create or reuse a connection to the low level dropbox client      * @throws Exception      */
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
if|if
condition|(
name|configuration
operator|.
name|getClient
argument_list|()
operator|==
literal|null
condition|)
block|{
comment|//create dropbox client
name|configuration
operator|.
name|createClient
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"consumer dropbox client created"
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
block|}
comment|/**      * Lifecycle method invoked when the consumer has destroyed.      * Erase the reference to the dropbox low level client      * @throws Exception      */
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
if|if
condition|(
name|configuration
operator|.
name|getClient
argument_list|()
operator|==
literal|null
condition|)
block|{
name|configuration
operator|.
name|setClient
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"consumer dropbox client deleted"
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

