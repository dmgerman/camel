begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jasypt
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jasypt
package|;
end_package

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
name|LinkedList
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
name|jasypt
operator|.
name|encryption
operator|.
name|pbe
operator|.
name|StandardPBEStringEncryptor
import|;
end_import

begin_class
DECL|class|Main
specifier|public
class|class
name|Main
block|{
DECL|field|encryptor
specifier|private
specifier|final
name|StandardPBEStringEncryptor
name|encryptor
init|=
operator|new
name|StandardPBEStringEncryptor
argument_list|()
decl_stmt|;
DECL|field|options
specifier|private
specifier|final
name|List
argument_list|<
name|Option
argument_list|>
name|options
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|command
specifier|private
name|String
name|command
decl_stmt|;
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
DECL|field|input
specifier|private
name|String
name|input
decl_stmt|;
DECL|field|algorithm
specifier|private
name|String
name|algorithm
decl_stmt|;
DECL|class|Option
specifier|private
specifier|abstract
class|class
name|Option
block|{
DECL|field|abbreviation
specifier|private
name|String
name|abbreviation
decl_stmt|;
DECL|field|fullName
specifier|private
name|String
name|fullName
decl_stmt|;
DECL|field|description
specifier|private
name|String
name|description
decl_stmt|;
DECL|method|Option (String abbreviation, String fullName, String description)
specifier|protected
name|Option
parameter_list|(
name|String
name|abbreviation
parameter_list|,
name|String
name|fullName
parameter_list|,
name|String
name|description
parameter_list|)
block|{
name|this
operator|.
name|abbreviation
operator|=
literal|"-"
operator|+
name|abbreviation
expr_stmt|;
name|this
operator|.
name|fullName
operator|=
literal|"-"
operator|+
name|fullName
expr_stmt|;
name|this
operator|.
name|description
operator|=
name|description
expr_stmt|;
block|}
DECL|method|processOption (String arg, LinkedList<String> remainingArgs)
specifier|public
name|boolean
name|processOption
parameter_list|(
name|String
name|arg
parameter_list|,
name|LinkedList
argument_list|<
name|String
argument_list|>
name|remainingArgs
parameter_list|)
block|{
if|if
condition|(
name|arg
operator|.
name|equalsIgnoreCase
argument_list|(
name|abbreviation
argument_list|)
operator|||
name|fullName
operator|.
name|startsWith
argument_list|(
name|arg
argument_list|)
condition|)
block|{
name|doProcess
argument_list|(
name|arg
argument_list|,
name|remainingArgs
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
DECL|method|getAbbreviation ()
specifier|public
name|String
name|getAbbreviation
parameter_list|()
block|{
return|return
name|abbreviation
return|;
block|}
DECL|method|getDescription ()
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
block|}
DECL|method|getFullName ()
specifier|public
name|String
name|getFullName
parameter_list|()
block|{
return|return
name|fullName
return|;
block|}
DECL|method|getInformation ()
specifier|public
name|String
name|getInformation
parameter_list|()
block|{
return|return
literal|"  "
operator|+
name|getAbbreviation
argument_list|()
operator|+
literal|" or "
operator|+
name|getFullName
argument_list|()
operator|+
literal|" = "
operator|+
name|getDescription
argument_list|()
return|;
block|}
DECL|method|doProcess (String arg, LinkedList<String> remainingArgs)
specifier|protected
specifier|abstract
name|void
name|doProcess
parameter_list|(
name|String
name|arg
parameter_list|,
name|LinkedList
argument_list|<
name|String
argument_list|>
name|remainingArgs
parameter_list|)
function_decl|;
block|}
DECL|class|ParameterOption
specifier|private
specifier|abstract
class|class
name|ParameterOption
extends|extends
name|Option
block|{
DECL|field|parameterName
specifier|private
name|String
name|parameterName
decl_stmt|;
DECL|method|ParameterOption (String abbreviation, String fullName, String description, String parameterName)
specifier|protected
name|ParameterOption
parameter_list|(
name|String
name|abbreviation
parameter_list|,
name|String
name|fullName
parameter_list|,
name|String
name|description
parameter_list|,
name|String
name|parameterName
parameter_list|)
block|{
name|super
argument_list|(
name|abbreviation
argument_list|,
name|fullName
argument_list|,
name|description
argument_list|)
expr_stmt|;
name|this
operator|.
name|parameterName
operator|=
name|parameterName
expr_stmt|;
block|}
DECL|method|doProcess (String arg, LinkedList<String> remainingArgs)
specifier|protected
name|void
name|doProcess
parameter_list|(
name|String
name|arg
parameter_list|,
name|LinkedList
argument_list|<
name|String
argument_list|>
name|remainingArgs
parameter_list|)
block|{
if|if
condition|(
name|remainingArgs
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Expected fileName for "
argument_list|)
expr_stmt|;
name|showOptions
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|String
name|parameter
init|=
name|remainingArgs
operator|.
name|removeFirst
argument_list|()
decl_stmt|;
name|doProcess
argument_list|(
name|arg
argument_list|,
name|parameter
argument_list|,
name|remainingArgs
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getInformation ()
specifier|public
name|String
name|getInformation
parameter_list|()
block|{
return|return
literal|"  "
operator|+
name|getAbbreviation
argument_list|()
operator|+
literal|" or "
operator|+
name|getFullName
argument_list|()
operator|+
literal|"<"
operator|+
name|parameterName
operator|+
literal|"> = "
operator|+
name|getDescription
argument_list|()
return|;
block|}
DECL|method|doProcess (String arg, String parameter, LinkedList<String> remainingArgs)
specifier|protected
specifier|abstract
name|void
name|doProcess
parameter_list|(
name|String
name|arg
parameter_list|,
name|String
name|parameter
parameter_list|,
name|LinkedList
argument_list|<
name|String
argument_list|>
name|remainingArgs
parameter_list|)
function_decl|;
block|}
DECL|method|Main ()
specifier|public
name|Main
parameter_list|()
block|{
name|addOption
argument_list|(
operator|new
name|Option
argument_list|(
literal|"h"
argument_list|,
literal|"help"
argument_list|,
literal|"Displays the help screen"
argument_list|)
block|{
specifier|protected
name|void
name|doProcess
parameter_list|(
name|String
name|arg
parameter_list|,
name|LinkedList
argument_list|<
name|String
argument_list|>
name|remainingArgs
parameter_list|)
block|{
name|showOptions
argument_list|()
expr_stmt|;
comment|// no need to process further if user just wants help
name|System
operator|.
name|exit
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|addOption
argument_list|(
operator|new
name|ParameterOption
argument_list|(
literal|"c"
argument_list|,
literal|"command"
argument_list|,
literal|"Command can be encrypt or decrypt"
argument_list|,
literal|"command"
argument_list|)
block|{
specifier|protected
name|void
name|doProcess
parameter_list|(
name|String
name|arg
parameter_list|,
name|String
name|parameter
parameter_list|,
name|LinkedList
argument_list|<
name|String
argument_list|>
name|remainingArgs
parameter_list|)
block|{
if|if
condition|(
literal|"encrypt"
operator|.
name|equals
argument_list|(
name|parameter
argument_list|)
operator|||
literal|"decrypt"
operator|.
name|equals
argument_list|(
name|parameter
argument_list|)
condition|)
block|{
name|command
operator|=
name|parameter
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unknown command, was: "
operator|+
name|parameter
argument_list|)
throw|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|addOption
argument_list|(
operator|new
name|ParameterOption
argument_list|(
literal|"p"
argument_list|,
literal|"password"
argument_list|,
literal|"Password to use"
argument_list|,
literal|"password"
argument_list|)
block|{
specifier|protected
name|void
name|doProcess
parameter_list|(
name|String
name|arg
parameter_list|,
name|String
name|parameter
parameter_list|,
name|LinkedList
argument_list|<
name|String
argument_list|>
name|remainingArgs
parameter_list|)
block|{
name|password
operator|=
name|parameter
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|addOption
argument_list|(
operator|new
name|ParameterOption
argument_list|(
literal|"i"
argument_list|,
literal|"input"
argument_list|,
literal|"Text to encrypt or decrypt"
argument_list|,
literal|"input"
argument_list|)
block|{
specifier|protected
name|void
name|doProcess
parameter_list|(
name|String
name|arg
parameter_list|,
name|String
name|parameter
parameter_list|,
name|LinkedList
argument_list|<
name|String
argument_list|>
name|remainingArgs
parameter_list|)
block|{
name|input
operator|=
name|parameter
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|addOption
argument_list|(
operator|new
name|ParameterOption
argument_list|(
literal|"a"
argument_list|,
literal|"algorithm"
argument_list|,
literal|"Optional algorithm to use"
argument_list|,
literal|"algorithm"
argument_list|)
block|{
specifier|protected
name|void
name|doProcess
parameter_list|(
name|String
name|arg
parameter_list|,
name|String
name|parameter
parameter_list|,
name|LinkedList
argument_list|<
name|String
argument_list|>
name|remainingArgs
parameter_list|)
block|{
name|algorithm
operator|=
name|parameter
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|addOption (Option option)
specifier|private
name|void
name|addOption
parameter_list|(
name|Option
name|option
parameter_list|)
block|{
name|options
operator|.
name|add
argument_list|(
name|option
argument_list|)
expr_stmt|;
block|}
DECL|method|showOptions ()
specifier|private
name|void
name|showOptions
parameter_list|()
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Apache Camel Jasypt takes the following options:"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
for|for
control|(
name|Option
name|option
range|:
name|options
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|option
operator|.
name|getInformation
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
DECL|method|parseArguments (String[] arguments)
specifier|private
name|boolean
name|parseArguments
parameter_list|(
name|String
index|[]
name|arguments
parameter_list|)
block|{
name|LinkedList
argument_list|<
name|String
argument_list|>
name|args
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|arguments
argument_list|)
argument_list|)
decl_stmt|;
name|boolean
name|valid
init|=
literal|true
decl_stmt|;
while|while
condition|(
operator|!
name|args
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|String
name|arg
init|=
name|args
operator|.
name|removeFirst
argument_list|()
decl_stmt|;
name|boolean
name|handled
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Option
name|option
range|:
name|options
control|)
block|{
if|if
condition|(
name|option
operator|.
name|processOption
argument_list|(
name|arg
argument_list|,
name|args
argument_list|)
condition|)
block|{
name|handled
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|!
name|handled
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Error: Unknown option: "
operator|+
name|arg
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
name|valid
operator|=
literal|false
expr_stmt|;
break|break;
block|}
block|}
return|return
name|valid
return|;
block|}
DECL|method|run (String[] args)
specifier|public
name|void
name|run
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|parseArguments
argument_list|(
name|args
argument_list|)
condition|)
block|{
name|showOptions
argument_list|()
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|command
operator|==
literal|null
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Error: Command is empty"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
name|showOptions
argument_list|()
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|password
operator|==
literal|null
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Error: Password is empty"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
name|showOptions
argument_list|()
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|input
operator|==
literal|null
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Error: Input is empty"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
name|showOptions
argument_list|()
expr_stmt|;
return|return;
block|}
name|encryptor
operator|.
name|setPassword
argument_list|(
name|password
argument_list|)
expr_stmt|;
if|if
condition|(
name|algorithm
operator|!=
literal|null
condition|)
block|{
name|encryptor
operator|.
name|setAlgorithm
argument_list|(
name|algorithm
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
literal|"encrypt"
operator|.
name|equals
argument_list|(
name|command
argument_list|)
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Encrypted text: "
operator|+
name|encryptor
operator|.
name|encrypt
argument_list|(
name|input
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Decrypted text: "
operator|+
name|encryptor
operator|.
name|decrypt
argument_list|(
name|input
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|main (String[] args)
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
name|Main
name|main
init|=
operator|new
name|Main
argument_list|()
decl_stmt|;
if|if
condition|(
name|args
operator|.
name|length
operator|==
literal|0
condition|)
block|{
name|main
operator|.
name|showOptions
argument_list|()
expr_stmt|;
return|return;
block|}
else|else
block|{
name|main
operator|.
name|run
argument_list|(
name|args
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

