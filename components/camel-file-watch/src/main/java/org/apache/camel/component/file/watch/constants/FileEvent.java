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
name|io
operator|.
name|methvin
operator|.
name|watcher
operator|.
name|DirectoryChangeEvent
import|;
end_import

begin_class
DECL|class|FileEvent
specifier|public
class|class
name|FileEvent
block|{
DECL|field|eventType
specifier|private
name|FileEventEnum
name|eventType
decl_stmt|;
DECL|field|eventPath
specifier|private
name|Path
name|eventPath
decl_stmt|;
DECL|method|FileEvent (FileEventEnum eventType, Path eventPath)
specifier|public
name|FileEvent
parameter_list|(
name|FileEventEnum
name|eventType
parameter_list|,
name|Path
name|eventPath
parameter_list|)
block|{
name|this
operator|.
name|eventType
operator|=
name|eventType
expr_stmt|;
name|this
operator|.
name|eventPath
operator|=
name|eventPath
expr_stmt|;
block|}
DECL|method|FileEvent (DirectoryChangeEvent event)
specifier|public
name|FileEvent
parameter_list|(
name|DirectoryChangeEvent
name|event
parameter_list|)
block|{
name|this
operator|.
name|eventType
operator|=
name|FileEventEnum
operator|.
name|valueOf
argument_list|(
name|event
operator|.
name|eventType
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|eventPath
operator|=
name|event
operator|.
name|path
argument_list|()
expr_stmt|;
block|}
DECL|method|getEventType ()
specifier|public
name|FileEventEnum
name|getEventType
parameter_list|()
block|{
return|return
name|eventType
return|;
block|}
DECL|method|setEventType (FileEventEnum eventType)
specifier|public
name|void
name|setEventType
parameter_list|(
name|FileEventEnum
name|eventType
parameter_list|)
block|{
name|this
operator|.
name|eventType
operator|=
name|eventType
expr_stmt|;
block|}
DECL|method|getEventPath ()
specifier|public
name|Path
name|getEventPath
parameter_list|()
block|{
return|return
name|eventPath
return|;
block|}
DECL|method|setEventPath (Path eventPath)
specifier|public
name|void
name|setEventPath
parameter_list|(
name|Path
name|eventPath
parameter_list|)
block|{
name|this
operator|.
name|eventPath
operator|=
name|eventPath
expr_stmt|;
block|}
block|}
end_class

end_unit

