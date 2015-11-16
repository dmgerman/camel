begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.tools.apt.helper
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|tools
operator|.
name|apt
operator|.
name|helper
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
name|tools
operator|.
name|apt
operator|.
name|model
operator|.
name|EndpointOption
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
name|tools
operator|.
name|apt
operator|.
name|model
operator|.
name|EndpointPath
import|;
end_import

begin_class
DECL|class|EndpointHelper
specifier|public
specifier|final
class|class
name|EndpointHelper
block|{
DECL|method|EndpointHelper ()
specifier|private
name|EndpointHelper
parameter_list|()
block|{     }
comment|/**      * Returns the group name from the given label.      *<p/>      * The group name is a single name deducted from the label. The label can contain multiple names separated by comma.      * The group is the best guess as a group of those labels, so similar labels can be combined into the same group.      *      * @param label          the label      * @param consumerOnly   whether the component is consumer only      * @param producerOnly   whether the component is producer only      * @return the group name      */
DECL|method|labelAsGroupName (String label, boolean consumerOnly, boolean producerOnly)
specifier|public
specifier|static
name|String
name|labelAsGroupName
parameter_list|(
name|String
name|label
parameter_list|,
name|boolean
name|consumerOnly
parameter_list|,
name|boolean
name|producerOnly
parameter_list|)
block|{
comment|// if there is no label then use common as fallback
name|String
name|answer
init|=
literal|"common"
decl_stmt|;
if|if
condition|(
name|consumerOnly
condition|)
block|{
name|answer
operator|=
literal|"consumer"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|producerOnly
condition|)
block|{
name|answer
operator|=
literal|"producer"
expr_stmt|;
block|}
name|String
name|value
init|=
name|label
decl_stmt|;
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|value
argument_list|)
condition|)
block|{
comment|// we want to put advanced into own group, so look for a label that has advanced as prefix x,advanced => x (advanced)
if|if
condition|(
name|value
operator|.
name|contains
argument_list|(
literal|"advanced"
argument_list|)
condition|)
block|{
name|value
operator|=
name|value
operator|.
name|replaceFirst
argument_list|(
literal|"(\\w),(advanced)"
argument_list|,
literal|"$1 (advanced)"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|value
operator|.
name|contains
argument_list|(
literal|","
argument_list|)
condition|)
block|{
name|String
index|[]
name|array
init|=
name|value
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
comment|// grab last label which is the most specific label we want to use for the tab
name|answer
operator|=
name|array
index|[
name|array
operator|.
name|length
operator|-
literal|1
index|]
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
name|value
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
comment|/**      * A comparator to sort the endpoint/component options according to group and label.      */
DECL|method|createGroupAndLabelComparator ()
specifier|public
specifier|static
name|EndpointOptionGroupAndLabelComparator
name|createGroupAndLabelComparator
parameter_list|()
block|{
return|return
operator|new
name|EndpointOptionGroupAndLabelComparator
argument_list|()
return|;
block|}
comment|/**      * A comparator to sort the endpoint paths according to syntax.      *      * @param syntax the endpoint uri syntax      */
DECL|method|createPathComparator (String syntax)
specifier|public
specifier|static
name|EndpointPathComparator
name|createPathComparator
parameter_list|(
name|String
name|syntax
parameter_list|)
block|{
return|return
operator|new
name|EndpointPathComparator
argument_list|(
name|syntax
argument_list|)
return|;
block|}
comment|/**      * To sort the component/endpoint options in human friendly order.      *<p/>      * The order is to include options grouped by      *<ul>      *<li>common</li>      *<li>consumer</li>      *<li>consumer (advanced)</li>      *<li>producer</li>      *<li>consumer (advanced)</li>      *<li>... and the rest sorted by a..z</li>      *</ul>      */
DECL|class|EndpointOptionGroupAndLabelComparator
specifier|private
specifier|static
specifier|final
class|class
name|EndpointOptionGroupAndLabelComparator
implements|implements
name|Comparator
argument_list|<
name|EndpointOption
argument_list|>
block|{
annotation|@
name|Override
DECL|method|compare (EndpointOption o1, EndpointOption o2)
specifier|public
name|int
name|compare
parameter_list|(
name|EndpointOption
name|o1
parameter_list|,
name|EndpointOption
name|o2
parameter_list|)
block|{
name|String
name|name1
init|=
name|o1
operator|.
name|getName
argument_list|()
decl_stmt|;
name|String
name|name2
init|=
name|o2
operator|.
name|getName
argument_list|()
decl_stmt|;
name|String
name|label1
init|=
name|o1
operator|.
name|getLabel
argument_list|()
operator|!=
literal|null
condition|?
name|o1
operator|.
name|getLabel
argument_list|()
else|:
literal|"common"
decl_stmt|;
name|String
name|label2
init|=
name|o2
operator|.
name|getLabel
argument_list|()
operator|!=
literal|null
condition|?
name|o2
operator|.
name|getLabel
argument_list|()
else|:
literal|"common"
decl_stmt|;
name|String
name|group1
init|=
name|o1
operator|.
name|getGroup
argument_list|()
decl_stmt|;
name|String
name|group2
init|=
name|o2
operator|.
name|getGroup
argument_list|()
decl_stmt|;
comment|// if same label or group then sort by name
if|if
condition|(
name|label1
operator|.
name|equalsIgnoreCase
argument_list|(
name|label2
argument_list|)
operator|||
name|group1
operator|.
name|equalsIgnoreCase
argument_list|(
name|group2
argument_list|)
condition|)
block|{
return|return
name|name1
operator|.
name|compareToIgnoreCase
argument_list|(
name|name2
argument_list|)
return|;
block|}
name|int
name|score1
init|=
name|groupScore
argument_list|(
name|group1
argument_list|)
decl_stmt|;
name|int
name|score2
init|=
name|groupScore
argument_list|(
name|group2
argument_list|)
decl_stmt|;
if|if
condition|(
name|score1
operator|<
name|score2
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
elseif|else
if|if
condition|(
name|score2
operator|<
name|score1
condition|)
block|{
return|return
literal|1
return|;
block|}
else|else
block|{
comment|// compare by full label and name
name|int
name|score
init|=
name|label1
operator|.
name|compareToIgnoreCase
argument_list|(
name|label2
argument_list|)
decl_stmt|;
if|if
condition|(
name|score
operator|==
literal|0
condition|)
block|{
name|score
operator|=
name|name1
operator|.
name|compareToIgnoreCase
argument_list|(
name|name2
argument_list|)
expr_stmt|;
block|}
return|return
name|score
return|;
block|}
block|}
block|}
DECL|method|groupScore (String group)
specifier|private
specifier|static
name|int
name|groupScore
parameter_list|(
name|String
name|group
parameter_list|)
block|{
if|if
condition|(
literal|"common"
operator|.
name|equals
argument_list|(
name|group
argument_list|)
condition|)
block|{
return|return
literal|1
return|;
block|}
elseif|else
if|if
condition|(
literal|"common (advanced)"
operator|.
name|equals
argument_list|(
name|group
argument_list|)
condition|)
block|{
return|return
literal|2
return|;
block|}
elseif|else
if|if
condition|(
literal|"consumer"
operator|.
name|equals
argument_list|(
name|group
argument_list|)
condition|)
block|{
return|return
literal|3
return|;
block|}
elseif|else
if|if
condition|(
literal|"consumer (advanced)"
operator|.
name|equals
argument_list|(
name|group
argument_list|)
condition|)
block|{
return|return
literal|4
return|;
block|}
elseif|else
if|if
condition|(
literal|"producer"
operator|.
name|equals
argument_list|(
name|group
argument_list|)
condition|)
block|{
return|return
literal|5
return|;
block|}
elseif|else
if|if
condition|(
literal|"producer (advanced)"
operator|.
name|equals
argument_list|(
name|group
argument_list|)
condition|)
block|{
return|return
literal|6
return|;
block|}
else|else
block|{
return|return
literal|9
return|;
block|}
block|}
DECL|class|EndpointPathComparator
specifier|private
specifier|static
specifier|final
class|class
name|EndpointPathComparator
implements|implements
name|Comparator
argument_list|<
name|EndpointPath
argument_list|>
block|{
DECL|field|syntax
specifier|private
specifier|final
name|String
name|syntax
decl_stmt|;
DECL|method|EndpointPathComparator (String syntax)
specifier|public
name|EndpointPathComparator
parameter_list|(
name|String
name|syntax
parameter_list|)
block|{
name|this
operator|.
name|syntax
operator|=
name|syntax
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|compare (EndpointPath path1, EndpointPath path2)
specifier|public
name|int
name|compare
parameter_list|(
name|EndpointPath
name|path1
parameter_list|,
name|EndpointPath
name|path2
parameter_list|)
block|{
name|int
name|pos1
init|=
name|syntax
operator|!=
literal|null
condition|?
name|syntax
operator|.
name|indexOf
argument_list|(
name|path1
operator|.
name|getName
argument_list|()
argument_list|)
else|:
operator|-
literal|1
decl_stmt|;
name|int
name|pos2
init|=
name|syntax
operator|!=
literal|null
condition|?
name|syntax
operator|.
name|indexOf
argument_list|(
name|path2
operator|.
name|getName
argument_list|()
argument_list|)
else|:
operator|-
literal|1
decl_stmt|;
comment|// use position in syntax to determine the order
if|if
condition|(
name|pos1
operator|!=
operator|-
literal|1
operator|&&
name|pos2
operator|!=
operator|-
literal|1
condition|)
block|{
return|return
name|Integer
operator|.
name|compare
argument_list|(
name|pos1
argument_list|,
name|pos2
argument_list|)
return|;
block|}
return|return
literal|0
return|;
block|}
block|}
block|}
end_class

end_unit

