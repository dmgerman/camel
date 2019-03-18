begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.cafe
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|cafe
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
name|example
operator|.
name|cafe
operator|.
name|stuff
operator|.
name|Barista
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
name|cafe
operator|.
name|stuff
operator|.
name|CafeAggregationStrategy
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
name|cafe
operator|.
name|stuff
operator|.
name|OrderSplitter
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
name|cafe
operator|.
name|test
operator|.
name|TestDrinkRouter
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
name|cafe
operator|.
name|test
operator|.
name|TestWaiter
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
name|Registry
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

begin_class
DECL|class|CafeRouteBuilderTest
specifier|public
class|class
name|CafeRouteBuilderTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|waiter
specifier|protected
name|TestWaiter
name|waiter
init|=
operator|new
name|TestWaiter
argument_list|()
decl_stmt|;
DECL|field|driverRouter
specifier|protected
name|TestDrinkRouter
name|driverRouter
init|=
operator|new
name|TestDrinkRouter
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|bindBeans
argument_list|(
name|context
operator|.
name|getRegistry
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
DECL|method|bindBeans (Registry registry)
specifier|protected
name|void
name|bindBeans
parameter_list|(
name|Registry
name|registry
parameter_list|)
throws|throws
name|Exception
block|{
name|registry
operator|.
name|bind
argument_list|(
literal|"drinkRouter"
argument_list|,
name|driverRouter
argument_list|)
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"orderSplitter"
argument_list|,
operator|new
name|OrderSplitter
argument_list|()
argument_list|)
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"barista"
argument_list|,
operator|new
name|Barista
argument_list|()
argument_list|)
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"waiter"
argument_list|,
name|waiter
argument_list|)
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"aggregatorStrategy"
argument_list|,
operator|new
name|CafeAggregationStrategy
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSplitter ()
specifier|public
name|void
name|testSplitter
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|MockEndpoint
name|coldDrinks
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:coldDrinks"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|MockEndpoint
name|hotDrinks
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:hotDrinks"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|Order
name|order
init|=
operator|new
name|Order
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|order
operator|.
name|addItem
argument_list|(
name|DrinkType
operator|.
name|ESPRESSO
argument_list|,
literal|2
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|order
operator|.
name|addItem
argument_list|(
name|DrinkType
operator|.
name|CAPPUCCINO
argument_list|,
literal|2
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|coldDrinks
operator|.
name|expectedBodiesReceived
argument_list|(
operator|new
name|OrderItem
argument_list|(
name|order
argument_list|,
name|DrinkType
operator|.
name|ESPRESSO
argument_list|,
literal|2
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|hotDrinks
operator|.
name|expectedBodiesReceived
argument_list|(
operator|new
name|OrderItem
argument_list|(
name|order
argument_list|,
name|DrinkType
operator|.
name|CAPPUCCINO
argument_list|,
literal|2
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:cafe"
argument_list|,
name|order
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCafeRoute ()
specifier|public
name|void
name|testCafeRoute
parameter_list|()
throws|throws
name|Exception
block|{
name|driverRouter
operator|.
name|setTestModel
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Drink
argument_list|>
name|drinks
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Order
name|order
init|=
operator|new
name|Order
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|order
operator|.
name|addItem
argument_list|(
name|DrinkType
operator|.
name|ESPRESSO
argument_list|,
literal|2
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|order
operator|.
name|addItem
argument_list|(
name|DrinkType
operator|.
name|CAPPUCCINO
argument_list|,
literal|4
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|order
operator|.
name|addItem
argument_list|(
name|DrinkType
operator|.
name|LATTE
argument_list|,
literal|4
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|order
operator|.
name|addItem
argument_list|(
name|DrinkType
operator|.
name|MOCHA
argument_list|,
literal|2
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|drinks
operator|.
name|add
argument_list|(
operator|new
name|Drink
argument_list|(
literal|2
argument_list|,
name|DrinkType
operator|.
name|ESPRESSO
argument_list|,
literal|true
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|drinks
operator|.
name|add
argument_list|(
operator|new
name|Drink
argument_list|(
literal|2
argument_list|,
name|DrinkType
operator|.
name|CAPPUCCINO
argument_list|,
literal|false
argument_list|,
literal|4
argument_list|)
argument_list|)
expr_stmt|;
name|drinks
operator|.
name|add
argument_list|(
operator|new
name|Drink
argument_list|(
literal|2
argument_list|,
name|DrinkType
operator|.
name|LATTE
argument_list|,
literal|false
argument_list|,
literal|4
argument_list|)
argument_list|)
expr_stmt|;
name|drinks
operator|.
name|add
argument_list|(
operator|new
name|Drink
argument_list|(
literal|2
argument_list|,
name|DrinkType
operator|.
name|MOCHA
argument_list|,
literal|false
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|waiter
operator|.
name|setVerfiyDrinks
argument_list|(
name|drinks
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:cafe"
argument_list|,
name|order
argument_list|)
expr_stmt|;
comment|// wait enough time to let the aggregate complete
name|Thread
operator|.
name|sleep
argument_list|(
literal|10000
argument_list|)
expr_stmt|;
name|waiter
operator|.
name|verifyDrinks
argument_list|()
expr_stmt|;
block|}
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
name|CafeRouteBuilder
argument_list|()
return|;
block|}
block|}
end_class

end_unit

