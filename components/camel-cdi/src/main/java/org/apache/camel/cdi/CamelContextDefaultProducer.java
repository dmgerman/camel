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
name|impl
operator|.
name|DefaultCamelContext
import|;
end_import

begin_class
annotation|@
name|Vetoed
DECL|class|CamelContextDefaultProducer
specifier|final
class|class
name|CamelContextDefaultProducer
implements|implements
name|InjectionTarget
argument_list|<
name|DefaultCamelContext
argument_list|>
block|{
annotation|@
name|Override
DECL|method|produce (CreationalContext<DefaultCamelContext> ctx)
specifier|public
name|DefaultCamelContext
name|produce
parameter_list|(
name|CreationalContext
argument_list|<
name|DefaultCamelContext
argument_list|>
name|ctx
parameter_list|)
block|{
return|return
operator|new
name|DefaultCamelContext
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|inject (DefaultCamelContext instance, CreationalContext<DefaultCamelContext> ctx)
specifier|public
name|void
name|inject
parameter_list|(
name|DefaultCamelContext
name|instance
parameter_list|,
name|CreationalContext
argument_list|<
name|DefaultCamelContext
argument_list|>
name|ctx
parameter_list|)
block|{     }
annotation|@
name|Override
DECL|method|postConstruct (DefaultCamelContext instance)
specifier|public
name|void
name|postConstruct
parameter_list|(
name|DefaultCamelContext
name|instance
parameter_list|)
block|{     }
annotation|@
name|Override
DECL|method|preDestroy (DefaultCamelContext instance)
specifier|public
name|void
name|preDestroy
parameter_list|(
name|DefaultCamelContext
name|instance
parameter_list|)
block|{     }
annotation|@
name|Override
DECL|method|dispose (DefaultCamelContext instance)
specifier|public
name|void
name|dispose
parameter_list|(
name|DefaultCamelContext
name|instance
parameter_list|)
block|{     }
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

