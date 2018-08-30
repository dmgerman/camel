begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.idempotent.cassandra
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|idempotent
operator|.
name|cassandra
package|;
end_package

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|com
operator|.
name|datastax
operator|.
name|driver
operator|.
name|core
operator|.
name|Cluster
import|;
end_import

begin_import
import|import
name|com
operator|.
name|datastax
operator|.
name|driver
operator|.
name|core
operator|.
name|Session
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
name|cassandra
operator|.
name|BaseCassandraTest
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
name|cassandra
operator|.
name|CassandraUnitUtils
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
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * Unite test for {@link CassandraIdempotentRepository}  */
end_comment

begin_class
DECL|class|CassandraIdempotentTest
specifier|public
class|class
name|CassandraIdempotentTest
extends|extends
name|BaseCassandraTest
block|{
DECL|field|cluster
specifier|private
name|Cluster
name|cluster
decl_stmt|;
DECL|field|idempotentRepository
specifier|private
name|CassandraIdempotentRepository
name|idempotentRepository
decl_stmt|;
annotation|@
name|Override
DECL|method|doPreSetup ()
specifier|protected
name|void
name|doPreSetup
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|canTest
argument_list|()
condition|)
block|{
name|cluster
operator|=
name|CassandraUnitUtils
operator|.
name|cassandraCluster
argument_list|()
expr_stmt|;
name|Session
name|rootSession
init|=
name|cluster
operator|.
name|connect
argument_list|()
decl_stmt|;
name|CassandraUnitUtils
operator|.
name|loadCQLDataSet
argument_list|(
name|rootSession
argument_list|,
literal|"NamedIdempotentDataSet.cql"
argument_list|)
expr_stmt|;
name|rootSession
operator|.
name|close
argument_list|()
expr_stmt|;
name|idempotentRepository
operator|=
operator|new
name|NamedCassandraIdempotentRepository
argument_list|(
name|cluster
argument_list|,
name|CassandraUnitUtils
operator|.
name|KEYSPACE
argument_list|,
literal|"ID"
argument_list|)
expr_stmt|;
name|idempotentRepository
operator|.
name|setTable
argument_list|(
literal|"NAMED_CAMEL_IDEMPOTENT"
argument_list|)
expr_stmt|;
name|idempotentRepository
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
name|super
operator|.
name|doPreSetup
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
if|if
condition|(
name|canTest
argument_list|()
condition|)
block|{
name|idempotentRepository
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
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
literal|"direct:input"
argument_list|)
operator|.
name|idempotentConsumer
argument_list|(
name|header
argument_list|(
literal|"idempotentId"
argument_list|)
argument_list|,
name|idempotentRepository
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:output"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|send (String idempotentId, String body)
specifier|private
name|void
name|send
parameter_list|(
name|String
name|idempotentId
parameter_list|,
name|String
name|body
parameter_list|)
block|{
name|super
operator|.
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:input"
argument_list|,
name|body
argument_list|,
literal|"idempotentId"
argument_list|,
name|idempotentId
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testIdempotentRoute ()
specifier|public
name|void
name|testIdempotentRoute
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|canTest
argument_list|()
condition|)
block|{
return|return;
block|}
comment|// Given
name|MockEndpoint
name|mockOutput
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:output"
argument_list|)
decl_stmt|;
name|mockOutput
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|mockOutput
operator|.
name|expectedBodiesReceivedInAnyOrder
argument_list|(
literal|"A"
argument_list|,
literal|"B"
argument_list|)
expr_stmt|;
comment|// When
name|send
argument_list|(
literal|"1"
argument_list|,
literal|"A"
argument_list|)
expr_stmt|;
name|send
argument_list|(
literal|"2"
argument_list|,
literal|"B"
argument_list|)
expr_stmt|;
name|send
argument_list|(
literal|"1"
argument_list|,
literal|"A"
argument_list|)
expr_stmt|;
name|send
argument_list|(
literal|"2"
argument_list|,
literal|"B"
argument_list|)
expr_stmt|;
name|send
argument_list|(
literal|"1"
argument_list|,
literal|"A"
argument_list|)
expr_stmt|;
comment|// Then
name|mockOutput
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

