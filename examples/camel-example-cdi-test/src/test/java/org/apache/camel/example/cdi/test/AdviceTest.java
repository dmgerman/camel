begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.cdi.test
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|cdi
operator|.
name|test
package|;
end_package

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
name|javax
operator|.
name|enterprise
operator|.
name|event
operator|.
name|Observes
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
name|AdviceWithRouteBuilder
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
name|cdi
operator|.
name|Uri
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
name|model
operator|.
name|ModelCamelContext
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
name|reifier
operator|.
name|RouteReifier
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
name|spi
operator|.
name|CamelEvent
operator|.
name|CamelContextStartingEvent
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
name|cdi
operator|.
name|CamelCdiRunner
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|ClassRule
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
import|import
name|org
operator|.
name|junit
operator|.
name|rules
operator|.
name|Verifier
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import static
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
operator|.
name|assertIsSatisfied
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|CamelCdiRunner
operator|.
name|class
argument_list|)
DECL|class|AdviceTest
specifier|public
class|class
name|AdviceTest
block|{
annotation|@
name|ClassRule
DECL|field|verifier
specifier|public
specifier|static
name|MessageVerifier
name|verifier
init|=
operator|new
name|MessageVerifier
argument_list|()
decl_stmt|;
DECL|method|advice (@bserves CamelContextStartingEvent event, @Uri(R) MockEndpoint messages, ModelCamelContext context)
name|void
name|advice
parameter_list|(
annotation|@
name|Observes
name|CamelContextStartingEvent
name|event
parameter_list|,
annotation|@
name|Uri
argument_list|(
literal|"mock:messages"
argument_list|)
name|MockEndpoint
name|messages
parameter_list|,
name|ModelCamelContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
name|messages
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|messages
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello"
argument_list|,
literal|"Bye"
argument_list|)
expr_stmt|;
name|verifier
operator|.
name|messages
operator|=
name|messages
expr_stmt|;
name|RouteReifier
operator|.
name|adviceWith
argument_list|(
name|context
operator|.
name|getRouteDefinition
argument_list|(
literal|"route"
argument_list|)
argument_list|,
name|context
argument_list|,
operator|new
name|AdviceWithRouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|weaveAddLast
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:messages"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|test ()
specifier|public
name|void
name|test
parameter_list|()
block|{     }
DECL|class|MessageVerifier
specifier|private
specifier|static
class|class
name|MessageVerifier
extends|extends
name|Verifier
block|{
DECL|field|messages
name|MockEndpoint
name|messages
decl_stmt|;
annotation|@
name|Override
DECL|method|verify ()
specifier|protected
name|void
name|verify
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|assertIsSatisfied
argument_list|(
literal|2L
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|,
name|messages
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

