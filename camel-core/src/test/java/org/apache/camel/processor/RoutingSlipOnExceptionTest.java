begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
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
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_class
DECL|class|RoutingSlipOnExceptionTest
specifier|public
class|class
name|RoutingSlipOnExceptionTest
extends|extends
name|DynamicRouterOnExceptionTest
block|{
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
name|onException
argument_list|(
name|IllegalArgumentException
operator|.
name|class
argument_list|)
operator|.
name|redeliveryDelay
argument_list|(
literal|0
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|routingSlip
argument_list|(
name|method
argument_list|(
name|RoutingSlipOnExceptionTest
operator|.
name|class
argument_list|,
literal|"whereTo"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:end"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

