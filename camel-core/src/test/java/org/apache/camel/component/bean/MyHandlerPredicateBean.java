begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bean
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
name|Handler
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
name|Predicate
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|MyHandlerPredicateBean
specifier|public
class|class
name|MyHandlerPredicateBean
implements|implements
name|Predicate
block|{
annotation|@
name|Override
DECL|method|matches (Exchange exchange)
specifier|public
name|boolean
name|matches
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Handler
DECL|method|doSomething (String body)
specifier|public
name|String
name|doSomething
parameter_list|(
name|String
name|body
parameter_list|)
block|{
return|return
literal|"Hi "
operator|+
name|body
return|;
block|}
DECL|method|doSomethingElse (String body)
specifier|public
name|String
name|doSomethingElse
parameter_list|(
name|String
name|body
parameter_list|)
block|{
return|return
literal|"Bye "
operator|+
name|body
return|;
block|}
block|}
end_class

end_unit

