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
name|Optional
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
name|ConsulKeyValueActions
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
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertTrue
import|;
end_import

begin_class
DECL|class|ConsulKeyValueTest
specifier|public
class|class
name|ConsulKeyValueTest
extends|extends
name|ConsulTestSupport
block|{
annotation|@
name|Test
DECL|method|testKeyPut ()
specifier|public
name|void
name|testKeyPut
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|key
init|=
name|generateKey
argument_list|()
decl_stmt|;
name|String
name|val
init|=
name|generateRandomString
argument_list|()
decl_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:kv"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
name|val
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
name|ConsulConstants
operator|.
name|CONSUL_RESULT
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|fluentTemplate
argument_list|()
operator|.
name|withHeader
argument_list|(
name|ConsulConstants
operator|.
name|CONSUL_ACTION
argument_list|,
name|ConsulKeyValueActions
operator|.
name|PUT
argument_list|)
operator|.
name|withHeader
argument_list|(
name|ConsulConstants
operator|.
name|CONSUL_KEY
argument_list|,
name|key
argument_list|)
operator|.
name|withBody
argument_list|(
name|val
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:kv"
argument_list|)
operator|.
name|send
argument_list|()
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|Optional
argument_list|<
name|String
argument_list|>
name|keyVal
init|=
name|getConsul
argument_list|()
operator|.
name|keyValueClient
argument_list|()
operator|.
name|getValueAsString
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|keyVal
operator|.
name|isPresent
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|val
argument_list|,
name|keyVal
operator|.
name|get
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
literal|"direct:kv"
argument_list|)
operator|.
name|to
argument_list|(
literal|"consul:kv"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:kv"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

