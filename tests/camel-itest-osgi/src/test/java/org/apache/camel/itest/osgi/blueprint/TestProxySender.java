begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.osgi.blueprint
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|osgi
operator|.
name|blueprint
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
name|ExchangePattern
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
name|Pattern
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_interface
DECL|interface|TestProxySender
specifier|public
interface|interface
name|TestProxySender
block|{
DECL|method|hello (String name)
name|String
name|hello
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
annotation|@
name|Pattern
argument_list|(
name|value
operator|=
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
DECL|method|greeting (String message)
name|void
name|greeting
parameter_list|(
name|String
name|message
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

