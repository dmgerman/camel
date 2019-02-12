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
name|Endpoint
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
name|support
operator|.
name|service
operator|.
name|ServiceSupport
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
DECL|class|RemoveRouteStopEndpointTest
specifier|public
class|class
name|RemoveRouteStopEndpointTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testEndpointRegistryStopRouteEndpoints ()
specifier|public
name|void
name|testEndpointRegistryStopRouteEndpoints
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|seda
init|=
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"seda://foo"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|seda
argument_list|)
expr_stmt|;
name|Endpoint
name|log
init|=
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"log://bar"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|log
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should be started"
argument_list|,
operator|(
operator|(
name|ServiceSupport
operator|)
name|seda
operator|)
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should be started"
argument_list|,
operator|(
operator|(
name|ServiceSupport
operator|)
name|log
operator|)
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"seda:foo"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"seda:bar"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"log://foo"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"log://bar"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"mock://result"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"seda://stop"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"mock://stop"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
comment|// stop and remove bar route
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|stopRoute
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|context
operator|.
name|removeRoute
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"seda://foo"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"log://foo"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"seda://bar"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"log://bar"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"mock://result"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"seda://stop"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"mock://stop"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should be started"
argument_list|,
operator|(
operator|(
name|ServiceSupport
operator|)
name|seda
operator|)
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should be stopped"
argument_list|,
operator|(
operator|(
name|ServiceSupport
operator|)
name|log
operator|)
operator|.
name|isStopped
argument_list|()
argument_list|)
expr_stmt|;
comment|// stop and remove baz route
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|stopRoute
argument_list|(
literal|"baz"
argument_list|)
expr_stmt|;
name|context
operator|.
name|removeRoute
argument_list|(
literal|"baz"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"seda://foo"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"log://foo"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"seda://bar"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"log://bar"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"mock://result"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"seda://stop"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"mock://stop"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
comment|// stop and remove foo route
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|stopRoute
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|context
operator|.
name|removeRoute
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"seda://foo"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"log://foo"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"seda://bar"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"log://bar"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"mock://result"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"seda://stop"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"mock://stop"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Should not be started"
argument_list|,
operator|(
operator|(
name|ServiceSupport
operator|)
name|seda
operator|)
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Should not be started"
argument_list|,
operator|(
operator|(
name|ServiceSupport
operator|)
name|log
operator|)
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testEndpointRegistryStopRouteEndpointsContextStop ()
specifier|public
name|void
name|testEndpointRegistryStopRouteEndpointsContextStop
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|seda
init|=
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"seda://foo"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|seda
argument_list|)
expr_stmt|;
name|Endpoint
name|log
init|=
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"log://bar"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|log
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should be started"
argument_list|,
operator|(
operator|(
name|ServiceSupport
operator|)
name|seda
operator|)
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should be started"
argument_list|,
operator|(
operator|(
name|ServiceSupport
operator|)
name|log
operator|)
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"seda://foo"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"log://foo"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"seda://bar"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"log://bar"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"mock://result"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"seda://stop"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"mock://stop"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
comment|// stop and remove bar route
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|stopRoute
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|context
operator|.
name|removeRoute
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should be started"
argument_list|,
operator|(
operator|(
name|ServiceSupport
operator|)
name|seda
operator|)
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should be stopped"
argument_list|,
operator|(
operator|(
name|ServiceSupport
operator|)
name|log
operator|)
operator|.
name|isStopped
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"seda:foo"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"log://foo"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"seda://bar"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"log://bar"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"mock://result"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"seda://stop"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"mock://stop"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
comment|// stop and remove baz route
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|stopRoute
argument_list|(
literal|"baz"
argument_list|)
expr_stmt|;
name|context
operator|.
name|removeRoute
argument_list|(
literal|"baz"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"seda://foo"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"log://foo"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"seda://bar"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"log://bar"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"mock://result"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"seda://stop"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"mock://stop"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
comment|// stop camel which should stop the endpoint
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Should not be started"
argument_list|,
operator|(
operator|(
name|ServiceSupport
operator|)
name|seda
operator|)
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Should not be started"
argument_list|,
operator|(
operator|(
name|ServiceSupport
operator|)
name|log
operator|)
operator|.
name|isStarted
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
literal|"seda:foo"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:bar"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:bar"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:bar"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:stop"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"baz"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:stop"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit
