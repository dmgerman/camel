begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.restlet.route
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|restlet
operator|.
name|route
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
name|test
operator|.
name|AvailablePortFinder
import|;
end_import

begin_comment
comment|/**  * Route builder for RestletRouteBuilderAuthTest  */
end_comment

begin_class
DECL|class|TestRouteBuilder
specifier|public
class|class
name|TestRouteBuilder
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
comment|// get port number dynamically
name|int
name|port
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
decl_stmt|;
comment|// START SNIPPET: consumer_route
name|from
argument_list|(
literal|"restlet:http://localhost:"
operator|+
name|port
operator|+
literal|"/securedOrders?restletMethod=post&restletRealm=#realm"
argument_list|)
operator|.
name|process
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
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"received ["
operator|+
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|+
literal|"] as an order id = "
operator|+
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"id"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
comment|// END SNIPPET: consumer_route
comment|// START SNIPPET: producer_route
comment|// Note: restletMethod and restletRealmRef are stripped
comment|// from the query before a request is sent as they are
comment|// only processed by Camel.
name|from
argument_list|(
literal|"direct:start-auth"
argument_list|)
operator|.
name|to
argument_list|(
literal|"restlet:http://localhost:"
operator|+
name|port
operator|+
literal|"/securedOrders?restletMethod=post"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: producer_route
block|}
block|}
end_class

end_unit

