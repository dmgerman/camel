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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/**  * A helper class for creating delegate implementations of {@link InjectionTarget}  */
end_comment

begin_class
DECL|class|DelegateInjectionTarget
specifier|public
specifier|abstract
class|class
name|DelegateInjectionTarget
implements|implements
name|InjectionTarget
block|{
DECL|field|delegate
specifier|private
specifier|final
name|InjectionTarget
name|delegate
decl_stmt|;
DECL|method|DelegateInjectionTarget (InjectionTarget<Object> delegate)
specifier|public
name|DelegateInjectionTarget
parameter_list|(
name|InjectionTarget
argument_list|<
name|Object
argument_list|>
name|delegate
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|dispose (Object instance)
specifier|public
name|void
name|dispose
parameter_list|(
name|Object
name|instance
parameter_list|)
block|{
name|delegate
operator|.
name|dispose
argument_list|(
name|instance
argument_list|)
expr_stmt|;
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
DECL|method|inject (Object instance, CreationalContext ctx)
specifier|public
name|void
name|inject
parameter_list|(
name|Object
name|instance
parameter_list|,
name|CreationalContext
name|ctx
parameter_list|)
block|{
name|delegate
operator|.
name|inject
argument_list|(
name|instance
argument_list|,
name|ctx
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|postConstruct (Object instance)
specifier|public
name|void
name|postConstruct
parameter_list|(
name|Object
name|instance
parameter_list|)
block|{
name|delegate
operator|.
name|postConstruct
argument_list|(
name|instance
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|preDestroy (Object instance)
specifier|public
name|void
name|preDestroy
parameter_list|(
name|Object
name|instance
parameter_list|)
block|{
name|delegate
operator|.
name|preDestroy
argument_list|(
name|instance
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|produce (CreationalContext creationalContext)
specifier|public
name|Object
name|produce
parameter_list|(
name|CreationalContext
name|creationalContext
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|produce
argument_list|(
name|creationalContext
argument_list|)
return|;
block|}
block|}
end_class

end_unit

