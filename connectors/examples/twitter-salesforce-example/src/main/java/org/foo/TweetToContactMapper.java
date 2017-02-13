begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.foo
package|package
name|org
operator|.
name|foo
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
name|Exchange
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
name|Message
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
name|Processor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|foo
operator|.
name|salesforce
operator|.
name|upsert
operator|.
name|contact
operator|.
name|Contact
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

begin_comment
comment|/**  * To transform a tweet {@link Status} object into a salesforce {@link Contact} object.  */
end_comment

begin_class
annotation|@
name|Component
DECL|class|TweetToContactMapper
specifier|public
class|class
name|TweetToContactMapper
implements|implements
name|Processor
block|{
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
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
name|Contact
name|contact
init|=
operator|new
name|Contact
argument_list|()
decl_stmt|;
name|contact
operator|.
name|setLastName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|contact
operator|.
name|setTwitterScreenName__c
argument_list|(
name|screenName
argument_list|)
expr_stmt|;
name|in
operator|.
name|setBody
argument_list|(
name|contact
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

