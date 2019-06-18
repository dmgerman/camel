begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder.endpoint
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|endpoint
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
name|util
operator|.
name|function
operator|.
name|ThrowingConsumer
import|;
end_import

begin_comment
comment|/**  * A {@link RouteBuilder} which gives access to the endpoint DSL.  */
end_comment

begin_class
DECL|class|EndpointRouteBuilder
specifier|public
specifier|abstract
class|class
name|EndpointRouteBuilder
extends|extends
name|RouteBuilder
implements|implements
name|EndpointBuilderFactory
block|{
DECL|method|EndpointRouteBuilder ()
specifier|public
name|EndpointRouteBuilder
parameter_list|()
block|{     }
DECL|method|EndpointRouteBuilder (CamelContext context)
specifier|public
name|EndpointRouteBuilder
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
comment|/**      * Add routes to a context using a lambda expression.      * It can be used as following:      *<pre>      * RouteBuilder.addRoutes(context, rb ->      *     rb.from("direct:inbound").bean(ProduceTemplateBean.class)));      *</pre>      *      * @param context the camel context to add routes      * @param rbc a lambda expression receiving the {@code RouteBuilder} to use for creating routes      * @throws Exception if an error occurs      */
DECL|method|addEndpointRoutes (CamelContext context, ThrowingConsumer<EndpointRouteBuilder, Exception> rbc)
specifier|public
specifier|static
name|void
name|addEndpointRoutes
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|ThrowingConsumer
argument_list|<
name|EndpointRouteBuilder
argument_list|,
name|Exception
argument_list|>
name|rbc
parameter_list|)
throws|throws
name|Exception
block|{
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|EndpointRouteBuilder
argument_list|(
name|context
argument_list|)
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
name|rbc
operator|.
name|accept
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

