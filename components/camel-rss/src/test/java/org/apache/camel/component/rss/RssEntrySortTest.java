begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.rss
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|rss
package|;
end_package

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|syndication
operator|.
name|feed
operator|.
name|synd
operator|.
name|SyndEntry
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|syndication
operator|.
name|feed
operator|.
name|synd
operator|.
name|SyndFeed
import|;
end_import

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
name|javax
operator|.
name|naming
operator|.
name|Context
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
name|ContextTestSupport
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
name|util
operator|.
name|jndi
operator|.
name|JndiContext
import|;
end_import

begin_class
DECL|class|RssEntrySortTest
specifier|public
class|class
name|RssEntrySortTest
extends|extends
name|ContextTestSupport
block|{
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
literal|"myBean"
argument_list|,
literal|"getPubDate"
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
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
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
literal|"myBean"
argument_list|,
literal|"getPubDate"
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
DECL|method|createJndiContext ()
specifier|protected
name|Context
name|createJndiContext
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiContext
name|jndi
init|=
operator|new
name|JndiContext
argument_list|()
decl_stmt|;
name|jndi
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
return|return
name|jndi
return|;
block|}
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
literal|"rss:file:src/test/data/rss20.xml?splitEntries=true&sortEntries=true&consumer.delay=50"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:sorted"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"rss:file:src/test/data/rss20.xml?splitEntries=true&sortEntries=false&consumer.delay=50"
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
name|SyndFeed
name|feed
init|=
operator|(
name|SyndFeed
operator|)
name|body
decl_stmt|;
name|SyndEntry
name|syndEntry
init|=
operator|(
name|SyndEntry
operator|)
name|feed
operator|.
name|getEntries
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Date
name|date
init|=
name|syndEntry
operator|.
name|getUpdatedDate
argument_list|()
decl_stmt|;
if|if
condition|(
name|date
operator|==
literal|null
condition|)
block|{
name|date
operator|=
name|syndEntry
operator|.
name|getPublishedDate
argument_list|()
expr_stmt|;
block|}
return|return
name|date
return|;
block|}
block|}
block|}
end_class

end_unit

