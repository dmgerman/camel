begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.leveldb
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|leveldb
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|support
operator|.
name|DefaultExchange
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
name|After
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
name|Test
import|;
end_import

begin_class
DECL|class|LevelDBAggregationRepositoryMultipleRepoTest
specifier|public
class|class
name|LevelDBAggregationRepositoryMultipleRepoTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|levelDBFile
specifier|private
name|LevelDBFile
name|levelDBFile
decl_stmt|;
annotation|@
name|Override
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
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|deleteDirectory
argument_list|(
literal|"target/data"
argument_list|)
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
literal|"target/data/leveldb.dat"
argument_list|)
decl_stmt|;
name|levelDBFile
operator|=
operator|new
name|LevelDBFile
argument_list|()
expr_stmt|;
name|levelDBFile
operator|.
name|setFile
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|levelDBFile
operator|.
name|start
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
name|levelDBFile
operator|.
name|stop
argument_list|()
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMultipeRepo ()
specifier|public
name|void
name|testMultipeRepo
parameter_list|()
block|{
name|LevelDBAggregationRepository
name|repo1
init|=
operator|new
name|LevelDBAggregationRepository
argument_list|()
decl_stmt|;
name|repo1
operator|.
name|setLevelDBFile
argument_list|(
name|levelDBFile
argument_list|)
expr_stmt|;
name|repo1
operator|.
name|setRepositoryName
argument_list|(
literal|"repo1"
argument_list|)
expr_stmt|;
name|repo1
operator|.
name|setReturnOldExchange
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|LevelDBAggregationRepository
name|repo2
init|=
operator|new
name|LevelDBAggregationRepository
argument_list|()
decl_stmt|;
name|repo2
operator|.
name|setLevelDBFile
argument_list|(
name|levelDBFile
argument_list|)
expr_stmt|;
name|repo2
operator|.
name|setRepositoryName
argument_list|(
literal|"repo2"
argument_list|)
expr_stmt|;
name|repo2
operator|.
name|setReturnOldExchange
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// Can't get something we have not put in...
name|Exchange
name|actual
init|=
name|repo1
operator|.
name|get
argument_list|(
name|context
argument_list|,
literal|"missing"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|actual
argument_list|)
expr_stmt|;
name|actual
operator|=
name|repo2
operator|.
name|get
argument_list|(
name|context
argument_list|,
literal|"missing"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|actual
argument_list|)
expr_stmt|;
comment|// Store it..
name|Exchange
name|exchange1
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|exchange1
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"counter:1"
argument_list|)
expr_stmt|;
name|actual
operator|=
name|repo1
operator|.
name|add
argument_list|(
name|context
argument_list|,
literal|"foo"
argument_list|,
name|exchange1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|actual
argument_list|)
expr_stmt|;
comment|// Get it back..
name|actual
operator|=
name|repo1
operator|.
name|get
argument_list|(
name|context
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"counter:1"
argument_list|,
name|actual
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|repo2
operator|.
name|get
argument_list|(
name|context
argument_list|,
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
comment|// Change it..
name|Exchange
name|exchange2
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|exchange2
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"counter:2"
argument_list|)
expr_stmt|;
name|actual
operator|=
name|repo1
operator|.
name|add
argument_list|(
name|context
argument_list|,
literal|"foo"
argument_list|,
name|exchange2
argument_list|)
expr_stmt|;
comment|// the old one
name|assertEquals
argument_list|(
literal|"counter:1"
argument_list|,
name|actual
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
comment|// add to repo2
name|Exchange
name|exchange3
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|exchange3
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|actual
operator|=
name|repo2
operator|.
name|add
argument_list|(
name|context
argument_list|,
literal|"bar"
argument_list|,
name|exchange3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|actual
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|repo1
operator|.
name|get
argument_list|(
name|context
argument_list|,
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
comment|// Get it back..
name|actual
operator|=
name|repo1
operator|.
name|get
argument_list|(
name|context
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"counter:2"
argument_list|,
name|actual
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|repo2
operator|.
name|get
argument_list|(
name|context
argument_list|,
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|actual
operator|=
name|repo2
operator|.
name|get
argument_list|(
name|context
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|actual
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|repo1
operator|.
name|get
argument_list|(
name|context
argument_list|,
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMultipeRepoSameKeyDifferentContent ()
specifier|public
name|void
name|testMultipeRepoSameKeyDifferentContent
parameter_list|()
block|{
name|LevelDBAggregationRepository
name|repo1
init|=
operator|new
name|LevelDBAggregationRepository
argument_list|()
decl_stmt|;
name|repo1
operator|.
name|setLevelDBFile
argument_list|(
name|levelDBFile
argument_list|)
expr_stmt|;
name|repo1
operator|.
name|setRepositoryName
argument_list|(
literal|"repo1"
argument_list|)
expr_stmt|;
name|LevelDBAggregationRepository
name|repo2
init|=
operator|new
name|LevelDBAggregationRepository
argument_list|()
decl_stmt|;
name|repo2
operator|.
name|setLevelDBFile
argument_list|(
name|levelDBFile
argument_list|)
expr_stmt|;
name|repo2
operator|.
name|setRepositoryName
argument_list|(
literal|"repo2"
argument_list|)
expr_stmt|;
name|Exchange
name|exchange1
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|exchange1
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|repo1
operator|.
name|add
argument_list|(
name|context
argument_list|,
literal|"foo"
argument_list|,
name|exchange1
argument_list|)
expr_stmt|;
name|Exchange
name|exchange2
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|exchange2
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Bye World"
argument_list|)
expr_stmt|;
name|repo2
operator|.
name|add
argument_list|(
name|context
argument_list|,
literal|"foo"
argument_list|,
name|exchange2
argument_list|)
expr_stmt|;
name|Exchange
name|actual
init|=
name|repo1
operator|.
name|get
argument_list|(
name|context
argument_list|,
literal|"foo"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|actual
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|actual
operator|=
name|repo2
operator|.
name|get
argument_list|(
name|context
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|actual
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

