begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.ruby
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|ruby
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|impl
operator|.
name|DefaultCamelContext
import|;
end_import

begin_comment
comment|/**  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|RubyCamel
specifier|public
class|class
name|RubyCamel
block|{
DECL|field|camelContext
specifier|private
specifier|static
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|routes
specifier|private
specifier|static
name|List
argument_list|<
name|RouteBuilder
argument_list|>
name|routes
init|=
operator|new
name|ArrayList
argument_list|<
name|RouteBuilder
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|getRoutes ()
specifier|public
specifier|static
name|List
argument_list|<
name|RouteBuilder
argument_list|>
name|getRoutes
parameter_list|()
block|{
return|return
name|routes
return|;
block|}
DECL|method|addRouteBuilder (RouteBuilder builder)
specifier|public
specifier|static
name|void
name|addRouteBuilder
parameter_list|(
name|RouteBuilder
name|builder
parameter_list|)
block|{
name|routes
operator|.
name|add
argument_list|(
name|builder
argument_list|)
expr_stmt|;
block|}
DECL|method|getCamelContext ()
specifier|public
specifier|static
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
if|if
condition|(
name|camelContext
operator|==
literal|null
condition|)
block|{
name|camelContext
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
block|}
return|return
name|camelContext
return|;
block|}
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
specifier|static
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|RubyCamel
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
block|}
end_class

end_unit

