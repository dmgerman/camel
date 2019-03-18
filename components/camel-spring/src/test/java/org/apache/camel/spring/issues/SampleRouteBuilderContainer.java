begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.issues
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|issues
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
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|InitializingBean
import|;
end_import

begin_class
DECL|class|SampleRouteBuilderContainer
specifier|public
class|class
name|SampleRouteBuilderContainer
implements|implements
name|InitializingBean
block|{
DECL|field|routeBuilder
specifier|private
name|RouteBuilder
name|routeBuilder
decl_stmt|;
DECL|method|setRouteBuilder (RouteBuilder routeBuilder)
specifier|public
name|void
name|setRouteBuilder
parameter_list|(
name|RouteBuilder
name|routeBuilder
parameter_list|)
block|{
name|this
operator|.
name|routeBuilder
operator|=
name|routeBuilder
expr_stmt|;
block|}
DECL|method|afterPropertiesSet ()
specifier|public
name|void
name|afterPropertiesSet
parameter_list|()
throws|throws
name|Exception
block|{
name|routeBuilder
operator|.
name|configure
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

