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
name|processor
operator|.
name|FilterProcessor
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|FilterBuilder
specifier|public
class|class
name|FilterBuilder
extends|extends
name|FromBuilder
block|{
DECL|field|predicate
specifier|private
name|Predicate
name|predicate
decl_stmt|;
DECL|method|FilterBuilder (FromBuilder builder, Predicate predicate)
specifier|public
name|FilterBuilder
parameter_list|(
name|FromBuilder
name|builder
parameter_list|,
name|Predicate
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
comment|/**      * Adds another predicate using a logical AND      */
DECL|method|and (Predicate predicate)
specifier|public
name|FilterBuilder
name|and
parameter_list|(
name|Predicate
name|predicate
parameter_list|)
block|{
name|this
operator|.
name|predicate
operator|=
name|PredicateBuilder
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
comment|/**      * Adds another predicate using a logical OR      */
DECL|method|or (Predicate predicate)
specifier|public
name|FilterBuilder
name|or
parameter_list|(
name|Predicate
name|predicate
parameter_list|)
block|{
name|this
operator|.
name|predicate
operator|=
name|PredicateBuilder
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
name|getPredicate
parameter_list|()
block|{
return|return
name|predicate
return|;
block|}
DECL|method|createProcessor ()
specifier|public
name|FilterProcessor
name|createProcessor
parameter_list|()
throws|throws
name|Exception
block|{
comment|// lets create a single processor for all child predicates
name|Processor
name|childProcessor
init|=
name|super
operator|.
name|createProcessor
argument_list|()
decl_stmt|;
return|return
operator|new
name|FilterProcessor
argument_list|(
name|predicate
argument_list|,
name|childProcessor
argument_list|)
return|;
block|}
block|}
end_class

end_unit

