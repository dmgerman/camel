begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.cdi.internal
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cdi
operator|.
name|internal
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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|ApplicationScoped
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
name|BeanManager
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
name|InjectionTarget
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
name|cdi
operator|.
name|CdiCamelContext
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|deltaspike
operator|.
name|core
operator|.
name|api
operator|.
name|literal
operator|.
name|AnyLiteral
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|deltaspike
operator|.
name|core
operator|.
name|api
operator|.
name|literal
operator|.
name|DefaultLiteral
import|;
end_import

begin_comment
comment|/**  * Description of camel context bean.  */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|class|CamelContextBean
specifier|public
class|class
name|CamelContextBean
implements|implements
name|Bean
argument_list|<
name|CdiCamelContext
argument_list|>
block|{
DECL|field|beanManager
specifier|private
specifier|final
name|BeanManager
name|beanManager
decl_stmt|;
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
DECL|field|camelContextName
specifier|private
specifier|final
name|String
name|camelContextName
decl_stmt|;
DECL|field|target
specifier|private
specifier|final
name|InjectionTarget
argument_list|<
name|CdiCamelContext
argument_list|>
name|target
decl_stmt|;
DECL|field|config
specifier|private
specifier|final
name|CamelContextConfig
name|config
decl_stmt|;
DECL|method|CamelContextBean (BeanManager beanManager)
specifier|public
name|CamelContextBean
parameter_list|(
name|BeanManager
name|beanManager
parameter_list|)
block|{
name|this
argument_list|(
name|beanManager
argument_list|,
literal|"CamelContext"
argument_list|,
literal|""
argument_list|,
operator|new
name|CamelContextConfig
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|CamelContextBean (BeanManager beanManager, String name, String camelContextName, CamelContextConfig config)
specifier|public
name|CamelContextBean
parameter_list|(
name|BeanManager
name|beanManager
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|camelContextName
parameter_list|,
name|CamelContextConfig
name|config
parameter_list|)
block|{
name|this
operator|.
name|beanManager
operator|=
name|beanManager
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|camelContextName
operator|=
name|camelContextName
expr_stmt|;
name|this
operator|.
name|config
operator|=
name|config
expr_stmt|;
name|this
operator|.
name|target
operator|=
name|beanManager
operator|.
name|createInjectionTarget
argument_list|(
name|beanManager
operator|.
name|createAnnotatedType
argument_list|(
name|CdiCamelContext
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|create (CreationalContext<CdiCamelContext> context)
specifier|public
name|CdiCamelContext
name|create
parameter_list|(
name|CreationalContext
argument_list|<
name|CdiCamelContext
argument_list|>
name|context
parameter_list|)
block|{
name|CdiCamelContext
name|camelContext
init|=
name|target
operator|.
name|produce
argument_list|(
name|context
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|camelContextName
argument_list|)
condition|)
block|{
name|camelContext
operator|.
name|setName
argument_list|(
name|camelContextName
argument_list|)
expr_stmt|;
block|}
name|target
operator|.
name|postConstruct
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
name|context
operator|.
name|push
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
return|return
name|camelContext
return|;
block|}
annotation|@
name|Override
DECL|method|destroy (CdiCamelContext instance, CreationalContext<CdiCamelContext> context)
specifier|public
name|void
name|destroy
parameter_list|(
name|CdiCamelContext
name|instance
parameter_list|,
name|CreationalContext
argument_list|<
name|CdiCamelContext
argument_list|>
name|context
parameter_list|)
block|{
name|target
operator|.
name|preDestroy
argument_list|(
name|instance
argument_list|)
expr_stmt|;
name|target
operator|.
name|dispose
argument_list|(
name|instance
argument_list|)
expr_stmt|;
name|context
operator|.
name|release
argument_list|()
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
operator|new
name|HashSet
argument_list|<
name|Type
argument_list|>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|Object
operator|.
name|class
argument_list|,
name|CamelContext
operator|.
name|class
argument_list|,
name|CdiCamelContext
operator|.
name|class
argument_list|)
argument_list|)
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
operator|new
name|HashSet
argument_list|<
name|Annotation
argument_list|>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|DefaultLiteral
argument_list|()
argument_list|,
operator|new
name|AnyLiteral
argument_list|()
argument_list|)
argument_list|)
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
name|ApplicationScoped
operator|.
name|class
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
name|name
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
literal|false
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
name|target
operator|.
name|getInjectionPoints
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
name|CdiCamelContext
operator|.
name|class
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
operator|new
name|HashSet
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
argument_list|>
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
literal|false
return|;
block|}
DECL|method|getCamelContextName ()
specifier|public
name|String
name|getCamelContextName
parameter_list|()
block|{
return|return
name|camelContextName
return|;
block|}
DECL|method|configureCamelContext (CdiCamelContext camelContext)
specifier|public
name|void
name|configureCamelContext
parameter_list|(
name|CdiCamelContext
name|camelContext
parameter_list|)
block|{
name|config
operator|.
name|configure
argument_list|(
name|camelContext
argument_list|,
name|beanManager
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

