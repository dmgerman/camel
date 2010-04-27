begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.guice
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|guice
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

begin_comment
comment|/**  * Lets use a RouteBuilder as an installer of other routes  * where we can let the CamelContext resolve endpoints for us  */
end_comment

begin_class
DECL|class|MyRouteInstaller
specifier|public
class|class
name|MyRouteInstaller
extends|extends
name|RouteBuilder
block|{
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// TODO we could register explicit endpoints here too if we want
comment|// lets add some other route builders
name|includeRoutes
argument_list|(
operator|new
name|MyConfigurableRoute
argument_list|(
name|endpoint
argument_list|(
literal|"direct:a"
argument_list|)
argument_list|,
name|endpoint
argument_list|(
literal|"direct:b"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|includeRoutes
argument_list|(
operator|new
name|MyConfigurableRoute
argument_list|(
name|endpoint
argument_list|(
literal|"direct:c"
argument_list|)
argument_list|,
name|endpoint
argument_list|(
literal|"direct:d"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

