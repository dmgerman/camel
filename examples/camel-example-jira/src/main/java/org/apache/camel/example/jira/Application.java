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
name|springframework
operator|.
name|boot
operator|.
name|SpringApplication
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|autoconfigure
operator|.
name|SpringBootApplication
import|;
end_import

begin_comment
comment|//CHECKSTYLE:OFF
end_comment

begin_class
annotation|@
name|SpringBootApplication
DECL|class|Application
specifier|public
class|class
name|Application
block|{
comment|/**      * A main method to start this application.      */
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
block|{
name|SpringApplication
operator|.
name|run
argument_list|(
name|Application
operator|.
name|class
argument_list|,
name|args
argument_list|)
expr_stmt|;
block|}
block|}
end_class

begin_comment
comment|//CHECKSTYLE:ON
end_comment

end_unit

