begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.cdi
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
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Inject
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
name|cdi
operator|.
name|Uri
import|;
end_import

begin_comment
comment|/**  * Configures all our Camel routes, components, endpoints and beans  */
end_comment

begin_class
DECL|class|MyRoutes
specifier|public
class|class
name|MyRoutes
extends|extends
name|RouteBuilder
block|{
annotation|@
name|Inject
annotation|@
name|Uri
argument_list|(
literal|"timer:foo?period=5000"
argument_list|)
DECL|field|inputEndpoint
specifier|private
name|Endpoint
name|inputEndpoint
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|Uri
argument_list|(
literal|"log:output"
argument_list|)
DECL|field|resultEndpoint
specifier|private
name|Endpoint
name|resultEndpoint
decl_stmt|;
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
block|{
comment|// you can configure the route rule with Java DSL here
name|from
argument_list|(
name|inputEndpoint
argument_list|)
operator|.
name|to
argument_list|(
literal|"bean:counterBean"
argument_list|)
operator|.
name|to
argument_list|(
name|resultEndpoint
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

