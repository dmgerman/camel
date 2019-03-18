begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.widget
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|widget
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
name|Endpoint
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
name|Predicate
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

begin_comment
comment|/**  * The widget and gadget route that pickup incoming orders from the<tt>newOrder</tt> queue  * and route the orders to either the widget or gadget inventory system.  */
end_comment

begin_class
DECL|class|WidgetGadgetRoute
specifier|public
class|class
name|WidgetGadgetRoute
extends|extends
name|RouteBuilder
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// you can define the endpoints and predicates here
comment|// it is more common to inline the endpoints and predicates in the route
comment|// as shown in the CreateOrderRoute
name|Endpoint
name|newOrder
init|=
name|endpoint
argument_list|(
literal|"activemq:queue:newOrder"
argument_list|)
decl_stmt|;
name|Predicate
name|isWidget
init|=
name|xpath
argument_list|(
literal|"/order/product = 'widget'"
argument_list|)
decl_stmt|;
name|Endpoint
name|widget
init|=
name|endpoint
argument_list|(
literal|"activemq:queue:widget"
argument_list|)
decl_stmt|;
name|Endpoint
name|gadget
init|=
name|endpoint
argument_list|(
literal|"activemq:queue:gadget"
argument_list|)
decl_stmt|;
name|from
argument_list|(
name|newOrder
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|(
name|isWidget
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:widget"
argument_list|)
comment|// add a log so we can see this happening in the shell
operator|.
name|to
argument_list|(
name|widget
argument_list|)
operator|.
name|otherwise
argument_list|()
operator|.
name|to
argument_list|(
literal|"log:gadget"
argument_list|)
comment|// add a log so we can see this happening in the shell
operator|.
name|to
argument_list|(
name|gadget
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

