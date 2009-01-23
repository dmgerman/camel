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

begin_class
DECL|class|GenericFileConfiguration
specifier|public
class|class
name|GenericFileConfiguration
block|{
DECL|field|file
specifier|private
name|String
name|file
decl_stmt|;
DECL|field|directory
specifier|private
name|boolean
name|directory
init|=
literal|true
decl_stmt|;
DECL|method|GenericFileConfiguration ()
specifier|public
name|GenericFileConfiguration
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"/"
operator|+
name|file
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
name|setFile
argument_list|(
name|uri
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|getFile ()
specifier|public
name|String
name|getFile
parameter_list|()
block|{
return|return
name|file
return|;
block|}
DECL|method|setFile (String file)
specifier|public
name|void
name|setFile
parameter_list|(
name|String
name|file
parameter_list|)
block|{
comment|// Avoid accidentally putting everything in root on
comment|// servers that expose the full filesystem
if|if
condition|(
name|file
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|file
operator|=
name|file
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|file
operator|=
name|file
expr_stmt|;
block|}
DECL|method|isDirectory ()
specifier|public
name|boolean
name|isDirectory
parameter_list|()
block|{
return|return
name|directory
return|;
block|}
DECL|method|setDirectory (boolean directory)
specifier|public
name|void
name|setDirectory
parameter_list|(
name|boolean
name|directory
parameter_list|)
block|{
name|this
operator|.
name|directory
operator|=
name|directory
expr_stmt|;
block|}
block|}
end_class

end_unit

