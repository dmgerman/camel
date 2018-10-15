begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.jpa
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|jpa
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|javax
operator|.
name|persistence
operator|.
name|OptimisticLockException
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
name|examples
operator|.
name|Customer
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
name|AggregationStrategy
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
name|spring
operator|.
name|SpringRouteBuilder
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

begin_class
DECL|class|JpaPollingConsumerLockEntityTest
specifier|public
class|class
name|JpaPollingConsumerLockEntityTest
extends|extends
name|AbstractJpaTest
block|{
DECL|field|SELECT_ALL_STRING
specifier|protected
specifier|static
specifier|final
name|String
name|SELECT_ALL_STRING
init|=
literal|"select x from "
operator|+
name|Customer
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|" x"
decl_stmt|;
annotation|@
name|Before
annotation|@
name|Override
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|Customer
name|customer
init|=
operator|new
name|Customer
argument_list|()
decl_stmt|;
name|customer
operator|.
name|setName
argument_list|(
literal|"Donald Duck"
argument_list|)
expr_stmt|;
name|saveEntityInDB
argument_list|(
name|customer
argument_list|)
expr_stmt|;
name|Customer
name|customer2
init|=
operator|new
name|Customer
argument_list|()
decl_stmt|;
name|customer2
operator|.
name|setName
argument_list|(
literal|"Goofy"
argument_list|)
expr_stmt|;
name|saveEntityInDB
argument_list|(
name|customer2
argument_list|)
expr_stmt|;
name|assertEntityInDB
argument_list|(
literal|2
argument_list|,
name|Customer
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPollingConsumerWithLock ()
specifier|public
name|void
name|testPollingConsumerWithLock
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:locked"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"orders: 1"
argument_list|,
literal|"orders: 2"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"name"
argument_list|,
literal|"Donald%"
argument_list|)
expr_stmt|;
name|template
operator|.
name|asyncRequestBodyAndHeaders
argument_list|(
literal|"direct:locked"
argument_list|,
literal|"message"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|template
operator|.
name|asyncRequestBodyAndHeaders
argument_list|(
literal|"direct:locked"
argument_list|,
literal|"message"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPollingConsumerWithoutLock ()
specifier|public
name|void
name|testPollingConsumerWithoutLock
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:not-locked"
argument_list|)
decl_stmt|;
name|MockEndpoint
name|errMock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:error"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"orders: 1"
argument_list|)
expr_stmt|;
name|errMock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|errMock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|OptimisticLockException
operator|.
name|class
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"name"
argument_list|,
literal|"Donald%"
argument_list|)
expr_stmt|;
name|template
operator|.
name|asyncRequestBodyAndHeaders
argument_list|(
literal|"direct:not-locked"
argument_list|,
literal|"message"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|template
operator|.
name|asyncRequestBodyAndHeaders
argument_list|(
literal|"direct:not-locked"
argument_list|,
literal|"message"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
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
block|{
return|return
operator|new
name|SpringRouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|AggregationStrategy
name|enrichStrategy
init|=
operator|new
name|AggregationStrategy
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Exchange
name|aggregate
parameter_list|(
name|Exchange
name|originalExchange
parameter_list|,
name|Exchange
name|jpaExchange
parameter_list|)
block|{
name|Customer
name|customer
init|=
name|jpaExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Customer
operator|.
name|class
argument_list|)
decl_stmt|;
name|customer
operator|.
name|setOrderCount
argument_list|(
name|customer
operator|.
name|getOrderCount
argument_list|()
operator|+
literal|1
argument_list|)
expr_stmt|;
return|return
name|jpaExchange
return|;
block|}
block|}
decl_stmt|;
name|onException
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|simple
argument_list|(
literal|"${exception}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:error"
argument_list|)
operator|.
name|handled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:locked"
argument_list|)
operator|.
name|onException
argument_list|(
name|OptimisticLockException
operator|.
name|class
argument_list|)
operator|.
name|redeliveryDelay
argument_list|(
literal|60
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|2
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|pollEnrich
argument_list|()
operator|.
name|simple
argument_list|(
literal|"jpa://"
operator|+
name|Customer
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"?lockModeType=OPTIMISTIC_FORCE_INCREMENT&query=select c from Customer c where c.name like '${header.name}'"
argument_list|)
operator|.
name|aggregationStrategy
argument_list|(
name|enrichStrategy
argument_list|)
operator|.
name|to
argument_list|(
literal|"jpa://"
operator|+
name|Customer
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|simple
argument_list|(
literal|"orders: ${body.orderCount}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:locked"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:not-locked"
argument_list|)
operator|.
name|pollEnrich
argument_list|()
operator|.
name|simple
argument_list|(
literal|"jpa://"
operator|+
name|Customer
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"?query=select c from Customer c where c.name like '${header.name}'"
argument_list|)
operator|.
name|aggregationStrategy
argument_list|(
name|enrichStrategy
argument_list|)
operator|.
name|to
argument_list|(
literal|"jpa://"
operator|+
name|Customer
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|simple
argument_list|(
literal|"orders: ${body.orderCount}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:not-locked"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|routeXml ()
specifier|protected
name|String
name|routeXml
parameter_list|()
block|{
return|return
literal|"org/apache/camel/processor/jpa/springJpaRouteTest.xml"
return|;
block|}
annotation|@
name|Override
DECL|method|selectAllString ()
specifier|protected
name|String
name|selectAllString
parameter_list|()
block|{
return|return
name|SELECT_ALL_STRING
return|;
block|}
block|}
end_class

end_unit

