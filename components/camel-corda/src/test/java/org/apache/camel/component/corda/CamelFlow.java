begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.corda
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|corda
package|;
end_package

begin_import
import|import
name|net
operator|.
name|corda
operator|.
name|core
operator|.
name|flows
operator|.
name|FlowLogic
import|;
end_import

begin_class
DECL|class|CamelFlow
specifier|public
class|class
name|CamelFlow
extends|extends
name|FlowLogic
argument_list|<
name|String
argument_list|>
block|{
DECL|field|in
specifier|private
name|String
name|in
decl_stmt|;
DECL|method|CamelFlow (String in)
specifier|public
name|CamelFlow
parameter_list|(
name|String
name|in
parameter_list|)
block|{
name|this
operator|.
name|in
operator|=
name|in
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|call ()
specifier|public
name|String
name|call
parameter_list|()
block|{
return|return
name|in
operator|+
literal|" world!"
return|;
block|}
block|}
end_class

end_unit

