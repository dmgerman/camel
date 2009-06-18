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
name|java
operator|.
name|io
operator|.
name|BufferedReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedWriter
import|;
end_import

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
name|io
operator|.
name|FileReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|Message
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
DECL|class|RssCustomAggregatorTest
specifier|public
class|class
name|RssCustomAggregatorTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testMergingListOfEntries ()
specifier|public
name|void
name|testMergingListOfEntries
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|Exchange
name|exchange
init|=
name|mock
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|in
operator|.
name|getBody
argument_list|()
operator|instanceof
name|SyndFeed
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|in
operator|.
name|getHeader
argument_list|(
name|RssConstants
operator|.
name|RSS_FEED
argument_list|)
operator|instanceof
name|SyndFeed
argument_list|)
expr_stmt|;
name|SyndFeed
name|body
init|=
name|in
operator|.
name|getBody
argument_list|(
name|SyndFeed
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|20
argument_list|,
name|body
operator|.
name|getEntries
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|copy
argument_list|(
literal|"src/test/data/rss20.xml"
argument_list|,
literal|"target/rss20.xml"
argument_list|)
expr_stmt|;
block|}
DECL|method|copy (String source, String destination)
specifier|private
name|void
name|copy
parameter_list|(
name|String
name|source
parameter_list|,
name|String
name|destination
parameter_list|)
throws|throws
name|IOException
block|{
name|BufferedReader
name|input
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|FileReader
argument_list|(
operator|new
name|File
argument_list|(
name|source
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|BufferedWriter
name|output
init|=
operator|new
name|BufferedWriter
argument_list|(
operator|new
name|FileWriter
argument_list|(
operator|new
name|File
argument_list|(
name|destination
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|line
init|=
literal|null
decl_stmt|;
while|while
condition|(
operator|(
name|line
operator|=
name|input
operator|.
name|readLine
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
name|output
operator|.
name|write
argument_list|(
name|line
argument_list|)
expr_stmt|;
block|}
name|input
operator|.
name|close
argument_list|()
expr_stmt|;
name|output
operator|.
name|close
argument_list|()
expr_stmt|;
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
comment|// START SNIPPET: ex
name|from
argument_list|(
literal|"rss:file:src/test/data/rss20.xml?sortEntries=true&consumer.delay=50"
argument_list|)
operator|.
name|to
argument_list|(
literal|"seda:temp"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"rss:file:target/rss20.xml?sortEntries=true&consumer.delay=50"
argument_list|)
operator|.
name|to
argument_list|(
literal|"seda:temp"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:temp"
argument_list|)
operator|.
name|aggregate
argument_list|(
operator|new
name|AggregateRssFeedCollection
argument_list|()
argument_list|)
operator|.
name|batchTimeout
argument_list|(
literal|5000L
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: ex
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

