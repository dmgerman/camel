begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file
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
package|;
end_package

begin_class
DECL|class|GenericFileHelper
specifier|public
specifier|final
class|class
name|GenericFileHelper
block|{
DECL|method|GenericFileHelper ()
specifier|private
name|GenericFileHelper
parameter_list|()
block|{     }
DECL|method|asExclusiveReadLockKey (GenericFile file, String key)
specifier|public
specifier|static
name|String
name|asExclusiveReadLockKey
parameter_list|(
name|GenericFile
name|file
parameter_list|,
name|String
name|key
parameter_list|)
block|{
comment|// use the copy from absolute path as that was the original path of the file when the lock was acquired
comment|// for example if the file consumer uses preMove then the file is moved and therefore has another name
comment|// that would no longer match
name|String
name|path
init|=
name|file
operator|.
name|getCopyFromAbsoluteFilePath
argument_list|()
operator|!=
literal|null
condition|?
name|file
operator|.
name|getCopyFromAbsoluteFilePath
argument_list|()
else|:
name|file
operator|.
name|getAbsoluteFilePath
argument_list|()
decl_stmt|;
return|return
name|asExclusiveReadLockKey
argument_list|(
name|path
argument_list|,
name|key
argument_list|)
return|;
block|}
DECL|method|asExclusiveReadLockKey (String path, String key)
specifier|public
specifier|static
name|String
name|asExclusiveReadLockKey
parameter_list|(
name|String
name|path
parameter_list|,
name|String
name|key
parameter_list|)
block|{
return|return
name|path
operator|+
literal|"-"
operator|+
name|key
return|;
block|}
block|}
end_class

end_unit

