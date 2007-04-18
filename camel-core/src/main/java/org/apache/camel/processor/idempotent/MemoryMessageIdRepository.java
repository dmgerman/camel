begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.idempotent
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|idempotent
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_comment
comment|/**  * A simple memory implementation of {@link MessageIdRepository}; though warning this could use up lots of RAM!  *  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|MemoryMessageIdRepository
specifier|public
class|class
name|MemoryMessageIdRepository
implements|implements
name|MessageIdRepository
block|{
DECL|field|set
specifier|private
name|Set
name|set
decl_stmt|;
comment|/**      * Creates a new MemoryMessageIdRepository with a memory based respository      */
DECL|method|memoryMessageIdRepository ()
specifier|public
specifier|static
name|MessageIdRepository
name|memoryMessageIdRepository
parameter_list|()
block|{
return|return
name|memoryMessageIdRepository
argument_list|(
operator|new
name|HashSet
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Creates a new MemoryMessageIdRepository using the given {@link Set} to use to store the      * processed Message ID objects      */
DECL|method|memoryMessageIdRepository (Set set)
specifier|public
specifier|static
name|MessageIdRepository
name|memoryMessageIdRepository
parameter_list|(
name|Set
name|set
parameter_list|)
block|{
return|return
operator|new
name|MemoryMessageIdRepository
argument_list|(
name|set
argument_list|)
return|;
block|}
DECL|method|MemoryMessageIdRepository (Set set)
specifier|public
name|MemoryMessageIdRepository
parameter_list|(
name|Set
name|set
parameter_list|)
block|{
name|this
operator|.
name|set
operator|=
name|set
expr_stmt|;
block|}
DECL|method|contains (String messageId)
specifier|public
name|boolean
name|contains
parameter_list|(
name|String
name|messageId
parameter_list|)
block|{
synchronized|synchronized
init|(
name|set
init|)
block|{
if|if
condition|(
name|set
operator|.
name|contains
argument_list|(
name|messageId
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
else|else
block|{
name|set
operator|.
name|add
argument_list|(
name|messageId
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
block|}
block|}
end_class

end_unit

