begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|spi
operator|.
name|CamelContextNameStrategy
import|;
end_import

begin_comment
comment|/**  * A default name strategy which auto assigns a name using a prefix-counter pattern.  */
end_comment

begin_class
DECL|class|DefaultCamelContextNameStrategy
specifier|public
class|class
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
DECL|field|prefix
specifier|private
specifier|final
name|String
name|prefix
decl_stmt|;
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
DECL|method|DefaultCamelContextNameStrategy ()
specifier|public
name|DefaultCamelContextNameStrategy
parameter_list|()
block|{
name|this
argument_list|(
literal|"camel"
argument_list|)
expr_stmt|;
block|}
DECL|method|DefaultCamelContextNameStrategy (String prefix)
specifier|public
name|DefaultCamelContextNameStrategy
parameter_list|(
name|String
name|prefix
parameter_list|)
block|{
name|this
operator|.
name|prefix
operator|=
name|prefix
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|getNextName
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
name|name
operator|=
name|getNextName
argument_list|()
expr_stmt|;
block|}
return|return
name|name
return|;
block|}
annotation|@
name|Override
DECL|method|getNextName ()
specifier|public
name|String
name|getNextName
parameter_list|()
block|{
return|return
name|prefix
operator|+
literal|"-"
operator|+
name|getNextCounter
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isFixedName ()
specifier|public
name|boolean
name|isFixedName
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
DECL|method|getNextCounter ()
specifier|public
specifier|static
name|int
name|getNextCounter
parameter_list|()
block|{
comment|// we want to start counting from 1, so increment first
return|return
name|CONTEXT_COUNTER
operator|.
name|incrementAndGet
argument_list|()
return|;
block|}
comment|/**      * To reset the counter, should only be used for testing purposes.      *      * @param value the counter value      */
DECL|method|setCounter (int value)
specifier|public
specifier|static
name|void
name|setCounter
parameter_list|(
name|int
name|value
parameter_list|)
block|{
name|CONTEXT_COUNTER
operator|.
name|set
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

