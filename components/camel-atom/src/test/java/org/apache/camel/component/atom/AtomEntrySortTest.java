begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.atom
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|atom
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|abdera
operator|.
name|model
operator|.
name|Entry
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
name|Body
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
name|ExpressionBuilder
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
name|spi
operator|.
name|Registry
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
DECL|class|AtomEntrySortTest
specifier|public
class|class
name|AtomEntrySortTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testSortedEntries ()
specifier|public
name|void
name|testSortedEntries
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:sorted"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectsAscending
argument_list|(
name|ExpressionBuilder
operator|.
name|beanExpression
argument_list|(
literal|"myBean?method=getPubDate"
argument_list|)
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|mock
operator|.
name|setResultWaitTime
argument_list|(
literal|15000L
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUnSortedEntries ()
specifier|public
name|void
name|testUnSortedEntries
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:unsorted"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectsAscending
argument_list|(
name|ExpressionBuilder
operator|.
name|beanExpression
argument_list|(
literal|"myBean?method=getPubDate"
argument_list|)
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|mock
operator|.
name|setResultWaitTime
argument_list|(
literal|2000L
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsNotSatisfied
argument_list|(
literal|2000L
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|bindToRegistry (Registry registry)
specifier|protected
name|void
name|bindToRegistry
parameter_list|(
name|Registry
name|registry
parameter_list|)
throws|throws
name|Exception
block|{
name|registry
operator|.
name|bind
argument_list|(
literal|"myBean"
argument_list|,
operator|new
name|MyBean
argument_list|()
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
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"atom:file:src/test/data/unsortedfeed.atom?splitEntries=true&sortEntries=true&delay=50"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:sorted"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"atom:file:src/test/data/unsortedfeed.atom?splitEntries=true&sortEntries=false&delay=50"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:unsorted"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|MyBean
specifier|public
specifier|static
class|class
name|MyBean
block|{
DECL|method|getPubDate (@ody Object body)
specifier|public
name|Date
name|getPubDate
parameter_list|(
annotation|@
name|Body
name|Object
name|body
parameter_list|)
block|{
name|Entry
name|syndEntry
init|=
operator|(
name|Entry
operator|)
name|body
decl_stmt|;
return|return
name|syndEntry
operator|.
name|getUpdated
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

