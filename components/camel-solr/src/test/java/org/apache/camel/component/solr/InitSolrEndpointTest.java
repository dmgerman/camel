begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.solr
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|solr
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
name|ResolveEndpointFailedException
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
DECL|class|InitSolrEndpointTest
specifier|public
class|class
name|InitSolrEndpointTest
extends|extends
name|SolrTestSupport
block|{
DECL|field|solrUrl
specifier|private
name|String
name|solrUrl
init|=
literal|"solr://localhost:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/solr"
decl_stmt|;
annotation|@
name|Test
DECL|method|endpointCreatedCorrectlyWithAllOptions ()
specifier|public
name|void
name|endpointCreatedCorrectlyWithAllOptions
parameter_list|()
throws|throws
name|Exception
block|{
name|SolrEndpoint
name|solrEndpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
name|solrUrl
operator|+
name|getFullOptions
argument_list|()
argument_list|,
name|SolrEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"queue size incorrect"
argument_list|,
literal|5
argument_list|,
name|solrEndpoint
operator|.
name|getStreamingQueueSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"thread count incorrect"
argument_list|,
literal|1
argument_list|,
name|solrEndpoint
operator|.
name|getStreamingThreadCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|solrEndpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|streamingEndpointCreatedCorrectly ()
specifier|public
name|void
name|streamingEndpointCreatedCorrectly
parameter_list|()
throws|throws
name|Exception
block|{
name|SolrEndpoint
name|solrEndpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
name|solrUrl
argument_list|,
name|SolrEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|solrEndpoint
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"queue size incorrect"
argument_list|,
name|SolrConstants
operator|.
name|DEFUALT_STREAMING_QUEUE_SIZE
argument_list|,
name|solrEndpoint
operator|.
name|getStreamingQueueSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"thread count incorrect"
argument_list|,
name|SolrConstants
operator|.
name|DEFAULT_STREAMING_THREAD_COUNT
argument_list|,
name|solrEndpoint
operator|.
name|getStreamingThreadCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|ResolveEndpointFailedException
operator|.
name|class
argument_list|)
DECL|method|wrongURLFormatFailsEndpointCreation ()
specifier|public
name|void
name|wrongURLFormatFailsEndpointCreation
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"solr://localhost:x99/solr"
argument_list|)
expr_stmt|;
block|}
DECL|method|getFullOptions ()
specifier|private
name|String
name|getFullOptions
parameter_list|()
block|{
return|return
literal|"?streamingQueueSize=5&streamingThreadCount=1"
operator|+
literal|"&maxRetries=1&soTimeout=100&connectionTimeout=100"
operator|+
literal|"&defaultMaxConnectionsPerHost=100&maxTotalConnections=100"
operator|+
literal|"&followRedirects=false&allowCompression=true"
operator|+
literal|"&requestHandler=/update"
return|;
block|}
block|}
end_class

end_unit

