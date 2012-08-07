begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.neo4j
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|neo4j
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
name|EndpointInject
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
name|Produce
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
name|ProducerTemplate
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
name|builder
operator|.
name|RouteBuilder
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
name|mock
operator|.
name|MockEndpoint
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
name|Ignore
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

begin_import
import|import
name|org
operator|.
name|neo4j
operator|.
name|graphdb
operator|.
name|Node
import|;
end_import

begin_import
import|import
name|org
operator|.
name|neo4j
operator|.
name|graphdb
operator|.
name|Relationship
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|data
operator|.
name|neo4j
operator|.
name|rest
operator|.
name|SpringRestGraphDatabase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|data
operator|.
name|neo4j
operator|.
name|support
operator|.
name|Neo4jTemplate
import|;
end_import

begin_class
annotation|@
name|Ignore
argument_list|(
literal|"This test need to start the neo4j server first"
argument_list|)
DECL|class|RestNeo4jProducerCreateRelationshipIntegrationTest
specifier|public
class|class
name|RestNeo4jProducerCreateRelationshipIntegrationTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:start"
argument_list|)
DECL|field|template
specifier|protected
name|ProducerTemplate
name|template
decl_stmt|;
DECL|field|neo4jEndpoint
specifier|private
specifier|final
name|String
name|neo4jEndpoint
init|=
literal|"neo4j:http://localhost:7474/db/data/"
decl_stmt|;
DECL|field|neo
specifier|private
specifier|final
name|Neo4jTemplate
name|neo
init|=
operator|new
name|Neo4jTemplate
argument_list|(
operator|new
name|SpringRestGraphDatabase
argument_list|(
literal|"http://localhost:7474/db/data/"
argument_list|)
argument_list|)
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:end"
argument_list|)
DECL|field|end
specifier|private
name|MockEndpoint
name|end
decl_stmt|;
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
name|neo4jEndpoint
argument_list|)
operator|.
name|process
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
name|arg0
parameter_list|)
throws|throws
name|Exception
block|{
name|Long
name|id
init|=
operator|(
name|Long
operator|)
name|arg0
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Neo4jEndpoint
operator|.
name|HEADER_RELATIONSHIP_ID
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|Relationship
name|r
init|=
name|neo
operator|.
name|getRelationship
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|r
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"tickles"
argument_list|,
name|r
operator|.
name|getType
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
name|end
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|testCreateNodes ()
specifier|public
name|void
name|testCreateNodes
parameter_list|()
throws|throws
name|InterruptedException
block|{
specifier|final
name|int
name|messageCount
init|=
literal|100
decl_stmt|;
name|end
operator|.
name|expectedMessageCount
argument_list|(
name|messageCount
argument_list|)
expr_stmt|;
name|Thread
name|t
init|=
operator|new
name|Thread
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
for|for
control|(
name|int
name|k
init|=
literal|0
init|;
name|k
operator|<
name|messageCount
condition|;
name|k
operator|++
control|)
block|{
name|Node
name|start
init|=
name|neo
operator|.
name|createNode
argument_list|()
decl_stmt|;
name|Node
name|end
init|=
name|neo
operator|.
name|createNode
argument_list|()
decl_stmt|;
name|BasicRelationship
name|r
init|=
operator|new
name|BasicRelationship
argument_list|(
name|start
argument_list|,
name|end
argument_list|,
literal|"tickles"
argument_list|)
decl_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|r
argument_list|,
name|Neo4jEndpoint
operator|.
name|HEADER_OPERATION
argument_list|,
name|Neo4jOperation
operator|.
name|CREATE_RELATIONSHIP
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
decl_stmt|;
name|t
operator|.
name|start
argument_list|()
expr_stmt|;
name|t
operator|.
name|join
argument_list|()
expr_stmt|;
name|end
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

