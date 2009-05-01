begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model.loadbalancer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|loadbalancer
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
name|XmlTransient
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
name|XmlType
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
name|Exchange
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
name|Processor
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
name|processor
operator|.
name|loadbalancer
operator|.
name|LoadBalancer
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
name|RouteContext
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
name|util
operator|.
name|IntrospectionSupport
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * Represents an XML&lt;loadBalance/&gt; element  */
end_comment

begin_class
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|"loadBalancer"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|LoadBalancerDefinition
specifier|public
class|class
name|LoadBalancerDefinition
extends|extends
name|IdentifiedType
implements|implements
name|LoadBalancer
block|{
annotation|@
name|XmlTransient
DECL|field|loadBalancer
specifier|private
name|LoadBalancer
name|loadBalancer
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|loadBalancerTypeName
specifier|private
name|String
name|loadBalancerTypeName
decl_stmt|;
DECL|method|LoadBalancerDefinition ()
specifier|public
name|LoadBalancerDefinition
parameter_list|()
block|{     }
DECL|method|LoadBalancerDefinition (LoadBalancer loadBalancer)
specifier|public
name|LoadBalancerDefinition
parameter_list|(
name|LoadBalancer
name|loadBalancer
parameter_list|)
block|{
name|this
operator|.
name|loadBalancer
operator|=
name|loadBalancer
expr_stmt|;
block|}
DECL|method|LoadBalancerDefinition (String loadBalancerTypeName)
specifier|protected
name|LoadBalancerDefinition
parameter_list|(
name|String
name|loadBalancerTypeName
parameter_list|)
block|{
name|this
operator|.
name|loadBalancerTypeName
operator|=
name|loadBalancerTypeName
expr_stmt|;
block|}
DECL|method|getLoadBalancer (RouteContext routeContext, LoadBalancerDefinition type, String ref)
specifier|public
specifier|static
name|LoadBalancer
name|getLoadBalancer
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|LoadBalancerDefinition
name|type
parameter_list|,
name|String
name|ref
parameter_list|)
block|{
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|ref
argument_list|,
literal|"ref or loadBalancer"
argument_list|)
expr_stmt|;
name|LoadBalancer
name|loadBalancer
init|=
name|routeContext
operator|.
name|lookup
argument_list|(
name|ref
argument_list|,
name|LoadBalancer
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|loadBalancer
operator|instanceof
name|LoadBalancerDefinition
condition|)
block|{
name|type
operator|=
operator|(
name|LoadBalancerDefinition
operator|)
name|loadBalancer
expr_stmt|;
block|}
else|else
block|{
return|return
name|loadBalancer
return|;
block|}
block|}
return|return
name|type
operator|.
name|getLoadBalancer
argument_list|(
name|routeContext
argument_list|)
return|;
block|}
comment|/**      * Sets a named property on the data format instance using introspection      */
DECL|method|setProperty (Object bean, String name, Object value)
specifier|protected
name|void
name|setProperty
parameter_list|(
name|Object
name|bean
parameter_list|,
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
try|try
block|{
name|IntrospectionSupport
operator|.
name|setProperty
argument_list|(
name|bean
argument_list|,
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Failed to set property "
operator|+
name|name
operator|+
literal|" on "
operator|+
name|bean
operator|+
literal|". Reason: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Allows derived classes to customize the load balancer      */
DECL|method|configureLoadBalancer (LoadBalancer loadBalancer)
specifier|protected
name|void
name|configureLoadBalancer
parameter_list|(
name|LoadBalancer
name|loadBalancer
parameter_list|)
block|{     }
DECL|method|getLoadBalancer (RouteContext routeContext)
specifier|public
name|LoadBalancer
name|getLoadBalancer
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
if|if
condition|(
name|loadBalancer
operator|==
literal|null
condition|)
block|{
name|loadBalancer
operator|=
name|createLoadBalancer
argument_list|(
name|routeContext
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|loadBalancer
argument_list|,
literal|"loadBalancer"
argument_list|)
expr_stmt|;
name|configureLoadBalancer
argument_list|(
name|loadBalancer
argument_list|)
expr_stmt|;
block|}
return|return
name|loadBalancer
return|;
block|}
comment|/**      * Factory method to create the load balancer instance      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|createLoadBalancer (RouteContext routeContext)
specifier|protected
name|LoadBalancer
name|createLoadBalancer
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
if|if
condition|(
name|loadBalancerTypeName
operator|!=
literal|null
condition|)
block|{
name|Class
name|type
init|=
name|routeContext
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveClass
argument_list|(
name|loadBalancerTypeName
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot find class: "
operator|+
name|loadBalancerTypeName
operator|+
literal|" in the classpath"
argument_list|)
throw|;
block|}
return|return
operator|(
name|LoadBalancer
operator|)
name|ObjectHelper
operator|.
name|newInstance
argument_list|(
name|type
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|addProcessor (Processor processor)
specifier|public
name|void
name|addProcessor
parameter_list|(
name|Processor
name|processor
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|loadBalancer
argument_list|,
literal|"loadBalancer"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|loadBalancer
operator|.
name|addProcessor
argument_list|(
name|processor
argument_list|)
expr_stmt|;
block|}
DECL|method|getProcessors ()
specifier|public
name|List
argument_list|<
name|Processor
argument_list|>
name|getProcessors
parameter_list|()
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|loadBalancer
argument_list|,
literal|"loadBalancer"
argument_list|,
name|this
argument_list|)
expr_stmt|;
return|return
name|loadBalancer
operator|.
name|getProcessors
argument_list|()
return|;
block|}
DECL|method|removeProcessor (Processor processor)
specifier|public
name|void
name|removeProcessor
parameter_list|(
name|Processor
name|processor
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|loadBalancer
argument_list|,
literal|"loadBalancer"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|loadBalancer
operator|.
name|removeProcessor
argument_list|(
name|processor
argument_list|)
expr_stmt|;
block|}
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|loadBalancer
argument_list|,
literal|"loadBalancer"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|loadBalancer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

