begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|awaitility
operator|.
name|Awaitility
operator|.
name|await
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
name|util
operator|.
name|EventObject
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
name|CountDownLatch
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
name|TimeUnit
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
name|management
operator|.
name|event
operator|.
name|RouteAddedEvent
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
name|EventNotifierSupport
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
name|FileUtil
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
DECL|class|FileWatcherReloadStrategyTest
specifier|public
class|class
name|FileWatcherReloadStrategyTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|reloadStrategy
specifier|private
name|FileWatcherReloadStrategy
name|reloadStrategy
decl_stmt|;
annotation|@
name|Override
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|reloadStrategy
operator|=
operator|new
name|FileWatcherReloadStrategy
argument_list|()
expr_stmt|;
name|reloadStrategy
operator|.
name|setFolder
argument_list|(
literal|"target/dummy"
argument_list|)
expr_stmt|;
comment|// to make unit test faster
name|reloadStrategy
operator|.
name|setPollTimeout
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|context
operator|.
name|setReloadStrategy
argument_list|(
name|reloadStrategy
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
annotation|@
name|Test
DECL|method|testAddNewRoute ()
specifier|public
name|void
name|testAddNewRoute
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteDirectory
argument_list|(
literal|"target/dummy"
argument_list|)
expr_stmt|;
name|createDirectory
argument_list|(
literal|"target/dummy"
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// there are 0 routes to begin with
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|context
operator|.
name|getRoutes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Copying file to target/dummy"
argument_list|)
expr_stmt|;
comment|// create an xml file with some routes
name|FileUtil
operator|.
name|copyFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"src/test/resources/org/apache/camel/model/barRoute.xml"
argument_list|)
argument_list|,
operator|new
name|File
argument_list|(
literal|"target/dummy/barRoute.xml"
argument_list|)
argument_list|)
expr_stmt|;
comment|// wait for that file to be processed
comment|// (is slow on osx, so wait up till 20 seconds)
name|await
argument_list|()
operator|.
name|atMost
argument_list|(
literal|20
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
operator|.
name|untilAsserted
argument_list|(
parameter_list|()
lambda|->
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|context
operator|.
name|getRoutes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// and the route should work
name|getMockEndpoint
argument_list|(
literal|"mock:bar"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:bar"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUpdateExistingRoute ()
specifier|public
name|void
name|testUpdateExistingRoute
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteDirectory
argument_list|(
literal|"target/dummy"
argument_list|)
expr_stmt|;
name|createDirectory
argument_list|(
literal|"target/dummy"
argument_list|)
expr_stmt|;
comment|// the bar route is added two times, at first, and then when updated
specifier|final
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|context
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|addEventNotifier
argument_list|(
operator|new
name|EventNotifierSupport
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|notify
parameter_list|(
name|EventObject
name|event
parameter_list|)
throws|throws
name|Exception
block|{
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isEnabled
parameter_list|(
name|EventObject
name|event
parameter_list|)
block|{
return|return
name|event
operator|instanceof
name|RouteAddedEvent
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
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
literal|"direct:bar"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:foo"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|context
operator|.
name|getRoutes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// and the route should work sending to mock:foo
name|getMockEndpoint
argument_list|(
literal|"mock:bar"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:foo"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:bar"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|resetMocks
argument_list|()
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Copying file to target/dummy"
argument_list|)
expr_stmt|;
comment|// create an xml file with some routes
name|FileUtil
operator|.
name|copyFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"src/test/resources/org/apache/camel/model/barRoute.xml"
argument_list|)
argument_list|,
operator|new
name|File
argument_list|(
literal|"target/dummy/barRoute.xml"
argument_list|)
argument_list|)
expr_stmt|;
comment|// wait for that file to be processed and remove/add the route
comment|// (is slow on osx, so wait up till 20 seconds)
name|boolean
name|done
init|=
name|latch
operator|.
name|await
argument_list|(
literal|20
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should reload file within 20 seconds"
argument_list|,
name|done
argument_list|)
expr_stmt|;
comment|// and the route should be changed to route to mock:bar instead of mock:foo
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:bar"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:foo"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:bar"
argument_list|,
literal|"Bye World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUpdateXmlRoute ()
specifier|public
name|void
name|testUpdateXmlRoute
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteDirectory
argument_list|(
literal|"target/dummy"
argument_list|)
expr_stmt|;
name|createDirectory
argument_list|(
literal|"target/dummy"
argument_list|)
expr_stmt|;
comment|// the bar route is added two times, at first, and then when updated
specifier|final
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|context
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|addEventNotifier
argument_list|(
operator|new
name|EventNotifierSupport
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|notify
parameter_list|(
name|EventObject
name|event
parameter_list|)
throws|throws
name|Exception
block|{
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isEnabled
parameter_list|(
name|EventObject
name|event
parameter_list|)
block|{
return|return
name|event
operator|instanceof
name|RouteAddedEvent
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// there are 0 routes to begin with
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|context
operator|.
name|getRoutes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Copying file to target/dummy"
argument_list|)
expr_stmt|;
comment|// create an xml file with some routes
name|FileUtil
operator|.
name|copyFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"src/test/resources/org/apache/camel/model/barRoute.xml"
argument_list|)
argument_list|,
operator|new
name|File
argument_list|(
literal|"target/dummy/barRoute.xml"
argument_list|)
argument_list|)
expr_stmt|;
comment|// wait for that file to be processed
comment|// (is slow on osx, so wait up till 20 seconds)
name|await
argument_list|()
operator|.
name|atMost
argument_list|(
literal|20
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
operator|.
name|untilAsserted
argument_list|(
parameter_list|()
lambda|->
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|context
operator|.
name|getRoutes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// and the route should work
name|getMockEndpoint
argument_list|(
literal|"mock:bar"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:bar"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|resetMocks
argument_list|()
expr_stmt|;
comment|// now update the file
name|log
operator|.
name|info
argument_list|(
literal|"Updating file in target/dummy"
argument_list|)
expr_stmt|;
comment|// create an xml file with some routes
name|FileUtil
operator|.
name|copyFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"src/test/resources/org/apache/camel/model/barUpdatedRoute.xml"
argument_list|)
argument_list|,
operator|new
name|File
argument_list|(
literal|"target/dummy/barRoute.xml"
argument_list|)
argument_list|)
expr_stmt|;
comment|// wait for that file to be processed and remove/add the route
comment|// (is slow on osx, so wait up till 20 seconds)
name|boolean
name|done
init|=
name|latch
operator|.
name|await
argument_list|(
literal|20
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should reload file within 20 seconds"
argument_list|,
name|done
argument_list|)
expr_stmt|;
comment|// and the route should work with the update
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:bar"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Bye Camel"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:bar"
argument_list|,
literal|"Camel"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

