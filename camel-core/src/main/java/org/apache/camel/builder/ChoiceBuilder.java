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
name|ChoiceProcessor
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
DECL|class|ChoiceBuilder
specifier|public
class|class
name|ChoiceBuilder
extends|extends
name|FromBuilder
block|{
DECL|field|parent
specifier|private
specifier|final
name|FromBuilder
name|parent
decl_stmt|;
DECL|field|predicateBuilders
specifier|private
name|List
argument_list|<
name|WhenBuilder
argument_list|>
name|predicateBuilders
init|=
operator|new
name|ArrayList
argument_list|<
name|WhenBuilder
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|otherwise
specifier|private
name|FromBuilder
name|otherwise
decl_stmt|;
DECL|method|ChoiceBuilder (FromBuilder parent)
specifier|public
name|ChoiceBuilder
parameter_list|(
name|FromBuilder
name|parent
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|)
expr_stmt|;
name|this
operator|.
name|parent
operator|=
name|parent
expr_stmt|;
block|}
comment|/**      * Adds a predicate which if it is true then the message exchange is sent to the given destination      *      * @return a builder for creating a when predicate clause and action      */
annotation|@
name|Fluent
argument_list|(
name|nestedActions
operator|=
literal|true
argument_list|)
DECL|method|when ( @luentArgvalue=R,element=true) Predicate predicate)
specifier|public
name|WhenBuilder
name|when
parameter_list|(
annotation|@
name|FluentArg
argument_list|(
name|value
operator|=
literal|"predicate"
argument_list|,
name|element
operator|=
literal|true
argument_list|)
name|Predicate
name|predicate
parameter_list|)
block|{
name|WhenBuilder
name|answer
init|=
operator|new
name|WhenBuilder
argument_list|(
name|this
argument_list|,
name|predicate
argument_list|)
decl_stmt|;
name|predicateBuilders
operator|.
name|add
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
annotation|@
name|Fluent
argument_list|(
name|nestedActions
operator|=
literal|true
argument_list|)
DECL|method|otherwise ()
specifier|public
name|FromBuilder
name|otherwise
parameter_list|()
block|{
name|this
operator|.
name|otherwise
operator|=
operator|new
name|FromBuilder
argument_list|(
name|parent
argument_list|)
expr_stmt|;
return|return
name|otherwise
return|;
block|}
DECL|method|getPredicateBuilders ()
specifier|public
name|List
argument_list|<
name|WhenBuilder
argument_list|>
name|getPredicateBuilders
parameter_list|()
block|{
return|return
name|predicateBuilders
return|;
block|}
DECL|method|getOtherwise ()
specifier|public
name|FromBuilder
name|getOtherwise
parameter_list|()
block|{
return|return
name|otherwise
return|;
block|}
annotation|@
name|Override
DECL|method|createProcessor ()
specifier|public
name|Processor
name|createProcessor
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|FilterProcessor
argument_list|>
name|filters
init|=
operator|new
name|ArrayList
argument_list|<
name|FilterProcessor
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|WhenBuilder
name|predicateBuilder
range|:
name|predicateBuilders
control|)
block|{
name|filters
operator|.
name|add
argument_list|(
name|predicateBuilder
operator|.
name|createProcessor
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Processor
name|otherwiseProcessor
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|otherwise
operator|!=
literal|null
condition|)
block|{
name|otherwiseProcessor
operator|=
name|otherwise
operator|.
name|createProcessor
argument_list|()
expr_stmt|;
block|}
return|return
operator|new
name|ChoiceProcessor
argument_list|(
name|filters
argument_list|,
name|otherwiseProcessor
argument_list|)
return|;
block|}
block|}
end_class

end_unit

