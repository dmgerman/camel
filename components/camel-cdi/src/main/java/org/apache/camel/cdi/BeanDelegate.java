begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.cdi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cdi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Annotation
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Type
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|context
operator|.
name|spi
operator|.
name|CreationalContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|Bean
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|InjectionPoint
import|;
end_import

begin_class
DECL|class|BeanDelegate
specifier|final
class|class
name|BeanDelegate
parameter_list|<
name|T
parameter_list|>
implements|implements
name|Bean
argument_list|<
name|T
argument_list|>
block|{
DECL|field|delegate
specifier|private
specifier|final
name|Bean
argument_list|<
name|T
argument_list|>
name|delegate
decl_stmt|;
DECL|field|qualifiers
specifier|private
specifier|final
name|Set
argument_list|<
name|Annotation
argument_list|>
name|qualifiers
decl_stmt|;
DECL|method|BeanDelegate (Bean<T> delegate, Set<? extends Annotation> qualifiers)
name|BeanDelegate
parameter_list|(
name|Bean
argument_list|<
name|T
argument_list|>
name|delegate
parameter_list|,
name|Set
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|qualifiers
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
name|this
operator|.
name|qualifiers
operator|=
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|qualifiers
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getTypes ()
specifier|public
name|Set
argument_list|<
name|Type
argument_list|>
name|getTypes
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|getTypes
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getQualifiers ()
specifier|public
name|Set
argument_list|<
name|Annotation
argument_list|>
name|getQualifiers
parameter_list|()
block|{
return|return
name|qualifiers
return|;
block|}
annotation|@
name|Override
DECL|method|getScope ()
specifier|public
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|getScope
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|getScope
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|getName
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getStereotypes ()
specifier|public
name|Set
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
argument_list|>
name|getStereotypes
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|getStereotypes
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isAlternative ()
specifier|public
name|boolean
name|isAlternative
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|isAlternative
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getBeanClass ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getBeanClass
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|getBeanClass
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getInjectionPoints ()
specifier|public
name|Set
argument_list|<
name|InjectionPoint
argument_list|>
name|getInjectionPoints
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|getInjectionPoints
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isNullable ()
specifier|public
name|boolean
name|isNullable
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|isNullable
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|create (CreationalContext<T> creationalContext)
specifier|public
name|T
name|create
parameter_list|(
name|CreationalContext
argument_list|<
name|T
argument_list|>
name|creationalContext
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|create
argument_list|(
name|creationalContext
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|destroy (T instance, CreationalContext<T> creationalContext)
specifier|public
name|void
name|destroy
parameter_list|(
name|T
name|instance
parameter_list|,
name|CreationalContext
argument_list|<
name|T
argument_list|>
name|creationalContext
parameter_list|)
block|{
name|delegate
operator|.
name|destroy
argument_list|(
name|instance
argument_list|,
name|creationalContext
argument_list|)
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
name|delegate
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

