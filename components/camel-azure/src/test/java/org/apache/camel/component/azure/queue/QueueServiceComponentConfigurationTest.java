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
name|Endpoint
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
DECL|class|QueueServiceComponentConfigurationTest
specifier|public
class|class
name|QueueServiceComponentConfigurationTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testCreateEndpointWithMinConfigForClientOnly ()
specifier|public
name|void
name|testCreateEndpointWithMinConfigForClientOnly
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
literal|"https://camelazure.queue.core.windows.net/testqueue/messages"
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
name|QueueServiceComponent
name|component
init|=
operator|new
name|QueueServiceComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|QueueServiceEndpoint
name|endpoint
init|=
operator|(
name|QueueServiceEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"azure-queue://camelazure/testqueue?azureQueueClient=#azureQueueClient"
argument_list|)
decl_stmt|;
name|doTestCreateEndpointWithMinConfig
argument_list|(
name|endpoint
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCreateEndpointWithMinConfigForCredsOnly ()
specifier|public
name|void
name|testCreateEndpointWithMinConfigForCredsOnly
parameter_list|()
throws|throws
name|Exception
block|{
name|registerCredentials
argument_list|()
expr_stmt|;
name|QueueServiceComponent
name|component
init|=
operator|new
name|QueueServiceComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|QueueServiceEndpoint
name|endpoint
init|=
operator|(
name|QueueServiceEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"azure-queue://camelazure/testqueue?credentials=#creds"
argument_list|)
decl_stmt|;
name|doTestCreateEndpointWithMinConfig
argument_list|(
name|endpoint
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCreateEndpointWithMaxConfig ()
specifier|public
name|void
name|testCreateEndpointWithMaxConfig
parameter_list|()
throws|throws
name|Exception
block|{
name|registerCredentials
argument_list|()
expr_stmt|;
name|QueueServiceComponent
name|component
init|=
operator|new
name|QueueServiceComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|QueueServiceEndpoint
name|endpoint
init|=
operator|(
name|QueueServiceEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"azure-queue://camelazure/testqueue?credentials=#creds"
operator|+
literal|"&operation=addMessage&queuePrefix=prefix&messageTimeToLive=100&messageVisibilityDelay=10"
argument_list|)
decl_stmt|;
name|doTestCreateEndpointWithMaxConfig
argument_list|(
name|endpoint
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|doTestCreateEndpointWithMinConfig (QueueServiceEndpoint endpoint, boolean clientExpected)
specifier|private
name|void
name|doTestCreateEndpointWithMinConfig
parameter_list|(
name|QueueServiceEndpoint
name|endpoint
parameter_list|,
name|boolean
name|clientExpected
parameter_list|)
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"camelazure"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getAccountName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"testqueue"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getQueueName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|clientExpected
condition|)
block|{
name|assertNotNull
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getAzureQueueClient
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getCredentials
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getAzureQueueClient
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getCredentials
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|QueueServiceOperations
operator|.
name|listQueues
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getOperation
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getQueuePrefix
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getMessageTimeToLive
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getMessageVisibilityDelay
argument_list|()
argument_list|)
expr_stmt|;
name|createConsumer
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
DECL|method|doTestCreateEndpointWithMaxConfig (QueueServiceEndpoint endpoint, boolean clientExpected)
specifier|private
name|void
name|doTestCreateEndpointWithMaxConfig
parameter_list|(
name|QueueServiceEndpoint
name|endpoint
parameter_list|,
name|boolean
name|clientExpected
parameter_list|)
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"camelazure"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getAccountName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"testqueue"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getQueueName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|clientExpected
condition|)
block|{
name|assertNotNull
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getAzureQueueClient
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getCredentials
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getAzureQueueClient
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getCredentials
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|QueueServiceOperations
operator|.
name|addMessage
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getOperation
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"prefix"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getQueuePrefix
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|100
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getMessageTimeToLive
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getMessageVisibilityDelay
argument_list|()
argument_list|)
expr_stmt|;
name|createConsumer
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNoCredentials ()
specifier|public
name|void
name|testNoCredentials
parameter_list|()
throws|throws
name|Exception
block|{
name|QueueServiceComponent
name|component
init|=
operator|new
name|QueueServiceComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
try|try
block|{
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"azure-queue://camelazure/testqueue"
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
literal|"Credentials must be specified."
argument_list|,
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testTooManyPathSegments ()
specifier|public
name|void
name|testTooManyPathSegments
parameter_list|()
throws|throws
name|Exception
block|{
name|QueueServiceComponent
name|component
init|=
operator|new
name|QueueServiceComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
try|try
block|{
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"azure-queue://camelazure/testqueue/1"
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
literal|"Only the account and queue names must be specified."
argument_list|,
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testTooFewPathSegments ()
specifier|public
name|void
name|testTooFewPathSegments
parameter_list|()
throws|throws
name|Exception
block|{
name|QueueServiceComponent
name|component
init|=
operator|new
name|QueueServiceComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
try|try
block|{
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"azure-queue://camelazure?operation=addMessage"
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
literal|"The queue name must be specified."
argument_list|,
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createConsumer (Endpoint endpoint)
specifier|private
specifier|static
name|void
name|createConsumer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
throws|throws
name|Exception
block|{
name|endpoint
operator|.
name|createConsumer
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
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
comment|// noop
block|}
block|}
argument_list|)
expr_stmt|;
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

