begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
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
name|IdentifiedType
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|Metadata
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
name|FactoryBean
import|;
end_import

begin_comment
comment|/**  * Configuration of routes using XML  *  * @version  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"spring,configuration,routing"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"routeContext"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|CamelRouteContextFactoryBean
specifier|public
class|class
name|CamelRouteContextFactoryBean
extends|extends
name|IdentifiedType
implements|implements
name|FactoryBean
argument_list|<
name|List
argument_list|<
name|RouteDefinition
argument_list|>
argument_list|>
block|{
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"route"
argument_list|,
name|required
operator|=
literal|true
argument_list|)
DECL|field|routes
specifier|private
name|List
argument_list|<
name|RouteDefinition
argument_list|>
name|routes
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|getObject ()
specifier|public
name|List
argument_list|<
name|RouteDefinition
argument_list|>
name|getObject
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|routes
return|;
block|}
DECL|method|getObjectType ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getObjectType
parameter_list|()
block|{
return|return
name|routes
operator|.
name|getClass
argument_list|()
return|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|getRoutes ()
specifier|public
name|List
argument_list|<
name|RouteDefinition
argument_list|>
name|getRoutes
parameter_list|()
block|{
return|return
name|routes
return|;
block|}
DECL|method|setRoutes (List<RouteDefinition> routes)
specifier|public
name|void
name|setRoutes
parameter_list|(
name|List
argument_list|<
name|RouteDefinition
argument_list|>
name|routes
parameter_list|)
block|{
name|this
operator|.
name|routes
operator|=
name|routes
expr_stmt|;
block|}
block|}
end_class

end_unit

