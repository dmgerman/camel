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
name|support
operator|.
name|SimpleRegistry
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
name|TestSupport
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
comment|/**  * Example for wiki documentation  */
end_comment

begin_class
DECL|class|AtomGoodBlogsTest
specifier|public
class|class
name|AtomGoodBlogsTest
extends|extends
name|TestSupport
block|{
comment|// START SNIPPET: e1
comment|// This is the CamelContext that is the heart of Camel
DECL|field|context
specifier|private
name|CamelContext
name|context
decl_stmt|;
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
comment|// First we register a blog service in our bean registry
name|SimpleRegistry
name|registry
init|=
operator|new
name|SimpleRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"blogService"
argument_list|,
operator|new
name|BlogService
argument_list|()
argument_list|)
expr_stmt|;
comment|// Then we create the camel context with our bean registry
name|context
operator|=
operator|new
name|DefaultCamelContext
argument_list|(
name|registry
argument_list|)
expr_stmt|;
comment|// Then we add all the routes we need using the route builder DSL syntax
name|context
operator|.
name|addRoutes
argument_list|(
name|createMyRoutes
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
comment|/**      * This is the route builder where we create our routes using the Camel DSL      */
DECL|method|createMyRoutes ()
specifier|protected
name|RouteBuilder
name|createMyRoutes
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
comment|// We pool the atom feeds from the source for further processing in the seda queue
comment|// we set the delay to 1 second for each pool as this is a unit test also and we can
comment|// not wait the default poll interval of 60 seconds.
comment|// Using splitEntries=true will during polling only fetch one Atom Entry at any given time.
comment|// As the feed.atom file contains 7 entries, using this will require 7 polls to fetch the entire
comment|// content. When Camel have reach the end of entries it will refresh the atom feed from URI source
comment|// and restart - but as Camel by default uses the UpdatedDateFilter it will only deliver new
comment|// blog entries to "seda:feeds". So only when James Straham updates his blog with a new entry
comment|// Camel will create an exchange for the seda:feeds.
name|from
argument_list|(
literal|"atom:file:src/test/data/feed.atom?splitEntries=true&delay=1000"
argument_list|)
operator|.
name|to
argument_list|(
literal|"seda:feeds"
argument_list|)
expr_stmt|;
comment|// From the feeds we filter each blot entry by using our blog service class
name|from
argument_list|(
literal|"seda:feeds"
argument_list|)
operator|.
name|filter
argument_list|()
operator|.
name|method
argument_list|(
literal|"blogService"
argument_list|,
literal|"isGoodBlog"
argument_list|)
operator|.
name|to
argument_list|(
literal|"seda:goodBlogs"
argument_list|)
expr_stmt|;
comment|// And the good blogs is moved to a mock queue as this sample is also used for unit testing
comment|// this is one of the strengths in Camel that you can also use the mock endpoint for your
comment|// unit tests
name|from
argument_list|(
literal|"seda:goodBlogs"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
comment|/**      * This is the actual junit test method that does the assertion that our routes is working as expected      */
annotation|@
name|Test
DECL|method|testFiltering ()
specifier|public
name|void
name|testFiltering
parameter_list|()
throws|throws
name|Exception
block|{
comment|// create and start Camel
name|context
operator|=
name|createCamelContext
argument_list|()
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// Get the mock endpoint
name|MockEndpoint
name|mock
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:result"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// There should be at least two good blog entries from the feed
name|mock
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
comment|// Asserts that the above expectations is true, will throw assertions exception if it failed
comment|// Camel will default wait max 20 seconds for the assertions to be true, if the conditions
comment|// is true sooner Camel will continue
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
comment|// stop Camel after use
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
comment|/**      * Services for blogs      */
DECL|class|BlogService
specifier|public
class|class
name|BlogService
block|{
comment|/**          * Tests the blogs if its a good blog entry or not          */
DECL|method|isGoodBlog (Exchange exchange)
specifier|public
name|boolean
name|isGoodBlog
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Entry
name|entry
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Entry
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|title
init|=
name|entry
operator|.
name|getTitle
argument_list|()
decl_stmt|;
comment|// We like blogs about Camel
name|boolean
name|good
init|=
name|title
operator|.
name|toLowerCase
argument_list|()
operator|.
name|contains
argument_list|(
literal|"camel"
argument_list|)
decl_stmt|;
return|return
name|good
return|;
block|}
block|}
comment|// END SNIPPET: e1
block|}
end_class

end_unit

