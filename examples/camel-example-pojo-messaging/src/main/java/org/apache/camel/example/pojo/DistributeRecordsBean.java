begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.pojo
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|pojo
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
name|Consume
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
name|RecipientList
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
name|language
operator|.
name|xpath
operator|.
name|XPath
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

begin_comment
comment|//START SNIPPET: ex
end_comment

begin_class
DECL|class|DistributeRecordsBean
specifier|public
class|class
name|DistributeRecordsBean
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
name|DistributeRecordsBean
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Consume
argument_list|(
literal|"activemq:personnel.records"
argument_list|)
annotation|@
name|RecipientList
DECL|method|route (@PathR) String city)
specifier|public
name|String
index|[]
name|route
parameter_list|(
annotation|@
name|XPath
argument_list|(
literal|"/person/city/text()"
argument_list|)
name|String
name|city
parameter_list|)
block|{
if|if
condition|(
name|city
operator|.
name|equals
argument_list|(
literal|"London"
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Person is from EMEA region"
argument_list|)
expr_stmt|;
return|return
operator|new
name|String
index|[]
block|{
literal|"file:target/messages/emea/hr_pickup"
block|,
literal|"file:target/messages/emea/finance_pickup"
block|}
return|;
block|}
else|else
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Person is from AMER region"
argument_list|)
expr_stmt|;
return|return
operator|new
name|String
index|[]
block|{
literal|"file:target/messages/amer/hr_pickup"
block|,
literal|"file:target/messages/amer/finance_pickup"
block|}
return|;
block|}
block|}
block|}
end_class

begin_comment
comment|//END SNIPPET: ex
end_comment

end_unit

