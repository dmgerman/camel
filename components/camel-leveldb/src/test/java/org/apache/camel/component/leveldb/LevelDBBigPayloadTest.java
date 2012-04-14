begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicLong
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
name|processor
operator|.
name|aggregate
operator|.
name|UseLatestAggregationStrategy
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
name|Before
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

begin_comment
comment|/**  * Test issue with leveldb file store growing to large  */
end_comment

begin_class
annotation|@
name|Ignore
argument_list|(
literal|"Run this test manually"
argument_list|)
DECL|class|LevelDBBigPayloadTest
specifier|public
class|class
name|LevelDBBigPayloadTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|TIME
specifier|private
specifier|static
specifier|final
name|long
name|TIME
init|=
literal|60
operator|*
literal|1000
decl_stmt|;
DECL|field|NUMBER
specifier|private
specifier|static
specifier|final
name|AtomicLong
name|NUMBER
init|=
operator|new
name|AtomicLong
argument_list|()
decl_stmt|;
DECL|field|repo
specifier|private
name|LevelDBAggregationRepository
name|repo
decl_stmt|;
annotation|@
name|Before
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
name|deleteDirectory
argument_list|(
literal|"target/data"
argument_list|)
expr_stmt|;
name|repo
operator|=
operator|new
name|LevelDBAggregationRepository
argument_list|(
literal|"repo1"
argument_list|,
literal|"target/data/leveldb.dat"
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBigPayload ()
specifier|public
name|void
name|testBigPayload
parameter_list|()
throws|throws
name|Exception
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Running test for "
operator|+
name|TIME
operator|+
literal|" millis."
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|60
operator|*
literal|1000
argument_list|)
expr_stmt|;
comment|// assert the file size of the repo is not big< 32mb
name|File
name|file
init|=
operator|new
name|File
argument_list|(
literal|"target/data/leveldb.dat"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|file
operator|+
literal|" should exists"
argument_list|,
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|long
name|size
init|=
name|file
operator|.
name|length
argument_list|()
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
name|file
operator|+
literal|" size is "
operator|+
name|size
argument_list|)
expr_stmt|;
comment|// should be about 32mb, so we say 34 just in case
name|assertTrue
argument_list|(
name|file
operator|+
literal|" should not be so big in size, was: "
operator|+
name|size
argument_list|,
name|size
operator|<
literal|34
operator|*
literal|1024
operator|*
literal|1024
argument_list|)
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
literal|"timer:foo"
argument_list|)
operator|.
name|bean
argument_list|(
name|BigPayload
operator|.
name|class
argument_list|)
operator|.
name|aggregate
argument_list|(
name|method
argument_list|(
name|LevelDBBigPayloadTest
operator|.
name|class
argument_list|,
literal|"number"
argument_list|)
argument_list|,
operator|new
name|UseLatestAggregationStrategy
argument_list|()
argument_list|)
operator|.
name|aggregationRepository
argument_list|(
name|repo
argument_list|)
operator|.
name|completionSize
argument_list|(
literal|2
argument_list|)
operator|.
name|completionTimeout
argument_list|(
literal|5000
argument_list|)
operator|.
name|log
argument_list|(
literal|"Aggregated key ${header.CamelAggregatedCorrelationKey}"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|number ()
specifier|public
specifier|static
name|long
name|number
parameter_list|()
block|{
comment|// return 123; (will not cause leveldb to grow in size)
return|return
name|NUMBER
operator|.
name|incrementAndGet
argument_list|()
return|;
block|}
block|}
end_class

end_unit

