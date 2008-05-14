begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.config.scan.route
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|config
operator|.
name|scan
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
name|spring
operator|.
name|config
operator|.
name|scan
operator|.
name|component
operator|.
name|MyProcessor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|annotation
operator|.
name|Autowired
import|;
end_import

begin_class
DECL|class|MyRouteBuilder
specifier|public
class|class
name|MyRouteBuilder
extends|extends
name|RouteBuilder
block|{
DECL|field|component
name|MyProcessor
name|component
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|name
operator|=
literal|"start"
argument_list|)
DECL|field|startEndpoint
name|Endpoint
name|startEndpoint
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|name
operator|=
literal|"result"
argument_list|)
DECL|field|resultEndpoint
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
throws|throws
name|Exception
block|{
name|from
argument_list|(
name|startEndpoint
argument_list|)
operator|.
name|process
argument_list|(
name|component
argument_list|)
operator|.
name|to
argument_list|(
name|resultEndpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Autowired
DECL|method|setComponent (MyProcessor component)
specifier|public
name|void
name|setComponent
parameter_list|(
name|MyProcessor
name|component
parameter_list|)
block|{
name|this
operator|.
name|component
operator|=
name|component
expr_stmt|;
block|}
block|}
end_class

end_unit

