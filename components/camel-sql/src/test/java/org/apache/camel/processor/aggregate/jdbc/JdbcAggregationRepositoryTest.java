begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.aggregate.jdbc
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
name|jdbc
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|JdbcAggregationRepositoryTest
specifier|public
class|class
name|JdbcAggregationRepositoryTest
extends|extends
name|AbstractJdbcAggregationTestSupport
block|{
annotation|@
name|Override
DECL|method|configureJdbcAggregationRepository ()
name|void
name|configureJdbcAggregationRepository
parameter_list|()
block|{
name|repo
operator|.
name|setReturnOldExchange
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testOperations ()
specifier|public
name|void
name|testOperations
parameter_list|()
block|{
comment|// Can't get something we have not put in...
name|Exchange
name|actual
init|=
name|repo
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
name|repo
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
name|repo
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
comment|// Get it back..
name|actual
operator|=
name|repo
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
comment|// now remove it
name|repo
operator|.
name|remove
argument_list|(
name|context
argument_list|,
literal|"foo"
argument_list|,
name|actual
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
literal|"foo"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|actual
argument_list|)
expr_stmt|;
comment|// add it again
name|exchange1
operator|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|exchange1
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"counter:3"
argument_list|)
expr_stmt|;
name|actual
operator|=
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
name|repo
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
literal|"counter:3"
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

