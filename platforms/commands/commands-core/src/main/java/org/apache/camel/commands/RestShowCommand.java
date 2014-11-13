begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.commands
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|commands
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintStream
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
name|CamelContext
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
name|model
operator|.
name|ModelHelper
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
name|model
operator|.
name|rest
operator|.
name|RestDefinition
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
name|model
operator|.
name|rest
operator|.
name|RestsDefinition
import|;
end_import

begin_class
DECL|class|RestShowCommand
specifier|public
class|class
name|RestShowCommand
extends|extends
name|AbstractContextCommand
block|{
DECL|method|RestShowCommand (String context)
specifier|public
name|RestShowCommand
parameter_list|(
name|String
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|performContextCommand (CamelController camelController, CamelContext camelContext, PrintStream out, PrintStream err)
specifier|protected
name|Object
name|performContextCommand
parameter_list|(
name|CamelController
name|camelController
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|,
name|PrintStream
name|out
parameter_list|,
name|PrintStream
name|err
parameter_list|)
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|RestDefinition
argument_list|>
name|rests
init|=
name|camelController
operator|.
name|getRestDefinitions
argument_list|(
name|context
argument_list|)
decl_stmt|;
if|if
condition|(
name|rests
operator|==
literal|null
operator|||
name|rests
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|"There are no REST services in CamelContext with name: "
operator|+
name|context
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
comment|// use a routes definition to dump the rests
name|RestsDefinition
name|def
init|=
operator|new
name|RestsDefinition
argument_list|()
decl_stmt|;
name|def
operator|.
name|setRests
argument_list|(
name|rests
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
name|ModelHelper
operator|.
name|dumpModelAsXml
argument_list|(
name|def
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

