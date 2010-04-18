begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hawtdb
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hawtdb
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
name|impl
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
name|Test
import|;
end_import

begin_class
DECL|class|HawtDBAggregationRepositoryAlotDataTest
specifier|public
class|class
name|HawtDBAggregationRepositoryAlotDataTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|hawtDBFile
specifier|private
name|HawtDBFile
name|hawtDBFile
decl_stmt|;
annotation|@
name|Override
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
literal|"target/data/hawtdb.dat"
argument_list|)
decl_stmt|;
name|hawtDBFile
operator|=
operator|new
name|HawtDBFile
argument_list|()
expr_stmt|;
name|hawtDBFile
operator|.
name|setFile
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|hawtDBFile
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|hawtDBFile
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
DECL|method|testWithAlotOfDataSameKey ()
specifier|public
name|void
name|testWithAlotOfDataSameKey
parameter_list|()
block|{
name|HawtDBAggregationRepository
name|repo
init|=
operator|new
name|HawtDBAggregationRepository
argument_list|()
decl_stmt|;
name|repo
operator|.
name|setHawtDBFile
argument_list|(
name|hawtDBFile
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setRepositoryName
argument_list|(
literal|"repo1"
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|100
condition|;
name|i
operator|++
control|)
block|{
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
literal|"counter:"
operator|+
name|i
argument_list|)
expr_stmt|;
name|repo
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
block|}
comment|// Get it back..
name|Exchange
name|actual
init|=
name|repo
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
literal|"counter:99"
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
annotation|@
name|Test
DECL|method|testWithAlotOfDataTwoKesy ()
specifier|public
name|void
name|testWithAlotOfDataTwoKesy
parameter_list|()
block|{
name|HawtDBAggregationRepository
name|repo
init|=
operator|new
name|HawtDBAggregationRepository
argument_list|()
decl_stmt|;
name|repo
operator|.
name|setHawtDBFile
argument_list|(
name|hawtDBFile
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setRepositoryName
argument_list|(
literal|"repo1"
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|100
condition|;
name|i
operator|++
control|)
block|{
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
literal|"counter:"
operator|+
name|i
argument_list|)
expr_stmt|;
name|String
name|key
init|=
name|i
operator|%
literal|2
operator|==
literal|0
condition|?
literal|"foo"
else|:
literal|"bar"
decl_stmt|;
name|repo
operator|.
name|add
argument_list|(
name|context
argument_list|,
name|key
argument_list|,
name|exchange1
argument_list|)
expr_stmt|;
block|}
comment|// Get it back..
name|Exchange
name|actual
init|=
name|repo
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
literal|"counter:98"
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
name|repo
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
literal|"counter:99"
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
annotation|@
name|Test
DECL|method|testWithAlotOfDataWithDifferentKesy ()
specifier|public
name|void
name|testWithAlotOfDataWithDifferentKesy
parameter_list|()
block|{
name|HawtDBAggregationRepository
name|repo
init|=
operator|new
name|HawtDBAggregationRepository
argument_list|()
decl_stmt|;
name|repo
operator|.
name|setHawtDBFile
argument_list|(
name|hawtDBFile
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setRepositoryName
argument_list|(
literal|"repo1"
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|100
condition|;
name|i
operator|++
control|)
block|{
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
literal|"counter:"
operator|+
name|i
argument_list|)
expr_stmt|;
name|String
name|key
init|=
literal|"key"
operator|+
name|i
decl_stmt|;
name|repo
operator|.
name|add
argument_list|(
name|context
argument_list|,
name|key
argument_list|,
name|exchange1
argument_list|)
expr_stmt|;
block|}
comment|// Get it back..
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|100
condition|;
name|i
operator|++
control|)
block|{
name|Exchange
name|actual
init|=
name|repo
operator|.
name|get
argument_list|(
name|context
argument_list|,
literal|"key"
operator|+
name|i
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"counter:"
operator|+
name|i
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
block|}
end_class

end_unit

