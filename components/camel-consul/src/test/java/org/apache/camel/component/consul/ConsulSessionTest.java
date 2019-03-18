begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.consul
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|consul
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
name|UUID
import|;
end_import

begin_import
import|import
name|com
operator|.
name|orbitz
operator|.
name|consul
operator|.
name|model
operator|.
name|session
operator|.
name|ImmutableSession
import|;
end_import

begin_import
import|import
name|com
operator|.
name|orbitz
operator|.
name|consul
operator|.
name|model
operator|.
name|session
operator|.
name|SessionCreatedResponse
import|;
end_import

begin_import
import|import
name|com
operator|.
name|orbitz
operator|.
name|consul
operator|.
name|model
operator|.
name|session
operator|.
name|SessionInfo
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
name|consul
operator|.
name|endpoint
operator|.
name|ConsulSessionActions
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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
DECL|class|ConsulSessionTest
specifier|public
class|class
name|ConsulSessionTest
extends|extends
name|ConsulTestSupport
block|{
annotation|@
name|Test
DECL|method|testServiceInstance ()
specifier|public
name|void
name|testServiceInstance
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|name
init|=
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
specifier|final
name|int
name|sessions
init|=
name|getConsul
argument_list|()
operator|.
name|sessionClient
argument_list|()
operator|.
name|listSessions
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
block|{
name|List
argument_list|<
name|SessionInfo
argument_list|>
name|list
init|=
name|fluentTemplate
argument_list|()
operator|.
name|withHeader
argument_list|(
name|ConsulConstants
operator|.
name|CONSUL_ACTION
argument_list|,
name|ConsulSessionActions
operator|.
name|LIST
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:consul"
argument_list|)
operator|.
name|request
argument_list|(
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|sessions
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|list
operator|.
name|stream
argument_list|()
operator|.
name|anyMatch
argument_list|(
name|s
lambda|->
name|s
operator|.
name|getName
argument_list|()
operator|.
name|isPresent
argument_list|()
operator|&&
name|s
operator|.
name|getName
argument_list|()
operator|.
name|get
argument_list|()
operator|.
name|equals
argument_list|(
name|name
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|SessionCreatedResponse
name|res
init|=
name|fluentTemplate
argument_list|()
operator|.
name|withHeader
argument_list|(
name|ConsulConstants
operator|.
name|CONSUL_ACTION
argument_list|,
name|ConsulSessionActions
operator|.
name|CREATE
argument_list|)
operator|.
name|withBody
argument_list|(
name|ImmutableSession
operator|.
name|builder
argument_list|()
operator|.
name|name
argument_list|(
name|name
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:consul"
argument_list|)
operator|.
name|request
argument_list|(
name|SessionCreatedResponse
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|res
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|{
name|List
argument_list|<
name|SessionInfo
argument_list|>
name|list
init|=
name|fluentTemplate
argument_list|()
operator|.
name|withHeader
argument_list|(
name|ConsulConstants
operator|.
name|CONSUL_ACTION
argument_list|,
name|ConsulSessionActions
operator|.
name|LIST
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:consul"
argument_list|)
operator|.
name|request
argument_list|(
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|sessions
operator|+
literal|1
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|list
operator|.
name|stream
argument_list|()
operator|.
name|anyMatch
argument_list|(
name|s
lambda|->
name|s
operator|.
name|getName
argument_list|()
operator|.
name|isPresent
argument_list|()
operator|&&
name|s
operator|.
name|getName
argument_list|()
operator|.
name|get
argument_list|()
operator|.
name|equals
argument_list|(
name|name
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|{
name|fluentTemplate
argument_list|()
operator|.
name|withHeader
argument_list|(
name|ConsulConstants
operator|.
name|CONSUL_ACTION
argument_list|,
name|ConsulSessionActions
operator|.
name|DESTROY
argument_list|)
operator|.
name|withHeader
argument_list|(
name|ConsulConstants
operator|.
name|CONSUL_SESSION
argument_list|,
name|res
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:consul"
argument_list|)
operator|.
name|send
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|SessionInfo
argument_list|>
name|list
init|=
name|fluentTemplate
argument_list|()
operator|.
name|withHeader
argument_list|(
name|ConsulConstants
operator|.
name|CONSUL_ACTION
argument_list|,
name|ConsulSessionActions
operator|.
name|LIST
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:consul"
argument_list|)
operator|.
name|request
argument_list|(
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|sessions
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|list
operator|.
name|stream
argument_list|()
operator|.
name|anyMatch
argument_list|(
name|s
lambda|->
name|s
operator|.
name|getName
argument_list|()
operator|.
name|isPresent
argument_list|()
operator|&&
name|s
operator|.
name|getName
argument_list|()
operator|.
name|get
argument_list|()
operator|.
name|equals
argument_list|(
name|name
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
block|{
name|from
argument_list|(
literal|"direct:consul"
argument_list|)
operator|.
name|to
argument_list|(
literal|"consul:session"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

