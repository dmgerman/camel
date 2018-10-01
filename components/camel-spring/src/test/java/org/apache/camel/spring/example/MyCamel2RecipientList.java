begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.example
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|example
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

begin_class
DECL|class|MyCamel2RecipientList
specifier|public
class|class
name|MyCamel2RecipientList
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
name|MyCamel2RecipientList
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Consume
argument_list|(
name|uri
operator|=
literal|"direct:foo"
argument_list|,
name|context
operator|=
literal|"camel-2"
argument_list|)
annotation|@
name|RecipientList
argument_list|(
name|context
operator|=
literal|"camel-2"
argument_list|)
DECL|method|doSomething (String body)
specifier|public
name|String
index|[]
name|doSomething
parameter_list|(
name|String
name|body
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Received body: "
operator|+
name|body
argument_list|)
expr_stmt|;
return|return
operator|new
name|String
index|[]
block|{
literal|"mock:foo"
block|,
literal|"mock:result"
block|}
return|;
block|}
block|}
end_class

end_unit

