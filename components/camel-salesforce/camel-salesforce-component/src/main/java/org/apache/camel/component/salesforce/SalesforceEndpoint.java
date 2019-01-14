begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|salesforce
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
name|salesforce
operator|.
name|internal
operator|.
name|OperationName
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
name|salesforce
operator|.
name|internal
operator|.
name|streaming
operator|.
name|SubscriptionHelper
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
name|support
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
name|impl
operator|.
name|SynchronousDelegateProducer
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
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|UriPath
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|client
operator|.
name|HttpClient
import|;
end_import

begin_comment
comment|/**  * The salesforce component is used for integrating Camel with the massive Salesforce API.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.12.0"
argument_list|,
name|scheme
operator|=
literal|"salesforce"
argument_list|,
name|title
operator|=
literal|"Salesforce"
argument_list|,
name|syntax
operator|=
literal|"salesforce:operationName:topicName"
argument_list|,
name|label
operator|=
literal|"api,cloud,crm"
argument_list|)
DECL|class|SalesforceEndpoint
specifier|public
class|class
name|SalesforceEndpoint
extends|extends
name|DefaultEndpoint
block|{
annotation|@
name|UriPath
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|description
operator|=
literal|"The operation to use"
argument_list|,
name|enums
operator|=
literal|"getVersions,getResources,"
operator|+
literal|"getGlobalObjects,getBasicInfo,getDescription,getSObject,createSObject,updateSObject,deleteSObject,"
operator|+
literal|"getSObjectWithId,upsertSObject,deleteSObjectWithId,getBlobField,query,queryMore,queryAll,search,apexCall,"
operator|+
literal|"recent,createJob,getJob,closeJob,abortJob,createBatch,getBatch,getAllBatches,getRequest,getResults,"
operator|+
literal|"createBatchQuery,getQueryResultIds,getQueryResult,getRecentReports,getReportDescription,executeSyncReport,"
operator|+
literal|"executeAsyncReport,getReportInstances,getReportResults,limits,approval,approvals,composite-tree,"
operator|+
literal|"composite-batch,composite"
argument_list|)
DECL|field|operationName
specifier|private
specifier|final
name|OperationName
name|operationName
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|description
operator|=
literal|"The name of the topic to use"
argument_list|)
DECL|field|topicName
specifier|private
specifier|final
name|String
name|topicName
decl_stmt|;
annotation|@
name|UriParam
DECL|field|config
specifier|private
specifier|final
name|SalesforceEndpointConfig
name|config
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|description
operator|=
literal|"The replayId value to use when subscribing"
argument_list|)
DECL|field|replayId
specifier|private
name|Long
name|replayId
decl_stmt|;
DECL|method|SalesforceEndpoint (String uri, SalesforceComponent salesforceComponent, SalesforceEndpointConfig config, OperationName operationName, String topicName)
specifier|public
name|SalesforceEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|SalesforceComponent
name|salesforceComponent
parameter_list|,
name|SalesforceEndpointConfig
name|config
parameter_list|,
name|OperationName
name|operationName
parameter_list|,
name|String
name|topicName
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|salesforceComponent
argument_list|)
expr_stmt|;
name|this
operator|.
name|config
operator|=
name|config
expr_stmt|;
name|this
operator|.
name|operationName
operator|=
name|operationName
expr_stmt|;
name|this
operator|.
name|topicName
operator|=
name|topicName
expr_stmt|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
comment|// producer requires an operation, topicName must be the invalid operation name
if|if
condition|(
name|operationName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Invalid Operation %s"
argument_list|,
name|topicName
argument_list|)
argument_list|)
throw|;
block|}
name|SalesforceProducer
name|producer
init|=
operator|new
name|SalesforceProducer
argument_list|(
name|this
argument_list|)
decl_stmt|;
if|if
condition|(
name|isSynchronous
argument_list|()
condition|)
block|{
return|return
operator|new
name|SynchronousDelegateProducer
argument_list|(
name|producer
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|producer
return|;
block|}
block|}
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
comment|// consumer requires a topicName, operation name must be the invalid topic name
if|if
condition|(
name|topicName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Invalid topic name %s, matches a producer operation name"
argument_list|,
name|operationName
operator|.
name|value
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
specifier|final
name|SubscriptionHelper
name|subscriptionHelper
init|=
name|getComponent
argument_list|()
operator|.
name|getSubscriptionHelper
argument_list|()
decl_stmt|;
specifier|final
name|SalesforceConsumer
name|consumer
init|=
operator|new
name|SalesforceConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|,
name|subscriptionHelper
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
return|return
name|consumer
return|;
block|}
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|SalesforceComponent
name|getComponent
parameter_list|()
block|{
return|return
operator|(
name|SalesforceComponent
operator|)
name|super
operator|.
name|getComponent
argument_list|()
return|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
comment|// re-use endpoint instance across multiple threads
comment|// the description of this method is a little confusing
return|return
literal|true
return|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|SalesforceEndpointConfig
name|getConfiguration
parameter_list|()
block|{
return|return
name|config
return|;
block|}
DECL|method|getOperationName ()
specifier|public
name|OperationName
name|getOperationName
parameter_list|()
block|{
return|return
name|operationName
return|;
block|}
DECL|method|getTopicName ()
specifier|public
name|String
name|getTopicName
parameter_list|()
block|{
return|return
name|topicName
return|;
block|}
DECL|method|setReplayId (final Long replayId)
specifier|public
name|void
name|setReplayId
parameter_list|(
specifier|final
name|Long
name|replayId
parameter_list|)
block|{
name|this
operator|.
name|replayId
operator|=
name|replayId
expr_stmt|;
block|}
DECL|method|getReplayId ()
specifier|public
name|Long
name|getReplayId
parameter_list|()
block|{
return|return
name|replayId
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
try|try
block|{
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
comment|// check if this endpoint has its own http client that needs to be started
specifier|final
name|HttpClient
name|httpClient
init|=
name|getConfiguration
argument_list|()
operator|.
name|getHttpClient
argument_list|()
decl_stmt|;
if|if
condition|(
name|httpClient
operator|!=
literal|null
operator|&&
name|getComponent
argument_list|()
operator|.
name|getConfig
argument_list|()
operator|.
name|getHttpClient
argument_list|()
operator|!=
name|httpClient
condition|)
block|{
specifier|final
name|String
name|endpointUri
init|=
name|getEndpointUri
argument_list|()
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Starting http client for {} ..."
argument_list|,
name|endpointUri
argument_list|)
expr_stmt|;
name|httpClient
operator|.
name|start
argument_list|()
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Started http client for {}"
argument_list|,
name|endpointUri
argument_list|)
expr_stmt|;
block|}
block|}
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
try|try
block|{
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
comment|// check if this endpoint has its own http client that needs to be stopped
specifier|final
name|HttpClient
name|httpClient
init|=
name|getConfiguration
argument_list|()
operator|.
name|getHttpClient
argument_list|()
decl_stmt|;
if|if
condition|(
name|httpClient
operator|!=
literal|null
operator|&&
name|getComponent
argument_list|()
operator|.
name|getConfig
argument_list|()
operator|.
name|getHttpClient
argument_list|()
operator|!=
name|httpClient
condition|)
block|{
specifier|final
name|String
name|endpointUri
init|=
name|getEndpointUri
argument_list|()
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Stopping http client for {} ..."
argument_list|,
name|endpointUri
argument_list|)
expr_stmt|;
name|httpClient
operator|.
name|stop
argument_list|()
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Stopped http client for {}"
argument_list|,
name|endpointUri
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

