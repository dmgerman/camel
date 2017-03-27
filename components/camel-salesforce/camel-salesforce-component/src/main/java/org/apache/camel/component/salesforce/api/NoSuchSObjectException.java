begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.api
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|salesforce
operator|.
name|api
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|salesforce
operator|.
name|api
operator|.
name|dto
operator|.
name|RestError
import|;
end_import

begin_class
DECL|class|NoSuchSObjectException
specifier|public
specifier|final
class|class
name|NoSuchSObjectException
extends|extends
name|SalesforceException
block|{
DECL|method|NoSuchSObjectException (final List<RestError> restErrors)
specifier|public
name|NoSuchSObjectException
parameter_list|(
specifier|final
name|List
argument_list|<
name|RestError
argument_list|>
name|restErrors
parameter_list|)
block|{
name|super
argument_list|(
name|restErrors
argument_list|,
literal|404
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

