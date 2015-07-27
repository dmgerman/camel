begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.karaf.commands
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
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|commands
operator|.
name|EndpointStatisticCommand
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|gogo
operator|.
name|commands
operator|.
name|Argument
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|gogo
operator|.
name|commands
operator|.
name|Command
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|gogo
operator|.
name|commands
operator|.
name|Option
import|;
end_import

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"camel"
argument_list|,
name|name
operator|=
literal|"endpoint-stats"
argument_list|,
name|description
operator|=
literal|"Display endpoint runtime statistics"
argument_list|)
DECL|class|EndpointStats
specifier|public
class|class
name|EndpointStats
extends|extends
name|CamelCommandSupport
block|{
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|0
argument_list|,
name|name
operator|=
literal|"name"
argument_list|,
name|description
operator|=
literal|"The name of the Camel context"
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
DECL|field|name
name|String
name|name
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--filter"
argument_list|,
name|aliases
operator|=
literal|"-f"
argument_list|,
name|description
operator|=
literal|"Filter the list by in,out,static,dynamic"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|true
argument_list|)
DECL|field|filter
name|String
index|[]
name|filter
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--decode"
argument_list|,
name|aliases
operator|=
literal|"-d"
argument_list|,
name|description
operator|=
literal|"Whether to decode the endpoint uri so its human readable"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|,
name|valueToShowInHelp
operator|=
literal|"true"
argument_list|)
DECL|field|decode
name|boolean
name|decode
init|=
literal|true
decl_stmt|;
DECL|method|doExecute ()
specifier|protected
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
block|{
name|EndpointStatisticCommand
name|command
init|=
operator|new
name|EndpointStatisticCommand
argument_list|(
name|name
argument_list|,
name|decode
argument_list|,
name|filter
argument_list|)
decl_stmt|;
return|return
name|command
operator|.
name|execute
argument_list|(
name|camelController
argument_list|,
name|System
operator|.
name|out
argument_list|,
name|System
operator|.
name|err
argument_list|)
return|;
block|}
block|}
end_class

end_unit

