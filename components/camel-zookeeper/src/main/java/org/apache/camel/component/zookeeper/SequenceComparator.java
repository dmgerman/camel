begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|org
operator|.
name|apache
operator|.
name|zookeeper
operator|.
name|CreateMode
import|;
end_import

begin_comment
comment|/**  * Nodes created with any of Sequential {@link CreateMode}s will have a 10  * character sequence attached to their node names.  *<code>SequenceComparator</code> is a Natural comparator used to compare lists  * of objects with these appended sequences.  */
end_comment

begin_class
DECL|class|SequenceComparator
specifier|public
class|class
name|SequenceComparator
extends|extends
name|NaturalSortComparator
block|{
DECL|field|ZOOKEEPER_SEQUENCE_LENGTH
specifier|public
specifier|static
specifier|final
name|int
name|ZOOKEEPER_SEQUENCE_LENGTH
init|=
literal|10
decl_stmt|;
annotation|@
name|Override
DECL|method|compare (CharSequence sequencedNode, CharSequence otherSequencedNode)
specifier|public
name|int
name|compare
parameter_list|(
name|CharSequence
name|sequencedNode
parameter_list|,
name|CharSequence
name|otherSequencedNode
parameter_list|)
block|{
if|if
condition|(
name|sequencedNode
operator|==
literal|null
operator|&&
name|otherSequencedNode
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
name|sequencedNode
operator|!=
literal|null
operator|&&
name|otherSequencedNode
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
name|sequencedNode
operator|==
literal|null
operator|&&
name|otherSequencedNode
operator|!=
literal|null
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
return|return
name|super
operator|.
name|compare
argument_list|(
name|getZooKeeperSequenceNumber
argument_list|(
name|sequencedNode
argument_list|)
argument_list|,
name|getZooKeeperSequenceNumber
argument_list|(
name|otherSequencedNode
argument_list|)
argument_list|)
return|;
block|}
DECL|method|getZooKeeperSequenceNumber (CharSequence sequencedNodeName)
specifier|private
name|CharSequence
name|getZooKeeperSequenceNumber
parameter_list|(
name|CharSequence
name|sequencedNodeName
parameter_list|)
block|{
name|int
name|len
init|=
name|sequencedNodeName
operator|.
name|length
argument_list|()
decl_stmt|;
return|return
name|sequencedNodeName
operator|.
name|subSequence
argument_list|(
name|len
operator|-
name|ZOOKEEPER_SEQUENCE_LENGTH
argument_list|,
name|len
argument_list|)
return|;
block|}
block|}
end_class

end_unit

