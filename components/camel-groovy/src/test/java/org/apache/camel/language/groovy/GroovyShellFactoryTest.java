begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language.groovy
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
operator|.
name|groovy
package|;
end_package

begin_import
import|import
name|groovy
operator|.
name|lang
operator|.
name|GroovyShell
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
name|DefaultExchange
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
name|CamelTestSupport
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
name|org
operator|.
name|mockito
operator|.
name|ArgumentMatchers
operator|.
name|any
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|BDDMockito
operator|.
name|given
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|mock
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|verify
import|;
end_import

begin_class
DECL|class|GroovyShellFactoryTest
specifier|public
class|class
name|GroovyShellFactoryTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testExpressionReturnsTheCorrectValue ()
specifier|public
name|void
name|testExpressionReturnsTheCorrectValue
parameter_list|()
block|{
comment|// Given
name|GroovyShellFactory
name|groovyShellFactory
init|=
name|mock
argument_list|(
name|GroovyShellFactory
operator|.
name|class
argument_list|)
decl_stmt|;
name|given
argument_list|(
name|groovyShellFactory
operator|.
name|createGroovyShell
argument_list|(
name|any
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|willReturn
argument_list|(
operator|new
name|GroovyShell
argument_list|()
argument_list|)
expr_stmt|;
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
literal|"groovyShellFactory"
argument_list|,
name|groovyShellFactory
argument_list|)
expr_stmt|;
name|CamelContext
name|camelContext
init|=
operator|new
name|DefaultCamelContext
argument_list|(
name|registry
argument_list|)
decl_stmt|;
comment|// When
name|assertExpression
argument_list|(
name|GroovyLanguage
operator|.
name|groovy
argument_list|(
literal|"exchange.in.body"
argument_list|)
argument_list|,
operator|new
name|DefaultExchange
argument_list|(
name|camelContext
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// Then
name|verify
argument_list|(
name|groovyShellFactory
argument_list|)
operator|.
name|createGroovyShell
argument_list|(
name|any
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

