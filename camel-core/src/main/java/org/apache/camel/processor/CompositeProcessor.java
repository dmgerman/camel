begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Processor
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
name|ServiceSupport
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
name|ServiceHelper
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_comment
comment|/**  * Represents a composite pattern, aggregating a collection of processors together as a single processor  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|CompositeProcessor
specifier|public
class|class
name|CompositeProcessor
parameter_list|<
name|E
parameter_list|>
extends|extends
name|ServiceSupport
implements|implements
name|Processor
argument_list|<
name|E
argument_list|>
block|{
DECL|field|processors
specifier|private
specifier|final
name|Collection
argument_list|<
name|Processor
argument_list|<
name|E
argument_list|>
argument_list|>
name|processors
decl_stmt|;
DECL|method|CompositeProcessor (Collection<Processor<E>> processors)
specifier|public
name|CompositeProcessor
parameter_list|(
name|Collection
argument_list|<
name|Processor
argument_list|<
name|E
argument_list|>
argument_list|>
name|processors
parameter_list|)
block|{
name|this
operator|.
name|processors
operator|=
name|processors
expr_stmt|;
block|}
DECL|method|onExchange (E exchange)
specifier|public
name|void
name|onExchange
parameter_list|(
name|E
name|exchange
parameter_list|)
block|{
for|for
control|(
name|Processor
argument_list|<
name|E
argument_list|>
name|processor
range|:
name|processors
control|)
block|{
name|processor
operator|.
name|onExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"[ "
argument_list|)
decl_stmt|;
name|boolean
name|first
init|=
literal|true
decl_stmt|;
for|for
control|(
name|Processor
argument_list|<
name|E
argument_list|>
name|processor
range|:
name|processors
control|)
block|{
if|if
condition|(
name|first
condition|)
block|{
name|first
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|builder
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
name|builder
operator|.
name|append
argument_list|(
name|processor
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|builder
operator|.
name|append
argument_list|(
literal|" ]"
argument_list|)
expr_stmt|;
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|getProcessors ()
specifier|public
name|Collection
argument_list|<
name|Processor
argument_list|<
name|E
argument_list|>
argument_list|>
name|getProcessors
parameter_list|()
block|{
return|return
name|processors
return|;
block|}
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|startServices
argument_list|(
name|processors
argument_list|)
expr_stmt|;
block|}
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|stopServices
argument_list|(
name|processors
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

