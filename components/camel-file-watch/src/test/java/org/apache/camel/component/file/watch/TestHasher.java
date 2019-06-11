begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file.watch
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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|hashing
operator|.
name|FileHasher
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
name|hashing
operator|.
name|HashCode
import|;
end_import

begin_class
DECL|class|TestHasher
specifier|public
class|class
name|TestHasher
implements|implements
name|FileHasher
block|{
annotation|@
name|Override
DECL|method|hash (Path path)
specifier|public
name|HashCode
name|hash
parameter_list|(
name|Path
name|path
parameter_list|)
throws|throws
name|IOException
block|{
comment|// Always return constant
comment|// This should cause every event is triggered only once (hashcode remains the same), so we can test this.
comment|// Never use this outside junit
return|return
name|HashCode
operator|.
name|fromLong
argument_list|(
literal|1L
argument_list|)
return|;
block|}
block|}
end_class

end_unit

