begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.aggregate.cassandra
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|aggregate
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
name|Set
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
name|CamelContext
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
name|impl
operator|.
name|DefaultCamelContext
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
name|DefaultExchange
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
name|After
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
name|Before
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertFalse
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_comment
comment|/**  * Unite test for {@link CassandraAggregationRepository}  */
end_comment

begin_class
DECL|class|CassandraAggregationRepositoryTest
specifier|public
class|class
name|CassandraAggregationRepositoryTest
block|{
annotation|@
name|Rule
DECL|field|cassandraRule
specifier|public
name|CassandraCQLUnit
name|cassandraRule
init|=
name|CassandraUnitUtils
operator|.
name|cassandraCQLUnit
argument_list|(
literal|"AggregationDataSet.cql"
argument_list|)
decl_stmt|;
DECL|field|cluster
specifier|private
name|Cluster
name|cluster
decl_stmt|;
DECL|field|session
specifier|private
name|Session
name|session
decl_stmt|;
DECL|field|aggregationRepository
specifier|private
name|CassandraAggregationRepository
name|aggregationRepository
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
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
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|camelContext
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
name|cluster
operator|=
name|CassandraUnitUtils
operator|.
name|cassandraCluster
argument_list|()
expr_stmt|;
name|session
operator|=
name|cluster
operator|.
name|connect
argument_list|(
name|CassandraUnitUtils
operator|.
name|KEYSPACE
argument_list|)
expr_stmt|;
name|aggregationRepository
operator|=
operator|new
name|CassandraAggregationRepository
argument_list|(
name|session
argument_list|)
expr_stmt|;
name|aggregationRepository
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
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
name|aggregationRepository
operator|.
name|stop
argument_list|()
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
try|try
block|{
name|CassandraUnitUtils
operator|.
name|cleanEmbeddedCassandra
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|// ignore shutdown errors
block|}
block|}
DECL|method|exists (String key)
specifier|private
name|boolean
name|exists
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
name|session
operator|.
name|execute
argument_list|(
literal|"select KEY from CAMEL_AGGREGATION where KEY=?"
argument_list|,
name|key
argument_list|)
operator|.
name|one
argument_list|()
operator|!=
literal|null
return|;
block|}
annotation|@
name|Test
DECL|method|testAdd ()
specifier|public
name|void
name|testAdd
parameter_list|()
block|{
comment|// Given
name|String
name|key
init|=
literal|"Add"
decl_stmt|;
name|assertFalse
argument_list|(
name|exists
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
comment|// When
name|aggregationRepository
operator|.
name|add
argument_list|(
name|camelContext
argument_list|,
name|key
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
comment|// Then
name|assertTrue
argument_list|(
name|exists
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetExists ()
specifier|public
name|void
name|testGetExists
parameter_list|()
block|{
comment|// Given
name|String
name|key
init|=
literal|"Get_Exists"
decl_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
name|aggregationRepository
operator|.
name|add
argument_list|(
name|camelContext
argument_list|,
name|key
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|exists
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
comment|// When
name|Exchange
name|exchange2
init|=
name|aggregationRepository
operator|.
name|get
argument_list|(
name|camelContext
argument_list|,
name|key
argument_list|)
decl_stmt|;
comment|// Then
name|assertNotNull
argument_list|(
name|exchange2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|exchange2
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetNotExists ()
specifier|public
name|void
name|testGetNotExists
parameter_list|()
block|{
comment|// Given
name|String
name|key
init|=
literal|"Get_NotExists"
decl_stmt|;
name|assertFalse
argument_list|(
name|exists
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
comment|// When
name|Exchange
name|exchange2
init|=
name|aggregationRepository
operator|.
name|get
argument_list|(
name|camelContext
argument_list|,
name|key
argument_list|)
decl_stmt|;
comment|// Then
name|assertNull
argument_list|(
name|exchange2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRemoveExists ()
specifier|public
name|void
name|testRemoveExists
parameter_list|()
block|{
comment|// Given
name|String
name|key
init|=
literal|"Remove_Exists"
decl_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
name|aggregationRepository
operator|.
name|add
argument_list|(
name|camelContext
argument_list|,
name|key
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|exists
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
comment|// When
name|aggregationRepository
operator|.
name|remove
argument_list|(
name|camelContext
argument_list|,
name|key
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
comment|// Then
name|assertFalse
argument_list|(
name|exists
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRemoveNotExists ()
specifier|public
name|void
name|testRemoveNotExists
parameter_list|()
block|{
comment|// Given
name|String
name|key
init|=
literal|"RemoveNotExists"
decl_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|exists
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
comment|// When
name|aggregationRepository
operator|.
name|remove
argument_list|(
name|camelContext
argument_list|,
name|key
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
comment|// Then
name|assertFalse
argument_list|(
name|exists
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetKeys ()
specifier|public
name|void
name|testGetKeys
parameter_list|()
block|{
comment|// Given
name|String
index|[]
name|keys
init|=
block|{
literal|"GetKeys1"
block|,
literal|"GetKeys2"
block|}
decl_stmt|;
name|addExchanges
argument_list|(
name|keys
argument_list|)
expr_stmt|;
comment|// When
name|Set
argument_list|<
name|String
argument_list|>
name|keySet
init|=
name|aggregationRepository
operator|.
name|getKeys
argument_list|()
decl_stmt|;
comment|// Then
for|for
control|(
name|String
name|key
range|:
name|keys
control|)
block|{
name|assertTrue
argument_list|(
name|keySet
operator|.
name|contains
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testConfirmExist ()
specifier|public
name|void
name|testConfirmExist
parameter_list|()
block|{
comment|// Given
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
literal|4
condition|;
name|i
operator|++
control|)
block|{
name|String
name|key
init|=
literal|"Confirm_"
operator|+
name|i
decl_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|setExchangeId
argument_list|(
literal|"Exchange_"
operator|+
name|i
argument_list|)
expr_stmt|;
name|aggregationRepository
operator|.
name|add
argument_list|(
name|camelContext
argument_list|,
name|key
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|exists
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// When
name|aggregationRepository
operator|.
name|confirm
argument_list|(
name|camelContext
argument_list|,
literal|"Exchange_2"
argument_list|)
expr_stmt|;
comment|// Then
name|assertTrue
argument_list|(
name|exists
argument_list|(
literal|"Confirm_1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|exists
argument_list|(
literal|"Confirm_2"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|exists
argument_list|(
literal|"Confirm_3"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConfirmNotExist ()
specifier|public
name|void
name|testConfirmNotExist
parameter_list|()
block|{
comment|// Given
name|String
index|[]
name|keys
init|=
operator|new
name|String
index|[
literal|3
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
literal|4
condition|;
name|i
operator|++
control|)
block|{
name|keys
index|[
name|i
operator|-
literal|1
index|]
operator|=
literal|"Confirm"
operator|+
name|i
expr_stmt|;
block|}
name|addExchanges
argument_list|(
name|keys
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|key
range|:
name|keys
control|)
block|{
name|assertTrue
argument_list|(
name|exists
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// When
name|aggregationRepository
operator|.
name|confirm
argument_list|(
name|camelContext
argument_list|,
literal|"Exchange-Confirm5"
argument_list|)
expr_stmt|;
comment|// Then
for|for
control|(
name|String
name|key
range|:
name|keys
control|)
block|{
name|assertTrue
argument_list|(
name|exists
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|addExchanges (String... keys)
specifier|private
name|void
name|addExchanges
parameter_list|(
name|String
modifier|...
name|keys
parameter_list|)
block|{
for|for
control|(
name|String
name|key
range|:
name|keys
control|)
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|setExchangeId
argument_list|(
literal|"Exchange-"
operator|+
name|key
argument_list|)
expr_stmt|;
name|aggregationRepository
operator|.
name|add
argument_list|(
name|camelContext
argument_list|,
name|key
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testScan ()
specifier|public
name|void
name|testScan
parameter_list|()
block|{
comment|// Given
name|String
index|[]
name|keys
init|=
block|{
literal|"Scan1"
block|,
literal|"Scan2"
block|}
decl_stmt|;
name|addExchanges
argument_list|(
name|keys
argument_list|)
expr_stmt|;
comment|// When
name|Set
argument_list|<
name|String
argument_list|>
name|exchangeIdSet
init|=
name|aggregationRepository
operator|.
name|scan
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
comment|// Then
for|for
control|(
name|String
name|key
range|:
name|keys
control|)
block|{
name|assertTrue
argument_list|(
name|exchangeIdSet
operator|.
name|contains
argument_list|(
literal|"Exchange-"
operator|+
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testRecover ()
specifier|public
name|void
name|testRecover
parameter_list|()
block|{
comment|// Given
name|String
index|[]
name|keys
init|=
block|{
literal|"Recover1"
block|,
literal|"Recover2"
block|}
decl_stmt|;
name|addExchanges
argument_list|(
name|keys
argument_list|)
expr_stmt|;
comment|// When
name|Exchange
name|exchange2
init|=
name|aggregationRepository
operator|.
name|recover
argument_list|(
name|camelContext
argument_list|,
literal|"Exchange-Recover2"
argument_list|)
decl_stmt|;
name|Exchange
name|exchange3
init|=
name|aggregationRepository
operator|.
name|recover
argument_list|(
name|camelContext
argument_list|,
literal|"Exchange-Recover3"
argument_list|)
decl_stmt|;
comment|// Then
name|assertNotNull
argument_list|(
name|exchange2
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|exchange3
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

