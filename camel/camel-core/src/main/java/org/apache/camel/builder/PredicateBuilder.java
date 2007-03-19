begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
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
name|Predicate
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
name|Exchange
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
name|Predicates
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|PredicateBuilder
specifier|public
class|class
name|PredicateBuilder
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
extends|extends
name|DestinationBuilder
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
DECL|method|PredicateBuilder (DestinationBuilder<E> builder, Predicate<E> predicate)
specifier|public
name|PredicateBuilder
parameter_list|(
name|DestinationBuilder
argument_list|<
name|E
argument_list|>
name|builder
parameter_list|,
name|Predicate
argument_list|<
name|E
argument_list|>
name|predicate
parameter_list|)
block|{
name|super
argument_list|(
name|builder
argument_list|)
expr_stmt|;
name|this
operator|.
name|predicate
operator|=
name|predicate
expr_stmt|;
block|}
comment|/**      * Adds another predicate using a logican AND      */
DECL|method|and (Predicate<E> predicate)
specifier|public
name|PredicateBuilder
argument_list|<
name|E
argument_list|>
name|and
parameter_list|(
name|Predicate
argument_list|<
name|E
argument_list|>
name|predicate
parameter_list|)
block|{
name|this
operator|.
name|predicate
operator|=
name|Predicates
operator|.
name|and
argument_list|(
name|this
operator|.
name|predicate
argument_list|,
name|predicate
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Adds another predicate using a logican OR      */
DECL|method|or (Predicate<E> predicate)
specifier|public
name|PredicateBuilder
argument_list|<
name|E
argument_list|>
name|or
parameter_list|(
name|Predicate
argument_list|<
name|E
argument_list|>
name|predicate
parameter_list|)
block|{
name|this
operator|.
name|predicate
operator|=
name|Predicates
operator|.
name|or
argument_list|(
name|this
operator|.
name|predicate
argument_list|,
name|predicate
argument_list|)
expr_stmt|;
return|return
name|this
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
block|}
end_class

end_unit

