begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
package|;
end_package

begin_comment
comment|/**  * @version $Revision: 519941 $  */
end_comment

begin_class
DECL|class|InterceptorProcessor
specifier|public
class|class
name|InterceptorProcessor
parameter_list|<
name|E
parameter_list|>
implements|implements
name|Processor
argument_list|<
name|E
argument_list|>
block|{
DECL|field|next
specifier|protected
name|Processor
argument_list|<
name|E
argument_list|>
name|next
decl_stmt|;
DECL|method|InterceptorProcessor ()
specifier|public
name|InterceptorProcessor
parameter_list|()
block|{     }
DECL|method|onExchange (E exchange)
specifier|public
name|void
name|onExchange
parameter_list|(
name|E
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|next
operator|!=
literal|null
condition|)
block|{
name|next
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
return|return
literal|"intercept("
operator|+
name|next
operator|+
literal|")"
return|;
block|}
DECL|method|getNext ()
specifier|public
name|Processor
argument_list|<
name|E
argument_list|>
name|getNext
parameter_list|()
block|{
return|return
name|next
return|;
block|}
DECL|method|setNext (Processor<E> next)
specifier|public
name|void
name|setNext
parameter_list|(
name|Processor
argument_list|<
name|E
argument_list|>
name|next
parameter_list|)
block|{
name|this
operator|.
name|next
operator|=
name|next
expr_stmt|;
block|}
block|}
end_class

end_unit

