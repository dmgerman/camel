begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
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
name|util
operator|.
name|FileUtil
import|;
end_import

begin_class
DECL|class|GenericFileConfiguration
specifier|public
class|class
name|GenericFileConfiguration
block|{
DECL|field|directory
specifier|private
name|String
name|directory
decl_stmt|;
DECL|method|needToNormalize ()
specifier|public
name|boolean
name|needToNormalize
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|configure (URI uri)
specifier|public
name|void
name|configure
parameter_list|(
name|URI
name|uri
parameter_list|)
block|{
name|setDirectory
argument_list|(
name|uri
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|getDirectory ()
specifier|public
name|String
name|getDirectory
parameter_list|()
block|{
return|return
name|directory
return|;
block|}
DECL|method|setDirectory (String directory)
specifier|public
name|void
name|setDirectory
parameter_list|(
name|String
name|directory
parameter_list|)
block|{
name|this
operator|.
name|directory
operator|=
name|needToNormalize
argument_list|()
comment|// must normalize path to cater for Windows and other OS
condition|?
name|FileUtil
operator|.
name|normalizePath
argument_list|(
name|directory
argument_list|)
comment|// for the remote directory we don't need to do that
else|:
name|directory
expr_stmt|;
comment|// endpoint directory must not be null
if|if
condition|(
name|this
operator|.
name|directory
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|directory
operator|=
literal|""
expr_stmt|;
block|}
block|}
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|directory
return|;
block|}
block|}
end_class

end_unit

