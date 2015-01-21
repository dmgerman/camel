begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
package|;
end_package

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
name|XmlAttribute
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
name|Service
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
name|WrapProcessor
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
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|Policy
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
name|spi
operator|.
name|TransactedPolicy
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
comment|/**  * Defines a policy the route will use  *  * @version   */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"eip,policy"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"policy"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|PolicyDefinition
specifier|public
class|class
name|PolicyDefinition
extends|extends
name|OutputDefinition
argument_list|<
name|PolicyDefinition
argument_list|>
block|{
comment|// TODO: Align this code with TransactedDefinition
annotation|@
name|XmlTransient
DECL|field|type
specifier|protected
name|Class
argument_list|<
name|?
extends|extends
name|Policy
argument_list|>
name|type
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|ref
specifier|protected
name|String
name|ref
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|policy
specifier|private
name|Policy
name|policy
decl_stmt|;
DECL|method|PolicyDefinition ()
specifier|public
name|PolicyDefinition
parameter_list|()
block|{     }
DECL|method|PolicyDefinition (Policy policy)
specifier|public
name|PolicyDefinition
parameter_list|(
name|Policy
name|policy
parameter_list|)
block|{
name|this
operator|.
name|policy
operator|=
name|policy
expr_stmt|;
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
literal|"Policy["
operator|+
name|description
argument_list|()
operator|+
literal|"]"
return|;
block|}
DECL|method|description ()
specifier|protected
name|String
name|description
parameter_list|()
block|{
if|if
condition|(
name|policy
operator|!=
literal|null
condition|)
block|{
return|return
name|policy
operator|.
name|toString
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|"ref:"
operator|+
name|ref
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|getShortName ()
specifier|public
name|String
name|getShortName
parameter_list|()
block|{
comment|// a policy can be a hidden disguise for a transacted definition
name|boolean
name|transacted
init|=
name|type
operator|!=
literal|null
operator|&&
name|type
operator|.
name|isAssignableFrom
argument_list|(
name|TransactedPolicy
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|transacted
condition|?
literal|"transacted"
else|:
literal|"policy"
return|;
block|}
annotation|@
name|Override
DECL|method|getLabel ()
specifier|public
name|String
name|getLabel
parameter_list|()
block|{
return|return
name|getShortName
argument_list|()
operator|+
literal|"["
operator|+
name|getDescription
argument_list|()
operator|+
literal|"]"
return|;
block|}
annotation|@
name|Override
DECL|method|isAbstract ()
specifier|public
name|boolean
name|isAbstract
parameter_list|()
block|{
comment|// policy should NOT be abstract
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|isTopLevelOnly ()
specifier|public
name|boolean
name|isTopLevelOnly
parameter_list|()
block|{
comment|// a policy is often top-level but you can have it in lower-levels as well
return|return
literal|false
return|;
block|}
DECL|method|getRef ()
specifier|public
name|String
name|getRef
parameter_list|()
block|{
return|return
name|ref
return|;
block|}
DECL|method|setRef (String ref)
specifier|public
name|void
name|setRef
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
name|this
operator|.
name|ref
operator|=
name|ref
expr_stmt|;
block|}
comment|/**      * Sets a policy type that this definition should scope within.      *<p/>      * Is used for convention over configuration situations where the policy      * should be automatic looked up in the registry and it should be based      * on this type. For instance a {@link org.apache.camel.spi.TransactedPolicy}      * can be set as type for easy transaction configuration.      *<p/>      * Will by default scope to the wide {@link Policy}      *      * @param type the policy type      */
DECL|method|setType (Class<? extends Policy> type)
specifier|public
name|void
name|setType
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|Policy
argument_list|>
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
comment|/**      * Sets a reference to use for lookup the policy in the registry.      *      * @param ref the reference      * @return the builder      */
DECL|method|ref (String ref)
specifier|public
name|PolicyDefinition
name|ref
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
name|setRef
argument_list|(
name|ref
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|createProcessor (RouteContext routeContext)
specifier|public
name|Processor
name|createProcessor
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
throws|throws
name|Exception
block|{
name|Policy
name|policy
init|=
name|resolvePolicy
argument_list|(
name|routeContext
argument_list|)
decl_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|policy
argument_list|,
literal|"policy"
argument_list|,
name|this
argument_list|)
expr_stmt|;
comment|// before wrap
name|policy
operator|.
name|beforeWrap
argument_list|(
name|routeContext
argument_list|,
name|this
argument_list|)
expr_stmt|;
comment|// create processor after the before wrap
name|Processor
name|childProcessor
init|=
name|this
operator|.
name|createChildProcessor
argument_list|(
name|routeContext
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|// wrap
name|Processor
name|target
init|=
name|policy
operator|.
name|wrap
argument_list|(
name|routeContext
argument_list|,
name|childProcessor
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|target
operator|instanceof
name|Service
operator|)
condition|)
block|{
comment|// wrap the target so it becomes a service and we can manage its lifecycle
name|target
operator|=
operator|new
name|WrapProcessor
argument_list|(
name|target
argument_list|,
name|childProcessor
argument_list|)
expr_stmt|;
block|}
return|return
name|target
return|;
block|}
DECL|method|resolvePolicy (RouteContext routeContext)
specifier|protected
name|Policy
name|resolvePolicy
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
if|if
condition|(
name|policy
operator|!=
literal|null
condition|)
block|{
return|return
name|policy
return|;
block|}
comment|// reuse code on transacted definition to do the resolution
return|return
name|TransactedDefinition
operator|.
name|doResolvePolicy
argument_list|(
name|routeContext
argument_list|,
name|getRef
argument_list|()
argument_list|,
name|type
argument_list|)
return|;
block|}
block|}
end_class

end_unit

