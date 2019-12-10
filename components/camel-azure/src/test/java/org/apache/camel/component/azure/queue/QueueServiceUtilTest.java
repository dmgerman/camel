begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.azure.queue
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
name|queue
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
name|com
operator|.
name|microsoft
operator|.
name|azure
operator|.
name|storage
operator|.
name|StorageCredentials
import|;
end_import

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
name|StorageCredentialsAccountAndKey
import|;
end_import

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
name|core
operator|.
name|Base64
import|;
end_import

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
name|queue
operator|.
name|CloudQueue
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|QueueServiceUtilTest
specifier|public
class|class
name|QueueServiceUtilTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testPrepareUri ()
specifier|public
name|void
name|testPrepareUri
parameter_list|()
throws|throws
name|Exception
block|{
name|registerCredentials
argument_list|()
expr_stmt|;
name|QueueServiceEndpoint
name|endpoint
init|=
operator|(
name|QueueServiceEndpoint
operator|)
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"azure-queue://camelazure/testqueue?credentials=#creds"
argument_list|)
decl_stmt|;
name|URI
name|uri
init|=
name|QueueServiceUtil
operator|.
name|prepareStorageQueueUri
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"https://camelazure.queue.core.windows.net/testqueue"
argument_list|,
name|uri
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetConfiguredClient ()
specifier|public
name|void
name|testGetConfiguredClient
parameter_list|()
throws|throws
name|Exception
block|{
name|CloudQueue
name|client
init|=
operator|new
name|CloudQueue
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"https://camelazure.queue.core.windows.net/testqueue"
argument_list|)
argument_list|,
name|newAccountKeyCredentials
argument_list|()
argument_list|)
decl_stmt|;
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|bind
argument_list|(
literal|"azureQueueClient"
argument_list|,
name|client
argument_list|)
expr_stmt|;
name|QueueServiceEndpoint
name|endpoint
init|=
operator|(
name|QueueServiceEndpoint
operator|)
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"azure-queue://camelazure/testqueue?azureQueueClient=#azureQueueClient"
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|client
argument_list|,
name|QueueServiceUtil
operator|.
name|getConfiguredClient
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetConfiguredClientUriMismatch ()
specifier|public
name|void
name|testGetConfiguredClientUriMismatch
parameter_list|()
throws|throws
name|Exception
block|{
name|CloudQueue
name|client
init|=
operator|new
name|CloudQueue
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"https://camelazure.queue.core.windows.net/testqueue"
argument_list|)
argument_list|,
name|newAccountKeyCredentials
argument_list|()
argument_list|)
decl_stmt|;
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|bind
argument_list|(
literal|"azureQueueClient"
argument_list|,
name|client
argument_list|)
expr_stmt|;
name|QueueServiceEndpoint
name|endpoint
init|=
operator|(
name|QueueServiceEndpoint
operator|)
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"azure-queue://camelazure/testqueue2?azureQueueClient=#azureQueueClient"
argument_list|)
decl_stmt|;
try|try
block|{
name|QueueServiceUtil
operator|.
name|getConfiguredClient
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|ex
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Invalid Client URI"
argument_list|,
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|registerCredentials ()
specifier|private
name|void
name|registerCredentials
parameter_list|()
block|{
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|bind
argument_list|(
literal|"creds"
argument_list|,
name|newAccountKeyCredentials
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|newAccountKeyCredentials ()
specifier|private
name|StorageCredentials
name|newAccountKeyCredentials
parameter_list|()
block|{
return|return
operator|new
name|StorageCredentialsAccountAndKey
argument_list|(
literal|"camelazure"
argument_list|,
name|Base64
operator|.
name|encode
argument_list|(
literal|"key"
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

