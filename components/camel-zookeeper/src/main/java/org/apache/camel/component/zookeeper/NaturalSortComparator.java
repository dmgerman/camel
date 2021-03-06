begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.zookeeper
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|zookeeper
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
import|;
end_import

begin_comment
comment|/**  *<code>NaturalSortComparator</code> is a fast comparator for sorting a  * collection of Strings in a human readable fashion.  *<p>  * This implementation considers sequences of digits to be positive integer  * values, '.' does not indicate a decimal value nor '-' a negative one. As a  * result, 12345.12345 will sort higher than 12345.5432 and -12346 will sort  * higher than 12345.  *<p>  * it does work well for sorting software versions e.g. camel-2.2.0 sorting  * higher than camel-2.1.0  */
end_comment

begin_class
DECL|class|NaturalSortComparator
specifier|public
class|class
name|NaturalSortComparator
implements|implements
name|Comparator
argument_list|<
name|CharSequence
argument_list|>
block|{
DECL|enum|Order
specifier|public
enum|enum
name|Order
block|{
DECL|enumConstant|Ascending
DECL|enumConstant|Descending
name|Ascending
argument_list|(
literal|1
argument_list|)
block|,
name|Descending
argument_list|(
operator|-
literal|1
argument_list|)
block|;
DECL|field|direction
name|int
name|direction
decl_stmt|;
DECL|method|Order (int direction)
name|Order
parameter_list|(
name|int
name|direction
parameter_list|)
block|{
name|this
operator|.
name|direction
operator|=
name|direction
expr_stmt|;
block|}
block|}
DECL|field|order
specifier|private
name|Order
name|order
decl_stmt|;
DECL|method|NaturalSortComparator ()
specifier|public
name|NaturalSortComparator
parameter_list|()
block|{
name|this
argument_list|(
name|Order
operator|.
name|Ascending
argument_list|)
expr_stmt|;
block|}
DECL|method|NaturalSortComparator (Order order)
specifier|public
name|NaturalSortComparator
parameter_list|(
name|Order
name|order
parameter_list|)
block|{
if|if
condition|(
name|order
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|order
operator|=
name|order
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|compare (CharSequence first, CharSequence second)
specifier|public
name|int
name|compare
parameter_list|(
name|CharSequence
name|first
parameter_list|,
name|CharSequence
name|second
parameter_list|)
block|{
if|if
condition|(
name|first
operator|==
literal|null
operator|&&
name|second
operator|==
literal|null
condition|)
block|{
return|return
literal|0
return|;
block|}
if|if
condition|(
name|first
operator|!=
literal|null
operator|&&
name|second
operator|==
literal|null
condition|)
block|{
return|return
literal|1
return|;
block|}
if|if
condition|(
name|first
operator|==
literal|null
operator|&&
name|second
operator|!=
literal|null
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
name|int
name|compare
init|=
literal|0
decl_stmt|;
name|int
name|fx
init|=
literal|0
decl_stmt|;
name|int
name|sx
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|fx
operator|<
name|first
operator|.
name|length
argument_list|()
operator|&&
name|sx
operator|<
name|second
operator|.
name|length
argument_list|()
operator|&&
name|compare
operator|==
literal|0
condition|)
block|{
if|if
condition|(
name|isDigit
argument_list|(
name|first
operator|.
name|charAt
argument_list|(
name|fx
argument_list|)
argument_list|)
operator|&&
name|isDigit
argument_list|(
name|second
operator|.
name|charAt
argument_list|(
name|sx
argument_list|)
argument_list|)
condition|)
block|{
name|int
name|flen
init|=
name|getNumSequenceLength
argument_list|(
name|first
argument_list|,
name|fx
argument_list|)
decl_stmt|;
name|int
name|slen
init|=
name|getNumSequenceLength
argument_list|(
name|second
argument_list|,
name|sx
argument_list|)
decl_stmt|;
if|if
condition|(
name|flen
operator|==
name|slen
condition|)
block|{
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|flen
operator|&&
name|compare
operator|==
literal|0
condition|;
name|x
operator|++
control|)
block|{
comment|/** the first difference in digit wins */
name|compare
operator|=
name|first
operator|.
name|charAt
argument_list|(
name|fx
operator|++
argument_list|)
operator|-
name|second
operator|.
name|charAt
argument_list|(
name|sx
operator|++
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|compare
operator|=
name|flen
operator|-
name|slen
expr_stmt|;
block|}
block|}
else|else
block|{
name|compare
operator|=
name|first
operator|.
name|charAt
argument_list|(
name|fx
argument_list|)
operator|-
name|second
operator|.
name|charAt
argument_list|(
name|sx
argument_list|)
expr_stmt|;
block|}
name|fx
operator|++
expr_stmt|;
name|sx
operator|++
expr_stmt|;
block|}
if|if
condition|(
name|compare
operator|==
literal|0
condition|)
block|{
name|compare
operator|=
name|first
operator|.
name|length
argument_list|()
operator|-
name|second
operator|.
name|length
argument_list|()
expr_stmt|;
block|}
return|return
name|order
operator|.
name|direction
operator|*
name|compare
return|;
block|}
DECL|method|isDigit (char c)
specifier|private
name|boolean
name|isDigit
parameter_list|(
name|char
name|c
parameter_list|)
block|{
return|return
name|c
operator|>=
literal|48
operator|&&
name|c
operator|<
literal|57
return|;
block|}
DECL|method|getNumSequenceLength (CharSequence sequence, int index)
specifier|private
name|int
name|getNumSequenceLength
parameter_list|(
name|CharSequence
name|sequence
parameter_list|,
name|int
name|index
parameter_list|)
block|{
name|int
name|x
init|=
name|index
decl_stmt|;
while|while
condition|(
name|x
operator|<
name|sequence
operator|.
name|length
argument_list|()
operator|&&
name|isDigit
argument_list|(
name|sequence
operator|.
name|charAt
argument_list|(
name|x
argument_list|)
argument_list|)
condition|)
block|{
name|x
operator|++
expr_stmt|;
block|}
return|return
name|x
operator|-
name|index
return|;
block|}
block|}
end_class

end_unit

