begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.scp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|scp
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|CamelContext
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
name|component
operator|.
name|file
operator|.
name|GenericFileExclusiveReadLockStrategy
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
name|component
operator|.
name|file
operator|.
name|GenericFileProcessStrategy
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
name|component
operator|.
name|file
operator|.
name|strategy
operator|.
name|GenericFileNoOpProcessStrategy
import|;
end_import

begin_class
DECL|class|ScpProcessStrategyFactory
specifier|public
specifier|final
class|class
name|ScpProcessStrategyFactory
block|{
DECL|method|ScpProcessStrategyFactory ()
specifier|private
name|ScpProcessStrategyFactory
parameter_list|()
block|{     }
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|createGenericFileProcessStrategy (CamelContext context, Map<String, Object> params)
specifier|public
specifier|static
parameter_list|<
name|LsEntry
parameter_list|>
name|GenericFileProcessStrategy
argument_list|<
name|LsEntry
argument_list|>
name|createGenericFileProcessStrategy
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|params
parameter_list|)
block|{
comment|/*         // We assume a value is present only if its value not null for String and 'true' for boolean         Expression moveExpression = (Expression) params.get("move");         Expression moveFailedExpression = (Expression) params.get("moveFailed");         Expression preMoveExpression = (Expression) params.get("preMove");         boolean isNoop = params.get("noop") != null;         boolean isDelete = params.get("delete") != null;         boolean isMove = moveExpression != null || preMoveExpression != null || moveFailedExpression != null;         */
comment|// default strategy will do nothing
name|GenericFileNoOpProcessStrategy
argument_list|<
name|LsEntry
argument_list|>
name|strategy
init|=
operator|new
name|GenericFileNoOpProcessStrategy
argument_list|<>
argument_list|()
decl_stmt|;
name|strategy
operator|.
name|setExclusiveReadLockStrategy
argument_list|(
operator|(
name|GenericFileExclusiveReadLockStrategy
argument_list|<
name|LsEntry
argument_list|>
operator|)
name|getExclusiveReadLockStrategy
argument_list|(
name|params
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|strategy
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|}
argument_list|)
DECL|method|getExclusiveReadLockStrategy (Map<String, Object> params)
specifier|private
specifier|static
parameter_list|<
name|LsEntry
parameter_list|>
name|GenericFileExclusiveReadLockStrategy
argument_list|<
name|LsEntry
argument_list|>
name|getExclusiveReadLockStrategy
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|params
parameter_list|)
block|{
name|GenericFileExclusiveReadLockStrategy
argument_list|<
name|LsEntry
argument_list|>
name|strategy
init|=
operator|(
name|GenericFileExclusiveReadLockStrategy
argument_list|<
name|LsEntry
argument_list|>
operator|)
name|params
operator|.
name|get
argument_list|(
literal|"exclusiveReadLockStrategy"
argument_list|)
decl_stmt|;
if|if
condition|(
name|strategy
operator|!=
literal|null
condition|)
block|{
return|return
name|strategy
return|;
block|}
comment|/*         // no explicit strategy set then fallback to readLock option         String readLock = (String) params.get("readLock");         if (ObjectHelper.isNotEmpty(readLock)) {             if ("none".equals(readLock) || "false".equals(readLock)) {                 return null;             } else if ("rename".equals(readLock)) {                 GenericFileRenameExclusiveReadLockStrategy<LsEntry> readLockStrategy = new GenericFileRenameExclusiveReadLockStrategy<LsEntry>();                 Long timeout = (Long) params.get("readLockTimeout");                 if (timeout != null) {                     readLockStrategy.setTimeout(timeout);                 }                 return readLockStrategy;             } else if ("changed".equals(readLock)) {                 GenericFileExclusiveReadLockStrategy readLockStrategy = new SftpChangedExclusiveReadLockStrategy();                 Long timeout = (Long) params.get("readLockTimeout");                 if (timeout != null) {                     readLockStrategy.setTimeout(timeout);                 }                 Long checkInterval = (Long) params.get("readLockCheckInterval");                 if (checkInterval != null) {                     readLockStrategy.setCheckInterval(checkInterval);                 }                 return readLockStrategy;             }         } */
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

