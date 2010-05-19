begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.web.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|web
operator|.
name|model
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
name|ServiceStatus
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
name|DescriptionDefinition
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
name|RouteDefinition
import|;
end_import

begin_comment
comment|/**  * Represents a route.  *<p/>  * We need this model to link the RouteDefinition with a CamelContext  */
end_comment

begin_class
DECL|class|Route
specifier|public
class|class
name|Route
block|{
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|route
specifier|private
name|RouteDefinition
name|route
decl_stmt|;
DECL|method|Route ()
specifier|public
name|Route
parameter_list|()
block|{     }
DECL|method|Route (CamelContext camelContext, RouteDefinition route)
specifier|public
name|Route
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|RouteDefinition
name|route
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|this
operator|.
name|route
operator|=
name|route
expr_stmt|;
block|}
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|route
operator|.
name|getId
argument_list|()
return|;
block|}
DECL|method|getDescription ()
specifier|public
name|DescriptionDefinition
name|getDescription
parameter_list|()
block|{
return|return
name|route
operator|.
name|getDescription
argument_list|()
return|;
block|}
DECL|method|getStatus ()
specifier|public
name|ServiceStatus
name|getStatus
parameter_list|()
block|{
return|return
name|route
operator|.
name|getStatus
argument_list|(
name|camelContext
argument_list|)
return|;
block|}
DECL|method|isStartable ()
specifier|public
name|boolean
name|isStartable
parameter_list|()
block|{
return|return
name|route
operator|.
name|isStartable
argument_list|(
name|camelContext
argument_list|)
return|;
block|}
DECL|method|isStoppable ()
specifier|public
name|boolean
name|isStoppable
parameter_list|()
block|{
return|return
name|route
operator|.
name|isStoppable
argument_list|(
name|camelContext
argument_list|)
return|;
block|}
block|}
end_class

end_unit

