begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.karaf.commands.catalog.completers
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|karaf
operator|.
name|commands
operator|.
name|catalog
operator|.
name|completers
package|;
end_package

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
name|karaf
operator|.
name|commands
operator|.
name|internal
operator|.
name|CamelControllerImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|shell
operator|.
name|api
operator|.
name|action
operator|.
name|lifecycle
operator|.
name|Service
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|shell
operator|.
name|api
operator|.
name|console
operator|.
name|CommandLine
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|shell
operator|.
name|api
operator|.
name|console
operator|.
name|Completer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|shell
operator|.
name|api
operator|.
name|console
operator|.
name|Session
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|shell
operator|.
name|support
operator|.
name|completers
operator|.
name|StringsCompleter
import|;
end_import

begin_class
annotation|@
name|Service
DECL|class|ComponentListCompleter
specifier|public
class|class
name|ComponentListCompleter
extends|extends
name|CamelControllerImpl
implements|implements
name|Completer
block|{
annotation|@
name|Override
DECL|method|complete (Session session, CommandLine commandLine, List<String> candidates)
specifier|public
name|int
name|complete
parameter_list|(
name|Session
name|session
parameter_list|,
name|CommandLine
name|commandLine
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|candidates
parameter_list|)
block|{
try|try
block|{
name|StringsCompleter
name|delegate
init|=
operator|new
name|StringsCompleter
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|components
init|=
name|listComponentsCatalog
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|String
name|buffer
init|=
name|commandLine
operator|.
name|getCursorArgument
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|row
range|:
name|components
control|)
block|{
name|String
name|name
init|=
name|row
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
name|boolean
name|match
decl_stmt|;
if|if
condition|(
name|buffer
operator|==
literal|null
operator|||
name|buffer
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|match
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
name|match
operator|=
name|name
operator|.
name|startsWith
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|match
condition|)
block|{
name|delegate
operator|.
name|getStrings
argument_list|()
operator|.
name|add
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|delegate
operator|.
name|complete
argument_list|(
name|session
argument_list|,
name|commandLine
argument_list|,
name|candidates
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// nothing to do, no completion
block|}
return|return
literal|0
return|;
block|}
block|}
end_class

end_unit

