begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cometd
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cometd
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
name|After
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
comment|/**  * Unit testing for using a CometdProducer and a CometdConsumer  */
end_comment

begin_class
DECL|class|CometdProducerConsumerTest
specifier|public
class|class
name|CometdProducerConsumerTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|URI
specifier|private
specifier|static
specifier|final
name|String
name|URI
init|=
literal|"cometd://127.0.0.1:9080/service/test?baseResource=file:./target/test-classes/webapp&"
operator|+
literal|"timeout=240000&interval=0&maxInterval=30000&multiFrameInterval=1500&jsonCommented=true&logLevel=2"
decl_stmt|;
annotation|@
name|Test
DECL|method|testProducer ()
specifier|public
name|void
name|testProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|Person
name|person
init|=
operator|new
name|Person
argument_list|(
literal|"David"
argument_list|,
literal|"Greco"
argument_list|)
decl_stmt|;
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:input"
argument_list|,
name|person
argument_list|)
expr_stmt|;
name|MockEndpoint
name|ep
init|=
operator|(
name|MockEndpoint
operator|)
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:test"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|exchanges
init|=
name|ep
operator|.
name|getReceivedExchanges
argument_list|()
decl_stmt|;
for|for
control|(
name|Exchange
name|exchange
range|:
name|exchanges
control|)
block|{
name|Person
name|person1
init|=
operator|(
name|Person
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"David"
argument_list|,
name|person1
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Greco"
argument_list|,
name|person1
operator|.
name|getSurname
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
name|stopCamelContext
argument_list|()
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
literal|"direct:input"
argument_list|)
operator|.
name|to
argument_list|(
name|URI
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|URI
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:test"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|Person
specifier|public
specifier|static
class|class
name|Person
block|{
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
DECL|field|surname
specifier|private
name|String
name|surname
decl_stmt|;
DECL|method|Person (String name, String surname)
name|Person
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|surname
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|surname
operator|=
name|surname
expr_stmt|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|getSurname ()
specifier|public
name|String
name|getSurname
parameter_list|()
block|{
return|return
name|surname
return|;
block|}
DECL|method|setName (String name)
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
DECL|method|setSurname (String surname)
specifier|public
name|void
name|setSurname
parameter_list|(
name|String
name|surname
parameter_list|)
block|{
name|this
operator|.
name|surname
operator|=
name|surname
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

