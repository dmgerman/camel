begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hystrix.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hystrix
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|HystrixConfigurationDefinition
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|Bean
import|;
end_import

begin_class
DECL|class|HystrixHierarchicalConfiguration
specifier|public
class|class
name|HystrixHierarchicalConfiguration
block|{
annotation|@
name|Bean
argument_list|(
name|name
operator|=
literal|"ref-hystrix"
argument_list|)
DECL|method|hystrixConfiguration ()
specifier|public
name|HystrixConfigurationDefinition
name|hystrixConfiguration
parameter_list|()
block|{
return|return
operator|new
name|HystrixConfigurationDefinition
argument_list|()
operator|.
name|groupKey
argument_list|(
literal|"ref-group-key"
argument_list|)
operator|.
name|corePoolSize
argument_list|(
literal|5
argument_list|)
return|;
block|}
annotation|@
name|Bean
DECL|method|routeBuilder ()
specifier|public
name|RouteBuilder
name|routeBuilder
parameter_list|()
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
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"hystrix-route"
argument_list|)
operator|.
name|hystrix
argument_list|()
operator|.
name|hystrixConfiguration
argument_list|(
literal|"ref-hystrix"
argument_list|)
operator|.
name|hystrixConfiguration
argument_list|()
operator|.
name|groupKey
argument_list|(
literal|"local-conf-group-key"
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|to
argument_list|(
literal|"direct:foo"
argument_list|)
operator|.
name|onFallback
argument_list|()
operator|.
name|transform
argument_list|()
operator|.
name|constant
argument_list|(
literal|"Fallback message"
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
name|from
argument_list|(
literal|"direct:foo"
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|body
argument_list|(
name|b
lambda|->
literal|"Bye World"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

