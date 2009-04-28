begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.loadbalancer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|loadbalancer
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CopyOnWriteArrayList
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
name|Navigate
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

begin_comment
comment|/**  * A default base class for a {@link LoadBalancer} implementation  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|LoadBalancerSupport
specifier|public
specifier|abstract
class|class
name|LoadBalancerSupport
extends|extends
name|ServiceSupport
implements|implements
name|LoadBalancer
implements|,
name|Navigate
argument_list|<
name|Processor
argument_list|>
block|{
DECL|field|processors
specifier|private
specifier|final
name|List
argument_list|<
name|Processor
argument_list|>
name|processors
init|=
operator|new
name|CopyOnWriteArrayList
argument_list|<
name|Processor
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|addProcessor (Processor processor)
specifier|public
name|void
name|addProcessor
parameter_list|(
name|Processor
name|processor
parameter_list|)
block|{
name|processors
operator|.
name|add
argument_list|(
name|processor
argument_list|)
expr_stmt|;
block|}
DECL|method|removeProcessor (Processor processor)
specifier|public
name|void
name|removeProcessor
parameter_list|(
name|Processor
name|processor
parameter_list|)
block|{
name|processors
operator|.
name|remove
argument_list|(
name|processor
argument_list|)
expr_stmt|;
block|}
DECL|method|getProcessors ()
specifier|public
name|List
argument_list|<
name|Processor
argument_list|>
name|getProcessors
parameter_list|()
block|{
return|return
name|processors
return|;
block|}
DECL|method|next ()
specifier|public
name|List
argument_list|<
name|Processor
argument_list|>
name|next
parameter_list|()
block|{
if|if
condition|(
operator|!
name|hasNext
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
operator|new
name|ArrayList
argument_list|<
name|Processor
argument_list|>
argument_list|(
name|processors
argument_list|)
return|;
block|}
DECL|method|hasNext ()
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|processors
operator|.
name|size
argument_list|()
operator|>
literal|0
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

