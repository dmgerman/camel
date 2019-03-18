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
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicInteger
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
name|spi
operator|.
name|CamelContextNameStrategy
import|;
end_import

begin_comment
comment|/**  * A {@link CamelContextNameStrategy} for Camel contexts that are managed by Camel CDI.  *  * As opposed to {@link org.apache.camel.impl.DefaultCamelContextNameStrategy},  * this implementation does not increment the suffix for proxies that are created  * each time a contextual reference to a normal-scoped bean is retrieved.  *  * It is used by Camel CDI for custom Camel context beans that do not override  * the context name nor the naming strategy.  *  * @see CamelContextNameStrategy  */
end_comment

begin_class
annotation|@
name|Vetoed
DECL|class|CdiCamelContextNameStrategy
specifier|final
class|class
name|CdiCamelContextNameStrategy
extends|extends
name|DefaultCamelContextNameStrategy
implements|implements
name|CamelContextNameStrategy
block|{
DECL|field|CONTEXT_COUNTER
specifier|private
specifier|static
specifier|final
name|AtomicInteger
name|CONTEXT_COUNTER
init|=
operator|new
name|AtomicInteger
argument_list|(
literal|0
argument_list|)
decl_stmt|;
annotation|@
name|Override
DECL|method|getNextName ()
specifier|public
name|String
name|getNextName
parameter_list|()
block|{
return|return
literal|"camel"
operator|+
literal|"-"
operator|+
name|CONTEXT_COUNTER
operator|.
name|incrementAndGet
argument_list|()
return|;
block|}
block|}
end_class

end_unit

