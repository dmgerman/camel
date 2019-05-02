begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|model
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
name|rest
operator|.
name|DummyRestConsumerFactory
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
name|rest
operator|.
name|DummyRestProcessorFactory
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
name|JndiRegistry
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
name|RouteIdFactory
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
DECL|class|RouteIdFactoryTest
specifier|public
class|class
name|RouteIdFactoryTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|jndi
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"dummy-rest"
argument_list|,
operator|new
name|DummyRestConsumerFactory
argument_list|()
argument_list|)
expr_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"dummy-rest-api"
argument_list|,
operator|new
name|DummyRestProcessorFactory
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
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
name|context
operator|.
name|setNodeIdFactory
argument_list|(
operator|new
name|RouteIdFactory
argument_list|()
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start1?timeout=30000"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start2"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|rest
argument_list|(
literal|"/say/hello"
argument_list|)
operator|.
name|get
argument_list|(
literal|"/bar"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|rest
argument_list|(
literal|"/say/hello"
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|rest
argument_list|()
operator|.
name|get
argument_list|(
literal|"/hello"
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
annotation|@
name|Test
DECL|method|testDirectRouteIdWithOptions ()
specifier|public
name|void
name|testDirectRouteIdWithOptions
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"start1"
argument_list|,
name|context
operator|.
name|getRouteDefinitions
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDirectRouteId ()
specifier|public
name|void
name|testDirectRouteId
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"start2"
argument_list|,
name|context
operator|.
name|getRouteDefinitions
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRestRouteIdWithVerbUri ()
specifier|public
name|void
name|testRestRouteIdWithVerbUri
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"get-say-hello-bar"
argument_list|,
name|context
operator|.
name|getRouteDefinitions
argument_list|()
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRestRouteIdWithoutVerbUri ()
specifier|public
name|void
name|testRestRouteIdWithoutVerbUri
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"get-say-hello"
argument_list|,
name|context
operator|.
name|getRouteDefinitions
argument_list|()
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRestRouteIdWithoutPathUri ()
specifier|public
name|void
name|testRestRouteIdWithoutPathUri
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"get-hello"
argument_list|,
name|context
operator|.
name|getRouteDefinitions
argument_list|()
operator|.
name|get
argument_list|(
literal|4
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

