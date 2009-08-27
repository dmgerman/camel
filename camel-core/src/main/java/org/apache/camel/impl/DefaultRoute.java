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
name|Route
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
name|Service
import|;
end_import

begin_comment
comment|/**  * A<a href="http://camel.apache.org/routes.html">Route</a>  * defines the processing used on an inbound message exchange  * from a specific {@link org.apache.camel.Endpoint} within a {@link org.apache.camel.CamelContext}  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|DefaultRoute
specifier|public
specifier|abstract
class|class
name|DefaultRoute
extends|extends
name|ServiceSupport
implements|implements
name|Route
block|{
DECL|field|endpoint
specifier|private
specifier|final
name|Endpoint
name|endpoint
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
argument_list|()
decl_stmt|;
DECL|field|services
specifier|private
specifier|final
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
DECL|method|DefaultRoute (Endpoint endpoint)
specifier|public
name|DefaultRoute
parameter_list|(
name|Endpoint
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
DECL|method|DefaultRoute (Endpoint endpoint, Service... services)
specifier|public
name|DefaultRoute
parameter_list|(
name|Endpoint
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
literal|"Route "
operator|+
name|getId
argument_list|()
return|;
block|}
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|properties
operator|.
name|get
argument_list|(
name|Route
operator|.
name|ID_PROPERTY
argument_list|)
return|;
block|}
DECL|method|getEndpoint ()
specifier|public
name|Endpoint
name|getEndpoint
parameter_list|()
block|{
return|return
name|endpoint
return|;
block|}
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
DECL|method|onStartingServices (List<Service> services)
specifier|public
name|void
name|onStartingServices
parameter_list|(
name|List
argument_list|<
name|Service
argument_list|>
name|services
parameter_list|)
throws|throws
name|Exception
block|{
name|addServices
argument_list|(
name|services
argument_list|)
expr_stmt|;
block|}
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
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{     }
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// clear services when stopping
name|services
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

