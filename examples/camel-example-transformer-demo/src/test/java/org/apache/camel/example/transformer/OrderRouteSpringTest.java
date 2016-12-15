begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.transformer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|transformer
package|;
end_package

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|ObjectMapper
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
name|EndpointInject
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
name|Processor
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
name|Produce
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
name|ProducerTemplate
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
name|example
operator|.
name|transformer
operator|.
name|demo
operator|.
name|Order
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
name|example
operator|.
name|transformer
operator|.
name|demo
operator|.
name|OrderResponse
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
name|spring
operator|.
name|CamelSpringDelegatingTestContextLoader
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
name|spring
operator|.
name|CamelSpringRunner
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
name|spring
operator|.
name|MockEndpointsAndSkip
import|;
end_import

begin_import
import|import
name|org
operator|.
name|custommonkey
operator|.
name|xmlunit
operator|.
name|XMLUnit
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|ContextConfiguration
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|CamelSpringRunner
operator|.
name|class
argument_list|)
annotation|@
name|ContextConfiguration
argument_list|(
name|value
operator|=
literal|"/META-INF/spring/camel-context.xml"
argument_list|,
name|loader
operator|=
name|CamelSpringDelegatingTestContextLoader
operator|.
name|class
argument_list|)
annotation|@
name|MockEndpointsAndSkip
argument_list|(
literal|"direct:csv"
argument_list|)
DECL|class|OrderRouteSpringTest
specifier|public
class|class
name|OrderRouteSpringTest
block|{
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:java"
argument_list|)
DECL|field|javaProducer
specifier|protected
name|ProducerTemplate
name|javaProducer
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:xml"
argument_list|)
DECL|field|xmlProducer
specifier|protected
name|ProducerTemplate
name|xmlProducer
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:json"
argument_list|)
DECL|field|jsonProducer
specifier|protected
name|ProducerTemplate
name|jsonProducer
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:direct:csv"
argument_list|)
DECL|field|mockCsv
specifier|private
name|MockEndpoint
name|mockCsv
decl_stmt|;
annotation|@
name|Before
DECL|method|before ()
specifier|public
name|void
name|before
parameter_list|()
block|{
name|mockCsv
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testJava ()
specifier|public
name|void
name|testJava
parameter_list|()
throws|throws
name|Exception
block|{
name|mockCsv
operator|.
name|whenAnyExchangeReceived
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Object
name|mockBody
init|=
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
name|Order
operator|.
name|class
argument_list|,
name|mockBody
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|Order
name|mockOrder
init|=
operator|(
name|Order
operator|)
name|mockBody
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Order-Java-0001"
argument_list|,
name|mockOrder
operator|.
name|getOrderId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"MILK"
argument_list|,
name|mockOrder
operator|.
name|getItemId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|mockOrder
operator|.
name|getQuantity
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|mockCsv
operator|.
name|setExpectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Order
name|order
init|=
operator|new
name|Order
argument_list|()
operator|.
name|setOrderId
argument_list|(
literal|"Order-Java-0001"
argument_list|)
operator|.
name|setItemId
argument_list|(
literal|"MILK"
argument_list|)
operator|.
name|setQuantity
argument_list|(
literal|3
argument_list|)
decl_stmt|;
name|Object
name|answer
init|=
name|javaProducer
operator|.
name|requestBody
argument_list|(
name|order
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|OrderResponse
operator|.
name|class
argument_list|,
name|answer
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|OrderResponse
name|or
init|=
operator|(
name|OrderResponse
operator|)
name|answer
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Order-Java-0001"
argument_list|,
name|or
operator|.
name|getOrderId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|or
operator|.
name|isAccepted
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Order accepted:[item='MILK' quantity='3']"
argument_list|,
name|or
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
name|mockCsv
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testXML ()
specifier|public
name|void
name|testXML
parameter_list|()
throws|throws
name|Exception
block|{
name|mockCsv
operator|.
name|whenAnyExchangeReceived
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Object
name|mockBody
init|=
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
name|Order
operator|.
name|class
argument_list|,
name|mockBody
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|Order
name|mockOrder
init|=
operator|(
name|Order
operator|)
name|mockBody
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Order-XML-0001"
argument_list|,
name|mockOrder
operator|.
name|getOrderId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"MIKAN"
argument_list|,
name|mockOrder
operator|.
name|getItemId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|365
argument_list|,
name|mockOrder
operator|.
name|getQuantity
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|mockCsv
operator|.
name|setExpectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|String
name|order
init|=
literal|"<order orderId=\"Order-XML-0001\" itemId=\"MIKAN\" quantity=\"365\"/>"
decl_stmt|;
name|String
name|expectedAnswer
init|=
literal|"<orderResponse orderId=\"Order-XML-0001\" accepted=\"true\" description=\"Order accepted:[item='MIKAN' quantity='365']\"/>"
decl_stmt|;
name|String
name|answer
init|=
name|xmlProducer
operator|.
name|requestBody
argument_list|(
literal|"direct:xml"
argument_list|,
name|order
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|XMLUnit
operator|.
name|compareXML
argument_list|(
name|expectedAnswer
argument_list|,
name|answer
argument_list|)
expr_stmt|;
name|mockCsv
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testJSON ()
specifier|public
name|void
name|testJSON
parameter_list|()
throws|throws
name|Exception
block|{
name|mockCsv
operator|.
name|whenAnyExchangeReceived
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Object
name|mockBody
init|=
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
name|Order
operator|.
name|class
argument_list|,
name|mockBody
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|Order
name|mockOrder
init|=
operator|(
name|Order
operator|)
name|mockBody
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Order-JSON-0001"
argument_list|,
name|mockOrder
operator|.
name|getOrderId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"MIZUYO-KAN"
argument_list|,
name|mockOrder
operator|.
name|getItemId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|16350
argument_list|,
name|mockOrder
operator|.
name|getQuantity
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|mockCsv
operator|.
name|setExpectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|String
name|order
init|=
literal|"{\"orderId\":\"Order-JSON-0001\", \"itemId\":\"MIZUYO-KAN\", \"quantity\":\"16350\"}"
decl_stmt|;
name|OrderResponse
name|expected
init|=
operator|new
name|OrderResponse
argument_list|()
operator|.
name|setAccepted
argument_list|(
literal|true
argument_list|)
operator|.
name|setOrderId
argument_list|(
literal|"Order-JSON-0001"
argument_list|)
operator|.
name|setDescription
argument_list|(
literal|"Order accepted:[item='MIZUYO-KAN' quantity='16350']"
argument_list|)
decl_stmt|;
name|ObjectMapper
name|jsonMapper
init|=
operator|new
name|ObjectMapper
argument_list|()
decl_stmt|;
name|String
name|expectedJson
init|=
name|jsonMapper
operator|.
name|writeValueAsString
argument_list|(
name|expected
argument_list|)
decl_stmt|;
name|String
name|answer
init|=
name|jsonProducer
operator|.
name|requestBody
argument_list|(
literal|"direct:json"
argument_list|,
name|order
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|expectedJson
argument_list|,
name|answer
argument_list|)
expr_stmt|;
name|mockCsv
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

