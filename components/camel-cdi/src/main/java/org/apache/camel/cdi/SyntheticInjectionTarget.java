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
name|function
operator|.
name|Consumer
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
name|Supplier
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

begin_class
annotation|@
name|Vetoed
DECL|class|SyntheticInjectionTarget
class|class
name|SyntheticInjectionTarget
parameter_list|<
name|T
parameter_list|>
implements|implements
name|InjectionTarget
argument_list|<
name|T
argument_list|>
block|{
DECL|field|produce
specifier|private
specifier|final
name|Supplier
argument_list|<
name|T
argument_list|>
name|produce
decl_stmt|;
DECL|field|postConstruct
specifier|private
specifier|final
name|Consumer
argument_list|<
name|T
argument_list|>
name|postConstruct
decl_stmt|;
DECL|field|preDestroy
specifier|private
specifier|final
name|Consumer
argument_list|<
name|T
argument_list|>
name|preDestroy
decl_stmt|;
DECL|method|SyntheticInjectionTarget (Supplier<T> produce)
name|SyntheticInjectionTarget
parameter_list|(
name|Supplier
argument_list|<
name|T
argument_list|>
name|produce
parameter_list|)
block|{
name|this
argument_list|(
name|produce
argument_list|,
name|t
lambda|->
block|{         }
argument_list|)
expr_stmt|;
block|}
DECL|method|SyntheticInjectionTarget (Supplier<T> produce, Consumer<T> postConstruct)
name|SyntheticInjectionTarget
parameter_list|(
name|Supplier
argument_list|<
name|T
argument_list|>
name|produce
parameter_list|,
name|Consumer
argument_list|<
name|T
argument_list|>
name|postConstruct
parameter_list|)
block|{
name|this
argument_list|(
name|produce
argument_list|,
name|postConstruct
argument_list|,
name|t
lambda|->
block|{         }
argument_list|)
expr_stmt|;
block|}
DECL|method|SyntheticInjectionTarget (Supplier<T> produce, Consumer<T> postConstruct, Consumer<T> preDestroy)
name|SyntheticInjectionTarget
parameter_list|(
name|Supplier
argument_list|<
name|T
argument_list|>
name|produce
parameter_list|,
name|Consumer
argument_list|<
name|T
argument_list|>
name|postConstruct
parameter_list|,
name|Consumer
argument_list|<
name|T
argument_list|>
name|preDestroy
parameter_list|)
block|{
name|this
operator|.
name|produce
operator|=
name|produce
expr_stmt|;
name|this
operator|.
name|postConstruct
operator|=
name|postConstruct
expr_stmt|;
name|this
operator|.
name|preDestroy
operator|=
name|preDestroy
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|inject (T instance, CreationalContext<T> ctx)
specifier|public
name|void
name|inject
parameter_list|(
name|T
name|instance
parameter_list|,
name|CreationalContext
argument_list|<
name|T
argument_list|>
name|ctx
parameter_list|)
block|{      }
annotation|@
name|Override
DECL|method|postConstruct (T instance)
specifier|public
name|void
name|postConstruct
parameter_list|(
name|T
name|instance
parameter_list|)
block|{
name|postConstruct
operator|.
name|accept
argument_list|(
name|instance
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|preDestroy (T instance)
specifier|public
name|void
name|preDestroy
parameter_list|(
name|T
name|instance
parameter_list|)
block|{
name|preDestroy
operator|.
name|accept
argument_list|(
name|instance
argument_list|)
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
return|return
name|produce
operator|.
name|get
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|dispose (T instance)
specifier|public
name|void
name|dispose
parameter_list|(
name|T
name|instance
parameter_list|)
block|{      }
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
block|}
end_class

end_unit

