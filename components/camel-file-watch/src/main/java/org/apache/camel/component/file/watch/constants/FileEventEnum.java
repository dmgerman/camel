begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file.watch.constants
package|package
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
name|watch
operator|.
name|constants
package|;
end_package

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|StandardWatchEventKinds
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|WatchEvent
import|;
end_import

begin_enum
DECL|enum|FileEventEnum
specifier|public
enum|enum
name|FileEventEnum
block|{
DECL|enumConstant|CREATE
name|CREATE
block|,
DECL|enumConstant|DELETE
name|DELETE
block|,
DECL|enumConstant|MODIFY
name|MODIFY
block|;
DECL|method|valueOf (WatchEvent<?> watchEvent)
specifier|public
specifier|static
name|FileEventEnum
name|valueOf
parameter_list|(
name|WatchEvent
argument_list|<
name|?
argument_list|>
name|watchEvent
parameter_list|)
block|{
if|if
condition|(
name|watchEvent
operator|.
name|context
argument_list|()
operator|instanceof
name|Path
condition|)
block|{
return|return
name|valueOf
argument_list|(
name|watchEvent
operator|.
name|kind
argument_list|()
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|valueOf (WatchEvent.Kind<?> watchEventKind)
specifier|public
specifier|static
name|FileEventEnum
name|valueOf
parameter_list|(
name|WatchEvent
operator|.
name|Kind
argument_list|<
name|?
argument_list|>
name|watchEventKind
parameter_list|)
block|{
if|if
condition|(
name|watchEventKind
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
elseif|else
if|if
condition|(
name|watchEventKind
operator|.
name|equals
argument_list|(
name|StandardWatchEventKinds
operator|.
name|ENTRY_CREATE
argument_list|)
condition|)
block|{
return|return
name|FileEventEnum
operator|.
name|CREATE
return|;
block|}
elseif|else
if|if
condition|(
name|watchEventKind
operator|.
name|equals
argument_list|(
name|StandardWatchEventKinds
operator|.
name|ENTRY_DELETE
argument_list|)
condition|)
block|{
return|return
name|FileEventEnum
operator|.
name|DELETE
return|;
block|}
elseif|else
if|if
condition|(
name|watchEventKind
operator|.
name|equals
argument_list|(
name|StandardWatchEventKinds
operator|.
name|ENTRY_MODIFY
argument_list|)
condition|)
block|{
return|return
name|FileEventEnum
operator|.
name|MODIFY
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|kind ()
specifier|public
name|WatchEvent
operator|.
name|Kind
argument_list|<
name|Path
argument_list|>
name|kind
parameter_list|()
block|{
switch|switch
condition|(
name|this
condition|)
block|{
case|case
name|CREATE
case|:
return|return
name|StandardWatchEventKinds
operator|.
name|ENTRY_CREATE
return|;
case|case
name|MODIFY
case|:
return|return
name|StandardWatchEventKinds
operator|.
name|ENTRY_MODIFY
return|;
case|case
name|DELETE
case|:
return|return
name|StandardWatchEventKinds
operator|.
name|ENTRY_DELETE
return|;
default|default:
return|return
literal|null
return|;
block|}
block|}
block|}
end_enum

end_unit

