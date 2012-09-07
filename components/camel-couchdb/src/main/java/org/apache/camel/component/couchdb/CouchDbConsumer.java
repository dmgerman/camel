begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.couchdb
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|couchdb
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutorService
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
name|DefaultConsumer
import|;
end_import

begin_class
DECL|class|CouchDbConsumer
specifier|public
class|class
name|CouchDbConsumer
extends|extends
name|DefaultConsumer
block|{
DECL|field|couchClient
specifier|private
specifier|final
name|CouchDbClientWrapper
name|couchClient
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|CouchDbEndpoint
name|endpoint
decl_stmt|;
DECL|field|executor
specifier|private
name|ExecutorService
name|executor
decl_stmt|;
DECL|field|task
specifier|private
name|CouchDbChangesetTracker
name|task
decl_stmt|;
DECL|method|CouchDbConsumer (CouchDbEndpoint endpoint, CouchDbClientWrapper couchClient, Processor processor)
specifier|public
name|CouchDbConsumer
parameter_list|(
name|CouchDbEndpoint
name|endpoint
parameter_list|,
name|CouchDbClientWrapper
name|couchClient
parameter_list|,
name|Processor
name|processor
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
name|couchClient
operator|=
name|couchClient
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
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
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Starting CouchDB consumer"
argument_list|)
expr_stmt|;
name|executor
operator|=
name|endpoint
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newFixedThreadPool
argument_list|(
name|this
argument_list|,
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|task
operator|=
operator|new
name|CouchDbChangesetTracker
argument_list|(
name|endpoint
argument_list|,
name|this
argument_list|,
name|couchClient
argument_list|)
expr_stmt|;
name|executor
operator|.
name|submit
argument_list|(
name|task
argument_list|)
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
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Stopping CouchDB consumer"
argument_list|)
expr_stmt|;
if|if
condition|(
name|task
operator|!=
literal|null
condition|)
block|{
name|task
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|executor
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|shutdownNow
argument_list|(
name|executor
argument_list|)
expr_stmt|;
name|executor
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

