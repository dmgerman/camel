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
name|util
operator|.
name|ServiceHelper
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

begin_comment
comment|/**  * Implements a Choice structure where one or more predicates are used which if they are true their processors  * are used, with a default otherwise clause used if none match.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|ChoiceProcessor
specifier|public
class|class
name|ChoiceProcessor
extends|extends
name|ServiceSupport
implements|implements
name|Processor
block|{
DECL|field|filters
specifier|private
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
DECL|field|otherwise
specifier|private
name|Processor
name|otherwise
decl_stmt|;
DECL|method|ChoiceProcessor (List<FilterProcessor> filters, Processor otherwise)
specifier|public
name|ChoiceProcessor
parameter_list|(
name|List
argument_list|<
name|FilterProcessor
argument_list|>
name|filters
parameter_list|,
name|Processor
name|otherwise
parameter_list|)
block|{
name|this
operator|.
name|filters
operator|=
name|filters
expr_stmt|;
name|this
operator|.
name|otherwise
operator|=
name|otherwise
expr_stmt|;
block|}
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
for|for
control|(
name|FilterProcessor
name|filterProcessor
range|:
name|filters
control|)
block|{
name|Predicate
argument_list|<
name|Exchange
argument_list|>
name|predicate
init|=
name|filterProcessor
operator|.
name|getPredicate
argument_list|()
decl_stmt|;
if|if
condition|(
name|predicate
operator|!=
literal|null
operator|&&
name|predicate
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
name|filterProcessor
operator|.
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
if|if
condition|(
name|otherwise
operator|!=
literal|null
condition|)
block|{
name|otherwise
operator|.
name|process
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
literal|"choice{"
argument_list|)
decl_stmt|;
name|boolean
name|first
init|=
literal|true
decl_stmt|;
for|for
control|(
name|FilterProcessor
name|processor
range|:
name|filters
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
literal|"when "
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|processor
operator|.
name|getPredicate
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|": "
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|processor
operator|.
name|getProcessor
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|otherwise
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
literal|", otherwise: "
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|otherwise
argument_list|)
expr_stmt|;
block|}
name|builder
operator|.
name|append
argument_list|(
literal|"}"
argument_list|)
expr_stmt|;
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|getFilters ()
specifier|public
name|List
argument_list|<
name|FilterProcessor
argument_list|>
name|getFilters
parameter_list|()
block|{
return|return
name|filters
return|;
block|}
DECL|method|getOtherwise ()
specifier|public
name|Processor
name|getOtherwise
parameter_list|()
block|{
return|return
name|otherwise
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
name|filters
argument_list|)
expr_stmt|;
name|ServiceHelper
operator|.
name|startServices
argument_list|(
name|otherwise
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
name|otherwise
argument_list|)
expr_stmt|;
name|ServiceHelper
operator|.
name|stopServices
argument_list|(
name|filters
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

