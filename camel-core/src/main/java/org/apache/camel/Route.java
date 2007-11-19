begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
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
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * A<a href="http://activemq.apache.org/camel/routes.html">Route</a>  * defines the processing used on an inbound message exchange  * from a specific {@see Endpoint} within a {@link CamelContext}  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|Route
specifier|public
class|class
name|Route
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
block|{
DECL|field|PARENT_PROPERTY
specifier|public
specifier|static
specifier|final
name|String
name|PARENT_PROPERTY
init|=
literal|"parent"
decl_stmt|;
DECL|field|GROUP_PROPERTY
specifier|public
specifier|static
specifier|final
name|String
name|GROUP_PROPERTY
init|=
literal|"group"
decl_stmt|;
DECL|field|properties
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|(
literal|16
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|private
name|Endpoint
argument_list|<
name|E
argument_list|>
name|endpoint
decl_stmt|;
DECL|field|services
specifier|private
name|List
argument_list|<
name|Service
argument_list|>
name|services
init|=
operator|new
name|ArrayList
argument_list|<
name|Service
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|Route (Endpoint<E> endpoint)
specifier|public
name|Route
parameter_list|(
name|Endpoint
argument_list|<
name|E
argument_list|>
name|endpoint
parameter_list|)
block|{
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
DECL|method|Route (Endpoint<E> endpoint, Service... services)
specifier|public
name|Route
parameter_list|(
name|Endpoint
argument_list|<
name|E
argument_list|>
name|endpoint
parameter_list|,
name|Service
modifier|...
name|services
parameter_list|)
block|{
name|this
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
for|for
control|(
name|Service
name|service
range|:
name|services
control|)
block|{
name|addService
argument_list|(
name|service
argument_list|)
expr_stmt|;
block|}
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
literal|"Route"
return|;
block|}
DECL|method|getEndpoint ()
specifier|public
name|Endpoint
argument_list|<
name|E
argument_list|>
name|getEndpoint
parameter_list|()
block|{
return|return
name|endpoint
return|;
block|}
DECL|method|setEndpoint (Endpoint<E> endpoint)
specifier|public
name|void
name|setEndpoint
parameter_list|(
name|Endpoint
argument_list|<
name|E
argument_list|>
name|endpoint
parameter_list|)
block|{
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
comment|/**      * This property map is used to associate information about      * the route.      *      * @return      */
DECL|method|getProperties ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getProperties
parameter_list|()
block|{
return|return
name|properties
return|;
block|}
DECL|method|getServicesForRoute ()
specifier|public
name|List
argument_list|<
name|Service
argument_list|>
name|getServicesForRoute
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Service
argument_list|>
name|servicesForRoute
init|=
operator|new
name|ArrayList
argument_list|<
name|Service
argument_list|>
argument_list|(
name|getServices
argument_list|()
argument_list|)
decl_stmt|;
name|addServices
argument_list|(
name|servicesForRoute
argument_list|)
expr_stmt|;
return|return
name|servicesForRoute
return|;
block|}
comment|/**      * Returns the additional services required for this particular route      */
DECL|method|getServices ()
specifier|public
name|List
argument_list|<
name|Service
argument_list|>
name|getServices
parameter_list|()
block|{
return|return
name|services
return|;
block|}
DECL|method|setServices (List<Service> services)
specifier|public
name|void
name|setServices
parameter_list|(
name|List
argument_list|<
name|Service
argument_list|>
name|services
parameter_list|)
block|{
name|this
operator|.
name|services
operator|=
name|services
expr_stmt|;
block|}
DECL|method|addService (Service service)
specifier|public
name|void
name|addService
parameter_list|(
name|Service
name|service
parameter_list|)
block|{
name|getServices
argument_list|()
operator|.
name|add
argument_list|(
name|service
argument_list|)
expr_stmt|;
block|}
comment|/**      * Strategy method to allow derived classes to lazily load services for the route      */
DECL|method|addServices (List<Service> services)
specifier|protected
name|void
name|addServices
parameter_list|(
name|List
argument_list|<
name|Service
argument_list|>
name|services
parameter_list|)
throws|throws
name|Exception
block|{     }
block|}
end_class

end_unit

