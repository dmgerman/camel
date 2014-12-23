begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cassandra
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cassandra
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|ConsistencyLevel
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
name|ResultSet
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
name|Row
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
name|cassandraunit
operator|.
name|CassandraCQLUnit
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Rule
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
DECL|class|CassandraComponentProducerTest
specifier|public
class|class
name|CassandraComponentProducerTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|CQL
specifier|private
specifier|static
specifier|final
name|String
name|CQL
init|=
literal|"insert into camel_user(login, first_name, last_name) values (?, ?, ?)"
decl_stmt|;
DECL|field|NOT_CONSISTENT_URI
specifier|private
specifier|static
specifier|final
name|String
name|NOT_CONSISTENT_URI
init|=
literal|"cql://localhost/camel_ks?cql="
operator|+
name|CQL
operator|+
literal|"&consistencyLevel=ANY"
decl_stmt|;
annotation|@
name|Rule
DECL|field|cassandra
specifier|public
name|CassandraCQLUnit
name|cassandra
init|=
name|CassandraUnitUtils
operator|.
name|cassandraCQLUnit
argument_list|()
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:input"
argument_list|)
DECL|field|producerTemplate
name|ProducerTemplate
name|producerTemplate
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:inputNotConsistent"
argument_list|)
DECL|field|notConsistentProducerTemplate
name|ProducerTemplate
name|notConsistentProducerTemplate
decl_stmt|;
annotation|@
name|BeforeClass
DECL|method|setUpClass ()
specifier|public
specifier|static
name|void
name|setUpClass
parameter_list|()
throws|throws
name|Exception
block|{
name|CassandraUnitUtils
operator|.
name|startEmbeddedCassandra
argument_list|()
expr_stmt|;
block|}
annotation|@
name|AfterClass
DECL|method|tearDownClass ()
specifier|public
specifier|static
name|void
name|tearDownClass
parameter_list|()
throws|throws
name|Exception
block|{
name|CassandraUnitUtils
operator|.
name|cleanEmbeddedCassandra
argument_list|()
expr_stmt|;
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
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:input"
argument_list|)
operator|.
name|to
argument_list|(
literal|"cql://localhost/camel_ks?cql="
operator|+
name|CQL
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:inputNotConsistent"
argument_list|)
operator|.
name|to
argument_list|(
name|NOT_CONSISTENT_URI
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|testRequestUriCql ()
specifier|public
name|void
name|testRequestUriCql
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|response
init|=
name|producerTemplate
operator|.
name|requestBody
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"w_jiang"
argument_list|,
literal|"Willem"
argument_list|,
literal|"Jiang"
argument_list|)
argument_list|)
decl_stmt|;
name|Cluster
name|cluster
init|=
name|CassandraUnitUtils
operator|.
name|cassandraCluster
argument_list|()
decl_stmt|;
name|Session
name|session
init|=
name|cluster
operator|.
name|connect
argument_list|(
name|CassandraUnitUtils
operator|.
name|KEYSPACE
argument_list|)
decl_stmt|;
name|ResultSet
name|resultSet
init|=
name|session
operator|.
name|execute
argument_list|(
literal|"select login, first_name, last_name from camel_user where login = ?"
argument_list|,
literal|"w_jiang"
argument_list|)
decl_stmt|;
name|Row
name|row
init|=
name|resultSet
operator|.
name|one
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|row
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Willem"
argument_list|,
name|row
operator|.
name|getString
argument_list|(
literal|"first_name"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Jiang"
argument_list|,
name|row
operator|.
name|getString
argument_list|(
literal|"last_name"
argument_list|)
argument_list|)
expr_stmt|;
name|session
operator|.
name|close
argument_list|()
expr_stmt|;
name|cluster
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRequestMessageCql ()
specifier|public
name|void
name|testRequestMessageCql
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|response
init|=
name|producerTemplate
operator|.
name|requestBodyAndHeader
argument_list|(
operator|new
name|Object
index|[]
block|{
literal|"Claus 2"
block|,
literal|"Ibsen 2"
block|,
literal|"c_ibsen"
block|}
argument_list|,
name|CassandraConstants
operator|.
name|CQL_QUERY
argument_list|,
literal|"update camel_user set first_name=?, last_name=? where login=?"
argument_list|)
decl_stmt|;
name|Cluster
name|cluster
init|=
name|CassandraUnitUtils
operator|.
name|cassandraCluster
argument_list|()
decl_stmt|;
name|Session
name|session
init|=
name|cluster
operator|.
name|connect
argument_list|(
name|CassandraUnitUtils
operator|.
name|KEYSPACE
argument_list|)
decl_stmt|;
name|ResultSet
name|resultSet
init|=
name|session
operator|.
name|execute
argument_list|(
literal|"select login, first_name, last_name from camel_user where login = ?"
argument_list|,
literal|"c_ibsen"
argument_list|)
decl_stmt|;
name|Row
name|row
init|=
name|resultSet
operator|.
name|one
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|row
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Claus 2"
argument_list|,
name|row
operator|.
name|getString
argument_list|(
literal|"first_name"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Ibsen 2"
argument_list|,
name|row
operator|.
name|getString
argument_list|(
literal|"last_name"
argument_list|)
argument_list|)
expr_stmt|;
name|session
operator|.
name|close
argument_list|()
expr_stmt|;
name|cluster
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRequestNotConsistent ()
specifier|public
name|void
name|testRequestNotConsistent
parameter_list|()
throws|throws
name|Exception
block|{
name|CassandraEndpoint
name|endpoint
init|=
name|getMandatoryEndpoint
argument_list|(
name|NOT_CONSISTENT_URI
argument_list|,
name|CassandraEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|ConsistencyLevel
operator|.
name|ANY
argument_list|,
name|endpoint
operator|.
name|getConsistencyLevel
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|response
init|=
name|notConsistentProducerTemplate
operator|.
name|requestBody
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"j_anstey"
argument_list|,
literal|"Jonathan"
argument_list|,
literal|"Anstey"
argument_list|)
argument_list|)
decl_stmt|;
block|}
block|}
end_class

end_unit

