begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|DrinkRouter
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
name|stuff
operator|.
name|Waiter
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
name|impl
operator|.
name|JndiRegistry
import|;
end_import

begin_comment
comment|/**  * A simple example router from Cafe Demo  */
end_comment

begin_class
DECL|class|CafeRouteBuilder
specifier|public
class|class
name|CafeRouteBuilder
extends|extends
name|RouteBuilder
block|{
DECL|method|main (String[] args)
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
name|CafeRouteBuilder
name|builder
init|=
operator|new
name|CafeRouteBuilder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|runCafeRouteDemo
argument_list|()
expr_stmt|;
block|}
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
operator|new
name|JndiRegistry
argument_list|()
decl_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"drinkRouter"
argument_list|,
operator|new
name|DrinkRouter
argument_list|()
argument_list|)
expr_stmt|;
name|jndi
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
name|jndi
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
name|jndi
operator|.
name|bind
argument_list|(
literal|"waiter"
argument_list|,
operator|new
name|Waiter
argument_list|()
argument_list|)
expr_stmt|;
name|jndi
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
return|return
name|jndi
return|;
block|}
DECL|method|runCafeRouteDemo ()
specifier|public
name|void
name|runCafeRouteDemo
parameter_list|()
throws|throws
name|Exception
block|{
comment|// create CamelContext
name|DefaultCamelContext
name|camelContext
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|camelContext
operator|.
name|setRegistry
argument_list|(
name|createRegistry
argument_list|()
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|addRoutes
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|start
argument_list|()
expr_stmt|;
name|ProducerTemplate
name|template
init|=
name|camelContext
operator|.
name|createProducerTemplate
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
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:cafe"
argument_list|,
name|order
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|6000
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
comment|//START SNIPPET: RouteConfig
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:cafe"
argument_list|)
operator|.
name|split
argument_list|()
operator|.
name|method
argument_list|(
literal|"orderSplitter"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:drink"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:drink"
argument_list|)
operator|.
name|recipientList
argument_list|()
operator|.
name|method
argument_list|(
literal|"drinkRouter"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:coldDrinks?concurrentConsumers=2"
argument_list|)
operator|.
name|to
argument_list|(
literal|"bean:barista?method=prepareColdDrink"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:deliveries"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:hotDrinks?concurrentConsumers=3"
argument_list|)
operator|.
name|to
argument_list|(
literal|"bean:barista?method=prepareHotDrink"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:deliveries"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:deliveries"
argument_list|)
operator|.
name|aggregate
argument_list|(
operator|new
name|CafeAggregationStrategy
argument_list|()
argument_list|)
operator|.
name|method
argument_list|(
literal|"waiter"
argument_list|,
literal|"checkOrder"
argument_list|)
operator|.
name|completionTimeout
argument_list|(
literal|5
operator|*
literal|1000L
argument_list|)
operator|.
name|to
argument_list|(
literal|"bean:waiter?method=prepareDelivery"
argument_list|)
operator|.
name|to
argument_list|(
literal|"bean:waiter?method=deliverCafes"
argument_list|)
expr_stmt|;
block|}
comment|//END SNIPPET: RouteConfig
block|}
end_class

end_unit

