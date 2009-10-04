begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
package|;
end_package

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
name|java
operator|.
name|util
operator|.
name|List
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
name|Consumer
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
name|Route
import|;
end_import

begin_comment
comment|/**  * Information about a route to be started where we want to control the order  * in which they are started by {@link org.apache.camel.impl.DefaultCamelContext}  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|StartupRouteHolder
class|class
name|StartupRouteHolder
block|{
DECL|field|startupOrder
specifier|private
specifier|final
name|int
name|startupOrder
decl_stmt|;
DECL|field|route
specifier|private
specifier|final
name|Route
name|route
decl_stmt|;
DECL|field|inputs
specifier|private
specifier|final
name|List
argument_list|<
name|Consumer
argument_list|>
name|inputs
init|=
operator|new
name|ArrayList
argument_list|<
name|Consumer
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|StartupRouteHolder (int startupOrder, Route route)
name|StartupRouteHolder
parameter_list|(
name|int
name|startupOrder
parameter_list|,
name|Route
name|route
parameter_list|)
block|{
name|this
operator|.
name|startupOrder
operator|=
name|startupOrder
expr_stmt|;
name|this
operator|.
name|route
operator|=
name|route
expr_stmt|;
block|}
DECL|method|addInput (Consumer input)
name|void
name|addInput
parameter_list|(
name|Consumer
name|input
parameter_list|)
block|{
name|inputs
operator|.
name|add
argument_list|(
name|input
argument_list|)
expr_stmt|;
block|}
DECL|method|getStartupOrder ()
specifier|public
name|int
name|getStartupOrder
parameter_list|()
block|{
return|return
name|startupOrder
return|;
block|}
DECL|method|getRoute ()
specifier|public
name|Route
name|getRoute
parameter_list|()
block|{
return|return
name|route
return|;
block|}
DECL|method|getInputs ()
specifier|public
name|List
argument_list|<
name|Consumer
argument_list|>
name|getInputs
parameter_list|()
block|{
return|return
name|inputs
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Route "
operator|+
name|route
operator|.
name|getId
argument_list|()
operator|+
literal|" starts in order "
operator|+
name|startupOrder
return|;
block|}
block|}
end_class

end_unit

