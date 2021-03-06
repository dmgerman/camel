begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.cloud
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|cloud
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

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
name|stream
operator|.
name|Collectors
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
name|cloud
operator|.
name|ServiceDefinition
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

begin_import
import|import static
name|junit
operator|.
name|framework
operator|.
name|TestCase
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|junit
operator|.
name|framework
operator|.
name|TestCase
operator|.
name|assertFalse
import|;
end_import

begin_import
import|import static
name|junit
operator|.
name|framework
operator|.
name|TestCase
operator|.
name|assertNull
import|;
end_import

begin_import
import|import static
name|junit
operator|.
name|framework
operator|.
name|TestCase
operator|.
name|assertTrue
import|;
end_import

begin_class
DECL|class|ServiceDefinitionTest
specifier|public
class|class
name|ServiceDefinitionTest
block|{
annotation|@
name|Test
DECL|method|testParse ()
specifier|public
name|void
name|testParse
parameter_list|()
block|{
name|List
argument_list|<
name|ServiceDefinition
argument_list|>
name|definitions
init|=
name|DefaultServiceDefinition
operator|.
name|parse
argument_list|(
literal|"svc1@host:2001,myId/svc1@host:2001"
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|definitions
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|definitions
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
name|assertEquals
argument_list|(
literal|"svc1"
argument_list|,
name|definitions
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"host"
argument_list|,
name|definitions
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2001
argument_list|,
name|definitions
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"myId"
argument_list|,
name|definitions
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
name|assertEquals
argument_list|(
literal|"svc1"
argument_list|,
name|definitions
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"host"
argument_list|,
name|definitions
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2001
argument_list|,
name|definitions
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMatch ()
specifier|public
name|void
name|testMatch
parameter_list|()
block|{
name|List
argument_list|<
name|ServiceDefinition
argument_list|>
name|definitions
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|definitions
operator|.
name|add
argument_list|(
name|DefaultServiceDefinition
operator|.
name|builder
argument_list|()
operator|.
name|withName
argument_list|(
literal|"service-1"
argument_list|)
operator|.
name|withHost
argument_list|(
literal|"host-1.domain1.com"
argument_list|)
operator|.
name|withPort
argument_list|(
literal|2001
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
name|definitions
operator|.
name|add
argument_list|(
name|DefaultServiceDefinition
operator|.
name|builder
argument_list|()
operator|.
name|withName
argument_list|(
literal|"service-2"
argument_list|)
operator|.
name|withHost
argument_list|(
literal|"host-2.domain1.com"
argument_list|)
operator|.
name|withPort
argument_list|(
literal|2001
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
name|definitions
operator|.
name|add
argument_list|(
name|DefaultServiceDefinition
operator|.
name|builder
argument_list|()
operator|.
name|withName
argument_list|(
literal|"service-3"
argument_list|)
operator|.
name|withHost
argument_list|(
literal|"host-3.domain1.com"
argument_list|)
operator|.
name|withPort
argument_list|(
literal|2001
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
name|definitions
operator|.
name|add
argument_list|(
name|DefaultServiceDefinition
operator|.
name|builder
argument_list|()
operator|.
name|withName
argument_list|(
literal|"service-4"
argument_list|)
operator|.
name|withHost
argument_list|(
literal|"host-3.domain2.com"
argument_list|)
operator|.
name|withPort
argument_list|(
literal|2001
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|DefaultServiceDefinition
operator|.
name|builder
argument_list|()
operator|.
name|withName
argument_list|(
literal|"*"
argument_list|)
operator|.
name|withHost
argument_list|(
literal|".*\\.domain1\\.com"
argument_list|)
operator|.
name|withPort
argument_list|(
literal|2001
argument_list|)
operator|.
name|build
argument_list|()
operator|.
name|matches
argument_list|(
name|definitions
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|DefaultServiceDefinition
operator|.
name|builder
argument_list|()
operator|.
name|withName
argument_list|(
literal|"service-1"
argument_list|)
operator|.
name|withHost
argument_list|(
literal|"host-1.domain1.com"
argument_list|)
operator|.
name|withPort
argument_list|(
literal|2001
argument_list|)
operator|.
name|build
argument_list|()
operator|.
name|matches
argument_list|(
name|definitions
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|DefaultServiceDefinition
operator|.
name|builder
argument_list|()
operator|.
name|withName
argument_list|(
literal|"service-.*"
argument_list|)
operator|.
name|withHost
argument_list|(
literal|"host-1.domain1.com"
argument_list|)
operator|.
name|withPort
argument_list|(
literal|2001
argument_list|)
operator|.
name|build
argument_list|()
operator|.
name|matches
argument_list|(
name|definitions
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|DefaultServiceDefinition
operator|.
name|builder
argument_list|()
operator|.
name|withName
argument_list|(
literal|"service-.*"
argument_list|)
operator|.
name|withHost
argument_list|(
literal|"host-.*\\.domain.*\\.com"
argument_list|)
operator|.
name|withPort
argument_list|(
literal|2001
argument_list|)
operator|.
name|build
argument_list|()
operator|.
name|matches
argument_list|(
name|definitions
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|DefaultServiceDefinition
operator|.
name|builder
argument_list|()
operator|.
name|withName
argument_list|(
literal|"service-.*"
argument_list|)
operator|.
name|withHost
argument_list|(
literal|"host-.*\\.domain.*\\.com"
argument_list|)
operator|.
name|withPort
argument_list|(
literal|2001
argument_list|)
operator|.
name|build
argument_list|()
operator|.
name|matches
argument_list|(
name|definitions
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|DefaultServiceDefinition
operator|.
name|builder
argument_list|()
operator|.
name|withName
argument_list|(
literal|"service-.*"
argument_list|)
operator|.
name|withHost
argument_list|(
literal|"host-.*\\.domain.*\\.com"
argument_list|)
operator|.
name|withPort
argument_list|(
literal|2001
argument_list|)
operator|.
name|build
argument_list|()
operator|.
name|matches
argument_list|(
name|definitions
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|DefaultServiceDefinition
operator|.
name|builder
argument_list|()
operator|.
name|withName
argument_list|(
literal|"service-.*"
argument_list|)
operator|.
name|withHost
argument_list|(
literal|"host-.*\\.domain.*\\.com"
argument_list|)
operator|.
name|withPort
argument_list|(
literal|2001
argument_list|)
operator|.
name|build
argument_list|()
operator|.
name|matches
argument_list|(
name|definitions
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|DefaultServiceDefinition
operator|.
name|builder
argument_list|()
operator|.
name|withName
argument_list|(
literal|"service-.*"
argument_list|)
operator|.
name|withHost
argument_list|(
literal|"host-1.domain1.com"
argument_list|)
operator|.
name|withPort
argument_list|(
literal|2001
argument_list|)
operator|.
name|build
argument_list|()
operator|.
name|matches
argument_list|(
name|definitions
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|DefaultServiceDefinition
operator|.
name|builder
argument_list|()
operator|.
name|withName
argument_list|(
literal|"*"
argument_list|)
operator|.
name|withHost
argument_list|(
literal|".*\\.domain1\\.com"
argument_list|)
operator|.
name|withPort
argument_list|(
literal|2001
argument_list|)
operator|.
name|build
argument_list|()
operator|.
name|matches
argument_list|(
name|definitions
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|DefaultServiceDefinition
operator|.
name|builder
argument_list|()
operator|.
name|withName
argument_list|(
literal|"*"
argument_list|)
operator|.
name|withHost
argument_list|(
literal|".*\\.domain1\\.com"
argument_list|)
operator|.
name|withPort
argument_list|(
literal|2002
argument_list|)
operator|.
name|build
argument_list|()
operator|.
name|matches
argument_list|(
name|definitions
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

