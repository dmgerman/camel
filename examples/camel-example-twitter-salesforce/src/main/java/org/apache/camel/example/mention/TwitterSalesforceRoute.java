begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.mention
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|mention
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
name|springframework
operator|.
name|stereotype
operator|.
name|Component
import|;
end_import

begin_import
import|import
name|twitter4j
operator|.
name|Status
import|;
end_import

begin_import
import|import
name|twitter4j
operator|.
name|User
import|;
end_import

begin_class
annotation|@
name|Component
DECL|class|TwitterSalesforceRoute
specifier|public
class|class
name|TwitterSalesforceRoute
extends|extends
name|RouteBuilder
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"twitter:timeline/mentions"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Mention ${body}"
argument_list|)
operator|.
name|process
argument_list|(
name|exchange
lambda|->
block|{
name|Status
name|status
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Status
operator|.
name|class
argument_list|)
decl_stmt|;
name|User
name|user
init|=
name|status
operator|.
name|getUser
argument_list|()
decl_stmt|;
name|String
name|name
init|=
name|user
operator|.
name|getName
argument_list|()
decl_stmt|;
name|String
name|screenName
init|=
name|user
operator|.
name|getScreenName
argument_list|()
decl_stmt|;
name|String
name|json
init|=
name|String
operator|.
name|format
argument_list|(
literal|"{\"name\": \"%s\", \"screenName\": \"%s\"}"
argument_list|,
name|name
argument_list|,
name|screenName
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|json
argument_list|)
expr_stmt|;
block|}
argument_list|)
operator|.
name|log
argument_list|(
literal|"JSon: ${body}"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

