begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.robotframework
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|robotframework
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|ObjectHelper
import|;
end_import

begin_class
DECL|class|RobotFrameworkArguments
specifier|public
class|class
name|RobotFrameworkArguments
block|{
DECL|field|arguments
specifier|private
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|arguments
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|addFileToArguments (File file, String flag)
specifier|public
name|void
name|addFileToArguments
parameter_list|(
name|File
name|file
parameter_list|,
name|String
name|flag
parameter_list|)
block|{
if|if
condition|(
name|isFileValid
argument_list|(
name|file
argument_list|)
condition|)
block|{
name|String
name|path
init|=
operator|!
name|file
operator|.
name|getName
argument_list|()
operator|.
name|toUpperCase
argument_list|()
operator|.
name|equals
argument_list|(
literal|"NONE"
argument_list|)
condition|?
name|file
operator|.
name|getPath
argument_list|()
else|:
name|file
operator|.
name|getName
argument_list|()
decl_stmt|;
name|add
argument_list|(
name|flag
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|isFileValid (File file)
specifier|protected
name|boolean
name|isFileValid
parameter_list|(
name|File
name|file
parameter_list|)
block|{
return|return
name|file
operator|!=
literal|null
operator|&&
name|file
operator|.
name|getPath
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|file
operator|.
name|getPath
argument_list|()
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
return|;
block|}
DECL|method|addNonEmptyStringToArguments (String variableToAdd, String flag)
specifier|public
name|void
name|addNonEmptyStringToArguments
parameter_list|(
name|String
name|variableToAdd
parameter_list|,
name|String
name|flag
parameter_list|)
block|{
if|if
condition|(
operator|!
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|variableToAdd
argument_list|)
condition|)
block|{
name|addStringToArguments
argument_list|(
name|variableToAdd
argument_list|,
name|flag
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|addFlagToArguments (boolean flag, String argument)
specifier|public
name|void
name|addFlagToArguments
parameter_list|(
name|boolean
name|flag
parameter_list|,
name|String
name|argument
parameter_list|)
block|{
if|if
condition|(
name|flag
condition|)
block|{
name|add
argument_list|(
name|argument
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|addStringToArguments (String variableToAdd, String flag)
specifier|public
name|void
name|addStringToArguments
parameter_list|(
name|String
name|variableToAdd
parameter_list|,
name|String
name|flag
parameter_list|)
block|{
name|add
argument_list|(
name|flag
argument_list|,
name|variableToAdd
argument_list|)
expr_stmt|;
block|}
DECL|method|addListToArguments (String variablesToAdd, String flag)
specifier|public
name|void
name|addListToArguments
parameter_list|(
name|String
name|variablesToAdd
parameter_list|,
name|String
name|flag
parameter_list|)
block|{
if|if
condition|(
name|variablesToAdd
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|String
index|[]
name|splittedVariablesToAdd
init|=
name|variablesToAdd
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
name|addListToArguments
argument_list|(
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|splittedVariablesToAdd
argument_list|)
argument_list|)
argument_list|,
name|flag
argument_list|)
expr_stmt|;
block|}
DECL|method|addListToArguments (List<String> variablesToAdd, String flag)
specifier|public
name|void
name|addListToArguments
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|variablesToAdd
parameter_list|,
name|String
name|flag
parameter_list|)
block|{
if|if
condition|(
name|variablesToAdd
operator|==
literal|null
condition|)
block|{
return|return;
block|}
for|for
control|(
name|String
name|variableToAdd
range|:
name|variablesToAdd
control|)
block|{
if|if
condition|(
operator|!
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|variableToAdd
argument_list|)
condition|)
block|{
name|add
argument_list|(
name|flag
argument_list|,
name|variableToAdd
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|addFileListToArguments (List<File> variablesToAdd, String flag)
specifier|public
name|void
name|addFileListToArguments
parameter_list|(
name|List
argument_list|<
name|File
argument_list|>
name|variablesToAdd
parameter_list|,
name|String
name|flag
parameter_list|)
block|{
if|if
condition|(
name|variablesToAdd
operator|==
literal|null
condition|)
block|{
return|return;
block|}
for|for
control|(
name|File
name|variableToAdd
range|:
name|variablesToAdd
control|)
block|{
name|addFileToArguments
argument_list|(
name|variableToAdd
argument_list|,
name|flag
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|add (String... values)
specifier|public
name|void
name|add
parameter_list|(
name|String
modifier|...
name|values
parameter_list|)
block|{
name|arguments
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|values
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|toArray ()
specifier|public
name|String
index|[]
name|toArray
parameter_list|()
block|{
return|return
name|arguments
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|arguments
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
block|}
end_class

end_unit

