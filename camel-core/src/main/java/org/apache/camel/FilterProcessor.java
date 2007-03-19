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
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|FilterProcessor
specifier|public
class|class
name|FilterProcessor
parameter_list|<
name|E
parameter_list|>
implements|implements
name|Processor
argument_list|<
name|E
argument_list|>
block|{
DECL|field|predicate
specifier|private
name|Predicate
argument_list|<
name|E
argument_list|>
name|predicate
decl_stmt|;
DECL|field|processor
specifier|private
name|Processor
argument_list|<
name|E
argument_list|>
name|processor
decl_stmt|;
DECL|method|FilterProcessor (Predicate<E> predicate, Processor<E> processor)
specifier|public
name|FilterProcessor
parameter_list|(
name|Predicate
argument_list|<
name|E
argument_list|>
name|predicate
parameter_list|,
name|Processor
argument_list|<
name|E
argument_list|>
name|processor
parameter_list|)
block|{
name|this
operator|.
name|predicate
operator|=
name|predicate
expr_stmt|;
name|this
operator|.
name|processor
operator|=
name|processor
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
if|if
condition|(
name|predicate
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|)
condition|)
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
return|return
literal|"if ("
operator|+
name|predicate
operator|+
literal|") "
operator|+
name|processor
return|;
block|}
DECL|method|getPredicate ()
specifier|public
name|Predicate
argument_list|<
name|E
argument_list|>
name|getPredicate
parameter_list|()
block|{
return|return
name|predicate
return|;
block|}
DECL|method|getProcessor ()
specifier|public
name|Processor
argument_list|<
name|E
argument_list|>
name|getProcessor
parameter_list|()
block|{
return|return
name|processor
return|;
block|}
block|}
end_class

end_unit

