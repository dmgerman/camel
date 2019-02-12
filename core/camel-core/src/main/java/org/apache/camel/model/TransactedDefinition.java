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
name|TransactedPolicy
import|;
end_import

begin_comment
comment|/**  * Enables transaction on the route  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"configuration"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"transacted"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|TransactedDefinition
specifier|public
class|class
name|TransactedDefinition
extends|extends
name|OutputDefinition
argument_list|<
name|TransactedDefinition
argument_list|>
block|{
comment|// JAXB does not support changing the ref attribute from required to optional
comment|// if we extend PolicyDefinition so we must make a copy of the class
annotation|@
name|XmlTransient
DECL|field|PROPAGATION_REQUIRED
specifier|public
specifier|static
specifier|final
name|String
name|PROPAGATION_REQUIRED
init|=
literal|"PROPAGATION_REQUIRED"
decl_stmt|;
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
init|=
name|TransactedPolicy
operator|.
name|class
decl_stmt|;
annotation|@
name|XmlAttribute
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
DECL|method|TransactedDefinition ()
specifier|public
name|TransactedDefinition
parameter_list|()
block|{     }
DECL|method|TransactedDefinition (Policy policy)
specifier|public
name|TransactedDefinition
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
name|String
name|desc
init|=
name|description
argument_list|()
decl_stmt|;
if|if
condition|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|desc
argument_list|)
condition|)
block|{
return|return
literal|"Transacted"
return|;
block|}
else|else
block|{
return|return
literal|"Transacted["
operator|+
name|desc
operator|+
literal|"]"
return|;
block|}
block|}
DECL|method|description ()
specifier|protected
name|String
name|description
parameter_list|()
block|{
if|if
condition|(
name|ref
operator|!=
literal|null
condition|)
block|{
return|return
literal|"ref:"
operator|+
name|ref
return|;
block|}
elseif|else
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
literal|""
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
return|return
literal|"transacted"
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
name|String
name|desc
init|=
name|description
argument_list|()
decl_stmt|;
if|if
condition|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|desc
argument_list|)
condition|)
block|{
return|return
literal|"transacted"
return|;
block|}
else|else
block|{
return|return
literal|"transacted["
operator|+
name|desc
operator|+
literal|"]"
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|isAbstract ()
specifier|public
name|boolean
name|isAbstract
parameter_list|()
block|{
return|return
literal|true
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
comment|// transacted is top level as we only allow have it configured once per route
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|isWrappingEntireOutput ()
specifier|public
name|boolean
name|isWrappingEntireOutput
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|getPolicy ()
specifier|public
name|Policy
name|getPolicy
parameter_list|()
block|{
return|return
name|policy
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
DECL|method|getType ()
specifier|public
name|Class
argument_list|<
name|?
extends|extends
name|Policy
argument_list|>
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
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
name|TransactedDefinition
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
block|}
end_class

end_unit
