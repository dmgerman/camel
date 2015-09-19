begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.commands.jolokia
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|commands
operator|.
name|jolokia
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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

begin_class
annotation|@
name|Ignore
argument_list|(
literal|"used for manual testing"
argument_list|)
DECL|class|JolokiaRemoteTest
specifier|public
class|class
name|JolokiaRemoteTest
block|{
DECL|field|url
specifier|private
name|String
name|url
init|=
literal|"http://localhost:8080/jolokia"
decl_stmt|;
DECL|field|controller
specifier|private
name|JolokiaCamelController
name|controller
decl_stmt|;
annotation|@
name|Test
DECL|method|testPing ()
specifier|public
name|void
name|testPing
parameter_list|()
throws|throws
name|Exception
block|{
name|controller
operator|=
operator|new
name|DefaultJolokiaCamelController
argument_list|()
expr_stmt|;
name|controller
operator|.
name|connect
argument_list|(
name|url
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|boolean
name|pong
init|=
name|controller
operator|.
name|ping
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Ping responsed: "
operator|+
name|pong
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRemoteCamelContexts ()
specifier|public
name|void
name|testRemoteCamelContexts
parameter_list|()
throws|throws
name|Exception
block|{
name|controller
operator|=
operator|new
name|DefaultJolokiaCamelController
argument_list|()
expr_stmt|;
name|controller
operator|.
name|connect
argument_list|(
name|url
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|data
init|=
name|controller
operator|.
name|getCamelContexts
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRemoteCamelContextInformation ()
specifier|public
name|void
name|testRemoteCamelContextInformation
parameter_list|()
throws|throws
name|Exception
block|{
name|controller
operator|=
operator|new
name|DefaultJolokiaCamelController
argument_list|()
expr_stmt|;
name|controller
operator|.
name|connect
argument_list|(
name|url
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|data
init|=
name|controller
operator|.
name|getCamelContextInformation
argument_list|(
literal|"camel-1"
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRemoteCamelContextStatsAsXml ()
specifier|public
name|void
name|testRemoteCamelContextStatsAsXml
parameter_list|()
throws|throws
name|Exception
block|{
name|controller
operator|=
operator|new
name|DefaultJolokiaCamelController
argument_list|()
expr_stmt|;
name|controller
operator|.
name|connect
argument_list|(
name|url
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|String
name|data
init|=
name|controller
operator|.
name|getCamelContextStatsAsXml
argument_list|(
literal|"camel-1"
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRemoteCamelContextControl ()
specifier|public
name|void
name|testRemoteCamelContextControl
parameter_list|()
throws|throws
name|Exception
block|{
name|controller
operator|=
operator|new
name|DefaultJolokiaCamelController
argument_list|()
expr_stmt|;
name|controller
operator|.
name|connect
argument_list|(
name|url
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|controller
operator|.
name|suspendContext
argument_list|(
literal|"camel-1"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|data
init|=
name|controller
operator|.
name|getCamelContextInformation
argument_list|(
literal|"camel-1"
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|controller
operator|.
name|resumeContext
argument_list|(
literal|"camel-1"
argument_list|)
expr_stmt|;
name|data
operator|=
name|controller
operator|.
name|getCamelContextInformation
argument_list|(
literal|"camel-1"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRemoteGetAllRoutes ()
specifier|public
name|void
name|testRemoteGetAllRoutes
parameter_list|()
throws|throws
name|Exception
block|{
name|controller
operator|=
operator|new
name|DefaultJolokiaCamelController
argument_list|()
expr_stmt|;
name|controller
operator|.
name|connect
argument_list|(
name|url
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|data
init|=
name|controller
operator|.
name|getRoutes
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRemoteGetRoutes ()
specifier|public
name|void
name|testRemoteGetRoutes
parameter_list|()
throws|throws
name|Exception
block|{
name|controller
operator|=
operator|new
name|DefaultJolokiaCamelController
argument_list|()
expr_stmt|;
name|controller
operator|.
name|connect
argument_list|(
name|url
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|data
init|=
name|controller
operator|.
name|getRoutes
argument_list|(
literal|"camel-1"
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRemoteGetRoutesFilter ()
specifier|public
name|void
name|testRemoteGetRoutesFilter
parameter_list|()
throws|throws
name|Exception
block|{
name|controller
operator|=
operator|new
name|DefaultJolokiaCamelController
argument_list|()
expr_stmt|;
name|controller
operator|.
name|connect
argument_list|(
name|url
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|data
init|=
name|controller
operator|.
name|getRoutes
argument_list|(
literal|null
argument_list|,
literal|"route2"
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRemoteResetRouteStats ()
specifier|public
name|void
name|testRemoteResetRouteStats
parameter_list|()
throws|throws
name|Exception
block|{
name|controller
operator|=
operator|new
name|DefaultJolokiaCamelController
argument_list|()
expr_stmt|;
name|controller
operator|.
name|connect
argument_list|(
name|url
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|controller
operator|.
name|resetRouteStats
argument_list|(
literal|"camel-1"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRemoteRouteControl ()
specifier|public
name|void
name|testRemoteRouteControl
parameter_list|()
throws|throws
name|Exception
block|{
name|controller
operator|=
operator|new
name|DefaultJolokiaCamelController
argument_list|()
expr_stmt|;
name|controller
operator|.
name|connect
argument_list|(
name|url
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|controller
operator|.
name|suspendRoute
argument_list|(
literal|"camel-1"
argument_list|,
literal|"route1"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|data
init|=
name|controller
operator|.
name|getRoutes
argument_list|(
literal|"camel-1"
argument_list|,
literal|"route1"
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|controller
operator|.
name|resumeRoute
argument_list|(
literal|"camel-1"
argument_list|,
literal|"route1"
argument_list|)
expr_stmt|;
name|data
operator|=
name|controller
operator|.
name|getRoutes
argument_list|(
literal|"camel-1"
argument_list|,
literal|"route1"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRouteModel ()
specifier|public
name|void
name|testRouteModel
parameter_list|()
throws|throws
name|Exception
block|{
name|controller
operator|=
operator|new
name|DefaultJolokiaCamelController
argument_list|()
expr_stmt|;
name|controller
operator|.
name|connect
argument_list|(
name|url
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|String
name|data
init|=
name|controller
operator|.
name|getRouteModelAsXml
argument_list|(
literal|"camel-1"
argument_list|,
literal|"route1"
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRouteStats ()
specifier|public
name|void
name|testRouteStats
parameter_list|()
throws|throws
name|Exception
block|{
name|controller
operator|=
operator|new
name|DefaultJolokiaCamelController
argument_list|()
expr_stmt|;
name|controller
operator|.
name|connect
argument_list|(
name|url
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|String
name|data
init|=
name|controller
operator|.
name|getRouteStatsAsXml
argument_list|(
literal|"camel-1"
argument_list|,
literal|"route1"
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRestsModel ()
specifier|public
name|void
name|testRestsModel
parameter_list|()
throws|throws
name|Exception
block|{
name|controller
operator|=
operator|new
name|DefaultJolokiaCamelController
argument_list|()
expr_stmt|;
name|controller
operator|.
name|connect
argument_list|(
name|url
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|String
name|data
init|=
name|controller
operator|.
name|getRestModelAsXml
argument_list|(
literal|"camel-1"
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetEndpoints ()
specifier|public
name|void
name|testGetEndpoints
parameter_list|()
throws|throws
name|Exception
block|{
name|controller
operator|=
operator|new
name|DefaultJolokiaCamelController
argument_list|()
expr_stmt|;
name|controller
operator|.
name|connect
argument_list|(
name|url
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|data
init|=
name|controller
operator|.
name|getEndpoints
argument_list|(
literal|"camel-1"
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetRestServices ()
specifier|public
name|void
name|testGetRestServices
parameter_list|()
throws|throws
name|Exception
block|{
name|controller
operator|=
operator|new
name|DefaultJolokiaCamelController
argument_list|()
expr_stmt|;
name|controller
operator|.
name|connect
argument_list|(
name|url
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|data
init|=
name|controller
operator|.
name|getRestServices
argument_list|(
literal|"camel-1"
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExplainEndpointJson ()
specifier|public
name|void
name|testExplainEndpointJson
parameter_list|()
throws|throws
name|Exception
block|{
name|controller
operator|=
operator|new
name|DefaultJolokiaCamelController
argument_list|()
expr_stmt|;
name|controller
operator|.
name|connect
argument_list|(
name|url
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|String
name|data
init|=
name|controller
operator|.
name|explainEndpointAsJSon
argument_list|(
literal|"camel-1"
argument_list|,
literal|"log:foo"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExplainEipJson ()
specifier|public
name|void
name|testExplainEipJson
parameter_list|()
throws|throws
name|Exception
block|{
name|controller
operator|=
operator|new
name|DefaultJolokiaCamelController
argument_list|()
expr_stmt|;
name|controller
operator|.
name|connect
argument_list|(
name|url
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|String
name|data
init|=
name|controller
operator|.
name|explainEipAsJSon
argument_list|(
literal|"camel-1"
argument_list|,
literal|"transform"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testListComponents ()
specifier|public
name|void
name|testListComponents
parameter_list|()
throws|throws
name|Exception
block|{
name|controller
operator|=
operator|new
name|DefaultJolokiaCamelController
argument_list|()
expr_stmt|;
name|controller
operator|.
name|connect
argument_list|(
name|url
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|data
init|=
name|controller
operator|.
name|listComponents
argument_list|(
literal|"camel-1"
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInflight ()
specifier|public
name|void
name|testInflight
parameter_list|()
throws|throws
name|Exception
block|{
name|controller
operator|=
operator|new
name|DefaultJolokiaCamelController
argument_list|()
expr_stmt|;
name|controller
operator|.
name|connect
argument_list|(
name|url
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|data
init|=
name|controller
operator|.
name|browseInflightExchanges
argument_list|(
literal|"camel-1"
argument_list|,
literal|500
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

