begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|StringJoiner
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Function
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
name|CreationException
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
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|PassivationCapable
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
name|CdiSpiHelper
operator|.
name|createBeanId
import|;
end_import

begin_class
DECL|class|SyntheticBean
class|class
name|SyntheticBean
parameter_list|<
name|T
parameter_list|>
extends|extends
name|SyntheticBeanAttributes
argument_list|<
name|T
argument_list|>
implements|implements
name|Bean
argument_list|<
name|T
argument_list|>
implements|,
name|PassivationCapable
block|{
DECL|field|type
specifier|private
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|type
decl_stmt|;
DECL|field|target
specifier|private
specifier|final
name|InjectionTarget
argument_list|<
name|T
argument_list|>
name|target
decl_stmt|;
DECL|field|toString
specifier|private
specifier|final
name|Function
argument_list|<
name|Bean
argument_list|<
name|T
argument_list|>
argument_list|,
name|String
argument_list|>
name|toString
decl_stmt|;
DECL|method|SyntheticBean (BeanManager manager, SyntheticAnnotated annotated, Class<?> type, InjectionTarget<T> target, Function<Bean<T>, String> toString)
name|SyntheticBean
parameter_list|(
name|BeanManager
name|manager
parameter_list|,
name|SyntheticAnnotated
name|annotated
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|InjectionTarget
argument_list|<
name|T
argument_list|>
name|target
parameter_list|,
name|Function
argument_list|<
name|Bean
argument_list|<
name|T
argument_list|>
argument_list|,
name|String
argument_list|>
name|toString
parameter_list|)
block|{
name|super
argument_list|(
name|manager
argument_list|,
name|annotated
argument_list|)
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|target
operator|=
name|target
expr_stmt|;
name|this
operator|.
name|toString
operator|=
name|toString
expr_stmt|;
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
name|type
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
try|try
block|{
name|T
name|instance
init|=
name|target
operator|.
name|produce
argument_list|(
name|creationalContext
argument_list|)
decl_stmt|;
name|target
operator|.
name|inject
argument_list|(
name|instance
argument_list|,
name|creationalContext
argument_list|)
expr_stmt|;
name|target
operator|.
name|postConstruct
argument_list|(
name|instance
argument_list|)
expr_stmt|;
return|return
name|instance
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|cause
parameter_list|)
block|{
throw|throw
operator|new
name|CreationException
argument_list|(
literal|"Error while instantiating "
operator|+
name|this
argument_list|,
name|cause
argument_list|)
throw|;
block|}
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
try|try
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
block|}
catch|catch
parameter_list|(
name|Exception
name|cause
parameter_list|)
block|{
throw|throw
operator|new
name|InjectionException
argument_list|(
literal|"Error while destroying "
operator|+
name|this
argument_list|,
name|cause
argument_list|)
throw|;
block|}
finally|finally
block|{
name|creationalContext
operator|.
name|release
argument_list|()
expr_stmt|;
block|}
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
name|Collections
operator|.
name|emptySet
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
literal|false
return|;
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
name|toString
operator|.
name|apply
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
operator|new
name|StringJoiner
argument_list|(
literal|"%"
argument_list|)
operator|.
name|add
argument_list|(
literal|"CAMEL-CDI"
argument_list|)
operator|.
name|add
argument_list|(
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|)
operator|.
name|add
argument_list|(
name|type
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|add
argument_list|(
name|createBeanId
argument_list|(
name|this
argument_list|)
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

