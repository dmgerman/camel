begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.jira
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|jira
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
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|stereotype
operator|.
name|Component
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jira
operator|.
name|JiraConstants
operator|.
name|ISSUE_KEY
import|;
end_import

begin_class
annotation|@
name|Component
DECL|class|AttachFileRoute
specifier|public
class|class
name|AttachFileRoute
extends|extends
name|RouteBuilder
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|AttachFileRoute
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|">>>>>>>>>>>>>>>>>>>>> jira example - add attachment"
argument_list|)
expr_stmt|;
comment|// change the fields accordinly to your target jira server
name|from
argument_list|(
literal|"file:///A_valid_directory?fileName=my_file.png&noop=true&delay=50000"
argument_list|)
operator|.
name|setHeader
argument_list|(
name|ISSUE_KEY
argument_list|,
parameter_list|()
lambda|->
literal|"MYP-13"
argument_list|)
operator|.
name|log
argument_list|(
literal|"  JIRA attach: ${header.camelFileName} to MYP-13"
argument_list|)
operator|.
name|to
argument_list|(
literal|"jira://attach"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

