begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Ordered
import|;
end_import

begin_comment
comment|/**  * A comparator to sort {@link Ordered}  */
end_comment

begin_class
DECL|class|OrderedComparator
specifier|public
specifier|final
class|class
name|OrderedComparator
implements|implements
name|Comparator
argument_list|<
name|Object
argument_list|>
block|{
DECL|field|INSTANCE
specifier|private
specifier|static
specifier|final
name|OrderedComparator
name|INSTANCE
init|=
operator|new
name|OrderedComparator
argument_list|()
decl_stmt|;
DECL|field|INSTANCE_REVERSE
specifier|private
specifier|static
specifier|final
name|OrderedComparator
name|INSTANCE_REVERSE
init|=
operator|new
name|OrderedComparator
argument_list|(
literal|true
argument_list|)
decl_stmt|;
DECL|field|reverse
specifier|private
specifier|final
name|boolean
name|reverse
decl_stmt|;
comment|/**      * Favor using the static instance {@link #get()}      */
DECL|method|OrderedComparator ()
specifier|public
name|OrderedComparator
parameter_list|()
block|{
name|this
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
comment|/**      * Favor using the static instance {@link #getReverse()}      */
DECL|method|OrderedComparator (boolean reverse)
specifier|public
name|OrderedComparator
parameter_list|(
name|boolean
name|reverse
parameter_list|)
block|{
name|this
operator|.
name|reverse
operator|=
name|reverse
expr_stmt|;
block|}
comment|/**      * Gets the comparator that sorts a..z      */
DECL|method|get ()
specifier|public
specifier|static
name|OrderedComparator
name|get
parameter_list|()
block|{
return|return
name|INSTANCE
return|;
block|}
comment|/**      * Gets the comparator that sorts z..a (reverse)      */
DECL|method|getReverse ()
specifier|public
specifier|static
name|OrderedComparator
name|getReverse
parameter_list|()
block|{
return|return
name|INSTANCE_REVERSE
return|;
block|}
annotation|@
name|Override
DECL|method|compare (Object o1, Object o2)
specifier|public
name|int
name|compare
parameter_list|(
name|Object
name|o1
parameter_list|,
name|Object
name|o2
parameter_list|)
block|{
name|Integer
name|num1
init|=
literal|0
decl_stmt|;
name|Integer
name|num2
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|o1
operator|instanceof
name|Ordered
condition|)
block|{
name|num1
operator|=
operator|(
operator|(
name|Ordered
operator|)
name|o1
operator|)
operator|.
name|getOrder
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|o2
operator|instanceof
name|Ordered
condition|)
block|{
name|num2
operator|=
operator|(
operator|(
name|Ordered
operator|)
name|o2
operator|)
operator|.
name|getOrder
argument_list|()
expr_stmt|;
block|}
name|int
name|answer
init|=
name|num1
operator|.
name|compareTo
argument_list|(
name|num2
argument_list|)
decl_stmt|;
return|return
name|reverse
condition|?
operator|-
literal|1
operator|*
name|answer
else|:
name|answer
return|;
block|}
block|}
end_class

end_unit

