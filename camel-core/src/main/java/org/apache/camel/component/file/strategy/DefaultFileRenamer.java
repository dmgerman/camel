begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file.strategy
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
name|strategy
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_comment
comment|/**  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|DefaultFileRenamer
specifier|public
class|class
name|DefaultFileRenamer
implements|implements
name|FileRenamer
block|{
DECL|field|ON_WINDOWS
specifier|private
specifier|static
specifier|final
name|boolean
name|ON_WINDOWS
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"os.name"
argument_list|)
operator|.
name|startsWith
argument_list|(
literal|"Windows"
argument_list|)
decl_stmt|;
DECL|field|namePrefix
specifier|private
name|String
name|namePrefix
decl_stmt|;
DECL|field|namePostfix
specifier|private
name|String
name|namePostfix
decl_stmt|;
DECL|method|DefaultFileRenamer ()
specifier|public
name|DefaultFileRenamer
parameter_list|()
block|{     }
DECL|method|DefaultFileRenamer (String namePrefix, String namePostfix)
specifier|public
name|DefaultFileRenamer
parameter_list|(
name|String
name|namePrefix
parameter_list|,
name|String
name|namePostfix
parameter_list|)
block|{
name|this
operator|.
name|namePrefix
operator|=
name|namePrefix
expr_stmt|;
name|this
operator|.
name|namePostfix
operator|=
name|namePostfix
expr_stmt|;
block|}
DECL|method|renameFile (File file)
specifier|public
name|File
name|renameFile
parameter_list|(
name|File
name|file
parameter_list|)
block|{
name|File
name|parent
init|=
name|file
operator|.
name|getParentFile
argument_list|()
decl_stmt|;
name|String
name|name
init|=
name|renameFileName
argument_list|(
name|file
argument_list|)
decl_stmt|;
if|if
condition|(
name|ON_WINDOWS
operator|&&
operator|(
name|name
operator|.
name|indexOf
argument_list|(
literal|":"
argument_list|)
operator|>=
literal|0
operator|||
name|name
operator|.
name|startsWith
argument_list|(
literal|"//"
argument_list|)
operator|)
condition|)
block|{
return|return
operator|new
name|File
argument_list|(
name|name
argument_list|)
return|;
block|}
return|return
operator|new
name|File
argument_list|(
name|parent
argument_list|,
name|name
argument_list|)
return|;
block|}
DECL|method|getNamePostfix ()
specifier|public
name|String
name|getNamePostfix
parameter_list|()
block|{
return|return
name|namePostfix
return|;
block|}
comment|/**      * Sets the name postfix appended to moved files. For example      * to rename all the files from * to *.done set this value to ".done"      */
DECL|method|setNamePostfix (String namePostfix)
specifier|public
name|void
name|setNamePostfix
parameter_list|(
name|String
name|namePostfix
parameter_list|)
block|{
name|this
operator|.
name|namePostfix
operator|=
name|namePostfix
expr_stmt|;
block|}
DECL|method|getNamePrefix ()
specifier|public
name|String
name|getNamePrefix
parameter_list|()
block|{
return|return
name|namePrefix
return|;
block|}
comment|/**      * Sets the name prefix appended to moved files. For example      * to move processed files into a hidden directory called ".camel"      * set this value to ".camel/"      */
DECL|method|setNamePrefix (String namePrefix)
specifier|public
name|void
name|setNamePrefix
parameter_list|(
name|String
name|namePrefix
parameter_list|)
block|{
name|this
operator|.
name|namePrefix
operator|=
name|namePrefix
expr_stmt|;
block|}
DECL|method|renameFileName (File file)
specifier|protected
name|String
name|renameFileName
parameter_list|(
name|File
name|file
parameter_list|)
block|{
name|StringBuffer
name|buffer
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
if|if
condition|(
name|namePrefix
operator|!=
literal|null
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|namePrefix
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
name|file
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|namePostfix
operator|!=
literal|null
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|namePostfix
argument_list|)
expr_stmt|;
block|}
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

