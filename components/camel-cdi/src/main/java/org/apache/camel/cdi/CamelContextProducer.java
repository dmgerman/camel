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
name|beans
operator|.
name|Introspector
import|;
end_import

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
name|InjectionException
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
name|Annotated
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
name|AnnotatedField
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
name|AnnotatedMethod
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
name|Producer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Named
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
name|impl
operator|.
name|DefaultCamelContext
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
name|impl
operator|.
name|DefaultCamelContextNameStrategy
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
name|impl
operator|.
name|ExplicitCamelContextNameStrategy
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
name|CamelContextNameStrategy
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cdi
operator|.
name|AnyLiteral
operator|.
name|ANY
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cdi
operator|.
name|DefaultLiteral
operator|.
name|DEFAULT
import|;
end_import

begin_class
DECL|class|CamelContextProducer
specifier|final
class|class
name|CamelContextProducer
parameter_list|<
name|T
extends|extends
name|CamelContext
parameter_list|>
extends|extends
name|DelegateProducer
argument_list|<
name|T
argument_list|>
block|{
DECL|field|logger
specifier|private
specifier|final
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|annotated
specifier|private
specifier|final
name|Annotated
name|annotated
decl_stmt|;
DECL|field|manager
specifier|private
specifier|final
name|BeanManager
name|manager
decl_stmt|;
DECL|field|extension
specifier|private
specifier|final
name|CdiCamelExtension
name|extension
decl_stmt|;
DECL|method|CamelContextProducer (Producer<T> delegate, Annotated annotated, BeanManager manager, CdiCamelExtension extension)
name|CamelContextProducer
parameter_list|(
name|Producer
argument_list|<
name|T
argument_list|>
name|delegate
parameter_list|,
name|Annotated
name|annotated
parameter_list|,
name|BeanManager
name|manager
parameter_list|,
name|CdiCamelExtension
name|extension
parameter_list|)
block|{
name|super
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
name|this
operator|.
name|annotated
operator|=
name|annotated
expr_stmt|;
name|this
operator|.
name|manager
operator|=
name|manager
expr_stmt|;
name|this
operator|.
name|extension
operator|=
name|extension
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|produce (CreationalContext<T> ctx)
specifier|public
name|T
name|produce
parameter_list|(
name|CreationalContext
argument_list|<
name|T
argument_list|>
name|ctx
parameter_list|)
block|{
name|T
name|context
init|=
name|super
operator|.
name|produce
argument_list|(
name|ctx
argument_list|)
decl_stmt|;
comment|// Do not override the name if it's been already set (in the bean constructor for example)
if|if
condition|(
name|context
operator|.
name|getNameStrategy
argument_list|()
operator|instanceof
name|DefaultCamelContextNameStrategy
condition|)
block|{
name|context
operator|.
name|setNameStrategy
argument_list|(
name|nameStrategy
argument_list|(
name|annotated
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// Add bean registry and Camel injector
if|if
condition|(
name|context
operator|instanceof
name|DefaultCamelContext
condition|)
block|{
name|DefaultCamelContext
name|adapted
init|=
name|context
operator|.
name|adapt
argument_list|(
name|DefaultCamelContext
operator|.
name|class
argument_list|)
decl_stmt|;
name|adapted
operator|.
name|setRegistry
argument_list|(
operator|new
name|CdiCamelRegistry
argument_list|(
name|manager
argument_list|)
argument_list|)
expr_stmt|;
name|adapted
operator|.
name|setInjector
argument_list|(
operator|new
name|CdiCamelInjector
argument_list|(
name|context
operator|.
name|getInjector
argument_list|()
argument_list|,
name|manager
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// Fail fast for the time being to avoid side effects by the time these two methods get declared on the CamelContext interface
throw|throw
operator|new
name|InjectionException
argument_list|(
literal|"Camel CDI requires Camel context ["
operator|+
name|context
operator|.
name|getName
argument_list|()
operator|+
literal|"] to be a subtype of DefaultCamelContext"
argument_list|)
throw|;
block|}
comment|// Add event notifier if at least one observer is present
name|Set
argument_list|<
name|Annotation
argument_list|>
name|qualifiers
init|=
name|CdiSpiHelper
operator|.
name|excludeElementOfTypes
argument_list|(
name|CdiSpiHelper
operator|.
name|getQualifiers
argument_list|(
name|annotated
argument_list|,
name|manager
argument_list|)
argument_list|,
name|Named
operator|.
name|class
argument_list|)
decl_stmt|;
name|qualifiers
operator|.
name|add
argument_list|(
name|ANY
argument_list|)
expr_stmt|;
if|if
condition|(
name|qualifiers
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|qualifiers
operator|.
name|add
argument_list|(
name|DEFAULT
argument_list|)
expr_stmt|;
block|}
name|qualifiers
operator|.
name|retainAll
argument_list|(
name|extension
operator|.
name|getObserverEvents
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|qualifiers
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|context
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|addEventNotifier
argument_list|(
operator|new
name|CdiEventNotifier
argument_list|(
name|manager
argument_list|,
name|qualifiers
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|context
return|;
block|}
annotation|@
name|Override
DECL|method|dispose (T context)
specifier|public
name|void
name|dispose
parameter_list|(
name|T
name|context
parameter_list|)
block|{
name|super
operator|.
name|dispose
argument_list|(
name|context
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|context
operator|.
name|getStatus
argument_list|()
operator|.
name|isStopped
argument_list|()
condition|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"Camel CDI is stopping Camel context [{}]"
argument_list|,
name|context
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|cause
parameter_list|)
block|{
throw|throw
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|cause
argument_list|)
throw|;
block|}
block|}
block|}
DECL|method|nameStrategy (Annotated annotated)
specifier|private
specifier|static
name|CamelContextNameStrategy
name|nameStrategy
parameter_list|(
name|Annotated
name|annotated
parameter_list|)
block|{
if|if
condition|(
name|annotated
operator|.
name|isAnnotationPresent
argument_list|(
name|ContextName
operator|.
name|class
argument_list|)
condition|)
block|{
return|return
operator|new
name|ExplicitCamelContextNameStrategy
argument_list|(
name|annotated
operator|.
name|getAnnotation
argument_list|(
name|ContextName
operator|.
name|class
argument_list|)
operator|.
name|value
argument_list|()
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|annotated
operator|.
name|isAnnotationPresent
argument_list|(
name|Named
operator|.
name|class
argument_list|)
condition|)
block|{
comment|// TODO: support stereotype with empty @Named annotation
name|String
name|name
init|=
name|annotated
operator|.
name|getAnnotation
argument_list|(
name|Named
operator|.
name|class
argument_list|)
operator|.
name|value
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
name|annotated
operator|instanceof
name|AnnotatedField
condition|)
block|{
name|name
operator|=
operator|(
operator|(
name|AnnotatedField
operator|)
name|annotated
operator|)
operator|.
name|getJavaMember
argument_list|()
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|annotated
operator|instanceof
name|AnnotatedMethod
condition|)
block|{
name|name
operator|=
operator|(
operator|(
name|AnnotatedMethod
operator|)
name|annotated
operator|)
operator|.
name|getJavaMember
argument_list|()
operator|.
name|getName
argument_list|()
expr_stmt|;
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
literal|"get"
argument_list|)
condition|)
block|{
name|name
operator|=
name|Introspector
operator|.
name|decapitalize
argument_list|(
name|name
operator|.
name|substring
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|name
operator|=
name|Introspector
operator|.
name|decapitalize
argument_list|(
name|CdiSpiHelper
operator|.
name|getRawType
argument_list|(
name|annotated
operator|.
name|getBaseType
argument_list|()
argument_list|)
operator|.
name|getSimpleName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|ExplicitCamelContextNameStrategy
argument_list|(
name|name
argument_list|)
return|;
block|}
else|else
block|{
comment|// Use a specific naming strategy for Camel CDI as the default one increments the suffix for each CDI proxy created
return|return
operator|new
name|CdiCamelContextNameStrategy
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

